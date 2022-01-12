package fr.craftyourliferp.network;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.network.INetworkElement;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketPageTransmission<E extends INetworkElement> extends PacketBase {

	
	/*
	 * 0 : ask element to Server
	 * 1 : send elements to Client
	 */
	protected byte action;
		
	public List<E> elements = new ArrayList<E>();
	
	public int askedPage = 0;
	
	public PacketPageTransmission()
	{
		
	}
	
	public PacketPageTransmission(List<E> elementsToTransmit)
	{
		elements = elementsToTransmit;
		action = 1;
	}
	
	public PacketPageTransmission(final int askedPage)
	{
		this.askedPage = askedPage;
		action = 0;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(action);
		data.writeInt(askedPage);

		if(action == 1)
		{
			data.writeInt(elements.size());
			for(E e : elements)
			{
				e.encodeInto(ctx, data);
			}
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		action = data.readByte();
		askedPage = data.readInt();
		
		if(action == 1)
		{
			int elementsSize = data.readInt();
			
			Type type = getClass().getGenericSuperclass();
		    ParameterizedType paramType = (ParameterizedType) type;
		    Class<E> genericClass = (Class<E>) paramType.getActualTypeArguments()[0];
		   
		    E e = null;
			for(int i = 0; i < elementsSize; i++)
			{
				try {
					e = genericClass.newInstance();
				} catch (InstantiationException | IllegalAccessException e1) {
					e1.printStackTrace();
				}
				e.decodeInto(ctx, data);
				elements.add(e);
			}
		}
		
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) { }

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) { }

}
