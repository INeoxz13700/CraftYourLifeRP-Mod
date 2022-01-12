package fr.craftyourliferp.game.events;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.items.IItemPress;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketCustomInterract;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.item.ItemStack;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class PlayerItemInteractionListener
{
	
	@SubscribeEvent(priority=EventPriority.HIGHEST,receiveCanceled=false)
	public void onPlayerPressItem(PlayerInteractEvent event)
	{
		/*PlayerCachedData data = PlayerCachedData.getData(event.entityPlayer);
		
		if(!data.isUsingItem())
		{
			if(event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof IItemPress)
			{
				if(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)
				{
					IItemPress item = (IItemPress)event.entityPlayer.getHeldItem().getItem();
					data.usedItem = event.entityPlayer.getHeldItem();
					data.itemPressTicks = 0;
					item.onItemRightClicked(event.entityPlayer,event.entityPlayer.worldObj,event.entityPlayer.getHeldItem());
				}
			}
		}*/
		
		ExtendedPlayer data = ExtendedPlayer.get(event.entityPlayer);
		
		if(!data.isUsingItem())
		{
			if(event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() instanceof IItemPress)
			{
				if(event.action == Action.RIGHT_CLICK_AIR || event.action == Action.RIGHT_CLICK_BLOCK)
				{
					IItemPress item = (IItemPress)event.entityPlayer.getHeldItem().getItem();
					data.usedItem = event.entityPlayer.getHeldItem();
					data.itemPressTicks = 0;
					item.onItemRightClicked(event.entityPlayer,event.entityPlayer.worldObj,event.entityPlayer.getHeldItem());
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerPressingItem(PlayerTickEvent event)
	{
		/*if(event.phase == TickEvent.Phase.END)
		{
			PlayerCachedData data = PlayerCachedData.getData(event.player);
			
			if(event.player.getHeldItem() != null)
			{
				if(data != null && data.isUsingItem())
				{
					if(!ServerUtils.areItemStacksEquals(event.player.getHeldItem(), data.usedItem))
					{							
						((IItemPress)data.usedItem.getItem()).onItemStopUsing(event.player,event.player.worldObj,data.usedItem,data.itemPressTicks);
						data.setItemReleased();
					}
					else if(event.player.getHeldItem().getItem() instanceof IItemPress)
					{
						((IItemPress)event.player.getHeldItem().getItem()).onItemUsing(event.player, event.player.worldObj, data.usedItem,data.itemPressTicks++);
						CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(event.player, false, PacketCustomInterract.syncUsingItem(event.player));
					}
				}
			}
			else
			{
				if(data != null && data.isUsingItem())
				{
					((IItemPress)data.usedItem.getItem()).onItemStopUsing(event.player,event.player.worldObj,data.usedItem,data.itemPressTicks);
					data.setItemReleased();
					CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(event.player, false, PacketCustomInterract.syncUsingItem(event.player));
				}
			}
		}*/
		
		if(event.phase == TickEvent.Phase.END)
		{
			ExtendedPlayer data = ExtendedPlayer.get(event.player);
			
			if(event.player.getHeldItem() != null)
			{
				if(data != null && data.isUsingItem())
				{
					if(!ServerUtils.areItemStacksEquals(event.player.getHeldItem(), data.usedItem))
					{							
						((IItemPress)data.usedItem.getItem()).onItemStopUsing(event.player,event.player.worldObj,data.usedItem,data.itemPressTicks);
						data.setItemReleased();
					}
					else if(event.player.getHeldItem().getItem() instanceof IItemPress)
					{
						((IItemPress)event.player.getHeldItem().getItem()).onItemUsing(event.player, event.player.worldObj, data.usedItem,data.itemPressTicks++);
						CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(event.player, false, PacketCustomInterract.syncUsingItem(event.player));
					}
				}
			}
			else
			{
				if(data != null && data.isUsingItem())
				{
					((IItemPress)data.usedItem.getItem()).onItemStopUsing(event.player,event.player.worldObj,data.usedItem,data.itemPressTicks);
					data.setItemReleased();
					CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(event.player, false, PacketCustomInterract.syncUsingItem(event.player));
				}
			}
		}
	}
	
}
