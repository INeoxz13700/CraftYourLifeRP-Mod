package fr.craftyourliferp.ingame.gui;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketMessageDisplay;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

public class CylrpMessageHUD {
	
	public String msgDisplaying = "";
	
    private int msgPlayingUpFor = 0;
    
    private byte type;
            
    private static CylrpMessageHUD currentDisplayingMessage;
    
    private static CylrpMessageHUD currentDisplayingSubMessage;

    
    
    public static void sendMessage(String msg, int time, byte type)
    {
    	CylrpMessageHUD message = new CylrpMessageHUD();
    	
    	message.msgDisplaying = msg.replaceAll("&", "\u00a7");
    	message.msgPlayingUpFor = time;
    	message.type = type;
    	
    	if(type == 0)
    	{
    		currentDisplayingMessage = message;
    	}
    	else
    	{
    		currentDisplayingSubMessage = message; 
    	}
    }

    
     @SubscribeEvent
	 @SideOnly(Side.CLIENT)
	 public void onGuiIngameRender(RenderGameOverlayEvent.Chat event)
	 {
	    	int width = event.resolution.getScaledWidth(), height = event.resolution.getScaledHeight();
	    	
	        
	    	if (currentDisplayingMessage != null)
	        {
	    		if(currentDisplayingMessage.msgPlayingUpFor > 0)
	    		{
		    		currentDisplayingMessage.msgPlayingUpFor--;
		    		
		            String message = "\u00a76[\u00a7eCYLRP\u00a76] " + currentDisplayingMessage.msgDisplaying;
		            
		           
		            int x = width/ 2;
		            Minecraft.getMinecraft().fontRenderer.drawStringWithShadow(message, x - Minecraft.getMinecraft().fontRenderer.getStringWidth(message) / 2, 15,0);
	      
	    		}
	    		else
	    		{
	    			currentDisplayingMessage = null;
	    		}
	        }

	    	
	    	if (currentDisplayingSubMessage != null)
	    	{
		    	if (currentDisplayingSubMessage.msgPlayingUpFor > 0)
		        {
		    		currentDisplayingSubMessage.msgPlayingUpFor--;
		            GuiUtils.renderCenteredText(currentDisplayingSubMessage.msgDisplaying, width / 2, height - 65);
		        }
		    	else
		    	{
		    		currentDisplayingSubMessage = null;
		    	}
	    	}
	}
    
    
    
    
 
    
}

