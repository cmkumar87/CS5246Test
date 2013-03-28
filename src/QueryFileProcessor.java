import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class QueryFileProcessor
{
	public static void main(String[] args) throws IOException
	{
		// File directories.
		String inPathOriginalQueries = "test_files/processedMQ.txt";
		String inPathPOSTaggedQueries = "test_files/output-processedMQ.caseless-left3words.txt";
		String inPathNERTaggedAllCapsQueries = "test_files/output-processedMQAllCaps.3class.txt";
		String inPathNERTaggedFirstCapsQueries = "test_files/output-processedMQFirstCaps.3class.txt";
		String outPath = "test_files/output-processedMQ.all.txt";
		// Open files.
		FileReader fileInOriginal = new FileReader(inPathOriginalQueries);
		Scanner sc = new Scanner(fileInOriginal);
		FileReader fileInPOS = new FileReader(inPathPOSTaggedQueries);
		Scanner scPOS = new Scanner (fileInPOS);
		FileReader fileInNERAllCaps = new FileReader(inPathNERTaggedAllCapsQueries);
		Scanner scNERAllCaps = new Scanner(fileInNERAllCaps);
		FileReader fileInNERFirstCaps = new FileReader(inPathNERTaggedFirstCapsQueries);
		Scanner scNERFirstCaps = new Scanner(fileInNERFirstCaps);
		FileWriter fileOut = new FileWriter(outPath);
		BufferedWriter writer = new BufferedWriter(fileOut);
		
		// Write query output format.
		writer.write("Format: <original-query> | <POS-output0>, <POS-output1>, ... | <NER-all-caps-output0>, <NER-all-caps-output1>, ... | <NER-first-caps-output0>, <NER-first-caps-output1>, ...\n");
		writer.write("e.g.  : zebulon vance | zebulon vance | <LOCATION>ZEBULON</LOCATION>, <ORGANIZATION>VANCE</ORGANIZATION> | <PERSON>Zebulon Vance</PERSON>\n\n");
		
		// Move scanners to first query.
		for (int i = 0; i < 3; i++)
		{
			scPOS.nextLine();
			scNERAllCaps.nextLine();
			scNERFirstCaps.nextLine();
		}
		
		// Read and write to file queries identified as containing entities.
		while (sc.hasNextLine())
		{
			// Get next line from all files.
			String originalQuery = sc.nextLine();
			String POSOutput = scPOS.nextLine();
			String NERAllCapsOutput = scNERAllCaps.nextLine();
			String NERFirstCapsOutput = scNERFirstCaps.nextLine();
			
			// Get tokens of current query.
			String[] originalTokens = originalQuery.split(" ");
			String[] POSTokens = POSOutput.split(" ");
			String[] NERAllCapsTokens = NERAllCapsOutput.split(" ");
			String[] NERFirstCapsTokens = NERFirstCapsOutput.split(" ");
			
			// Check if any word(s) tagged as NNP(S).
			String NNPString = "";
			boolean successiveNNP = false;
			for (int i = 0; i < POSTokens.length; i++)
			{
				Matcher m = Pattern.compile(".+NNPS?").matcher(POSTokens[i]);
				if (m.matches())
				{
					if (successiveNNP) NNPString += " ";
					else if (NNPString.length() > 0) NNPString += ", ";
					NNPString += POSTokens[i].split("_")[0];
					successiveNNP = true;
				}
				else successiveNNP = false;
			}
			
			// Check if any word(s) in all caps queries tagged as entity.
			String NERAllCapsString = "";
			boolean successiveNER = false;
			for (int i = 0; i < NERAllCapsTokens.length; i++)
			{
				Matcher mStart = Pattern.compile("<.+>.+").matcher(NERAllCapsTokens[i]);
				Matcher mEnd = Pattern.compile(".+</.+>").matcher(NERAllCapsTokens[i]);
				if (mStart.matches())
				{
					successiveNER = true;
					if (NERAllCapsString.length() > 0) NERAllCapsString += ",";
				}
				if (mEnd.matches())
				{
					successiveNER = false;
					if (NERAllCapsString.length() > 0) NERAllCapsString += " ";
					NERAllCapsString += NERAllCapsTokens[i];
				}
				if (successiveNER)
				{
					if (NERAllCapsString.length() > 0) NERAllCapsString += " ";
					NERAllCapsString += NERAllCapsTokens[i];
				}
			}
			
			// Check if any word(s) in first caps queries tagged as entity.
			String NERFirstCapsString = "";
			successiveNER = false;
			for (int i = 0; i < NERFirstCapsTokens.length; i++)
			{
				Matcher mStart = Pattern.compile("<.+>.+").matcher(NERFirstCapsTokens[i]);
				Matcher mEnd = Pattern.compile(".+</.+>").matcher(NERFirstCapsTokens[i]);
				if (mStart.matches())
				{
					successiveNER = true;
					if (NERFirstCapsString.length() > 0) NERFirstCapsString += ",";
				}
				if (mEnd.matches())
				{
					successiveNER = false;
					if (NERFirstCapsString.length() > 0) NERFirstCapsString += " ";
					NERFirstCapsString += NERFirstCapsTokens[i];
				}
				if (successiveNER)
				{
					if (NERFirstCapsString.length() > 0) NERFirstCapsString += " ";
					NERFirstCapsString += NERFirstCapsTokens[i];
				}
			}
			
			// Check if any of the taggers produced an output.
			// Write query and output in specified format if any tagger has an output.
			if (NNPString.length() > 0 || NERAllCapsString.length() > 0 || NERFirstCapsString.length() > 0)
			{
				writer.write(originalQuery + " | ");
				writer.write(NNPString + " | ");
				writer.write(NERAllCapsString + " | ");
				writer.write(NERFirstCapsString + "\n");
			}
		}
		
		// Close files.
		sc.close();
		fileInOriginal.close();
		scPOS.close();
		fileInPOS.close();
		scNERAllCaps.close();
		fileInNERAllCaps.close();
		scNERFirstCaps.close();
		fileInNERFirstCaps.close();
		writer.close();
		fileOut.close();
	}
}
