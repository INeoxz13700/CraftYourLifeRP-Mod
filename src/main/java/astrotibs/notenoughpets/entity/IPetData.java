package astrotibs.notenoughpets.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public interface IPetData {

	public long getPetBirthday();
	
	public void setPetBirthday(long Birthday);
	
	public String getPetName();
	
	public void setPetName(String name);
	
	public void setEnergy(float energy);
	
	public void setFood(float food);
	
	public void setHygiene(float hygiene);
	
	public float getEnergy();
	
	public float getFood();
	
	public float getHygiene();
	
	public EntityLivingBase getOwnerImplements();
	
	public void setTamedImplements(boolean value);
	
	public void func_152115_bImplements(String UUID);
	
    public EntityAISit func_70907_rImplements();
    
    public void setGrowingAgeImplements (int growthTick);

    public int getGrowingAgeImplements();
    
    public void playTameEffectImplements(boolean bool);
    
    public float getMood();
    
    public PetStateEnum getPetState();
    
    public void setPetState(PetStateEnum state);
    
    public boolean isSittingImplements();
    
    public int getAgeImplements();
    
    public ChunkCoordinates getHomePositionImplements();
    
    public void setHomeAreaImplements(int x, int y, int z, int range);
    
    public float func_110174_bM_Implements();
    
    public boolean isWithinHomeDistanceCurrentPositionImplements();
    
	
}
