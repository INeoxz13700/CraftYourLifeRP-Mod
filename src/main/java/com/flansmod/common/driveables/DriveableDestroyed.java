package com.flansmod.common.driveables;

import fr.craftyourliferp.utils.ISaveHandler;
import net.minecraft.nbt.NBTTagCompound;

public class DriveableDestroyed implements ISaveHandler  {

	private int vehicleId;
	
	private long destroyTime;
	
	public DriveableDestroyed() 
	{ 
		
	}
	
	private DriveableDestroyed(int vehicleId) 
	{ 
		this.vehicleId = vehicleId;
		this.destroyTime = System.currentTimeMillis();
	}
	
	public static DriveableDestroyed destroyVehicle(int vehicleId)
	{
		return new DriveableDestroyed(vehicleId);
	}
	
	public int getVehicleId()
	{
		return vehicleId;
	}
	
	public long getDestroyedTime()
	{
		return destroyTime;
	}

	@Override
	public void writeToNbt(NBTTagCompound compound)
	{
		compound.setInteger("VehicleId", vehicleId);
		compound.setLong("DestroyTime", destroyTime);
	}

	@Override
	public void readFromNbt(NBTTagCompound compound) 
	{
		vehicleId = compound.getInteger("VehicleId");
		destroyTime = compound.getLong("DestroyTime");
	}
	
}
