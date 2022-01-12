package fr.craftyourliferp.data;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketSyncIdentity;
import fr.craftyourliferp.network.PacketSyncPhone;
import fr.craftyourliferp.network.PacketUpdateIdentity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class IdentityData {
	
	public boolean waitingDataFromClient;

	public String lastname;
	
	public String name;
	
	public String birthday;
	
	public String gender;
	
	public IdentityData() {
		this.lastname = "test";
	}
	
	public void updateData(String lastname, String name, String birthday, String gender)
	{
			this.lastname = lastname;
			this.name = name;
			this.birthday = birthday;
			this.gender = gender;
			this.waitingDataFromClient = false;
	}
	
	public void syncData(EntityPlayer p)
	{
		if(!p.worldObj.isRemote)
		{
			CraftYourLifeRPMod.packetHandler.sendTo(new PacketUpdateIdentity(this.lastname, this.name, this.birthday, this.gender), (EntityPlayerMP)p);
			return;
		}
		
		CraftYourLifeRPMod.packetHandler.sendToServer(new PacketSyncIdentity());

	}
	
	
}
