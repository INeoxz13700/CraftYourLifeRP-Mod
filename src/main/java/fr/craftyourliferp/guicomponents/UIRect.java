package fr.craftyourliferp.guicomponents;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.util.ResourceLocation;

public class UIRect extends GraphicObject {

	public UIButton moveBtn;
	
	public UIButton editBtn;
	

	public UIRect(UIColor color)
	{
		this(color,null);
	}
	
	public UIRect(UIColor color, UIColor contourColor)
	{
		this.color = color;
		this.contourColor = contourColor;
	}
	
	
	@Override
	public void draw(int x, int y)
	{
		if(!visible) return;
		
		GuiUtils.drawRect(posX, posY, width, height, this.color.toHex(), this.color.getNormalizedAlpha());
		super.draw(x, y);
	}
	
	
	public void copyTo(UIRect object)
	{
		super.copyTo(object);
	}

}
