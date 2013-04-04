package com.textprocessing.assignment.ontology.executor.patterns;

import java.util.Iterator;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;

public class ExecutorLevelOne implements IExecutorLevels {
	private String queryString = "select * where { %s ?pre %s } ";
	
	@Override
	public boolean processRequest(RepositoryConnection con, String entityOne, String entityTwo) {
		queryString = String.format(queryString, entityOne, entityTwo);
		boolean status = false;
		try{
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, queryString);
			TupleQueryResult result = tupleQuery.evaluate();
			while(result.hasNext()){
				BindingSet bindingSet = result.next();
				Iterator<Binding> itr = bindingSet.iterator();
				while (itr.hasNext()) {
					status = true;
					Binding binding = (Binding) itr.next();
					System.out.println("Pattern One or Two |" + binding.getName() + " ---- " +binding.getValue());
				}
				System.out.println("***********");
			}
			
		}catch(Exception e){
			System.err.println(e);
		}
		return status;
	}
}
