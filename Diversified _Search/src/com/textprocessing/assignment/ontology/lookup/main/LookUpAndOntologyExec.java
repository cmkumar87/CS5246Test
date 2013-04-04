package com.textprocessing.assignment.ontology.lookup.main;

import java.util.List;

import com.textprocessing.assignment.ontology.executor.OntologyExecutor;
import com.textprocessing.assignment.ontology.lookup.DbpediaLookupExecutor;
import com.textprocessing.assignment.ontology.lookup.LookupContent;

public class LookUpAndOntologyExec {
	
	private DbpediaLookupExecutor lookupExecutor;
	private OntologyExecutor executor;
	private static final String SESAME_SERVER = "http://dbpedia.org/sparql";
	
	public void execute(String entityOne, String entityTwo) throws Exception {
		lookupExecutor = new DbpediaLookupExecutor();
		executor = new OntologyExecutor(SESAME_SERVER);
		List<LookupContent> contentsOne = lookupExecutor.processHttpGet(entityOne);
		List<LookupContent> contentsTwo = lookupExecutor.processHttpGet(entityTwo);
		if (contentsOne != null && contentsTwo != null && contentsOne.size() >0 && contentsTwo.size() > 0) {
			String entityOneUri = String.format("<%s>",contentsOne.get(0).getURI());
			String entityTwoUri = String.format("<%s>",contentsTwo.get(0).getURI());
			executor.processRequest(entityOneUri, entityTwoUri);
		}else{
			System.out.println("URI is missing for either of the two entities ");
		}
	}

	public static void main(String[] args) {
		LookUpAndOntologyExec ontologyExec = new LookUpAndOntologyExec();
		try{
			//	ontologyExec.execute("sachin", "mumbai");
			//	ontologyExec.execute("singapore", "sentosa");
			ontologyExec.execute("united_states", "bill_gates");
			//	ontologyExec.execute("united_states", "a_p_j");
			//	ontologyExec.execute("united_states", "india");
			//	ontologyExec.execute("united_states", "sachin");
			//	ontologyExec.execute("united_states", "chennai");
			//	ontologyExec.execute("india", "mumbai");
			//	ontologyExec.execute("india", "bill_gates");
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
}
