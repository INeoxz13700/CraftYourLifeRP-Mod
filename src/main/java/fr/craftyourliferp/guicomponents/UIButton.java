package fr.craftyourliferp.guicomponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import akka.Main;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.ICallback;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;



@SideOnly(Side.CLIENT)
public class UIButton extends GraphicObject implements IGuiClickableElement {
	
	public enum Type {
		ROUNDED,
		SQUARE;
	}
	 
	private Type type;
	
	public UIImage texture;
	
	public UIImage hoverTexture;
	
	private String displayedText;
	
	private boolean displayText;	
	
	public UIRect rect;
	
	public UIRect hoverRect;
	
	public UIColor hoverColor;
	
	public UIColor textColor = new UIColor(255,255,255,255);
	
	public CallBackObject callback;
	
	
	public UIButton(Type type , String text, ResourceLocation texture, ResourceLocation hoverTexture, boolean displayText, CallBackObject callback) {
		this.type = type;
		this.displayedText = text;
		this.texture = new UIImage(texture);
		this.hoverTexture = hoverTexture == null ? null : new UIImage(hoverTexture);
		this.displayText = displayText;
		this.callback = callback;
	}

	public UIButton(Type type , UIRect rect, String text, UIRect hoverRect, CallBackObject callBackObject) {
		this.type = type;
		this.rect = rect;
		this.displayedText = text;
		this.hoverRect = hoverRect;	
		this.callback = callBackObject;
	}
	
	public UIButton(String text,UIColor textColor, UIColor hoverColor,float textScale, CallBackObject callback) {
		this.type = Type.SQUARE;
		this.displayedText = text;
		this.textColor = textColor;
		this.hoverColor = hoverColor;	
		this.callback = callback;
		this.scale = textScale;
	}


	@Override
	public void draw(int x, int y)
	{
		GL11.glColor4f(1f, 1f, 1f, 1f);
		if(!visible) return;
				
		
		if(!this.isTextured() && this.rect == null)
		{
			if(isHover(x, y))
			{
				GuiUtils.renderText(displayedText, posX, posY, hoverColor.toRGB(), scale);
			}
			else
			{
				GuiUtils.renderText(displayedText, posX, posY, textColor.toRGB(), scale);
			}
		}
		else if(!this.isTextured() && this.rect != null)
		{
			if(isHover(x,y))
			{

				if(hoverRect != null)
				{
					hoverRect.draw(x, y);
				}
				else 
				{
					rect.draw(x, y);
				}
				
				if(displayedText != null) GuiUtils.renderCenteredText(displayedText, toCenterX(), toCenterY(), 0.8f, hoverColor == null ? textColor.toRGB() : hoverColor.toRGB());
			}
			else
			{
				if(isEnabled() || hoverRect == null)
				{
					rect.draw(x, y);
				}
				else
				{
					hoverRect.draw(x, y);
				}
				if(displayedText != null) GuiUtils.renderCenteredText(displayedText, toCenterX(), toCenterY(), 0.8f, textColor.toRGB());
			}
		}
		else
		{
			if(!this.enabled)
			{
				texture.color = new UIColor("#878787");
			}
			
			if(isHover(x,y))
			{
				if(hoverTexture != null) 
				{
					hoverTexture.draw(x, y);
				}
				else
				{
					texture.color = new UIColor("#878787");
					texture.draw(x, y);
				}
			}
			else
			{
				texture.draw(x, y);
			}
			if(displayText)
			{
				GuiUtils.renderCenteredText(displayedText, toCenterX(), toCenterY(), 0.6f, textColor.toRGB());
			}
			texture.color = new UIColor("#FFFFFF");
		}
		
		super.draw(x, y);
		GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	@Override
	public boolean isHover(int posX, int posY)
	{
		if(!enabled)
		{
			return false;
		}
		
		if(type == Type.SQUARE)
		{
			if(!this.isTextured() && rect == null)
			{
				if(posX >= this.posX - scale && posX <= this.posX + width && posY >= this.posY && posY <= this.posY + height - scale)
				{
					return true;
				}
			}
			else
			{
				if(posX >= this.posX * scale  && posX <= (this.posX + this.width) * scale && posY >= this.posY * scale  && posY <= (this.posY + this.height) * scale)
					return true;
			}
		}
		else if(type == Type.ROUNDED)
		{
			double centerx = (this.posX  + getRadius()) * scale;
			double centery = (this.posY  + getRadius()) * scale;
			
			if( Math.sqrt(Math.pow((posX - centerx),2) + Math.pow((centery - posY),2)) < getRadius() * scale) 
			{
				return true;
			}
		}
		return false;
	}
	
	@Override
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		
		if(this.isTextured()) 
		{
			this.texture.setPosition(x, y, width, height);
			if(hoverTexture != null) this.hoverTexture.setPosition(x, y, width, height);
		}
		else
		{
			if(rect != null) this.rect.setPosition(posX, posY, width, height);
			if(hoverRect != null) this.hoverRect.setPosition(posX, posY, width, height);
		}
		return this;
	}
	
	public GraphicObject setPosition(int x, int y)
	{
		super.setPosition(x, y, getTextWidth(), getTextHeight());
		return this;
	}
	
	@Override
	public void setY(int y)
	{
		setPosition(posX,y,width,height);
	}
	
	@Override
	public void setX(int x)
	{
		setPosition(x,posY,width,height);
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
	
	public boolean isClicked(int x, int y)
	{
		return enabled && visible && isHover(x, y);
	}
	
	public float getRadius() {
		return (width/2);
	}

	public void setDisplayText(String text)
	{
		this.displayedText = text;
	}
	
	public boolean isTextured()
	{
		return this.texture != null;
	}
	
	public UIButton setTextColor(UIColor color)
	{
		this.textColor = color;
		return this;
	}
	
	public UIButton setTextHoverColor(UIColor color)
	{
		this.hoverColor = color;
		return this;
	}
	
	public UIRect getRect()
	{
		return rect;
	}
	
	@Override
	public int toCenterY()
	{
		return posY + height / 2 - 3;
	}

	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}

	@Override
	public boolean onLeftClick(int x, int y) {
		if(isClicked(x,y)) 
		{
			if(callback != null)
			{
				callback.call();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}
	
	public String getText()
	{
		return displayedText;
	}
	
	public int getTextWidth()
	{
		return (int) (mc.fontRenderer.getStringWidth(this.displayedText) * scale);
	}
	
	public int getTextHeight()
	{
		return (int) (mc.fontRenderer.FONT_HEIGHT * scale);
	}
	
	public static class CallBackObject implements ICallback
	{
				
		public CallBackObject() { }

		
		@Override
		public void call() { }
		
		@Override
		public void call(EntityPlayer player) { }


		@Override
		public void call(World world) { }

	}
	


}

