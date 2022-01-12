package fr.craftyourliferp.data;

import java.awt.image.BufferedImage;
import java.io.File;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftyourliferp.cosmetics.CosmeticCachedData;
import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.renderer.ImageBufferDownload;
import net.minecraft.client.renderer.ThreadDownloadImageData;
import net.minecraft.client.renderer.texture.ITextureObject;
import net.minecraft.client.renderer.texture.TextureManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.StringUtils;

public class AvatarUpdater {
	
	public enum AvatarType
	{
		SKIN,
		CAPE;
	}
	
	private final static String skinUrl = CraftYourLifeRPMod.apiIp + "/skins";
	private final static String capeUrl = CraftYourLifeRPMod.apiIp + "/capes";

	public EntityPlayer player;
	
	
	public AvatarUpdater(EntityPlayer p)
	{
		player = p;
	}
	
    public Object[] getDownloadImageAvatar(AvatarType type)
    {

        TextureManager texturemanager = Minecraft.getMinecraft().getTextureManager();
        
        ResourceLocation texture = getLocationFromType(type);
                
        Object object = texturemanager.getTexture(texture);


        if(type == AvatarType.SKIN)
        {
    	    object = new ThreadDownloadImageData((File)null, skinUrl + "/" + player.getCommandSenderName() + ".png" , AbstractClientPlayer.locationStevePng, new ImageBufferDownload());
        }
        else
        {	
        	String url = capeUrl + "/" + player.getCommandSenderName() + ".png";
        	
        	if(!exists(url))
        	{
        		Object[] obj = new Object[2];

        		obj[0] = (ThreadDownloadImageData)object;
        		obj[1] = null;
        		
        		return obj;
        	}
        	
        	object = new ThreadDownloadImageData((File)null, url , null, new ImageBufferDownload());
        }


	    texturemanager.loadTexture(texture, (ITextureObject)object);

		Object[] obj = new Object[2];

		obj[0] = (ThreadDownloadImageData)object;
		obj[1] = texture;
		
		return obj;    
	}
    
    public ResourceLocation getLocationFromType(AvatarType type)
    {
    	if(type == AvatarType.SKIN)
    	{
    		return new ResourceLocation(player.getCommandSenderName() + "_skin");
    	}
    	else
    	{
    		return new ResourceLocation(player.getCommandSenderName() + "_cape");
    	}
    }
	
	public void updateAvatar()
	{		
		AbstractClientPlayer absPlayer = (AbstractClientPlayer)player;
		
		Object[] data = getDownloadImageAvatar(AvatarType.CAPE);
		
		ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, absPlayer, (ResourceLocation)data[1], 2);

		data = getDownloadImageAvatar(AvatarType.SKIN);
		
		ReflectionHelper.setPrivateValue(AbstractClientPlayer.class, absPlayer, (ResourceLocation)data[1], 1);
	}
	
	
	public boolean exists(String url){
		
		HttpURLConnection con = null;
		
	    try {
	      HttpURLConnection.setFollowRedirects(false);

	      con = (HttpURLConnection) new URL(url).openConnection();
	      
	      con.setRequestMethod("HEAD");
	      
	      return (con.getResponseCode() == HttpURLConnection.HTTP_OK);
	    }
	    catch (Exception e) {
	       e.printStackTrace();
	       return false;
	    }
	    finally
	    {
	    	if(con != null)
	    	{
	    		con.disconnect();
	    	}
	    }
	    
	}
	



}
