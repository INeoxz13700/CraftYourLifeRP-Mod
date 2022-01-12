package astrotibs.notenoughpets.ai;


import java.util.Iterator;

import astrotibs.notenoughpets.config.GeneralConfig;
import astrotibs.notenoughpets.entity.EntityParrotNEP;
import astrotibs.notenoughpets.entity.IPetData;
import astrotibs.notenoughpets.entity.PetStateEnum;
import astrotibs.notenoughpets.pathfinding.minecraft.PathNavigateFlying;
import astrotibs.notenoughpets.util.Reference;
import net.minecraft.block.Block;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

// Re-written in v2.0.0 so that Parrots can follow you
public class EntityAIFollowOwnerNEP extends EntityAIBase
{
	private final EntityTameable thePet;
    private EntityLivingBase theOwner;
    protected World world;
    private final double followSpeed;
    private final PathNavigate petPathfinder;
    private int timeToRecalcPath;
    float maxDist;
    float minDist;
    private boolean oldWaterCost;
	
	public EntityAIFollowOwnerNEP(EntityTameable tameableIn, double followSpeedIn, float minDistIn, float maxDistIn)
	{
		this.thePet = tameableIn;
        this.world = tameableIn.worldObj;
        this.followSpeed = followSpeedIn;
        this.petPathfinder = tameableIn.getNavigator();
        this.minDist = minDistIn;
        this.maxDist = maxDistIn;
        this.setMutexBits(3);
	}


    /**
     * Returns whether the EntityAIBase should begin execution.
     */
	@Override
    public boolean shouldExecute()
    {
        EntityLivingBase entitylivingbase = this.thePet.getOwner();

        if (entitylivingbase == null)
        {
            return false;
        }
        else if (this.thePet.isSitting())
        {
            return false;
        }
        else if (this.thePet.getDistanceSqToEntity(entitylivingbase) < (double)(this.minDist * this.minDist))
        {
            return false;
        }
        else
        {
        	IPetData petData = (IPetData)thePet;
        	if(petData.getPetState() != PetStateEnum.FOLLOW_OWNER)
        	{
        		return false;
        	}
            this.theOwner = entitylivingbase;
            return true;
        }
    }
	
