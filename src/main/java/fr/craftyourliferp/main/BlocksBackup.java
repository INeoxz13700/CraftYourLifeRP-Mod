package fr.craftyourliferp.main;

import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.data.BlockData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.utils.DataUtils;
import fr.craftyourliferp.utils.ISaveHandler;
import net.minecraft.block.Block;
import net.minecraft.block.BlockDoor;
import net.minecraft.item.Item;
import net.minecraft.item.ItemDoor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class BlocksBackup implements ISaveHandler {
	
	private World world;
	
	private	List<BlockData> blocks = new ArrayList<BlockData>();
	
	public final static int updateTime = 2; //In seconds
	
	public BlocksBackup()
	{
		this.world = MinecraftServer.getServer().getEntityWorld();
	}
	
	public void addBlock(BlockData blockData)
	{
		Block block = world.getBlock((int)blockData.position.xCoord, (int)blockData.position.yCoord, (int)blockData.position.zCoord);
		blocks.add(blockData);
		WorldData.get(world).markDirty();
	}
	
	public void update()
	{
		List<BlockData> toRemove = new ArrayList<BlockData>();

		for(BlockData bData : blocks)
		{
			if((System.currentTimeMillis() - bData.timeSinceBlockWaiting) / 1000 >= bData.timeToBackup)
			{
				toRemove.add(bData);
				regenBlock(bData);
			}
		}
		
		blocks.removeAll(toRemove);
	}
	
	private void regenBlock(BlockData blockData)
	{
		Block block = Block.getBlockById(blockData.blockId);
		if(block instanceof BlockDoor)
		{
			ItemStack is = new ItemStack(Item.getItemFromBlock(block));
			ItemDoor.placeDoorBlock(world, (int)blockData.position.xCoord, (int)blockData.position.yCoord, (int)blockData.position.zCoord, blockData.metaData, block);
		}
	}
	
	@Override
	public void writeToNbt(NBTTagCompound compound) {
		NBTTagList blockTags = new NBTTagList();
		
		for(BlockData block : blocks)
		{
			NBTTagCompound blockTag = new NBTTagCompound();
			block.writeToNbt(blockTag);
			blockTags.appendTag(blockTag);
		}
		
		compound.setTag("BlocksData", blockTags);
	}

	@Override
	public void readFromNbt(NBTTagCompound compound) {
		NBTTagList blockTags = (NBTTagList) compound.getTag("BlocksData");
		
		for(int i = 0; i < blockTags.tagCount(); i++)
		{
			NBTTagCompound blockTag = blockTags.getCompoundTagAt(i);
			BlockData blockData = new BlockData();
			blockData.readFromNbt(blockTag);
			blocks.add(blockData);
		}
	}
	
}



