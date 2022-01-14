package fr.craftyourliferp.ingame.gui;

import java.awt.Color;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Iterator;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.stream.Collectors;

import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.google.common.base.Preconditions;

import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIDialogBox;
import fr.craftyourliferp.guicomponents.UIDropdown;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;

public class GuiBase extends GuiScreen implements IGuiEmplacement {

	protected GuiBase parent;
	
	protected List<GuiBase> childs = new ArrayList();
	
	protected UIRect guiRect = new UIRect(new UIColor(0,0,0,0));
	
	private List<GraphicObject> components = new ArrayList();
	
	protected List<UIDialogBox> dialogBoxs = new ArrayList();
		
	private RenderItem itemRenderer;
	
	private int guiTicks;
	
	private boolean isVisible = true;
	
	protected boolean isInitialized = false;
	
	private boolean automaticNightMode = false;
	
	
	public GuiBase()
	{
		this(new UIColor(255,255,255,0));
	}
	
	public GuiBase(UIColor color)
	{
		this(color, null);
	}
	
	public GuiBase(UIColor color, UIColor contourColor)
	{
		guiRect = new UIRect(color);
		guiRect.contourColor = contourColor;
	}
	
	
	@Override
	public void initGui() 
	{	
		super.initGui();
		if(!this.isInitialized) registerChilds();
		this.getComponents().clear();
		this.isInitialized = false;
		this.initializeComponent();
		this.initChilds();
		itemRenderer = new RenderItem();
		this.isInitialized = true;
		
		if(automaticNightMode)
		{
			Date date = new Date();   // given date
			Calendar calendar = GregorianCalendar.getInstance(); // creates a new calendar instance
			calendar.setTime(date);   // assigns calendar to given date 
			int hours = calendar.get(Calendar.HOUR_OF_DAY); 	
			if(hours >= 19 || hours <= 7)
			{
				setNightMode(new ArrayList<UIColor>());
			}
		}
		
	}
	
	public void initChilds()
	{
		for(int i = 0; i < childs.size(); i++)
		{
			childs.get(i).initGui();
		}
	}
	
	//Call this method for add child
	public void registerChilds()
	{
		
	}
	
	public void initializeComponent() 
	{ 
		
	}
	

	
	public GuiBase setWindowSize(int width, int height)
	{
		guiRect.setWidth(width);
		guiRect.setHeight(height);
		return this;
	}
	
	public GuiBase setWindowPosition(int x, int y)
	{
		guiRect.setX(x);
		guiRect.setY(y);
		return this;
	}
	
	public UIDialogBox displayDialogBox(String message, CallBackObject confirmBtnCallback, CallBackObject cancelBtnCallback)
	{
		UIDialogBox dialogBox = new UIDialogBox(new UIButton(Type.SQUARE,"confirmBtn",new ResourceLocation("craftyourliferp","gui/dialogbox/button_confirm.png"),null,false,confirmBtnCallback), new UIButton(Type.SQUARE,"cancelBtn",new ResourceLocation("craftyourliferp","gui/dialogbox/button_cancel.png"),null,false,cancelBtnCallback), new UIRect(new UIColor("#2e2e2e")));
		if(parent != null)dialogBox.setPosition(parent.getWindowPosX() + (parent.getWindowWidth() - 250) / 2, parent.getWindowPosY() + (parent.getWindowHeight()-120) / 2, 250, 120);
		else dialogBox.setPosition(getWindowPosX() + (getWindowWidth() - 250) / 2, getWindowPosY() + (getWindowHeight()-120) / 2, 250, 120);
		dialogBox.setText(message);
		
		if(parent != null) parent.dialogBoxs.add(dialogBox);
		else dialogBoxs.add(dialogBox);
		return dialogBox;
	}
	
	public void destroyDialogBox(int index)
	{
		if(parent != null) parent.dialogBoxs.remove(index);
		else dialogBoxs.remove(index);
	}
	
	public void destroyDialogBox(UIDialogBox dialogBox)
	{
		dialogBoxs.remove(dialogBox);
	}
	
	public int getWindowPosX() { return guiRect.getX(); }
	
	public int getWindowPosY() { return guiRect.getY(); }
	
	public int getWindowWidth() { return guiRect.getWidth(); }
	
	public int getWindowHeight() { return guiRect.getHeight(); }
	
