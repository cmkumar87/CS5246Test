package com.textprocessing.assignment.ontology.executor;

import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

import com.textprocessing.assignment.ontology.executor.patterns.ExecutorLevelFour;
import com.textprocessing.assignment.ontology.executor.patterns.ExecutorLevelOne;
import com.textprocessing.assignment.ontology.executor.patterns.ExecutorLevelThree;
import com.textprocessing.assignment.ontology.executor.patterns.IExecutorLevels;

public class OntologyExecutor {
	
	private String sesameServer;
	private RepositoryConnection con;
	private IExecutorLevels executorLevels;

	public OntologyExecutor(String sesameServer) {
		this.sesameServer = sesameServer;
		getConnection();
	}
	
	private void getConnection() {
		Repository myRepository = new HTTPRepository(sesameServer);
		try {
			myRepository.initialize();
			con = myRepository.getConnection();
		}catch(RepositoryException e){
			System.err.println(e);
		}
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
