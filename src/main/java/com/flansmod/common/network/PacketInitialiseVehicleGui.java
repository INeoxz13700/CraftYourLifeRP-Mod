package com.flansmod.common.network;

import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.VehiclesGui;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import com.flansmod.common.*;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.VehicleType;
import com.flansmod.common.types.InfoType;

public class PacketInitialiseVehicleGui extends PacketBase
{
	
	public enum Category {
		Plane,
		Vehicle,
		Boat;
	}
	
	public Category category;
	
    public int id;
        
    public PacketInitialiseVehicleGui() {}

    public PacketInitialiseVehicleGui(int id)
    {
         this.id = id;
         this.category = Category.Vehicle;
    }
    
    public PacketInitialiseVehicleGui(Category category)
    {
    	this.id = 0;
    	this.category = category;
    }
    

	@Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
        data.writeInt(this.id);
        if(category != null) data.writeInt(this.category.ordinal());
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
      	this.id = data.readInt();
      	this.category = Category.values()[data.readInt()];
    }

    @Override
    public void handleClientSide(EntityPlayer clientPlayer)
    {
    	System.out.println("Packet received from server");
        Minecraft mc = Minecraft.getMinecraft();
        GuiScreen gui = mc.currentScreen;

        if(gui instanceof VehiclesGui)
        {
            ItemStack is = new ItemStack(Item.getItemById(id), 1);
            VehiclesGui vehicleGui = (VehiclesGui) gui;
            vehicleGui.ownedVehicle.add(is);
            vehicleGui.updateScrollviewContents();
        }
    }

	@Override
	public void handleServerSide(EntityPlayerMP playerEntity) {
		System.out.println("Packet received from client");
		ExtendedPlayer p = ExtendedPlayer.get(playerEntity);
		if(this.category == Category.Plane)
		{
			for(ItemStack is : p.ownedPlane) {
				FlansMod.packetHandler.sendTo(new PacketInitialiseVehicleGui(Item.getIdFromItem(is.getItem())), playerEntity);
			}
		}
		else if(this.category == Category.Vehicle)
		{
			for(ItemStack is : p.ownedVehicle) {
				FlansMod.packetHandler.sendTo(new PacketInitialiseVehicleGui(Item.getIdFromItem(is.getItem())), playerEntity);
			}
		}
		else
		{
			for(ItemStack is : p.ownedBoat) {
				FlansMod.packetHandler.sendTo(new PacketInitialiseVehicleGui(Item.getIdFromItem(is.getItem())), playerEntity);
			}
		}

	}
}