	public GraphicObject addComponent(GraphicObject component)
	{
		getComponents().add(component);
		return component;
	}
	
	public void removeComponent(GraphicObject component)
	{
		components.remove(component);
	}
	
	public GraphicObject getComponent(int index)
	{
		if(index < 0)
		{
			index = 0;
		}
		else if(index > getComponents().size()-1)
		{
			index = getComponents().size() -1;
		}
		
		return getComponents().get(index);
	}
	
	public void setComponent(int index, GraphicObject component)
	{
		components.set(index,component);
	}
	
	public int getIndexOf(GraphicObject component)
	{
		return components.indexOf(component);
	}
	
	public GuiBase setParent(GuiBase gui)
	{
		this.parent = gui;
		return this;
	}
	
	
	public GuiBase addChild(GuiBase child)
	{		
		child.setParent(this);
		child.setVisible(true);
		this.childs.add(child);
				
		return child;
	}
	
	public GuiBase getChild(int index)
	{
		return childs.get(index);
	}
	
	
	public void removeChild(int index)
	{
		this.childs.get(index).onChildClose();
		this.childs.remove(index);
	}
	
	public void removeChild(GuiBase gui)
	{
		gui.onChildClose();
		this.childs.remove(gui);
	}
	
	@Override
	protected void mouseClicked(int x, int y, int mouseBtn)
    {		
		if(dialogBoxs.size() > 0)
		{
			for(int i = 0; i < dialogBoxs.size(); i++)
			{
				UIDialogBox dialogBox = dialogBoxs.get(i);
				switch(mouseBtn)
	            {
	            	case 0:
	            		if(dialogBox.onLeftClick(x, y)) return;
	            		break;
	            	case 1:
	            		if(dialogBox.onRightClick(x, y)) return;
	            		break;     
	            	case 2:
	            		if(dialogBox.onWheelClick(x, y)) return;
	            		break;
	            	default:
	           			break;
	            }
			}
			return;
		}
	

		for(int i = childs.size()-1; i != -1; i--) 
		{
			GuiBase child = childs.get(i);
			if(child.mouseHoverGui(x, y))
			{

				if(!child.isVisible) continue;
					
				child.mouseClicked(x, y, mouseBtn);
				break;
			}
		}
		
		
		List<IGuiClickableElement> list = Arrays.stream(getComponents().toArray()).filter(e -> e instanceof IGuiClickableElement).map(sc -> (IGuiClickableElement)sc).collect(Collectors.toList());
		
		for(IGuiClickableElement element : list)
        {
	        if(this.mouseOnDropdown(x, y) && !(element instanceof UIDropdown))
	        {
	        	continue;
	        }
    		switch(mouseBtn)
            {
            	case 0:
            		if(element.onLeftClick(x, y));// return;
            	case 1:
            		if(element.onRightClick(x, y)) return;
            	case 2:
            		if(element.onWheelClick(x, y)) return;
            	default:
           			break;
            }
        }
    }
	
    @Override
    public void handleMouseInput()
    {
    	super.handleMouseInput();
    	int i = Integer.signum(Mouse.getEventDWheel());
    	onScroll(i);
    	i = 0;
    }
    
    public void onScroll(int i)
    {
    	for(GuiBase child : childs)
    	{
    		child.onScroll(i);
    	}
    }
	
	 @Override
	 protected void mouseMovedOrUp(int x, int y, int state)	 
	 {
		 for(int i = 0; i < childs.size(); i++) childs.get(i).mouseMovedOrUp(x, y, state);
		 if(state == 0) 
		 {
				List<IGuiDraggableElement> list = Arrays.stream(getComponents().toArray()).filter(e -> e instanceof IGuiDraggableElement).map(sc -> (IGuiDraggableElement)sc).collect(Collectors.toList());
				for(IGuiDraggableElement element : list) element.mouseReleased(x, y);
		 }
	 }
	 
	 @Override
	 protected void keyTyped(char character, int keycode)
	 {
		  for(int i = 0; i < childs.size(); i++) childs.get(i).keyTyped(character, keycode);
		  List<IGuiKeytypeElement> list = Arrays.stream(getComponents().toArray()).filter(e -> e instanceof IGuiKeytypeElement).map(sc -> (IGuiKeytypeElement)sc).collect(Collectors.toList());
		  for(IGuiKeytypeElement element : list) element.keyTyped(character, keycode);
		  
		  
		  //if(!isChild())super.keyTyped(character, keycode);
	 }
	 
