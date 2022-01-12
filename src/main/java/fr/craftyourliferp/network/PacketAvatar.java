package fr.craftyourliferp.network;

import java.net.HttpURLConnection;
import java.util.List;
import java.util.UUID;

import org.apache.commons.codec.digest.DigestUtils;

import fr.craftyourliferp.data.AvatarUpdater;
import fr.craftyourliferp.ingame.gui.GuiSkin;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;

public class PacketAvatar extends PacketBase {
	
	/*
	 * 2 update avatar
	 */
	public byte type;
	
	public int entityId;

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(type);
		if(type == 2)
		{
			data.writeInt(entityId);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		type = data.readByte();
		if(type == 2)
		{
			entityId = data.readInt();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		if(type == 2)
		{
			CraftYourLifeRPMod.packetHandler.sendToAllAround(this,playerEntity.posX,playerEntity.posY,playerEntity.posZ,64F,playerEntity.dimension);
		}
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		if(type == 2)
		{
			EntityPlayer player = null;
			
			if(clientPlayer.getEntityId() == entityId)
			{
				player = clientPlayer;
			}
			else
			{
				player = (EntityPlayer) clientPlayer.worldObj.getEntityByID(entityId);
			}

			new AvatarUpdater(player).updateAvatar();
		}
	}

}
