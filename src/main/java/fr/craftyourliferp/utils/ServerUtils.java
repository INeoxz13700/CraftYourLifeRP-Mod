package fr.craftyourliferp.utils;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.simpleimpl.SimpleNetworkWrapper;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketBase;
import fr.craftyourliferp.network.PacketMessageDisplay;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import net.minecraft.world.WorldServer;

public class ServerUtils 
{
	
	
	public static boolean isOp(EntityPlayer player)
	{ 
		
		if(player == null) return false;
		
		return FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(player.getGameProfile());
	}
	
	public static boolean isIlegalJob(String jobName)
	{
		if(jobName == null) return false;
		
		return jobName.equalsIgnoreCase("voleur") || jobName.equalsIgnoreCase("rebelle") || jobName.equalsIgnoreCase("psychopathe") || jobName.equalsIgnoreCase("terroriste") || jobName.equalsIgnoreCase("hacker");
	}
	
	public static boolean isForceOrder(String jobName)
	{
		if(jobName == null) return false;
		
		return jobName.equalsIgnoreCase("gendarme") || jobName.equalsIgnoreCase("policier") || jobName.equalsIgnoreCase("bac") || jobName.equalsIgnoreCase("gign") || jobName.equalsIgnoreCase("douanier") || jobName.equalsIgnoreCase("militaire") || jobName.equalsIgnoreCase("pompier") ||  jobName.equalsIgnoreCase("medecin");
	}
	
	public static boolean isCounted(String jobName)
	{
		if(jobName == null)
		{
			return false;
		} 
		return jobName.equalsIgnoreCase("gendarme") || jobName.equalsIgnoreCase("policier") || jobName.equalsIgnoreCase("gign")  || jobName.equalsIgnoreCase("militaire") || jobName.equalsIgnoreCase("bac");
	}

	
	/*public static int getForceOrderCount(World world)
	{
		int count = 0;
		for(EntityPlayer player : (List<EntityPlayer>)world.playerEntities)
		{
			PlayerCachedData cachedData = PlayerCachedData.getData(player);
			if(isCounted(cachedData.serverData.job)) count++;
		}
		return count;
	}*/
	
	public static int getForceOrderCount(World world)
	{
		int count = 0;
		for(EntityPlayer player : (List<EntityPlayer>)world.playerEntities)
		{
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
			if(isCounted(extendedPlayer.serverData.job)) count++;
		}
		return count;
	}
	
	/*public static boolean canUseBelier(EntityPlayer player)
	{
		PlayerCachedData cachedData = PlayerCachedData.getData(player);
		ExtendedPlayer ep = ExtendedPlayer.get(player);

		if((System.currentTimeMillis() - ep.lastBelierUseTime) / 1000 >= ExtendedPlayer.belierUseTime)
		{
			
			if(cachedData.serverData.job.equalsIgnoreCase("policier") || cachedData.serverData.job.equalsIgnoreCase("gendarme"))
			{
				if(cachedData.serverData.grade >= 3)
				{
					return true;
				}
			}
			
			if(cachedData.serverData.job.equalsIgnoreCase("bac"))
			{
				return true;
			}
			
			if(cachedData.serverData.grade >= 2 && (cachedData.serverData.job.equalsIgnoreCase("gign") || cachedData.serverData.job.equalsIgnoreCase("militaire") || cachedData.serverData.job.equalsIgnoreCase("pompier")))
			{
				return true;
			}
			
			return false;
		}
		else
		{
			long timeLeft = ep.belierUseTime - (System.currentTimeMillis() - ep.lastBelierUseTime) / 1000;
			ServerUtils.sendChatMessage(player, "§cVous devez attendre §4" + ((timeLeft / 60) + 1)  + " minutes");
		}
		
		return false;
	}*/
	
