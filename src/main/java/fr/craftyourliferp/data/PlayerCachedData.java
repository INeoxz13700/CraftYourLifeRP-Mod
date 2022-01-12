package fr.craftyourliferp.data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import fr.craftyourliferp.animations.PlayerAnimator;
import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.blocks.tileentity.IStealingTileEntity;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import fr.craftyourliferp.entities.EntityFootballBall;
import fr.craftyourliferp.entities.EntityItemCollider;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.network.PacketCustomInterract;
import fr.craftyourliferp.network.PacketProning;
import fr.craftyourliferp.network.PacketStealing;
import fr.craftyourliferp.network.PacketSyncPlayerData;
import fr.craftyourliferp.phone.web.page.BMGPage;
import fr.craftyourliferp.phone.web.page.BitcoinConverterPage;
import fr.craftyourliferp.phone.web.page.WebPageData;
import fr.craftyourliferp.utils.MinecraftUtils;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.IAttribute;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.Vec3;

public class PlayerCachedData {
	
	private static HashMap<String, PlayerCachedData> datas = new HashMap<String, PlayerCachedData>();
		
	private EntityPlayer player;
		
    //public boolean passwordReceived;
	
	//public Vec3 connectionPos;
	
	public long lastSkinUpdateRequest;
		
	//public EntityFootballBall selectedBall;
	
	//public ShopData shopData;
	
	//public WebPageData currentPageData;
	
	//public PlayerData serverData;
	
	//public IStealingTileEntity stealingTile;
	
	//public WorldSelector regionBuilder;
	
	/*
	 * 0: fire
	 * 1: explosion
	 */
	//public byte builderType;
				
	//0 : no animation
	//public byte currentAnimation;
	
	//public ItemStack usedItem;
	//public int itemPressTicks;
	
	/*public int ticksChangeSlot = -1;
	public int slotEquippedShield = -1;
	public int previousSelectedSlot;
	
	public EntityItemCollider entityItemCollider;
	
	//public PlayerAnimator currentPlayingAnimation;
		
	//public boolean proning = false;
	
	//public int lastProningTick;
	
	public boolean resetTransformations;
	
	private PlayerCachedData() { }
	
	
	
	/*public static PlayerCachedData createCachedData(EntityPlayer player)
	{
		
		String id = getEntityId(player);
		
		if(datas.containsKey(id))
		{
			return datas.get(id);
		}
		
		PlayerCachedData data = new PlayerCachedData();
		
		data.player = player;
		
		datas.put(id, data);
		data.initData();
		
		return data;
	}*/
	
	
	
	/*private static String getEntityId(EntityPlayer player)
	{
		if(player == null)
		{
			return "";
		}
		else
		{
			if(player.worldObj.isRemote)
			{
				return "Client-" + player.getEntityId();
			}
			else
			{
				return "Server-" + player.getEntityId();
			}
		}
	}*/
	
	/*public static PlayerCachedData getData(EntityPlayer player)
	{
		String id = getEntityId(player);
		if(datas.containsKey(id))
		{
			PlayerCachedData cachedData = datas.get(id);
			cachedData.player = player;
			return cachedData;
		}
		else
		{
			return createCachedData(player);
		}
	}*/
	
	public static PlayerCachedData getData(String id)
	{
		if(datas.containsKey(id))
		{
			return datas.get(id);
		}
		else
		{
			return null;
		}
	}
	
	
	/*private void initData()
	{
		shopData = new ShopData();
		serverData = new PlayerData();
	}*/
	
	public static void clearDatas()
	{
		datas.clear();
	}
	
	
	/*public void onPlayerDisconnect()
	{
		String id = getEntityId(player);
		
		datas.remove(id);
	}*/

	public static int getDataCount()
	{
		return datas.size();
	}
	
	/*public WebPageData openPage(byte pageId)
	{
		switch(pageId)
		{
			case 1:
			{
				currentPageData = new BitcoinConverterPage(player);
				break;
			}
			case 2:
			{
				currentPageData = new BMGPage(player);
				break;
			}
		}
		
		currentPageData.initPage();
		
		return currentPageData;
	}*/
	
	/*public WebPageData openPage()
	{
		return currentPageData;
	}
	
	public EntityPlayer getPlayer()
	{
		return player;
	}*/
	
	/*public void setNotStealing()
	{
		
		if(!getPlayer().worldObj.isRemote) 
		{
			TileEntity tile = (TileEntity) stealingTile;

			CraftYourLifeRPMod.packetHandler.sendTo(PacketStealing.notificateClient(tile.xCoord, tile.yCoord, tile.zCoord), (EntityPlayerMP)player);
		}

		
		stealingTile.resetStealing();
		stealingTile = null;
	}
	
	public boolean isStealing()
	{
		return stealingTile != null;
	}*/
	
	/*public void syncData()
	{
		if(player.worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendToServer(PacketSyncPlayerData.askDataToServer());
		}
		else
		{
			if(serverData != null)
			{
				CraftYourLifeRPMod.packetHandler.sendTo(PacketSyncPlayerData.sendDataToClient(this), (EntityPlayerMP) player);
			}
		}
	}*/
	
	/*public void newRegionBuilder(String regionName)
	{
		regionBuilder = new WorldSelector(player.worldObj);
		regionBuilder.setName(regionName);
	}*/
	
	 /*public void updatePlayerData()
	 {
		 System.out.println("/getplayerdata " + player.getCommandSenderName());
	 }*/
	 
	 /*public boolean isUsingItem()
	 {
		 return usedItem != null;
	 }*/
	 
	 /*public void setItemReleased()
	 {
		 usedItem = null;
		 itemPressTicks = 0;
		 
		 CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(player,false, PacketCustomInterract.syncUsingItem(player));
	 }*/
	 
	 /*public boolean isProning()
	 {
		 return proning;
	 }
	 
	 public void setProning(boolean proning)
	 {
		 this.proning = proning;
		 
		 if(proning)
		 {
			 MinecraftUtils.setPlayerSize(player, 0.8F, 0.6F);
		 }
		 else
		 { 
			 MinecraftUtils.resetPlayerSize(player);
		 }
		 
		 if(!player.worldObj.isRemote)
		 {
			 if(proning)
			 {
				 player.eyeHeight = 0.6F;
			 }
			 else
			 {
				 player.eyeHeight = player.getDefaultEyeHeight();
			 }
			 

			CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(player, false, PacketProning.syncPlayerState(player, PlayerCachedData.getData(player).isProning()));
		 }
		 else
		 {
			 resetTransformations = true;
		 }
	 }*/
	 
	 
	
	
	
	
}

