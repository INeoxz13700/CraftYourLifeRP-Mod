package astrotibs.notenoughpets.ai.minecraft;

import javax.annotation.Nullable;

import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.util.Vec3;

public class EntityAIWanderAvoidWater extends EntityAIWander
{
    protected final float probability;

    public EntityAIWanderAvoidWater(EntityCreature creature, double speed)
    {
        this(creature, speed, 0.001F);
    }

    public EntityAIWanderAvoidWater(EntityCreature creature, double speed, float probability)
    {
        super(creature, speed);
        this.probability = probability;
    }

    @Nullable
    protected Vec3 getPosition()
    {
    	EntityCreature entity_reflected = ReflectionHelper.getPrivateValue( EntityAIWander.class, this, new String[]{"entity", "field_75457_a"} );
    	
        if (entity_reflected.isInWater())
        {
            Vec3 vec3d = RandomPositionGenerator.findRandomTarget(entity_reflected, 15, 7);
            return vec3d == null ? RandomPositionGenerator.findRandomTarget(entity_reflected, 10, 7) : vec3d;
        }
        else
        {
            return entity_reflected.getRNG().nextFloat() >= this.probability ? RandomPositionGenerator.findRandomTarget(entity_reflected, 10, 7) : RandomPositionGenerator.findRandomTarget(entity_reflected, 10, 7);
        }
    }
}