	 @Override
	 public void updateScreen()
	 {
		  guiTicks++;
		  for(int i = 0; i < childs.size(); i++) childs.get(i).updateScreen();
		  List<UITextField> list = Arrays.stream(getComponents().toArray()).filter(e -> e instanceof UITextField).map(sc -> (UITextField)sc).collect(Collectors.toList());
		  for(UITextField element : list) element.updateCursorCounter();
		  if(!isChild())super.updateScreen();
	 }


	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{		
		
		if(!isVisible) return;
		
		guiRect.draw(x, y);
	
		
		for(GraphicObject object : getComponents())
		{
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, object.getZIndex());
			object.draw(x,y);
			GL11.glPopMatrix();;
		}
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0, 0, 999);
		for(int i = 0; i < dialogBoxs.size(); i++)
		{
			dialogBoxs.get(i).draw(x, y);
		}
		GL11.glPopMatrix();
		
		for(int i = 0; i < childs.size(); i++)
		{
			GuiBase child = childs.get(i);
			if(!child.isVisible) continue;
				
			child.drawScreen(x, y, partialTicks);
			
		}

	}
	

	@Override
	public int toCenterX() {
		return (width - guiRect.getWidth());
	}


	@Override
	public int toCenterY() {
		return (height - guiRect.getHeight());
	}


	@Override
	public int toRight() {
		return (width-guiRect.getWidth());
	}


	@Override
	public int toBottom() {
		return (height-guiRect.getHeight());
	}
	
	public boolean mouseOnDropdown(int x, int y)
	{
		for(int i = 0; i < childs.size(); i++)
		{
			childs.get(i).mouseOnDropdown(x, y);
		}
		List<UIDropdown> dropdowns = this.getComponents().stream().filter(t -> t instanceof UIDropdown).map(t->(UIDropdown)t).collect(Collectors.toList());
		for(UIDropdown dropdown : dropdowns)
		{
			if(dropdown.wasClicked)
			{
				return true;
			}
		}
		return false;
	}
	
	public void drawHoveringText(List p_146283_1_, int p_146283_2_, int p_146283_3_, FontRenderer font)
	{
		if (!p_146283_1_.isEmpty())
	    {
			GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        RenderHelper.disableStandardItemLighting();
	        GL11.glDisable(GL11.GL_LIGHTING);
	        GL11.glDisable(GL11.GL_DEPTH_TEST);
	        int k = 0;
	        Iterator iterator = p_146283_1_.iterator();

	        while (iterator.hasNext())
	        {
	        	String s = (String)iterator.next();
	            int l = font.getStringWidth(s);

	            if (l > k)
	            {
	            	k = l;
	            }
	        }

	        int j2 = p_146283_2_ + 12;
	        int k2 = p_146283_3_ - 12;
	        int i1 = 8;

	        if (p_146283_1_.size() > 1)
	        {
	        	i1 += 2 + (p_146283_1_.size() - 1) * 10;
	        }

	        if (j2 + k > getWindowWidth())
	        {
	        	j2 -= 28 + k;
	        }

	        if (k2 + i1 + 6 > getWindowHeight())
	        {
	        	k2 = getWindowHeight() - i1 - 6;
	        }

	        this.zLevel = 300.0F;
	        itemRender.zLevel = 300.0F;
	        int j1 = -267386864;
	        this.drawGradientRect(j2 - 3, k2 - 4, j2 + k + 3, k2 - 3, j1, j1);
	        this.drawGradientRect(j2 - 3, k2 + i1 + 3, j2 + k + 3, k2 + i1 + 4, j1, j1);
	        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 + i1 + 3, j1, j1);
	        this.drawGradientRect(j2 - 4, k2 - 3, j2 - 3, k2 + i1 + 3, j1, j1);
	        this.drawGradientRect(j2 + k + 3, k2 - 3, j2 + k + 4, k2 + i1 + 3, j1, j1);
	        int k1 = 1347420415;
	        int l1 = (k1 & 16711422) >> 1 | k1 & -16777216;
	        this.drawGradientRect(j2 - 3, k2 - 3 + 1, j2 - 3 + 1, k2 + i1 + 3 - 1, k1, l1);
	        this.drawGradientRect(j2 + k + 2, k2 - 3 + 1, j2 + k + 3, k2 + i1 + 3 - 1, k1, l1);
	        this.drawGradientRect(j2 - 3, k2 - 3, j2 + k + 3, k2 - 3 + 1, k1, k1);
	        this.drawGradientRect(j2 - 3, k2 + i1 + 2, j2 + k + 3, k2 + i1 + 3, l1, l1);

	        for (int i2 = 0; i2 < p_146283_1_.size(); ++i2)
	        {
	        	String s1 = (String)p_146283_1_.get(i2);
	            font.drawStringWithShadow(s1, j2, k2, -1);

	            if (i2 == 0)
	            {
	            	k2 += 2;
	            }

	            k2 += 10;
	        }

	        this.zLevel = 0.0F;
	        itemRender.zLevel = 0.0F;
	        GL11.glEnable(GL11.GL_LIGHTING);
	        GL11.glEnable(GL11.GL_DEPTH_TEST);
	        RenderHelper.enableStandardItemLighting();
	        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	    }
	}
	
	public int getGuiTicks()
	{
		return guiTicks;
	}
	
	@Override
    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
	public boolean dialogBoxActive()
	{
		return dialogBoxs.size() > 0;
	}
	
	public boolean isChild()
	{
		return parent != null;
	}
	
	public GuiBase getParent()
	{
		return parent;
	}
	
	public boolean mouseHoverGui(int x, int y)
	{
		 if(x >= getWindowPosX() && x <= getWindowPosX()+ getWindowWidth() && y >= getWindowPosY() && y <= getWindowPosY() + getWindowHeight())
		 {
			 return true;
		 }
		 return false;
	}
	
	public void setVisible(boolean visible)
	{
		isVisible = visible;
		if(parent != null && !visible)
		{
			if(this.isInitialized) onChildClose();
		}
	}
	
	public boolean IsVisible()
	{
		return isVisible;
	}
	
	public void onChildClose()
	{
		
	}
	
	public GuiBase activeAutomaticNightMode()
	{
		this.automaticNightMode = true;
		return this;
	}
	
	
	public void setNightMode(List<UIColor> componentsToAffect)
	{
		for(UIColor color : componentsToAffect)
		{
			color.InvertColor();
		}
	}
	
	public UIRect getRect()
	{
		return this.guiRect;
	}

	public List<GraphicObject> getComponents() {
		return components;
	}

	public void setComponents(List<GraphicObject> components) 
	{
		this.components = components;
	}
	
	@Override
	public void onGuiClosed()
	{
		for(int i = 0; i < childs.size(); i++)
		{
			childs.get(i).onGuiClosed();
		}
		
		super.onGuiClosed();
	}
	
	/*//Optimisation
	private void unloadTextures()
	{
		components.stream().filter(x -> x instanceof UIImage && ((UIImage)x).texture != null).map(x-> (UIImage)x).forEach(action ->
		{
			mc.getTextureManager().deleteTexture(action.texture);
		});
		
		if(this instanceof GuiScrollableView)
		{
			GuiScrollableView scrollableView = (GuiScrollableView)this;
			scrollableView.contentRect.childs.stream().filter(x -> x instanceof UIImage && ((UIImage)x).texture != null).map(x-> (UIImage)x).forEach(action ->
			{
				mc.getTextureManager().deleteTexture(action.texture);
			});
		}
	}
	
	private void loadTextures()
	{
		components.stream().filter(x -> x instanceof UIImage && ((UIImage)x).texture != null).map(x-> (UIImage)x).forEach(action ->
		{
			mc.getTextureManager().loadTexture(action.texture, mc.getTextureManager().getTexture(action.texture));
		});
		
		if(this instanceof GuiScrollableView)
		{
			GuiScrollableView scrollableView = (GuiScrollableView)this;
			scrollableView.contentRect.childs.stream().filter(x -> x instanceof UIImage && ((UIImage)x).texture != null).map(x-> (UIImage)x).forEach(action ->
			{
				mc.getTextureManager().loadTexture(action.texture, mc.getTextureManager().getTexture(action.texture));
			});
		}
	}*/
	



}
