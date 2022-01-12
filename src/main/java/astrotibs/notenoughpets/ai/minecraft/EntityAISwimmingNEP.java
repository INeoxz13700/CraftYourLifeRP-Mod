package astrotibs.notenoughpets.ai.minecraft;

import astrotibs.notenoughpets.pathfinding.minecraft.PathNavigateFlying;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.pathfinding.PathNavigate;

public class EntityAISwimmingNEP extends EntityAIBase
{
	private final EntityLiving entity;
	
    public EntityAISwimmingNEP(EntityLiving entityIn)
    {
        this.entity = entityIn;
        this.setMutexBits(4);

        if (entityIn.getNavigator() instanceof PathNavigateFlying)
        {
            ((PathNavigateFlying)entityIn.getNavigator()).setCanFloat(true);
        }
        else if (entityIn.getNavigator() instanceof PathNavigate)
        {
            ((PathNavigate)entityIn.getNavigator()).setCanSwim(true);
        }
	}

    /**
     * Returns whether the EntityAIBase should begin execution.
     */
    public boolean shouldExecute()
    {
        return this.entity.isInWater() || this.entity.handleLavaMovement();
    }

    /**
     * Keep ticking a continuous task that has already been started
     */
    public void updateTask()
    {
        if (this.entity.getRNG().nextFloat() < 0.8F)
        {
            this.entity.getJumpHelper().setJumping();
        }
    }
}