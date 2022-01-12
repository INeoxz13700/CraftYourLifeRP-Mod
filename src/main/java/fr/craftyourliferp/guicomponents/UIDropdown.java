package fr.craftyourliferp.guicomponents;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.utils.FontUtils;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.util.ResourceLocation;

public class UIDropdown extends GraphicObject implements IGuiClickableElement
{
	
	private UIImage arrow = new UIImage(new ResourceLocation("craftyourliferp","gui/dropdown_ico.png"));
		
	private List<String> elements = new ArrayList();
	
	private int elementHeight;
		
	public boolean wasClicked = false;
	
		
	public UIDropdown(int elementHeight, List<String> elements, UIColor rectColor)
	{
		this.elementHeight = elementHeight;
		this.elements = elements;
		this.color = rectColor;
	}
	
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		arrow.setPosition(x+width-(int)(7*scale), (int)(y+(height-(7*scale))/2), (int)(6 * scale), (int)(6 * scale));
		return this;
	}
	
	public void draw(int x, int y)
	{	
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 100);
		
		GuiUtils.drawRect(this.posX, this.posY, this.width, this.height, color.toHex(), color.getNormalizedAlpha());
		String selectedElement = this.getSelectedElement();
		int characterToDisplay = FontUtils.getNumberCharacterInWidth(width-10,0.9f);

		
		
		GuiUtils.renderText(selectedElement.length() >= characterToDisplay ? selectedElement.substring(0, characterToDisplay) + "..." : selectedElement, this.posX + 3,(this.posY + (this.height-(int)(mc.fontRenderer.FONT_HEIGHT*scale)) / 2) + 1,GuiUtils.gameColor, 0.9f);
		
		if(!this.wasClicked)
		{
			arrow.draw(x, y);
		}
		
		if(this.wasClicked)
		{
			GuiUtils.StartRotation(arrow.posX, arrow.posY, arrow.width, arrow.height, 180);
			arrow.draw(x, y);
			GuiUtils.StopRotation();
			
			int posY = this.posY + this.height;
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, 900);
			for(String str : elements)
			{
				if(elements.indexOf(str) != 0)
				{
					GuiUtils.drawRect(this.posX, posY, this.width, this.elementHeight+2, color.toHex(), 1f);
					characterToDisplay = FontUtils.getNumberCharacterInWidth(width,0.9f);
					GuiUtils.renderText(str.length() >= characterToDisplay ? str.substring(0,characterToDisplay) + "..." : str, this.posX + 3 , posY + this.elementHeight / 2 - 1,GuiUtils.gameColor,0.9f);
					posY+= this.elementHeight+2;
				}
			}
			GuiUtils.drawRect(posX, posY, width, 0.25f, "#FFFFFF", 0.4f);
			GL11.glPopMatrix();
		}
		GL11.glPopMatrix();
	}
	
	
	public String getSelectedElement()
	{
		if(elements.size() == 0)
		{
			return "Aucun Element";
		}
		
		return this.elements.get(0);
	}
	
	public int getClickedIndex(int y)
	{	
		int index = (y - this.posY) /  (this.elementHeight+2);
		
		if(index >= elements.size())
		{
			index = elements.size()-1;
		}
		
		if(index < 0) index = 0;
		
		return index;
	}
	
	public String getElementHoverMouse(int y)
	{
		int index = getClickedIndex(y);
		
		return elements.get(index);
	}
	
	public void setElement(int index)
	{
		String temp = this.elements.get(index);
		
		this.elements.set(index, this.elements.get(0));
		this.elements.set(0, temp);
	}
		
	public void setElementByName(String name)
	{
		int index = 0;
		for(String el : elements)
		{
			if(el.equalsIgnoreCase(name))
			{
				this.setElement(index);
				return;
			}
			index++;
		}
	}

	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}
	

	@Override
	public boolean onLeftClick(int x, int y) {
		if(x > this.posX && x < this.posX + this.width && y > this.posY && y < this.posY + this.height)
		{
			if(!wasClicked) 
				this.wasClicked = true;
			else
				this.wasClicked = false;
			return true;
		}
		else if(this.wasClicked && x > this.posX && x < this.posX + this.width && y > this.posY + this.height && y < this.posY + this.height + (this.elements.size()) * this.elementHeight)
		{
			this.setElement(this.getClickedIndex(y));
			this.wasClicked = false;
			return true;
		}
		else
		{
			this.wasClicked = false;
			return false;
		}
	}

	@Override
	public boolean onWheelClick(int x, int y) 
	{
		return false;
	}
	
}