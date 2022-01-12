package fr.craftyourliferp.main;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;import java.util.stream.Collector;
import java.util.stream.Collectors;

import fr.craftyourliferp.utils.DataUtils;
import fr.craftyourliferp.utils.MathsUtils;
import fr.craftyourliferp.utils.ServerUtils;
import fr.craftyourliferp.utils.WorldUtils;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.world.MinecraftException;
import net.minecraft.world.World;
import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.storage.IChunkLoader;
import net.minecraft.world.storage.IPlayerFileData;
import net.minecraft.world.storage.ISaveHandler;
import net.minecraft.world.storage.WorldInfo;

public class WorldSelector {
	
	private Vec3 pos1;

	private Vec3 pos2;
	
	private Vec3 localPos1;
	
	private Vec3 localPos2;

	private World worldObj;
	
	private String selectorName;
	

	public WorldSelector(World world)
	{
		this.worldObj = world;
	}
	
	public Vec3 getSelectedPos1()
	{
		return pos1;
	}
	
	public Vec3 getSelectedPos2()
	{
		return pos2;
	}
	
	public void setSelectedPos1(Vec3 coordinates)
	{
		pos1 = coordinates;
		localPos1 = Vec3.createVectorHelper(0.0D, 0.0D, 0.0D);
	}
	
	
	public void setSelectedPos2(Vec3 coordinates)
	{
		pos2 = coordinates;
		localPos2 = Vec3.createVectorHelper(pos2.xCoord - pos1.xCoord, pos2.yCoord - pos1.yCoord, pos2.zCoord - pos1.zCoord);
	}
	
	public void resetSelection()
	{
		pos1 = null;
		pos2 = null;
		localPos1 = null;
		localPos2 = null;
	}
	
	public boolean entityInsideSelection(Entity entity)
	{
		return this.insideSelection(Vec3.createVectorHelper(entity.posX, entity.posY, entity.posZ));
	}
	
	public World getWorld()
	{
		return worldObj;
	}
	
