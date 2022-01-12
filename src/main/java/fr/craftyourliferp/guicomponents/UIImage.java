package fr.craftyourliferp.guicomponents;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.util.ResourceLocation;

public class UIImage extends GraphicObject {
	
	public ResourceLocation texture;
	
	protected boolean transparency;

	public UIImage()
	{
		
	}
	
	
	public UIImage(ResourceLocation texture)
	{
		this.texture = texture;
		if(this.texture != null)
		{
			this.transparency = texture.getResourcePath().contains(".png") ? true : false;
		}
		else this.transparency = false;
	}
	
	@Override
	public void draw(int x, int y)
	{
		GL11.glColor4f((float)color.getRed() / 255f,(float)color.getGreen() / 255f,(float)color.getBlue()/255f,color.getNormalizedAlpha());
		if(this.texture != null && this.transparency)
		{
			GuiUtils.drawImageWithTransparency(posX, posY, texture, width, height);
		}
		else
		{
			GuiUtils.drawImage(posX, posY, texture, width, height);
		}
		super.draw(x, y);
	}
	
	public void setTexture(ResourceLocation texture)
	{
		this.texture = texture;
	}
	
	

}