	/**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
	@Override
    public boolean continueExecuting()
    {
     	IPetData petData = (IPetData)thePet;
    	if(petData.getPetState() != PetStateEnum.FOLLOW_OWNER)
    	{
    		return false;
    	}
		
        return
        		!this.petPathfinder.noPath()
        		&& this.thePet.getDistanceSqToEntity(this.theOwner) > (double)(this.maxDist * this.maxDist)
        		&& !this.thePet.isSitting();
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
	@Override
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        
        if (this.thePet.getNavigator() instanceof PathNavigateFlying)
        {
        	this.oldWaterCost = ((PathNavigateFlying)this.thePet.getNavigator()).getAvoidsWater();
            ((PathNavigateFlying)this.thePet.getNavigator()).setAvoidsWater(false);
        }
        else if (this.thePet.getNavigator() instanceof PathNavigate)
        {
        	this.oldWaterCost = ((PathNavigate)this.thePet.getNavigator()).getAvoidsWater();
            ((PathNavigate)this.thePet.getNavigator()).setAvoidsWater(false);
        }
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
	@Override
    public void resetTask()
    {
        this.theOwner = null;
        
        this.petPathfinder.clearPathEntity();
        
        if (this.thePet.getNavigator() instanceof PathNavigateFlying)
        {
        	this.oldWaterCost = ((PathNavigateFlying)this.thePet.getNavigator()).getAvoidsWater();
            ((PathNavigateFlying)this.thePet.getNavigator()).setAvoidsWater(true);
        }
        else if (this.thePet.getNavigator() instanceof PathNavigate)
        {
        	this.oldWaterCost = ((PathNavigate)this.thePet.getNavigator()).getAvoidsWater();
            ((PathNavigate)this.thePet.getNavigator()).setAvoidsWater(true);
        }
    }

	/**
	 * Updates the task. This was introduced in order to allow control over the entity's teleport range.
	 */
	@Override
	public void updateTask()
	{
		this.thePet.getLookHelper().setLookPositionWithEntity(this.theOwner, 10.0F, (float)this.thePet.getVerticalFaceSpeed());
		
        if (!this.thePet.isSitting())
        {
            if (--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = 10;

                if (!this.petPathfinder.tryMoveToEntityLiving(this.theOwner, this.followSpeed))
                {
                    if (!this.thePet.getLeashed() && !this.thePet.isRiding())
                    {
                    	// Teleportation handler - rejigged
                    	float catTeleportDist = Math.max(GeneralConfig.followTeleportCat, 3F);
                    	float dogTeleportDist = Math.max(GeneralConfig.followTeleportDog, 3F);
                    	float parrotTeleportDist = Math.max(GeneralConfig.followTeleportParrot, 3F); // v2.0.0
                    	
                        if (
                        		(
                        				this.thePet instanceof EntityOcelot
                        				&& GeneralConfig.followTeleportCat > 0 // Fixed to allow teleport disabling - v2.1.0
                        				&& this.thePet.getDistanceSqToEntity(this.theOwner) >= (catTeleportDist*catTeleportDist)
                        				)
                        		||
                        		(
                        				this.thePet instanceof EntityWolf
                        				&& GeneralConfig.followTeleportDog > 0 // Fixed to allow teleport disabling - v2.1.0
                        				&& this.thePet.getDistanceSqToEntity(this.theOwner) >= (dogTeleportDist*dogTeleportDist)
                        				)
                        		||
                        		( // v2.0.0
                        				this.thePet instanceof EntityParrotNEP
                        				&& GeneralConfig.followTeleportParrot > 0 // Fixed to allow teleport disabling - v2.1.0
                        				&& this.thePet.getDistanceSqToEntity(this.theOwner) >= (parrotTeleportDist*parrotTeleportDist)
                        				)
                        		||
                        		(
                        				!(this.thePet instanceof EntityOcelot)
                        				&& !(this.thePet instanceof EntityWolf)
                        				&& !(this.thePet instanceof EntityParrotNEP) // v2.0.0
                        				&& this.thePet.getDistanceSqToEntity(this.theOwner) >= 144.0D
                        				)
                        		)
                        {
                            int i = MathHelper.floor_double(this.theOwner.posX) - 2;
                            int j = MathHelper.floor_double(this.theOwner.posZ) - 2;
                            int k = MathHelper.floor_double(this.theOwner.boundingBox.minY);
                            
                            for (int l = 0; l <= 4; ++l)
                            {
                                for (int i1 = 0; i1 <= 4; ++i1)
                                {
                                	if ((l < 1 || i1 < 1 || l > 3 || i1 > 3) && this.isTeleportFriendlyBlock(i, j, k, l, i1))
                                    {
                                		// Added half-block offset to setLocationAndAngles - v1.3.0
                                        this.thePet.setLocationAndAngles((double)((float)(i + l)+Reference.SPAWN_BLOCK_OFFSET), (double)k, (double)((float)(j + i1)+Reference.SPAWN_BLOCK_OFFSET), this.thePet.rotationYaw, this.thePet.rotationPitch);
                                        
                                        this.petPathfinder.clearPathEntity();
                                        
                                        return;
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
	}

	// v2.0.0
    protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset)
    {
		return this.world.doesBlockHaveSolidTopSurface(this.world, x + xOffset, y - 1, z + zOffset)
    			&& this.isEmptyBlock(x + xOffset, y, z + zOffset)
    			&& this.isEmptyBlock(x + xOffset, y + 1, z + zOffset);
    }
	
	private boolean isEmptyBlock(int x, int y, int z)
    {
        Block block = this.world.getBlock(x, y, z);
        return block == Blocks.air ? true : !block.isNormalCube();
    }
    
	
	/**
	 * Iterates through entity's AI tasks, and removes the vanilla "follow" version so that it can be replaced with NEC's.
	 */
	public static void removeVanillaFollow(EntityTameable entity)
	{
		Iterator iterator = entity.tasks.taskEntries.iterator();
		
		while (iterator.hasNext())
		{
			EntityAITasks.EntityAITaskEntry entityaitaskentry = (EntityAITasks.EntityAITaskEntry)iterator.next();
			EntityAIBase entityaibase1 = entityaitaskentry.action;
			
			if (entityaibase1 instanceof EntityAIFollowOwner)
			{
				iterator.remove();
			}
		}
	}
	
}
