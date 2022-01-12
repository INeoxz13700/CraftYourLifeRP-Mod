package fr.craftyourliferp.data;

import java.sql.Timestamp;
import java.util.List;

import fr.craftyourliferp.main.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;

public class SmsData {
	
	public long date; 
	
	public String senderNumber;
	
	public String receiverNumber;
	
	public String message;
	
	public boolean readed = false;
	
	
	public SmsData(long date, String senderNumber, String receiverNumber, String message, boolean readed)
	{
		this.date = date;
		this.message = message;
		this.senderNumber = senderNumber; 
		this.receiverNumber = receiverNumber;
		this.readed = readed;
	}
	
	public static SmsData getSmsData(long date, String senderNumber, String message, EntityPlayer p)
	{
		for(SmsData sms : ExtendedPlayer.get(p).phoneData.sms)
		{
			if(sms.date  == date && sms.senderNumber.equalsIgnoreCase(senderNumber) && sms.message.equalsIgnoreCase(message))
			{
				return sms;
			}
		}
		return null;
	}
	

	
	
	
	
	
	
	
	
	
	

}
