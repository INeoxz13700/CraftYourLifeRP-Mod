package fr.craftyourliferp.data;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.nbt.NBTTagCompound;

public class ContactData {
		
	private List<ContactData> contacts = new ArrayList();

	public String name;
	
	public String number;
	
	public ContactData(String name, String number) 
	{ 
		this.name = name;
		this.number = number;
	}
	
	public ContactData() 
	{ 
		
	}
	

}
