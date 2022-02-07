package fr.craftyourliferp.game.events;

import java.util.List;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketRegionSync;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;

public class RegionHandler {
	
	public void onRegionEnter(EntityPlayer player, List<String> regions)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
		extendedPlayer.currentRegions = regions;
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketRegionSync(extendedPlayer.currentRegions), (EntityPlayerMP)player);
	}
	
	public void onRegionExit(EntityPlayer player, List<String> regions)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
		extendedPlayer.currentRegions.removeAll(regions);
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketRegionSync(extendedPlayer.currentRegions), (EntityPlayerMP)player);
	}
	
}
