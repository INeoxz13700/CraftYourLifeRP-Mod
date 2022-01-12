package fr.craftyourliferp.blocks.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;


import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketStealing;
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

public class TileEntityGoldIngot extends TileEntity implements IStealingTileEntity {
		
	public int direction;
		
	public EntityPlayer entityStealing;
	
	public long stealingStartedTime;
	
	public TileEntityGoldIngot() {
		
	}

   
   @Override
   public void readFromNBT(NBTTagCompound tag)
   {
       direction = tag.getInteger("Direction");
	   super.readFromNBT(tag);

   }
   
   @Override
   public void writeToNBT(NBTTagCompound tag)
   {
       tag.setFloat("Direction", direction);
	   super.writeToNBT(tag);
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
		return 4;
	}

	@Override
	public String getDisplayMessageInLook() 
	{
		return "Pour dérober le lingot";
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
		
		Item goldIngot = Item.getItemFromBlock(block);
		
		worldObj.setBlock(xCoord, yCoord, zCoord, Blocks.air);
		
		entityStealing.inventory.addItemStackToInventory(new ItemStack(goldIngot, 1));
		
		entityStealing.addChatMessage(new ChatComponentText("§aVous venez de dérober un lingot d'or!"));
		
		//PlayerCachedData.getData(entityStealing).setNotStealing();
		ExtendedPlayer.get(entityStealing).setNotStealing();

	}
   
}