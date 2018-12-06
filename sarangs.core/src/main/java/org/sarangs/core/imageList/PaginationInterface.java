package org.sarangs.core.imageList;

import java.util.List;

public interface PaginationInterface {
	
	List<String> getPaginationData(String path, String number);
	
	String getMatches();
}
