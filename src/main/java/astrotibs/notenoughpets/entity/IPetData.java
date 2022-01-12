package astrotibs.notenoughpets.entity;

import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.util.ChunkCoordinates;

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
	
	public EntityLivingBase getOwner();
	
	public void setTamed(boolean value);
	
	public void func_152115_b(String UUID);
	
    public EntityAISit func_70907_r();
    
    public void setGrowingAge (int growthTick);

    public int getGrowingAge();
    
    public void playTameEffect(boolean bool);
    
    public float getMood();
    
    public PetStateEnum getPetState();
    
    public void setPetState(PetStateEnum state);
    
    public boolean isSitting();
    
    public int getAge();
    
    public ChunkCoordinates getHomePosition();
    
    public void setHomeArea(int x, int y, int z, int range);
    
    public float func_110174_bM();
    
    public boolean isWithinHomeDistanceCurrentPosition();
	
}
