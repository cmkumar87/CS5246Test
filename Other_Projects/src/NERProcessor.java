import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Pattern;

public class NERProcessor
{
	public static void main(String[] args) throws IOException
	{
		String inPath = "Other_Projects/test_files/output-processedMQ.3class.txt";
		String outPath = inPath;
		
		FileReader fileIn = new FileReader(inPath);
		Scanner sc = new Scanner(fileIn);
		int entities = 0;
		int entityCount = 0;
		int wordCount = 0;
		int queryCount = 0;
		while (sc.hasNextLine())
		{
			entities = 0;
			
			String query = sc.nextLine();
			String[] tokens = query.split(" ");
			
			Pattern p_LOCATION = Pattern.compile("</LOCATION>");
			Pattern p_ORGANIZATION = Pattern.compile("</ORGANIZATION>");
			Pattern p_PERSON = Pattern.compile("</PERSON>");
			for (int i = 0; i < tokens.length; i++)
			{
				wordCount++;
				if (p_LOCATION.matcher(tokens[i]).find())
				{
					entities++;
					entityCount++;
					Pattern p = Pattern.compile("<LOCATION>");
					int j = i;
					while (j >= 0 && !p.matcher(tokens[j]).find())
					{
						entityCount++;
						j--;
					}
				}
				if (p_ORGANIZATION.matcher(tokens[i]).find())
				{
					entities++;
					entityCount++;
					Pattern p = Pattern.compile("<ORGANIZATION>");
					int j = i;
					while (j >= 0 && !p.matcher(tokens[j]).find())
					{
						entityCount++;
						j--;
					}
				}
				if (p_PERSON.matcher(tokens[i]).find())
				{
					entities++;
					entityCount++;
					Pattern p = Pattern.compile("<PERSON>");
					int j = i;
					while (j >= 0 && !p.matcher(tokens[j]).find())
					{
						entityCount++;
						j--;
					}
				}
			}
			
			if (entities > 1) queryCount++;
		}
		
		System.out.println("Words tagged as entities: " + entityCount + "/" + wordCount + " (" + 100.0 * entityCount / wordCount + "%)");
		System.out.println("Queries with 2 or more entities: " + queryCount + "/40000 (" + 100.0 * queryCount / 40000 + "%)");
		
		sc.close();
		fileIn.close();
	}
}
