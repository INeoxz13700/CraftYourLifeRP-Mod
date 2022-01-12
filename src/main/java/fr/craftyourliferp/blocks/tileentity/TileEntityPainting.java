package fr.craftyourliferp.blocks.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;


import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketStealing;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;

public class TileEntityPainting extends TileEntity implements IStealingTileEntity {
	
	public final static int paintTypeCount = 12;
	
	public int direction;
	
	public byte type = 0;
	
	public EntityPlayer entityStealing;
	
	public long stealingStartedTime;
	
	public TileEntityPainting() {
		
	}

   
   @Override
   public void readFromNBT(NBTTagCompound tag)
   {
       direction = tag.getInteger("Direction");
       type = tag.getByte("PaintingType");
	   super.readFromNBT(tag);

   }
   
   @Override
   public void writeToNBT(NBTTagCompound tag)
   {
       tag.setInteger("Direction", direction);
       tag.setByte("PaintingType", type);
	   super.writeToNBT(tag);
   }
   
   public void setPaintingType(int type)
   {
	   if(type > paintTypeCount-1)
	   {
		   type = 0;
	   }
	   if(type < 0)
	   {
		   type = 0;
	   }
	   
	   this.type = (byte)type;
	   
	   markDirty();
   }
   
   public Packet getDescriptionPacket()
   {
       NBTTagCompound compound = new NBTTagCompound();
       this.writeToNBT(compound);
       return new S35PacketUpdateTileEntity(this.xCoord,this.yCoord, this.zCoord, 0, compound);
   }
   
   @Override
   public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
   {
   	this.readFromNBT(pkt.func_148857_g());
   }


	@Override
	public int getStealingTime() 
	{
		return 10;
	}

	@Override
	public String getDisplayMessageInLook() 
	{
		return "Pour dérober le tableau";
	}

	@Override
	public Entity getThiefEntity()
	{
		return entityStealing;
	}


	@Override
	public long getStealingStartedTime() 
	{
		return stealingStartedTime;
	}


	@Override
	public void resetStealing() {
		entityStealing = null;
		stealingStartedTime = 0;
	}


	@Override
	public void setStealingEntity(EntityPlayer entity) {
		entityStealing = entity;
		stealingStartedTime = System.currentTimeMillis();
	}


	@Override
	public void finalizeStealing() {
		
		Block block = worldObj.getBlock(xCoord, yCoord, zCoord);
		
		if(block == null) return;
		
		Item paintingItem = Item.getItemFromBlock(block);
		
		ItemStack itemstack = new ItemStack(paintingItem,1);
		itemstack.stackTagCompound = new NBTTagCompound();
		
		itemstack.stackTagCompound.setByte("PaintingType", type);
		
		worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
		
		entityStealing.inventory.addItemStackToInventory(itemstack);
		
		entityStealing.addChatMessage(new ChatComponentText("§aVous venez de dérober un tableau!"));
		
		ServerUtils.broadcastMessage("§9[Force de l'ordre] §cLe musée ce fait braquer des renforts sont demandés", (byte)3);
		ServerUtils.broadcastMessage("§9[Force de l'ordre] §cLe musée ce fait braquer des renforts sont demandés", (byte)0);
		
		System.out.println("/wanted add " + entityStealing.getCommandSenderName() + " 4");
		//PlayerCachedData.getData(entityStealing).setNotStealing();
		ExtendedPlayer.get(entityStealing).setNotStealing();

	}
   
}