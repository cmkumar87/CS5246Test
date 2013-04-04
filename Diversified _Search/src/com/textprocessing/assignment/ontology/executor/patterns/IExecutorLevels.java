package com.textprocessing.assignment.ontology.executor.patterns;

import org.openrdf.repository.RepositoryConnection;

public interface IExecutorLevels {

	boolean processRequest(RepositoryConnection con, String entityOne, String entityTwo);
	
}
