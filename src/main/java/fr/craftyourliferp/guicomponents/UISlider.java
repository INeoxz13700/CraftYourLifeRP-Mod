package fr.craftyourliferp.guicomponents;

import java.awt.Color;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.ingame.gui.IGuiDraggableElement;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.settings.GameSettings;

public class UISlider extends GraphicObject implements IGuiClickableElement, IGuiDraggableElement{
	
	private UIRect sliderRect;
		
    private float value;
    
    public boolean dragging;
    
    private String displayText;
    
    private double defaultValue;
    
    private double rangeMin;
    private double rangeMax;
    

    
	public UISlider(double rangeMin, double rangeMax, double defaultValue)
	{
		this.rangeMin = rangeMin;
		this.rangeMax =  rangeMax;
		this.defaultValue = defaultValue;
		sliderRect = new UIRect(new UIColor(0,0,0,200));
		sliderRect.contourColor = new UIColor(255,255,255,255);		
		normalize();
	}
	
    
	@Override
    public void draw(int x, int y)
    {
		this.sliderRect.draw(x, y);
    	GuiUtils.renderCenteredText("" + denormalize(), posX + width / 2, posY + (height - (int)(mc.fontRenderer.FONT_HEIGHT * 0.8f)) / 2, 0.8f);
    	this.mouseMove(x, y);
    	super.draw(x, y);
    }
    
    @Override
	public GraphicObject setPosition(int posX, int posY, int width, int height) {
		super.setPosition(posX, posY, width, height);
		this.sliderRect.setPosition(posX, posY, width, height);
		return this;
	}
    
    @Override
    public void setX(int x)
    {
    	setPosition(x,posY,width,height);
    }
    
    @Override
    public void setY(int y)
    {
    	setPosition(posX,y,width,height);

    }
    
    @Override
    public void setWidth(int width)
    {
    	setPosition(posX,posY,width,height);
    }
    
    @Override
    public void setHeight(int height)
    {
    	setPosition(posX,posY,width,height);

    }
    
	public boolean isHover(int x, int y)
	{
		if(x >= posX * scale && x <= (posX + width) * scale && y >= posY * scale && y <= (posY + height) * scale)
		{
			return true;
		}
		return false;
	}
	
	public int denormalize()
	{
		return (int) (value * rangeMax);
	}
	
	public void normalize()
	{
		this.value = (float) this.defaultValue / (float)rangeMax;
	}
	
	public float getValue()
	{
		return this.value;
	}
	
	
	@Override
	public void mouseMove(int x, int y) {
    	if (dragging)
        {
    		this.value = (float)(x - (this.posX)) / (float)(width);

    		if (this.value < 0.0F)
    		{
    			this.value = 0.0F;
    		}

    		if (this.value > 1.0F)
    		{
    			this.value = 1.0F;
    		}
    		
        }
    	GuiUtils.drawRect(posX + (int)(value * (float)(this.width - 4)), posY, 4, height,"#FFFFFF",1);
        GuiUtils.drawRect(posX + (int)(value * (float)(this.width - 4)), posY, 4, height, "#FFFFFF", 1);
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}


	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}


	@Override
	public boolean onLeftClick(int x, int y) {
        if (isHover(x, y))
        {
            this.value = (float)(x - posX);

            if (this.value < 0.0F)
            {
                this.value = 0.0F;
            }

            if (this.value > 1.0F)
            {
                this.value = 1.0F;
            }

            this.dragging = true;
            return true;
        }
        return false;
	}


	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}


	@Override
	public void mouseReleased(int x, int y) {
		dragging = false;
	}

}
