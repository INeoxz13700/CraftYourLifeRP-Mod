package fr.craftyourliferp.utils;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class WorldUtils {

	
	public static EntityPlayer getPlayerByPersistenId(String UUID, World world)
	{
		List<EntityPlayer> players = world.playerEntities;
		for(int i = 0; i < players.size(); i++)
		{
			EntityPlayer player = players.get(i);
			if(player.getPersistentID().toString().equalsIgnoreCase(UUID))
			{
				return player;
			}
		}
		return null;
	}
	
	public static void createFlameArroundEntity(World world, Entity entity)
	{
		final int checkLimit = 100;
		int check = 0;
	
		for(int i = (int) (entity.posX - 5); i < entity.posX + 5; i++)
		{
			for(int j = (int) (entity.posZ -5); j < entity.posZ + 5; j++)
			{
				if(MathHelper.getRandomIntegerInRange(world.rand, 0, 1) == 1)
				{
					int posY = (int)entity.posY;
					while(world.getBlock(i, posY, j) == Blocks.air)
					{
						if(check++ >= checkLimit)
						{
							return;
						}
						posY--;	
					}
					if(world.getBlock(i, (int) posY+1, j) == Blocks.air)
					{
						world.setBlock(i, posY+1, j, Blocks.fire);
					}
				}

			}
		}
	}
	
	@SideOnly(Side.CLIENT)
	public static MovingObjectPosition getMouseOver(double distance, float p_78473_1_)
    {
		MovingObjectPosition movingObject = null;
		Minecraft mc = Minecraft.getMinecraft();
		Entity pointedEntity = null;
        if (mc.renderViewEntity != null)
        {
            if (mc.theWorld != null)
            {
            	mc.pointedEntity = null;
                double d0 = (double)distance;
                movingObject = mc.renderViewEntity.rayTrace(d0, p_78473_1_);
                double d1 = d0;
                Vec3 vec3 = mc.renderViewEntity.getPosition(p_78473_1_);


                if (movingObject != null)
                {
                    d1 = movingObject.hitVec.distanceTo(vec3);
                }

                Vec3 vec31 = mc.renderViewEntity.getLook(p_78473_1_);
                Vec3 vec32 = vec3.addVector(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0);
                pointedEntity = null;
                Vec3 vec33 = null;
                float f1 = 1.0F;
                List list = mc.theWorld.getEntitiesWithinAABBExcludingEntity(mc.renderViewEntity, mc.renderViewEntity.boundingBox.addCoord(vec31.xCoord * d0, vec31.yCoord * d0, vec31.zCoord * d0).expand((double)f1, (double)f1, (double)f1));
                double d2 = d1;

                for (int i = 0; i < list.size(); ++i)
                {
                    Entity entity = (Entity)list.get(i);
                    if (entity.canBeCollidedWith())
                    {
                        float f2 = entity.getCollisionBorderSize();
                        AxisAlignedBB axisalignedbb = entity.boundingBox.expand((double)f2, (double)f2, (double)f2);
                        MovingObjectPosition movingobjectposition = axisalignedbb.calculateIntercept(vec3, vec32);

                        if (axisalignedbb.isVecInside(vec3))
                        {
                            if (0.0D < d2 || d2 == 0.0D)
                            {
                                pointedEntity = entity;
                                vec33 = movingobjectposition == null ? vec3 : movingobjectposition.hitVec;
                                d2 = 0.0D;
                            }
                        }
                        else if (movingobjectposition != null)
                        {
                            double d3 = vec3.distanceTo(movingobjectposition.hitVec);

                            if (d3 < d2 || d2 == 0.0D)
                            {
                                if (entity == mc.renderViewEntity.ridingEntity && !entity.canRiderInteract())
                                {
                                    if (d2 == 0.0D)
                                    {
                                        pointedEntity = entity;
                                        vec33 = movingobjectposition.hitVec;
                                    }
                                }
                                else
                                {

                                    pointedEntity = entity;
                                    movingObject.typeOfHit = MovingObjectType.ENTITY;
                                    movingObject.entityHit = pointedEntity;
                                    vec33 = movingobjectposition.hitVec;
                                    d2 = d3;
                                }
                            }
                        }
                    }
                }
            }
        }
        return movingObject;
    }
	
	public static int getTopBlock(World world, int x, int z)
	{
		for (int j=255; j != 0; j--)
		{
			if(!CraftYourLifeRPMod.fireHandler.canPlaceFire(world, x, j, z))
			{
				return j;
			}
		}

		return 0;
	}
	
	public static int getBottomFromY(World world, int x, int y, int z)
	{
		while(world.getBlock(x, y, z) == Blocks.air)
		{
			y--;
		}
		return y+1;
	}
		
	
	public static Vec3 getRandomPointFromPlayerPos(World world, EntityPlayer player) //Y block top block at pos
	{
		int forceExit = 100;
		int x = (int) (player.posX + MathHelper.getRandomIntegerInRange(player.getRNG(), -10, 10));
		int z = (int) (player.posZ + MathHelper.getRandomIntegerInRange(player.getRNG(), -10, 10));
		
		int i = -2;
		for(i = -2; i < 2; i++)
		{
			if(CraftYourLifeRPMod.fireHandler.canPlaceFire(world,x, (int)player.posY+i, z))
			{
				break;
			}
			
		}
		
		Vec3 randomPos = Vec3.createVectorHelper(x,player.posY+i,z);
		return randomPos;
	}
	
	public static Vec3 getRandomPointFromWorld(World world) //y is first block air
	{
		int x = MathHelper.getRandomIntegerInRange(world.rand, -5000, 5000);
		int z = MathHelper.getRandomIntegerInRange(world.rand, -5000, 5000);

		Vec3 randomPos = Vec3.createVectorHelper(MathHelper.getRandomIntegerInRange(world.rand, -5000, 5000),WorldUtils.getTopBlock(world, x, z)+1,MathHelper.getRandomIntegerInRange(world.rand, -5000, 5000));
		return randomPos;
	}
	
}
