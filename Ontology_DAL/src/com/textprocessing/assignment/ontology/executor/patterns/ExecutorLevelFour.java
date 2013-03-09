package com.textprocessing.assignment.ontology.executor.patterns;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.RepositoryConnection;

public class ExecutorLevelFour implements IExecutorLevels{
	
	private static final String OBJ_ONE = "obj1";
	private static final String OBJ_TWO = "obj2";
	List<String> commonObjs = new ArrayList<String>();
	private String queryString = "select * where { " +
			" { %s <http://purl.org/dc/terms/subject> ?obj1 } union " +
			" { %s <http://purl.org/dc/terms/subject> ?obj2 } }" ;
	private String singleQueryStr = "select * where { " +
			" %s <http://purl.org/dc/terms/subject> %s }";
	 

	@Override
	public boolean processRequest(RepositoryConnection con, String entityOne, String entityTwo) {
		queryString = String.format(queryString, entityOne, entityTwo);
		boolean status = false;
		List<String> objOne = new ArrayList<String>();
		List<String> objTwo = new ArrayList<String>();
		try{
			TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, queryString);
			TupleQueryResult result = tupleQuery.evaluate();
			while(result.hasNext()){
				BindingSet bindingSet = result.next();
				Iterator<Binding> itr = bindingSet.iterator();
				while (itr.hasNext()) {
					Binding binding = (Binding) itr.next();
				//	System.out.println(binding.getName() + " ---- " +binding.getValue());
					String name = binding.getName();
					String value = binding.getValue().stringValue();
					if (name.equalsIgnoreCase(OBJ_ONE)) {
						objOne.add(value);
					}else{
						objTwo.add(value);
					}
				}
			//	System.out.println("***********");
			}
			checkCatForObjOneListEntityTwo(con, objOne, entityTwo);
			checkCatForObjOneListEntityTwo(con, objTwo, entityOne);
			if (commonObjs.size() > 0) {
				status = true;
			}
		}catch(Exception e){
			System.err.println(e);
		}
		return status;
	}
	
	
	private void checkCatForObjOneListEntityTwo(RepositoryConnection con, List<String> objList, String entity) {
		Iterator<String> objListItr = objList.iterator();
		while (objListItr.hasNext()) {
			String objOne = (String) objListItr.next();
			objOne = String.format("<%s>", objOne);
			singleQueryStr = String.format(singleQueryStr, objOne, entity);
			try{
				TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, singleQueryStr);
				TupleQueryResult result = tupleQuery.evaluate();
				while(result.hasNext()){
					BindingSet bindingSet = result.next();
					Iterator<Binding> itr = bindingSet.iterator();
					while (itr.hasNext()) {
						Binding binding = (Binding) itr.next();
					//	System.out.println(binding.getName() + " ---- " +binding.getValue());
						String name = binding.getName();
						String value = binding.getValue().stringValue();
						if (name.contains("category") || name.contains("Category")) {
							commonObjs.add(value);
							System.out.println(String.format("Pattern Four - Common Objs - %s", value));
						}
					}
				}
			}catch(Exception e){
				System.err.println(e);
			}
		}
	}
}
