
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import edu.stanford.nlp.ling.Sentence;
import edu.stanford.nlp.ling.TaggedWord;
import edu.stanford.nlp.ling.HasWord;
import edu.stanford.nlp.tagger.maxent.MaxentTagger;

public class TaggerDemo
{
	public static void main(String[] args) throws Exception
	{
		String modelFile = "POSmodels/english-caseless-left3words-distsim.tagger";
		
		// usage: java TaggerDemo <model-file> <file-to-tag> <output-file>
		// or
		// change the hardcoded string(s) in the else section (default model is english-caseless-left3words-distsim)
		
		if (args.length > 0)
		{
			modelFile = args[0];
		}
		
		MaxentTagger tagger = new MaxentTagger(modelFile);
		
		if (args.length > 1)
		{
			/*
			List<List<HasWord>> sentences = MaxentTagger.tokenizeText(new BufferedReader(new FileReader(args[1])));
			for (List<HasWord> sentence : sentences)
			{
				ArrayList<TaggedWord> tSentence = tagger.tagSentence(sentence);
				System.out.println(Sentence.listToString(tSentence, false));
			}
			*/
			
			if (args.length > 2)
			{
				FileWriter fileOut = new FileWriter(args[2]);
				BufferedWriter writer = new BufferedWriter(fileOut);
				writer.write("");
				writer.close();
			}
			
			FileReader fileIn = new FileReader(args[1]);
			Scanner sc = new Scanner(fileIn);
			int count = 0;
			while (sc.hasNextLine())
			{
				String sentence = sc.nextLine();
				
				count++;
				System.out.print("Processing line " + count + "... ");
				
				if (args.length > 2)
				{
					FileWriter fileOut = new FileWriter(args[2], true);
					BufferedWriter writer = new BufferedWriter(fileOut);
					writer.write(tagger.tagString(sentence) + "\n");
					writer.close();
				}
				
				System.out.println("done.");
			}
		}
		else
		{
			String s = "onthology tutorial in java";
			System.out.println(tagger.tagString(s));
		}
	}
}