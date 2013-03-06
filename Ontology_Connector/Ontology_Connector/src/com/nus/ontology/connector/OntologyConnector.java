package com.nus.ontology.connector;

import java.util.Iterator;

import org.openrdf.query.Binding;
import org.openrdf.query.BindingSet;
import org.openrdf.query.QueryLanguage;
import org.openrdf.query.TupleQuery;
import org.openrdf.query.TupleQueryResult;
import org.openrdf.repository.Repository;
import org.openrdf.repository.RepositoryConnection;
import org.openrdf.repository.RepositoryException;
import org.openrdf.repository.http.HTTPRepository;

public class OntologyConnector {

	String sesameServer = "http://dbpedia.org/sparql";
	String repositoryID = "sparql";

	public OntologyConnector() {
		//	Repository myRepository = new HTTPRepository(sesameServer, repositoryID);
		Repository myRepository = new HTTPRepository(sesameServer);
		try {
			myRepository.initialize();
			RepositoryConnection con = myRepository.getConnection();
			try{
				/*String queryString = "SELECT ?rel ?obj WHERE {" +
						" <http://dbpedia.org/resource/India> ?rel ?obj }";*/
				/*String queryString = "SELECT ?obj WHERE {" +
						" <http://dbpedia.org/resource/India> <http://dbpedia.org/ontology/currency> ?obj }";*/
				/*String queryString = "SELECT ?sub ?obj WHERE {" +
						" ?sub <http://dbpedia.org/ontology/currency> ?obj }";*/
/*				String queryString = "SELECT ?rel ?obj WHERE {" +
						"<http://dbpedia.org/resource/cloud-computing> ?rel ?obj }";*/
				String queryString = "SELECT ?property ?hasValue ?isValueOf " +
						" WHERE { " +
						" { <http://dbpedia.org/resource/India> ?property ?hasValue } " +
						" UNION " +
						" { ?isValueOf ?property <http://dbpedia.org/resource/India> } " +
						" FILTER ( REGEX(STR(?hasValue), 'category', 'i') || REGEX(STR(?isValueOf), 'category', 'i') ) " +
						"}";
				TupleQuery tupleQuery = con.prepareTupleQuery(QueryLanguage.SERQL, queryString);
				TupleQueryResult result = tupleQuery.evaluate();
				while(result.hasNext()){
					BindingSet bindingSet = result.next();
					Iterator<Binding> itr = bindingSet.iterator();
					while (itr.hasNext()) {
						Binding binding = (Binding) itr.next();
						System.out.println(binding.getName() + " ---- " +binding.getValue());
					}
					System.out.println("***********");
				}
			}catch(Exception e){
				e.printStackTrace();
			}
		} catch (RepositoryException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		OntologyConnector connector = new OntologyConnector();

	}

}
