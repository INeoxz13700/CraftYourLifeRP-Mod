package fr.craftyourliferp.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import fr.craftyourliferp.data.SmsData;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketUpdateSmsData extends PacketBase {
	
	public long date;
	public String message;
	public String senderNumber;
	
	public PacketUpdateSmsData(SmsData sms)
	{
		//date = sms.date.getTime();
		message = sms.message;
		senderNumber = sms.senderNumber;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeLong(date);
		ByteBufUtils.writeUTF8String(data, message);
		ByteBufUtils.writeUTF8String(data, senderNumber);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.date = data.readLong();
		this.message = ByteBufUtils.readUTF8String(data);
		this.senderNumber = ByteBufUtils.readUTF8String(data);
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		SmsData sData = SmsData.getSmsData(date, senderNumber, message, playerEntity);
		sData.readed = true;
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		
	}

	 
	
}
