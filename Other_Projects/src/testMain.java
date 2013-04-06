import java.io.IOException;

public class testMain
{
	public static void main(String[] args) throws ClassNotFoundException, IOException
	{
		ApiKeyManager apm = new ApiKeyManager();
		
		for (int i = 0; i < 200; i++)
		{
			System.out.print("Using key #" + (i + 1) + "... ");
			ApiKey key = apm.useNextKey();
			System.out.println("Quota reached? " + apm.limitReached());
		}
		
		//System.out.println(apm.limitReached());
		
		//System.out.println(key.getApi());
		//System.out.println(key.getCx());
		//System.out.println(key.getRemainingQuota());
	}
}