/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package org.sarangs.core.models;


import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import javax.annotation.PostConstruct;
import javax.inject.Inject;
import javax.inject.Named;
import javax.jcr.Session;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.Source;
import org.apache.sling.settings.SlingSettingsService;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.day.cq.commons.date.DateUtil;
import com.day.durbo.DurboInput.Node;

//import org.jsoup.Jsoup;
//import org.jsoup.nodes.Document;

@Model(adaptables = SlingHttpServletRequest.class)
public class HelloWorldModel {
	private final Logger logger = LoggerFactory.getLogger(getClass());

	@Inject
	SlingHttpServletRequest request;

	private String message;

	private String sel = null;

	private List<SimplePage> pageList = null;

	SimplePage page = null;

	private List<String> file;

	@Inject
	@Source("osgi-services")
	PaginationInterface service;

	@PostConstruct
	protected void init() {
		try {
			pageList = new ArrayList<SimplePage>();
			Session session = request.getResource().getResourceResolver()
					.adaptTo(Session.class);
			ValueMap node = request.getResource().getValueMap();
			String path = node.get("text", String.class);

			if (request != null) {
				List<String> selectors = Arrays.asList(request
						.getRequestPathInfo().getSelectors());
				if (selectors.size() > 0)
					this.sel = selectors.get(0);
				else
					this.sel = "0";

				this.file = service.getPaginationData(path, String.valueOf(12),
						this.sel);
				this.message = service.getMatches();
				int perPage = 20;
				int seq = (Integer.parseInt(this.message) + 1) - (perPage * Integer.parseInt(this.sel));
				
				
				for (String s : this.file) {
					page = new SimplePage();
					
					 seq = seq - 1;
					String title = session.getNode(s + "/jcr:content").getProperty("jcr:title").getString();
			
					String sub_title = strCut(title,"",31,0,true,true);
					
					String author = session.getNode(s + "/jcr:content").getProperty("jcr:createdBy").getString();
					
					String extraUrl = "";
					try {
						extraUrl = session.getNode(s+"/jcr:content").getProperty("sling:vanityPath").getString();
					}	catch (Exception e) {
						extraUrl = "";
					}
					
					String thum = "";
					try {
						thum = session.getNode(s+"/jcr:content").getProperty("jcr:description").getString();
						thum = thum + "/jcr:content/renditions/cq5dam.thumbnail.319.319.png";
					} catch (Exception e) {
						thum = s + ".thumb.800.480.png";
					}
					
					String date = "";
					try {
						date = session.getNode(s + "/jcr:content").getProperty("./orgDate").getString().substring(0, 10);
						
						if(date == null || date == "") {
							 date = session.getNode(s + "/jcr:content").getProperty("jcr:created").getString().substring(0, 10);
						}
					} catch (Exception e) {
						 date = session.getNode(s + "/jcr:content").getProperty("jcr:created").getString().substring(0, 10);
					}
					
					String content = "";
							
					try {
						content = session.getNode(s + "/jcr:content").getProperty("./pageTitle").getString();
						//Document doc = Jsoup.parse(content);
						//content = content.substring(0,117);
					
					} catch (Exception e) {
					    	content = "";
					}

//
//							try {
//						if(content == null || content.equals("") ) {
//			content = session.getNode(s + "/jcr:content/par/text").getProperty("./text").getString();
//			Document doc = Jsoup.parse(content);
//		content = doc.body().text().substring(0,117);

//	} catch (Exception e) {
//	    try {
//	    	content = session.getNode(s + "/jcr:content/par/contentfragment").getProperty("./text").getString();
//	    	Document doc = Jsoup.parse(content);
//			content = doc.body().text().substring(0,117);
//	    } catch (Exception a) {
//	    	content = "";
//	    }
//	}
					
					String seqOdd = "even";
					
					if(seq%2==0) { 
						 seqOdd = "odd";
					} else {
						 seqOdd = "even";
					}
					
					page.setPath(s);
					page.setTitle(title);
					page.setSubTitle(sub_title);
					page.setThum(thum);
					page.setAuthor(author);
					page.setDate(date);
					page.setContent(content);
					page.setSeq(seq);
					page.setSeqOdd(seqOdd);
					page.setextraUrl(extraUrl);
					pageList.add(page);

				}

			}
		} catch (Exception e) {
			logger.info("Exception:" + e.getMessage());
		}
	}

	public List<String> getFiles() {

		return this.file;
	}

	public String getMessage() {
		return this.message;
	}

	public List<SimplePage> getPage() {
		return this.pageList;
	}
	
	 public String strCut(String szText, String szKey, int nLength, int nPrev, boolean isNotag, boolean isAdddot){  // 문자열 자르기
		    
		    String r_val = szText;
		    int oF = 0, oL = 0, rF = 0, rL = 0; 
		    int nLengthPrev = 0;
		    Pattern p = Pattern.compile("<(/?)([^<>]*)?>", Pattern.CASE_INSENSITIVE);  // 태그제거 패턴
		   
		    if(isNotag) {r_val = p.matcher(r_val).replaceAll("");}  // 태그 제거
		    r_val = r_val.replaceAll("&amp;", "&");
		    r_val = r_val.replaceAll("(!/|\r|\n|&nbsp;)", "");  // 공백제거
		 
		    try {
		      byte[] bytes = r_val.getBytes("UTF-8");     // 바이트로 보관
		      if(szKey != null && !szKey.equals("")) {
		        nLengthPrev = (r_val.indexOf(szKey) == -1)? 0: r_val.indexOf(szKey);  // 일단 위치찾고
		        nLengthPrev = r_val.substring(0, nLengthPrev).getBytes("MS949").length;  // 위치까지길이를 byte로 다시 구한다
		        nLengthPrev = (nLengthPrev-nPrev >= 0)? nLengthPrev-nPrev:0;    // 좀 앞부분부터 가져오도록한다.
		      }
		    
		      // x부터 y길이만큼 잘라낸다. 한글안깨지게.
		      int j = 0;
		      if(nLengthPrev > 0) while(j < bytes.length) {
		        if((bytes[j] & 0x80) != 0) {
		          oF+=2; rF+=3; if(oF+2 > nLengthPrev) {break;} j+=3;
		        } else {if(oF+1 > nLengthPrev) {break;} ++oF; ++rF; ++j;}
		      }
		      
		      j = rF;
		      while(j < bytes.length) {
		        if((bytes[j] & 0x80) != 0) {
		          if(oL+2 > nLength) {break;} oL+=2; rL+=3; j+=3;
		        } else {if(oL+1 > nLength) {break;} ++oL; ++rL; ++j;}
		      }
		      r_val = new String(bytes, rF, rL, "UTF-8");  // charset 옵션
		      if(isAdddot && rF+rL+3 <= bytes.length) {r_val+="...";}  // ...을 붙일지말지 옵션 
		    } catch(UnsupportedEncodingException e){ e.printStackTrace(); }   
		    
		    return r_val;
		  }
	

}