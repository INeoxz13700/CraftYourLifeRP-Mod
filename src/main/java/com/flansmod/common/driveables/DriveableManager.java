package com.flansmod.common.driveables;

import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import fr.craftyourliferp.main.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;

public class DriveableManager {

	
	private HashMap<EntityDriveable, String> driveablesInWorld;
	
	public DriveableManager()
	{
		driveablesInWorld = new HashMap();
	}
	
	public void addDriveable(EntityPlayer player, EntityDriveable driveable)
	{
		driveablesInWorld.put(driveable,player.getCommandSenderName());
		player.worldObj.spawnEntityInWorld(driveable);
	}
	
	public void removeDriveables(EntityPlayer player)
	{
		List<EntityDriveable> driveables = getPlayerDriveablesInWorld(player);
		for(int i = 0; i < driveables.size(); i++)
		{
			EntityDriveable driveable = driveables.get(i);
			driveablesInWorld.remove(driveable);
			driveable.setDead();
		}
	}
	
	public void removeDriveablesFromSameType(EntityDriveable driveable, EntityPlayer player)
	{
		List<EntityDriveable> driveables = driveablesInWorld.keySet().stream().filter(x -> driveablesInWorld.get(x).equals(player.getCommandSenderName()) && x.getDriveableName().equals(driveable.getDriveableName())).collect(Collectors.toList());
		for(int i = 0; i < driveables.size(); i++)
		{
			EntityDriveable listDriveable = driveables.get(i);
			driveablesInWorld.remove(listDriveable);
			listDriveable.setDead();
		}	
	}
	
	public void removeDriveablesFromSameType(EntityDriveable driveable, String playerName)
	{
		List<EntityDriveable> driveables = driveablesInWorld.keySet().stream().filter(x -> driveablesInWorld.get(x).equals(playerName) && x.getDriveableName().equals(driveable.getDriveableName())).collect(Collectors.toList());
		for(int i = 0; i < driveables.size(); i++)
		{
			EntityDriveable listDriveable = driveables.get(i);
			driveablesInWorld.remove(listDriveable);
			listDriveable.setDead();
		}
	}
	
	public List<EntityDriveable> getPlayerDriveablesInWorld(EntityPlayer player)
	{
		return driveablesInWorld.keySet().stream().filter(x -> driveablesInWorld.get(x).equals(player.getCommandSenderName())).collect(Collectors.toList());
	}
	
	public void onPlayerDisconnect(EntityPlayer player)
	{
		removeDriveables(player);
	}
}
