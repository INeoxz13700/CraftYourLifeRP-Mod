package astrotibs.notenoughpets.pathfinding.minecraft;

import astrotibs.notenoughpets.util.minecraft.BlockPos;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.init.Blocks;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.util.MathHelper;
import net.minecraft.world.IBlockAccess;

//Adapted back from 1.12. This class is used in the 1.8 to 1.11 versions of NEC
public class FlyingNodeProcessor extends WalkNodeProcessor
{
    public void init(IBlockAccess sourceIn, EntityLiving mob)
    {
        super.initProcessor(sourceIn, mob);
    }
    
    @Override
    public PathPoint getPathPointTo(Entity entity)
    {
        int i;

        if (this.getCanSwim() && entity.isInWater())
        {
            i = (int)entity.boundingBox.minY;
            BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos(MathHelper.floor_double(entity.posX), i, MathHelper.floor_double(entity.posZ));

            for (Block block = this.blockaccess.getBlock(blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getY(), blockpos$mutableblockpos.getZ());
            		block == Blocks.flowing_water || block == Blocks.water;
            		block = this.blockaccess.getBlock(blockpos$mutableblockpos.getX(), blockpos$mutableblockpos.getY(), blockpos$mutableblockpos.getZ()))
            {
                ++i;
                blockpos$mutableblockpos.setPos(MathHelper.floor_double(entity.posX), i, MathHelper.floor_double(entity.posZ));
            }
        }
        else
        {
            i = MathHelper.floor_double(entity.boundingBox.minY + 0.5D);
        }
        
        BlockPos blockpos1 = new BlockPos(entity);
        
        return super.openPoint(blockpos1.getX(), i, blockpos1.getZ());
    }

    /**
     * Returns PathPoint for given coordinates
     */
    @Override
    public PathPoint getPathPointToCoords(Entity entityIn, double x, double y, double z)
    {
        return super.openPoint(MathHelper.floor_double(x), MathHelper.floor_double(y), MathHelper.floor_double(z));
    }
    
    @Override
    public int findPathOptions(PathPoint[] pathOptions, Entity entityIn, PathPoint currentPoint, PathPoint targetPoint, float maxDistance)
    {
        int i = 0;
        PathPoint pathpoint = this.openPoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord + 1);
        PathPoint pathpoint1 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord);
        PathPoint pathpoint2 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord);
        PathPoint pathpoint3 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord, currentPoint.zCoord - 1);
        PathPoint pathpoint4 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord);
        PathPoint pathpoint5 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord);

        if (pathpoint != null && !pathpoint.isFirst && pathpoint.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint;
        }

        if (pathpoint1 != null && !pathpoint1.isFirst && pathpoint1.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint1;
        }

        if (pathpoint2 != null && !pathpoint2.isFirst && pathpoint2.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint2;
        }

        if (pathpoint3 != null && !pathpoint3.isFirst && pathpoint3.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint3;
        }

        if (pathpoint4 != null && !pathpoint4.isFirst && pathpoint4.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint4;
        }

        if (pathpoint5 != null && !pathpoint5.isFirst && pathpoint5.distanceTo(targetPoint) < maxDistance)
        {
            pathOptions[i++] = pathpoint5;
        }

        boolean flag = pathpoint3 == null;
        boolean flag1 = pathpoint == null;
        boolean flag2 = pathpoint2 == null;
        boolean flag3 = pathpoint1 == null;
        boolean flag4 = pathpoint4 == null;
        boolean flag5 = pathpoint5 == null;

        if (flag && flag3)
        {
            PathPoint pathpoint6 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord - 1);

            if (pathpoint6 != null && !pathpoint6.isFirst && pathpoint6.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint6;
            }
        }

        if (flag && flag2)
        {
            PathPoint pathpoint7 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord - 1);

            if (pathpoint7 != null && !pathpoint7.isFirst && pathpoint7.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint7;
            }
        }

        if (flag1 && flag3)
        {
            PathPoint pathpoint8 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord, currentPoint.zCoord + 1);

            if (pathpoint8 != null && !pathpoint8.isFirst && pathpoint8.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint8;
            }
        }

        if (flag1 && flag2)
        {
            PathPoint pathpoint9 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord, currentPoint.zCoord + 1);

            if (pathpoint9 != null && !pathpoint9.isFirst && pathpoint9.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint9;
            }
        }

        if (flag && flag4)
        {
            PathPoint pathpoint10 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord - 1);

            if (pathpoint10 != null && !pathpoint10.isFirst && pathpoint10.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint10;
            }
        }

        if (flag1 && flag4)
        {
            PathPoint pathpoint11 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord + 1, currentPoint.zCoord + 1);

            if (pathpoint11 != null && !pathpoint11.isFirst && pathpoint11.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint11;
            }
        }

        if (flag2 && flag4)
        {
            PathPoint pathpoint12 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord + 1, currentPoint.zCoord);

            if (pathpoint12 != null && !pathpoint12.isFirst && pathpoint12.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint12;
            }
        }

        if (flag3 && flag4)
        {
            PathPoint pathpoint13 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord + 1, currentPoint.zCoord);

            if (pathpoint13 != null && !pathpoint13.isFirst && pathpoint13.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint13;
            }
        }

        if (flag && flag5)
        {
            PathPoint pathpoint14 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord - 1);

            if (pathpoint14 != null && !pathpoint14.isFirst && pathpoint14.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint14;
            }
        }

        if (flag1 && flag5)
        {
            PathPoint pathpoint15 = this.openPoint(currentPoint.xCoord, currentPoint.yCoord - 1, currentPoint.zCoord + 1);

            if (pathpoint15 != null && !pathpoint15.isFirst && pathpoint15.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint15;
            }
        }

        if (flag2 && flag5)
        {
            PathPoint pathpoint16 = this.openPoint(currentPoint.xCoord + 1, currentPoint.yCoord - 1, currentPoint.zCoord);

            if (pathpoint16 != null && !pathpoint16.isFirst && pathpoint16.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint16;
            }
        }

        if (flag3 && flag5)
        {
            PathPoint pathpoint17 = this.openPoint(currentPoint.xCoord - 1, currentPoint.yCoord - 1, currentPoint.zCoord);

            if (pathpoint17 != null && !pathpoint17.isFirst && pathpoint17.distanceTo(targetPoint) < maxDistance)
            {
                pathOptions[i++] = pathpoint17;
            }
        }

        return i;
    }
    
}