package fr.craftyourliferp.network;

import java.util.Random;

import fr.craftyourliferp.data.BlockData;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.items.IItemPress;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;

public class PacketCustomInterract extends PacketBase {

	/*
	 *  0: custom right click
	 *  1: mouse released
	 *  2: sync using item
	 */
	private byte type;
	
	private int x;
	private int y;
	private int z;
	
	private int sideHit;
	
	private int entityId;
	private boolean isUsing;
	
	public PacketCustomInterract()
	{
		
	}
	
	public PacketCustomInterract(int x, int y, int z, int sideHit)
	{
		this.x = x;
		this.y = y;
		this.z = z;
		this.sideHit = sideHit;
		this.type = 0;
	}
	
	public static PacketCustomInterract syncMouseIsReleased()
	{
		PacketCustomInterract packet = new PacketCustomInterract();
		packet.type = 1;
		return packet;
	}
	
	/** server side only **/
	/*public static PacketCustomInterract syncUsingItem(EntityPlayer player)
	{
		if(player.worldObj.isRemote) return null;
		
		PlayerCachedData data = PlayerCachedData.getData(player);
		PacketCustomInterract packet = new PacketCustomInterract();
		packet.entityId = player.getEntityId();
		packet.type = 2;
		packet.isUsing = data.isUsingItem();
		return packet;
	}*/
	public static PacketCustomInterract syncUsingItem(EntityPlayer player)
	{
		if(player.worldObj.isRemote) return null;
		
		ExtendedPlayer data = ExtendedPlayer.get(player);
		PacketCustomInterract packet = new PacketCustomInterract();
		packet.entityId = player.getEntityId();
		packet.type = 2;
		packet.isUsing = data.isUsingItem();
		return packet;
	}
	
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(type);
		if(type == 0)
		{
			data.writeInt(x);
			data.writeInt(y);
			data.writeInt(z);
			data.writeInt(sideHit);
		}
		else if(type == 2)
		{
			data.writeInt(entityId);
			data.writeBoolean(isUsing);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		type = data.readByte();
		if(type == 0)
		{
			x = data.readInt();
			y = data.readInt();
			z = data.readInt();
			sideHit = data.readInt();
		}
		else if(type == 2)
		{
			entityId = data.readInt();
			isUsing = data.readBoolean();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		if(type == 0)
		{
			if(playerEntity.getHeldItem() != null && playerEntity.getHeldItem().getItem() == ModdedItems.itemCrowbar)
			{
				Block block = playerEntity.worldObj.getBlock(x, y, z);
				
				if(block == null) return;
				
				if(block instanceof BlockDoor)
				{
					//PlayerCachedData cachedData = PlayerCachedData.getData(playerEntity);
					ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
	
					
					if(ServerUtils.canUseBelier(playerEntity))
					{
						int rand = MathHelper.getRandomIntegerInRange(new Random(), 0, 100) / 2;
						if(rand >= 40 && rand <= 50)
						{
							//ExtendedPlayer.get(playerEntity).lastBelierUseTime = System.currentTimeMillis();
							extendedPlayer.lastBelierUseTime = System.currentTimeMillis();
							WorldData world = WorldData.get(playerEntity.worldObj);
	
							int meta = playerEntity.worldObj.getBlockMetadata(x, y, z);
							Vec3 blockCoordinates = Vec3.createVectorHelper(x, y, z);
	
							if(playerEntity.worldObj.getBlock((int)blockCoordinates.xCoord, (int)blockCoordinates.yCoord-1, (int)blockCoordinates.zCoord) instanceof BlockDoor)
							{
								blockCoordinates = Vec3.createVectorHelper((int)blockCoordinates.xCoord, (int)blockCoordinates.yCoord-1, (int)blockCoordinates.zCoord);
								meta = playerEntity.worldObj.getBlockMetadata((int)blockCoordinates.xCoord, (int)blockCoordinates.yCoord, (int)blockCoordinates.zCoord);
								world.getBlocksBackup().addBlock(new BlockData(Block.getIdFromBlock(block), (byte) meta, blockCoordinates, 600));
							}
							else
							{
								world.getBlocksBackup().addBlock(new BlockData(Block.getIdFromBlock(block), (byte) meta, blockCoordinates, 600));
							}
							
							playerEntity.worldObj.setBlock((int)blockCoordinates.xCoord, (int)blockCoordinates.yCoord, (int)blockCoordinates.zCoord, Blocks.air);
							playerEntity.addChatComponentMessage(new ChatComponentText("§aTentative réussi"));
						}
						else
						{
							playerEntity.addChatComponentMessage(new ChatComponentText("§cTentative échoué"));
						}
					}
					else
					{
						playerEntity.addChatComponentMessage(new ChatComponentText("§cVous ne pouvez pas utiliser le belier"));
					}
				}
			}
		}
		else if(type == 1)
		{
			if(playerEntity.getHeldItem() != null && playerEntity.getHeldItem().getItem() instanceof IItemPress)
			{
				/*PlayerCachedData cachedData = PlayerCachedData.getData(playerEntity);
				
				if(cachedData.isUsingItem())
				{
					((IItemPress)playerEntity.getHeldItem().getItem()).onItemStopUsing(playerEntity, playerEntity.worldObj, playerEntity.getHeldItem(), cachedData.itemPressTicks);
					cachedData.setItemReleased();
				}*/
				
				ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
				
				if(extendedPlayer.isUsingItem())
				{
					((IItemPress)playerEntity.getHeldItem().getItem()).onItemStopUsing(playerEntity, playerEntity.worldObj, playerEntity.getHeldItem(), extendedPlayer.itemPressTicks);
					extendedPlayer.setItemReleased();
				}
			}
			
		}
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		EntityPlayer playerToSync = (EntityPlayer)clientPlayer.worldObj.getEntityByID(entityId);
		
		if(playerToSync == null) return;
		
		/*if(type == 2)
		{
			PlayerCachedData cachedData = PlayerCachedData.createCachedData(playerToSync);
			
			if(!isUsing && cachedData.isUsingItem())
			{
				((IItemPress)cachedData.usedItem.getItem()).onItemStopUsing(playerToSync, playerToSync.worldObj, playerToSync.getHeldItem(), cachedData.itemPressTicks);
				cachedData.setItemReleased();
			}
			else if(isUsing && !cachedData.isUsingItem())
			{
				if(playerToSync.getHeldItem() != null && playerToSync.getHeldItem().getItem() instanceof IItemPress)
				{
					cachedData.usedItem = playerToSync.getHeldItem();
					cachedData.itemPressTicks = 0;
					((IItemPress)playerToSync.getHeldItem().getItem()).onItemRightClicked(playerToSync, playerToSync.worldObj, playerToSync.getHeldItem());
				}
			}
		}*/
		
		if(type == 2)
		{
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerToSync);
			
			if(!isUsing && extendedPlayer.isUsingItem())
			{
				((IItemPress)extendedPlayer.usedItem.getItem()).onItemStopUsing(playerToSync, playerToSync.worldObj, playerToSync.getHeldItem(), extendedPlayer.itemPressTicks);
				extendedPlayer.setItemReleased();
			}
			else if(isUsing && !extendedPlayer.isUsingItem())
			{
				if(playerToSync.getHeldItem() != null && playerToSync.getHeldItem().getItem() instanceof IItemPress)
				{
					extendedPlayer.usedItem = playerToSync.getHeldItem();
					extendedPlayer.itemPressTicks = 0;
					((IItemPress)playerToSync.getHeldItem().getItem()).onItemRightClicked(playerToSync, playerToSync.worldObj, playerToSync.getHeldItem());
				}
			}
		}
	}

}
