package fr.craftyourliferp.capture;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import cpw.mods.fml.common.registry.EntityRegistry;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.utils.ICallback;
import fr.craftyourliferp.utils.ISaveHandler;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.nbt.NBTTagString;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import noppes.npcs.controllers.ServerCloneController;
import noppes.npcs.entity.EntityNPCInterface;
import scala.sys.process.processInternal;

public class CaptureProcess implements ISaveHandler
{

	private int tickCapture;
	
	public long lastCaptureTimer;
	
	@CaptureProperty
	private int captureDurationInSeconds;
	
	@CaptureProperty
	private int captureAvaibleTimerInSeconds;
	
	@CaptureProperty
	private int captureMaxEntities;
	
	private HashMap<String, EntityPlayer> playersInCapture;
	
	private List<Entity> captureEntities;
	
	private List<String> captureEntitiesId;
	
	public ICallback onCaptureUpdate = new ICallback()
	{

		@Override
		public void call() {}

		@Override
		public void call(EntityPlayer player) { }

		@Override
		public void call(World world)
		{ 
	
		}
		
	};
	
	public ICallback onRegionCaptureStartCallback = new ICallback()
	{

		@Override
		public void call() {}

		@Override
		public void call(EntityPlayer player) 
		{
		
		}

		@Override
		public void call(World world) { }
		
	};
	
	public ICallback onRegionCapturedCallback = new ICallback()
	{

		@Override
		public void call() { }

		@Override
		public void call(EntityPlayer player) { }

		@Override
		public void call(World world) 
		{ 
			WorldSelector selector = new WorldSelector(world);
			selector.setSelectedPos1(Vec3.createVectorHelper(-196, 69, 1086));
			selector.setSelectedPos2(Vec3.createVectorHelper(-196, 71, 1082));
			selector.clearBlocksInArea();
			for(EntityPlayer player : getPlayersInCapture().values())
			{
				ServerUtils.sendMessage("§aZone sous contrôle!", player,1000,0);
			}
		}
		
	};
	
	public ICallback onPlayerEnterRegionCallback = new ICallback()
	{

		@Override
		public void call() {}

		@Override
		public void call(EntityPlayer player) 
		{	
			if(getPlayersInCapture().size() == 0)
			{
				onRegionCaptureStartCallback.call();
			}
			
			ServerUtils.sendMessage("§cDes renforts arrivent prenez le contrôle de la zone!", player,1000,0);
			
			getPlayersInCapture().put(player.getCommandSenderName(), player);
		}

		@Override
		public void call(World world) { }
		
	};
	
	public ICallback onPlayerLeaveRegionCallback = new ICallback()
	{
		@Override
		public void call() {}

		@Override
		public void call(EntityPlayer player) 
		{	
			getPlayersInCapture().remove(player.getCommandSenderName());
			if(getPlayersInCapture().size() == 0)
			{
				clearEntities();
				setTickCapture(0);
			}
		}

		@Override
		public void call(World world) { }
	};
	
	
	protected CaptureProcess()
	{
		this.setTickCapture(0);
		this.lastCaptureTimer = 0;
		this.setPlayersInCapture(new HashMap());
		this.captureEntities = new ArrayList();
		this.captureEntitiesId = new ArrayList();
	}
	
	public CaptureProcess(int captureAvaibleTimerInSeconds, int captureDurationInSeconds, int captureMaxEntities)
	{
		this.captureDurationInSeconds = captureDurationInSeconds;
		this.captureAvaibleTimerInSeconds = captureAvaibleTimerInSeconds;
		this.captureMaxEntities = captureMaxEntities;
		this.setTickCapture(0);
		this.setPlayersInCapture(new HashMap());
		this.captureEntities = new ArrayList();
		this.captureEntitiesId = new ArrayList();
	}

	public boolean isCapturable(World world)
	{
		long elapsedTime = System.currentTimeMillis() - lastCaptureTimer;
		if(elapsedTime < captureAvaibleTimerInSeconds*1000 && !inCondition(world)) return false;
		
		return true;
	}
	
	public boolean isCaptured()
	{
		return getTickCapture() >= captureDurationInSeconds * 20;
	}
	
