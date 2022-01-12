package fr.craftyourliferp.blocks.tileentity;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.items.ModdedItems;
import net.minecraft.block.Block;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;

public class TileEntityCorpseFreezer extends TileEntity {

	public int direction;
			
	public byte state;
	
	public TileEntityCorpseFreezer()
	{

	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag)
    {
		direction = tag.getInteger("direction");
        state = tag.getByte("state");
    	super.readFromNBT(tag);
    }

	@Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("direction", direction);
        tag.setByte("state", state);
    	super.writeToNBT(tag);
    }
	
	public Packet getDescriptionPacket()
	{
	    NBTTagCompound compound = new NBTTagCompound();
	    this.writeToNBT(compound);
	    return new S35PacketUpdateTileEntity(this.xCoord,this.yCoord, this.zCoord, 0, compound);
	 }
	    
	 @Override
	 public void onDataPacket(NetworkManager net, S35PacketUpdateTileEntity pkt)
	 {
		 this.readFromNBT(pkt.func_148857_g());
	 }
	 
	 @SideOnly(Side.CLIENT)
	 public AxisAlignedBB getRenderBoundingBox()
	 {
	        AxisAlignedBB bb = INFINITE_EXTENT_AABB;
	        Block type = getBlockType();
	        if (type == ModdedItems.CorpseFreezerBlock)
	        {
	            bb = AxisAlignedBB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 2, yCoord + 2 ,zCoord + 2);
	        }
	        return bb;
	  }
	
}
