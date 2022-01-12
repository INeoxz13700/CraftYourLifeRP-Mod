package fr.craftyourliferp.blocks.tileentity;

import java.util.ArrayList;
import java.util.List;

import javax.vecmath.Vector3d;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.network.PacketFlashBang;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
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

public class TileEntityRadar extends TileEntity {
	
	public List<EntityVehicle> flashedVehicles = new ArrayList<EntityVehicle>();
		
	public boolean isOpen = true;
	
	public float rotation;
		
	public Minecraft mc;
	
	public int limitSpeed;
	
	public int penaltyValue = 100;
	
	public int radius = 5;
	
	
	
	public TileEntityRadar() {
		
	}

	
   @Override
   public void updateEntity(){
	  if(!worldObj.isRemote) {
		  
		   if(isOpen)
		   {
			 
			  List<EntityVehicle> vehicles = this.worldObj.getEntitiesWithinAABB(EntityVehicle.class, AxisAlignedBB.getBoundingBox(this.xCoord - radius,this.yCoord,this.zCoord - radius,this.xCoord + radius,this.yCoord + 100,this.zCoord + radius));
			  for(EntityVehicle v : vehicles)
			  {
				  EntityPlayer victim = v.placer;
				  
				  if(victim != null) {
					  if(v != null && v.speed > this.limitSpeed && !flashedVehicles.contains(v))
					  {
						  //PlayerCachedData cachedData = PlayerCachedData.getData(victim);
						  ExtendedPlayer cachedData = ExtendedPlayer.get(victim);

						  if(cachedData != null)
						  {
							  if(ServerUtils.isForceOrder(cachedData.serverData.job))
							  {
								  continue;
							  }
						  }
						  
						  victim.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[RADAR] " + EnumChatFormatting.DARK_GREEN + "Vous avez dépassé la limite de vitesse de : " + EnumChatFormatting.YELLOW + this.limitSpeed + " km/h"));
						  victim.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[RADAR] " + EnumChatFormatting.DARK_GREEN + "Vous avez reçu une ammende"));
						  victim.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "[RADAR] " + "Un individu à peut être voler votre véhicule."));
						
						  ExtendedPlayer.get(victim).PenaltyManager.addPenalty(v, penaltyValue, "Radar : " + limitSpeed + " km/h",null, false);
						  flashedVehicles.add(v);
						  if(v.getControllingEntity() != null)
							  FlansMod.packetHandler.sendTo(new PacketFlashBang(20), (EntityPlayerMP) v.getControllingEntity());
					 }
				  }
				  
			  }
			  
		  }
		   
		  
		  List<EntityVehicle> removeList = new ArrayList<EntityVehicle>();
		  
		  for(EntityVehicle flashed : flashedVehicles)
		  {
			  if(getDistanceToEntity(flashed, this.xCoord, this.yCoord, this.zCoord) > (this.radius * 2))
			  {
				  removeList.add(flashed);
			  }
		  }
		  flashedVehicles.removeAll(removeList);
	  }
	  
   }
   
   public static double getDistanceToEntity(Entity entity, int x , int y, int z) {
		double deltaX = entity.posX - x;
		double deltaY = entity.posY - y;
		double deltaZ = entity.posZ - z;
			
		return Math.sqrt((deltaX * deltaX) + (deltaY * deltaY) + (deltaZ * deltaZ));
	}
   
   @Override
   public void readFromNBT(NBTTagCompound tag)
   {
       isOpen = tag.getBoolean("IsOpen");
       limitSpeed = tag.getInteger("SpeedLimit");
       radius = tag.getInteger("Radius");
       penaltyValue = tag.getInteger("PenaltyValue");
       rotation = tag.getFloat("Rotation");
	   super.readFromNBT(tag);

   }
   
   @Override
   public void writeToNBT(NBTTagCompound tag)
   {
       tag.setBoolean("IsOpen", isOpen);
       tag.setInteger("SpeedLimit", limitSpeed);
       tag.setInteger("Radius", radius);
       tag.setFloat("Rotation", rotation);
       tag.setInteger("PenaltyValue", penaltyValue);    
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