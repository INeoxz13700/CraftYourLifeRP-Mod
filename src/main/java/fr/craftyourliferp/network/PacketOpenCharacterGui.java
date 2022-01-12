package fr.craftyourliferp.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiCharacter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketOpenCharacterGui extends PacketBase {
	
	boolean send = true;
	
	
	public PacketOpenCharacterGui() {
		
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeBoolean(send);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		send = data.readBoolean();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		Minecraft.getMinecraft().displayGuiScreen(new GuiCharacter(clientPlayer));
	}
	
	
	
}
