package com.textprocessing.assignment.ontology.executor.disambiguity;

import java.util.List;

import org.openrdf.repository.RepositoryConnection;

public interface IDisambiguityExecutor {

	public List<String> processRequest(RepositoryConnection con, String entity);

}
