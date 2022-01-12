package fr.craftyourliferp.guicomponents;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class FrameAnimator {
	
	public ResourceLocation[] textures;
	
	public ResourceLocation currentTexture;
	
	private int currentIndex = 0;
	
	private int ticks;
	
	private int frameUpdateTicks;
	
	public FrameAnimator(int framesCount,String locationPath, String textureName,String textureFormat, int frameUpdateTicks)
	{
		
		this.frameUpdateTicks = frameUpdateTicks;
		
		textures = new ResourceLocation[framesCount];
				
		for(int i = 0; i < textures.length; i++)
		{
			textures[i] = new ResourceLocation(locationPath + "/" + textureName.replace("frame", "" + (i+1)) + "." + textureFormat);
			Minecraft.getMinecraft().getTextureManager().bindTexture(textures[i]);
		}
			
		currentTexture = textures[0];
	}
	

	public void next()
	{
		ticks++;
		if(currentIndex == textures.length) currentIndex = 0;

		currentTexture = textures[currentIndex];

	  	if(ticks % frameUpdateTicks == 0) 
	  	{
			currentIndex++;	
	  	}
	}

	
}
