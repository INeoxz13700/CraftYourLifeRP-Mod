package fr.craftyourliferp.game.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.items.IItemPress;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketCustomInterract;
import net.minecraft.client.Minecraft;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraftforge.client.event.MouseEvent;

@SideOnly(Side.CLIENT)
public class MouseListener {
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void onMouseRightClick(MouseEvent event)
	{
		if(event.button == 1)
		{
			Minecraft mc = Minecraft.getMinecraft();
			
			if(CraftYourLifeRPClient.cachedData.isUsingItem())
			{
				if(!event.buttonstate) //is Released
				{
					IItemPress item = (IItemPress)CraftYourLifeRPClient.cachedData.usedItem.getItem();
	                item.onItemStopUsing(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, CraftYourLifeRPClient.cachedData.usedItem, CraftYourLifeRPClient.cachedData.itemPressTicks);
					CraftYourLifeRPClient.cachedData.setItemReleased();
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketCustomInterract.syncMouseIsReleased());              
	                return;
				}
			}
			
			if(mc.objectMouseOver != null && mc.objectMouseOver.typeOfHit != null && mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK)
    		{
                ItemStack itemstack = mc.thePlayer.inventory.getCurrentItem();
                if(itemstack != null && itemstack.getItem() == ModdedItems.itemCrowbar)
                {
	        		int i =  mc.objectMouseOver.blockX;
	                int j =  mc.objectMouseOver.blockY;
	                int k =  mc.objectMouseOver.blockZ; 
	                CraftYourLifeRPMod.packetHandler.sendToServer(new PacketCustomInterract(i, j, k, mc.objectMouseOver.sideHit));              
	            }
    		}
		}
	}
	
	@SubscribeEvent(priority=EventPriority.HIGHEST)
	@SideOnly(Side.CLIENT)
	public void onClientTick(ClientTickEvent event)
	{
		if(event.phase == Phase.END)
		{
			if(Minecraft.getMinecraft().theWorld != null && Minecraft.getMinecraft().currentScreen != null && CraftYourLifeRPClient.cachedData != null && CraftYourLifeRPClient.cachedData.isUsingItem())
			{
				IItemPress item = (IItemPress)CraftYourLifeRPClient.cachedData.usedItem.getItem();
                item.onItemStopUsing(Minecraft.getMinecraft().thePlayer, Minecraft.getMinecraft().theWorld, CraftYourLifeRPClient.cachedData.usedItem, CraftYourLifeRPClient.cachedData.itemPressTicks);
				CraftYourLifeRPClient.cachedData.setItemReleased();
				CraftYourLifeRPMod.packetHandler.sendToServer(PacketCustomInterract.syncMouseIsReleased());            
			}
		}
	}
	
	
}
