package org.dhis2.integration;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;

public class PostDataValueSetQueryBuilder implements Processor {

	// dataElementIdScheme=CODE&categoryOptionComboIdScheme=CODE&importStrategy=CREATE_AND_UPDATE&dryRun=false"
	
    public void process(Exchange exchange) throws Exception {
        
    	String query = new StringBuilder().append("dataElementIdScheme=CODE")
    			.append("&categoryOptionComboIdScheme=CODE")
    			.append("&importStrategy=CREATE_AND_UPDATE")
    			.append("&dryRun=false")
    			.toString();
    	
    	exchange.getMessage().setHeader(Exchange.HTTP_QUERY, query);
    	exchange.getMessage().setHeader(Exchange.HTTP_METHOD, "POST");
    	exchange.getMessage().setBody(exchange.getIn().getBody());
        		 	
    }
}