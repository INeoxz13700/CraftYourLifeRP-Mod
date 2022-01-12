package fr.craftyourliferp.data;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Base64;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.FileEditor;
import fr.craftyourliferp.utils.HTTPUtils;
import net.minecraft.client.Minecraft;

public class UserSession {

	private String username;
	
	private String cryptedPassword;
	
	private UserSession() { }
	
	public static UserSession getSession() throws Exception
	{
FileEditor editor = new FileEditor(new File("Session.cylrp"),true);
		
		UserSession session = new UserSession();
		
		String mcUsername = Minecraft.getMinecraft().getSession().getUsername();
		
		session.username = editor.getString("username");
		
		
		if(session.username == null || !mcUsername.equalsIgnoreCase(session.username)) 
		{
			return null;
		}
		
		session.cryptedPassword = editor.getString("password");
		
		if(session.cryptedPassword == null)
		{

			return null;
		}
		
		Object[] cData = connectUser(session.username,session.cryptedPassword);
		if((boolean)cData[3])
		{

			editor.writeDataToFile();
			return session;
		}
		

		
		editor.reset();
		editor.writeDataToFile();
		return null;
	}
	
	public static UserSession createSession(String username, String cryptedPassword, boolean save) throws Exception
	{
		FileEditor editor = new FileEditor(new File("Session.cylrp"),true);

		UserSession session = new UserSession();
		session.username = username;
		session.cryptedPassword = cryptedPassword;
		
		if(save)
		{
			editor.reset();
			editor.write("username", username);
			editor.write("password", cryptedPassword);
			
			System.out.println("Session saved");
			
			editor.writeDataToFile();
		}
		
		return session;
	}
	
	public static void destroySession() throws Exception
	{
		FileEditor editor = new FileEditor(new File("Session.cylrp"),false);
		if(editor.fileExist())
		{
			editor.reset();
			editor.writeDataToFile();
			System.out.println("Session destroyed!");
		}
	}
	
	public static Object[] connectUser(String username, String password, String connectionData)
	{
		String url = CraftYourLifeRPMod.apiIp +  "/connection_new.php";
		try {
			username = URLEncoder.encode(username, "UTF-8");
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
		}
		String result = HTTPUtils.doPostHttp(url,"pseudo="+username+"&password="+password+"&cdata=" + connectionData);
		
		System.out.println(result);
		if(result.equalsIgnoreCase("too many attempts"))
		{
			System.out.println("session incorrect");
			return new Object[] {username, password, "§cTrop de tentative réessayez dans 10 minutes", false};
		}
		else if(result.equalsIgnoreCase("true"))
		{
			System.out.println("Session sucessfully obtained");
			return new Object[] {username, password, "§aAuthentification réussi!", true};
		}
		else if(result.equalsIgnoreCase("false")) {
			System.out.println("session incorrect");
			return new Object[] {username, password, "§cIdentifiants Incorrect", false};
		}
		else if(result.equalsIgnoreCase("blacklisted"))
		{
			System.out.println("session incorrect");
			return new Object[] {username, password, "§cHmm, why ?", false};
		}
		else
		{
			System.out.println("session incorrect");
			return new Object[] {username, password, result, false};
		}
	}
	
	public static Object[] connectUser(String username, String password)
	{
		return connectUser(username, password, "");
	}
	
	public String getUsername()
	{
		return this.username;
	}
	
	public String getCryptedPassword()
	{
		return this.cryptedPassword;
	}
	
	public String getBasicToken()
	{
		return Base64.getEncoder().encodeToString((username + ":" + cryptedPassword).getBytes());
	}
	

}
