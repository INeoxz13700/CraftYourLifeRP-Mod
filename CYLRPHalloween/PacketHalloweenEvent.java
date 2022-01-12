package fr.craftyourliferp.network;

import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.halloween.HalloweenEvent;
import fr.craftyourliferp.halloween.HalloweenEvent.HalloweenItem;
import fr.craftyourliferp.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class PacketHalloweenEvent extends PacketBase {

	public int index;
	
	public PacketHalloweenEvent()
	{
		
	}
	
	public PacketHalloweenEvent(int index)
	{
		this.index = index;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeInt(index);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		index = data.readInt();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		HalloweenItem item = HalloweenEvent.items.get(index);
				
		List<ItemStack> itemstacks = new ArrayList();
		
		itemstacks.add(new ItemStack(HalloweenEvent.moneyItem,item.price));
		
		if(ServerUtils.playerHaveItemsInInventory(itemstacks, playerEntity))
		{
			ServerUtils.removeItemsFromInventory(itemstacks, playerEntity);
			if(item.callback != null)
			{
				item.callback.call(playerEntity);
			}
		}
		else
		{
			ServerUtils.sendChatMessage(playerEntity, "§cVous n'avez pas les jetons nécessaire");
		}
	}
	
	@Override
	public void handleClientSide(EntityPlayer clientPlayer) { }
	
}
