package com.textprocessing.assignment.ontology.executor.categories;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;

public class CategoryExecutor implements ICategoryExecutor{
	

	@Override
	public Map<String, String> processRequest(RepositoryConnection con, String entity) {
		String queryString = "SELECT * WHERE { " +
				" <http://dbpedia.org/resource/Category:%s> ?pre ?obj " +
				" }";
		
		queryString = String.format(queryString, entity);
		//System.out.println(String.format("Sparql Query - %s", queryString));
		Map<String, String> resultMap = new HashMap<String, String>();
		try{
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, queryString);
			TupleQueryResult result = tupleQuery.evaluate();
			while(result.hasNext()){
				BindingSet bindingSet = result.next();
				Iterator<Binding> itr = bindingSet.iterator();
				String keyStr = "";
				String valueStr = "";
				while (itr.hasNext()) {
					Binding binding = (Binding) itr.next();
					String key = binding.getName();
					String value = binding.getValue().stringValue();
					if(key.equalsIgnoreCase("pre")){
						keyStr = value;
					}else{
						valueStr = value;
					}
				}
				resultMap.put(keyStr, valueStr);
			}
		}catch(Exception e){
			System.out.println(e);
		}
		return resultMap;
	}

}
