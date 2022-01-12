package fr.craftyourliferp.entities;

import java.util.List;

import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.EntityWheel;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import saracalia.svm.util.tmt.Vec3d;

public class EntityStopStick extends Entity {
	
	private static final int autoDespawnTime = 10 * 60; // in seconds
	
	private long spawnTime;
	
	private int size;
	
	public EntityStopStick(World p_i1582_1_) 
	{
		super(p_i1582_1_);
		this.ignoreFrustumCheck = true;
		this.setSize(0.5F, 1);	
		this.setSize(4);
	}

	
	public EntityStopStick(World p_i1582_1_, int size, int x, int y, int z) 
	{
		super(p_i1582_1_);
		this.ignoreFrustumCheck = true;
		this.setPosition(x, y, z);
		this.setSize(0.5F, 1);	
		this.setSize(size);

	}
	
	public void setSize(int size)
	{
		if(!worldObj.isRemote)
		{
			this.dataWatcher.updateObject(10, size);
		}
		this.size = size;
	}
	
	@Override
	public void onEntityUpdate()
	{
		
		super.onEntityUpdate();
		
		
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        this.prevRotationYaw = rotationYaw;
                
        if(!worldObj.isRemote) 
        {
    		if((worldObj.getTotalWorldTime() - spawnTime) >= (20*this.autoDespawnTime))
    		{
    			this.setDead();
    		}
    		
        	this.checkCollision();
        }
	    this.motionY -= 0.0350D;
	        	    
	    float fallDistance = this.fallDistance;

	    this.moveEntity(this.motionX, this.motionY, this.motionZ);
	}
	
	private void checkCollision()
	{
        if(rotationYaw == 0)
        {
        	List<EntityWheel> list = worldObj.getEntitiesWithinAABB(EntityWheel.class, boundingBox.addCoord(-size*1.9f, 1, 0));
        
        	for(EntityWheel wheel : list)
        	{
        		if(wheel.vehicle instanceof EntityVehicle)
        		{
            		wheel.setDead();
        		}
        	}
        }
        else if(rotationYaw == 90)
        {
        	List<EntityWheel> list = worldObj.getEntitiesWithinAABB(EntityWheel.class, boundingBox.addCoord(0, 1, -size*1.9));
        
        	for(EntityWheel wheel : list)
        	{
        		if(wheel.vehicle instanceof EntityVehicle)
        		{
            		wheel.setDead();
        		}
        	}
        }
        else if(rotationYaw == 180)
        {
        	List<EntityWheel> list = worldObj.getEntitiesWithinAABB(EntityWheel.class, boundingBox.addCoord(size*1.9f, 1, 0));
        	for(EntityWheel wheel : list)
        	{
        		if(wheel.vehicle instanceof EntityVehicle)
        		{
            		wheel.setDead();
        		}
        	}
        }
        else
        {
        	List<EntityWheel> list = worldObj.getEntitiesWithinAABB(EntityWheel.class, boundingBox.addCoord(0, 1, size*1.9f));
        	for(EntityWheel wheel : list)
        	{
        		if(wheel.vehicle instanceof EntityVehicle)
        		{
            		wheel.setDead();
        		}
        	}
        }
	}

	@Override
	protected void entityInit() 
	{
		this.dataWatcher.addObject(10,  0);
		
		spawnTime = worldObj.getTotalWorldTime();
	}

	@Override
	protected void readEntityFromNBT(NBTTagCompound tag)
	{
		dataWatcher.updateObject(10, (int)tag.getInteger("Size"));
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound tag)
	{
	
		tag.setInteger("Size", getSize());
	}
    
    public boolean destroy(int x, int y, int z, EntityPlayer player)
    {
    	
    	 AxisAlignedBB boundingBox = this.boundingBox;
    	 WorldSelector selector = new WorldSelector(worldObj);
    	 if(rotationYaw == 0)
         {		 
    		boundingBox = boundingBox.addCoord(-size*1.9f, 2, 0);
    		
         	selector.setSelectedPos1(Vec3.createVectorHelper((int)boundingBox.minX, (int)boundingBox.minY, (int)boundingBox.minZ));
         	selector.setSelectedPos2(Vec3.createVectorHelper((int)boundingBox.maxX, (int)boundingBox.maxY, (int)boundingBox.maxZ));
       
         }
         else if(rotationYaw == 90)
         {     
    		boundingBox = boundingBox.addCoord(0, 2, -size*1.9);

        	
        	selector.setSelectedPos1(Vec3.createVectorHelper((int)boundingBox.minX, (int)boundingBox.minY, (int)boundingBox.minZ));
        	selector.setSelectedPos2(Vec3.createVectorHelper((int)boundingBox.maxX, (int)boundingBox.maxY, (int)boundingBox.maxZ));
      
         }
         else if(rotationYaw == 180)
         {
    		boundingBox = boundingBox.addCoord(size*1.9f, 2, 0);
    		
         	selector.setSelectedPos1(Vec3.createVectorHelper((int)boundingBox.minX, (int)boundingBox.minY, (int)boundingBox.minZ));
         	selector.setSelectedPos2(Vec3.createVectorHelper((int)boundingBox.maxX, (int)boundingBox.maxY, (int)boundingBox.maxZ));
       

         }
         else
         {
     		boundingBox = boundingBox.addCoord(0, 2, size*1.9f);
     		
         	selector.setSelectedPos1(Vec3.createVectorHelper((int)boundingBox.minX, (int)boundingBox.minY, (int)boundingBox.minZ));
         	selector.setSelectedPos2(Vec3.createVectorHelper((int)boundingBox.maxX, (int)boundingBox.maxY, (int)boundingBox.maxZ));
       
         }
    	 
    	 
      	 if(selector.insideSelection(Vec3.createVectorHelper(x, y, z)))
    	 {
      		 if(!ServerUtils.canUseStopStick(player))
        	 {
        		 ServerUtils.sendChatMessage(player, "§cVous n'avez pas le métier ou grade nécessaire pour intéragir §cavec cette objet");
        		 return false;
        	 }
      		 
      		 if(getSize() == 2)
      		 {
      			 player.inventory.addItemStackToInventory(new ItemStack(ModdedItems.itemStopStickSize2,1));
      		 }
      		 else if(getSize() == 4)
     		 {
     			 player.inventory.addItemStackToInventory(new ItemStack(ModdedItems.itemStopStickSize4,1));
     		 }
      		 else if(getSize() == 6)
     		 {
     			 player.inventory.addItemStackToInventory(new ItemStack(ModdedItems.itemStopStickSize6,1));
     		 }
      		 
      		 setDead();
    	 }
    	 
    
    	 return false;
    }
	
	
	public int getSize()
	{
		if(worldObj.isRemote)
		{
			return dataWatcher.getWatchableObjectInt(10);
		}
		else
		{
			return size;
		}
	}

}
