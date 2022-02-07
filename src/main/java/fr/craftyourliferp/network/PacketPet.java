package fr.craftyourliferp.network;

import java.util.List;
import java.util.Optional;

import astrotibs.notenoughpets.entity.IPetData;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiNamePet;
import fr.craftyourliferp.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.AxisAlignedBB;

public class PacketPet extends PacketBase {

	/*
	 * 0: open gui
	 * 1: change pet name
	 * 2: play effect
	 */
	private byte type;
	
	private byte id;
	
	private int entityId;
	
	private String petName;
	
	

	public PacketPet()
	{
		
	}
	
	/*
	 * 0: name animal
	 */
	public static PacketPet openGui(int guiId, int entityId)
	{
		PacketPet packet = new PacketPet();
		packet.type = 0;
		packet.id = (byte)guiId;
		packet.entityId = entityId;
		return packet;
	}
	
	public static PacketPet changeName(String name)
	{
		PacketPet packet = new PacketPet();
		packet.type = 1;
		packet.petName = name;
		return packet;
	}
	
	public static PacketPet playEffect(int entityId)
	{
		PacketPet packet = new PacketPet();
		packet.type = 2;
		packet.entityId = entityId;
		return packet;
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		data.writeByte(type);
		if(type == 0)
		{
			data.writeByte(id);
			data.writeInt(entityId);
		}
		else if(type == 1)
		{
			ByteBufUtils.writeUTF8String(data, petName);
		}
		else if(type == 2)
		{
			data.writeInt(entityId);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		type = data.readByte();
		if(type == 0)
		{
			id = data.readByte();
			entityId = data.readInt();
		}
		else if(type == 1)
		{
			petName = ByteBufUtils.readUTF8String(data);
		}
		else if(type == 2)
		{
			entityId = data.readInt();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		if(type == 1)
		{
			if(petName.isEmpty())
			{
				ServerUtils.sendChatMessage(playerEntity, "§cVeuillez entrez un prénom!");
			}
			else if(petName.length() > 16)
			{
				ServerUtils.sendChatMessage(playerEntity, "§cTaille maximal du prénom = 16");
			}
			else
			{
				List<IPetData> petDatas = (List<IPetData>)playerEntity.worldObj.getEntitiesWithinAABB(IPetData.class, AxisAlignedBB.getBoundingBox(playerEntity.posX-6, playerEntity.posY-6, playerEntity.posZ-6, playerEntity.posX+6, playerEntity.posY+6, playerEntity.posZ+6));
			    Optional<IPetData> optional = petDatas.stream().filter(x -> x.getPetName() == null).findFirst();

				if(optional.isPresent())
				{
					IPetData petData = (IPetData)optional.get();
					ServerUtils.sendChatMessage(playerEntity, "§b" + petName + "§a a rejoint votre famille!");
					
					petData.setPetName(petName);
				}
				else
				{
					ServerUtils.sendChatMessage(playerEntity, "Il n'y a aucun animal de compagnie dans vos alentours");
					playerEntity.closeScreen();
				}
			}
		}
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer)
	{
		if(type == 0)
		{
			Minecraft minecraft = Minecraft.getMinecraft();
			switch(id)
			{
				case 0:
				{
					//open name gui
					Entity entity = clientPlayer.worldObj.getEntityByID(entityId);
					if(entity instanceof IPetData)
					{
						minecraft.displayGuiScreen(new GuiNamePet((EntityLiving)clientPlayer.worldObj.getEntityByID(entityId)));
					}
					break;
				}
				default:
				{
					break;
				}
			}
		}
		else if(type == 2)
		{
			Entity entity = clientPlayer.worldObj.getEntityByID(entityId);
			if(entity instanceof IPetData)
			{
				IPetData petData = (IPetData)entity;
				petData.playTameEffectImplements(true);
			}
		}
	}
	
}