	public static boolean canUseBelier(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

		if((System.currentTimeMillis() - extendedPlayer.lastBelierUseTime) / 1000 >= ExtendedPlayer.belierUseTime)
		{
			
			if(extendedPlayer.serverData.job.equalsIgnoreCase("policier") || extendedPlayer.serverData.job.equalsIgnoreCase("gendarme"))
			{
				if(extendedPlayer.serverData.grade >= 3)
				{
					return true;
				}
			}
			
			if(extendedPlayer.serverData.job.equalsIgnoreCase("bac"))
			{
				return true;
			}
			
			if(extendedPlayer.serverData.grade >= 2 && (extendedPlayer.serverData.job.equalsIgnoreCase("gign") || extendedPlayer.serverData.job.equalsIgnoreCase("militaire") || extendedPlayer.serverData.job.equalsIgnoreCase("pompier")))
			{
				return true;
			}
			
			return false;
		}
		else
		{
			long timeLeft = extendedPlayer.belierUseTime - (System.currentTimeMillis() - extendedPlayer.lastBelierUseTime) / 1000;
			ServerUtils.sendChatMessage(player, "§cVous devez attendre §4" + ((timeLeft / 60) + 1)  + " minutes");
		}
		
		return false;
	}
	
	/*public static boolean canUseStopStick(EntityPlayer player)
	{
		PlayerCachedData cachedData = PlayerCachedData.getData(player);

		if(cachedData.serverData.grade >= 2 && (cachedData.serverData.job.equalsIgnoreCase("bac") || cachedData.serverData.job.equalsIgnoreCase("gign") || cachedData.serverData.job.equalsIgnoreCase("militaire") || cachedData.serverData.job.equalsIgnoreCase("policier") || cachedData.serverData.job.equalsIgnoreCase("gendarme") || cachedData.serverData.job.equalsIgnoreCase("douanier")))
		{
			return true;
		}
			
		return false;
	}*/
	
	public static boolean canUseStopStick(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

		if(extendedPlayer.serverData.grade >= 2 && (extendedPlayer.serverData.job.equalsIgnoreCase("bac") || extendedPlayer.serverData.job.equalsIgnoreCase("gign") || extendedPlayer.serverData.job.equalsIgnoreCase("militaire") || extendedPlayer.serverData.job.equalsIgnoreCase("policier") || extendedPlayer.serverData.job.equalsIgnoreCase("gendarme") || extendedPlayer.serverData.job.equalsIgnoreCase("douanier")))
		{
			return true;
		}
			
		return false;
	}

	
	/*public static boolean canUseImpound(EntityPlayer player)
	{
		PlayerCachedData cachedData = PlayerCachedData.getData(player);

		if(cachedData.serverData.job.equalsIgnoreCase("douanier") && cachedData.serverData.grade >= 3)
		{
			return true;
		}
		
		if(cachedData.serverData.grade >= 2 && (cachedData.serverData.job.equalsIgnoreCase("policier") || cachedData.serverData.job.equalsIgnoreCase("gendarme")))
		{
			return true;
		}
		
		return false;
	}*/
	
	public static boolean canUseImpound(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

		if(extendedPlayer.serverData.job.equalsIgnoreCase("douanier") && extendedPlayer.serverData.grade >= 3)
		{
			return true;
		}
		
		if(extendedPlayer.serverData.grade >= 2 && (extendedPlayer.serverData.job.equalsIgnoreCase("policier") || extendedPlayer.serverData.job.equalsIgnoreCase("gendarme")))
		{
			return true;
		}
		
		return false;
	}
	
	/*public static boolean canPutPenalty(EntityPlayer player)
	{
		PlayerCachedData cachedData = PlayerCachedData.getData(player);

		if(cachedData.serverData.job.equalsIgnoreCase("bac"))
		{
			return true;
		}
		
		if(cachedData.serverData.job.equalsIgnoreCase("douanier") && cachedData.serverData.grade >= 3)
		{
			return true;
		}
		
		if(cachedData.serverData.grade >= 2 && (cachedData.serverData.job.equalsIgnoreCase("policier") || cachedData.serverData.job.equalsIgnoreCase("gendarme") || cachedData.serverData.job.equalsIgnoreCase("gign") || cachedData.serverData.job.equalsIgnoreCase("militaire")))
		{
			return true;
		}
		
		return false;
	}*/
	
	public static boolean canPutPenalty(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

		if(extendedPlayer.serverData.job.equalsIgnoreCase("bac"))
		{
			return true;
		}
		
		if(extendedPlayer.serverData.job.equalsIgnoreCase("douanier") && extendedPlayer.serverData.grade >= 3)
		{
			return true;
		}
		
		if(extendedPlayer.serverData.grade >= 2 && (extendedPlayer.serverData.job.equalsIgnoreCase("policier") || extendedPlayer.serverData.job.equalsIgnoreCase("gendarme") || extendedPlayer.serverData.job.equalsIgnoreCase("gign") || extendedPlayer.serverData.job.equalsIgnoreCase("militaire")))
		{
			return true;
		}
		
		return false;
	}
	
