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
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;

import java.util.ArrayList;

import com.flansmod.common.*;
import com.flansmod.common.driveables.EntityVehicle;

public class PacketBuySlot extends PacketBase
{
        
    public PacketBuySlot() {}


	@Override
    public void encodeInto(ChannelHandlerContext ctx, ByteBuf data){}

    @Override
    public void decodeInto(ChannelHandlerContext ctx, ByteBuf data){}

    @Override
    public void handleServerSide(EntityPlayerMP playerEntity)
    {
        ExtendedPlayer data = ExtendedPlayer.get(playerEntity);
        if(data.ownedSlot == 11) {
        	return;
        }
        if(data.getVcoins() >= data.ownedSlot * ExtendedPlayer.slotPriceFactor) {
        	data.setVcoins(data.getVcoins() - (data.ownedSlot * ExtendedPlayer.slotPriceFactor));
        	data.setOwnedSlots(data.getOwnedSlots() + 1);
        }
        else {
        	playerEntity.closeScreen();
        	playerEntity.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Vous n'avez pas les Vcoins nécéssaire pour acheter un slot"));
        }
    }

    @Override
    @SideOnly(Side.CLIENT)
    public void handleClientSide(EntityPlayer clientPlayer)
    {
    }
}