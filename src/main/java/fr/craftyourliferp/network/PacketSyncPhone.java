package fr.craftyourliferp.network;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
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

public class PacketSyncPhone extends PacketBase {
	
	
	
	public String clientNumber;
	
	
	public PacketSyncPhone() 
	{
		
	}
	
	public PacketSyncPhone(String clientNumber) 
	{
		this.clientNumber = clientNumber;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) 
	{
		ByteBufUtils.writeUTF8String(data, this.clientNumber);
	}
		


	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {	
		this.clientNumber = ByteBufUtils.readUTF8String(data);
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		ExtendedPlayer.get(playerEntity).syncPhone();
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) {
		
		ExtendedPlayer pData = ExtendedPlayer.get(clientPlayer);	
		
		pData.phoneData.setNumber(clientNumber);
		
		pData.phoneData.initDatabase();
		pData.phoneData.loadContacts();
		pData.phoneData.loadSms();
		
	}

}
