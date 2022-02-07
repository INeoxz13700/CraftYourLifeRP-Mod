package fr.craftyourliferp.entities;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.FMLCommonHandler;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.block.material.Material;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.item.ItemExpireEvent;

public class EntityFootballBall extends Entity {
		
	private Vec3 spawnPos;
	
	private WorldSelector selector;

	private WorldSelector post1selector;
	private WorldSelector post2selector;

	
	private boolean spawning = false;
	
	public EntityFootballBall(World p_i1582_1_) {
		super(p_i1582_1_);
        this.setSize(0.5F, 0.5F);
        entityCollisionReduction = 0.8f; // 80% reduction£
        spawning = true;
        isImmuneToFire = false;
        
        selector = new WorldSelector(worldObj);
        post1selector =  new WorldSelector(worldObj);
        post2selector =  new WorldSelector(worldObj);
        
		this.dataWatcher.addObject(20,  0);
		this.dataWatcher.addObject(21,  0);
		this.dataWatcher.addObject(22,  0);

		this.dataWatcher.addObject(23,  0);
		this.dataWatcher.addObject(24,  0);
		this.dataWatcher.addObject(25,  0);
        

	}


	@Override
	protected void readEntityFromNBT(NBTTagCompound compound) {
		

		if(compound.hasKey("spawnPosX")) spawnPos = Vec3.createVectorHelper(compound.getDouble("spawnPosX"), compound.getDouble("spawnPosY"), compound.getDouble("spawnPosZ"));
		
		selector.readFromNbt("terrain",compound);
		post1selector.readFromNbt("cage-1", compound);
		post2selector.readFromNbt("cage-2", compound);
	}

	@Override
	protected void writeEntityToNBT(NBTTagCompound compound) {
		
		compound.setDouble("spawnPosX", spawnPos.xCoord);
		compound.setDouble("spawnPosY", spawnPos.yCoord);
		compound.setDouble("spawnPosZ", spawnPos.zCoord);

		selector.writeToNbt("terrain",compound);
		post1selector.writeToNbt("cage-1", compound);
		post2selector.writeToNbt("cage-2", compound);
		
		if(selector.getSelectedPos1() != null)
		{
			this.dataWatcher.updateObject(20,  (int)selector.getSelectedPos1().xCoord);
			this.dataWatcher.updateObject(21,  (int)selector.getSelectedPos1().yCoord);
			this.dataWatcher.updateObject(22,  (int)selector.getSelectedPos1().zCoord);
		}
		
		if(selector.getSelectedPos2() != null)
		{
			this.dataWatcher.updateObject(23,  (int)selector.getSelectedPos2().xCoord);
			this.dataWatcher.updateObject(24,  (int)selector.getSelectedPos2().yCoord);
			this.dataWatcher.updateObject(25,  (int)selector.getSelectedPos2().zCoord);
		}
	}
	

	public void synchronise()
	{
		this.writeEntityToNBT(this.getEntityData());
	}
	
	@Override
    public void onEntityUpdate()
    {
		super.onEntityUpdate();
		        
        this.prevPosX = this.posX;
        this.prevPosY = this.posY;
        this.prevPosZ = this.posZ;
        
        this.prevRotationPitch = rotationPitch;
        this.prevRotationYaw = rotationYaw;
        

        if(spawnPos == null)
        {
        	 spawnPos = Vec3.createVectorHelper(posX, posY, posZ);
        }
        	
	
	    this.motionY -= 0.0350D;
	        
	    this.noClip = this.func_145771_j(this.posX, (this.boundingBox.minY + this.boundingBox.maxY) / 2.0D, this.posZ);
	    
	    float fallDistance = this.fallDistance;

	    this.moveEntity(this.motionX, this.motionY, this.motionZ);
	    
	    
	    if(!worldObj.isRemote)
	    {
		    if(post1selector.entityInsideSelection(this))
		    {
		    	teleportToInitialPosition();
		    	worldObj.playSoundAtEntity(this, "craftyourliferp:goal", 1.0F, 1.0F);
		    }
		    else if(post2selector.entityInsideSelection(this))
		    {
		    	teleportToInitialPosition();
		    	worldObj.playSoundAtEntity(this, "craftyourliferp:goal", 1.0F, 1.0F);
		    }
	    }
	
		if(selector.selectionDefinied() && !selector.entityInsideSelection(this))
		{
	    	teleportToInitialPosition();
		}    
	    
        rotationYaw = (float)(Math.atan2(posX-prevPosX,posZ-prevPosZ) * MathsUtils.Rad2Deg);
        rotationPitch = (float)MathsUtils.Lerp(prevRotationPitch, (float)(rotationPitch+15), (float) getVelocity()*10);

	    if(this.onGround)
	    {
	    	
	    	if(spawning) spawning = false;
	    	
	    	this.motionY += fallDistance * 2/3 * 0.05D;

	    	
		    this.motionX = MathsUtils.Lerp((float)motionX, 0, 0.04f);
		    this.motionZ = MathsUtils.Lerp((float)motionZ, 0, 0.04f);
	    }
	    else
	    {
		    this.motionX = MathsUtils.Lerp((float)motionX, 0, 0.025f);
		    this.motionZ = MathsUtils.Lerp((float)motionZ, 0, 0.025f);
	    }
	    
	    if(this.isCollidedHorizontally)
	    {
	    	Vec3 inverseDir = this.getDirection().crossProduct(Vec3.createVectorHelper(-1, -1, -1));
	    	
	    	if(inverseDir.lengthVector() == 0)
	    	{

		    	this.motionX -= 0.2; 
		    	this.motionZ -= 0.2; 
		    	
	    	}
	    	else
	    	{
		    	this.motionX += inverseDir.xCoord - motionX; 
		    	this.motionZ += inverseDir.zCoord - motionZ; 
	    	}

	    }
	    
	   
	    
        if(this.isBurning())
        {
        	this.setDead();
        }
        
        
        if(getDistance(spawnPos.xCoord, spawnPos.yCoord, spawnPos.zCoord) > 100)
        {
        	teleportToInitialPosition();
        }
                
    }
	
	
    public boolean canBeCollidedWith()
    {
        return true;
    }

    
    public boolean attackEntityFrom(DamageSource p_70097_1_, float p_70097_2_)
    {
        if (this.isEntityInvulnerable())
        {
        	if(!worldObj.isRemote)
        	{
	        	Entity damager = p_70097_1_.getSourceOfDamage();
	        	
	        	if(damager == null) return false;
	        	
	        	if(damager instanceof EntityPlayer)
	        	{
		        	if(FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(((EntityPlayer)p_70097_1_.getSourceOfDamage()).getGameProfile()))
		        	{
		        		this.setDead();
		        		return true;
		        	}
	        	}
        	}
        }
        return false;
    }
    
