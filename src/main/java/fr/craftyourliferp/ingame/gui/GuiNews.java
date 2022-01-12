package fr.craftyourliferp.ingame.gui;

import java.awt.Color;

import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guireaderfile.UIReader;
import net.minecraft.util.ResourceLocation;

public class GuiNews extends GuiScrollableView 
{
	
	private UIReader uiReader;
	
	public GuiNews()
	{
		super(new UIColor("#303030"), new UIColor("#66a0c6"), false);
		spacing = 1;
		uiReader = new UIReader("http://92.222.94.48/api/news.kfile");
	}
	
	
	@Override
	public void initGui() 
	{
		setWindowSize(300, 225);
		setWindowPosition((parent.width-300)/2, (parent.height-225)/2);
		super.initGui();
	}
	

	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);		
	}
	
	@Override
	public void initializeComponent() 
	{ 
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		this.viewport = new UIRect(new UIColor("#303030"));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.selectedScrollBar = scrollBarVertical;

		
		this.addComponent(new UIButton(Type.SQUARE,"close_btn",new ResourceLocation("craftyourliferp","gui/close.png"),null,false,new CallBackObject()
		{
			@Override
			public void call()
			{
				parent.removeChild(0);
			}
		}).setPosition(getWindowPosX() + getWindowWidth() - 15, getWindowPosY()+2,10,10));
		
		this.addComponent(new UIText("News",new UIColor("#66a0c6"),2));
		
		UIText text = (UIText)getComponent(1);
		text.setPosition(getWindowPosX()+(getWindowWidth()-text.getWidth())/2, getWindowPosY()+5, 0, 0);
		
		setScrollViewPosition(getWindowPosX() + 2, getWindowPosY() + 25, 285, 195);
		

		updateScrollviewContents();
		
		contentRect.setHeight(contentRect.getHeight() + spacing + 10);
	}
	
	public void updateScrollviewContents()
	{
		this.updateContentElements();
	}
	
	
	public void updateContentElements()
	{
		contentRect.childs.clear();
		for(GraphicObject obj : uiReader.getComponents())
		{
			obj.localPosX = 0;
			obj.localPosY = 0;
			addToContainer(obj.setPosition(0, 0, obj.getWidth(),obj.getHeight()));
		}
		contentRect.setHeight(contentRect.getHeight() + spacing + 1);
	}
	
	 public GraphicObject addToContainer(GraphicObject object)
	 {
		 if(object == null)  return null;
		 
		 contentRect.addChild(object);
		 
		 object.localPosX = 5;
		 object.localPosY = contentRect.getHeight()+10;
		 
		 if(object.localPosY + object.getHeight() > contentRect.getHeight())
		 {
			 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
		 }
		 
		 return object;
	 }
	 
	
	
}
