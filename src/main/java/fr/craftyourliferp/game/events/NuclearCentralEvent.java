package fr.craftyourliferp.game.events;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import com.google.gson.JsonSyntaxException;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.client.util.JsonException;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

public class NuclearCentralEvent {

	private static final List<Integer> GazMaskId = Arrays.asList(7306,7938);
	
	@SubscribeEvent
	public void onPlayerTickEvent(PlayerTickEvent event)
	{
		if(event.player.capabilities.isCreativeMode) return;
		
		
		if(event.phase == TickEvent.Phase.END) return;
		
		World world = event.player.worldObj;
		
		if(world.isRemote)
		{
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(event.player);
			boolean disableShader = false;
			if(isOnNuclearGazRegion(event.player))
			{
				ItemStack helmet = event.player.getCurrentArmor(3);
				if(helmet == null || (helmet.getItem() != null && !GazMaskId.contains(Item.getIdFromItem(helmet.getItem()))))
				{
					if(event.player.ticksExisted % 20 == 0) CraftYourLifeRPMod.getClientData().displayShader(12);
					
					return;
				}
				else
				{
					disableShader = true;
				}
			}
			else
			{
				disableShader = true;
			}
			if(extendedPlayer.getgAlcolInBlood() > 1.5F)
			{
				disableShader = false;
				if(event.player.ticksExisted % 20 == 0) CraftYourLifeRPMod.getClientData().displayShader(19);
				
			}
			else
			{
				disableShader = true;
			}
			
			if(disableShader && event.player == Minecraft.getMinecraft().thePlayer) CraftYourLifeRPMod.getClientData().disableShader();
			
		}
		else
		{
			if(isOnNuclearGazRegion(event.player))
			{
				ItemStack helmet = event.player.getCurrentArmor(3);
				if(helmet == null || (helmet.getItem() != null && !GazMaskId.contains(Item.getIdFromItem(helmet.getItem()))))
				{
					if(event.player.ticksExisted % 40 == 0) event.player.attackEntityFrom(DamageSource.wither, 1.0F);
					if(event.player.ticksExisted % (20*5) == 0)
					{
						ServerUtils.sendChatMessage(event.player, "§cVous n'avez pas de masque à gaz fuyez vite!");
					}
				}
			}
		}
		
		
								
		

	}
	
	public boolean isOnNuclearGazRegion(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

		for(String region :  extendedPlayer.currentRegions)
		{
			if(region.contains("nucleargaz") || region.contains("nuclearmineralregen"))
			{
				return true;
			}
		}
		return false;
	}
	
}
