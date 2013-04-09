package com.textprocessing.assignment.processor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openrdf.repository.RepositoryConnection;

import com.textprocessing.assignment.ontology.executor.OntologyConnector;
import com.textprocessing.assignment.ontology.executor.categories.CategoryExecutor;
import com.textprocessing.assignment.ontology.executor.categories.ICategoryExecutor;
import com.textprocessing.assignment.ontology.executor.disambiguity.DisamibiguityExecutor;
import com.textprocessing.assignment.ontology.executor.disambiguity.IDisambiguityExecutor;
import com.textprocessing.assignment.ontology.lookup.DbpediaLookupExecutor;
import com.textprocessing.assignment.ontology.lookup.LookupContent;
import com.textprocessing.assignment.query.cleaner.StringProcessor;

public class DiversificationProcessor {

	private IDisambiguityExecutor disambiguityExecutor;
	private static final String SESAME_SERVER = "http://dbpedia.org/sparql";
	private OntologyConnector connector;
	private RepositoryConnection con;
	private DbpediaLookupExecutor lookupExecutor;
	private StringProcessor strProcessor;
	
	public DiversificationProcessor() {
		connector = new OntologyConnector(SESAME_SERVER);
		con = connector.getConnection();
		strProcessor = new StringProcessor();
	}
	
	private List<String> processSearchForCategory(String searchQuery) {
		List<String> objs = new ArrayList<String>();
		ICategoryExecutor categoryExecutor = new CategoryExecutor();
		searchQuery = upperCaseUnderScore(searchQuery);
		Map<String, String> levelOneresultMap = categoryExecutor.processRequest(con, searchQuery);
		if (levelOneresultMap == null || levelOneresultMap.size() == 0 ) {
			System.out.println("No category at first level of query | Proceeding with bigrams");
			String reSearchQuery = searchQuery.replace("_", " ");
			List<String> tokens = removePerpositions(reSearchQuery);
			List<String> cleanedToken = cleanUpTokens(tokens);
			Map<String, String> bigramTokensMap = frameBigramQueries(cleanedToken);
			Map<String, String> upperCasedBigrams = upperCaseUnderScoreAll(bigramTokensMap);
			//reformulatedQueries(upperCasedBigrams);
			objs = reExecuteBigramQueries(upperCasedBigrams);
		}else{
			objs = checkSkosObjs(levelOneresultMap);
		}
		if(objs != null && objs.size() > 0){
			System.out.println("Categories reformulation result for query - " + searchQuery + "\n" + objs.toString());
		}
		return objs;
	}
	
	private List<String> reExecuteBigramQueries(Map<String, String> upperBigramTokensMap) {
		ICategoryExecutor categoryExecutor = new CategoryExecutor();
		Set<String> keySet = upperBigramTokensMap.keySet();
		Iterator<String> keysItr = keySet.iterator();
		List<String> objs = new ArrayList<String>();
		while (keysItr.hasNext()) {
			Map<String, String> levelTworesultMap = new HashMap<String, String>();
			String otherToken = (String) keysItr.next();
			String biGramContent = upperBigramTokensMap.get(otherToken).trim();
			levelTworesultMap = categoryExecutor.processRequest(con, biGramContent);
			if (levelTworesultMap != null && levelTworesultMap.size() > 0 ) {
			//	System.out.println("categories found at secoud level of query | " + otherToken +" | " + biGramContent);
				List<String> unStructuredObjs = checkSkosObjs(levelTworesultMap);
				for (int i = 0; i < unStructuredObjs.size(); i++) {
					objs.add(String.format(otherToken.replace("_", " ").trim(), unStructuredObjs.get(i)));
				}
			}else{
			//	System.out.println("No categories fount at secoud level of query | " + otherToken +" | " + biGramContent);
			}
		}
		return objs;
	}
	
	
	private List<String> checkSkosObjs(Map<String, String> levelTwoResultMap) {
		Set<String> keySet = levelTwoResultMap.keySet();
		Iterator<String> keys = keySet.iterator();
		List<String> objs = new ArrayList<String>();
		while (keys.hasNext()) {
			String pre = (String) keys.next();
			if(pre.contains("http://www.w3.org/2004/02/skos/core#")){
				String removePrefix = removeUnderscoreIndi(levelTwoResultMap.get(pre));
				removePrefix = removePrefix.replace("Category:", "");
				removePrefix = removePrefix.replace("Category", "");
				removePrefix = removePrefix.replace("_", " ");
				removePrefix = removePrefix.replace("-", " ");
				objs.add(removePrefix);
			}
		}
		return objs;
	}
	
