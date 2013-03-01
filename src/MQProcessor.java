import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MQProcessor
{
	public static void main(String[] args) throws IOException
	{
		String inPath = "test_files/09.mq.topics.20001-60000";
		String outPath = "test_files/processedMQ";
		
		String outPathOriginal = outPath + ".txt";
		String outPathFirstCaps = outPath + "FirstCaps.txt";
		String outPathAllCaps = outPath + "AllCaps.txt";
		
		FileReader fileIn = new FileReader(inPath);
		FileWriter fileOutOriginal = new FileWriter(outPathOriginal);
		FileWriter fileOutFirstCaps = new FileWriter(outPathFirstCaps);
		FileWriter fileOutAllCaps = new FileWriter(outPathAllCaps);
		BufferedWriter writerOriginal = new BufferedWriter(fileOutOriginal);
		BufferedWriter writerFirstCaps = new BufferedWriter(fileOutFirstCaps);
		BufferedWriter writerAllCaps = new BufferedWriter(fileOutAllCaps);
		
		Scanner sc = new Scanner(fileIn);
		int count = 0;
		while (sc.hasNextLine())
		{
			String sentence = sc.nextLine();
			count++;
			
			Pattern p = Pattern.compile("\\d+:\\d:");
			Matcher m = p.matcher(sentence);
			
			if (m.find()) sentence = sentence.substring(m.end(), sentence.length());
			else System.out.println("Pattern not found at line " + count);
			
			// Write queries as it is.
			writerOriginal.write(sentence + "\n");
			
			String[] tokens = sentence.split(" ");
			
			// Write queries with first letters capitalized.
			for (int i = 0; i < tokens.length; i++)
			{
				writerFirstCaps.write(Character.toUpperCase(tokens[i].charAt(0)) + tokens[i].substring(1));
				if (i < tokens.length - 1) writerFirstCaps.write(" ");
				else writerFirstCaps.write("\n");
			}
			
			// Write queries with all letter capitalized.
			for (int i = 0; i < tokens.length; i++)
			{
				writerAllCaps.write(tokens[i].toUpperCase());
				if (i < tokens.length - 1) writerAllCaps.write(" ");
				else writerAllCaps.write("\n");
			}
		}
		
		sc.close();
		fileIn.close();
		writerOriginal.close();
		writerFirstCaps.close();
		writerAllCaps.close();
		fileOutOriginal.close();
		fileOutFirstCaps.close();
		fileOutAllCaps.close();
	}
}
