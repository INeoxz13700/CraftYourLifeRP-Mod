package fr.craftyourliferp.network;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import fr.craftyourliferp.data.ContactData;
import fr.craftyourliferp.data.SmsData;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketSyncIdentity extends PacketBase {
	
	public boolean sync;
	
	public PacketSyncIdentity() {
		this.sync = true;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeBoolean(this.sync);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.sync = data.readBoolean();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		ExtendedPlayer.get(playerEntity).identityData.syncData(playerEntity);
	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		
	}


}
