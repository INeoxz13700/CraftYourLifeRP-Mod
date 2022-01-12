package fr.craftyourliferp.data;

import java.sql.Timestamp;

import net.minecraft.nbt.NBTTagCompound;

public class ConversationData {
	
	public Timestamp date; 
	
	public String number;
	
	public String message;
	
	public ConversationData(Timestamp date, String number, String message)
	{
		this.date = date;
		this.message = message;
		this.number = number;
	}
	
	public void writeToNBT(NBTTagCompound tag)
	{
		tag.setString("Type", "SmsData");
		tag.setString("Number", number);
		tag.setLong("Date", date.getTime());
		tag.setString("Message", message);
	}
	
	
	
	
	
	
	

}
