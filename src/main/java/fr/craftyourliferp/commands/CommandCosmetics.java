package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.ExtendedPlayer;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandCosmetics implements ICommand {

    	private final List aliases;
    	    	
    	public CommandCosmetics() {
    		aliases = new ArrayList();
    		aliases.add("cm");
    	}
    	
    	@Override
    	public int compareTo(Object arg0) {
    		return 0;
    	}

    	@Override
    	public String getCommandName() {
    		return "cosmetique";
    	}

    	@Override
    	public String getCommandUsage(ICommandSender var1) {
    		// TODO Auto-generated method stub
    		return "§c/cosmetique unlock <Username> <Id>";
    	}

    	@Override
    	public List getCommandAliases() {
    		return this.aliases;
    	}

    	@Override
    	public void processCommand(ICommandSender sender, String[] args) {
    		World world = sender.getEntityWorld();
    		if(!world.isRemote) {
    			if(args.length == 3)
    			{
    				if(args[0].equalsIgnoreCase("unlock"))
    				{
    					if(!canCommandSenderUseCommand(sender))
    					{
    						sender.addChatMessage(new ChatComponentText("§cPermission Error"));
    						return;
    					}
    					EntityPlayer player = world.getPlayerEntityByName(args[1]);
        				if(player != null)
        				{
        					int id = 0;
        					try
        					{
        						id = Integer.parseInt(args[2]);
        					}
        					catch(Exception e)
        					{
        						e.printStackTrace();
        						return;
        					}
            				CosmeticObject.setCosmetiqueUnlocked(player, id);
        				}
        				else
        				{
        					sender.addChatMessage(new ChatComponentText("§cUtilisateur introuvable!"));
        				}
    				}
    				else if(args[0].equalsIgnoreCase("lock"))
    				{
    					if(!canCommandSenderUseCommand(sender))
    					{
    						sender.addChatMessage(new ChatComponentText("§cPermission Error"));
    						return;
    					}
    					EntityPlayer player = world.getPlayerEntityByName(args[1]);
        				if(player != null)
        				{
        					int id = 0;
        					try
        					{
        						id = Integer.parseInt(args[2]);
        					}
        					catch(Exception e)
        					{
        						e.printStackTrace();
        						return;
        					}
            				CosmeticObject.setCosmetiqueLocked(player, id);
        				}
        				else
        				{
        					sender.addChatMessage(new ChatComponentText("§cUtilisateur introuvable!"));
        				}
    				}
    			}
    			else
    			{
					sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
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

