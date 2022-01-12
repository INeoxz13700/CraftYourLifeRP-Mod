package fr.craftyourliferp.blocks.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;


import cpw.mods.fml.common.FMLCommonHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
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

public class TileEntityComputer extends TileEntity {
		
	public int direction;	
	
	public TileEntityComputer() {
		
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
   
}