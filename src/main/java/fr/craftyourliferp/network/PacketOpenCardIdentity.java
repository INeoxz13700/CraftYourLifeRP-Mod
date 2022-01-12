package fr.craftyourliferp.network;

import java.util.ArrayList;
import java.util.ListIterator;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.IdentityData;
import fr.craftyourliferp.ingame.gui.GuiCharacter;
import fr.craftyourliferp.ingame.gui.GuiIdentityCard;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.management.ServerConfigurationManager;

public class PacketOpenCardIdentity extends PacketBase {
	
	public String lastName;
	public String name;
	public String gender;
	public String birthday;
	
	public int targetId;
	
	boolean sendFromClient;
	
	public PacketOpenCardIdentity() {

	}
	
	public PacketOpenCardIdentity(IdentityData id) {
		this.lastName = id.lastname;
		this.name = id.name;
		this.gender = id.gender;
		this.birthday = id.birthday;
		sendFromClient = false;
	}
	
	public PacketOpenCardIdentity(EntityPlayer target)
	{
		targetId = target.getEntityId();
		sendFromClient = true;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeBoolean(sendFromClient);
		if(!sendFromClient)
		{
			ByteBufUtils.writeUTF8String(data, this.lastName);
			ByteBufUtils.writeUTF8String(data, this.name);
			ByteBufUtils.writeUTF8String(data, this.gender);
			ByteBufUtils.writeUTF8String(data, this.birthday);
		}
		else
		{
			data.writeInt(targetId);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		this.sendFromClient = data.readBoolean();
		if(!sendFromClient)
		{
			this.lastName = ByteBufUtils.readUTF8String(data);
			this.name = ByteBufUtils.readUTF8String(data);
			this.gender = ByteBufUtils.readUTF8String(data);
			this.birthday = ByteBufUtils.readUTF8String(data);
		}
		else
		{
			this.targetId = data.readInt();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		EntityPlayer target = (EntityPlayer)playerEntity.getEntityWorld().getEntityByID(targetId);
		if(target.getCurrentEquippedItem() != null && target.getCurrentEquippedItem().getItem() == ModdedItems.identityCard)
		{
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketOpenCardIdentity(ExtendedPlayer.get(target).identityData), (EntityPlayerMP) playerEntity);
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		IdentityData tId = new IdentityData();
		tId.lastname = this.lastName;
		tId.name = this.name;
		tId.gender = this.gender;
		tId.birthday = this.birthday;
		Minecraft.getMinecraft().displayGuiScreen(new GuiIdentityCard(tId));
	}

	
	
}
