import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

import javax.security.auth.login.AccountLockedException;
import javax.security.auth.login.CredentialException;
import javax.security.auth.login.CredentialExpiredException;
import javax.security.auth.login.CredentialNotFoundException;
import javax.security.auth.login.FailedLoginException;

import org.wikipedia.Wiki;

public class Wikipedia
{
	public static void main(String[] args) throws FileNotFoundException, IOException, ClassNotFoundException
	{
		Wiki wiki = null;
		
		File f = new File("wiki.dat");
		if (f.exists())
		{
			// Already have a copy on disk.
			ObjectInputStream in = new ObjectInputStream(new FileInputStream(f));
			wiki = (Wiki) in.readObject();
		}
		else
		{
			try
			{
				// Create a new connection to en.wikipedia.org.
				wiki = new Wiki("en.wikipedia.org");
				
				// Set edit throttle to 0.2Hz.
				wiki.setThrottle(5000);
				
				// Log in as user ExampleBot, with specified password.
				wiki.login("and9608", "thesun");
			}
			catch (FailedLoginException ex)
			{
				// Deal with failed login attempt.
				System.out.println("Failed to log in.");
				ex.printStackTrace();
			}
		}
		
		boolean[] pages = wiki.exists("Singapore", "hotel");
		
		for (boolean page : pages)
		{
			System.out.println(page);
		}
		
		ObjectOutputStream out = new ObjectOutputStream(new FileOutputStream(f));
		out.writeObject(wiki); // If want session to persist.
		out.close();
		wiki.logout();
	}
}