	public void update(World world)
	{
		onCaptureUpdate.call(world);
		setTickCapture(getTickCapture() + getPlayersInCapture().size());

		if(getTickCapture() % 40 == 0)
		{
			for(EntityPlayer player : getPlayersInCapture().values())
			{
				float capturePercentage = (getTickCapture() / (float) (captureDurationInSeconds * 20)) * 100;
				ServerUtils.sendMessage("Contrôle de la zone : §e" + (int)capturePercentage + "§f%", player, 500, 1);
			}
			
			List<Entity> toRemove = new ArrayList();
			for(Entity entity : captureEntities)
			{
				if(entity.isDead)
				{
					toRemove.add(entity);
				}
			}
			captureEntities.removeAll(toRemove);
			
			
			if(captureEntities.size() < captureMaxEntities)
			{
				List<String> entities = getCaptureEntitiesId();
				if(entities.size() == 0) return;
				
				String entityId = entities.get(MathHelper.getRandomIntegerInRange(world.rand, 0, entities.size()-1));
				Entity entity = null;
				
				if(entityId.startsWith("customnpcs"))
				{
					NBTTagCompound compound = ServerCloneController.Instance.getCloneData(MinecraftServer.getServer(), entityId.split(":")[1], 1);
					entity = EntityList.createEntityFromNBT(compound, world);
				}
				else
				{
					entity = EntityList.createEntityByName(entityId, world);
				}
				
				if(entity != null)
				{
					WorldSelector selector = new WorldSelector(world);
					selector.setSelectedPos1(Vec3.createVectorHelper(-172, 67, 1090));
					selector.setSelectedPos2(Vec3.createVectorHelper(-190, 74, 1079));
					Vec3 spawnPoint = selector.getRandomPosInSelection(true);
					if(world.getBlock((int)spawnPoint.xCoord, (int) spawnPoint.yCoord, (int)spawnPoint.zCoord) == Blocks.air);
					{
						entity.setPosition(spawnPoint.xCoord, spawnPoint.yCoord, spawnPoint.zCoord);
						if (entity instanceof EntityNPCInterface) {
							 EntityNPCInterface npc = (EntityNPCInterface)entity;
							 npc.ai.startPos = new int[] { MathHelper.floor_double(-176), MathHelper.floor_double(69), MathHelper.floor_double(1085) };
						} 
						world.spawnEntityInWorld(entity);
						captureEntities.add(entity);
					}
				}
				
			}
		}		
	}
	
	public boolean inCondition(World world)
	{
	
		if(ServerUtils.getForceOrderCount(world) > 2)
		{
			return true;
		}
		
		return false;
	}
	
	public boolean playerInCondition(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
		
		if(extendedPlayer == null) return false;
		
		/*if(ServerUtils.isIlegalJob(extendedPlayer.serverData.job))
		{
			return true;
		}
		
		return false;
		*/
		
		return true;
	}
	
	public boolean playerCaptureAlready(EntityPlayer player)
	{
		return getPlayersInCapture().containsKey(player.getCommandSenderName());
	}
	
	public int getCaptureDuration()
	{
		return captureDurationInSeconds;
	}
	
	public int getPlayersCount()
	{
		return getPlayersInCapture().size();
	}
	
	public boolean entityCanSpawn(World world, Vec3 coordinates)
	{
		return world.getBlock((int)coordinates.xCoord, (int)coordinates.yCoord, (int)coordinates.zCoord) == Blocks.air;
	}
	
	public List<String> getCaptureEntitiesId()
	{
		return captureEntitiesId;
	}
	
	public void clearEntities()
	{
		for(Entity entity : captureEntities)
		{
			entity.setDead();
		}
		captureEntities.clear();
	}
	
	public int getTickCapture() {
		return tickCapture;
	}

	public void setTickCapture(int tickCapture) {
		this.tickCapture = tickCapture;
	}

	public HashMap<String, EntityPlayer> getPlayersInCapture() {
		return playersInCapture;
	}

	public void setPlayersInCapture(HashMap<String, EntityPlayer> playersInCapture) {
		this.playersInCapture = playersInCapture;
	}
	
	
	public static CaptureProcess copyFrom(CaptureProcess copy) throws IllegalArgumentException, IllegalAccessException
	{
		CaptureProcess captureProcess = new CaptureProcess();
		
		Field[] copyFields = copy.getClass().getDeclaredFields();

		for(int i = 0; i < copyFields.length; i++)
		{
			Field field = copyFields[i];
			if(field.isAnnotationPresent(CaptureProperty.class))
			{
				field.set(captureProcess, field.get(copy));
			}
		}
		
		return captureProcess;
	}
	
	@Override
	public void writeToNbt(NBTTagCompound compound)
	{
		compound.setInteger("CaptureType", CraftYourLifeRPMod.captureHander.getCaptureType(this));
		compound.setInteger("CaptureTick", getTickCapture());
		compound.setLong("LastCaptureTimer", lastCaptureTimer);
		NBTTagList tagList = new NBTTagList();
		for(String entity : captureEntitiesId)
		{
			if(entity.isEmpty() || entity == null) continue;
			
			NBTTagString string = new NBTTagString(entity);
			tagList.appendTag(string);
		}
		compound.setTag("EntitiesId", tagList);
	}


	@Override
	public void readFromNbt(NBTTagCompound compound) {
		setTickCapture(compound.getInteger("CaptureTick"));
		lastCaptureTimer = compound.getLong("LastCaptureTimer");
		NBTTagList tagList = (NBTTagList) compound.getTag("EntitiesId");
		for(int i = 0; i < tagList.tagCount(); i++)
		{
			captureEntitiesId.add(tagList.getStringTagAt(i));
		}
	}
	
	@Override
	public boolean equals(Object obj)
	{
		if(obj instanceof CaptureProcess)
		{
			CaptureProcess process = (CaptureProcess)obj;
			if(process.captureDurationInSeconds == captureDurationInSeconds && process.captureAvaibleTimerInSeconds == captureAvaibleTimerInSeconds && process.getClass().equals(getClass())) return true;
		}
		
		return false;
	}
	
	@Override
	public int hashCode()
	{
		return captureDurationInSeconds + captureAvaibleTimerInSeconds;
	}

}
