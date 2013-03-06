import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class QueryProcessor
{
	public static void main(String[] args) throws IOException
	{
		// File directories.
		String inPathOriginalQueries = "test_files/processedMQ.txt";
		String inPathPOSTaggedQueries = "test_files/output-processedMQ.caseless-left3words.txt";
		String inPathNERTaggedAllCapsQueries = "test_files/processedMQAllCaps.3class.txt";
		String inPathNERTaggedFirstCapsQueries = "test_files/processedMQFirstCaps.3class.txt";
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
		// e.g.: obama family tree | obama family | obama
		writer.write("Format: <original-query> | <POS-output0>, <POS-output1>, ... | <NER-output0>, <NER-output1>, ...\n\n");
		// e.g.: obama family tree | obama_NNP family_NNP tree_NN | <PERSON>obama</PERSON> family tree
		writer.write("Format: <original-query> | <POS-output> | <NER-output>\n\n");
		
		// Read and write to file queries identified as containing entities.
		while (sc.hasNextLine())
		{
			// Get next line from all files.
			String originalQuery = sc.nextLine();
			String POSOutput = scPOS.nextLine();
			String NERAllCapsOutput = scNERAllCaps.nextLine();
			String NERFirstCapsOutput = scNERFirstCaps.nextLine();
			
			// Check if any word(s) tagged as NNP(S).
			
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
