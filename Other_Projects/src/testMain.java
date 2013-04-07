import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

import argo.jdom.JdomParser;
import argo.jdom.JsonRootNode;
import argo.saj.InvalidSyntaxException;

public class testMain
{
	public static void main(String[] args) throws ClassNotFoundException, IOException
	{
		/*
		ApiKeyManager apm = new ApiKeyManager();
		
		for (int i = 0; i < 200; i++)
		{
			//System.out.print("Using key #" + (i + 1) + "... ");
			//ApiKey key = apm.useNextKey();
			//System.out.println("Quota reached? " + apm.limitReached());
		}
		
		//System.out.println(apm.limitReached());
		
		//System.out.println(key.getApi());
		//System.out.println(key.getCx());
		//System.out.println(key.getRemainingQuota());
		
		File f = new File("Other_Projects/search_result");
		BufferedWriter bw = new BufferedWriter(new FileWriter(f));
		//JdomParser jParser = new JdomParser();
		//try {
		//	JsonRootNode rootNode = jParser.parse(br);
		//} catch (InvalidSyntaxException e) {
		//	// TODO Auto-generated catch block
		//	e.printStackTrace();
		//}
		*/
		
		GoogleSearch gs = new GoogleSearch("test");
		ArrayList<ApiKey> keyList = gs.getApiKeys();
		for (int i = 0; i < keyList.size(); i++)
		{
			ApiKey apiKey = keyList.get(i);
			System.out.println("API key : " + apiKey.getApi());
			System.out.println("Site cx : " + apiKey.getCx());
		}
	}
}