package astrotibs.notenoughpets.ai.minecraft;

import astrotibs.notenoughpets.entity.EntityParrotNEP;
import astrotibs.notenoughpets.util.minecraft.MathHelper18;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.util.MathHelper;

/**
 * Pasted in from 1.12.2
 */
public class EntityFlyHelper extends EntityMoveHelper
{
    public EntityFlyHelper(EntityLiving entityLiving)
    {
        super(entityLiving);
    }
    
    @Override
    public void onUpdateMoveHelper()
    {
    	EntityLiving entity_reflected = ReflectionHelper.getPrivateValue(EntityMoveHelper.class, this, new String[]{"entity", "field_75648_a"});
    	
    	if (entity_reflected instanceof EntityParrotNEP)
    	{
    		if (this.isUpdating())
            {
            	ReflectionHelper.setPrivateValue(EntityMoveHelper.class, this, false, new String[]{"update", "field_75643_f"});
            	
                ((EntityParrotNEP)entity_reflected).setNoGravity(true);
                double posX_reflected = ReflectionHelper.getPrivateValue(EntityMoveHelper.class, this, new String[]{"posX", "field_75646_b"});
            	double posY_reflected = ReflectionHelper.getPrivateValue(EntityMoveHelper.class, this, new String[]{"posY", "field_75647_c"});
            	double posZ_reflected = ReflectionHelper.getPrivateValue(EntityMoveHelper.class, this, new String[]{"posZ", "field_75644_d"});
            	
            	double d0 = posX_reflected - entity_reflected.posX;
                double d1 = posY_reflected - entity_reflected.posY;
                double d2 = posZ_reflected - entity_reflected.posZ;
                double d3 = d0 * d0 + d1 * d1 + d2 * d2;

                if (d3 < 2.500000277905201E-7D)
                {
                	((EntityParrotNEP)entity_reflected).setMoveVertical(0.0F);
                    entity_reflected.setMoveForward(0.0F);
                    return;
                }

                float f = (float)(MathHelper18.atan2(d2, d0) * (180D / Math.PI)) - 90.0F; // New direction to move towards
                entity_reflected.rotationYaw = this.limitAngle(entity_reflected.rotationYaw, f, 10.0F); // Limits yaw direction change to 10 degrees
                float f1;
                
                double speed_reflected = ReflectionHelper.getPrivateValue(EntityMoveHelper.class, this, new String[]{"speed", "field_75645_e"});
                
                if (entity_reflected.onGround)
                {
                    f1 = (float)(speed_reflected * entity_reflected.getEntityAttribute(SharedMonsterAttributes.movementSpeed).getAttributeValue());
                }
                else
                {
                    f1 = (float)(speed_reflected * ((EntityParrotNEP)entity_reflected).flyingSpeed);
                }
                
                entity_reflected.setAIMoveSpeed(f1);
                double d4 = (double)MathHelper.sqrt_double(d0 * d0 + d2 * d2); // Speed component parallel to the ground
                float f2 = (float)(-(MathHelper18.atan2(d1, d4) * (180D / Math.PI)));
                entity_reflected.rotationPitch = this.limitAngle(entity_reflected.rotationPitch, f2, 10.0F); // Limits change to vertical direction angle to 10 degree
                
                ((EntityParrotNEP)entity_reflected).setMoveVertical(d1 > 0.0D ? f1 : -f1);
            }
            else
            {
            	((EntityParrotNEP)entity_reflected).setNoGravity(false);
                ((EntityParrotNEP)entity_reflected).setMoveVertical(0.0F);
                
                entity_reflected.setMoveForward(0.0F);
            }
    	}
    }
    
    /**
     * Limits the given angle to a upper and lower limit.
     * Pasted in from 1.8's EntityMoveHelper
     */
    protected float limitAngle(float angleIn, float lower, float upper)
    {
        float f = MathHelper.wrapAngleTo180_float(lower - angleIn);

        if (f > upper)
        {
            f = upper;
        }

        if (f < -upper)
        {
            f = -upper;
        }

        float f1 = angleIn + f;

        if (f1 < 0.0F)
        {
            f1 += 360.0F;
        }
        else if (f1 > 360.0F)
        {
            f1 -= 360.0F;
        }

        return f1;
    }
}