package fr.craftyourliferp.thirst;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import fr.craftyourliferp.main.ExtendedPlayer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.event.entity.living.LivingDeathEvent;

public class ThirstEventHandler {

	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
		if(event.phase == event.phase.START)
		{
			if(!event.player.worldObj.isRemote)
			{
				ExtendedPlayer ep = ExtendedPlayer.get(event.player);

				ep.thirst.onUpdate(event.player);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerRespawn(PlayerRespawnEvent event)
	{
		EntityPlayer player = event.player;
		ExtendedPlayer ep = ExtendedPlayer.get(player);
		ep.thirst.setThirst(ThirstStats.initialThirst);
	}
	

	
	
}
