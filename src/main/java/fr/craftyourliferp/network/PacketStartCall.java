package fr.craftyourliferp.network;

import java.io.IOException;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.NumberData;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.phone.NetworkCallTransmitter;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import fr.craftyourliferp.phone.Call;
import fr.craftyourliferp.phone.CallHandler;

public class PacketStartCall extends PacketBase {
	
	private String receiverNumber;
	
	private String callerUsername;
	
	private String callerNumber;
	
	
	//when client start call
	public PacketStartCall(String receiverNumber, String callerNumber, String callerUsername)
	{		
		this.receiverNumber = receiverNumber;
		this.callerUsername = callerUsername;
		this.callerNumber = callerNumber;
	}
	
	//method from server
	public PacketStartCall(String callerNumber)
	{
		this.receiverNumber = "";
		this.callerUsername = "";
		this.callerNumber = callerNumber;
	}
	
	public PacketStartCall()
	{
		
	}
	
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		ByteBufUtils.writeUTF8String(data, this.receiverNumber);
		ByteBufUtils.writeUTF8String(data, this.callerUsername);
		ByteBufUtils.writeUTF8String(data, this.callerNumber);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.receiverNumber = ByteBufUtils.readUTF8String(data);
		this.callerUsername = ByteBufUtils.readUTF8String(data);
		this.callerNumber = ByteBufUtils.readUTF8String(data);
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		
			String receiverUsername = null;
			
			try {
				receiverUsername = NumberData.getUsernameByNumber(this.receiverNumber);
			} catch (IOException e) {
				e.printStackTrace();
			}
			
			if(receiverUsername == null)
			{
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(4), playerEntity);
				return;
			}
			
			
			EntityPlayer receiverPlayerEntity = MinecraftServer.getServer().getEntityWorld().getPlayerEntityByName(receiverUsername);
			
			
			if(receiverPlayerEntity == null)
			{
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(3), playerEntity);
				return;
			}
			else if(receiverPlayerEntity.getCommandSenderName().equalsIgnoreCase(playerEntity.getCommandSenderName()))
			{
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(0), playerEntity);
				return;
			}
			
			boolean containPhone = false;
			for(Object obj : receiverPlayerEntity.inventoryContainer.inventoryItemStacks)
			{
				ItemStack it = (ItemStack) obj;
				if(it != null && it.getItem() != null && it.getItem() == ModdedItems.KSamsung)
					containPhone = true;
				
			}
			
			if(!containPhone)
			{
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(3), playerEntity);
				return;
			}
			
			
			NetworkCallTransmitter nt = null;
			
			try
			{
				nt = new NetworkCallTransmitter(playerEntity, receiverPlayerEntity,this.callerNumber, this.receiverNumber);
			} catch (Exception e) {
				
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketConnectingCall(2), playerEntity);
				
				e.printStackTrace();
				
				return;
			}
			
			nt.sendCallRequest();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		
		
		if(GuiPhone.getPhone() == null)
		{
			GuiPhone.setPhone(new GuiPhone());
		}
		
		if(GuiPhone.getPhone().settings.notDisturb) return;
		
		Minecraft.getMinecraft().displayGuiScreen(GuiPhone.getPhone());
		
		GuiPhone.getPhone().setCallHandler(new CallHandler(this.callerNumber, true));
		GuiPhone.getPhone().openApp(0);
		GuiPhone.getPhone().currentApp.updateGuiState();
	}

}
