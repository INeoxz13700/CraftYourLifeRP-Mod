package fr.craftyourliferp.utils;

import net.minecraft.nbt.NBTTagCompound;

public interface ISaveHandler {

	
	public void writeToNbt(NBTTagCompound compound);
	
	public void readFromNbt(NBTTagCompound compound);

}
