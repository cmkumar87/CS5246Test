import java.util.ArrayList;

public class sampleStringProcessor
{
	// Sample code for using StringProcessor.
	public static void main(String[] args)
	{
		StringProcessor sp = new StringProcessor();
		String str = "Some problems are so complex that you have to be highly intelligent and well informed just to be undecided about them.";
		ArrayList<String> tokens = new ArrayList<String>();
		
		tokens = sp.tokenize(str);
		
		System.out.print("Without tags : ");
		for (int i = 0; i < tokens.size(); i++)
		{
			System.out.print(tokens.get(i) + " ");
		}
		
		System.out.println();
		tokens = sp.tokenizeWithTags(str);
		
		System.out.print("With tags : ");
		for (int i = 0; i < tokens.size(); i++)
		{
			System.out.print(tokens.get(i) + " ");
		}
	}
}
