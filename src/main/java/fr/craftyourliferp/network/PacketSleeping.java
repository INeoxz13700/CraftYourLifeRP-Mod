package fr.craftyourliferp.network;

import java.net.URL;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.nicoszpako.armamania.common.ItemCigarette;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.ingame.gui.GuiSleeping;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PacketSleeping extends PacketBase {

	/*
	 *  0: unsleep local player
	 *  1: sync unsleep
	 *  2: sync sleep
	 *  3: subit death
	 *  4: sync should be reanimate
	 */
	private byte action;
	
	private int entityId;
	
	private int x,y,z;
	
	private int bedDirection;
	
	private boolean shouldBeReanimate;
		
	public PacketSleeping() { }
	
	public PacketSleeping(byte action) 
	{ 
		this.action = action;
	}
 
	
	public static PacketSleeping syncSleeping(int entityId, int x, int y, int z, int bedDirection)
	{
		PacketSleeping packet = new PacketSleeping();
		packet.action = 2;
		packet.entityId = entityId;
		packet.x = x;
		packet.y = y;
		packet.z = z;
		packet.bedDirection = bedDirection;
		return packet;
	}

	
	public static PacketSleeping unSleep()
	{
		PacketSleeping packet = new PacketSleeping((byte)0);
		return packet;
	}
	
	//Server side only
	public static PacketSleeping unSleep(int entityId)
	{
		PacketSleeping packet = new PacketSleeping((byte)1);
		packet.entityId = entityId;
		return packet;
	}
	
	public static PacketSleeping subitDeath()
	{
		PacketSleeping packet = new PacketSleeping((byte)3);
		return packet;
	}
	
	public static PacketSleeping syncShouldBeReanimate(boolean shouldBe)
	{
		PacketSleeping packet = new PacketSleeping((byte)4);
		packet.shouldBeReanimate = shouldBe;
		return packet;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(action);
		if(action == 1)
		{
			data.writeInt(entityId);
		}
		else if(action == 2)
		{
			data.writeInt(entityId);
			data.writeInt(x);
			data.writeByte(y);
			data.writeInt(z);
			data.writeByte(bedDirection);
		}
		else if(action == 4)
		{
			data.writeBoolean(shouldBeReanimate);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		action = data.readByte();
		if(action == 1)
		{
			entityId = data.readInt();
		}
		else if(action == 2)
		{
			entityId = data.readInt();
			x = data.readInt();
			y = data.readByte();
			z = data.readInt();
			bedDirection = data.readByte();
		}
		else if(action == 4)
		{
			shouldBeReanimate = data.readBoolean();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{ 
		if(action == 0)
		{
			if(ExtendedPlayer.wakeUpPlayer(playerEntity))
			CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(playerEntity, true, unSleep(playerEntity.getEntityId()));
		}
		else if(action == 3)
		{
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(playerEntity);
			if(extendedPlayer.reanimatingPlayername == null && extendedPlayer.getShouldBeReanimate() || extendedPlayer.shouldBeInEthylicComa())
			{
				CraftYourLifeRPMod.reanimationHandler.subitDeath(playerEntity);
			}
			else
			{
				ServerUtils.sendMessage("§cUn médecin est entrain de vous réanimer vous ne pouvez pas mourir subitement", playerEntity, 1000, 0);
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		
		if(action == 1)
		{
			Entity entity = clientPlayer.worldObj.getEntityByID(entityId);
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				ExtendedPlayer.wakeUpPlayer(entityPlayer);
			}
		}
		else if(action == 2) 
		{
			Entity entity = clientPlayer.worldObj.getEntityByID(entityId);
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer entityPlayer = (EntityPlayer) entity;
				ExtendedPlayer.sleepPlayerAt(entityPlayer, x, y, z, bedDirection);
			}
		}
		else if(action == 4)
		{
			ExtendedPlayer.get(clientPlayer).setShouldBeReanimate(shouldBeReanimate);
			if(Minecraft.getMinecraft().currentScreen instanceof GuiSleeping)
			{
				GuiSleeping gui = (GuiSleeping) Minecraft.getMinecraft().currentScreen;
				gui.updateGui();
			}
		}
	}

}
