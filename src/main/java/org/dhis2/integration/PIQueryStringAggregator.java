package org.dhis2.integration;

import org.apache.camel.AggregationStrategy;
import org.apache.camel.Exchange;

public class PIQueryStringAggregator implements AggregationStrategy {

	@Override
	public Exchange aggregate(Exchange oldExchange, Exchange newExchange) {
		if (oldExchange == null) {
			return newExchange; 
		}
		
		String body = newExchange.getIn().getBody(String.class);
		String existing = oldExchange.getIn().getBody(String.class);
		oldExchange.getIn().setBody(existing + ";" + body);
		
		return oldExchange;
	}
	
}
