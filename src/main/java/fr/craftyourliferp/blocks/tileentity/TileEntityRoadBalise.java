package fr.craftyourliferp.blocks.tileentity;

import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;

public class TileEntityRoadBalise extends TileEntity {

	public int direction;
	
	
	public TileEntityRoadBalise()
	{

	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag)
    {
        direction = tag.getInteger("Direction");
        
    	super.readFromNBT(tag);
    }

	@Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setInteger("Direction", direction);
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
	
}
