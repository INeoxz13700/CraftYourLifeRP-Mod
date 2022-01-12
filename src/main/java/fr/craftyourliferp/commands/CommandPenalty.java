package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.network.PacketOpenGui;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.command.WrongUsageException;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class CommandPenalty implements ICommand {
	
	public static boolean vehicleIsBreakable = false;

	@Override
	public int compareTo(Object arg0) {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String getCommandName() {
		return "penalty";
	}

	@Override
	public String getCommandUsage(ICommandSender p_71518_1_) {
		return "/penalty add [Username] [Price] [Treasury] [Reason]";
	}

	@Override
	public List getCommandAliases() {
		return null;
	}

	@Override
	public void processCommand(ICommandSender sender, String[] args) {
		if(args.length == 0)
		{
            throw new WrongUsageException(this.getCommandUsage(sender));
		}
		else 
		{	
			 if(!canCommandSenderUseCommand(sender)) 
			 {
		        	return;
		     }
			 
			 if(args[0].matches("add"))
			 {
				 if(args[1].isEmpty())
				 {
					 throw new WrongUsageException(this.getCommandUsage(sender));
				 }
				 
				 if(args[2].isEmpty())
				 {
					 throw new WrongUsageException(this.getCommandUsage(sender));
				 }
				 
				 EntityPlayer targetPlayer = sender.getEntityWorld().getPlayerEntityByName(args[1]);
				 EntityPlayer expeditorPlayer = sender.getEntityWorld().getPlayerEntityByName(args[2]);

				 
				 if(targetPlayer == null)
				 {
					 System.out.println("[Penalty] Player not found");
					 return;
				 }
				 
				 if(expeditorPlayer == null)
				 {
					 System.out.println("[Penalty] Player not found");
					 return;
				 }
				 
				 System.out.println("/getplayerdata " + expeditorPlayer.getCommandSenderName());

				 
				 if(!ServerUtils.canPutPenalty(expeditorPlayer))
				 {
					 ServerUtils.sendChatMessage(expeditorPlayer, "§cVous n'avez pas le grade nécessaire pour mettre des amendes");
					 return;
				 }
				 
				 if(args[3].isEmpty())
				 {
					 throw new WrongUsageException(this.getCommandUsage(sender));
				 }
				 
				 if(args[4].isEmpty())
				 {
					 throw new WrongUsageException(this.getCommandUsage(sender));
				 }
				 
				 if(args[5].isEmpty())
				 {
					 throw new WrongUsageException(this.getCommandUsage(sender));
				 }
				 
				 int price;
				 try
				 {
					price = Integer.parseInt(args[3]);
				 }
				 catch(Exception e)
				 {
					 e.printStackTrace();
					 return;
				 }
				 
				 String treasury = args[4];
				 String reason = "";
				 
				 for(int i = 5; i < args.length; i++)
				 {
					 reason += args[i] + " ";
				 }
				 reason = reason.trim();
				 
				 ExtendedPlayer player = ExtendedPlayer.get(targetPlayer);
				 player.PenaltyManager.addPenalty(price, reason,treasury);
				 System.out.println("Penalty added to " + player.getPlayer().getCommandSenderName());
			 }
			 else if(args[0].matches("remove"))
			 {
				 if(args[1].isEmpty())
				 {
					 throw new WrongUsageException(this.getCommandUsage(sender));
				 }
				 
				 EntityPlayer targetPlayer = sender.getEntityWorld().getPlayerEntityByName(args[1]);
				 
				 if(targetPlayer == null)
				 {
					 System.out.println("[Penalty] Player not found");
					 return;
				 }
				 
				 if(args[2].isEmpty())
				 {
					 throw new WrongUsageException(this.getCommandUsage(sender));
				 }
				 
				 String id = args[2];
				 
				 ExtendedPlayer player = ExtendedPlayer.get(targetPlayer);
				 player.PenaltyManager.removePenalty(id);
				 System.out.println("Penalty removed for " + player.getPlayer().getCommandSenderName());
			 }
			 else if(args[0].matches("gui"))
			 {
				 if(args[1].isEmpty())
				 {
					 return;
				 }
				 
				 EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[1]);
				 
				 if(player == null)
				 {
					 return;
				 }
				 
				 FlansMod.getPacketHandler().sendTo(new PacketOpenGui((byte)0), (EntityPlayerMP)player);
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

}
