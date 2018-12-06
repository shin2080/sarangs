package org.sarangs.core.servlets;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Properties;
import org.apache.felix.scr.annotations.Property;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import javax.servlet.http.HttpSession;


@Component(metatype = true, immediate = true, label = "CoderMagnet Servlet")
@Service
@Properties({

  @Property(name = "sling.servlet.methods", value = { "POST", "GET" }),
  @Property(name = "sling.servlet.paths", value = { "/bin/userService" }) })

public class SessionUser extends SlingAllMethodsServlet{

	private static final Logger LOG = LoggerFactory.getLogger(SessionUser.class);
	public Map<String, Object> UserInfo = new HashMap<String, Object>();
	
	@Override
	 protected void doGet(SlingHttpServletRequest request, SlingHttpServletResponse response) throws ServletException,
	   IOException {

		LOG.info("############# Start ###################");
		
		String username = "shin2180";
		  
		  HttpSession sess = request.getSession(true);
		  sess.setAttribute("USER", username);
		  
		  if (sess.getAttribute("USER") == null) {
		    // send 'no user/password match' message
			  LOG.info("############# No Session ###################");
		  } else {
		    // send 'successful login' screen
			  LOG.info("############# Yes Session " + sess.getAttribute("USER") + "###################");
		  }

	  // TODO: Code goes here
	 }
}
