package com.flansmod.common.network;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.FlansMod;

import cpw.mods.fml.common.network.ByteBufUtils;
import fr.craftyourliferp.ingame.gui.PenaltyGui;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.penalty.Penalty;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class PacketPenalty extends PacketListTransmission<Penalty> 
{

	/*
	 * 0 : pay Penalty
	 * 1 : get Penalties
	 */
	private byte packetType;
	
	private List<String> penaltiesId = new ArrayList();
	
	public PacketPenalty() {}
	
	//Used for transmit to client penalties
	public PacketPenalty(List<Penalty> penalties, int lastIndex)
	{
		super(penalties);
		super.lastIndex = lastIndex;
		packetType = 1;
	}
	
	//Used for ask server elements
	public PacketPenalty(int elementsCount)
	{
		super(elementsCount);
		packetType = 1;
	}
	
	//Used to pay penalty
	public static PacketPenalty payPenalties(List<String> penaltiesId)
	{
		PacketPenalty penalty = new PacketPenalty();
		penalty.penaltiesId = penaltiesId;
		penalty.packetType = 0;
		return penalty;
	}

	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(packetType);
		if(packetType == 0)
		{
			data.writeInt(penaltiesId.size());
			for(String id : penaltiesId)
			{
				ByteBufUtils.writeUTF8String(data, id);
			}
		}
		else
		{
			super.encodeInto(ctx, data);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		packetType = data.readByte();
		if(packetType == 0)
		{
			int count = data.readInt();
			for(int i = 0; i < count; i++)
			{
				String penaltyId = ByteBufUtils.readUTF8String(data);
				penaltiesId.add(penaltyId);
			}
		}
		else
		{
			super.decodeInto(ctx, data);
		}
		
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{ 
		ExtendedPlayer ep = ExtendedPlayer.get(playerEntity);

		if(packetType == 0)
		{
			List<String> toRemove = new ArrayList<>();
			for(String id : penaltiesId)
			{
				if(ep.PenaltyManager.extractPenalty(id)) toRemove.add(id);
			}
			FlansMod.getPacketHandler().sendTo(PacketPenalty.payPenalties(toRemove), playerEntity);
		}
		else
		{
			
			if(lastIndex < 0 || askedElementCount < 0) return;
			
			
			int elementLeftToTransmit = ep.PenaltyManager.getPenaltyCount() - lastIndex;
	
			int elementToTransmit = 0;
			
			elementToTransmit = Math.min(askedElementCount, elementLeftToTransmit);
			
			
			if(elementToTransmit < 0) elementToTransmit = 0;
			
			for(int i = 0; i < elementToTransmit; i++)
			{
				elements.add(ep.PenaltyManager.getPenaltyAt(lastIndex + i));
			}
			
			lastIndex += elementToTransmit;  
			
			
			FlansMod.packetHandler.sendTo(new PacketPenalty(elements,lastIndex), playerEntity);
		}
	}
	

	@Override
	public void handleClientSide(EntityPlayer clientPlayer) 
	{ 
		GuiScreen currentGui = Minecraft.getMinecraft().currentScreen;
		if(currentGui instanceof PenaltyGui)
		{
			PenaltyGui gui = (PenaltyGui) currentGui;
			if(packetType == 0)
			{
				gui.removePenalties(penaltiesId);
			}
			else
			{
				gui.guiElementsSync.lastIndex = lastIndex;
				gui.guiElementsSync.received = true;
				
				if(elements.size() > 0)
				{
					gui.scrollBarVertical.setValue(0.7f);
				}
				
				for(Penalty penalty : elements)
				{
					gui.addPenalty(penalty);
				}
			}
		}
		elements.clear();
	}
	
}

