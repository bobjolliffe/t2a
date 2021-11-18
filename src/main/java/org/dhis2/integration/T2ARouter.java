package org.dhis2.integration;

import java.util.Base64;
import java.util.concurrent.ExecutorService;

import org.apache.camel.CamelContext;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.springframework.stereotype.Component;

/**
 * Pulling program indicators one at a time as datavaluesets (pool size of 10
 * crashes the system :-( )
 */
@Component
public class T2ARouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		String t2aAuthHeader = getAuthHeader("dhis2.t2a");
		String PIGroup = "ShLDZIE0Cwa";

		ThreadPoolBuilder builder = new ThreadPoolBuilder(getContext());
		ExecutorService programIndicatorPool = builder.poolSize(4).build();

		/*
		 * Strategy 1: split and pull one PI at a time
		 * 
		 */
		from("timer:analytics?repeatCount=1&period=3000000")
			.routeId("t2a")
			.setHeader("Authorization", constant(t2aAuthHeader))
			.to("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/programIndicatorGroups/" + PIGroup)
			.split().jsonpath("$.programIndicators[*].id").executorService(programIndicatorPool)
				.log("Procesing programIndicator: ${body}")
				.process(new DataValueSetQueryBuilder())
				.setHeader("Authorization", constant(t2aAuthHeader))
				.toD("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/analytics/dataValueSet.json")
				.log("${body}")
				.process(new PostDataValueSetQueryBuilder()).setHeader("Authorization", constant(t2aAuthHeader))
				.setHeader("Content-Type", constant("application/json")).log("Posting datavalueset")
				.toD("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/dataValueSets").log("${body}")
				.end()
			.log("T2A done");

		/*
		 * Strategy 2: All-in-one
		 * 
		 */
		/*
		 * from("timer:analytics?repeatCount=1&period=3000000") .routeId("t2a")
		 * .setHeader("Authorization", constant(t2aAuthHeader) ) .to(
		 * "https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/programIndicatorGroups/" +
		 * PIGroup) .split(new JsonPathExpression("$.programIndicators[*].id"),new
		 * PIQueryStringAggregator()) .log("${body}") .end()
		 * .log("Procesing programIndicators: ${body}") .process(new
		 * DataValueSetQueryBuilder()) .setHeader("Authorization",
		 * constant(t2aAuthHeader) ) .toD(
		 * "https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/analytics/dataValueSet.json")
		 * .process(new PostDataValueSetQueryBuilder()) .setHeader("Authorization",
		 * constant(t2aAuthHeader) ) .setHeader("Content-Type",
		 * constant("application/json")) .log("Posting datavalueset")
		 * .toD("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/dataValueSets")
		 * .log("${body}") .log("T2A done");
		 */
	}

	private String getAuthHeader(String prefix) {
		CamelContext context = getContext();
		String user = context.getPropertiesComponent().resolveProperty(prefix + ".user").get();
		String pwd = context.getPropertiesComponent().resolveProperty(prefix + ".pwd").get();

		return "Basic " + Base64.getEncoder().encodeToString((user + ":" + pwd).getBytes());
	}
}
