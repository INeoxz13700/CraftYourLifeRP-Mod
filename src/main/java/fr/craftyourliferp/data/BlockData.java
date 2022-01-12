package fr.craftyourliferp.data;

import fr.craftyourliferp.utils.DataUtils;
import fr.craftyourliferp.utils.ISaveHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.Vec3;

public class BlockData implements ISaveHandler
{
		
		public int timeToBackup;
		
		public long timeSinceBlockWaiting;
		
		public Vec3 position;
		
		public int blockId;
		
		public byte metaData;
		
		public BlockData()
		{
			
		}
		
		public BlockData(int blockId,byte metaData, Vec3 position, int timeToBackup)
		{
			this.blockId = blockId;
			this.position = position;
			this.timeToBackup = timeToBackup;
			this.timeSinceBlockWaiting = System.currentTimeMillis();
			this.metaData = metaData;
		}

		@Override
		public void writeToNbt(NBTTagCompound compound)
		{
			compound.setInteger("TimeToRegen", timeToBackup);
			compound.setLong("TimeSinceBlockWaiting", timeSinceBlockWaiting);
			DataUtils.writeVector3ToNbt("Position", position, compound);
			compound.setByte("MetaData", metaData);
			compound.setInteger("BlockId", blockId);
		}

		@Override
		public void readFromNbt(NBTTagCompound compound) {
			timeToBackup = compound.getInteger("TimeToRegen");
			timeSinceBlockWaiting = compound.getLong("TimeSinceBlockWaiting");
			position = DataUtils.readVector3FromNbt("Position", compound);
			metaData = compound.getByte("MetaData");
			blockId = compound.getInteger("BlockId");
		}
		
}