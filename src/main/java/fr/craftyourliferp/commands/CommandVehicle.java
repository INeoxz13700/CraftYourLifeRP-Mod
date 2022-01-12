package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.driveables.EntityVehicle;

import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class CommandVehicle implements ICommand {
	
	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "voiture";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/voiture";
	}

	@Override
	public List getCommandAliases() {
		List aliases = new ArrayList<String>();
		aliases.add("/vo");
		return aliases;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] arguments) {
		if(arguments.length <= 0)
		{
            throw new WrongUsageException(this.getCommandUsage(sender));
		}
		else 
		{			
			 if(!canCommandSenderUseCommand(sender)) 
			 {
		        return;
		     }
		     else if(arguments[0].matches("give"))
		     {
		    	if(!MinecraftServer.getServer().isDedicatedServer()) 
		    	{
		    		 if(arguments.length == 2 && this.isInteger(arguments[1]) && Integer.parseInt(arguments[1]) > 0) 
		    		 {
		    			 ExtendedPlayer pData = ExtendedPlayer.get((EntityPlayer) sender);
						 int value = Integer.parseInt(arguments[1]);
						 pData.setVcoins(pData.getVcoins() + value);
						 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Vous venez de vous ajoutez " + EnumChatFormatting.AQUA + value + EnumChatFormatting.GREEN + " vcoins!"));
						 
		    		 }
		    		 else
		    		 {
		    			 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Vous devez insérer une valeur > 0"));
		    		 }
		    	}
		    	else 
		    	{
		    		 if(arguments.length >= 2 && arguments[1].matches("vcoins")) 
		    		 {
		    			 if(arguments.length >= 3 && !arguments[2].isEmpty())
		    			 {
		    				 World ServerWorld = MinecraftServer.getServer().getEntityWorld();
		    				 if(isOnline(ServerWorld,arguments[2])) 
		    				 {
		    					 if(arguments.length >= 4 && this.isInteger(arguments[3]) && Integer.parseInt(arguments[3]) > 0) 
		    					 {
		    						 EntityPlayer p = this.getPlayer(ServerWorld, arguments[2]);
		    						 ExtendedPlayer pData = ExtendedPlayer.get(p);
		    						 int value = Integer.parseInt(arguments[3]);
		    						 pData.setVcoins(pData.getVcoins() + value);
		    						 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Vous venez d'ajouter " + EnumChatFormatting.AQUA + value + EnumChatFormatting.GREEN + "à " + EnumChatFormatting.GOLD + p.getCommandSenderName()));
		    					 }
		    					 else 
		    					 {
		    						 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Vous devez entrer un nombre > 0"));
		    					 }
		    				 }
		    				 else 
		    				 {
		    					 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Ce joueur n'existe pas ou n'est pas connecté"));
		    				 }
		    			 }
		    			 else 
		    			 {
		    				 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Vous devez insérer un pseudo"));
		    			 }	
		    		 }
		    	 }
		     }
		     else if(arguments[0].matches("licence"))
		     {
		    	 if(arguments.length == 3)
		    	 {
		    		 if(arguments[1].matches("unlock"))
		    		 {
		    			 if(!arguments[2].isEmpty())
		    			 {
		    		        World serverWorld = sender.getEntityWorld();

		    				EntityPlayer argPlayer = serverWorld.getPlayerEntityByName(arguments[2]);
		    				
		    				if(argPlayer == null) return;
		    				
		    				ExtendedPlayer exPlayer = ExtendedPlayer.get(argPlayer);
		    				
		    				exPlayer.hasLicence = true;
		    			 }
		    		 }
		    	 }
		     }
		     else if(arguments[0].matches("reset")) 
		     {
		    	 World ServerWorld = MinecraftServer.getServer().getEntityWorld();
		    	 
		    	 if(arguments.length >= 2 && isOnline(ServerWorld,arguments[1])) 
		    	 {
		    		 EntityPlayer p = this.getPlayer(ServerWorld, arguments[1]);
		    		 
		    		 ExtendedPlayer pData = ExtendedPlayer.get(p);
		    		 
	    			 pData.ownedVehicle.clear();
	    			 pData.ownedBoat.clear();
	    			 pData.ownedPlane.clear();
	    			 
	    			 pData.ownedSlot = ExtendedPlayer.startOwnedSlot;
	    			 
	    			 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Les donnees du joueur " + EnumChatFormatting.AQUA + p.getCommandSenderName() + EnumChatFormatting.GREEN + " ont étaient reset"));
	    		 }
	    		 else 
	    		 {
	    			 sender.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Ce joueur n'existe pas ou n'est pas connecté"));
	    		 }
		    	 
		     }
		     else if(arguments[0].matches("slots"))
		     {
		    	 if(arguments.length == 4)
		    	 {
			    	 if(arguments[1].matches("set"))
			    	 {
			    		 if(arguments[2].isEmpty())
			    		 {
			    			 return;
			    		 }
			    		 
			    		 EntityPlayer target = sender.getEntityWorld().getPlayerEntityByName(arguments[2]);
			    		 
			    		 if(target == null)
			    		 {
			    			 return;
			    		 }
			    		 
			    		 try
			    		 {
			    			int slot = Integer.parseInt(arguments[3]);
			    			ExtendedPlayer.get(target).setOwnedSlots(slot);
			    			sender.addChatMessage(new ChatComponentText(EnumChatFormatting.AQUA + target.getCommandSenderName()  + EnumChatFormatting.GREEN + "a maintenant " + EnumChatFormatting.AQUA + slot + " slots"));
			    		 }
			    		 catch(Exception e)
			    		 {
			    			 e.printStackTrace();
			    		 }
			    		 
			    	 }
		    	 }
		     }
		}
		  
	}
		
	
	

	@Override
	public boolean canCommandSenderUseCommand(ICommandSender sender) {
		if(sender instanceof EntityPlayer)
		{
    		EntityPlayer player = (EntityPlayer)sender;
    		
    		if(ServerUtils.isOp(player)) 
    		{
    			return true;
    		}
    		
    		return false;
    		
		}
		return true;
	}

	@Override
	public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
		// TODO Auto-generated method stub
		return false;
	}
	
	public static boolean isInteger(String s) {
	    try { 
	        Integer.parseInt(s); 
	    } catch(NumberFormatException e) { 
	        return false; 
	    } catch(NullPointerException e) {
	        return false;
	    }
	    // only got here if we didn't return false
	    return true;
	}
	
	public boolean isOnline(World world, String name) {
		for(Object player : world.playerEntities) {
			EntityPlayer p  = (EntityPlayer) player;
			if(p.getCommandSenderName().equalsIgnoreCase(name))
				return true;
		}
		return false;	
	}
	
	public EntityPlayer getPlayer(World world, String name) {
		for(Object player : world.playerEntities) {
			EntityPlayer p  = (EntityPlayer) player;
			if(p.getCommandSenderName().equalsIgnoreCase(name))
				return p;
		}
		return null;	
	}

}
