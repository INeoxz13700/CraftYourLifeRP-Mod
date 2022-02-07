package fr.craftyourliferp.game.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.entities.EntityLootableBody;
import fr.craftyourliferp.main.CommonProxy;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.phone.NetworkCallTransmitter;
import fr.craftyourliferp.shield.ShieldStats;
import fr.craftyourliferp.thirst.ThirstStats;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.living.LivingAttackEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;

public class PlayerLifeHandler {

	 	@SubscribeEvent
	    public void onDamage(LivingAttackEvent event)
	    {
	    	if(event.source == null || event.source.getEntity() == null) return;

	    	if(!event.source.getEntity().worldObj.isRemote)
	    	{
	        	if(event.source.getEntity() instanceof EntityPlayer)
	        	{
	        		EntityPlayer attacker = (EntityPlayer)event.source.getEntity();

	        		ExtendedPlayer data = ExtendedPlayer.get(attacker);
	         		
	    	        if(data.currentAnimation != 0)
	    	        {
	    	        	event.setCanceled(true);
	    	        }
	    	        
	        		if(!data.passwordReceived)
	        		{
	            		event.setCanceled(true);
	        		}
	        	}
	    	}
	    }
	 	
	 	
	    @SubscribeEvent
	    public void onHurt(LivingHurtEvent e)
	    {
	    	if(!e.entityLiving.worldObj.isRemote)
	    	{
		    	if(e.entityLiving instanceof EntityPlayer) 
		    	{
		    		
		    		EntityPlayer victim = (EntityPlayer) e.entityLiving;
		    		World world = victim.worldObj;
		    		Entity source = e.source.getEntity();

		    
		    		ExtendedPlayer vData = ExtendedPlayer.get(victim);
		    		
			        if(vData.getShouldBeReanimate() || vData.reanimatingPlayername != null)
			        {
			        	e.setCanceled(true);
			        	e.ammount = 0;
			        	return;
			        }
		    		
			        
			        if(vData.isSleeping() && CraftYourLifeRPMod.reanimationHandler.damageForceWakeupFromBed(victim))
			        {
			        	ExtendedPlayer.forceWakeupPlayer(victim);
			        }
		    	}	
	    	}
	    }
	    
	    
	    @SubscribeEvent
	    public void onLivingDeathEvent(LivingDeathEvent event)
	    {
	        if (!event.entity.worldObj.isRemote && event.entity instanceof EntityPlayer)
	        {
	        	EntityPlayer player = (EntityPlayer) event.entity;
	        	
	            NBTTagCompound playerData = new NBTTagCompound();
	            ((ExtendedPlayer) (event.entity.getExtendedProperties(ExtendedPlayer.EXT_PROP_NAME))).saveNBTData(playerData);
	            CommonProxy.storeEntityData(((EntityPlayer) event.entity).getDisplayName(), playerData);
	            ExtendedPlayer.saveProxyData((EntityPlayer) event.entity);      
	        
	    		NetworkCallTransmitter activeCall = NetworkCallTransmitter.getByUsername(player.getCommandSenderName());
	    		if(activeCall != null)
	    		{
	    			activeCall.finishCall();
	    		}
	    		WorldData worldData = WorldData.get(player.worldObj);
	    		
		    	System.out.println("/menotte false " + player.getCommandSenderName());
		    	
				EntityLootableBody corpse = new EntityLootableBody(player.worldObj);
				corpse.setOwner(player);
				corpse.setPositionAndRotation(player.posX, player.posY, player.posZ, player.getRotationYawHead(), 0);
				player.worldObj.spawnEntityInWorld(corpse);
	        }   
	    }
	    
	    @SubscribeEvent
	    public void onPlayerRespawn(PlayerRespawnEvent e)
	    {
	    	EntityPlayer victim = e.player;
	    	World world = e.player.worldObj;
			ExtendedPlayer vData = ExtendedPlayer.get(e.player);
	    	if(!e.player.worldObj.isRemote)
	    	{
	    		vData.syncPhone();
	    		vData.syncCosmetics();
	    	}
	    	
			if(vData.shouldBeInEthylicComa())
			{
				CraftYourLifeRPMod.reanimationHandler.subitDeath(victim);
				return;
			}
			
			if(!vData.getShouldBeReanimate())
			{	
				if(ServerUtils.getPlayerCountForJob(victim.worldObj, "Medecin") == 0)
				{
    				CraftYourLifeRPMod.reanimationHandler.subitDeath(victim);
				}
				else
				{
					CraftYourLifeRPMod.reanimationHandler.placePlayerInReanimation(victim);
				}
			}
	 
	    	
	    	
	    }
	
}
