package fr.craftyourliferp.items;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;

public interface IItemCollider {

	public float[] getSize();
	
	
	public Vec3 getRelativePositionFromPlayer();
	
	public void onItemAttacked(ItemStack item,EntityPlayer player, Entity collidedWith, float damage);
	
}
