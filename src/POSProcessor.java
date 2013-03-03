import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class POSProcessor
{
	public static void main(String[] args) throws IOException
	{
		String inPath = "test_files/output-processedMQ.caseless-left3words.txt";
		String outPath = inPath;
		
		FileReader fileIn = new FileReader(inPath);
		Scanner sc = new Scanner(fileIn);
		int properNouns = 0;
		int properNounCount = 0;
		int wordCount = 0;
		int queryCount = 0;
		while (sc.hasNextLine())
		{
			properNouns = 0;
			
			String query = sc.nextLine();
			String[] tokens = query.split(" ");
			
			Pattern p = Pattern.compile(".+NNPS?");
			for (int i = 0; i < tokens.length; i++)
			{
				wordCount++;
				Matcher m = p.matcher(tokens[i]);
				if (m.matches())
				{
					properNouns++;
					properNounCount++;
				}
			}
			
			if (properNouns > 1) queryCount++;
		}
		
		System.out.println("Words tagged as proper noun: " + properNounCount + "/" + wordCount + " (" + 100.0 * properNounCount / wordCount + "%)");
		System.out.println("Queries with 2 or more proper nouns: " + queryCount + "/40000 (" + 100.0 * queryCount / 40000 + "%)");
		
		sc.close();
		fileIn.close();
	}
}
