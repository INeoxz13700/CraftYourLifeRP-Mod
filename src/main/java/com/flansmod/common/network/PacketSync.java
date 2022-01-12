package com.flansmod.common.network;



import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.ExtendedPlayer;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

import java.util.ArrayList;

import com.flansmod.common.*;
import com.flansmod.common.driveables.EntityVehicle;

public class PacketSync extends PacketBase
{
    public int vCoins;
    public int ownedSlots;
        
    public PacketSync() {}

    public PacketSync(int value)
    {
         this.vCoins = value;
    }

    public PacketSync(int vCoins, int ownedSlot) 
    {
    	this.vCoins = vCoins;
    	this.ownedSlots = ownedSlot;
	}
    

	@Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
        data.writeInt(this.vCoins);
        data.writeInt(this.ownedSlots);
    }

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data)
    {
        this.vCoins = data.readInt();
        this.ownedSlots = data.readInt();
    }

    @Override
    public void handleServerSide(EntityPlayerMP playerEntity)
    {
    	ExtendedPlayer ep = ExtendedPlayer.get(playerEntity);
    	FlansMod.getPacketHandler().sendTo(new PacketSync(ep.vCoins,ep.ownedSlot),playerEntity);
    	System.out.println("Packet received from Client");  	
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer clientPlayer)
    {
    	System.out.println("Packet received from Server");
        ExtendedPlayer data = ExtendedPlayer.get(clientPlayer);
        data.vCoins = this.vCoins;
        data.ownedSlot = this.ownedSlots;
    }
}