package fr.craftyourliferp.utils;

import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public interface ICallback {

	void call();
		
	void call(EntityPlayer player);
	
	void call(World world);
	
}
