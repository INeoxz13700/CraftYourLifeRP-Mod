package astrotibs.notenoughpets.pathfinding.minecraft;

import astrotibs.notenoughpets.util.minecraft.BlockPos;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

// Adapted back from 1.12. This class is used in the 1.8 to 1.11 versions of NEC
public class PathNavigateFlying extends PathNavigate18
{
	protected NodeProcessor nodeProcessor;
    private boolean canEnterDoors;
    private boolean canBreakDoors;
    private boolean avoidsWater;
    private boolean canSwim;
    private boolean shouldAvoidWater;
    
    public PathNavigateFlying(EntityLiving entityLiving, World world)
    {
        super(entityLiving, world);
    }
    
    @Override
    protected PathFinder getPathFinder()
    {
        this.nodeProcessor = new FlyingNodeProcessor();
        this.setCanEnterDoors(true);
        return new PathFinder(this.nodeProcessor);
    }
    
    /**
     * If on ground or swimming and can swim
     */
    
    @Override
    protected boolean canNavigate()
    {
        return this.canFloat() && this.isInLiquid() || !this.theEntity.isRiding();
    }
    
    
    @Override
    protected Vec3 getEntityPosition()
    {
        return Vec3.createVectorHelper(this.theEntity.posX, this.theEntity.posY, this.theEntity.posZ);
    }
    
    /**
     * Returns the path to the given EntityLiving. Args : entity
     */
    @Override
    public PathEntity getPathToEntityLiving(Entity entityIn)
    {
        return this.getPathToPos(new BlockPos(entityIn));
    }
    
    
    @Override
    public void onUpdateNavigation()
    {
    	++this.totalTicks;
        
        if (!this.noPath())
        {
            if (this.canNavigate())//(canNavigate_boolean)
            {
            	this.pathFollow();
            }
            else if (this.currentPath != null && this.currentPath.getCurrentPathIndex() < this.currentPath.getCurrentPathLength())
            {
            	Vec3 vec3d = this.currentPath.getVectorFromIndex(this.theEntity, this.currentPath.getCurrentPathIndex());

                if (
                		MathHelper.floor_double(this.theEntity.posX) == MathHelper.floor_double(vec3d.xCoord)
                		&& MathHelper.floor_double(this.theEntity.posY) == MathHelper.floor_double(vec3d.yCoord)
                		&& MathHelper.floor_double(this.theEntity.posZ) == MathHelper.floor_double(vec3d.zCoord)
                		)
                {
                	this.currentPath.setCurrentPathIndex(this.currentPath.getCurrentPathIndex() + 1);
                }
            }
            
            // I don't think this exists in 1.12 anymore
            //this.debugPathFinding();

            if (!this.noPath())
            {
                Vec3 vec3d1 = this.currentPath.getPosition(this.theEntity);
                this.theEntity.getMoveHelper().setMoveTo(vec3d1.xCoord, vec3d1.yCoord, vec3d1.zCoord, this.speed);
            }
        }
    }

    /**
     * Checks if the specified entity can safely walk to the specified location.
     */
	@Override
    protected boolean isDirectPathBetweenPoints(Vec3 posVec31, Vec3 posVec32, int sizeX, int sizeY, int sizeZ)
    {
        int i = MathHelper.floor_double(posVec31.xCoord);
        int j = MathHelper.floor_double(posVec31.yCoord);
        int k = MathHelper.floor_double(posVec31.zCoord);
        double d0 = posVec32.xCoord - posVec31.xCoord;
        double d1 = posVec32.yCoord - posVec31.yCoord;
        double d2 = posVec32.zCoord - posVec31.zCoord;
        double d3 = d0 * d0 + d1 * d1 + d2 * d2;

        if (d3 < 1.0E-8D)
        {
            return false;
        }
        else
        {
            double d4 = 1.0D / Math.sqrt(d3);
            d0 = d0 * d4;
            d1 = d1 * d4;
            d2 = d2 * d4;
            double d5 = 1.0D / Math.abs(d0);
            double d6 = 1.0D / Math.abs(d1);
            double d7 = 1.0D / Math.abs(d2);
            double d8 = (double)i - posVec31.xCoord;
            double d9 = (double)j - posVec31.yCoord;
            double d10 = (double)k - posVec31.zCoord;

            if (d0 >= 0.0D)
            {
                ++d8;
            }

            if (d1 >= 0.0D)
            {
                ++d9;
            }

            if (d2 >= 0.0D)
            {
                ++d10;
            }

            d8 = d8 / d0;
            d9 = d9 / d1;
            d10 = d10 / d2;
            int l = d0 < 0.0D ? -1 : 1;
            int i1 = d1 < 0.0D ? -1 : 1;
            int j1 = d2 < 0.0D ? -1 : 1;
            int k1 = MathHelper.floor_double(posVec32.xCoord);
            int l1 = MathHelper.floor_double(posVec32.yCoord);
            int i2 = MathHelper.floor_double(posVec32.zCoord);
            int j2 = k1 - i;
            int k2 = l1 - j;
            int l2 = i2 - k;

            while (j2 * l > 0 || k2 * i1 > 0 || l2 * j1 > 0)
            {
                if (d8 < d10 && d8 <= d9)
                {
                    d8 += d5;
                    i += l;
                    j2 = k1 - i;
                }
                else if (d9 < d8 && d9 <= d10)
                {
                    d9 += d6;
                    j += i1;
                    k2 = l1 - j;
                }
                else
                {
                    d10 += d7;
                    k += j1;
                    l2 = i2 - k;
                }
            }

            return true;
        }
    }
    
    //Renamed from setCanOpenDoors in 1.12
    public void setCanBreakDoors(boolean p_192879_1_)
    {
        this.canBreakDoors = p_192879_1_;
    }

    public void setCanEnterDoors(boolean p_192878_1_)
    {
        this.canEnterDoors = p_192878_1_;
    }

    public void setCanFloat(boolean p_192877_1_)
    {
        this.canSwim = p_192877_1_;
    }
    
    public void setAvoidsWater(boolean avoidsWaterIn)
    {
        this.avoidsWater = avoidsWaterIn;
    }

    public boolean canFloat()
    {
        return this.canSwim;
    }
    
    // Not used until 1.9 - Added condition in RandomPositionGenerator.findRandomTargetBlock
    public boolean canEntityStandOnPos(BlockPos pos)
    {
    	World worldObj_reflected = this.worldObj;
    	
        return worldObj_reflected.isSideSolid(pos.getX(), pos.getY()-1, pos.getZ(), ForgeDirection.UP);
    }
    
    public boolean getAvoidsWater()
    {
        return this.avoidsWater;
    }
    
}