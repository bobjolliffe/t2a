package org.dhis2.integration;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class DataValueSetQueryBuilder implements Processor {

    public void process(Exchange exchange) throws Exception {
        
    	String PI = exchange.getIn().getBody(String.class);
    	
    	String query = new StringBuilder().append("dimension=dx:" + PI)
    			.append("&dimension=ou:LEVEL-4")
    			.append("&dimension=pe:LAST_4_QUARTERS")
    			.append("&outputIdScheme=ATTRIBUTE:vudyDP7jUy5")
    			.toString();
    	
    	exchange.getOut().setHeader(Exchange.HTTP_QUERY, query);
        		
    }
	
}
