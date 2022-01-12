package fr.craftyourliferp.items;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class itemCrowbar extends Item {

	
    public itemCrowbar() {
    	setMaxStackSize(1);
    }

    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {
		if(itemRand.nextInt() > 5){
			world.playSoundAtEntity(player, "cylrp:nail.eject", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		}else{
			world.playSoundAtEntity(player, "cylrp:nail.eject.1", 0.5F, 0.4F / (itemRand.nextFloat() * 0.4F + 0.8F));
		}
		
		
		
        return itemstack;
    }


}

