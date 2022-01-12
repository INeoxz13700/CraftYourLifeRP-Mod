package fr.craftyourliferp.blocks.tileentity;

import net.minecraft.block.Block;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.NetworkManager;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S35PacketUpdateTileEntity;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;

public class TileEntityAtm extends TileEntity {

	public float rotation;
	
	public float scale;
	
	public TileEntityAtm()
	{

	}
	
	@Override
    public void readFromNBT(NBTTagCompound tag)
    {
        rotation = tag.getFloat("rotation");
        scale = tag.getFloat("scale");
        
    	super.readFromNBT(tag);
    }

	@Override
    public void writeToNBT(NBTTagCompound tag)
    {
        tag.setFloat("rotation", rotation);
        tag.setFloat("scale", scale);
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
