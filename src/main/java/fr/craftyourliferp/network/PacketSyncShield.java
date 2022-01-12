package fr.craftyourliferp.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketSyncShield extends PacketBase {
	
	public float shield;
	
	public PacketSyncShield() {
		
	}
	
	public PacketSyncShield(float shield) {
		this.shield = shield;
	}
	

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeFloat(shield);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.shield = data.readFloat();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketSyncShield(ExtendedPlayer.get(playerEntity).shield.shield), playerEntity);
	}

	@SideOnly(Side.CLIENT)
	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		ExtendedPlayer.get(clientPlayer).shield.setShield(shield);
	}
	
	public float getShield() {
		return this.shield;
	}

}
