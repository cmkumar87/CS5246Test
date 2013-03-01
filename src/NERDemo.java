import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

import edu.stanford.nlp.ie.AbstractSequenceClassifier;
import edu.stanford.nlp.ie.crf.CRFClassifier;
import edu.stanford.nlp.io.IOUtils;
import edu.stanford.nlp.ling.CoreAnnotations.AnswerAnnotation;
import edu.stanford.nlp.ling.CoreLabel;


public class NERDemo
{
	public static void main (String[] args) throws IOException
	{
		String serializedClassifier = "NERclassifiers/english.all.3class.distsim.crf.ser.gz";
		
		// usage: java NERDemo <classifier-file> <file-to-classify> <output-file>
		// or
		// change the hardcoded string(s) in the else section (default classifier is english.all.3class.distsim)
		
		if (args.length > 0)
		{
			serializedClassifier = args[0];
		}
		
		AbstractSequenceClassifier<CoreLabel> classifier = CRFClassifier.getClassifierNoExceptions(serializedClassifier);
		
		if (args.length > 1)
		{
			/*
			System.out.println(args[1]);
			String fileContents = IOUtils.slurpFile(args[1]);
			java.util.List<java.util.List<CoreLabel>> out = classifier.classify(fileContents);
			for (java.util.List<CoreLabel> sentence : out)
			{
				for (CoreLabel word : sentence)
				{
					System.out.print(word.word() + '/' + word.get(AnswerAnnotation.class) + ' ');
				}
				System.out.println();
			}
			out = classifier.classifyFile(args[1]);
			for (java.util.List<CoreLabel> sentence : out)
			{
				for (CoreLabel word : sentence)
				{
					System.out.print(word.word() + '/' + word.get(AnswerAnnotation.class) + ' ');
				}
				System.out.println();
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
					writer.write(classifier.classifyWithInlineXML(sentence) + "\n");
					writer.close();
				}
				
				System.out.println("done.");
			}
		}
		else
		{
			String s1 = "Onthology Tutorial in Java";
			//String s2 = "albert einstein was born in germany.";
			System.out.println(classifier.classifyToString(s1));
			/*
			System.out.println(classifier.classifyWithInlineXML(s2));
			System.out.println(classifier.classifyToString(s2, "xml", true));
			int i = 0;
			for (java.util.List<CoreLabel> lcl : classifier.classify(s2))
			{
				for (CoreLabel cl : lcl)
				{
					System.out.println(i++ + ":");
					System.out.println(cl);
				}
			}
			*/
		}
	}
}
