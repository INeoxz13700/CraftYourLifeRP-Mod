package fr.craftyourliferp.ingame.gui;

import java.util.Arrays;
import java.util.List;

import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.company.CompanyManager;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.pedagogical.PedagogicalManager;
import fr.craftyourliferp.pedagogical.Tab;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

public class GuiPedagogical extends GuiBase {

    //protected IBrowser browser = null;
        
    private int field_146298_h;	
    
    private int eventButton;
    
    private long lastMouseEvent;
    
    private int browserX1;
    private int browserX2;
    
    private int browserY1;
    private int browserY2;
    
    public PedagogicalManager manager;
    
    public List<Tab> tabs;
    
	public String selectedCategory = "";

    
    public GuiPedagogical()
    {
    	//manager = CraftYourLifeRPMod.getPedagogical();
    	//tabs = manager.getTabsFromRegion(CraftYourLifeRPMod.getRgListener().getActualRegion());
    }
	
	@Override
	public void initGui()
	{
		
		 this.setWindowSize(width, height);
		 this.setWindowPosition(0, 0);
		/* if(browser == null) {
	            //Grab the API and make sure it isn't null.
	            API api = MCEFApi.getAPI();
	            if(api == null)
	                return;
	            
	            //Create a browser and resize it to fit the screen
	            browser = api.createBrowser("http://www.craftyourliferp.fr");
	            Keyboard.enableRepeatEvents(true);
	     }
		 
	     if(browser != null)
	     	browser.resize(mc.displayWidth-scaleX(96), mc.displayHeight-scaleY(38));*/
	     super.initGui();
	}
	

