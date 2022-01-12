package fr.craftyourliferp.penalty;

import java.util.UUID;

import cpw.mods.fml.common.network.ByteBufUtils;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.nbt.NBTTagCompound;

public class VehiclePenalty extends Penalty {

	private int vehicleId;
	
	private boolean isImpound = false;
	
	public VehiclePenalty()
	{

	}
	
	public VehiclePenalty(int vehicleId, int price, String reason, String treasuryOwner, boolean isImpound)
	{
		super(price,reason,treasuryOwner);
		this.vehicleId = vehicleId;
		this.isImpound = isImpound;	
	}
	
	public int getVehicleId()
	{
		return vehicleId;
	}
	
	public boolean isImpound()
	{
		return isImpound;
	}
	
	@Override
	public void writeToNbt(NBTTagCompound compound)
	{
		super.writeToNbt(compound);
		
		compound.setInteger("VehicleId", vehicleId);
		compound.setBoolean("IsImpound", isImpound);

	}

	@Override
	public void readFromNbt(NBTTagCompound compound) 
	{
		super.readFromNbt(compound);
				
		vehicleId = compound.getInteger("VehicleId");
		isImpound = compound.getBoolean("IsImpound");

	}
	
}
