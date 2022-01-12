package fr.craftyourliferp.network;

import fr.craftyourliferp.effects.Effect;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketEffect extends PacketBase {

	public int id;
	
	public int timeInSeconds;
	
	public static PacketEffect displayEffect(int id, int timeInSeconds)
	{
		PacketEffect packet = new PacketEffect();
		packet.id = id;
		packet.timeInSeconds = timeInSeconds;
		return packet;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeInt(id);
		data.writeInt(timeInSeconds);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		id = data.readInt();
		timeInSeconds = data.readInt();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) { }

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		Effect.renderEffect(id, timeInSeconds);
	}

}
