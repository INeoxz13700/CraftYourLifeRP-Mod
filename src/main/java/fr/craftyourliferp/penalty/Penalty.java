package fr.craftyourliferp.penalty;

import java.io.File;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.RandomStringUtils;

import com.flansmod.common.network.INetworkElement;

import cpw.mods.fml.common.network.ByteBufUtils;
import fr.craftyourliferp.utils.ISaveHandler;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import net.minecraft.nbt.NBTTagCompound;

public class Penalty implements ISaveHandler, INetworkElement {

	private String id;
	
	private int price;
	
	private long penaltyDate;
	
	private String reason = "";
	
	private String treasuryOwner;
	
	public Penalty() { }
	
	public Penalty(int price, String reason, String owner)
	{
		this.price = price;
		id = generateId();
		penaltyDate = System.currentTimeMillis();
		this.reason = reason;
		this.treasuryOwner = owner;
	}
	
	public String getId()
	{
		return id;
	}
	
	public String getTreasuryOwner()
	{
		return treasuryOwner;
	}
	
	public int getPrice()
	{
		return price;
	}
	
	public void setPrice(int value)
	{
		price = value;
	}
	
	public void addToPrice(int value)
	{
		price += value;
	}
	
	public long getDate()
	{
		return penaltyDate;
	}
	
	private static String generateId()
	{
		int idLenght = 12;
	    return RandomStringUtils.random(idLenght, true, true);
	}
	
	public String getReason()
	{
		return reason;
	}
	
	public String getFormattedDate()
	{
		Timestamp timestamp = new Timestamp(getDate());
		return new SimpleDateFormat("dd/MM/yyyy").format(timestamp);
	}

	@Override
	public void writeToNbt(NBTTagCompound compound)
	{
		compound.setInteger("Price", price);
		compound.setLong("Date", penaltyDate);
		compound.setString("Id", id);
		compound.setString("Reason",reason);
		
		if(treasuryOwner != null) compound.setString("TreasuryOwner", treasuryOwner);
	}

	@Override
	public void readFromNbt(NBTTagCompound compound) 
	{
		price = compound.getInteger("Price");
		penaltyDate = compound.getLong("Date");
		id = compound.getString("Id");
		reason = compound.getString("Reason");
		
		
		if(compound.hasKey("TreasuryOwner")) treasuryOwner = compound.getString("TreasuryOwner");
	}

	@Override
	public void encodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		ByteBufUtils.writeUTF8String(data, id);
		data.writeInt(price);
		data.writeLong(penaltyDate);
		ByteBufUtils.writeUTF8String(data, reason);
	}

	@Override
	public void decodeInto(ChannelHandlerContext ctx, ByteBuf data) {
		id = ByteBufUtils.readUTF8String(data);
		price = data.readInt();
		penaltyDate = data.readLong();
		reason = ByteBufUtils.readUTF8String(data);
	}


	
	
}
