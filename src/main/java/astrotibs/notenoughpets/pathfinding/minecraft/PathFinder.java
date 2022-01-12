package astrotibs.notenoughpets.pathfinding.minecraft;

import astrotibs.notenoughpets.util.minecraft.BlockPos;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.entity.Entity;
import net.minecraft.pathfinding.Path;
import net.minecraft.pathfinding.PathEntity;
import net.minecraft.pathfinding.PathPoint;
import net.minecraft.world.IBlockAccess;

public class PathFinder
{
    /** The path being generated */
    private Path path = new Path();
    /** Selection of path points to add to the path */
    private PathPoint[] pathOptions = new PathPoint[32];
    private NodeProcessor nodeProcessor;

    public PathFinder(NodeProcessor nodeProcessorIn)
    {
        this.nodeProcessor = nodeProcessorIn;
    }

    /**
     * Creates a path from one entity to another within a minimum distance
     */
    public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityFrom, Entity entityTo, float dist)
    {
        return this.createEntityPathTo(blockaccess, entityFrom, entityTo.posX, entityTo.boundingBox.minY, entityTo.posZ, dist);
    }

    /**
     * Creates a path from an entity to a specified location within a minimum distance
     */
    public PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, BlockPos targetPos, float dist)
    {
        return this.createEntityPathTo(blockaccess, entityIn, (double)((float)targetPos.getX() + 0.5F), (double)((float)targetPos.getY() + 0.5F), (double)((float)targetPos.getZ() + 0.5F), dist);
    }

    /**
     * Internal implementation of creating a path from an entity to a point
     */
    private PathEntity createEntityPathTo(IBlockAccess blockaccess, Entity entityIn, double x, double y, double z, float distance)
    {
        this.path.clearPath();
        this.nodeProcessor.initProcessor(blockaccess, entityIn);
        PathPoint pathpoint = this.nodeProcessor.getPathPointTo(entityIn);
        PathPoint pathpoint1 = this.nodeProcessor.getPathPointToCoords(entityIn, x, y, z);
        PathEntity pathentity = this.addToPath(entityIn, pathpoint, pathpoint1, distance);
        this.nodeProcessor.postProcess();
        return pathentity;
    }

    /**
     * Adds a path from start to end and returns the whole path
     */
    private PathEntity addToPath(Entity entityIn, PathPoint pathpointStart, PathPoint pathpointEnd, float maxDistance)
    {
    	
        //pathpointStart.totalPathDistance = 0.0F;
    	ReflectionHelper.setPrivateValue(PathPoint.class, pathpointStart, 0.0F, new String[]{"totalPathDistance", "field_75836_e"});
    	//pathpointStart.distanceToNext = pathpointStart.distanceToSquared(pathpointEnd);
    	ReflectionHelper.setPrivateValue(PathPoint.class, pathpointStart, pathpointStart.distanceToSquared(pathpointEnd), new String[]{"distanceToNext", "field_75833_f"});
        //pathpointStart.distanceToTarget = pathpointStart.distanceToNext;
        ReflectionHelper.setPrivateValue(PathPoint.class, pathpointStart, pathpointStart.distanceToSquared(pathpointEnd), new String[]{"distanceToTarget", "field_75834_g"});
        this.path.clearPath();
        this.path.addPoint(pathpointStart);
        PathPoint pathpoint = pathpointStart;

        while (!this.path.isPathEmpty())
        {
            PathPoint pathpoint1 = this.path.dequeue();

            if (pathpoint1.equals(pathpointEnd))
            {
                return this.createEntityPath(pathpointStart, pathpointEnd);
            }

            if (pathpoint1.distanceToSquared(pathpointEnd) < pathpoint.distanceToSquared(pathpointEnd))
            {
                pathpoint = pathpoint1;
            }

            pathpoint1.isFirst = true;
            int i = this.nodeProcessor.findPathOptions(this.pathOptions, entityIn, pathpoint1, pathpointEnd, maxDistance);

            for (int j = 0; j < i; ++j)
            {
            	float pathpoint1_totalPathDistance_reflected = ReflectionHelper.getPrivateValue(PathPoint.class, pathpoint1, new String[]{"totalPathDistance", "field_75836_e"});
            	
            	PathPoint pathpoint2 = this.pathOptions[j];
                
            	float pathpoint2_totalPathDistance_reflected = ReflectionHelper.getPrivateValue(PathPoint.class, pathpoint2, new String[]{"totalPathDistance", "field_75836_e"});
            	
            	float f = pathpoint1_totalPathDistance_reflected + pathpoint1.distanceToSquared(pathpoint2);

                if (f < maxDistance * 2.0F && (!pathpoint2.isAssigned() || f < pathpoint2_totalPathDistance_reflected))
                {
                    //pathpoint2.previous = pathpoint1;
                    ReflectionHelper.setPrivateValue(PathPoint.class, pathpoint2, pathpoint1, new String[]{"previous", "field_75841_h"});
                    //pathpoint2.totalPathDistance = f;
                    ReflectionHelper.setPrivateValue(PathPoint.class, pathpoint2, f, new String[]{"totalPathDistance", "field_75836_e"});
                    //pathpoint2.distanceToNext = pathpoint2.distanceToSquared(pathpointEnd);
                    ReflectionHelper.setPrivateValue(PathPoint.class, pathpoint2, pathpoint2.distanceToSquared(pathpointEnd), new String[]{"distanceToNext", "field_75833_f"});
                    
                    if (pathpoint2.isAssigned())
                    {
                        this.path.changeDistance(pathpoint2, f + pathpoint2.distanceToSquared(pathpointEnd));//pathpoint2.totalPathDistance + pathpoint2.distanceToNext);
                    }
                    else
                    {
                        //pathpoint2.distanceToTarget = pathpoint2.totalPathDistance + pathpoint2.distanceToNext;
                        ReflectionHelper.setPrivateValue(PathPoint.class, pathpoint2, f + pathpoint2.distanceToSquared(pathpointEnd), new String[]{"distanceToTarget", "field_75834_g"});
                        this.path.addPoint(pathpoint2);
                    }
                }
            }
        }

        if (pathpoint == pathpointStart)
        {
            return null;
        }
        else
        {
            return this.createEntityPath(pathpointStart, pathpoint);
        }
    }

    /**
     * Returns a new PathEntity for a given start and end point
     */
    private PathEntity createEntityPath(PathPoint start, PathPoint end)
    {
        int i = 1;

        for (
        		PathPoint pathpoint = end;
        		ReflectionHelper.getPrivateValue(PathPoint.class, pathpoint, new String[]{"previous", "field_75841_h"}) != null; //pathpoint.previous != null;
        		pathpoint = ReflectionHelper.getPrivateValue(PathPoint.class, pathpoint, new String[]{"previous", "field_75841_h"}) //pathpoint.previous
        				)
        {
            ++i;
        }

        PathPoint[] apathpoint = new PathPoint[i];
        PathPoint pathpoint1 = end;
        --i;

        for (
        		apathpoint[i] = end;
        		ReflectionHelper.getPrivateValue(PathPoint.class, pathpoint1, new String[]{"previous", "field_75841_h"}) != null;//pathpoint1.previous != null;
        		apathpoint[i] = pathpoint1
        				)
        {
            pathpoint1 = ReflectionHelper.getPrivateValue(PathPoint.class, pathpoint1, new String[]{"previous", "field_75841_h"});//pathpoint1.previous;
            --i;
        }

        return new PathEntity(apathpoint);
    }
}