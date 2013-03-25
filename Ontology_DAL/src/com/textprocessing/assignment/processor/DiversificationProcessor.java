package com.textprocessing.assignment.processor;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openrdf.repository.RepositoryConnection;

import com.textprocessing.assignment.ontology.executor.OntologyConnector;
import com.textprocessing.assignment.ontology.executor.disambiguity.DisamibiguityExecutor;
import com.textprocessing.assignment.ontology.executor.disambiguity.IDisambiguityExecutor;
import com.textprocessing.assignment.ontology.lookup.DbpediaLookupExecutor;
import com.textprocessing.assignment.ontology.lookup.LookupContent;

public class DiversificationProcessor {

	private IDisambiguityExecutor disambiguityExecutor;
	private static final String SESAME_SERVER = "http://dbpedia.org/sparql";
	private OntologyConnector connector;
	private RepositoryConnection con;
	private DbpediaLookupExecutor lookupExecutor;
	
	public DiversificationProcessor() {
		connector = new OntologyConnector(SESAME_SERVER);
		con = connector.getConnection();
	}
	
	public void diversifySearchQuery(String searchQuery) throws Exception {
		String[] firstLevel = searchQuery.split(":");
		List<String> objs = getDisambiquityResults(firstLevel[firstLevel.length - 1]);
		if (objs != null && objs.size() > 0) {
			List<String> updatedObjs = removeUnderScore(objs);
			System.out.println(String.format("There are disambiguities for search query - %s | Below is the list \n%s",firstLevel[firstLevel.length - 1], updatedObjs.toString()));
		}else{
			System.out.println(String.format("There are NO disambiguities for search query - %s | Proceeding with lookups", firstLevel[firstLevel.length - 1]));
			String[] tokenizedQuery = tokenizeSearchQuery(searchQuery);
			for (int i = 0; i < tokenizedQuery.length; i++) {
				lookupExecutor = new DbpediaLookupExecutor();
				List<LookupContent> contentDetails = lookupExecutor.processHttpGet(tokenizedQuery[i]);
				boolean entityStatus = getClassCate(contentDetails, tokenizedQuery[i]);
				if (!entityStatus) {
					System.out.println(String.format("Search Token - %s is NOT an entity", tokenizedQuery[i]));
				}
			//	System.out.println(String.format("Query Token - %s | Content - %s", tokenizedQuery[i], contentDetails.toString()));
			}
		}
		System.out.println("\n************END*********************\n");
	}
	
	
	private boolean getClassCate(List<LookupContent> contentDetails, String token) {
		Iterator<LookupContent> lookupItr = contentDetails.iterator();
		while (lookupItr.hasNext()) {
			LookupContent lookupContent = (LookupContent) lookupItr.next();
			if (lookupContent.getClasses() != null && lookupContent.getClasses().size() > 0) {
				System.out.println(String.format("Search Token - %s is an entity | Class - %s ",token, lookupContent.getClasses().get(0)));
				return true;
			}
		}
		return false;
	}
	
	private String[] tokenizeSearchQuery(String searchQuery) {
		String[] firstLevel = searchQuery.split(":");
		String reqStr = firstLevel[firstLevel.length - 1];
		String[] reqStrList = reqStr.split(" ");
		return reqStrList;
	}
	
	private List<String>  removeUnderScore(List<String> objs) {
		List<String> updatedList = new ArrayList<String>();
		Iterator<String> objsItr = objs.iterator();
		while (objsItr.hasNext()) {
			String indiObj = (String) objsItr.next();
			String indiList[] = indiObj.split("/");
			indiObj = indiList[indiList.length - 1];
			indiObj = indiObj.replace("_", " ");
			updatedList.add(indiObj);
		}
		return updatedList;
	}
	
	private List<String> getDisambiquityResults(String searchQuery) {
		disambiguityExecutor = new DisamibiguityExecutor();
		List<String> objs = disambiguityExecutor.processRequest(con, searchQuery);
		return objs;
	}
	
}
