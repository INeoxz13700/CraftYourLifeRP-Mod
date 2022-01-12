package fr.craftyourliferp.network;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.phone.Call;
import fr.craftyourliferp.phone.NetworkCallTransmitter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketFinishCall extends PacketBase {
	
	String number;
	
	boolean noData = false;
	
	public PacketFinishCall(String number)
	{
		this.number = number;
	}
	
	public PacketFinishCall()
	{
		noData = true;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		if(number != null)
			ByteBufUtils.writeUTF8String(data, this.number);
		data.writeBoolean(noData);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		if(number != null)
			this.number = ByteBufUtils.readUTF8String(data);
		this.noData = data.readBoolean();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		NetworkCallTransmitter nt = null;

		nt = NetworkCallTransmitter.getByUsername(playerEntity.getDisplayName());
		
		if(nt != null)
		{
			nt.finishCall(playerEntity);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		if(GuiPhone.getPhone().currentApp != null && GuiPhone.getPhone().currentApp instanceof Call)
		{
			((Call) GuiPhone.getPhone().currentApp).finishCall();
		}
	}

}
