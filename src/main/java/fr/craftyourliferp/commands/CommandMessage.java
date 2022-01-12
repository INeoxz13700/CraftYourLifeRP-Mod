package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketMessageDisplay;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandMessage implements ICommand{

	    	private final List aliases;
	    	    	
	    	public CommandMessage() {
	    		aliases = new ArrayList();
	    		aliases.add("hudm");
	    	}
	    	
	    	@Override
	    	public int compareTo(Object arg0) {
	    		// TODO Auto-generated method stub
	    		return 0;
	    	}

	    	@Override
	    	public String getCommandName() {
	    		// TODO Auto-generated method stub
	    		return "hudmessage";
	    	}

	    	@Override
	    	public String getCommandUsage(ICommandSender var1) {
	    		// TODO Auto-generated method stub
	    		return "§chudmessage <durée> <pseudo> <type> <message>";
	    	}

	    	@Override
	    	public List getCommandAliases() {
	    		// TODO Auto-generated method stub
	    		return this.aliases;
	    	}

	    	@Override
	    	public void processCommand(ICommandSender sender, String[] args) {
	    		World world = sender.getEntityWorld();
	    		
	    		
	    		if(!world.isRemote) {
	    			
	    			if(args.length == 0)
	    			{
						sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
						return;
	    			}
	    			
	    			if(args.length >= 4 && !args[0].isEmpty()) {
	    				int ms = 0;
	    				
	    				try 
    					{
    						ms = Integer.parseInt(args[0]);
    					}
    					catch(NumberFormatException e)
    					{
    						e.printStackTrace();
    						sender.addChatMessage(new ChatComponentText("§aInsérez une durée (chiffre)"));
    						return;
    					}
	    				
	    				
	    				if(!args[1].isEmpty())
	    				{
	    					EntityPlayer victim = world.getPlayerEntityByName(args[1]);
	    					
	    					
	    					if(victim == null)
	    					{
		    					sender.addChatMessage(new ChatComponentText("§aCe joueur n'existe pas ou n'est pas connecté!"));
		    					return;
	    					}
	    					
	    					if(!args[2].isEmpty())
		    				{
	    						int type = Integer.parseInt(args[2]);
	    						try
	    						{
	    							StringBuilder builder = new StringBuilder();
	    							
	    							if(args[3].isEmpty()) 
	    							{
	    								sender.addChatMessage(new ChatComponentText("§aInsérez un message"));
	    								return;
	    							}

	    							
    		        				for (int i = 3; i < args.length; i++) {
    		        				   builder.append(args[i]).append(" ");
    		        				}
    		        				String msg = builder.toString();
    		        				
	    							if(type == 0)
	    							{
	    		        				CraftYourLifeRPMod.packetHandler.sendTo(new PacketMessageDisplay(msg, ms,(byte)type), (EntityPlayerMP) victim);
	    							}
	    							else if(type == 1)
	    							{
	    		        				CraftYourLifeRPMod.packetHandler.sendTo(new PacketMessageDisplay(msg, ms,(byte)type), (EntityPlayerMP) victim);
	    							}
	    						}
	    						catch(NumberFormatException e)
	        					{
	        						e.printStackTrace();
	        						sender.addChatMessage(new ChatComponentText("§aInsérez un type (chiffre)"));
	        						return;
	        					}
		    				}
		    				else
		    				{
		    	
		    	    			sender.addChatMessage(new ChatComponentText("§CVous devez insérer un message"));
		    	    			return;
		    	    			
		    				}

	    			}
    				else
    				{
    					sender.addChatMessage(new ChatComponentText("§cVous devez insérer un pseudo"));
    				}

	    		}
	    		
	    	}
	   	}
	    	

	    	@Override
	    	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
	    		if(p_71519_1_ instanceof EntityPlayer)
	    		{
		    		EntityPlayer player = (EntityPlayer)p_71519_1_;
		    		
		    		if(MinecraftServer.getServer().getConfigurationManager().func_152596_g(player.getGameProfile())) 
		    		{
		    			return true;
		    		}
		    		
		    		return false;
		    		
	    		}
	    		return true;
	    	}

	    	@Override
	    	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
	    		return null;
	    	}

	    	@Override
	    	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
	    		return false;
	    	}
	    	
}



