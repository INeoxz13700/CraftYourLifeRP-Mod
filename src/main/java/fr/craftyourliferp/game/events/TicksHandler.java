package fr.craftyourliferp.game.events;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;

public class TicksHandler {

	public static int ticks;
	
	public static int lastTick;
		
	@SubscribeEvent
	public void onClientTick(TickEvent.ClientTickEvent event)
	{
		if(event.phase == Phase.START)
		{
			tick();
		}
	}

	private void tick()
	{
		lastTick = ticks;
		ticks++;
	}
	
}
