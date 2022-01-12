package fr.craftyourliferp.effects;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.util.ResourceLocation;

public class ScreamEffect extends Effect {

	@Override
	public int getId()
	{
		return 0;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public ResourceLocation getEffectTexture() 
	{
		return new ResourceLocation("craftyourliferp","gui/scream.png");
	}

	@Override
	public String getEffectSong() 
	{
		return "craftyourliferp:scream";
	}

}
