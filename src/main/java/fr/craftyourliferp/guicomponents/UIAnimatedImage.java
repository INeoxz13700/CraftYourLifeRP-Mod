package fr.craftyourliferp.guicomponents;

import org.lwjgl.opengl.GL11;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;

public class UIAnimatedImage extends UIImage {

	
	private FrameAnimator animator;
	
	
	public UIAnimatedImage(int framesCount,String locationPath, String textureName,String textureFormat, int frameUpdateTicks)
	{
		animator = new FrameAnimator(framesCount,locationPath,textureName,textureFormat,frameUpdateTicks);
		if(this.animator.textures[0] != null)
		{
			this.transparency = animator.textures[0].getResourcePath().contains(".png") ? true : false;
		}
	}
	
	@Override
	public void draw(int x, int y)
	{
		this.texture = animator.currentTexture;
		super.draw(x, y);
		animator.next();
	}
	
	
}
