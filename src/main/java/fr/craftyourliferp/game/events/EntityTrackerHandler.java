package fr.craftyourliferp.game.events;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketAnimation;
import fr.craftyourliferp.network.PacketBase;
import fr.craftyourliferp.network.PacketCosmetic;
import fr.craftyourliferp.network.PacketCustomInterract;
import fr.craftyourliferp.network.PacketProning;
import fr.craftyourliferp.network.PacketSleeping;
import net.minecraft.entity.EntityTracker;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.util.MathHelper;
import net.minecraft.world.WorldServer;
import net.minecraftforge.event.entity.player.PlayerEvent;

public class EntityTrackerHandler 
{

    
    @SubscribeEvent
    public void onEntityTracking(PlayerEvent.StartTracking event)
    {
    	if(event.target instanceof EntityPlayer)
    	{
    		EntityPlayer trackerPlayer = event.entityPlayer;
    		EntityPlayer targetPlayer = (EntityPlayer)event.target;
    		for(PacketBase packet : getSyncDataOf(targetPlayer))
    		{
        		CraftYourLifeRPMod.packetHandler.sendTo(packet,(EntityPlayerMP)trackerPlayer);
    		}
    	}
    }
    
    public void syncPlayerToPlayers(EntityPlayer thePlayerSync, boolean syncSelf, PacketBase packet)
    {
    	if(thePlayerSync.worldObj instanceof WorldServer)
    	{
    		WorldServer worldServer = (WorldServer) thePlayerSync.worldObj;
    		EntityTracker et = worldServer.getEntityTracker(); 
    		
    		for(EntityPlayer player : et.getTrackingPlayers(thePlayerSync))     		
        		CraftYourLifeRPMod.packetHandler.sendTo(packet, (EntityPlayerMP)player);
    		
    		if(syncSelf) CraftYourLifeRPMod.packetHandler.sendTo(packet, (EntityPlayerMP)thePlayerSync);

    	}
    }
    
    public void syncPlayerToPlayers(EntityPlayer thePlayerSync, boolean syncSelf, List<PacketBase> packets)
    {
    	for(PacketBase packet : packets)
		{
    		syncPlayerToPlayers(thePlayerSync, syncSelf, packet);
		}
    }
    
    /*public List<PacketBase> getSyncDataOf(EntityPlayer player) //a list of all data of player which should be synchronised to clients
    {    	
    	List<PacketBase> packets = new ArrayList<>();
   
    	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
    	PlayerCachedData cachedData = PlayerCachedData.getData(player);
    	
		PacketCosmetic packet = new PacketCosmetic((byte) 4);
		packet.cosmeticsToSynchronise = extendedPlayer.getEquippedCosmetics();
		packet.entityId = player.getEntityId();
		
		packets.add(packet);
    	packets.add(PacketProning.syncPlayerState(player, cachedData.isProning()));
    	packets.add(PacketAnimation.setAnimation(cachedData.currentAnimation,player.getEntityId()));
    	
    	if(extendedPlayer.isSleeping())
    	{
        	packets.add(PacketSleeping.syncSleeping(player.getEntityId(), MathHelper.floor_double(player.posX), MathHelper.floor_double(player.posY), MathHelper.floor_double(player.posZ)));
    	}
    	
    	return packets;
    }*/
    
    public List<PacketBase> getSyncDataOf(EntityPlayer player) //a list of all data of player which should be synchronised to clients
    {    	
    	List<PacketBase> packets = new ArrayList<>();
   
    	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
    	
		PacketCosmetic packet = new PacketCosmetic((byte) 4);
		packet.cosmeticsToSynchronise = extendedPlayer.getEquippedCosmetics();
		packet.entityId = player.getEntityId();
		
		packets.add(packet);
    	packets.add(PacketProning.syncPlayerState(player, extendedPlayer.isProning()));
    	packets.add(PacketAnimation.setAnimation(extendedPlayer.currentAnimation,player.getEntityId()));
    	
    	if(extendedPlayer.isSleeping())
    	{
    		int x = (int)MathHelper.floor_double(player.posX);
    		int y = (int)MathHelper.floor_double(player.posY);
    		int z = (int)MathHelper.floor_double(player.posZ);
    		int bedDirection = player.worldObj.getBlock(x, y, z).getBedDirection(player.worldObj, x, y, z);
        	packets.add(PacketSleeping.syncSleeping(player.getEntityId(), x, y, z, bedDirection));
    	}
    	
    	return packets; 
    }

	
}
