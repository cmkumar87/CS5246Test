package com.textprocessing.assignment.main;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import com.textprocessing.assignment.processor.DiversificationProcessor;
import com.textprocessing.assignment.query.ranking.QueryRank;

public class DiversificationMain {
	
	private static final String FILE_NAME = "09.mq.topics.20001-60000_10num.txt";
	private DiversificationProcessor processor = new DiversificationProcessor();

	public void diversifyFromFile() throws Exception {
		List<String> fileContent = readFile();
		Iterator<String> fileContentItr = fileContent.iterator();
		
		// Print ranking to file.
		PrintWriter writer = new PrintWriter("ranking.txt");
		writer.println("Format : line 1 - [unranked set] ; line 2 - [ranked based on formula 1] ; line 3 - [ranked based on formula 2]");
		writer.close();
		
		while (fileContentItr.hasNext()) {
			String searchQuery = (String) fileContentItr.next();
			this.diversifySearchQuery(searchQuery);
		}
	}

	public void diversifySearchQuery(String searchQuery) throws Exception {
		List<String> objs = processor.diversifySearchQuery(searchQuery);
		QueryRank rank = new QueryRank(objs);
		List<String> rankedObjs1 = rank.getRankedQueriesFormula1();
		List<String> rankedObjs2 = rank.getRankedQueriesFormula2(searchQuery);
		//System.out.println("Ranked queries - \n"+rankedObjs.toString() );
		//System.out.println("\n************END*********************\n");
		
		// Print ranking to file.
		PrintWriter writer = new PrintWriter(new BufferedWriter(new FileWriter("ranking.txt", true)));
		writer.println();
		writer.println(objs);
		writer.println(rankedObjs1);
		writer.println(rankedObjs2);
		writer.close();
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
		//main.diversifySearchQuery("sachin tendulkar");
	}
}
