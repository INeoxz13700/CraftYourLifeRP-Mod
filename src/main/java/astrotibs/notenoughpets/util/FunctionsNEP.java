package astrotibs.notenoughpets.util;

import net.minecraft.entity.EntityAgeable;
import net.minecraft.item.Item;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

//Added in v1.2

public class FunctionsNEP
{
	
	/**
	 * Input a "growing age" value--typically -24000 for a newborn and 0 for an adult,
	 * and return the median sound pitch that the animal should produce.
	 * @param entity - entity from which to extract a "growing age"
	 * @param range - the output pitch is distributed triangularly with this as the maximum offset
	 */
    public static float getScaledSoundPitch(EntityAgeable entity, float range)
    {
		return MathHelper.clamp_float(1.0F + 0.5F*(((float)entity.getGrowingAge())/-24000), 1.0F, 1.5F)
				+ (entity.worldObj.rand.nextFloat() - entity.worldObj.rand.nextFloat()) * range;
    }
	
    public static float getScaledSoundPitch(EntityAgeable entity)
    {
    	return getScaledSoundPitch(entity, 0.2F);
    }
    
    // Copied from EntityAgeable in 1.8 and modified
    public static void ageUp(EntityAgeable ageable, int growthSeconds, boolean showParticles)
    {
        int growingAge = ageable.getGrowingAge();
        int ticksToIncrease = growthSeconds * 20;
        
        if (ticksToIncrease > -growingAge) {ticksToIncrease = -growingAge;} // So that it doesn't overshoot zero
        
        if (showParticles)
        {
        	World world = ageable.worldObj;
        	
        	for (int i=0; i<=ticksToIncrease/(4*60); i++)
        	{
        		// Spawns a particle. Arg: particleType, x, y, z, velX, velY, velZ
        		world.spawnParticle(
        				"happyVillager", // Green sparkles
        				ageable.posX + (double)(world.rand.nextFloat() * ageable.width * 2.0F) - (double)ageable.width,
        				ageable.posY + 0.25D + (double)(world.rand.nextFloat() * ageable.height),
        				ageable.posZ + (double)(world.rand.nextFloat() * ageable.width * 2.0F) - (double)ageable.width,
        				0.0D, 0.0D, 0.0D);
        	}
        }
        
        ageable.setGrowingAge(growingAge + ticksToIncrease);
    }
    
    // v2.0.0
    /**
     * Item equivalent of Block.getBlockFromName(String)
     */
    public static Item getItemFromName(String itemName)
    {
        if (Item.itemRegistry.containsKey(itemName))
        {
            return (Item)Item.itemRegistry.getObject(itemName);
        }
        else
        {
            try
            {
                return (Item)Item.itemRegistry.getObjectById(Integer.parseInt(itemName));
            }
            catch (NumberFormatException numberformatexception)
            {
                return null;
            }
        }
    }
}
