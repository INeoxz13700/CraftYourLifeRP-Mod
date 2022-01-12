package fr.craftyourliferp.fire;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.utils.ServerUtils;
import fr.craftyourliferp.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBush;
import net.minecraft.block.BlockContainer;
import net.minecraft.block.BlockFlower;
import net.minecraft.entity.effect.EntityLightningBolt;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;

public class FireHandler {

	public List<Fire> fires = new ArrayList<Fire>();
	
	public final static int maxFiresInWorld = 1;
	
	public void startFireFromCoordinates(World world, int x, int y, int z, String cause, int maxFirePoint)
	{
		if(world.isRemote)
		{
			return;
		}
		
		if(getFireInCoordinates(x,z) != null)
		{
			return;
		}
		
		if(fires.size() > maxFiresInWorld)
		{
			return;
		}
	
				
		if(canPlaceFire(world,x,y,z))
		{
			Fire fire = new Fire(cause,maxFirePoint,x,z);
			
			FirePoint mainPoint = new FirePoint(fire,x,y,z, null);
			fire.points.add(mainPoint);
			
			world.setBlock(x, y, z, Blocks.fire);
			mainPoint.wasFire = true;

			fire.addPointFrom(world, mainPoint);

			this.fires.add(fire);

		}
	}
	
	public void removeFire(Fire fire)
	{
		fires.remove(fire);
		ServerUtils.broadcastMessage("§4[Attention] §cle feu aux coordonnées §f(" + fire.getX() + "," + fire.getZ() +") §cest sous contrôle", (byte)3);
	}
	
	public boolean canPlaceFire(World world, int x, int y, int z)
	{
		Block block = world.getBlock(x, y, z);
		if(block == Blocks.air || (block instanceof BlockBush && !(block instanceof BlockFlower)))
		{
			return true;
		}
		return false;
	}
	
	public Fire getFireInCoordinates(int x, int z)
	{
		for(Fire fire : fires)
		{
			if(fire.getX() == x && fire.getZ() == z)
			{
				return fire;
			}
		}
		return null;
	}
	
	public boolean blockIsInflammable(World world, int x, int y, int z)
	{
		return world.getBlock(x, y, z).isNormalCube();
	}
		

	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if(event.phase == Phase.END)
		{
			if(!event.world.isRemote)
			{	
				int count = ServerUtils.getPlayerCountForJob(event.world, "pompier");
				if(count > 0)
				{
					if(event.world == MinecraftServer.getServer().getEntityWorld())
					{
						for(int i = 0; i < fires.size(); i++)
						{
							Fire fire = fires.get(i);
									
							fire.tick(event.world);
							
						}

						WorldData worldData = WorldData.get(event.world);
						if(event.world.getTotalWorldTime() - worldData.lastFireNaturalTime >= 20 * 60 * 60)
						{
							if(worldData.getFires().size() == 0) return;
							
							WorldSelector selector = worldData.getFires().get(MathHelper.getRandomIntegerInRange(event.world.rand, 0, worldData.getFires().size()-1));
							
							Vec3 point = selector.getMiddlePoint();
							
							int x = (int)point.xCoord;
							int z = (int)point.zCoord;
							int y = WorldUtils.getTopBlock(event.world, x, z);
	
							Block block = event.world.getBlock((int)x, (int)y, (int)z);
							if(block == Blocks.grass)
							{
								worldData.setLastFireTime(event.world.getTotalWorldTime(),1);
		
								startFireFromCoordinates(event.world, (int)x, (int) y+1,  (int)z, "Catastrophe naturelle",500);
							}
						}
					}
				}
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(ServerUtils.getPlayerCountForJob(event.player.worldObj, "pompier") > 0)
		{
			if(!event.player.worldObj.isRemote && event.phase == Phase.END)
			{
					if(event.player.worldObj.isRaining())
					{
						WorldData worldData = WorldData.get(event.player.worldObj);
						

						if(worldData.getFires().size() == 0) return;
						
						WorldSelector selector = worldData.getFires().get(MathHelper.getRandomIntegerInRange(event.player.getRNG(), 0, worldData.getFires().size()-1));
						Vec3 point = selector.getMiddlePoint();
						int x = (int)point.xCoord;
						int z = (int)point.zCoord;
						int y = WorldUtils.getTopBlock(event.player.worldObj, x, z);
	
						float flammability = 0f;
						if(event.player.worldObj.getBlock(x, y, z) == Blocks.grass)
						{
							flammability = 0.001f;
						}
						else
						{
							flammability = (Blocks.fire.getFlammability(event.player.worldObj.getBlock(x, y, z)) / 100f);
	
						}
						if(flammability > 0)
						{
							WorldData data = WorldData.get(event.player.worldObj);
							
							if(event.player.worldObj.getTotalWorldTime() - data.lastFireThunderTime < 20 * 60 * 30)
							{
								return;
							}
							
							data.setLastFireTime(event.player.worldObj.getTotalWorldTime(), 2);
							double randomDouble = MathHelper.getRandomDoubleInRange(event.player.worldObj.rand, 0, 1);
							if(randomDouble >= 0.5 - flammability && randomDouble <= 0.5 + flammability)
							{

								if(canPlaceFire(event.player.worldObj,x, y+1, z))
								{
									event.player.worldObj.spawnEntityInWorld(new EntityLightningBolt(event.player.worldObj,x, y,z));
									startFireFromCoordinates(event.player.worldObj, x, y+1, z,"orage",500);
								}
							}
							else
							{
								if(event.player.worldObj.getBlock(x, y+1, z) == Blocks.fire)
								{
									event.player.worldObj.setBlock(x, y+1, z, Blocks.air);
								}
							}
						}
					}
			}
		}
	}
	
}
