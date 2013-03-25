package com.textprocessing.assignment.ontology.lookup;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.xpath.XPathExpressionException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;

public class DbpediaLookupExecutor {
	
	private XmlResponseHandler responseHandler;

	public List<LookupContent> processHttpGet(String keyWord) throws Exception {
		String url = "http://lookup.dbpedia.org/api/search.asmx/PrefixSearch?QueryClass=&MaxHits=5&QueryString=" + keyWord;
		DefaultHttpClient httpClient = new DefaultHttpClient();
		HttpGet httpGet = new HttpGet(url);
        responseHandler = new XmlResponseHandler();
		List<LookupContent> lookupContents = new ArrayList<LookupContent>();
		try{
			HttpResponse response = httpClient.execute(httpGet);
			//System.out.println(response.getStatusLine());
			HttpEntity entity = response.getEntity();
			InputStream stream = entity.getContent();
			 BufferedReader bReader = new BufferedReader(new InputStreamReader(stream));
             StringBuffer sbfFileContents = new StringBuffer();
             String line = null;
            
             while( (line = bReader.readLine()) != null){
            	 if(line.startsWith("<ArrayOfResult")){
            		 sbfFileContents.append("<ArrayOfResult>");
            	 }else
                     sbfFileContents.append(line);
             }
            
             String strContent = sbfFileContents.toString();
            // System.out.println(strContent);
             responseHandler.parseXmlContent(strContent.trim());
             List<String> lablesList = responseHandler.getAllValuesFromXmlBasedOnPattern("//ArrayOfResult/Result/Label/text()");
             lookupContents = getOtherDetails(lablesList);
             //System.out.println(lookupContents.toString());
		}catch(Exception e) {
			System.out.println(e);
			if(lookupContents.size() > 0){
				return lookupContents;
			}else{
				throw e;
			}
		}
		  return lookupContents;
	}
	
	private List<LookupContent> getOtherDetails(List<String> labels) throws XPathExpressionException {
		String patterns = "//ArrayOfResult/Result[Label='%s']/%s/text()";
		Iterator<String> labelsItr = labels.iterator();
		List<LookupContent> lookupContents = new ArrayList<LookupContent>();
		try{
		while (labelsItr.hasNext()) {
			String label = (String) labelsItr.next();
			if(label.contains("'")){
				label = label.replace("'", "\\'");
			}
			String uriPattern = String.format(patterns, label, "URI");
			String uriValue = responseHandler.getValuesFromXmlBasedOnPattern(uriPattern);
			String classPattern = String.format(patterns, label, "Classes/Class/URI");
			List<String> classes = responseHandler.getAllValuesFromXmlBasedOnPattern(classPattern);
			String catPattern = String.format(patterns, label, "Categories/Category/URI");
			List<String> categories = responseHandler.getAllValuesFromXmlBasedOnPattern(catPattern);
			String refCountPattern =  String.format(patterns, label, "Refcount");
			String count = responseHandler.getValuesFromXmlBasedOnPattern(refCountPattern);
			lookupContents.add(new LookupContent(label, uriValue, classes, categories, Integer.parseInt(count)));
		}
		}catch(XPathExpressionException e){
			if(lookupContents.size() > 0){
				return lookupContents;
			}else{
				throw e;
			}
		}
		return lookupContents;
	}
}
