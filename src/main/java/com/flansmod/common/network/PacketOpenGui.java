package com.flansmod.common.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.PenaltyGui;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketOpenGui extends PacketBase {
	
	public byte guiId;


	public PacketOpenGui()
	{
		
	}
	
	public PacketOpenGui(byte guiId)
	{
		this.guiId = guiId;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(guiId);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		guiId = data.readByte();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) { }

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		switch(guiId)
		{
			case 0:
			{
				Minecraft.getMinecraft().displayGuiScreen(new PenaltyGui());
				break;
			}
			/*case 1:
			{
				Minecraft.getMinecraft().displayGuiScreen(new GuiHalloweenEvent());
				break;
			}*/
		}	
	
	}

}
