package fr.craftyourliferp.network;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.phone.NetworkCallTransmitter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import fr.craftyourliferp.phone.Call;
import fr.craftyourliferp.phone.CallHandler;

public class PacketSendVoice extends PacketBase {
	
	public byte[] targetData;
		
	public PacketSendVoice() {

	}
	
	public PacketSendVoice(byte[] targetData, String senderUsername) {
		this.targetData = targetData;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeBytes(targetData);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		targetData = new byte[data.readableBytes()];
		data.readBytes(targetData);
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		
		NetworkCallTransmitter nt = NetworkCallTransmitter.getByUsername(playerEntity.getCommandSenderName());

		
		if(nt != null)
			nt.TransmitVoiceData(playerEntity, this.targetData);
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		CallHandler CH = GuiPhone.getPhone().getCallHandler();

		if(CH != null)
		{
			CH.recorder.ProcessDataFromServer(this.targetData);
		}

	}

}
