package com.textprocessing.assignment.query.ranking;
import java.io.IOException;
import java.util.ArrayList;

public class sampleQueryRank
{
	public static void main(String[] args) throws IOException
	{
		// Two ways of initializing:
		
		// 1.
		ArrayList<String> queryList = new ArrayList<String>();
		queryList.add("country sentosa");
		queryList.add("singapore sentosa");
		queryList.add("singapore tourist destination");
		
		QueryRank qr1 = new QueryRank(queryList);
		
		// 2.
		//QueryRank qr2 = new QueryRank();
		//qr2.addQuery("country sentosa");
		//qr2.addQuery("singapore sentosa");
		//qr2.addQuery("singapore tourist destination");
		
		// Retrieving ranked list:
		ArrayList<String> rankedList;
				
		// Call getRankedQueriesFormula1() to get ranked list of queries using first formula.
		rankedList = qr1.getRankedQueriesFormula1();
		System.out.println("Ranking based on first formula: ");
		for (int i = 0; i < rankedList.size(); i++)
		{
			System.out.println(rankedList.get(i));
		}
		
		// Call getRankedQueriesFormula2() to get ranked list of queries using second formula.
		// This method takes in the original query string as parameter.
		rankedList = qr1.getRankedQueriesFormula2("singapore sentosa");
		System.out.println("Ranking based on second formula: ");
		for (int i = 0; i < rankedList.size(); i++)
		{
			System.out.println(rankedList.get(i));
		}
	}
}