	public boolean insideSelection(Vec3 coordinates)
	{
		if(pos1 == null || pos2 == null)
		{
			
			return false;
		}
				
		Vec3 ballLocalPos = Vec3.createVectorHelper((int)coordinates.xCoord - pos1.xCoord, (int)coordinates.yCoord - pos1.yCoord, (int)coordinates.zCoord - pos1.zCoord);
				
		
		if(localPos2.xCoord < 0 && localPos2.zCoord < 0 && localPos2.yCoord < 0)
		{
			if(ballLocalPos.xCoord <= localPos1.xCoord && ballLocalPos.zCoord <= localPos1.zCoord && ballLocalPos.yCoord <= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord >= localPos2.xCoord && ballLocalPos.zCoord >= localPos2.zCoord && ballLocalPos.yCoord >= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		
		else if(localPos2.xCoord < 0 && localPos2.zCoord >= 0 && localPos2.yCoord < 0)
		{
			if(ballLocalPos.xCoord <= localPos1.xCoord && ballLocalPos.zCoord >= localPos1.zCoord && ballLocalPos.yCoord <= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord >= localPos2.xCoord && ballLocalPos.zCoord <= localPos2.zCoord && ballLocalPos.yCoord >= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		
		else if(localPos2.xCoord >= 0 && localPos2.zCoord < 0 && localPos2.yCoord < 0)
		{
			if(ballLocalPos.xCoord >= localPos1.xCoord && ballLocalPos.zCoord <= localPos1.zCoord && ballLocalPos.yCoord <= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord <= localPos2.xCoord && ballLocalPos.zCoord >= localPos2.zCoord && ballLocalPos.yCoord >= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		
		else if(localPos2.xCoord >= 0 && localPos2.zCoord >= 0 && localPos2.yCoord < 0)
		{
			if(ballLocalPos.xCoord >= localPos1.xCoord && ballLocalPos.zCoord >= localPos1.zCoord && ballLocalPos.yCoord <= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord <= localPos2.xCoord && ballLocalPos.zCoord <= localPos2.zCoord && ballLocalPos.yCoord >= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		
		else if(localPos2.xCoord < 0 && localPos2.zCoord < 0 && localPos2.yCoord >= 0)
		{
			if(ballLocalPos.xCoord <= localPos1.xCoord && ballLocalPos.zCoord <= localPos1.zCoord && ballLocalPos.yCoord >= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord >= localPos2.xCoord && ballLocalPos.zCoord >= localPos2.zCoord && ballLocalPos.yCoord <= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		
		else if(localPos2.xCoord < 0 && localPos2.zCoord >= 0 && localPos2.yCoord >= 0)
		{
			if(ballLocalPos.xCoord <= localPos1.xCoord && ballLocalPos.zCoord >= localPos1.zCoord && ballLocalPos.yCoord >= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord >= localPos2.xCoord && ballLocalPos.zCoord <= localPos2.zCoord && ballLocalPos.yCoord <= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		
		else if(localPos2.xCoord >= 0 && localPos2.zCoord < 0 && localPos2.yCoord >= 0)
		{
			if(ballLocalPos.xCoord >= localPos1.xCoord && ballLocalPos.zCoord <= localPos1.zCoord && ballLocalPos.yCoord >= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord <= localPos2.xCoord && ballLocalPos.zCoord >= localPos2.zCoord && ballLocalPos.yCoord <= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		
		else if(localPos2.xCoord >= 0 && localPos2.zCoord >= 0 && localPos2.yCoord >= 0)
		{			
			if(ballLocalPos.xCoord >= localPos1.xCoord && ballLocalPos.zCoord >= localPos1.zCoord && ballLocalPos.yCoord >= localPos1.yCoord)
			{
				if(ballLocalPos.xCoord <= localPos2.xCoord && ballLocalPos.zCoord <= localPos2.zCoord && ballLocalPos.yCoord <= localPos2.yCoord)
				{
					return true;
				}
			}
		}
		return false;
	}
	
	public Vec3 getMiddlePoint()
	{
		return Vec3.createVectorHelper((pos1.xCoord + pos2.xCoord) / 2f, (pos1.yCoord + pos2.xCoord) / 2f, (pos1.zCoord + pos2.zCoord) / 2f);
	}
	
	public boolean selectionDefinied()
	{
		return pos1 != null && pos2 != null;
	}
	
	public void writeToNbt(String keyName, NBTTagCompound compound)
	{
		if(selectionDefinied())
		{
			DataUtils.writeVector3ToNbt(keyName + "-pos1", pos1, compound);
			DataUtils.writeVector3ToNbt(keyName + "-pos2", pos2, compound);	
		}
	}
	
	public void readFromNbt(String keyName,NBTTagCompound compound)
	{
		if(compound.hasKey(keyName + "-pos1-x"))
		{
			setSelectedPos1(DataUtils.readVector3FromNbt(keyName + "-pos1", compound));
			setSelectedPos2(DataUtils.readVector3FromNbt(keyName + "-pos2", compound));
		}
	}
	
	public void writeToNbt(NBTTagCompound compound)
	{
		if(selectorName != null)
		{
			compound.setString("RegionName", selectorName);
			if(worldObj == null)
			{
				compound.setString("WorldName", MinecraftServer.getServer().getEntityWorld().getWorldInfo().getWorldName());
			}
			else
			{
				compound.setString("WorldName", worldObj.getWorldInfo().getWorldName());
			}
			writeToNbt(selectorName,compound);
		}
	}
	
	public void readFromNbt(NBTTagCompound compound)
	{
		if(compound.hasKey("RegionName"))
		{
			selectorName = compound.getString("RegionName");
			if(compound.hasKey("WorldName"))
			{
				worldObj = ServerUtils.getWorldFromName(compound.getString("WorldName"));
			}
			else
			{
				worldObj = MinecraftServer.getServer().getEntityWorld();
			}
			readFromNbt(selectorName,compound);
		}
	}
	
	public List getEntitiesInSelectedArea(Class entityType)
	{
		List<Entity> entities = new ArrayList();
		for(int i = 0; i < worldObj.loadedEntityList.size(); i++)
		{
			Entity entity = (Entity) worldObj.loadedEntityList.get(i);

			if(entityInsideSelection(entity))
			{

				entities.add(entity);
			}
		}
		return entities;
	}
	
	public void clearBlocksInArea()
	{
		Vec3 floor_coordinates1 = Vec3.createVectorHelper(Math.floor(pos1.xCoord), Math.floor(pos1.yCoord), Math.floor(pos1.zCoord));
		Vec3 floor_coordinates2 = Vec3.createVectorHelper(Math.floor(pos2.xCoord), Math.floor(pos2.yCoord), Math.floor(pos2.zCoord));

		for(int y = 0; y <= (int)Math.abs(floor_coordinates2.yCoord - floor_coordinates1.yCoord); y++)
		{
			for(int x = 0; x <= (int)Math.abs(floor_coordinates2.xCoord - floor_coordinates1.xCoord); x++)
			{
				for(int z = 0; z <= (int)Math.abs(floor_coordinates2.zCoord - floor_coordinates1.zCoord); z++)
				{
					worldObj.setBlock((int)floor_coordinates1.xCoord + MathsUtils.Clamp((int)(floor_coordinates2.xCoord-floor_coordinates1.xCoord), -1, 1) * x, (int)floor_coordinates1.yCoord + MathsUtils.Clamp((int)(floor_coordinates2.yCoord-floor_coordinates1.yCoord), -1, 1) * y, (int)floor_coordinates1.zCoord + MathsUtils.Clamp((int)(floor_coordinates2.zCoord-floor_coordinates1.zCoord), -1, 1) * z, Blocks.air);	
				}
			}
		}
	}
	
	public Vec3 getRandomPosInSelection(boolean topBlock)
	{ 
		if(topBlock)
		{
			double posX = MathHelper.getRandomDoubleInRange(worldObj.rand, MathsUtils.Min((float)pos1.xCoord, (float)pos2.xCoord), MathsUtils.Max((float)pos1.xCoord, (float)pos2.xCoord));
			double posY = MathHelper.getRandomDoubleInRange(worldObj.rand, MathsUtils.Min((float)pos1.yCoord, (float)pos2.yCoord), MathsUtils.Max((float)pos1.yCoord, (float)pos2.yCoord));
			double posZ = MathHelper.getRandomDoubleInRange(worldObj.rand, MathsUtils.Min((float)pos1.zCoord, (float)pos2.zCoord), MathsUtils.Max((float)pos1.zCoord, (float)pos2.zCoord));

			return Vec3.createVectorHelper(posX, WorldUtils.getBottomFromY(worldObj, (int)posX, (int)posY, (int)posZ) , posZ);
		}
		return Vec3.createVectorHelper(MathHelper.getRandomDoubleInRange(worldObj.rand, pos1.xCoord, pos2.xCoord), MathHelper.getRandomDoubleInRange(worldObj.rand, pos1.yCoord, pos2.yCoord), MathHelper.getRandomDoubleInRange(worldObj.rand, pos1.zCoord, pos2.zCoord));
	}

	
	public void setName(String regionName) 
	{
		selectorName = regionName;
	}
	
	public String getName()
	{
		return selectorName;
	}
	
	@Override
	public boolean equals(Object o)
	{
		if(o instanceof WorldSelector)
		{
			WorldSelector selector = (WorldSelector)o;
			
	
			if(selector.worldObj.getWorldInfo().getWorldName().equals(worldObj.getWorldInfo().getWorldName()) && selector.selectorName.equals(selectorName) && (selector.pos1.toString().equals(pos1.toString()) || selector.pos1.toString().equals(pos2.toString())) && (selector.pos2.toString().equals(pos2.toString()) || selector.pos2.toString().equals(pos1.toString()))) return true;
		}
		return false;
	}
	
    public int hashCode()
    {
        return (int)pos1.xCoord + (int)pos1.zCoord << 8 + (int)pos1.yCoord << 16 + (int)pos2.xCoord + (int)pos2.zCoord << 8 + (int)pos2.yCoord << 16;
    }

}
