package com.flansmod.common;

import java.util.List;
import java.util.UUID;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class VoitureKey extends Item {


    public VoitureKey() {
        this.setMaxStackSize(1);
        this.setHasSubtypes(true);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
        return itemstack;
    }

}
