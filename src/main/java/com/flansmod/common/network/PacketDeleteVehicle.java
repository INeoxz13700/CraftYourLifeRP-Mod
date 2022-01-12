package com.flansmod.common.network;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;
import java.util.List;

import com.flansmod.common.*;
import com.flansmod.common.driveables.DriveableData;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.ItemPlane;
import com.flansmod.common.driveables.ItemVehicle;

public class PacketDeleteVehicle extends PacketBase
{
    public int id;
        
    public PacketDeleteVehicle() {}

    public PacketDeleteVehicle(int value)
    {
         this.id = value;
    }

	@Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
        data.writeInt(this.id);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
        this.id = data.readInt();
    }

    @Override
    public void handleServerSide(EntityPlayerMP playerEntity)
    {
        ExtendedPlayer data = ExtendedPlayer.get(playerEntity);
        
        Item item = Item.getItemById(this.id);
        
        List<ItemStack> driveablesIs;
        
        boolean isVehicle = false;
        
        
        if(item instanceof ItemPlane)
        {
        	driveablesIs = data.ownedPlane;
        }
        else
        {
        	isVehicle = true;
        	ItemVehicle vehicle = (ItemVehicle) item;
        	if(vehicle.type.IsBoat())
        	{
        		driveablesIs = data.ownedBoat;
        	}
        	else
        	{
        		driveablesIs = data.ownedVehicle;
        	}
        }
        	
        
        for(ItemStack is : driveablesIs) {
        	if(Item.getIdFromItem(is.getItem()) == this.id) 
        	{
        		if(isVehicle)
        		{
        			ItemVehicle vehicle = (ItemVehicle) is.getItem();
        			
        			DriveableData driveableData = vehicle.getData(is, playerEntity.getEntityWorld());
        			
            		EntityDriveable driveable = (EntityDriveable) playerEntity.getEntityWorld().getEntityByID(driveableData.lastEntityId);
            		
            		
            		if(driveable != null && driveable.seats[0].riddenByEntity != null)
            		{
            			playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Votre véhicule à été volé vous ne pouvez pas le retirer de vos sauvegardes"));
            			return;
            		}
            		else
            		{
            			if(driveable != null) 
            			{
    	 					if(!is.hasTagCompound())
    	 					{
    	 						is.stackTagCompound = new NBTTagCompound();
    	 					}
    	 					driveable.getDriveableData().ownerName = null;
    	 					driveable.getDriveableData().writeToNBT(is.stackTagCompound);
    	 					//driveable.setDead();
    	 					WorldData.get(playerEntity.worldObj).driveablesManager.removeDriveablesFromSameType(driveable, playerEntity);

            			}
            		}
            		
            		
            		
            		if(data.vehicleIsImpound(id))
            		{
            			playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Véhicule en fourrière payez l'amende"));
            			return;
            		}
            		else if(data.vehicleStolen(id))
            		{
            			playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Votre véhicule à été volé payez les frais"));
            			return;
            		}
            		
            		
   
            		ItemStack givedIs = new ItemStack(Item.getItemById(this.id),1);
            		givedIs.stackTagCompound = is.stackTagCompound;
            		
            		if(givedIs.stackTagCompound.hasKey("OwnerName"))
            		{
            			givedIs.stackTagCompound.setString("OwnerName", "");
            		}
            		            		
            		if(!playerEntity.inventory.addItemStackToInventory(givedIs))
            		{
            			playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Le véhicule " + EnumChatFormatting.RED + is.getDisplayName() + EnumChatFormatting.GREEN + "n'a pas pu être ajouté dans votre inventaire screenez ce msg et contacter un admin"));
            		}  
            		
            		if(vehicle.type.IsBoat())
            		{
            			data.ownedBoat.remove(is);
            		}
            		else
            		{
            			data.ownedVehicle.remove(is);
            		}
            		
            		playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Le véhicule " + EnumChatFormatting.RED + is.getDisplayName() + EnumChatFormatting.GREEN + " a été supprimé de vos sauvegardes"));
            		return;
        		}
        		else
        		{
        			ItemPlane plane = (ItemPlane) is.getItem();

        			
        			DriveableData driveableData = plane.getPlaneData(is, playerEntity.getEntityWorld());

            		EntityDriveable driveable = (EntityDriveable) playerEntity.getEntityWorld().getEntityByID(driveableData.lastEntityId);

            		
            		
            		if(driveable != null)
            		{
	 					if(!is.hasTagCompound())
	 					{
	 						is.stackTagCompound = new NBTTagCompound();
	 					}
	 					driveable.getDriveableData().ownerName = null;
	 					//driveable.setDead();
	 					WorldData.get(playerEntity.worldObj).driveablesManager.removeDriveablesFromSameType(driveable, playerEntity);
            		}
            
            		
            		ItemStack givedIs = new ItemStack(Item.getItemById(this.id),1);
            		givedIs.stackTagCompound = is.stackTagCompound;
            		
            		if(givedIs.stackTagCompound.hasKey("OwnerName"))
            		{
            			givedIs.stackTagCompound.setString("OwnerName", "");
            		}
            		
            		if(!playerEntity.inventory.addItemStackToInventory(givedIs))
            		{
            			playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Le véhicule " + EnumChatFormatting.RED + is.getDisplayName() + EnumChatFormatting.GREEN + "n'a pas pu être ajouté dans votre inventaire screenez ce msg et contacter un admin"));
            		}
            		
            		data.ownedPlane.remove(is);
            		
            		playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "Le véhicule " + EnumChatFormatting.RED + is.getDisplayName() + EnumChatFormatting.GREEN + " a été supprimé de vos sauvegardes"));
            		return;
        		}
        	}
        }
        playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Tu ne possèdes pas ce véhicule"));
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer clientPlayer)
    {
    	
    }
}