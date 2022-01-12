package com.flansmod.common.network;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.ingame.gui.VehiclesGui;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.DriveableData;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.ItemPlane;
import com.flansmod.common.driveables.ItemVehicle;

public class PacketSpawnVehicle extends PacketBase 
{
	/*
	 * 0: spawn vehicle
	 * 1: send result
	 * 2: pay insurance
	 */
	public byte action;
	
	/*
	 * 0: vehicle stolen
	 * 1: vehicle impound
	 * 2: vehicle spawned successfully
	 * 3: no money
	 * 4: vehicle exploded
	 * 5: vehicle repaire 
	 */
	public byte result;
	
	public int vehicleId;	
	
	public PacketSpawnVehicle() {}
	
	public static PacketSpawnVehicle spawnVehicle(int vehicleId)
	{
		PacketSpawnVehicle packet = new PacketSpawnVehicle();
		packet.action = 0;
		packet.vehicleId = vehicleId;
		return packet;
	}
	
	public static PacketSpawnVehicle payInpound(int vehicleId)
	{
		PacketSpawnVehicle packet = new PacketSpawnVehicle();
		packet.action = 2;
		packet.vehicleId = vehicleId;
		return packet;
	}
	
	//Called only from server
	public static PacketSpawnVehicle sendResult(byte result, int vehicleId)
	{
		PacketSpawnVehicle packet = new PacketSpawnVehicle();
		packet.action = 1;
		packet.result = result;
		packet.vehicleId = vehicleId;
		return packet;
	}
	
	
	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) 
	{
		data.writeByte(action);
		data.writeInt(vehicleId);
		
		if(action == 1) data.writeByte(result);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) 
	{
		action = data.readByte();
		vehicleId = data.readInt();
		
		if(action == 1) result = data.readByte();
	}

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) 
	{
		ExtendedPlayer tempData = ExtendedPlayer.get(playerEntity);
		
		if(action == 0)
		{
			spawnVehicle(playerEntity, tempData);
		}
		else if(action == 2)
		{
			if(tempData.vehicleStolen(vehicleId))
			{				
				//PlayerCachedData tempData = PlayerCachedData.getData(playerEntity);

				if(tempData.serverData.money < 1600)
				{
					FlansMod.packetHandler.sendTo(PacketSpawnVehicle.sendResult((byte)3, vehicleId), playerEntity);
					return;
				}
				Integer id = new Integer(vehicleId);
				tempData.stolenVehiclesId.remove(id);
				spawnVehicle(playerEntity, tempData);
				System.out.println("/eco take " + playerEntity.getCommandSenderName() + " 1600");
				playerEntity.addChatComponentMessage(new ChatComponentText("§2Vous venez de payez les frais d'indemnisation de votre"));
				playerEntity.addChatComponentMessage(new ChatComponentText("§2véhicule §a(1600€)")); 
			}
			else if(tempData.vehicleExploded(vehicleId))
			{
				long explodedSince = System.currentTimeMillis() - tempData.explodedVehicles.get(vehicleId).getDestroyedTime();
				if(explodedSince < 900*1000)
				{
					FlansMod.packetHandler.sendTo(PacketSpawnVehicle.sendResult((byte)5, vehicleId), (EntityPlayerMP) playerEntity);
					return;
				}
				
				//PlayerCachedData tempData = PlayerCachedData.getData(playerEntity);
				if(tempData.serverData.money < 5000)
				{
					FlansMod.packetHandler.sendTo(PacketSpawnVehicle.sendResult((byte)3, vehicleId), playerEntity);
					return;
				}
				Integer id = new Integer(vehicleId);
				tempData.explodedVehicles.remove(id);
				spawnVehicle(playerEntity, tempData);
				System.out.println("/eco take " + playerEntity.getCommandSenderName() + " 5000");
				playerEntity.addChatComponentMessage(new ChatComponentText("§2Vous venez de payez les frais d'indemnisation de votre"));
				playerEntity.addChatComponentMessage(new ChatComponentText("§2véhicule §a(5000€)")); 
			}
		}

	}

	@Override
	@SideOnly(Side.CLIENT)
	public void handleClientSide(EntityPlayer clientPlayer)
	{ 
		if(action == 1)
		{
			GuiScreen currentGui = (GuiScreen) Minecraft.getMinecraft().currentScreen;
			
			if(currentGui == null) return;
			
			if(currentGui instanceof VehiclesGui)
			{
				VehiclesGui gui = (VehiclesGui) currentGui;
				
				if(result == 0)
				{
					gui.displayDialogBox("Votre véhicule à été volé (vous devez payer 1600$)",
					new UIButton.CallBackObject()
					{
						public void call()
						{
							gui.payInsurance(vehicleId);
							gui.destroyDialogBox(0);
						}
					},  
					new UIButton.CallBackObject()
					{
						public void call()
						{
							gui.destroyDialogBox(0);
						}
					});
				}
				else if(result == 1)
				{
					gui.displayDialogBox("Votre véhicule, est en fourrière (Payez votre amende)", new UIButton.CallBackObject()
					{
						public void call()
						{
							gui.destroyDialogBox(0);
						}
					}, null);
				}
				else if(result == 2)
				{
					Minecraft.getMinecraft().displayGuiScreen(null);
				}
				else if(result == 3)
				{
					gui.displayDialogBox("Vous n'avez pas suffisament d'argent.",
							new UIButton.CallBackObject()
							{
								public void call()
								{
									gui.destroyDialogBox(0);
								}
							},  
					null);
				}
				else if(result == 4)
				{
					gui.displayDialogBox("Votre véhicule à été détruite (vous devez payer 5000$)",
					new UIButton.CallBackObject()
					{
						public void call()
						{
							gui.payInsurance(vehicleId);
							gui.destroyDialogBox(0);
						}
					},  
					new UIButton.CallBackObject()
					{
						public void call()
						{
							gui.destroyDialogBox(0);
						}
					});
				}
				else if(result == 5)
				{	
					gui.displayDialogBox("Merci de patienter, votre véhicule est pris en charge \n par votre assureur (15min environs)", new UIButton.CallBackObject()
					{
						public void call()
						{
							gui.destroyDialogBox(0);
						}
					}, null);
				}
			}
		}
	}
	
	private void spawnVehicle(EntityPlayer playerEntity, ExtendedPlayer data)
	{
		
		ItemStack itemstack = null;
		
		//On récupère l'instance de l'itemstack dans les sauvegardes
		for(ItemStack is : data.ownedVehicle) 
		{
			if(Item.getIdFromItem(is.getItem()) == vehicleId) 
			{
				itemstack = is;
				break;
			}
		}
		
		for(ItemStack is : data.ownedBoat) 
		{
			if(Item.getIdFromItem(is.getItem()) == vehicleId) 
			{
				itemstack = is;
				break;
			}
		}
		
		for(ItemStack is : data.ownedPlane) {
			if(Item.getIdFromItem(is.getItem()) == vehicleId)
			{
				itemstack = is;
				break;
			}
		}
			
		if(itemstack == null) 
		{
			playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + " Vous ne possedez pas ce véhicule"));
			return;
		}

		//On met le stack à 1 pour être sur que le vehicule puisse spawn
		if(itemstack.stackSize == 0) itemstack.stackSize = 1;

		World world = playerEntity.worldObj;
		Item item = itemstack.getItem();	
		DriveableData isData = null;
		
		//On vérifie si c'est un avion ou véhicule
		if(itemstack.getItem() instanceof ItemPlane)
		{
			ItemPlane isItem = (ItemPlane)itemstack.getItem();
			
			if(data.vehicleExploded(vehicleId))
			{
				FlansMod.packetHandler.sendTo(sendResult((byte)4,vehicleId), (EntityPlayerMP) playerEntity);
				return;
			}
			
			isData = isItem.getPlaneData(itemstack, world);
			
		}
		else
		{
			ItemVehicle isItem = (ItemVehicle)itemstack.getItem();
			
			
			//vehicule volé ?
			if(data.vehicleStolen(vehicleId))
			{
				FlansMod.packetHandler.sendTo(sendResult((byte)0,vehicleId), (EntityPlayerMP) playerEntity);
				return;
			}
			//vehicule en fourrière ?
			else if(data.vehicleIsImpound(vehicleId))
			{
				FlansMod.packetHandler.sendTo(sendResult((byte)1,vehicleId), (EntityPlayerMP) playerEntity);
				return;
			}
			//véhicule détruite ?
			else if(data.vehicleExploded(vehicleId))
			{
				FlansMod.packetHandler.sendTo(sendResult((byte)4,vehicleId), (EntityPlayerMP) playerEntity);
				return;
			}
			
			isData = isItem.getData(itemstack, world);
		}
		
		 if (isData != null) 
		 {
		      EntityDriveable vehicle = null;
		      if (world.getEntityByID(isData.lastEntityId) instanceof EntityDriveable)
		      {
		    	  vehicle = (EntityDriveable)world.getEntityByID(isData.lastEntityId); 
		      }
		      if (vehicle != null)
		      {
			        if ((vehicle.seats[0]).riddenByEntity != null)
			        {
			          playerEntity.addChatComponentMessage(new ChatComponentText(EnumChatFormatting.RED + "Ce véhicule est volé!"));
			          return;
			        } 
			        
			        if (!itemstack.hasTagCompound()) itemstack.stackTagCompound = new NBTTagCompound(); 
			        
			        vehicle.getDriveableData().writeToNBT(itemstack.stackTagCompound);
			        vehicle.setDead();
		      } 
		 } 
		
		FlansMod.packetHandler.sendTo(PacketSpawnVehicle.sendResult((byte)2,vehicleId), (EntityPlayerMP)playerEntity);
		item.onItemRightClick(itemstack, world, playerEntity);
	}
	
}