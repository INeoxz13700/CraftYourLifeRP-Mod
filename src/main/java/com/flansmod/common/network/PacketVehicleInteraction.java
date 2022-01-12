package com.flansmod.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

import java.util.UUID;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.DriveableData;
import com.flansmod.common.driveables.DriveableType;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityVehicle;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.ingame.gui.VehicleInteractionGui;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketMessageDisplay;
import fr.craftyourliferp.utils.ServerUtils;

public class PacketVehicleInteraction extends PacketBase
{
	
	/*
	 * 0: open gui
	 * 1: control door
	 * 2: impound vehicle
	 * 3: extract driver from vehicle
	 */
	public byte action;
	
	public boolean state;
	
	public int entityID;
	
	public byte guiType;
	
	
	public PacketVehicleInteraction() {}
	

	public static PacketVehicleInteraction openGui(byte guiType, int entityId)
	{
		PacketVehicleInteraction packet = new PacketVehicleInteraction();
		packet.action = 0;
		packet.guiType = guiType;
		packet.entityID = entityId;
		return packet;
	}
	
	public static PacketVehicleInteraction stateDoor(int entityId, boolean state)
	{
		PacketVehicleInteraction packet = new PacketVehicleInteraction();
		packet.action = 1;
		packet.entityID = entityId;
		packet.state = state;
		return packet;
	}
	
	public static PacketVehicleInteraction impoundVehicle(int entityId)
	{
		PacketVehicleInteraction packet = new PacketVehicleInteraction();
		packet.action = 2;
		packet.entityID = entityId;
		return packet;
	}
	
	public static PacketVehicleInteraction extractDriverFromVehicle(int entityId)
	{
		PacketVehicleInteraction packet = new PacketVehicleInteraction();
		packet.action = 3;
		packet.entityID = entityId;
		return packet;
	}
	


	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
	{
		data.writeByte(action);
		data.writeInt(entityID);
		if(action == 0)
		{
			data.writeByte(guiType);
		}
		else if(action == 1)
		{
			data.writeBoolean(state);
		}
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
	{
		action = data.readByte();
		entityID = data.readInt();
		if(action == 0)
		{
			guiType = data.readByte();
		}
		else if(action == 1)
		{
			state = data.readBoolean();
		}
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		EntityDriveable driveable = (EntityDriveable)playerEntity.getEntityWorld().getEntityByID(entityID);
		if(action == 1)
		{
			driveable.driveableData.varDoor = state;
			playerEntity.worldObj.playSoundAtEntity(playerEntity, driveable.getDriveableType().lockOffSound, 1.0F, 1.0F);
		}
		else if(action == 2)
		{
			if(driveable instanceof EntityVehicle)
			{
				EntityVehicle vehicle = (EntityVehicle) driveable;
				if(vehicle.placer != null)
				{
					if(!vehicle.getVehicleType().hasSiren)
					{
						//PlayerCachedData tempData = PlayerCachedData.getData(playerEntity);
						ExtendedPlayer tempData = ExtendedPlayer.get(playerEntity);

						if(tempData.serverData.job == null || !ServerUtils.canUseImpound(playerEntity))
						{
							playerEntity.addChatMessage(new ChatComponentText("§cVous ne pouvez pas placer ce véhicle en fourrière"));
							return;
						}
						
						if(tempData.serverData.outservice)
						{
							playerEntity.addChatMessage(new ChatComponentText("§cVous êtes hors service!"));
							return;
						}
						
						if(vehicle.placer != null)
						{
							if(vehicle.seats[0].riddenByEntity != null)
							{
								playerEntity.addChatMessage(new ChatComponentText("§cIl y a quelqu'un dans le véhicule"));
								return;
							}
							
							ExtendedPlayer vicitmPersistentData = ExtendedPlayer.get(vehicle.placer);
							
							vicitmPersistentData.PenaltyManager.addPenalty(vehicle,200,"Fourrière " + vehicle.getDriveableName(), playerEntity,true);
							
							CraftYourLifeRPMod.packetHandler.sendTo(new PacketMessageDisplay("Votre véhicule a \\nété placé en \\nfourrière",100,(byte)2),(EntityPlayerMP) vehicle.placer);
						}
						
						playerEntity.addChatMessage(new ChatComponentText("§aVéhicule ajouté à la fourrière!"));
						
						vehicle.setDead();
					}
					else
					{
						playerEntity.addChatMessage(new ChatComponentText("§cVous ne pouvez pas placé ce véhicule dans la fourrière"));
					}
				}
			}
		}
		else if(action == 3)
		{
			if(driveable instanceof EntityVehicle)
			{
				EntityVehicle vehicle = (EntityVehicle) driveable;
				if(vehicle.throttle > 0.050)
				{
					ServerUtils.sendChatMessage(playerEntity, "§cLe véhicule avance trop vite");
					return;
				}
				else if(vehicle.seats[0].riddenByEntity == null)
				{
					ServerUtils.sendChatMessage(playerEntity, "§cIl n'y a personne dans le véhicule");
					return;
				}
				else if(!driveable.getDoor())
				{
					ServerUtils.sendChatMessage(playerEntity, "§cPas de chance, le véhicule est vérouillé de l'intérieur");
					return;
				}
				
				vehicle.seats[0].riddenByEntity.mountEntity(null);;
			}
		}
	}

	
	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer) 
	{
		if(action == 0)
		{
			Minecraft.getMinecraft().displayGuiScreen(new VehicleInteractionGui(entityID,guiType));
		}
	}
}