	private Map<String, String> upperCaseUnderScoreAll(Map<String, String> bigramTokensMap) {
		Set<String> keySet = bigramTokensMap.keySet();
		Iterator<String> keysItr = keySet.iterator();
		Map<String, String> upperBigramTokensMap = new HashMap<String, String>();
		while (keysItr.hasNext()) {
			String otherToken = (String) keysItr.next();
			String biGramContent = bigramTokensMap.get(otherToken).trim();
			String newBiGram = upperCaseUnderScore(biGramContent);
			upperBigramTokensMap.put(otherToken, newBiGram);
		}
		return upperBigramTokensMap;
	}
	
	private void reformulatedQueries(Map<String, String> bigramTokensMap) {
		Set<String> keySet = bigramTokensMap.keySet();
		Iterator<String> keysItr = keySet.iterator();
		while (keysItr.hasNext()) {
			String otherToken = (String) keysItr.next();
			String biGramContent = bigramTokensMap.get(otherToken);
			System.out.println(String.format("%s", biGramContent));
		}
	}
	
	private List<String> removePerpositions(String searchQuery) {
		List<String> tokens = strProcessor.tokenize(searchQuery);
		return tokens;
	}
	
	private List<String> cleanUpTokens(List<String> tokens) {
		List<String> cleanedTokens = new ArrayList<String>();
		for (int i = 0; i < tokens.size(); i++) {
			if(tokens.get(i).contains("_")){
				cleanedTokens.add(tokens.get(i).substring(0, tokens.get(i).indexOf("_")));
			}else{
				cleanedTokens.add(tokens.get(i));
			}
		}
		return cleanedTokens;
	}
	
	private Map<String, String> frameBigramQueries(List<String> tokens) {
		Map<String, String> bigramTokensMap = new HashMap<String, String>();
		for (int i = 0; i < tokens.size(); i++) {
			if (i < tokens.size()) {
				for (int j = i; j < tokens.size(); j++) {
					if(j+1 < tokens.size()){
						String bigramToken = tokens.get(i) + "_" + tokens.get(j+1);
						boolean bgStatus = false;
						StringBuffer otherTokens = new StringBuffer();
						for (int k = 0; k < tokens.size(); k++) {
							if(k == i || k == j+1){
								if(!bgStatus){
									bgStatus = true;
									otherTokens.append("%s" + "_");
								}
							}else{
								otherTokens.append(tokens.get(k) + "_");
							}
						}
						bigramTokensMap.put(otherTokens.toString() ,bigramToken);
					}
				}
			}
		}
		return bigramTokensMap;
	}
	
	private String upperCaseUnderScore(String searchQuery) {
		StringBuffer changedQuery = new StringBuffer();
		searchQuery = searchQuery.replace(" ", "_");
		for (int i = 0; i < searchQuery.length(); i++) {
			if (i == 0) {
				changedQuery.append(Character.toUpperCase(searchQuery.charAt(0)));
			}else{
				changedQuery.append(searchQuery.charAt(i));
			}
		}
		return changedQuery.toString();
	}
	
