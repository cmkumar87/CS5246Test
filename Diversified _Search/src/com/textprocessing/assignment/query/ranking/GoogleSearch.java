package com.textprocessing.assignment.query.ranking;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import argo.jdom.JdomParser;
import argo.jdom.JsonNode;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class GoogleSearch implements Comparable<GoogleSearch>
{
	private final String FILE_PATH = "api_keys.txt";
	
	private ArrayList<ApiKey> apiKeys;
	private String query;
	private int totalResults;
	private ArrayList<String> topK;
	private int score;
	
	public GoogleSearch(String q)
	{
		try
		{
			apiKeys = getApiFromFile(FILE_PATH);
		}
		catch (IOException e)
		{
			System.out.println("Could not open file \"" + FILE_PATH + "\".");
			e.printStackTrace();
		}
		query = q;
		totalResults = 0;
		topK = new ArrayList<String>();
		score = 0;
		
		search(query);
	}
	
	private void search(String q)
	{
		int i = 0;
		String apiKey = apiKeys.get(i).getApi();
		String cx = apiKeys.get(i).getCx();
		q = q.replaceAll(" ", "+");
		String urlString = "https://www.googleapis.com/customsearch/v1?key=" + apiKey + "&cx=" + cx + "&q=" + q + "&alt=json";
		// use &start=index to get to next page
		// e.g. if current pages has 10 results, specify &start=11 to get results from next page
		System.out.print("Connecting to " + urlString + "... ");
		
		try
		{
			URL url = new URL(urlString);
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			
			while (i < apiKeys.size() && conn.getResponseCode() == 403)
			{
				System.out.println("connection failed");
				i++;
				if (i == apiKeys.size())
				{
					System.out.println("All API keys out of query quota.");
					return;
				}
				apiKey = apiKeys.get(i).getApi();
				cx = apiKeys.get(i).getCx();
				urlString = "https://www.googleapis.com/customsearch/v1?key=" + apiKey + "&cx=" + cx + "&q=" + q + "&alt=json";
				// use &start=index to get to next page
				// e.g. if current pages has 10 results, specify &start=11 to get results from next page
				System.out.print("Connecting to " + urlString + "... ");
				
				url = new URL(urlString);
				conn = (HttpURLConnection) url.openConnection();
				conn.setRequestMethod("GET");
				conn.setRequestProperty("Accept", "application/json");
			}
			
			System.out.println("connected");
			
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			File f = new File("search_result");
			BufferedWriter bw = new BufferedWriter(new FileWriter(f));
			String output;
			while ((output = br.readLine()) != null)
			{
				bw.write(output);
			}
			
			bw.close();
			conn.disconnect();
			br.close();
			
			br = new BufferedReader(new FileReader(f));
			
			JdomParser jParser = new JdomParser();
			try
			{
				JsonRootNode rootNode = jParser.parse(br);
				
				// Get total results of query.
				JsonNode searchInfo = rootNode.getNode("searchInformation");
				totalResults = Integer.parseInt(searchInfo.getStringValue("totalResults"));
				
				if (totalResults > 0)
				{
					// Get links of top ten results.
					JsonNode items = rootNode.getNode("items");
					int index = 0;
					while (items.isObjectNode(index))
					{
						JsonNode result = items.getNode(index);
						topK.add(result.getStringValue("link"));
						index++;
					}
				}
			}
			catch (InvalidSyntaxException e)
			{
				e.printStackTrace();
				//return false;
			}
			
			conn.disconnect();
			br.close();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
			//return false;
		}
		catch (IOException e)
		{
			e.printStackTrace();
			//return false;
		}
		
		//return true;
	}
	
	public ArrayList<ApiKey> getApiKeys()
	{
		return apiKeys;
	}
	
	public String getQuery()
	{
		return query;
	}
	
	public int getTotalResults()
	{
		return totalResults;
	}
	
	public ArrayList<String> getTopK()
	{
		return topK;
	}
	
	public int getScore()
	{
		return score;
	}
	
	public void setScore(int s)
	{
		score = s;
	}
	
	@Override
	public int compareTo(GoogleSearch gs)
	{
		return gs.getScore() - score;
	}
	
	private ArrayList<ApiKey> getApiFromFile(String path) throws IOException
	{
		ArrayList<ApiKey> apiKeys = new ArrayList<ApiKey>();
		
		File f = new File(path);
		BufferedReader br = new BufferedReader(new FileReader(f));
		
		String output;
		while ((output = br.readLine()) != null)
		{
			String api = output.split(" ")[0];
			String cx = output.split(" ")[1];
			
			apiKeys.add(new ApiKey(api, cx));
		}
		
		return apiKeys;
	}
	
	/*
	public static void main(String[] args)
	{
		String apiKey = "AIzaSyCWXn-c6G3836KEoMMrbclToOrIe7o6f84";
		String cx = "001668935086444843946:fgb7egnnumk";
		
		String query = "singapore sentosa";
		query = query.replaceAll(" ", "+");
		URL url;
		
		try
		{
			url = new URL("https://www.googleapis.com/customsearch/v1?key=" + apiKey + "&cx=" + cx +"&q=" + query + "&alt=json");
			
			HttpURLConnection conn = (HttpURLConnection) url.openConnection();
			conn.setRequestMethod("GET");
			conn.setRequestProperty("Accept", "application/json");
			BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			JdomParser jParser = new JdomParser();
			try
			{
				JsonRootNode rootNode = jParser.parse(br);
				
				// Get total results of query
				JsonNode searchInfo = rootNode.getNode("searchInformation");
				int totalResults = Integer.parseInt(searchInfo.getStringValue("totalResults"));
				
				// Get results in first page
				JsonNode items = rootNode.getNode("items");
				int index = 0;
				while (items.isObjectNode(index))
				{
					JsonNode result = items.getNode(index);
					
					// Get result information
					String title = result.getStringValue("title");
					String link = result.getStringValue("link");
					String snippet = result.getStringValue("snippet");
					
					index++;
					System.out.println("Result #" + index + ":");
					System.out.println("\tTitle   : " + title);
					System.out.println("\tLink    : " + link);
					System.out.println("\tSnippet : " + snippet);
					System.out.println();
				}
				
				System.out.println("---------------------------------------");
				System.out.println("Total results : " + totalResults);
				System.out.println("---------------------------------------");
			}
			catch (InvalidSyntaxException e)
			{
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			Pattern totalResultsP = Pattern.compile("\"totalResults\": .+");
			Matcher m;
			while ((output = br.readLine()) != null)
			{
				m = totalResultsP.matcher(output);
				if (m.find())
				{
					System.out.println(output);
				}
				if (output.contains("\"snippet\": \""))
				{
					//String snippet = output.substring(output.indexOf("\"snippet\":\"") + ("\"snippet\":\"").length(), output.indexOf("\","));
					//System.out.println(snippet);
					System.out.println(output);
					//text.append(snippet + "\n\n\n"); // Will print the Google search links
				}
			}
			
			conn.disconnect();
		}
		catch (MalformedURLException e)
		{
			e.printStackTrace();
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
	}
	*/
}
