package fr.craftyourliferp.shield;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketSyncShield;
import net.minecraft.entity.player.EntityPlayer;

public class ShieldStats {
	
    public static final float maxShield = 100f;
    public static final float initialShield = 0f;
    
    private EntityPlayer player;
    
    public float shield;
    
    public ShieldStats(float shield, EntityPlayer p) {
    	this.player = p;
    	this.setShield(shield);
    }
    
    public float getShield() {
    	if(!player.worldObj.isRemote)
    	{
    		return this.shield;
    	}
    	ExtendedPlayer.get(player).syncShield();
    	return this.shield;
    }
    
    public void setShield(float value) {
    	if(!player.worldObj.isRemote)
    	{
    		this.shield = Math.min(value, maxShield);
    		if(ExtendedPlayer.get(player)!= null)
    		{
    			ExtendedPlayer.get(player).syncShield();
    		} 
    	}
    	else {
    		this.shield = Math.min(value, maxShield);
    		if(this.shield < 0)
    		{
    			this.shield = 0;
    		}
    	}
    }

}
