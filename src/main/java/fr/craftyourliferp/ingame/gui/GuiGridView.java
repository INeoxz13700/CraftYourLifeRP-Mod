package fr.craftyourliferp.ingame.gui;

import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIColor;

public class GuiGridView extends GuiScrollableView {
	
	protected int elementSize = 20;
	
	
	protected int lastElementposX;
	protected int lastElementposY;
	
	public GuiGridView()
	{
		spacing = 2;
	}

	public GuiGridView(UIColor backgroundColor, boolean unscaledgui) {
		this(backgroundColor, null, unscaledgui);
		
	}
	
	public GuiGridView(UIColor backgroundColor, UIColor contourColor, boolean unscaledgui) {
		super(backgroundColor, contourColor, unscaledgui);
	}
	
	@Override
	public void initializeComponent() 
	{ 
		super.initializeComponent();
	}
	
	public GraphicObject addToContainer(GraphicObject object)
	{
		 if(object == null)  return null;
		 
		 positionElement(object);
		 contentRect.addChild(object);
		 
		 return object;
	 }
	
	@Override
	protected void positionElement(GraphicObject object)
	{
		 if(contentRect.getChilds().size() == 0) 
		 {
			 lastElementposX = object.localPosX += lastElementposX + spacing;
			 lastElementposY = object.localPosY += spacing;
			 contentRect.setHeight(contentRect.getHeight() + elementSize + spacing);
		 }
		 else
		 {
			 lastElementposX = object.localPosX += lastElementposX + spacing + elementSize;
			 object.localPosY = lastElementposY;
		 }
		 
		 if(lastElementposX + object.getWidth() > viewport.getWidth())
		 {
			 lastElementposY = object.localPosY += spacing + elementSize;
			 object.localPosX = lastElementposX = spacing;
			 contentRect.setHeight(contentRect.getHeight() + elementSize + spacing);
		 }		 
	}
	
	public void resetContainerLayout()
	{
		lastElementposX = 0;
		lastElementposY = 0;
		contentRect.setWidth(0);
		contentRect.setHeight(0);
		scrollBarVertical.setValue(0f);
		scrollBarHorizontal.setValue(0f);
	}
	 
	
}
