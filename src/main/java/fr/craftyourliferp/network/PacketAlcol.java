package fr.craftyourliferp.network;

import eu.nicoszpako.armamania.common.ItemCigarette;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class PacketAlcol extends PacketBase {

	/*
	 * 0: sync alcol
	 * 1: sync cigarette use finish
	 */
	public byte type;
	
	public float alcol;
	
	public int entityId;
	
	public static PacketAlcol syncAlcol(float alcol)
	{
		PacketAlcol packet = new PacketAlcol();
		packet.type = 0;
		packet.alcol = alcol;
		return packet;
	}
	
	public static PacketAlcol syncCigaretteUseFinish(EntityPlayer user)
	{
		PacketAlcol packet = new PacketAlcol();
		packet.type = 1;
		packet.entityId = user.getEntityId();
		return packet;
	}
	
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(type);
		if(type == 0)
		{
			data.writeFloat(alcol);
		}
		else if(type == 1)
		{
			data.writeInt(entityId);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		type = data.readByte();
		if(type == 0)
		{
			alcol = data.readFloat();
		}
		else if(type == 1)
		{
			entityId = data.readInt();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) { }

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		
		if(type == 0)
		{
			ExtendedPlayer ep = ExtendedPlayer.get(clientPlayer);
			
			
			ep.setgAlcolInBlood(alcol);
		}
		else if(type == 1)
		{
			Entity entity = clientPlayer.worldObj.getEntityByID(entityId);
			if(entity instanceof EntityPlayer)
			{
				EntityPlayer player = (EntityPlayer)entity;
				World world = player.worldObj;
				world.spawnParticle("cloud", player.posX + player.getLookVec().xCoord, player.posY + player.getEyeHeight() / 2 + player.getLookVec().yCoord, player.posZ + player.getLookVec().zCoord, player.getLookVec().xCoord * 0.3f , 0.6+player.getLookVec().yCoord,player.getLookVec().zCoord* 0.3f);
				world.spawnParticle("cloud", player.posX + player.getLookVec().xCoord-0.5, player.posY + player.getEyeHeight() / 2 + player.getLookVec().yCoord, player.posZ + player.getLookVec().zCoord, player.getLookVec().xCoord * 0.3f, 0.6+player.getLookVec().yCoord, player.getLookVec().zCoord* 0.3f);
				world.spawnParticle("cloud", player.posX + player.getLookVec().xCoord+0.5, player.posY + player.getEyeHeight() / 2 + player.getLookVec().yCoord, player.posZ + player.getLookVec().zCoord, player.getLookVec().xCoord * 0.3f, 0.6+player.getLookVec().yCoord, player.getLookVec().zCoord* 0.3f);
			}
		}
		
	}

}
