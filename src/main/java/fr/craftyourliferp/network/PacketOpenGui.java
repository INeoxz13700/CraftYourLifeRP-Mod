package fr.craftyourliferp.network;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketOpenGui extends PacketBase{

	private byte guiId;
	
	public PacketOpenGui()
	{
		
	}
	
	public PacketOpenGui(byte guiId)
	{
		this.guiId = guiId;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) 
	{
		data.writeByte(guiId);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		guiId = data.readByte();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		playerEntity.openGui(CraftYourLifeRPMod.instance, guiId, playerEntity.worldObj, (int)playerEntity.posX, (int)playerEntity.posY, (int)playerEntity.posZ);
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		// TODO Auto-generated method stub
		
	}

}
