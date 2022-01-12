package fr.craftyourliferp.data;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.utils.ISaveHandler;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;

public class PaypalData implements ISaveHandler 
{
		
	public String password;
	
	public float accountSold;
	
	public String smsCode;
	
	public boolean isAuthentified;
	
	
	private String generateSmsCode()
	{
		String smsCode = "";
		Random rand = new Random();
		for(int i = 0; i < 6; i++)
		{
			char randomChar = (char) MathHelper.getRandomIntegerInRange(rand, 48, 57);
			smsCode += randomChar;
		}
		return smsCode;
	}
	
	public void updateSmsCode()
	{
		smsCode = generateSmsCode();
	}
	
	public boolean accountIsRegistered()
	{
		return password != null;
	}


	@Override
	public void writeToNbt(NBTTagCompound compound) 
	{
		if(accountIsRegistered())
		{
			compound.setString("Password", password);
			compound.setBoolean("Authentified", isAuthentified);
		}
	}


	@Override
	public void readFromNbt(NBTTagCompound compound) 
	{
		if(compound.hasKey("Password"))
		{
			password = compound.getString("Password");
			isAuthentified = compound.getBoolean("Authentified");
		}
	}
	
}
