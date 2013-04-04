package com.textprocessing.assignment.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.textprocessing.assignment.processor.DiversificationProcessor;
import com.textprocessing.assignment.query.ranking.QueryRank;

public class DiversificationMain {
	
	private static final String FILE_NAME = "09.mq.topics.20001-60000_10num.txt";

	public void diversifyFromFile() throws Exception {
		List<String> fileContent = readFile();
		Iterator<String> fileContentItr = fileContent.iterator();
		while (fileContentItr.hasNext()) {
			String searchQuery = (String) fileContentItr.next();
			this.diversifySearchQuery(searchQuery);
		}
	}

	public void diversifySearchQuery(String searchQuery) throws Exception {
		DiversificationProcessor processor = new DiversificationProcessor();
		List<String> objs = processor.diversifySearchQuery(searchQuery);
		QueryRank rank = new QueryRank(objs);
		List<String> rankedObjs = rank.getRankedQueriesFormula1();
		System.out.println("Ranked queries - \n"+rankedObjs.toString() );
		System.out.println("\n************END*********************\n");
	}

	private List<String> readFile() throws IOException {
		List<String> fileContent = new ArrayList<String>();
		FileReader fr = new FileReader(FILE_NAME); 
		BufferedReader br = new BufferedReader(fr); 
		String s; 
		while((s = br.readLine()) != null) { 
		//	System.out.println(s); 
			fileContent.add(s);
		} 
		fr.close(); 
		return fileContent;
	} 

	public static void main(String[] args) throws Exception {
		DiversificationMain main = new DiversificationMain();
		main.diversifyFromFile();
	//	main.diversifySearchQuery("obama family");
	}
}
