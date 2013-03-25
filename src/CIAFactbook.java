import java.io.IOException;
import java.net.MalformedURLException;

import factbook.Factbook;

public class CIAFactbook
{
	public static void main(String[] args) throws MalformedURLException, IOException
	{
		Factbook factbook = new Factbook();
		
		String background = factbook.get("us", "Background");
		
		System.out.println(background);
	}
}
