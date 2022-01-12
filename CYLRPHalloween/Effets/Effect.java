package fr.craftyourliferp.effects;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import net.minecraft.util.ResourceLocation;

public abstract class Effect {
	
	public static List<Effect> registeredEffects = new ArrayList();
	
	public int displayTime;
	
	public long displayStartedTimestamp;

	public abstract int getId();
	
	@SideOnly(Side.CLIENT)
	public abstract ResourceLocation getEffectTexture();
	
	public abstract String getEffectSong();
	
	public static void renderEffect(int id, int displayTimeInSeconds)
	{
		for(Effect effect : registeredEffects)
		{
			if(effect.getId() == id)
			{
				CraftYourLifeRPClient.currentEffect = effect;
				CraftYourLifeRPClient.currentEffect.displayTime = displayTimeInSeconds;
				CraftYourLifeRPClient.currentEffect.displayStartedTimestamp = System.currentTimeMillis();
				return;
			}
		}
	}
	
	public static Effect getEffect(int id)
	{
		for(Effect effect : registeredEffects)
		{
			if(effect.getId() == id)
			{
				return effect;
			}
		}
		return null;
	}
	
	
}
