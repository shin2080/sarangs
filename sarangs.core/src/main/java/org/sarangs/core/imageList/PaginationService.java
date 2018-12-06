package org.sarangs.core.imageList;

import java.util.List;
import java.util.ArrayList;

import javax.jcr.Session;
import java.util.HashMap;
import java.util.Map;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Service;

//Sling Imports
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.resource.ResourceResolver;


//QUeryBuilder APIs
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.Query;
import com.day.cq.search.PredicateGroup;
import com.day.cq.search.result.SearchResult;
import com.day.cq.search.result.Hit;

@Component
@Service
public class PaginationService implements PaginationInterface {

	Logger logger = LoggerFactory.getLogger(PaginationService.class);

	private Session session;
	private String matches;

	// Inject a Sling ResourceResolverFactory
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private QueryBuilder builder;
	
	
	@SuppressWarnings("finally")
	@Override
	public List<String> getPaginationData(String pathtopage, String number) {

		List<String> hyperLinks = new ArrayList<String>();

		
		try {

			logger.info("Getting Ready to create SESSION!!");

			// Invoke the adaptTo method to create a Session
		//	List<Customer> custList = new ArrayList<Customer>();
			Map<String, Object> param = new HashMap<String, Object>();
			param.put(ResourceResolverFactory.SUBSERVICE, "datawrite");
			ResourceResolver resolver = null;

			resolver = resolverFactory.getServiceResourceResolver(param);
			session = resolver.adaptTo(Session.class);
			    
			//ResourceResolver resourceResolver = resolverFactory
			//		.getAdministrativeResourceResolver(null);
			//session = resourceResolver.adaptTo(Session.class);

			// create query description as hash map
			Map<String, String> map = new HashMap<String, String>();
			map.put("path", pathtopage);
			map.put("type", "dam:Asset");
			map.put("p.limit", number);
			
			map.put("property","@jcr:content/metadata/cq:tags");
			map.put("property.operation", "equals");
			map.put("property.1_value", "sarangs:web");
			map.put("orderby", "@jcr:content/cq:name"); 
			map.put("orderby.sort", "asc"); 
			
			Query query = builder.createQuery(PredicateGroup.create(map),
					session);

			// query.setHitsPerPage(Integer.valueOf(number));

			SearchResult result = query.getResult();

			// lower
			long totalMatches = result.getTotalMatches();
			this.matches = String.valueOf(totalMatches);
			logger.info("Matches" + totalMatches);
			// logger.info("Matches"+ totalMatches);
	
			// iterating over the results
			for (Hit hit : result.getHits()) {
				String path = hit.getPath();
				// Create a result element
				hyperLinks.add(path);
				

			}

			// close the session

		} catch (Exception e) {
			this.logger.info("Something went wrong with session .. {}", e);
		} finally {
			session.logout();
			return hyperLinks;
		}

	}

	@Override
	public String getMatches() {
		logger.info("Returning matches:" + this.matches);
		return this.matches;
	}

}