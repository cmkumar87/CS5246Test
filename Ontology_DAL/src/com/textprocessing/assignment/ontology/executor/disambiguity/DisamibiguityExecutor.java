package com.textprocessing.assignment.ontology.executor.disambiguity;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;

public class DisamibiguityExecutor implements IDisambiguityExecutor{
	
	private String queryString = "SELECT DISTINCT ?obj " +
			" WHERE { " +
			" {   <http://dbpedia.org/resource/%s_(disambiguation)> dbpedia-owl:wikiPageDisambiguates ?obj   } " +
			" union " +
			" {   <http://dbpedia.org/resource/%s> dbpedia-owl:wikiPageDisambiguates ?obj   } " +
			" }";

	private String firstCharUpperCase(String word) {
		StringBuffer upperCaseStr = new StringBuffer();
		for (int i = 0; i < word.length(); i++) {
			if (i == 0) {
				upperCaseStr.append(Character.toUpperCase(word.charAt(0)));
			}else{
				upperCaseStr.append(word.charAt(i));
			}
		}
		return upperCaseStr.toString();
	}

	@Override
	public List<String> processRequest(RepositoryConnection con, String entity) {
		StringBuffer upperCaseStr = new StringBuffer();
		if(entity != null){
			String[] entities = entity.split(" ");
			for (int i = 0; i < entities.length; i++) {
				upperCaseStr.append(firstCharUpperCase(entities[i]));
				if(i != entities.length - 1){
					upperCaseStr.append("_");
				}
			}
		}
		//System.out.println(String.format("First char changed to upper case - %s", upperCaseStr));
		queryString = String.format(queryString, upperCaseStr, upperCaseStr);
		//System.out.println(String.format("Sparql Query - %s", queryString));
		List<String> objs = new ArrayList<String>();
		try{
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, queryString);
			TupleQueryResult result = tupleQuery.evaluate();
			while(result.hasNext()){
				BindingSet bindingSet = result.next();
				Iterator<Binding> itr = bindingSet.iterator();
				while (itr.hasNext()) {
					Binding binding = (Binding) itr.next();
					String value = binding.getValue().stringValue();
					objs.add(value);
				}
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return objs;
	}
}
