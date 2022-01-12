package fr.craftyourliferp.guicomponents;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.ingame.gui.IGuiEmplacement;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.entity.RenderItem;

public class GraphicObject extends Gui implements IGuiEmplacement {
	
	protected GraphicObject parent;
	
	public List<GraphicObject> childs = new ArrayList<GraphicObject>();
	
	public List<GraphicObject> visibleChilds = new ArrayList();
	
	protected Minecraft mc = Minecraft.getMinecraft();
	
	public RenderItem itemRender = new RenderItem();
	
	public  UIColor color = new UIColor(255,255,255);
	
	public UIColor contourColor;

	protected int posX;

	protected int posY;
	
	public int localPosX;
	
	public int localPosY;

	protected int width;

	protected int height;

	protected float scale = 1f;
	
	protected boolean visible = true;
	
	protected boolean enabled = true;
	
	protected int zIndex = 0;
	
	public int getX() {
		return this.posX;
	}
	
	public int getY() {
		return this.posY;
	}
	
	public int getX2()
	{
		return posX + width;
	}
	
	public int getY2()
	{
		return posY + height;
	}
	
	public int getWidth()
	{
		return this.width;
	}
	
	public int getHeight() {
		return this.height;
	}
	
	
	public GraphicObject setPosition(int posX, int posY, int width, int height) {
		this.posX = posX;
		this.posY = posY;
		this.width = width;
		this.height = height;
		return this;
	}
	
	public void setWidth(int width)
	{
		setPosition(posX,posY,width,height);
	}
	
	public void setHeight(int height)
	{
		setPosition(posX,posY,width,height);
	}
	
	public void setX(int x)
	{
		setPosition(x,posY,width,height);
		for(GraphicObject child : childs)
		{
			child.setX(x + child.localPosX);
		}
	}
	
	public void setY(int y)
	{
		setPosition(posX,y,width,height);
		for(GraphicObject child : childs)
		{
			child.setY(y + child.localPosY);
		}
	}
	
	public GraphicObject setScale(float value)
	{
		this.scale = value;
		return this;
	}
	
	public float getScale()
	{
		return this.scale;
	}
	
	public GraphicObject setZIndex(int zIndex)
	{
		this.zIndex = zIndex;
		return this;
	}
	
	public int getZIndex()
	{
		return zIndex;
	}
	

	public boolean isHover(int x, int y)
	{
		if(x >= posX * scale && x <= (posX + width) * scale && y >= posY * scale && y <= (posY + height) * scale)
		{
			return true;
		}
		return false;
	}
	
	public boolean isOnRect(GraphicObject object)
	{
		if(posX + width >= object.posX && posY + height >= object.posY && posX <= object.posX + object.width && posY <= object.posY + object.height)
		{
			return true;
		}
		return false;
	}
	
	public void draw(int x, int y)
	{
		if(!visible) return;
		
		for(GraphicObject element : visibleChilds)
		{
			element.draw(x, y);
		}
		if(contourColor != null)
		{
			String hex = contourColor.toHex();
			float alpha = (float)contourColor.getAlpha() / (float)255;
			GuiUtils.drawRect(posX-1, posY, 1, height, hex, alpha);
			GuiUtils.drawRect(posX-1,posY-1, width+2, 1, hex, alpha);
			GuiUtils.drawRect(posX-1, posY+height, width+2, 1, hex, alpha);
			GuiUtils.drawRect(posX+width, posY, 1, height, hex, alpha);
		}
	}

	@Override
	public int toCenterX() {
		return posX + width / 2;
	}

	@Override
	public int toCenterY() {
		return posY + height / 2;
	}

	@Override
	public int toRight() {
		return 0;
	}

	@Override
	public int toBottom() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public void addChild(GraphicObject object)
	{
		if(childs.contains(object)) return;
		
		childs.add(object);
	}
	
	public List<GraphicObject> getChilds()
	{
		return this.childs;
	}
	
	public List<GraphicObject> updateChildsOnRect(GraphicObject object)
	{
		return visibleChilds = this.childs.stream().filter(x -> x.isOnRect(object)).collect(Collectors.toList());
	}
	
	public GraphicObject setEnabled(boolean state)
	{
		this.enabled = state;
		return this;
	}
	
	public boolean isEnabled()
	{
		return this.enabled;
	}
	
	public void setVisible(boolean state)
	{
		this.visible = state;
	}
	
	public boolean isVisible()
	{
		return this.visible;
	}
	
	public GraphicObject setContourColor(UIColor color)
	{
		this.contourColor = color;
		return this;
	}
	
	public GraphicObject setColor(UIColor color)
	{
		this.color = color;
		return this;
	}
	
	public void copyTo(GraphicObject object)
	{
		object.color = this.color;
		object.setPosition(posX, posY, width, height);
		object.enabled = this.enabled;
		object.localPosX = this.localPosX;
		object.localPosY = this.localPosY;
		object.zIndex = this.zIndex;
		object.parent = this.parent;
		object.scale = this.scale;
	}


	
	
}
