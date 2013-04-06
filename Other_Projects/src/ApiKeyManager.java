import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ApiKeyManager
{
	private final String FILE_PATH = "Other_Projects/api_key_file";
	private final int API_INDEX = 0;
	private final int CX_INDEX = 1;
	private final String[][] KEYS = {{"AIzaSyCWXn-c6G3836KEoMMrbclToOrIe7o6f84", "001668935086444843946:fgb7egnnumk"}
									,{"testApiKey", "testCxKey"}
									};
	
	private ApiKey[] apiKeyList;
	private int keyPointer;
	
	public ApiKeyManager() throws IOException, ClassNotFoundException
	{
		File f = new File(FILE_PATH);
		if (f.exists())
		{
			FileInputStream fin = new FileInputStream(f);
			ObjectInputStream ois = new ObjectInputStream(fin);
			
			apiKeyList = (ApiKey[]) ois.readObject();
			
			ois.close();
			fin.close();
		}
		else
		{
			apiKeyList = new ApiKey[KEYS.length];
			resetQuota();
		}
		
		keyPointer = 0;
		updateKeyPointer();
	}
	
	public void resetQuota() throws IOException
	{
		for (int i = 0; i < KEYS.length; i++)
		{
			apiKeyList[i] = new ApiKey(KEYS[i][API_INDEX], KEYS[i][CX_INDEX]);
		}
		updateToFile();
		keyPointer = 0;
	}
	
	public ApiKey useNextKey() throws IOException
	{
		updateKeyPointer();
		apiKeyList[keyPointer].decrementQuota();
		updateToFile();
		return apiKeyList[keyPointer];
	}
	
	private void updateToFile() throws IOException
	{
		FileOutputStream fout = new FileOutputStream(FILE_PATH);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		
		oos.writeObject(apiKeyList);
		
		oos.close();
		fout.close();
	}
	
	private void updateKeyPointer()
	{
		while (keyPointer < KEYS.length && apiKeyList[keyPointer].getRemainingQuota() == 0) keyPointer++;
	}
	
	public boolean limitReached()
	{
		return (keyPointer >= KEYS.length - 1 && apiKeyList[keyPointer].getRemainingQuota() == 0);
	}
}