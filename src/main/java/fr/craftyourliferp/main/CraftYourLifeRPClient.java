package fr.craftyourliferp.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.UserSession;
import net.minecraft.client.audio.ISound;

public class CraftYourLifeRPClient 
{

    public static String ip = "213.32.17.6";
    public static String port = "25565";
    
	public static String version = "undefined";
	
	public static boolean[] wantedLevel = new boolean[5];  

	public static UserSession currentSession;
	
	public static boolean optimizeRendering = false;
	
	private static int displayBlackScreen; //display black screen in seconds
	
	//public static Effect currentEffect;
	
	//public static PlayerCachedData cachedData;
	
	public static ExtendedPlayer cachedData;

	
	public CraftYourLifeRPClient()
	{
    	try {
			currentSession = UserSession.getSession();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
    public static String getMacAdress() throws UnknownHostException,SocketException
    {
		InetAddress ipAddress = InetAddress.getLocalHost();
		NetworkInterface networkInterface = NetworkInterface.getByInetAddress(ipAddress);
		
		byte[] macAddressBytes = networkInterface.getHardwareAddress();
		StringBuilder macAddressBuilder = new StringBuilder();
		
		for (int macAddressByteIndex = 0; macAddressByteIndex < macAddressBytes.length; macAddressByteIndex++)
		{
		    String macAddressHexByte = String.format("%02X",
		            macAddressBytes[macAddressByteIndex]);
		    macAddressBuilder.append(macAddressHexByte);
		
		    if (macAddressByteIndex != macAddressBytes.length - 1)
		    {
		        macAddressBuilder.append(":");
		    }
		}
		
		return macAddressBuilder.toString();
    }
    
    public static String getComputerName()
    {
        Map<String, String> env = System.getenv();
        if (env.containsKey("COMPUTERNAME"))
            return env.get("COMPUTERNAME");
        else if (env.containsKey("HOSTNAME"))
            return env.get("HOSTNAME");
        else
            return "undefined";
    }
    
    public static String getComputerUID() throws IOException, InterruptedException 
    {
    	String OS = System.getProperty("os.name").toLowerCase();
    	
    	if(OS.indexOf("win") >= 0)
    	{
    		String machineId = "";
    		try
    		{
	    		String command = "wmic csproduct get UUID";
	    	    StringBuffer output = new StringBuffer();
	
	    	    Process SerNumProcess = Runtime.getRuntime().exec(command);
	    	    BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));
	
	    	    String line = "";
	    	    while ((line = sNumReader.readLine()) != null) {
	    	        output.append(line + "\n");
	    	    }
	    	    
	    	    machineId = output.toString().substring(output.indexOf("\n"), output.length()).trim();
    		}
    		catch(Exception e)
    		{
    			e.printStackTrace();
    		}
    		
    	    return machineId;
    	}
    	else if(OS.indexOf("mac") >= 0)
    	{
    		String command = "system_profiler SPHardwareDataType | awk '/UUID/ { print $3; }'";

    	    StringBuffer output = new StringBuffer();


    	    Process SerNumProcess = Runtime.getRuntime().exec(command);

    	    BufferedReader sNumReader = new BufferedReader(new InputStreamReader(SerNumProcess.getInputStream()));

    	    String line = "";

    	    while ((line = sNumReader.readLine()) != null) {
    	        output.append(line + "\n");
    	    }

    	    String machineId=output.toString().substring(output.indexOf("UUID: "), output.length()).replace("UUID: ", "");

    	    SerNumProcess.waitFor();

    	    sNumReader.close();

    	    return machineId;
    	}
    	else if(OS.indexOf("nix") >= 0 || OS.indexOf("nux") >= 0 || OS.indexOf("aix") > 0)
    	{
    		   StringBuffer output = new StringBuffer();
    	       Process process;
    	       process = Runtime.getRuntime().exec("cat /sys/class/dmi/id/product_uuid");
    	       BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
    	       String line = "";
    	       while ((line = reader.readLine()) != null) 
    	       {
    	    	   output.append(line + "\n");
    	       }
    	       return output.toString();      
    	}
    	
    	return "undefined";
    }
    
    public static void displayBlackScreen(int seconds)
    {
    	displayBlackScreen = seconds;
    }
    
    public static void decrementBlackScreenCounter()
    {
    	displayBlackScreen--;
    }
    
    public static int getBlackScreenTime()
    {
    	return displayBlackScreen;
    }
	
	
	
}
