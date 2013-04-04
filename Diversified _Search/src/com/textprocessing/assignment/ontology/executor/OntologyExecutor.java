package com.textprocessing.assignment.ontology.executor;

import org.openrdf.repository.RepositoryConnection;

import com.textprocessing.assignment.ontology.executor.patterns.ExecutorLevelFour;
import com.textprocessing.assignment.ontology.executor.patterns.ExecutorLevelOne;
import com.textprocessing.assignment.ontology.executor.patterns.ExecutorLevelThree;
import com.textprocessing.assignment.ontology.executor.patterns.IExecutorLevels;

public class OntologyExecutor {
	
	private RepositoryConnection con;
	private IExecutorLevels executorLevels;
	private OntologyConnector connector;

	public OntologyExecutor(String sesameServer) {
		connector = new OntologyConnector(sesameServer);
		con = connector.getConnection();
	}
	
	
	public void processRequest(String entityOne, String entityTwo) {
		boolean status = false;
		executorLevels = new ExecutorLevelOne();
		status = executorLevels.processRequest(con, entityOne, entityTwo);
		if (!status) {
			executorLevels = new ExecutorLevelOne();
			status = executorLevels.processRequest(con, entityTwo, entityOne);
		}if (!status) {
			executorLevels = new ExecutorLevelThree();
			status = executorLevels.processRequest(con, entityOne, entityTwo);
		}if (!status) {
			executorLevels = new ExecutorLevelFour();
			status = executorLevels.processRequest(con, entityOne, entityTwo);
		}if (!status) {
			System.out.println("NO RESULTS FROM ALL 4 PATTERNS :( ");
		}
	}
}