	public List<String> diversifySearchQuery(String searchQuery) throws Exception {
		String[] firstLevel = searchQuery.split(":");
		List<String> objs = getDisambiquityResults(firstLevel[firstLevel.length - 1]);
		if (objs != null && objs.size() > 0) {
			List<String> updatedObjs = removeUnderScore(objs);
			System.out.println(String.format("Disambiguities for search query - %s | Below is the list \n%s",firstLevel[firstLevel.length - 1], updatedObjs.toString()));
		}if(objs == null || objs.size() == 0){
			System.out.println(String.format("NO disambiguities for search query - %s | Proceeding with category findings", firstLevel[firstLevel.length - 1]));
			objs = processSearchForCategory(firstLevel[firstLevel.length - 1]);
		}if(objs == null || objs.size() == 0){
			System.out.println(String.format("NO categories for search query and bigrams of searc query - %s | Proceeding with lookups", firstLevel[firstLevel.length - 1]));
			//String[] tokenizedQuery = tokenizeSearchQuery(searchQuery);
			lookupExecutor = new DbpediaLookupExecutor();
			List<LookupContent> contentDetailsAll = lookupExecutor.processHttpGet(searchQuery);
			List<String> indiObjsAll = getClassCateForLookup(contentDetailsAll, searchQuery, "%s");
			if(indiObjsAll == null || indiObjsAll.size() == 0){
				System.out.println(String.format("NO categories for whole search query lookup - %s | Proceeding with lookups bigrams", firstLevel[firstLevel.length - 1]));
				List<String> tokens = removePerpositions(searchQuery);
				List<String> cleanedToken = cleanUpTokens(tokens);
				Map<String, String> bigramTokensMap = frameBigramQueries(cleanedToken);
				Map<String, String> upperCasedBigrams = upperCaseUnderScoreAll(bigramTokensMap);
				Set<String> keySet = upperCasedBigrams.keySet();
				Iterator<String> keySetItr = keySet.iterator();
				while(keySetItr.hasNext()){
					String remainPart = keySetItr.next();
					String bigram = upperCasedBigrams.get(remainPart);
					List<LookupContent> contentDetails = lookupExecutor.processHttpGet(bigram);
					List<String> indiObjs = getClassCateForLookup(contentDetails, bigram, remainPart);
					if (indiObjs == null || indiObjs.size() == 0) {
						System.out.println(String.format("Search Token - %s is NOT an entity", bigram));
					}else{
						objs.addAll(indiObjs);
					}
					//	System.out.println(String.format("Query Token - %s | Content - %s", tokenizedQuery[i], contentDetails.toString()));
				}
			}
			System.out.println("Lookup reformulation result for search query - " + firstLevel[firstLevel.length - 1] + "\n" +objs.toString());
		}
		return objs;
	}
	
	
	private List<String> getClassCate(List<LookupContent> contentDetails, String token, String searchQuery) {
		Iterator<LookupContent> lookupItr = contentDetails.iterator();
		List<String> objs = new ArrayList<String>();
		
		while (lookupItr.hasNext()) {
			LookupContent lookupContent = (LookupContent) lookupItr.next();
			if(lookupContent.getClasses() != null && lookupContent.getClasses().size() > 0) {
				if(lookupContent.getClasses().contains("http://dbpedia.org/ontology/Place") || lookupContent.getClasses().contains("http://dbpedia.org/ontology/Person")){
				if (lookupContent.getCategories() != null && lookupContent.getCategories().size() > 0) {
					for (int i = 0; i < lookupContent.getCategories().size(); i++) {
						String category = lookupContent.getCategories().get(i).replace("http://dbpedia.org/resource/Category:", "");

						objs.add(searchQuery.replace(token, category).replace("_", " ").replace("-", " "));
					}
					//System.out.println(String.format("Search Token - %s is an entity | categories - %s ",token, lookupContent.getCategories().get(0)));
					//return true;
				}
			}
		}
		}
		//System.out.println(String.format("Search Token - %s is an entity | reformulated - %s ",token, objs));
		return objs;
	}
	
	
	private List<String> getClassCateForLookup(List<LookupContent> contentDetails, String token, String searchQuery) {
		Iterator<LookupContent> lookupItr = contentDetails.iterator();
		List<String> objs = new ArrayList<String>();
		
		while (lookupItr.hasNext()) {
			LookupContent lookupContent = (LookupContent) lookupItr.next();
			if(lookupContent.getClasses() != null && lookupContent.getClasses().size() > 0) {
				if(lookupContent.getClasses().contains("http://dbpedia.org/ontology/Place") || lookupContent.getClasses().contains("http://dbpedia.org/ontology/Person")){
				if (lookupContent.getCategories() != null && lookupContent.getCategories().size() > 0) {
					for (int i = 0; i < lookupContent.getCategories().size(); i++) {
						String category = lookupContent.getCategories().get(i).replace("http://dbpedia.org/resource/Category:", "");

						objs.add(String.format(searchQuery, category).replace("_", " ").replace("-", " "));
					}
					//System.out.println(String.format("Search Token - %s is an entity | categories - %s ",token, lookupContent.getCategories().get(0)));
					//return true;
				}
			}
		}
		}
		//System.out.println(String.format("Search Token - %s is an entity | reformulated - %s ",token, objs));
		return objs;
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
			indiObj = removeUnderscoreIndi(indiObj);
			updatedList.add(indiObj);
		}
		return updatedList;
	}
	
	private String removeUnderscoreIndi(String indiObj) {
		String indiList[] = indiObj.split("/");
		indiObj = indiList[indiList.length - 1];
		indiObj = indiObj.replace("_", " ");
		return indiObj;
	}
	
	private String convertUpperIfSingleToken(String searchQuery) {

		StringBuffer strBuf = new StringBuffer();

		for (int i = 0; i < searchQuery.length(); i++) {
			strBuf.append(Character.toUpperCase(searchQuery.charAt(i)));
		}
		return strBuf.toString();

	}
	
	private List<String> getDisambiquityResults(String searchQuery) {
		disambiguityExecutor = new DisamibiguityExecutor();
		String[] tokens = searchQuery.split(" ");
		List<String> objs = new ArrayList<String>();
		if(tokens.length == 1){
			String refacSearchQuery = convertUpperIfSingleToken(searchQuery);
			objs = disambiguityExecutor.processRequest(con, refacSearchQuery);
			if(objs == null || objs.size() == 0){
				System.out.println("Disambiquity with upper case - failure | " + refacSearchQuery);
				objs = disambiguityExecutor.processRequest(con, searchQuery);
			}else{
				System.out.println("Disambiquity with upper case - Success | " + refacSearchQuery);
			}
		}else{
			objs = disambiguityExecutor.processRequest(con, searchQuery);
		}
		return objs;
	}
	
}
