package org.sarangs.core.models;

import java.util.List;

public interface NewListInterface {

List<String> getPaginationData(String path, String numberOfResults, String offset, String type, 
		String orderBy, String sortOrder);
	
	String getMatches();
}
