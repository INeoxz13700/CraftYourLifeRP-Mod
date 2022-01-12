package fr.craftyourliferp.guicomponents;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.ingame.gui.GuiScrollableView;
import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.ingame.gui.IGuiDraggableElement;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class UIScrollbarVertical extends GraphicObject implements IGuiClickableElement, IGuiDraggableElement {

	public GraphicObject background;
	
	public GraphicObject bar;
	
	public ResourceLocation bar_hoverTexture;
	public ResourceLocation barTexture;
	
    private float value;
    
    private float size;
    
    public boolean dragging;
    
    private int clickedY;
    private int previousBarY;
        
    private boolean mouseDown;
    

    private UIButton downArrowBtn;
    
    private UIButton upArrowBtn;
    
    private UIColor barColor;
    
    private UIColor barHoverColor;
    
    private UIColor textureColor = new UIColor(255,255,255);

    
    private ResourceLocation arrowTexture = new ResourceLocation("craftyourliferp","gui/scrollbar_arrow.png");
        
	public UIScrollbarVertical(UIColor barBackgroundColor, UIColor barColor)
	{
		background = new UIRect(barBackgroundColor);
		bar = new UIRect(barColor);	
		this.barColor = barColor;

		downArrowBtn = new UIButton(Type.SQUARE, new UIRect(barBackgroundColor),null,null, new UIButton.CallBackObject()
	    {
	    	@Override
	    	public void call()
	    	{
	    		scroll(1, size*0.009f);
	    	}
	    });
		
		upArrowBtn = new UIButton(Type.SQUARE, new UIRect(barBackgroundColor),null,null, new UIButton.CallBackObject()
	    {
	    	@Override
	    	public void call()
	    	{
	    		scroll(-1,size*0.009f);    	
	    	}
	    });   
		
		value = 0f;
		setVisible(false);
	}
	
	public UIScrollbarVertical(ResourceLocation barTexture, ResourceLocation bar_hoverTexture)
	{
		background = new UIRect(new UIColor(0,0,0,0));
		this.barTexture = barTexture;
		this.bar_hoverTexture = bar_hoverTexture;

		bar = new UIImage(barTexture);	
		value = 0f;
	}
	
	public boolean isTextured()
	{
		return bar instanceof UIImage;
	}
	
    
	@Override
    public void draw(int x, int y)
    {
		
		if(!isVisible()) return;
		
		background.draw(x, y);
		bar.draw(x, y);
		if(!isTextured())
		{
			upArrowBtn.draw(x, y);
			GL11.glColor3f(textureColor.getNormalizedRed(), textureColor.getNormalizedGreen(), textureColor.getNormalizedBlue());
			GuiUtils.drawImage(upArrowBtn.getX() + (upArrowBtn.getWidth()-4) / 2, upArrowBtn.getY() + (upArrowBtn.getHeight()-4) / 2, arrowTexture, 4, 4);
			downArrowBtn.draw(x, y);
			GuiUtils.StartRotation(downArrowBtn.getX() + (downArrowBtn.getWidth()-4) / 2, downArrowBtn.getY() + (downArrowBtn.getHeight()-4) / 2, 4, 4, 180);
			GL11.glColor3f(textureColor.getNormalizedRed(), textureColor.getNormalizedGreen(), textureColor.getNormalizedBlue());
			GuiUtils.drawImage(downArrowBtn.getX() + (downArrowBtn.getWidth()-4) / 2, downArrowBtn.getY() + (downArrowBtn.getHeight()-4) / 2, arrowTexture, 4, 4);
			GuiUtils.StopRotation();
			GL11.glColor3f(1f, 1f, 1f);

			
			if(dragging)
			{
				if(barHoverColor != null) bar.setColor(barHoverColor);
			}
			else
			{
				bar.setColor(barColor);
			}
		}
		else
		{
			if(dragging)
			{
				((UIImage)bar).texture = this.bar_hoverTexture;
			}
			else
			{
				((UIImage)bar).texture = this.barTexture;
			}
		}
		
    	super.draw(x, y);
    	
    	if(mouseDown)
    	{
    		if(downArrowBtn.isClicked(x, y))
    		{
    			downArrowBtn.onLeftClick(x, y);
    		}
    		else if(upArrowBtn.isClicked(x, y))
    		{
    			upArrowBtn.onLeftClick(x, y);
    		}
    	}
    	
    	mouseMove(x, y);
    }
    
    @Override
	public GraphicObject setPosition(int posX, int posY, int width, int height) {
		
    	if(!isTextured())
    	{
	    	super.setPosition(posX, posY+4, width, height-8);
			this.background.setPosition(posX, posY+4, width, height-8);
			this.upArrowBtn.setPosition(posX, posY, width, 4);
			this.downArrowBtn.setPosition(posX, posY+height-4, width, 4);	
    	}
    	else
    	{
			this.background.setPosition(posX, posY, width, height);
	    	super.setPosition(posX, posY, width, height);
    	}
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
	
	
	public float getValue()
	{
		return this.value;
	}
	
	public void setValue(float value)
	{
		clamp(value);
		this.value = value;
	}
	
	public void setSize(float size)
	{
		clamp(size);
		this.size = size;
	}
	


    /**
     * Fired when the mouse button is released. Equivalent of MouseListener.mouseReleased(MouseEvent e).
     */
    public void mouseReleased(int x, int y)
    {
        this.dragging = false;
        mouseDown = false;
    }


	@Override
	public void mouseMove(int x, int y) {
    	if (dragging)
        {
        	bar.setPosition(posX, (int)(y-(clickedY - previousBarY)), width, (int)(5+size*height));
        	if(bar.getY() < this.getY())
        	{
        		bar.setPosition(posX, posY, width, (int)(5+size*height));
        	}
        	if(bar.getY2() > this.getY2())
        	{
            	bar.setPosition(posX, this.getY2()-(int)(5+size*height), width, (int)(5+size*height));
        	}
    		this.value = (float)(bar.getY() - posY)/ (float)(height-5 - size*height);
        	clamp(value);
        }
    	else
    	{
        	bar.setPosition(posX, posY + (int) (value * (height-5 - size*(height-3))), width, (int)(5+size*height));
    	}
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
	}


	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}


	@Override
	public boolean onLeftClick(int x, int y) {
		
		if(!isTextured())
		{
			if(upArrowBtn.isClicked(x, y))
			{
				mouseDown = true;
				upArrowBtn.onLeftClick(x, y);
				return true;
			}
			
			if(downArrowBtn.isClicked(x, y))
			{
				mouseDown = true;
				downArrowBtn.onLeftClick(x, y);
				return true;
			}
		}
		
		if (isHover(x, y))
	    {

		    if(!bar.isHover(x, y)) 
		    {
				this.value = (float)(y - posY) / (float) height;
			    clamp(value);	
		    }
		    else
		    {
		    	previousBarY = bar.getY();
				clickedY = y;
		    	dragging = true;
		    }
			return true;
	    }
		return false;
	}


	public void clamp(float value)
	{
		 if (this.value < 0.0F)
		 {
			 this.value = 0.0F;
		 }
	
		 if (this.value > 1.0F)
		 {
			 this.value = 1.0F;
		 }
	}
	

	public void scroll(int direction,float value)
	{
		this.value += direction * value;
	}


	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}
	
	public UIScrollbarVertical setHoverColor(UIColor color)
	{
		barHoverColor = color;
		return this;
	}
	
	public UIScrollbarVertical setButtonColor(UIColor color)
	{
		textureColor = color;
		return this;
	}
	
}