	@Override
	public void registerChilds()
	{
		this.addChild(new TabGui());
		this.addChild(new CategoryGui());
	}
	
	
	@Override
	public void initializeComponent()
	{
		 this.addComponent(new UIRect(new UIColor("#272727")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
		 super.initializeComponent();
	}
	
    /*public int scaleY(int y) {
        double sy = ((double) y) / ((double) height) * ((double) mc.displayHeight);
        return (int) sy;
    }
    
    public int scaleX(int x) {
        double sx = ((double) x) / ((double) width) * ((double) mc.displayWidth);
        return (int) sx;
    }*/
	
    @Override
    public void updateScreen()
    {
        super.updateScreen();
    }
    
	/*public IBrowser loadUrl(String link)
	{
		if(browser != null)
		{
			browser.loadURL(link);
			return browser;
		}
		return null;
	}*/
	
	
	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);
		
				
		GuiBase categoryGui = getChild(1);
		GuiBase tabGui =  getChild(0);
		
		
        /*if(browser != null) {
        	GL11.glDepthMask(false);
        	GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glColor4f(1f, 1f, 1f, 1f);
            
            //param1=x param2=y2 param3=x2 param4=y      
            browserX1 = getWindowPosX()+96;
            browserX2 = getWindowPosX()+getWindowWidth();
            
            browserY1 = getWindowPosY()+37;
            browserY2 = getWindowPosY()+getWindowHeight();
            
            browser.draw(browserX1, browserY2, browserX2, browserY1); //Don't forget to flip Y axis.
            GL11.glDepthMask(true);
        }*/
        
		if(tabGui.mouseHoverGui(x, y))
		{
			GL11.glColor4f(1f, 1f, 1f, 1f);
			((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§aOnglets"}),x, y, Minecraft.getMinecraft().fontRenderer);	
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
		else if(categoryGui.mouseHoverGui(x, y))
		{
			GL11.glColor4f(1f, 1f, 1f, 1f);
			((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§aCatégories"}),x, y, Minecraft.getMinecraft().fontRenderer);	
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
	}
	
	@Override
	public void handleInput() 
	{
		/*while(Keyboard.next()) 
		{
            if(Keyboard.getEventKey() == Keyboard.KEY_ESCAPE) {
                mc.displayGuiScreen(null);
                return;
            }
	            
	    	boolean pressed = Keyboard.getEventKeyState();
	    	char key = Keyboard.getEventCharacter();
	    	int num = Keyboard.getEventKey();
	    	
	    	if(browser != null) { //Inject events into browser
                if(pressed)
                    browser.injectKeyPressed(key,num);
                else
                    browser.injectKeyReleased(key,num);

                if(key != 0)
                    browser.injectKeyTyped(key, 0);
            }
	    	
		}    

	        
	    while(Mouse.next()) {
	    	int btn = Mouse.getEventButton();
	    	boolean pressed = Mouse.getEventButtonState();
	    	int sx = Mouse.getEventX();
	    	int sy = Mouse.getEventY();
	    	int wheel = Mouse.getEventDWheel();
	    	
	    	
	    	

	            
	     	if(browser != null) { //Inject events into browser. TODO: Handle mods & leaving.
	     		
	     		float ratioX = (float)width / mc.displayWidth;
	     		float ratioY = (float)height / mc.displayHeight;

	     		int y = mc.displayHeight - sy;
	     		
	     		int x = (int)(sx - ((float)browserX1 / ratioX));
	     		y = (int)(y - ((float)browserY1 / ratioY));
	     		
	     		
	     		if(wheel != 0)
	     			browser.injectMouseWheel(x, y, 0, 1, wheel);
	     		else if(btn == -1)
	     			browser.injectMouseMove(x, y, 0, y < 0);
	     		else
	     			browser.injectMouseButton(x, y, 0, btn + 1, pressed, 1);
	        }
	     	

     		int x = sx * width / mc.displayWidth;
     		int y = height - sy * height / mc.displayHeight - 1;
     		int k = Mouse.getEventButton();
	            
	     	if(pressed) 
	     	{ 
	            if (this.mc.gameSettings.touchscreen && this.field_146298_h++ > 0)
	            {
	                return;
	            }
	            
	            
	            this.eventButton = k;
	            this.lastMouseEvent = Minecraft.getSystemTime();
	            this.mouseClicked(x, y, this.eventButton);
	        }
	     	else if(k != -1)
	     	{
	     		  if (this.mc.gameSettings.touchscreen && --this.field_146298_h > 0)
	              {
	                  return;
	              }
	     		  
	     		 this.eventButton = -1;
	             this.mouseMovedOrUp(x, y, k);
	     	}
	        else if (this.eventButton != -1 && this.lastMouseEvent > 0L)
	        {
	            long l = Minecraft.getSystemTime() - this.lastMouseEvent;
	            this.mouseClickMove(x, y, this.eventButton, l);
	        }
	    }*/
		
		super.handleInput();
	}
	
    @Override
    public void onGuiClosed() {
        /*if(browser != null)
            browser.close();
        
        Keyboard.enableRepeatEvents(false);*/
    }
	
}

class CategoryGui extends GuiScrollableView
{
	

	
	@Override
	public void initGui()
	{
		 this.setWindowSize(100, parent.getWindowHeight());
		 this.setWindowPosition(parent.getWindowPosX(), parent.getWindowPosY());
	     super.initGui();
	}
	
	@Override
	public void initializeComponent()
	{
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		this.viewport = new UIRect(new UIColor("#1e1e1e"));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.selectedScrollBar = scrollBarVertical;
						
		setScrollViewPosition(getWindowPosX(), getWindowPosY(), getWindowWidth()-10, getWindowHeight());
		parameterVerticalScrollbar(viewport.getX2(), viewport.getY(), 6, viewport.getHeight());
		

		updateScrollviewContents();

	}
	
	public void updateScrollviewContents()
	{
		updateContentElements();
	}
	
	public void updateContentElements()
	{
		contentRect.childs.clear();

		GuiPedagogical parentGui = (GuiPedagogical) parent;
		for(String category : parentGui.manager.getCategoriesFromTabs(parentGui.tabs))
		{
			UIButton categoryBtn = new UIButton(Type.SQUARE,new UIRect(new UIColor("#272727")),category,new UIRect(new UIColor("#1e1e1e")),new UIButton.CallBackObject()
			{
				@Override
				public void call()
				{
					parentGui.selectedCategory = category;
					TabGui tabGui = (TabGui)parent.getChild(0);
					tabGui.updateScrollviewContents();
				}
				
			});
			categoryBtn.localPosX = 0;
			categoryBtn.localPosY = 0;
			addToContainer(categoryBtn.setPosition(0, 0, 80,20));
		}
		
		contentRect.setHeight(contentRect.getHeight()+2);
	}
	
	public GraphicObject addToContainer(GraphicObject object)
	{
		 if(object == null)  return null;
		 
		 contentRect.addChild(object);
		 
		 object.localPosX = 4;
		 object.localPosY = contentRect.getHeight()+2;
		 
		 if(object.localPosY + object.getHeight() > contentRect.getHeight())
		 {
			 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
		 }
		 
		 return object;
	}
	
}

class TabGui extends GuiScrollableView
{

	
	@Override
	public void initGui()
	{
		 this.setWindowSize(parent.getWindowWidth()-100, 40);
		 this.setWindowPosition(parent.getWindowPosX()+98, parent.getWindowPosY());
	     super.initGui();
	}
	
	@Override
	public void initializeComponent()
	{
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		this.viewport = new UIRect(new UIColor("#1e1e1e"));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.selectedScrollBar = scrollBarVertical;
						
		setScrollViewPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()-10);
		parameterVerticalScrollbar(viewport.getX2(), viewport.getY(), 6, viewport.getHeight());
		

		updateScrollviewContents();

	}
	
	public void updateScrollviewContents()
	{
		
		GuiPedagogical parentGui = (GuiPedagogical) parent;
				
		if(parentGui.selectedCategory.equalsIgnoreCase(""))
		{
			return;
		}
		
		super.updateScrollviewContents();
		
		
		updateContentElements();
	}
	
	public void updateContentElements()
	{
		contentRect.childs.clear();
		

		GuiPedagogical parentGui = (GuiPedagogical) parent;
		
		List<Tab> tabs = parentGui.manager.getTabsFromSelectedCategory(parentGui.tabs, parentGui.selectedCategory);
		for(int i = 0; i < tabs.size(); i++)
		{
			Tab tab = tabs.get(i);
			UIButton categoryBtn = new UIButton(Type.SQUARE,new UIRect(new UIColor("#272727")),tab.displayname,new UIRect(new UIColor("#1e1e1e")),new UIButton.CallBackObject()
			{
				@Override
				public void call()
				{
					//parentGui.browser.loadURL(tab.url_page);
				}
				
			});
			categoryBtn.localPosX = 0;
			categoryBtn.localPosY = 0;
			addToContainer(categoryBtn.setPosition(0, 0, 80,20));
		}
		
		contentRect.setWidth(contentRect.getWidth()+spacing+1);
	}
	
	public GraphicObject addToContainer(GraphicObject object)
	{
		 if(object == null)  return null;
		 
		 contentRect.addChild(object);
		 
		 object.localPosX = contentRect.getWidth()+2;
		 object.localPosY = 4;
		 
		 if(object.localPosX + object.getWidth() > contentRect.getWidth())
		 {
			 contentRect.setWidth(contentRect.getWidth() + object.getWidth() + spacing);
		 }
		 
		 return object;
	}
	


	
}
