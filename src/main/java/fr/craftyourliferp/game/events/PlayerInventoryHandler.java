package fr.craftyourliferp.game.events;

import java.util.List;
import java.util.concurrent.Callable;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.eventhandler.Event.Result;
import cpw.mods.fml.common.gameevent.PlayerEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.ItemPickupEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiInventoryRP;
import fr.craftyourliferp.items.ItemBulletproofShield;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.stats.AchievementList;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ReportedException;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;

public class PlayerInventoryHandler {

	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void playerTickEventClient(PlayerTickEvent event)
	{
		if(event.phase == TickEvent.Phase.START) return;
		
		EntityPlayer player = event.player;
		if(player.capabilities.isCreativeMode) return;

		if(Minecraft.getMinecraft().thePlayer == player)
		{	
			if(player.getHeldItem() != null)
			{

					if(player.getHeldItem().getItem() == ModdedItems.itemBulletproofShield)
					{
						if(CraftYourLifeRPClient.cachedData.slotEquippedShield == -1)
						{
							if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1 || player.ticksExisted - CraftYourLifeRPClient.cachedData.ticksChangeSlot < 20*2)
							{
								if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1) CraftYourLifeRPClient.cachedData.ticksChangeSlot = player.ticksExisted;
								
								if(player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.previousSelectedSlot) != null &&  player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.previousSelectedSlot).getItem() == ModdedItems.itemBulletproofShield)
								{
									CraftYourLifeRPClient.cachedData.previousSelectedSlot++;
								}
								player.inventory.currentItem = CraftYourLifeRPClient.cachedData.previousSelectedSlot;
								event.player.addChatMessage(new ChatComponentText("§cVous devez attendre 2 secondes pour équipper le bouclier"));
							}
							else
							{
								CraftYourLifeRPClient.cachedData.slotEquippedShield = player.inventory.currentItem;
								CraftYourLifeRPClient.cachedData.ticksChangeSlot = -1;
							}
						}
					}
			}
				
			if(CraftYourLifeRPClient.cachedData != null && CraftYourLifeRPClient.cachedData.slotEquippedShield != -1)
			{
					if(player.getHeldItem() == null || player.getHeldItem().getItem() != ModdedItems.itemBulletproofShield)
					{
						if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1 || player.ticksExisted - CraftYourLifeRPClient.cachedData.ticksChangeSlot < 20*2)
						{
							if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1) CraftYourLifeRPClient.cachedData.ticksChangeSlot = player.ticksExisted;
							player.inventory.currentItem = CraftYourLifeRPClient.cachedData.slotEquippedShield;
							event.player.addChatMessage(new ChatComponentText("§cVous devez attendre 2 secondes pour changer d'item"));
							
							if(player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.slotEquippedShield) == null || player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.slotEquippedShield).getItem() != ModdedItems.itemBulletproofShield)
							{
								CraftYourLifeRPClient.cachedData.slotEquippedShield = -1;
								CraftYourLifeRPClient.cachedData.ticksChangeSlot = -1;
							}
						}
						else
						{
							CraftYourLifeRPClient.cachedData.slotEquippedShield = -1;
							CraftYourLifeRPClient.cachedData.ticksChangeSlot = -1;
						}
					}
			}
				
			if(CraftYourLifeRPClient.cachedData != null) CraftYourLifeRPClient.cachedData.previousSelectedSlot = player.inventory.currentItem;

		}

	}
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onInventoryOpen(GuiOpenEvent event)
	{
		Minecraft mc = Minecraft.getMinecraft();
		if(event.gui instanceof GuiInventory)
		{
			event.setCanceled(true);
			mc.displayGuiScreen(new GuiInventoryRP(mc.thePlayer));
			if(mc.thePlayer.openContainer instanceof ContainerPlayer)
			{
				for(int j = 0; j < 5; j++)
				{
					Slot slot = (Slot) mc.thePlayer.inventoryContainer.inventorySlots.get(j);
					if(j < 5)
					{
						slot.xDisplayPosition = -2000;
						slot.yDisplayPosition = -2000;
					}
				}
			}
		}
	}
	
    public int getFirstEmptyStack(InventoryPlayer inventory)
    {
        for (int i = 0; i < inventory.mainInventory.length; ++i)
        {
        	if(i == 0) continue;
        	
            if (inventory.mainInventory[i] == null)
            {
                return i;
            }
        }

        return -1;
    }
 
    @SubscribeEvent
    public void playerCheckInventoryEvent(PlayerTickEvent event)
    {
    	if(event.phase == TickEvent.Phase.END) return;
    	
    	if(event.player.worldObj.isRemote) return;
    	
    	if(event.player.capabilities.isCreativeMode) return;
    	
    	Slot bulletproofShieldSlot = (Slot) event.player.inventoryContainer.inventorySlots.get(36);
    	if(bulletproofShieldSlot.getHasStack())
    	{
    		
    		if(!(bulletproofShieldSlot.getStack().getItem() instanceof ItemBulletproofShield))
    		{
    			int slotIndex = getFirstEmptyStack(event.player.inventory);
    			if(slotIndex != -1)
    			{
    	        	event.player.inventory.setInventorySlotContents(slotIndex, bulletproofShieldSlot.getStack());
    	        	event.player.inventory.mainInventory[0] = null;
    			}
    			else
    			{
    				event.player.dropItem(bulletproofShieldSlot.getStack().getItem(), bulletproofShieldSlot.getStack().stackSize);
    	        	event.player.inventory.mainInventory[0] = null;
    			}
    		}
    	}
    	
    	if(event.player.ticksExisted % 20 == 0)
    	{
    		boolean flag = false;
    		if(event.player.inventory.mainInventory[0] == null)
    		{
    			for(int i = 1; i < event.player.inventory.mainInventory.length; i++)
        		{    			
        			ItemStack is = event.player.inventory.mainInventory[i];
        			
        			if(is == null) continue;
        			
        			if(is.getItem() instanceof ItemBulletproofShield)
        			{
        				if(!flag)
        				{
        					event.player.inventory.setInventorySlotContents(0, is);
                 	        event.player.inventory.mainInventory[i] = null;
                 	        flag = true;
        				}
        				else
        				{
        					event.player.dropItem(is.getItem(), is.stackSize);
                	        event.player.inventory.mainInventory[i] = null;
        				}
        			}
        		}
    		}
    		else
    		{
    			for(int i = 1; i < event.player.inventory.mainInventory.length; i++)
        		{    			
        			ItemStack is = event.player.inventory.mainInventory[i];
        			
        			if(is == null) continue;
        			
        			if(is.getItem() instanceof ItemBulletproofShield)
        			{
            	        event.player.dropItem(is.getItem(), is.stackSize);
            	        event.player.inventory.mainInventory[i] = null;
        			}
        		}
    		}
    		
    	}
    
    }


	

	
}
