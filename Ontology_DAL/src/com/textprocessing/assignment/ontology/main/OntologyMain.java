package com.textprocessing.assignment.ontology.main;

import com.textprocessing.assignment.ontology.executor.OntologyExecutor;

public class OntologyMain {
	
	private OntologyExecutor executor;
	private static final String SESAME_SERVER = "http://dbpedia.org/sparql";
	
	public OntologyMain() {
		executor = new OntologyExecutor(SESAME_SERVER);
	}
	
	public void processRequest(String entityOne, String entityTwo) {
		executor.processRequest(entityOne, entityTwo);
	}

	public static void main(String[] args) {
		OntologyMain main = new OntologyMain();
		//main.processRequest("<http://dbpedia.org/resource/Singapore>", "<http://dbpedia.org/resource/Sentosa>");
		//main.processRequest("<http://dbpedia.org/resource/United_States>", "<http://dbpedia.org/resource/Bill_Gates>");
		//main.processRequest("<http://dbpedia.org/resource/United_States>", "<http://dbpedia.org/resource/A._P._J._Abdul_Kalam>");
		//main.processRequest("<http://dbpedia.org/resource/United_States>", "<http://dbpedia.org/resource/India>");
		//main.processRequest("<http://dbpedia.org/resource/Sachin_Tendulkar>", "<http://dbpedia.org/resource/India>");
	//	main.processRequest("<http://dbpedia.org/resource/India>", "<http://dbpedia.org/resource/Sachin_Tendulkar>");
	//	main.processRequest("<http://dbpedia.org/resource/United_States>", "<http://dbpedia.org/resource/Sachin_Tendulkar>");
	//	main.processRequest("<http://dbpedia.org/resource/United_States>", "<http://dbpedia.org/resource/Chennai>");
	//	main.processRequest("<http://dbpedia.org/resource/India>", "<http://dbpedia.org/resource/Chennai>");
	//	main.processRequest("<http://dbpedia.org/resource/India>", "<http://dbpedia.org/resource/Mumbai>");
		main.processRequest("<http://dbpedia.org/resource/India>", "<http://dbpedia.org/resource/Bill_Gates>");

	}
}
