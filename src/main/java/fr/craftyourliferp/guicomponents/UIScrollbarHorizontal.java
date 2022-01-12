package fr.craftyourliferp.guicomponents;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.ingame.gui.GuiScrollableView;
import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.ingame.gui.IGuiDraggableElement;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class UIScrollbarHorizontal extends GraphicObject implements IGuiClickableElement, IGuiDraggableElement {

	public GraphicObject background;
	
	public GraphicObject bar;
	
	public ResourceLocation bar_hoverTexture;
	public ResourceLocation barTexture;
	
    private float value;
    
    private float size;
    
    public boolean dragging;
    
    private int clickedX;
    private int previousBarX;
    
    private boolean mouseDown;
    
    private UIButton leftArrowBtn;
    
    private UIButton rightArrowBtn;
    
    private ResourceLocation arrowTexture = new ResourceLocation("craftyourliferp","gui/scrollbar_arrow.png");

    private UIColor barColor;
    
    private UIColor barHoverColor;
    
    private UIColor textureColor = new UIColor(255,255,255);

	public UIScrollbarHorizontal(UIColor barBackgroundColor, UIColor barColor)
	{
		background = new UIRect(barBackgroundColor);
		bar = new UIRect(barColor);	
		
		this.barColor = barColor;

		rightArrowBtn = new UIButton(Type.SQUARE, new UIRect(barBackgroundColor),null,null, new UIButton.CallBackObject()
	    {
	    	@Override
	    	public void call()
	    	{
	    		scroll(1, size*0.009f);
	    	}
	    });
		
		leftArrowBtn = new UIButton(Type.SQUARE, new UIRect(barBackgroundColor),null,null, new UIButton.CallBackObject()
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
	
	public void scroll(int direction,float value)
	{
		this.value += direction * value;
	}

	
	public UIScrollbarHorizontal(ResourceLocation barTexture, ResourceLocation bar_hoverTexture)
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
			leftArrowBtn.draw(x, y);
			
			GuiUtils.StartRotation(leftArrowBtn.getX() + (leftArrowBtn.getWidth()-4) / 2, leftArrowBtn.getY() + (leftArrowBtn.getHeight()-4) / 2, 4, 4, -90);
			GL11.glColor3f(textureColor.getNormalizedRed(), textureColor.getNormalizedGreen(), textureColor.getNormalizedBlue());
			GuiUtils.drawImage(leftArrowBtn.getX() + (leftArrowBtn.getWidth()-4) / 2, leftArrowBtn.getY() + (leftArrowBtn.getHeight()-4) / 2, arrowTexture, 4, 4);
			GuiUtils.StopRotation();

			rightArrowBtn.draw(x, y);
			
			GL11.glColor3f(textureColor.getNormalizedRed(), textureColor.getNormalizedGreen(), textureColor.getNormalizedBlue());

			GuiUtils.StartRotation(rightArrowBtn.getX() + (rightArrowBtn.getWidth()-4) / 2, rightArrowBtn.getY() + (rightArrowBtn.getHeight()-4) / 2, 4, 4, 90);
			GuiUtils.drawImage(rightArrowBtn.getX() + (rightArrowBtn.getWidth()-4) / 2, rightArrowBtn.getY() + (rightArrowBtn.getHeight()-4) / 2, arrowTexture, 4, 4);
			GuiUtils.StopRotation();
		
			GL11.glColor3f(1f, 1f, 1f);
			
			if(dragging)
			{
				bar.setColor(barHoverColor);
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
    		if(leftArrowBtn.isClicked(x, y))
    		{
    			leftArrowBtn.onLeftClick(x, y);
    		}
    		else if(rightArrowBtn.isClicked(x, y))
    		{
    			rightArrowBtn.onLeftClick(x, y);
    		}
    	}
    	
    	mouseMove(x, y);

    }
    
    @Override
	public GraphicObject setPosition(int posX, int posY, int width, int height) {
    	if(!isTextured())
    	{
	    	super.setPosition(posX+4, posY, width-8, height);
			this.background.setPosition(posX+4, posY, width-8, height);
			this.leftArrowBtn.setPosition(posX, posY, 4, height);
			this.rightArrowBtn.setPosition(posX+width-4, posY, 4, height);	
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
	
	public UIScrollbarHorizontal setHoverColor(UIColor color)
	{
		barHoverColor = color;
		return this;
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
        	bar.setPosition((int)(x-(clickedX - previousBarX)), posY, (int)(5+size*width), height);
        	if(bar.getX() < this.getX())
        	{
        		bar.setPosition(posX, posY, (int)(5+size*width), height);
        	}
        	if(bar.getX2() > this.getX2())
        	{
            	bar.setPosition(this.getX2()-(int)(5+size*width), posY, (int)(5+size*width), height);
        	}
    		this.value = (float)(bar.getX() - posX)/ (float)(width-5 - size*width);
        	clamp(value);
        }
    	else
    	{
        	bar.setPosition(posX + (int) (value * (width-5 - size*(width-3))), posY, (int)(5+size*width), height);
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
			if(leftArrowBtn.isClicked(x, y))
			{
				mouseDown = true;
				leftArrowBtn.onLeftClick(x, y);
				return true;
			}
			
			if(rightArrowBtn.isClicked(x, y))
			{
				mouseDown = true;
				rightArrowBtn.onLeftClick(x, y);
				return true;
			}
		}
		
		if (isHover(x, y))
	    {

		    if(!bar.isHover(x, y)) 
		    {
				this.value = (float)(x - posX) / (float) width;
			    clamp(value);	
		    }
		    else
		    {
		    	previousBarX = bar.getX();
				clickedX = x;
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
	
	public UIScrollbarHorizontal setButtonColor(UIColor color)
	{
		textureColor = color;
		return this;
	}
	


	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}
	
}