    public boolean isEntityInvulnerable()
    {
        return true;
    }

    
    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    public boolean canBePushed()
    {
        return true;
    }

	
	public double getVelocity()
	{
		return (Math.abs(motionX) + Math.abs(motionZ));
	}

	
	@Override
    public float getCollisionBorderSize()
    {
        return 0.01F;
    }

	
	@Override
    public void onCollideWithPlayer(EntityPlayer p_70100_1_) 
    {
    	this.applyEntityCollision(p_70100_1_);
    }
	
	public Vec3 getGravityCenter()
	{
		return Vec3.createVectorHelper(this.boundingBox.minX+this.boundingBox.maxX / 2, this.boundingBox.minY+this.boundingBox.maxY / 2, this.boundingBox.minZ+this.boundingBox.maxZ / 2);
	}
	
	public Vec3 getDirection()
	{
		return Vec3.createVectorHelper(posX-prevPosX, 0, posZ-prevPosZ);
	}

	
	@Override
    public void applyEntityCollision(Entity entity)
    {
		
		if(spawning) return;
		
		this.worldObj.playSoundAtEntity(this, "craftyourliferp:ballkick", 2.0F, 1.5F);
		
        if (entity.riddenByEntity != this && entity.ridingEntity != this)
        {
        	if(!this.worldObj.isRemote)
        	{
	            double d0 = entity.posX - this.posX;
	            double d1 = entity.posZ - this.posZ;
	            
	            double d2 = MathHelper.getRandomDoubleInRange(this.rand, 0.01D, 0.5D);
	            
	            boolean isRunning = entity.isSprinting();

	            d0 *= (double)(1.0F - (isRunning ? 0.4F : 0.85F));
	            d1 *= (double)(1.0F - (isRunning ? 0.4F : 0.85F));
	            
	            

	            this.addVelocity(-d0+entity.motionX*entity.motionX, isRunning ? d2 : 0, -d1+entity.motionZ*entity.motionZ);
	             
	        }
        }
    }
	
    public AxisAlignedBB getBoundingBox()
    {
        return this.boundingBox;
    }
	
	public void teleportToInitialPosition()
	{
		motionX = 0;
		motionY = 0;
		motionZ = 0;
		this.setPositionAndRotation(spawnPos.xCoord, spawnPos.yCoord, spawnPos.zCoord, 0, 0);
	}
	
	public WorldSelector getWorldSelector()
	{
		return selector;
	}
	
	public WorldSelector getPost1Selector()
	{
		return post1selector;
	}
	
	public WorldSelector getPost2Selector()
	{
		return post2selector;
	}


	@Override
	protected void entityInit() 
	{
		if(worldObj.isRemote)
		{
			if(dataWatcher.hasChanges())
			{
				selector.setSelectedPos1(Vec3.createVectorHelper(this.dataWatcher.getWatchableObjectInt(20),this.dataWatcher.getWatchableObjectInt(21),this.dataWatcher.getWatchableObjectInt(22)));
				selector.setSelectedPos2(Vec3.createVectorHelper(this.dataWatcher.getWatchableObjectInt(23),this.dataWatcher.getWatchableObjectInt(24),this.dataWatcher.getWatchableObjectInt(25)));
			}
		}
	}
	

}
