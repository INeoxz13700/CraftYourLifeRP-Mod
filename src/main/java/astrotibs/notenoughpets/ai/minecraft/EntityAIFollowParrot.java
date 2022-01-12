package astrotibs.notenoughpets.ai.minecraft;

import java.util.List;

import javax.annotation.Nullable;

import astrotibs.notenoughpets.pathfinding.minecraft.PathNavigateFlying;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityLookHelper;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAIFollowParrot extends EntityAIBase
{
    private final EntityLiving entity;
    //private final Predicate<EntityLiving> followPredicate;
    private final IEntitySelector followPredicate;
    private EntityLiving followingEntity;
    private final double speedModifier;
    private final PathNavigate navigation;
    private int timeToRecalcPath;
    private final float stopDistance;
    private boolean oldWaterCost;
    private final float areaSize;
    
    public EntityAIFollowParrot(final EntityLiving entityTameable, double speed, float followBegin, float followCutoff)
    {
        this.entity = entityTameable;
        
        this.followPredicate = new IEntitySelector()
        {
            // Return whether the specified entity is applicable to this filter.
            public boolean isEntityApplicable(@Nullable Entity entity)
            {
            	if (entity instanceof EntityLiving)
            	{
            		return entity != null && entityTameable.getClass() != entity.getClass();
            	}
            	return false;
            }
        };
        
        this.speedModifier = speed;
        this.navigation = entityTameable.getNavigator();
        this.stopDistance = followBegin;
        this.areaSize = followCutoff;
        this.setMutexBits(3);
        
    }

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        List list = this.entity.worldObj.selectEntitiesWithinAABB(EntityLiving.class, this.entity.boundingBox.expand((double)this.areaSize, (double)this.areaSize, (double)this.areaSize), this.followPredicate);

        if (!list.isEmpty())
        {
            for (Object entityliving : list)
            {
                if (!((EntityLiving)entityliving).isInvisible())
                {
                    this.followingEntity = (EntityLiving)entityliving;
                    return true;
                }
            }
        }

        return false;
    }

    /**
     * Returns whether an in-progress EntityAIBase should continue executing
     */
    public boolean shouldContinueExecuting()
    {
        return this.followingEntity != null && !this.navigation.noPath() && this.entity.getDistanceSqToEntity(this.followingEntity) > (double)(this.stopDistance * this.stopDistance);
    }

    /**
     * Execute a one shot task or start executing a continuous task
     */
    public void startExecuting()
    {
        this.timeToRecalcPath = 0;
        
        if (this.entity.getNavigator() instanceof PathNavigateFlying)
        {
        	this.oldWaterCost = ((PathNavigateFlying)this.entity.getNavigator()).getAvoidsWater();
            ((PathNavigateFlying)this.entity.getNavigator()).setAvoidsWater(false);
        }
        else if (this.entity.getNavigator() instanceof PathNavigate)
        {
        	this.oldWaterCost = ((PathNavigate)this.entity.getNavigator()).getAvoidsWater();
            ((PathNavigate)this.entity.getNavigator()).setAvoidsWater(false);
        }
    }

    /**
     * Reset the task's internal state. Called when this task is interrupted by another one
     */
    public void resetTask()
    {
        this.followingEntity = null;
        this.navigation.clearPathEntity();
        
        if (this.entity.getNavigator() instanceof PathNavigateFlying)
        {
        	((PathNavigateFlying)this.entity.getNavigator()).setAvoidsWater(true);
        }
        else if (this.entity.getNavigator() instanceof PathNavigate)
        {
        	((PathNavigate)this.entity.getNavigator()).setAvoidsWater(true);
        }
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        if (this.followingEntity != null && !this.entity.getLeashed())
        {
            this.entity.getLookHelper().setLookPositionWithEntity(this.followingEntity, 10.0F, (float)this.entity.getVerticalFaceSpeed());

            if (--this.timeToRecalcPath <= 0)
            {
                this.timeToRecalcPath = 10;
                double d0 = this.entity.posX - this.followingEntity.posX;
                double d1 = this.entity.posY - this.followingEntity.posY;
                double d2 = this.entity.posZ - this.followingEntity.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 > (double)(this.stopDistance * this.stopDistance))
                {
                    this.navigation.tryMoveToEntityLiving(this.followingEntity, this.speedModifier);
                }
                else
                {
                    this.navigation.clearPathEntity();
                    EntityLookHelper entitylookhelper = this.followingEntity.getLookHelper();
                    
                    double posX_reflected = ReflectionHelper.getPrivateValue(EntityLookHelper.class, entitylookhelper, new String[]{"posX", "field_75656_e"});
                    double posY_reflected = ReflectionHelper.getPrivateValue(EntityLookHelper.class, entitylookhelper, new String[]{"posY", "field_75653_f"});
                    double posZ_reflected = ReflectionHelper.getPrivateValue(EntityLookHelper.class, entitylookhelper, new String[]{"posZ", "field_75654_g"});
                    
                    if (
                    		d3 <= (double)this.stopDistance
                    		|| (posX_reflected == this.entity.posX && posY_reflected == this.entity.posY && posZ_reflected == this.entity.posZ)
                    		)
                    {
                        double d4 = this.followingEntity.posX - this.entity.posX;
                        double d5 = this.followingEntity.posZ - this.entity.posZ;
                        this.navigation.tryMoveToXYZ(this.entity.posX - d4, this.entity.posY, this.entity.posZ - d5, this.speedModifier);
                    }
                }
            }
        }
    }
}