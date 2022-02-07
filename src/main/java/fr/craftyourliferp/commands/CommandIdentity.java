package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketMessageDisplay;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.network.rcon.RConConsoleSource;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandIdentity implements ICommand {


    	private final List aliases;
    	    	
    	public CommandIdentity() {
    		aliases = new ArrayList();
    		aliases.add("id");
    	}
    	
    	@Override
    	public int compareTo(Object arg0) {
    		return 0;
    	}

    	@Override
    	public String getCommandName() {
    		return "identity";
    	}

    	@Override
    	public String getCommandUsage(ICommandSender var1) {
    		return "identity help";
    	}

    	@Override
    	public List getCommandAliases() {
    		return this.aliases;
    	}

    	@Override
    	public void processCommand(ICommandSender sender, String[] args) 
    	{
    		World world = sender.getEntityWorld();
    		
  
    		
    		if(!world.isRemote) 
    		{
    			if(args.length >= 1 && args[0].equalsIgnoreCase("reset")) 
    			{
    		  		if(!this.canUseCommand(sender))
    	    		{
    	    			return;
    	    		}
    		  		
    				if(args.length >= 2 && !args[1].isEmpty())
    				{
    					EntityPlayerMP victim = (EntityPlayerMP)world.getPlayerEntityByName(args[1]);
    					 
    					if(victim == null)
    					{
    						ServerUtils.sendChatMessage(sender, "§CJoueur introuvable");
    					}
    					
    					ExtendedPlayer ep = ExtendedPlayer.get(victim);
    					ep.identityData.name = null;
    					victim.playerNetServerHandler.kickPlayerFromServer("§cChangez de nom RP!");
    				}
    				else
    				{
        				sender.addChatMessage(new ChatComponentText("§cle joueur n'est pas connecté ou inexistant"));
    				}
    			}
    			else if(args.length >= 1 && args[0].equalsIgnoreCase("name")) 
    			{
    				if(args.length >= 2 && !args[1].isEmpty())
    				{
    					
    					if(sender instanceof EntityPlayer)
    					{
    						//PlayerCachedData data = PlayerCachedData.getData((EntityPlayer)sender);
    						ExtendedPlayer data = ExtendedPlayer.get((EntityPlayer)sender);

    						if(data.serverData == null || data.serverData.job == null || !data.serverData.job.equals("maire"))
    						{
    							ServerUtils.sendChatMessage(sender, "§cVous devez être maire pour utiliser cette commande");
    							return;
    						}
    					}
    					
    					EntityPlayerMP victim = (EntityPlayerMP)world.getPlayerEntityByName(args[1]);
    					
    					if(victim == null)
    					{
    						ServerUtils.sendChatMessage(sender, "§CJoueur introuvable");
    						return;
    					}
    					
    					if(args[2].isEmpty())
    					{
    						ServerUtils.sendChatMessage(sender, "§c/identity name <Pseudo> <Prénom> <nom>");
    						return;
    					}
    					else if(args[3].isEmpty())
    					{
    						ServerUtils.sendChatMessage(sender, "§c/identity name <Pseudo> <Prénom> <nom>");
    						return;
    					}
    					
    					String name = args[3];
    					String lastname = args[2];
    					ExtendedPlayer ep = ExtendedPlayer.get(victim);
    					ep.identityData.name = name;
    					ep.identityData.lastname = lastname;
    					System.out.println("/bidentity " + victim.getCommandSenderName() + " " + lastname + " " + name);


    					ServerUtils.sendChatMessage(sender, "§aPrénom changé avec succès");
    					ServerUtils.sendChatMessage(victim, "§aVous vous appelez maintenant §6" + ep.identityData.lastname + " " + ep.identityData.name);

    				}
    				else
    				{
        				sender.addChatMessage(new ChatComponentText(ChatFormatting.RED + "le joueur n'est pas connecté ou inexistant"));
    				}
    			}
    			else if(args[0].equalsIgnoreCase("help"))
    			{
    				displayHelp(sender);
    			}
    			else
    			{
    				sender.addChatMessage(new ChatComponentText(ChatFormatting.RED + "/identity help"));
    				return;
    			}
    		}
    	}
    	

    	@Override
    	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
    		return true;
    	}
    	
    	public boolean canUseCommand(ICommandSender sender)
    	{
    		if(sender instanceof EntityPlayer)
    		{
    			if(ServerUtils.isOp((EntityPlayer)sender))
    			{
    				return true;
    			}
    			
    			return false;
    		}
    		
    		return true;
    	}
    	
    	public void displayHelp(ICommandSender sender)
    	{
    		sender.addChatMessage(new ChatComponentText("§aHelp:"));
    		sender.addChatMessage(new ChatComponentText("§a/identity reset <Pseudo>"));
    		sender.addChatMessage(new ChatComponentText("§a/identity name <Pseudo> <Prénom> <Nom>"));
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

