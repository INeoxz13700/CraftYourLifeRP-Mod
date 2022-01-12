package fr.craftyourliferp.guicomponents;

import java.util.ArrayList;
import java.util.List;

import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.gui.FontRenderer;

public class UIText extends GraphicObject
{
	
	private float size;
	
	private FontRenderer fontRenderer;
	
	private boolean centerText;
	
	private String text;

	
	public UIText()
	{
		this.fontRenderer = mc.fontRenderer;
		this.text = "";
		setSize(1f);
	}
	
	public UIText(String text, UIColor color, float size)
	{
		this.fontRenderer = mc.fontRenderer;
		this.color = color;
		this.text = text;
		setSize(size);
	}
	
	@Override
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		
		return this;
	}
	
	public GraphicObject setPosition(int x, int y)
	{
		setPosition(x, y, 0, 0);
		return this;
	}
	
	public GraphicObject setPosition(int x, int y, int width)
	{
		setPosition(x, y, width, 0);
		return this;
	}
	
	
	@Override
	public void draw(int x, int y)
	{
		
		if(!visible) return;
		

		
		if(centerText)
		{
			GuiUtils.renderCenteredText(text, posX, posY,size,color.toRGB());
		}
		else
		{
			
			if(text.contains("\\n"))
			{
				String[] splittedText = text.split("\\\\n");
				
				for(int i = 0; i < splittedText.length; i++)
				{
					GuiUtils.renderText(splittedText[i], posX, posY - (splittedText.length+2)  + (i)*8,color.toRGB(),size);
				}
			}
			else 
			{
				GuiUtils.renderText(text, posX, posY,color.toRGB(),size);
			}
		}
		
	}
	
	public void setSize(float size)
	{
		this.size = size;
		this.setWidth((int)(mc.fontRenderer.getStringWidth(text)*size));
		this.setHeight((int)(mc.fontRenderer.FONT_HEIGHT*size));
	}
	
	public float getSize()
	{
		return this.size;
	}
	
	public UIText setText(String text)
	{
		this.text = text;
		return this;
	}
	
	public String getText()
	{
		return this.text;
	}
	
	public int getTextHeight()
	{
		return (int) (mc.fontRenderer.FONT_HEIGHT*size);
	}
	
	public int getTextWidth()
	{
		return (int) (mc.fontRenderer.getStringWidth(text)*size);
	}
	
	public UIText setTextCenter(boolean state)
	{
		centerText = state;
		return this;
	}
	
	
	
	
	
}
