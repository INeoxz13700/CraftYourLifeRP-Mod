package fr.craftyourliferp.blocks.tileentity;

import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public interface IStealingTileEntity {

	public int getStealingTime();
	
	public String getDisplayMessageInLook();
	
	public Entity getThiefEntity();
	
	public long getStealingStartedTime();
	
	public void resetStealing();
	
	public void setStealingEntity(EntityPlayer entity);
	
	public void finalizeStealing();

}
