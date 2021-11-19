package org.dhis2.integration;

import java.util.LinkedHashMap;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.dhis2.integration.model.ProgramIndicatorGroup;

public class GatherPIQueryString implements Processor {

	// static String PI = "jSEquR6y8Fx;Z7r9H312QOu;ZeSCOQzujFZ;oPnDPdsC9Ic;MyOZiGalvsI;rrtwzWk80lS;JrUWL08Ddnv;szVjaIR5Y0w;tRlRFI5clem;lXNyCenjXnr;NRdnhrWhU5x;Y4IC1OjAdf3;LichImvUvB6;rp8sBCRlPXK;pkN8Etipkfm;Y8wvwRgGcUS;mvlf2nQYvV9;WR5MkUOzYQx;KgPvXVYytRH;aRIhTkvSCDu;TKh36q9byZ2;eLkTBRFURvg;wHpLgrq0RhX;p37LM8QH1y5;jwKEkp66bbi;V14skZKSqwe;lmlfITJdHAw;QbplzyMJIHI;OwzSNSMtcgA;mweuNzij888;D3eZeSgzOhV;oSaoqqQ2MSt;PQhiQM94UYf;D6fmWXsg4pM;wGetVr8DIzX;kmDgwo2xyPG;kvIrnIoJAYR;SgOZ40SAyBF;oVD0Omx832C;FZwrly5hxUk;zUjUpQKcgc9;G3g4XfQKemC;GH7d54KUSXT;NRHSfORo0MH;AMBYD2z0chD;cQKLW4zJWY7;YsxMI4Dv7s7;cuS1pT1wWAn;Eb9Gd37H6aO;ubKa6QPpGPU;Vq2QZrz5t7e;GYEaZfyLm4T;yVo8pfW8opl;WXGJbirp6DL;zgjfcJiBlED;czdztQdLtvp;usOS0aEFimg;vlzsn2ahiPt;uxRQkvPX3O0;lv098ob7lcD;aSliDbjlhox;Nrit96cdaRl;TO5t4GynFmy;rCh3rLTcUpi;NfgFScH9jCr;J5dqOCSj047;gS5n5YA7pDZ;Rz1R6RuKUwJ;iQS9zGYpokL;uyJRjx7Kg62;rMDJ40j7Gdz;kQp74SERV3T;oYKZO6PawO4;ZkenguAQ0U6;vuG9jUefhvX;XvDSvGx9euM;FMqTHEACG9S;RQ3sKIsE5TJ;eJbF4wquzx4;S1QUkIjBtRh;QC5QxnAGu9D;tKlafE4iYmF;Ndssi9W1fix;SaXYEOgPoae;B9OaviyjiPC;azNkWe4xzP6;jvbwDQuzrcA;bxCmHHPPzDQ";

	@Override
	public void process(Exchange exchange) throws Exception {
		
		ProgramIndicatorGroup piGroup = exchange.getIn().getBody(ProgramIndicatorGroup.class);
		 
		// transform to a ';' separated list of PI uids
		exchange.getIn().setBody(piGroup.PIsAsString());
		
	}
	
}
