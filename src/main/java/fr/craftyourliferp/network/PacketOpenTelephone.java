package fr.craftyourliferp.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiCharacter;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketOpenTelephone extends PacketBase {
	
	boolean initialize = false;
	
	
	public PacketOpenTelephone() {
		
	}
	
	public PacketOpenTelephone(boolean isInitialization) {
		this.initialize = isInitialization;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeBoolean(initialize);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		initialize = data.readBoolean();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		if(initialize)
		{
			new GuiPhone();
			return;
		}
		Minecraft.getMinecraft().displayGuiScreen(GuiPhone.getPhone() == null ? new GuiPhone() : GuiPhone.getPhone());
	}
	
	
	
}
