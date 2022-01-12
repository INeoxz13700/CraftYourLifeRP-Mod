package astrotibs.notenoughpets.ai.minecraft;

import java.util.Iterator;

import javax.annotation.Nullable;

import astrotibs.notenoughpets.util.minecraft.BlockPos;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.material.Material;
import net.minecraft.entity.EntityCreature;
import net.minecraft.entity.ai.EntityAIWander;
import net.minecraft.entity.ai.RandomPositionGenerator;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class EntityAIWanderAvoidWaterFlying extends EntityAIWanderAvoidWater
{
    public EntityAIWanderAvoidWaterFlying(EntityCreature creature, double speed)
    {
        super(creature, speed);
    }

    @Nullable
    protected Vec3 getPosition()
    {
        Vec3 vec3d = null;
        
        EntityCreature entity_reflected = ReflectionHelper.getPrivateValue( EntityAIWander.class, this, new String[]{"entity", "field_75457_a"} );
        
        if (entity_reflected.isInWater() || this.isOverWater())
        {
            vec3d = RandomPositionGenerator.findRandomTarget(entity_reflected, 15, 15);
        }
        
        if (entity_reflected.getRNG().nextFloat() >= this.probability)
        {
            vec3d = this.getTreePos();
        }

        return vec3d == null ? super.getPosition() : vec3d;
    }

    @Nullable
    private Vec3 getTreePos()
    {
    	EntityCreature entity_reflected = ReflectionHelper.getPrivateValue( EntityAIWander.class, this, new String[]{"entity", "field_75457_a"} );
    	
        BlockPos blockpos = new BlockPos(entity_reflected);
        BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
        BlockPos.MutableBlockPos blockpos$mutableblockpos1 = new BlockPos.MutableBlockPos();
        
        // func_191531_b is getAllInBoxMutable in 1.12
        Iterable<BlockPos.MutableBlockPos> iterable = BlockPos.MutableBlockPos.getAllInBoxMutable(
        		new BlockPos(MathHelper.floor_double(entity_reflected.posX - 3.0D), MathHelper.floor_double(entity_reflected.posY - 6.0D), MathHelper.floor_double(entity_reflected.posZ - 3.0D)),
        		new BlockPos(MathHelper.floor_double(entity_reflected.posX + 3.0D), MathHelper.floor_double(entity_reflected.posY + 6.0D), MathHelper.floor_double(entity_reflected.posZ + 3.0D)));
        Iterator iterator = iterable.iterator();
        BlockPos blockpos1;

        while (true)
        {
            if (!iterator.hasNext())
            {
                return null;
            }

            blockpos1 = (BlockPos)iterator.next();

            if (!blockpos.equals(blockpos1))
            {
            	blockpos$mutableblockpos1.setPos(blockpos1.getX(), blockpos1.getY()-1, blockpos1.getZ());
            	
                Block block = entity_reflected.worldObj.getBlock(blockpos$mutableblockpos1.getX(), blockpos$mutableblockpos1.getY(), blockpos$mutableblockpos1.getZ());
            	//Block block = entity_reflected.worldObj.getBlock(p_147439_1_, p_147439_2_, p_147439_3_)
                boolean flag = block instanceof BlockLeaves || block == Blocks.log || block == Blocks.log2;

                if (
                		flag
                		&& isAirBlock(entity_reflected.worldObj, blockpos1)
                		&& isAirBlock(entity_reflected.worldObj, blockpos$mutableblockpos.setPos(blockpos1.getX(), blockpos1.getY()+1, blockpos1.getZ()))
                		)
                {
                    break;
                }
            }
        }

        return Vec3.createVectorHelper((double)blockpos1.getX(), (double)blockpos1.getY(), (double)blockpos1.getZ());
    }
    
    /*
     * Pasted in from 1.12's Entity class
     */
    public boolean isOverWater()
    {
    	EntityCreature entity_reflected = ReflectionHelper.getPrivateValue( EntityAIWander.class, this, new String[]{"entity", "field_75457_a"} );
        return entity_reflected.worldObj.handleMaterialAcceleration(entity_reflected.boundingBox.expand(0.0D, -20.0D, 0.0D).expand(-0.001D, -0.001D, -0.001D), Material.water, entity_reflected);
    }

    // Explicitly defined here so that you can also modify the mutable block value
    private boolean isAirBlock(World world, BlockPos pos)
    {
    	return world.isAirBlock(pos.getX(), pos.getY(), pos.getZ());
    }
}