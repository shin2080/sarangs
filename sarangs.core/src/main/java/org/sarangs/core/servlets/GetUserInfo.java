package org.sarangs.core.servlets;

import javax.servlet.http.HttpSession;
import org.apache.sling.api.SlingHttpServletRequest;

public class GetUserInfo {
	public String userName = "";
	private SlingHttpServletRequest request;
	
	public void activate() throws Exception {	
		//request = null;
		HttpSession sess = request.getSession(true);
		userName = (String)sess.getAttribute("USER");
	}
}
