package fr.craftyourliferp.guicomponents;

import fr.craftyourliferp.utils.GuiUtils;

public class UIGradientRect extends UIRect {

	private UIColor color2;
	
	public UIGradientRect(UIColor color1, UIColor color2)
	{
		super(color1);
		this.color2 = color2;
	}
	
	@Override
	public void draw(int x , int y)
	{
		GuiUtils.renderGradientRect(posX, posY, posX+width, posY+height, color.toRGB(), color2.toRGB());
	}
	
}
