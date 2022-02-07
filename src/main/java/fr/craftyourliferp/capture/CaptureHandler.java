package fr.craftyourliferp.capture;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class CaptureHandler {

	
	private List<CaptureProcess> capturesType = new ArrayList();
		
	private HashMap<World, List<CaptureProcess>> updateableCaptures = new HashMap();
	
	public void registerCapturesType()
	{
		registerCaptureType(new CaptureProcess(60*60*3,60*2, 5));
	}
	
	private void registerCaptureType(CaptureProcess process)
	{
		if(capturesType.contains(process))
		{
			try 
			{
				throw new Exception("Ce type de capture existe déjà!");
			} catch (Exception e) {
				e.printStackTrace();
				return;
			}
		}
		capturesType.add(process);
	}
	
	public int getCaptureType(CaptureProcess process)
	{
		return capturesType.indexOf(process);
	}
	
	public CaptureProcess getCaptureProcessFromId(int id)
	{
		if(id > capturesType.size()-1)
		{
			return null;
		}
		
		CaptureProcess registeredProcess = capturesType.get(id);
		
		if(registeredProcess == null)
		{
			return null;
		}
		
		try 
		{
			CaptureProcess process = CaptureProcess.copyFrom(registeredProcess);
			return process;
		} 
		catch (IllegalArgumentException | IllegalAccessException e) 
		{
			e.printStackTrace();
			return null;
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(event.phase == Phase.START) return;
		
		World world = event.player.worldObj;
		
		if(!world.isRemote)
		{
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(event.player);		
			
			if(event.player.ticksExisted % 20 == 0)
			{	
				HashMap<WorldSelector, CaptureProcess> captures = getCaptureableRegionForPlayer(event.player);
				
				List<CaptureProcess> updateableCaptures = null;
				for(Map.Entry<WorldSelector, CaptureProcess> capture : captures.entrySet())
				{			
					
					CaptureProcess process = capture.getValue();
					if(process.playerInCondition(event.player)) 
					{
						extendedPlayer.getCapturingRegions().add(capture.getKey());
						process.onPlayerEnterRegionCallback.call(event.player);
						ServerUtils.sendMessage("§cDes renforts arrivent prenez le contrôle de la zone!", event.player,1000,0);
												
						if(!this.updateableCaptures.containsKey(world))
						{
							updateableCaptures = new ArrayList();
							this.updateableCaptures.put(world, updateableCaptures);

							if(!updateableCaptures.contains(process))
							{
								updateableCaptures.add(process);	
							}
							
						} 
						else
						{
							updateableCaptures = this.updateableCaptures.get(world);
							if(!updateableCaptures.contains(process))
							{
								updateableCaptures.add(process);								
							}
						}
						
					}
					else
					{
						ServerUtils.sendChatMessage(event.player, "§cVous devez avoir un métier illégal pour braquer le central nucléaire");
					}
				}	
								
				List<WorldSelector> toRemove = new ArrayList();
				for(WorldSelector selector : extendedPlayer.getCapturingRegions())
				{
					CaptureProcess process = getCapturingProcess(selector);
					
					if(process == null)
					{
						continue;
					}
					
					if(!process.playerCaptureAlready(event.player))
					{
						toRemove.add(selector);
						continue;
					}
					
					if(!selector.entityInsideSelection(event.player))
					{
						process.onPlayerLeaveRegionCallback.call(event.player);
						toRemove.add(selector);
						
						if(process.getPlayersCount() == 0)
						{
							this.updateableCaptures.get(selector.getWorld()).remove(process);
						}
					}
				}
				
				
				extendedPlayer.getCapturingRegions().removeAll(toRemove);
			}
		}
	}
	
	
	@SubscribeEvent
	public void onWorldTick(WorldTickEvent event)
	{
		if(event.phase == Phase.END) return;
		
		if(!event.world.isRemote)
		{
			if(updateableCaptures.containsKey(event.world))
			{
				List<CaptureProcess> toRemove = new ArrayList<CaptureProcess>();
				List<CaptureProcess> captures = updateableCaptures.get(event.world);
				for(CaptureProcess capture : captures) 
				{
					 capture.update(event.world);
					 
					 if(capture.isCaptured())
					 {
						 capture.onRegionCapturedCallback.call(event.world);
						 capture.lastCaptureTimer = System.currentTimeMillis();
						 capture.clearEntities();
						 capture.setTickCapture(0);
						 capture.getPlayersInCapture().clear();
						 toRemove.add(capture);
					 }
				}
				captures.removeAll(toRemove);
			}
		}
	}
	
	private HashMap<WorldSelector, CaptureProcess> getCaptureableRegionForPlayer(EntityPlayer player)
	{
		HashMap<WorldSelector, CaptureProcess> captureableRegions = new HashMap<WorldSelector, CaptureProcess>();
		WorldData worldData = WorldData.get(player.worldObj);

		for(Map.Entry<WorldSelector, CaptureProcess> captures : worldData.capturesProcess.entrySet())
		{
			if(!captures.getValue().playerCaptureAlready(player) && captures.getValue().isCapturable(player.worldObj) && captures.getKey().entityInsideSelection(player))
			{
				captureableRegions.put(captures.getKey(),captures.getValue());
			}
		}
		
		return captureableRegions;
	}
	
	private CaptureProcess getCapturingProcess(WorldSelector selector)
	{
		WorldData worldData = WorldData.get(selector.getWorld());
		if(worldData.capturesProcess.containsKey(selector))
		{
			return worldData.capturesProcess.get(selector);
		}
		return null;	
	}
	
}
