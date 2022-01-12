package astrotibs.notenoughpets.ai.minecraft;

import astrotibs.notenoughpets.ai.EntityAIFollowOwnerNEP;
import astrotibs.notenoughpets.util.minecraft.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.init.Blocks;

/**
 * Pasted in from 1.12.2
 */
public class EntityAIFollowOwnerFlying extends EntityAIFollowOwnerNEP
{
    public EntityAIFollowOwnerFlying(EntityTameable thePetIn, double followSpeedIn, float minDistIn, float maxDistIn)
    {
        super(thePetIn, followSpeedIn, minDistIn, maxDistIn);
    }
    
    public boolean isEmptyBlock(BlockPos pos)
    {
        Block block = this.world.getBlock(pos.getX(), pos.getY(), pos.getZ());
        return block == Blocks.air ? true : !(block.renderAsNormalBlock());
    }
    
    @Override
    protected boolean isTeleportFriendlyBlock(int x, int z, int y, int xOffset, int zOffset)
    {
    	BlockPos pos = new BlockPos(x + xOffset, y - 1, z + zOffset);
        Block block = this.world.getBlock(pos.getX(), pos.getY(), pos.getZ());
    	
        return ( (this.world.doesBlockHaveSolidTopSurface(this.world, pos.getX(), pos.getY(), pos.getZ()) || block.getMaterial() == Material.leaves)
        		&& this.isEmptyBlock(pos.up())
        		&& this.isEmptyBlock(pos.up(2)));
    }
}