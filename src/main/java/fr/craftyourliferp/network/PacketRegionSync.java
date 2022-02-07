package fr.craftyourliferp.network;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketRegionSync extends PacketBase {
	
	private List<String> currentRegions = new ArrayList();

	public PacketRegionSync()
	{
		
	}
	
	public PacketRegionSync(List<String> currentRegions)
	{
		this.currentRegions = currentRegions;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(currentRegions.size());
		for(String rg : currentRegions)
		{
			ByteBufUtils.writeUTF8String(data, rg);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		int size = data.readByte();
		for(int i = 0; i < size; i++)
		{
			currentRegions.add(ByteBufUtils.readUTF8String(data));
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {	}

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) {
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(clientPlayer);
		extendedPlayer.currentRegions = this.currentRegions;
	}

}
