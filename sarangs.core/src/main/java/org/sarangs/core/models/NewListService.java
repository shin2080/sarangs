package org.sarangs.core.models;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.jcr.Session;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.Service;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.search.PredicateGroup;
import com.day.cq.search.Query;
import com.day.cq.search.QueryBuilder;
import com.day.cq.search.result.Hit;
import com.day.cq.search.result.SearchResult;

@Component
@Service
public class NewListService implements NewListInterface {
	
	Logger logger = LoggerFactory.getLogger(NewListService.class);

	private Session session;
	private String matches;

	// Inject a Sling ResourceResolverFactory
	@Reference
	private ResourceResolverFactory resolverFactory;

	@Reference
	private QueryBuilder builder;
	
	
	@SuppressWarnings("finally")
	@Override
	public List<String> getPaginationData(String pathtopage, String number,
			String selector, String type, String orderBy, String sortOrder) {

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
			map.put("type", type);
			map.put("p.limit", number);
			map.put("1_property", "@jcr:content/chkInMenu");
			map.put("1_property.operation", "not");
			map.put("2_property","자유게시판");
			map.put("2_property.operation", "not");
			map.put("orderby", "@jcr:content/"+orderBy); 
			map.put("orderby.sort", sortOrder);
			
			Query query = builder.createQuery(PredicateGroup.create(map),
					session);
			query.setStart(Integer.valueOf(number) * Integer.valueOf(selector));
			logger.info("Multiply:" + Integer.valueOf(number)
					* Integer.valueOf(selector));
			// query.setHitsPerPage(Integer.valueOf(number));

			SearchResult result = query.getResult();

			// paging metadata
			int hitsPerPage = result.getHits().size(); // 20 (set above) or

			// lower
			long totalMatches = result.getTotalMatches();
			this.matches = String.valueOf(totalMatches);
			logger.info("Matches" + totalMatches);
			// logger.info("Matches"+ totalMatches);
			long offset = result.getStartIndex();
			long numberOfPages = totalMatches / 20;

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
