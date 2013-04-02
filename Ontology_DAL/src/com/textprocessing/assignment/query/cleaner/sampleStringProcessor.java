package com.textprocessing.assignment.query.cleaner;
import java.util.ArrayList;

public class sampleStringProcessor
{
	// Sample code for using StringProcessor.
	public static void main(String[] args)
	{
		StringProcessor sp = new StringProcessor();
		String str = "Sentosa of singapore";
		ArrayList<String> tokens = new ArrayList<String>();
		
		tokens = sp.tokenize(str);
		
		System.out.print("Without tags : ");
		for (int i = 0; i < tokens.size(); i++)
		{
			System.out.print(tokens.get(i) + " ");
		}
		
		System.out.println();
		tokens = sp.tokenizeWithTags(str);
		
		System.out.print("With tags : ");
		for (int i = 0; i < tokens.size(); i++)
		{
			System.out.print(tokens.get(i) + " ");
		}
	}
}
