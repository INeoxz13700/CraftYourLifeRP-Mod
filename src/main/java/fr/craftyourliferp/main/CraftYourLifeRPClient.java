package fr.craftyourliferp.main;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;
import java.util.Map;

import com.google.gson.JsonSyntaxException;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.UserSession;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.util.ResourceLocation;

public class CraftYourLifeRPClient 
{

    public static String ip = "213.32.17.6";
    public static String port = "25565";
    
	public static String version = "undefined";
	
	public static boolean[] wantedLevel = new boolean[5];  

	public static UserSession currentSession;
	
	public static boolean optimizeRendering = false;
	
	private static int displayBlackScreen; //display black screen in seconds
	
	private static int selectedShaderIndex;

	
	//public static Effect currentEffect;
	
	
	public static ExtendedPlayer cachedData;
	
    public static final ResourceLocation[] shaderResourceLocations = new ResourceLocation[] {new ResourceLocation("shaders/post/notch.json"), new ResourceLocation("shaders/post/fxaa.json"), new ResourceLocation("shaders/post/art.json"), new ResourceLocation("shaders/post/bumpy.json"), new ResourceLocation("shaders/post/blobs2.json"), new ResourceLocation("shaders/post/pencil.json"), new ResourceLocation("shaders/post/color_convolve.json"), new ResourceLocation("shaders/post/deconverge.json"), new ResourceLocation("shaders/post/flip.json"), new ResourceLocation("shaders/post/invert.json"), new ResourceLocation("shaders/post/ntsc.json"), new ResourceLocation("shaders/post/outline.json"), new ResourceLocation("shaders/post/phosphor.json"), new ResourceLocation("shaders/post/scan_pincushion.json"), new ResourceLocation("shaders/post/sobel.json"), new ResourceLocation("shaders/post/bits.json"), new ResourceLocation("shaders/post/desaturate.json"), new ResourceLocation("shaders/post/green.json"), new ResourceLocation("shaders/post/blur.json"), new ResourceLocation("shaders/post/wobble.json"), new ResourceLocation("shaders/post/blobs.json"), new ResourceLocation("shaders/post/antialias.json")};

	
	
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
    
    public static void displayShader(int index)
    {
    	if(selectedShaderIndex == index) return;
    	
    	Minecraft mc = Minecraft.getMinecraft();
		selectedShaderIndex = index;
	
			
        try {
            mc.entityRenderer.theShaderGroup = new ShaderGroup(mc.getTextureManager(), mc.getResourceManager(), mc.getFramebuffer(), shaderResourceLocations[selectedShaderIndex]);
            mc.entityRenderer.theShaderGroup.createBindFramebuffers(mc.displayWidth, mc.displayHeight); 
        }
            
        catch (IOException ioexception)
        {
    		selectedShaderIndex = EntityRenderer.shaderCount;
        }
        catch (JsonSyntaxException jsonsyntaxexception)
        {
    		selectedShaderIndex = EntityRenderer.shaderCount;
        }
    }
    
    public static void disableShader()
    {
    	Minecraft mc = Minecraft.getMinecraft();
		mc.entityRenderer.deactivateShader();
		selectedShaderIndex = EntityRenderer.shaderCount;
    }
	
	
	
}
