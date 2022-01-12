package com.flansmod.common;

import net.minecraft.util.Vec3;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;

public class FlansModSettings {
	
	public static FlansModSettings instance;
	
	public FlansModSettings()
	{
		instance = this;
	}

	public int bulletSnapshotMin;
	
	public int bulletSnapshotDivisor;
	
	public boolean driveablesBreakBlocks;
	
	public boolean shellsEnabled;
	
	public boolean bulletsEnabled = true;
	
	public boolean bombsEnabled = true;
	
	public boolean vehiclesNeedFuel = true;
	
	public boolean explosions;
	
	public boolean canBreakGuns= true;
	
	public boolean canBreakGlass;
	
	public boolean destroyVehicleOnDisconnect = true;
	
	public int weaponDrops;
	
	public Vec3 vehicleThiefArea;
		
	public void updateConfig()
	{
		Configuration config = FlansMod.configFile;
		ConfigCategory category = config.getCategory(Configuration.CATEGORY_GENERAL);
		
    	if(category==null) return;
    	
    	if(category.get("Driveable_break_blocks") != null)
    	{
    		category.get("Shells_enabled").set(FlansModSettings.instance.shellsEnabled);
    	}
    	if(category.get("Bullet_enabled") != null)
    	{
    		category.get("Bullet_enabled").set(FlansModSettings.instance.bulletsEnabled);
    	}
    	if(category.get("Bombs_enabled") != null)
    	{
    		category.get("Bombs_enabled").set(FlansModSettings.instance.bombsEnabled);
    	}
    	if(category.get("Vehicle_need_fuel") != null)
    	{
    		category.get("Vehicle_need_fuel").set(FlansModSettings.instance.vehiclesNeedFuel);
    	}
    	if(category.get("Explosions") != null)
    	{
    		category.get("Explosions").set(FlansModSettings.instance.explosions);
    	}
    	if(category.get("Guns_can_break") != null)
    	{
    		category.get("Guns_can_break").set(FlansModSettings.instance.canBreakGuns);
    	}
    	if(category.get("Glass_can_break") != null)
    	{
    		category.get("Glass_can_break").set(FlansModSettings.instance.canBreakGlass);
    	}
    	if(category.get("Weapon_drops") != null)
    	{
    		category.get("Weapon_drops").set(FlansModSettings.instance.weaponDrops);
    	}
    	if(category.get("Destroy_Vehicle_Disconnection") != null)
    	{
    		category.get("Destroy_Vehicle_Disconnection").set(FlansModSettings.instance.destroyVehicleOnDisconnect);
    	}
     
    	if(config.hasChanged())
    	{
    		config.save();
    	}
	}
}
