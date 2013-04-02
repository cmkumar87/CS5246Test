package com.textprocessing.assignment.ontology.executor.categories;

import java.util.Map;

import org.openrdf.repository.RepositoryConnection;

public interface ICategoryExecutor {

	public Map<String, String> processRequest(RepositoryConnection con, String entity);
	
}
