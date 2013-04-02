package com.textprocessing.assignment.query.cleaner;
import java.io.IOException;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class StringProcessor
{
	private String modelFile;
	private MaxentTagger tagger;
	
	public StringProcessor()
	{
		modelFile = "POSmodels/english-caseless-left3words-distsim.tagger";
		
		try
		{
			tagger = new MaxentTagger(modelFile);
		}
		catch (ClassNotFoundException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	// Tokenize and remove some words from a string.
	public ArrayList<String> tokenize(String str)
	{
		// Tag string.
		str = tagger.tagString(str);
		
		// Tokenize tagged string.
		String[] tokens = str.split(" ");
		
		// Remove tokens tagged as prepositions.
		ArrayList<String> tokenList = new ArrayList<String>();
		Pattern p = Pattern.compile(".+_(IN|TO)");
		Matcher m;
		for (int i = 0; i < tokens.length; i++)
		{
			m = p.matcher(tokens[i]);
			if (!m.matches())
			{
				// Remove POS tag from token.
				tokens[i] = tokens[i].split("_")[0];
				
				// Add token to list.
				tokenList.add(tokens[i]);
			}
		}
		
		return tokenList;
	}
	
	public ArrayList<String> tokenizeWithTags(String str)
	{
		// Tag string.
		str = tagger.tagString(str);
		
		// Tokenize tagged string.
		String[] tokens = str.split(" ");
		
		// Remove tokens tagged as prepositions.
		ArrayList<String> tokenList = new ArrayList<String>();
		Pattern p = Pattern.compile(".+_(IN|TO)");
		Matcher m;
		for (int i = 0; i < tokens.length; i++)
		{
			m = p.matcher(tokens[i]);
			if (!m.matches())
			{
				// Add token to list.
				tokenList.add(tokens[i]);
			}
		}
		
		return tokenList;
	}
}