	public static void sendChatMessage(ICommandSender sender, String msg)
	{
		sender.addChatMessage(new ChatComponentText(msg));
	}
	

	/*
	 * 0: hudmessage
	 * 1: hudsubmessage
	 * 2: notification
	 * 3: chatmessage
	 */
	public static void broadcastMessage(String msg, byte type)
	{
		if(type == 3)
		{
			for(Object obj : MinecraftServer.getServer().getEntityWorld().playerEntities)
			{
				EntityPlayer player = (EntityPlayer)obj;
				player.addChatMessage(new ChatComponentText(msg));
			}
		}
		else
		{
			PacketMessageDisplay packet = new PacketMessageDisplay(msg,1000,type);
			CraftYourLifeRPMod.packetHandler.sendToAll(packet);
		}
	}
	
	public static void sendMessage(String msg, EntityPlayer to, int duration, int type)
	{	
		PacketMessageDisplay packet = new PacketMessageDisplay(msg,duration,(byte)type);
		CraftYourLifeRPMod.packetHandler.sendTo(packet, (EntityPlayerMP)to);
	}
	
    public static void removeItemsFromInventory(List<ItemStack> toRemove, EntityPlayer player)
    {
    	for(ItemStack isToRemove : toRemove)
    	{
    		for(Object obj : player.inventoryContainer.inventorySlots)
    		{
    			Slot slot = (Slot) obj;
    			ItemStack isInv = slot.getStack();
    			
    			if(isInv == null) continue;
    			
    			if(isToRemove.getItem() == isInv.getItem() && isToRemove.getItemDamage() == isInv.getItemDamage())
    			{
    				if(isInv.stackSize >= isToRemove.stackSize)
    				{
	    				if(isInv.stackSize - isToRemove.stackSize == 0)
	    				{
	    					player.inventory.setInventorySlotContents(slot.getSlotIndex(), null);
	    					break;
	    				}
	    				else
	    				{

	    					isInv.stackSize -= isToRemove.stackSize;
	    					break;
	    				}
    				}
    				else
    				{
    					isToRemove.stackSize -= isInv.stackSize;
    					player.inventory.setInventorySlotContents(slot.getSlotIndex(), null);
    				}
    			}
    			
    		}
    	}
    }
    
    public static boolean playerHaveItemsInInventory(List<ItemStack> toRemove, EntityPlayer player)
    {
    	List<ItemStack> isPlayerHave = new ArrayList();
    	
    	for(ItemStack isToRemove : toRemove)
    	{
    		int quantity = isToRemove.stackSize;
    		for(Object obj : player.inventoryContainer.inventorySlots)
    		{
    			Slot slot = (Slot) obj;
    			ItemStack isInv = slot.getStack();
    			
    			if(isInv == null) continue;
    			    			
    			if(isToRemove.getItem() == isInv.getItem() && isToRemove.getItemDamage() == isInv.getItemDamage())
    			{		
    				if(isInv.stackSize >= quantity)
    				{
	    				if(isInv.stackSize - quantity == 0)
	    				{
	    					isPlayerHave.add(isToRemove);
	    					break;
	    				}
	    				else
	    				{

	    					isPlayerHave.add(isToRemove);
	    					break;
	    				}
    				}
    				else
    				{
    					quantity -= isInv.stackSize;
    				}
    			}
    		}
    	}
    	
    	return isPlayerHave.size() == toRemove.size();
    }
    
    public static void addItemStacksToInventory(EntityPlayer player, List<ItemStack> itemstacks)
    {
    	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
    	
    	for(ItemStack is : itemstacks)
    	{
    		if(!player.inventory.addItemStackToInventory(is))
    		{
    			extendedPlayer.itemStockage.add(is);
    			ServerUtils.sendChatMessage(player, "§cVotre inventaire est full!");
    			ServerUtils.sendChatMessage(player, "§cItem ajouté dans votre stockage du marché noir");
    			ServerUtils.sendChatMessage(player, "§cPrenez un screen de ce message (Preuve de remboursement)");
    		}
    	}
    }
    
