package org.dhis2.integration;

import java.util.Base64;
import java.util.concurrent.ExecutorService;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.builder.ThreadPoolBuilder;
import org.apache.camel.component.properties.PropertiesComponent;
import org.apache.camel.jsonpath.JsonPathExpression;
import org.dhis2.integration.model.ProgramIndicatorGroup;
import org.springframework.stereotype.Component;

/**
 * Pulling program indicators one at a time as datavaluesets (beware if your pool size is
 * too big it breaks the database :-( )
 */
@Component
public class T2ARouter extends RouteBuilder {

	@Override
	public void configure() throws Exception {
	
		PropertiesComponent properties = (PropertiesComponent) getContext().getPropertiesComponent();
		
		String t2aAuthHeader = getAuthHeader("dhis2.t2a");
		//String PIGroup = properties.resolveProperty("dhis2.t2a.PIGroup").get();
		//int poolSize = Integer.parseInt(properties.resolveProperty("dhis2.t2a.pool").get());
		
		ThreadPoolBuilder builder = new ThreadPoolBuilder(getContext());
		ExecutorService programIndicatorPool = builder.poolSize(10).maxPoolSize(40).build();

		/*
		 * Strategy 1: split and pull one PI at a time
		 * 
		 */
		/*from("timer:analytics?repeatCount=1&period=3000000")
			.routeId("t2a")
			.setHeader("Authorization", constant(t2aAuthHeader))
			.to("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/programIndicatorGroups/" + PIGroup)
			.split().jsonpath("$.programIndicators[*].id").executorService(programIndicatorPool)
				.log("Procesing programIndicator: ${body}")
				.process(new DataValueSetQueryBuilder())
				.setHeader("Authorization", constant(t2aAuthHeader))
				.toD("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/analytics/dataValueSet.json")
				// .log("${body}")
				.process(new PostDataValueSetQueryBuilder()).setHeader("Authorization", constant(t2aAuthHeader))
				.setHeader("Content-Type", constant("application/json")).log("Posting datavalueset")
				//.toD("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/dataValueSets").log("${body}")
				.end()
			.log("T2A done");
*/
		/*
		 * Strategy 2: All-in-one
		 * 
		 */
		 
		 from("timer:analytics?repeatCount=1&period=3000000") 
		   .routeId("t2a")
		   .log("Fetching progaram indicators from group")
		   .setHeader("Authorization", constant(t2aAuthHeader) ) 
		   .to("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/programIndicatorGroups/{{dhis2.t2a.PIGroup}}") 
		   .unmarshal().json(ProgramIndicatorGroup.class)
		   .process(new GatherPIQueryString())
		   .process(new DataValueSetQueryBuilder()) 
		   .log("Get the datavalueset")
		   .setHeader("Authorization", constant(t2aAuthHeader) ) 
		   .toD("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/analytics/dataValueSet.json")
		   .process(new PostDataValueSetQueryBuilder()) 
		   .setHeader("Authorization",constant(t2aAuthHeader) ) 
		   .setHeader("Content-Type", constant("application/json")) 
		   .log("Posting datavalueset")
		   .toD("https://{{dhis2.t2a.host}}/{{dhis2.t2a.path}}/api/dataValueSets")
		   .log("${body}") 
		   .log("T2A done");
	}
	
	private String getAuthHeader(String prefix) {
		PropertiesComponent properties = (PropertiesComponent) getContext().getPropertiesComponent();
		
		String user = properties.resolveProperty(prefix + ".user").get();
		String pwd = properties.resolveProperty(prefix + ".pwd").get();

		return "Basic " + Base64.getEncoder().encodeToString((user + ":" + pwd).getBytes());
	}
}
