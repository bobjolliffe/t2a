package org.dhis2.integration.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ProgramIndicatorGroup {

	static class ProgramIndicator {
		private String id;
		
		public String getId() {
			return id;
		}
		public void setId(String id) {
			this.id = id;
		}		
	}
	
	private List<ProgramIndicator> programIndicators;

	
	public List<ProgramIndicator> getProgramIndicators() {
		return programIndicators;
	}

	public void setProgramIndicators(List<ProgramIndicator> programIndicators) {
		this.programIndicators = programIndicators;
	}

	public String PIsAsString()
	{
		// 778ttg7t78;gygyugg56;guigh879yyyfg;89uy98
		String result = "";
		for (ProgramIndicator pi : programIndicators) 
		{
			result += pi.getId();
			result += ";";
		}
		result = result.substring(0, result.length() - 1);
		return result;
			
	}
}
