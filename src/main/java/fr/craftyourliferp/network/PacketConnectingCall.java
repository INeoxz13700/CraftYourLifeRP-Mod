package fr.craftyourliferp.network;

import java.io.IOException;

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

public class PacketConnectingCall extends PacketBase {
	
	private int type;

	public PacketConnectingCall()
	{
		
	}
	
	
	public PacketConnectingCall(int type)
	{
		this.type = type;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeInt(type);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.type = data.readInt();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		NetworkCallTransmitter nt = NetworkCallTransmitter.getByUsername(playerEntity.getCommandSenderName());
		nt.callRequestAnswer(type);
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		GuiPhone phone = GuiPhone.getPhone();
		CallHandler ch = phone.getCallHandler();
		
		if(type == 0)
			ch.callState = 0;
		else if(type == 1)
			ch.callState = 1;
		else if(type == 2)
			ch.callState = 2;
		else if(type == 3)
			ch.callState = 3;
		else if(type == 4)
			ch.callState = 4;
		
		phone.currentApp.updateGuiState();
	}

}