    public static void setLore(ItemStack is, List<String> loreData)
    {
        if (is.stackTagCompound == null)
        {
            is.stackTagCompound = new NBTTagCompound();
        }

        if (!is.stackTagCompound.hasKey("display", 10))
        {
            is.stackTagCompound.setTag("display", new NBTTagCompound());
        }
        
        NBTTagList lores = new NBTTagList();
        for(String lore : loreData)
        {
        	lores.appendTag(new NBTTagString(lore));
        }

        is.stackTagCompound.getCompoundTag("display").setTag("Lore", lores);
    }
    
    public static List<String> getLore(ItemStack is)
    {
    	if(is.stackTagCompound == null)
    	{
    		return new ArrayList<String>();
    	}
    	
    	List<String> loresList = new ArrayList();
    	
    	if (is.stackTagCompound.hasKey("display", 10))
        {
    		NBTTagCompound display = (NBTTagCompound) is.stackTagCompound.getTag("display");
    		
            NBTTagList lores = (NBTTagList)display.getTag("Lore");
       
            if(lores == null)
            {
            	return new ArrayList<String>();
            }
            
            for(int i = 0; i < lores.tagCount(); i++)
            {
            	String lore = lores.getStringTagAt(i);
            	loresList.add(lore);
            }
            
            return loresList;
        }
    	
    	return new ArrayList<String>();
    }
    
    public static boolean areItemStacksEquals(ItemStack is1, ItemStack is2)
    {
    	return is1.stackSize == is2.stackSize && is1.getItem() == is2.getItem();
    }
    
    public static void takeMoney(EntityPlayer player,float money)
    {
		System.out.println("/eco take " + player.getCommandSenderName() + " " + String.format("%f",money).replace(",", "."));
    }
    
    public static void addMoney(EntityPlayer player,float money)
    {
		System.out.println("/eco give " + player.getCommandSenderName() + " " + String.format("%f",money).replace(",", "."));
    }
    
    /*public static boolean playerHaveMoney(EntityPlayer player,float money)
    {
    	PlayerCachedData data = PlayerCachedData.getData(player);
    	if(data.serverData.money >= money)
    	{
    		return true;
    	}
    	
    	return false;
    }*/
    
    public static boolean playerHaveMoney(EntityPlayer player,float money)
    {
    	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
    	if(extendedPlayer.serverData.money >= money)
    	{
    		return true;
    	}
    	
    	return false;
    }
    
    public static void syncShieldToSpigot(String username, float shield)
    {
		System.out.println("/setshield " + username + " " + shield);
    }
    
    /*public static String getMoneyDisplay(String format,EntityPlayer player)
    {
    	PlayerCachedData data = PlayerCachedData.getData(player);
    	return String.format(format,data.serverData.money);
    }*/
    
    public static String getMoneyDisplay(String format,EntityPlayer player)
    {
    	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
    	return String.format(format,extendedPlayer.serverData.money);
    }
    
    public static String getMoneyDisplay(String format,float money)
    {
    	return String.format(format,money);
    }
    
    /*public static int getPlayerCountForJob(World world, String jobName)
    {
    	int count = 0;
    	for(EntityPlayer player : (List<EntityPlayer>)world.playerEntities)
    	{
    		if(PlayerCachedData.getData(player) != null)
    		{
    			PlayerCachedData cachedData = PlayerCachedData.getData(player);
    			if(cachedData.serverData != null &&  cachedData.serverData.job != null && cachedData.serverData.job.equalsIgnoreCase(jobName)) count++;
    		}
    	}
    	return count;
    }*/
    
    public static int getPlayerCountForJob(World world, String jobName)
    {
    	int count = 0;
    	for(EntityPlayer player : (List<EntityPlayer>)world.playerEntities)
    	{
        	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

    		if(extendedPlayer != null)
    		{
    			if(extendedPlayer.serverData != null &&  extendedPlayer.serverData.job != null && extendedPlayer.serverData.job.equalsIgnoreCase(jobName)) count++;
    		}
    	}
    	return count;
    }
    
    public static World getWorldFromName(String name)
    {
    	MinecraftServer server = MinecraftServer.getServer();
    	WorldServer[] worlds = server.worldServers;
    	for(WorldServer world : worlds)
    	{
    		if(world.getWorldInfo().getWorldName().equals(name))
    		{
    			return world;
    		}
    	}
    	return null;
    }
    
	
}
