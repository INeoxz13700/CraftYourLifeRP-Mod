package fr.craftyourliferp.network;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.ListIterator;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.NumberData;
import fr.craftyourliferp.data.SmsData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.phone.Sms;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;
import net.minecraft.util.ChatComponentText;

public class PacketSendSms extends PacketBase {
	
	/*
	 *  0 : sms cannot be send message
	 *  1 : send sms
	 */
	public byte packetType;
	
	public String receiverNumber;
	
	public String senderNumber;
	
	public String message;	
	
	public PacketSendSms()
	{
		
	}
	
	public PacketSendSms(String message , String receiverNumber, String senderNumber)
	{
		this.packetType = 1;
		this.message = message;
		this.receiverNumber = receiverNumber;
		this.senderNumber = senderNumber;
	}
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(packetType);
		
		if(packetType == 1)
		{
			ByteBufUtils.writeUTF8String(data, senderNumber);
			ByteBufUtils.writeUTF8String(data, receiverNumber);
			ByteBufUtils.writeUTF8String(data, message);
		}

	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.packetType = data.readByte();
		
		if(packetType == 1)
		{
			this.senderNumber = ByteBufUtils.readUTF8String(data);
			this.receiverNumber = ByteBufUtils.readUTF8String(data);
			this.message = ByteBufUtils.readUTF8String(data);
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		
		ExtendedPlayer senderData = ExtendedPlayer.get(playerEntity);
		
		ExtendedPlayer receiverData = null;
		
		EntityPlayer receiverEntity = null;
		
		try {
			String username = NumberData.getUsernameByNumber(receiverNumber);

			if(username == null)
			{
				packetType = 0;
				CraftYourLifeRPMod.packetHandler.sendTo(this, (EntityPlayerMP) playerEntity);
				return;
			}
			
			receiverEntity = playerEntity.worldObj.getPlayerEntityByName(username);
			
			if(receiverEntity == null)
			{
				WorldData.get(playerEntity.worldObj).addSmsToSend(new SmsData(0,senderNumber,receiverNumber,message,false));
				return;
			}
			

			if(receiverEntity.inventory.hasItem(ModdedItems.KSamsung)) receiverEntity.getEntityWorld().playSoundAtEntity(receiverEntity, "craftyourliferp:sms_received", 1.0f, 1.0f);

			receiverData = ExtendedPlayer.get(receiverEntity);
			
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketSendSms(message, receiverNumber, senderNumber),  (EntityPlayerMP) receiverEntity);

			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {

		GuiPhone phone = GuiPhone.getPhone();
		if(phone == null)
		{
			GuiPhone.setPhone(new GuiPhone());
		}
		
		if(packetType == 1)
		{
			ExtendedPlayer.get(clientPlayer).phoneData.addSms(message, senderNumber, receiverNumber);
		}
		else
		{
			if(Minecraft.getMinecraft().currentScreen instanceof GuiPhone)
			{
				phone.displayToast("Cette Sms n'a pas pu être envoyer !", 4);
			}
		}
		
	}
	

	
}
