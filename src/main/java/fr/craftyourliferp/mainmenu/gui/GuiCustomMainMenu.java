package fr.craftyourliferp.mainmenu.gui;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.URLEncoder;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import org.apache.commons.codec.digest.DigestUtils;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.google.gson.Gson;
import com.mojang.authlib.GameProfile;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.ServerData;
import fr.craftyourliferp.data.UserSession;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIAnimatedImage;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UICheckBox;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.guireaderfile.UIReader;
import fr.craftyourliferp.ingame.gui.GuiBase;
import fr.craftyourliferp.ingame.gui.GuiScrollableView;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.HTTPUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.util.ResourceLocation;
import scala.collection.concurrent.Debug;



@SideOnly(Side.CLIENT)
public class GuiCustomMainMenu extends GuiBase {
		
    public ResourceLocation background = new ResourceLocation(CraftYourLifeRPMod.name + ":gui/mainmenu/background_noel.png");
    //public ResourceLocation background = new ResourceLocation(CraftYourLifeRPMod.name + ":gui/mainmenu/background_halloween.png");
	//public ResourceLocation background = new ResourceLocation(CraftYourLifeRPMod.name + ":gui/mainmenu/background.png");

	public UIReader uiReader;	
	
	private long lastCheck;
	
	public ServerData data;
		
    
    public GuiCustomMainMenu()
    {
    	setVisible(true);
		uiReader = new UIReader(CraftYourLifeRPMod.apiIp +  "/news.kfile");
		Keyboard.enableRepeatEvents(true);
		
    }
   
    
    
    public void initGui() {
    	
    	this.setWindowPosition(0, 0);
    	this.setWindowSize(width, height);
    	this.activeAutomaticNightMode();
    	
    	
    	    	
    	if(CraftYourLifeRPMod.localhost)
    	{
    		CraftYourLifeRPMod.getClientData().ip = "127.0.0.1";
    		CraftYourLifeRPMod.getClientData().port = "25565";
    	}
    	else
    	{
	    	URL url = null;
	    	
			try 
			{
				url = new URL(CraftYourLifeRPMod.apiIp +  "/ip.txt");
				
				HttpURLConnection con = (HttpURLConnection)url.openConnection();
	
		        BufferedReader in = null;
				
				in = new BufferedReader(new InputStreamReader(con.getInputStream()));
	
	
		        String inputLine;
				
		        while ((inputLine = in.readLine()) != null)
				{
					String[] ip_and_port = inputLine.split(":");
					
					CraftYourLifeRPMod.getClientData().ip = ip_and_port[0];
					    
					if(ip_and_port.length == 2) CraftYourLifeRPMod.getClientData().port = ip_and_port[1];
				    else  CraftYourLifeRPMod.getClientData().port = "25565";
	
				}
			    in.close();
			    con.disconnect();
			} 
			catch (IOException e) 
			{
				e.printStackTrace();
				return;
			}
    	}
		
    	checkServerStatus();
    	lastCheck = System.currentTimeMillis();

        super.initGui();
        
    }
    
	@Override
    public void onScroll(int i)
    {
		if(getChild(1).IsVisible()) return;
		
    	getChild(0).onScroll(i);
    }
    
	@Override
	public void setNightMode(List<UIColor> componentsToAffect)
	{
	    componentsToAffect.add(getComponent(0).color);
	    componentsToAffect.add(((UIButton)getComponent(2)).rect.color);
	    componentsToAffect.add(((UIButton)getComponent(3)).rect.color);
	    componentsToAffect.add(((UIButton)getComponent(4)).textColor);
	    componentsToAffect.add(((UIButton)getComponent(5)).textColor);
	    componentsToAffect.add(((UIButton)getComponent(6)).textColor);

	    		
	    ConnectionBox box = (ConnectionBox) getChild(1);
	    		
	    componentsToAffect.add(box.getComponent(0).color);
	    		
	    componentsToAffect.add(box.getRect().color);
	    componentsToAffect.add(box.getComponent(2).color);
	    	
	    UITextField textField = (UITextField)box.getComponent(3);
	    componentsToAffect.add(textField.inputRect.color);
	    componentsToAffect.add(textField.textColor);
	    		
	    componentsToAffect.add(box.getComponent(4).color);

	    textField = (UITextField)box.getComponent(5);
	    componentsToAffect.add(textField.inputRect.color);
	    componentsToAffect.add(textField.textColor);
	    		
	    componentsToAffect.add(box.getComponent(7).color);
	    
	    UICheckBox checkBox = (UICheckBox)box.getComponent(9);
	    checkBox.setNightMode(true);
	    
	    componentsToAffect.add(box.getComponent(10).color);
	    	
	    GuiContent content = (GuiContent) getChild(0);
	    		
	    PresentationContainer presentationContainer = (PresentationContainer) content.contentRect.childs.get(0);
	    //presentationContainer.containerBackground = new UIImage(new ResourceLocation("craftyourliferp", "gui/mainmenu/noel_background_night.png"));
	    presentationContainer.containerBackground = new UIImage(new ResourceLocation("craftyourliferp", "gui/mainmenu/background_night.png"));

	    NewsContainer newsContainer = (NewsContainer)content.contentRect.childs.get(1);
	    InformationsContainer informationsContainer = (InformationsContainer)content.contentRect.childs.get(2);

	    componentsToAffect.add(newsContainer.containerBackground.color);
	    componentsToAffect.add(informationsContainer.containerBackground.color);
	    		
	    for(GraphicObject obj : newsContainer.news) componentsToAffect.add(obj.color);
	    
	    

		super.setNightMode(componentsToAffect);
	}
    
    public void drawScreen(int x, int y, float partialTicks)
    {
    	super.drawScreen(x, y, partialTicks);
		UIButton registerBtn = (UIButton) getComponent(2);
		UIButton connectBtn = (UIButton) getComponent(3);
    	if(CraftYourLifeRPMod.getClientData().currentSession != null)
    	{		
    		registerBtn.setDisplayText(CraftYourLifeRPMod.getClientData().currentSession.getUsername());
    		connectBtn.setDisplayText("Deconnexion");
    	}
    	else
    	{
    		registerBtn.setDisplayText("Inscription");
    		connectBtn.setDisplayText("Connexion");
    	}
    }
    
    @Override
    public void onGuiClosed()
    {
    	Keyboard.enableRepeatEvents(false);
    }
    
    @Override
	public void initializeComponent()
	{
    	UIRect rect = (UIRect)addComponent(new UIRect(new UIColor("#FFFFFF")).setPosition(0, 0, getWindowWidth(), getWindowHeight()));
    	
    	//addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/mainmenu/logo.png")).setPosition(4, 3, 35, 35));
    	addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/mainmenu/logon.png")).setPosition(4, 3, 35, 35));
    	//addComponent(new UIAnimatedImage(7,"craftyourliferp:gui/mainmenu","logoh-frame", "png",4)).setPosition(4, 3, 35, 35);

    	UIRect registerBtnRect = new UIRect(new UIColor("#FFFFFF"),new UIColor(234, 127, 51));
    	UIRect registerBtnRectHover = new UIRect(new UIColor(234, 127, 51),new UIColor(234, 127, 51));
    	UIButton registerBtn = (UIButton)addComponent(new UIButton(Type.SQUARE,registerBtnRect,"Inscription",registerBtnRectHover,new UIButton.CallBackObject()
    	{
    		@Override
    		public void call()
    		{
    			try {
					java.awt.Desktop.getDesktop().browse(new URI("https://www.craftyourliferp.fr"));
				} catch (IOException | URISyntaxException e) {
					e.printStackTrace();
				}
    		}
    		
    	}).setTextColor(new UIColor(234, 127, 51)).setTextHoverColor(new UIColor("#FFFFFF")).setPosition(getWindowPosX()+getWindowWidth()-80, 13, 60, 15));
    	
    	UIRect connectionBtnRect = new UIRect(new UIColor("#FFFFFF"),new UIColor("#DC3545"));
    	UIRect connectionBtnRectHover = new UIRect(new UIColor("#DC3545"),new UIColor("#DC3545"));
    	UIButton connectionBtn = (UIButton)addComponent(new UIButton(Type.SQUARE,connectionBtnRect,"Connexion",connectionBtnRectHover,new UIButton.CallBackObject()
    	{
    		@Override
    		public void call()
    		{
    			if(CraftYourLifeRPMod.getClientData().currentSession == null) 
    			{
    				GuiScrollableView gui = (GuiScrollableView) getChild(0);
    				gui.scrollBarVertical.setValue(0);
    				getChild(1).setVisible(true);
    			}
    			else 
    			{
    				ConnectionBox.disconnectAccount();
    			}
    		}
    		
    	}).setTextColor(new UIColor("#DC3545")).setTextHoverColor(new UIColor("#FFFFFF")).setPosition(registerBtn.getX() + -65, 13, 60, 15));
    	
    	UIButton settingsBtn = (UIButton)addComponent(new UIButton("Paramètres",new UIColor(68,68,68),new UIColor(239, 158, 61),1.1f,new UIButton.CallBackObject()
    	{
    		@Override
    		public void call()
    		{
    		    Minecraft.getMinecraft().displayGuiScreen(new GuiOptions(mc.currentScreen, mc.gameSettings));
    		}
    		
    	}).setPosition(connectionBtn.getX()-80, 17));
    	

    	
    	
    	UIButton playBtn = (UIButton)addComponent(new UIButton("Jouer",new UIColor(68,68,68),new UIColor(239, 158, 61),1.1f,new UIButton.CallBackObject()
    	{
    		@Override
    		public void call()
    		{
    			if(CraftYourLifeRPMod.getClientData().currentSession == null)
    			{
    				getChild(1).setVisible(true);
    				return;
    			}
    			FMLClientHandler.instance().connectToServerAtStartup(CraftYourLifeRPMod.getClientData().ip, Integer.parseInt(CraftYourLifeRPMod.getClientData().port));
    		}
    		
    	}).setPosition(settingsBtn.getX()- 40, 17));
    	
    	UIButton soloBtn = (UIButton)addComponent(new UIButton("Solo",new UIColor(68,68,68),new UIColor(239, 158, 61),1.1f,new UIButton.CallBackObject()
    	{
    		@Override
    		public void call()
    		{
    			mc.displayGuiScreen(new GuiSelectWorld(mc.currentScreen));
    		}
    		
    	}).setPosition(playBtn.getX()- 30, 17));
    	
    	super.initializeComponent();
	}
    
    @Override
    public void updateScreen()
    {
    	
        super.updateScreen();
                
        if(Keyboard.isKeyDown(Keyboard.KEY_LCONTROL) && Keyboard.isKeyDown(Keyboard.KEY_RCONTROL))
        {
        	if(CraftYourLifeRPClient.currentSession != null)
        	{
        		FMLClientHandler.instance().connectToServerAtStartup("dev.craftyourliferp.fr", 25423);
        	}
        }
        
    }
    
	 
	public void checkServerStatus() 
	{
		try {
			Gson gson = new Gson();

			String jsonStr = HTTPUtils.sendGETHttps("https://api.minetools.eu/ping/" + CraftYourLifeRPMod.getClientData().ip + "/" + CraftYourLifeRPMod.getClientData().port);
			
			
			data = gson.fromJson(jsonStr, ServerData.class);

		} catch (Exception e) {
			e.printStackTrace();
			return;
		}		
	}
	

    @Override
	public void registerChilds()
	{
    	addChild(new GuiContent(this));
    	addChild(new ConnectionBox()).setVisible(false);
    	super.registerChilds();
	}
    
	

}

class PresentationContainer extends GraphicObject
{
	public UIImage containerBackground; 
	
	private UIText text;
	
	public UIRect rect = new UIRect(new UIColor(0,0,0,150));
	private UIRect connectedRect = new UIRect(new UIColor(234, 127, 51));
	
   private UIImage logo;
	
	
	private GuiCustomMainMenu parent;

	
	public PresentationContainer(GuiCustomMainMenu parent)
	{
		this.parent = parent;
		containerBackground = new UIImage(parent.background);
		text = new UIText("CraftYourLifeRP - Version " + CraftYourLifeRPMod.getClientData().version,new UIColor(234, 127, 51),1.8f);
		//logo = new UIImage(new ResourceLocation("craftyourliferp","gui/mainmenu/logo.png")); 
		logo = new UIImage(new ResourceLocation("craftyourliferp","gui/mainmenu/logon.png")); 
		//logo = new UIAnimatedImage(7,"craftyourliferp:gui/mainmenu","logoh-frame", "png",4);
	}
	
	@Override
	public GraphicObject setPosition(int x ,int y, int width, int height)
	{
		containerBackground.setPosition(x, y, width, height);
		rect.setPosition(x, y, width, height);
		text.setPosition(x+70, y+50);
		logo.setPosition(x+70, y+80, 80, 80);
		connectedRect.setPosition(logo.getX2()+105, y+150, 70, 20);
		super.setPosition(x, y, width, height);
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
	
	@Override
	public void draw(int x, int y)
	{
		containerBackground.draw(x, y);
		rect.draw(x, y);
		text.draw(x, y);
		logo.draw(x, y);
		GuiUtils.renderText("Version 1.7.10", logo.getX2() + 30, logo.getY()+20,GuiUtils.gameColor,1.4f);
		GuiUtils.renderText("Ouvert depuis 2017 nous évoluons", logo.getX2() + 30, logo.getY()+40,GuiUtils.gameColor,1f);
		GuiUtils.renderText("de jour en jour merci à vous !", logo.getX2() + 30,logo.getY()+50,GuiUtils.gameColor,1f);
		
		if(parent.data != null)
		{
			GuiUtils.renderText("Rejoignez les", connectedRect.getX()-75, connectedRect.getY()+6); connectedRect.draw(x, y);
		
			GuiUtils.renderText(parent.data.players == null ? "Loading..." : parent.data.players.online + " Joueurs", connectedRect.getX()+6, connectedRect.getY()+6);
		}
		super.draw(x, y);
	}

	
}

class NewsContainer extends GraphicObject
{
	
	public List<GraphicObject> news = new ArrayList();
	
	public UIRect containerBackground; 
	
	private UIText text = new UIText("News",new UIColor("#2E324A"),2.5f);
	
	public NewsContainer()
	{
		containerBackground = new UIRect(new UIColor("#FFFFFF"));
	}
	
	@Override
	public GraphicObject setPosition(int x ,int y, int width, int height)
	{
		containerBackground.setPosition(x, y, width, height);
		text.setPosition(x+30, y+50);
		
		int initialY = y+100;
		for(GraphicObject obj : news)
		{
			obj.setPosition(x+30, initialY, 0, 0);
			initialY += obj.getHeight()+13;
		}
		
		super.setPosition(x, y, width, height);
		return this;
	}
	
	public int getHeight()
	{
		int height = 150;
		for(GraphicObject obj : news)
		{
			if(obj instanceof UIText)
			{
				height += ((UIText)obj).getTextHeight()+2f;
			}
			else
			{
				
			}
		}
		return height+5;
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
	
	@Override
	public void draw(int x, int y)
	{
		containerBackground.draw(x, y);
		text.draw(x, y);
		
		GuiUtils.drawRect(text.getX(), text.getY()+30, width-50, 0.4f, "#2E324A", 1f);
		
		for(GraphicObject obj : news)
		{
			obj.draw(x, y);
		}
		
		super.draw(x, y);
	}

	
}

class InformationsContainer extends GraphicObject
{
	
	
	public UIRect containerBackground; 
	
	private UIText text = new UIText("Copyright © 2017-2020 CraftYourLifeRP - Tous droits réservés", new UIColor("#2E324A"),1f);

	
	public InformationsContainer()
	{
		containerBackground = new UIRect(new UIColor("#FFFFFF"));
	}
	
	@Override
	public GraphicObject setPosition(int x ,int y, int width, int height)
	{
		containerBackground.setPosition(x, y, width, height);
		text.setPosition(x+(width-text.getTextWidth())/2, y+25);

		super.setPosition(x, y, width, height);
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
	
	@Override
	public void draw(int x, int y)
	{
		containerBackground.draw(x, y);

		GuiUtils.drawRect(containerBackground.getX()+5, containerBackground.getY()+5, width-18, 0.5, "#2E324A", 1f);
		text.draw(x, y);
		super.draw(x, y);
	}

	
}


class GuiContent extends GuiScrollableView
{
	
	private GuiCustomMainMenu parent;
	
	public GuiContent(GuiCustomMainMenu parent)
	{
		this.parent = parent;
    	spacing = 0;
	}
	
	@Override
	public void initGui()
	{
		setWindowPosition(0,0);
		setWindowSize(parent.width,parent.height);
		super.initGui();
	}
	
	@Override
	public void initializeComponent()
	{
		contentRect = new UIRect(new UIColor(0,0,0,0));
		viewport = new UIRect(new UIColor(0,0,0,0));


		
		scrollBarVertical = new UIScrollbarVertical(new UIColor(235,235,235),new UIColor(234, 127, 51)).setHoverColor(new UIColor(231, 109, 46));
		scrollBarVertical.setButtonColor(new UIColor(80,80,80));
		scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor(235,235,235),new UIColor(234, 127, 51)).setHoverColor(new UIColor(231, 109, 46));
		scrollBarHorizontal.setButtonColor(new UIColor(80,80,80));

		this.selectedScrollBar = scrollBarVertical;		
	
		
		setScrollViewPosition(getWindowPosX(), getWindowPosY()+41, getWindowWidth()-5, getWindowHeight());
		this.parameterVerticalScrollbar(getWindowPosX()+getWindowWidth()-6, 0, 6, getWindowHeight());
		
	
		updateScrollviewContents();
		
	}
	
	public GraphicObject addToContainer(GraphicObject object)
	{
		 if(object == null)  return null;
		 
		 contentRect.addChild(object);
		 
		 object.localPosX = 0;
		 object.localPosY = contentRect.getHeight();
		 
		 if(object.localPosY + object.getHeight() > contentRect.getHeight())
		 {
			 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
		 }
		 
		 return object;
	}

	
	public void updateScrollviewContents()
	{
		updateContentElements();
	}
	
	public void updateContentElements()
	{
		contentRect.childs.clear();

		addToContainer(new PresentationContainer(parent).setPosition(0, 0, getWindowWidth(), 200));
		
		NewsContainer container = new NewsContainer();
		container.news = parent.uiReader.getComponents();
		
		addToContainer(container.setPosition(0, 0, getWindowWidth(),container.getHeight()));
		addToContainer(new InformationsContainer().setPosition(0, 0, getWindowWidth(),80));
	
		contentRect.setHeight(contentRect.getHeight() - 2);
	}
	
	
}
	
class ConnectionBox extends GuiBase
{

	private InformationBox box;

	
	private GuiCustomMainMenu parentGui;
	
	
	@Override
	public void initGui()
	{
		this.parentGui = (GuiCustomMainMenu) parent;
		setWindowSize(parent.width,parent.height);
		setWindowPosition(0, 0);
		super.initGui();
	}
	
	public void displayInformationsBox(String message, int x,int y, int width, int height)
	{
		
		offsetYComponent(15);
		this.box = new InformationBox(new UIRect(new UIColor("#F0A195")), new UIText(message,new UIColor("#FFFFFF"),1f), new UIButton("X",new UIColor("#FFFFFF"),new UIColor("#FFFFFF"),1f,new CallBackObject()
		{
			@Override
			public void call()
			{
				offsetYComponent(-15);
				box = null;	
			}
				
		}));
		box.setPosition(x, y, width, height);
		
	}
	
	@Override
	public void initializeComponent()
	{
		box = null;
		this.addComponent(new UIRect(new UIColor(255,255,255,100)).setPosition(0, 0, getWindowWidth(), getWindowHeight()));
		

		
		this.guiRect = (UIRect)this.addComponent(new UIRect(new UIColor("#FFFFFF")).setPosition((getWindowWidth()-300)/2, 50, 300, 150));
		UIText username = (UIText)this.addComponent(new UIText("Connexion",new UIColor("#141823"),1.2f).setPosition(guiRect.getX() + 10, guiRect.getY() + 10));
		int width = guiRect.getWidth()-30;
		UITextField fieldUsername = (UITextField) this.addComponent(new UITextField(new UIRect(new UIColor("#FFFFFF"), new UIColor("#EEEEEE")),1f,fr.craftyourliferp.guicomponents.UITextField.Type.TEXT).setTextColor(new UIColor("#141823"))).setPosition(guiRect.getX() + (guiRect.getWidth()-width)  / 2, guiRect.getY() + 50, width, 15);
		fieldUsername.setEnabled(false);
		fieldUsername.setText(Minecraft.getMinecraft().getSession().getUsername());
		UIText password = (UIText)this.addComponent(new UIText("Pseudo",new UIColor("#5C5D5F"),1f).setPosition(fieldUsername.getX()+2, fieldUsername.getY()-15, 0, 0));
		

		
		UITextField fieldPassword = (UITextField) this.addComponent(new UITextField(new UIRect(new UIColor("#FFFFFF"), new UIColor("#EEEEEE")),1f,fr.craftyourliferp.guicomponents.UITextField.Type.PASSWORD).setTextColor(new UIColor("#141823"))).setPosition(guiRect.getX() + (guiRect.getWidth()-width)  / 2, guiRect.getY() + 90, width, 15);
		
		fieldPassword.setMaxStringLength(Integer.MAX_VALUE);
		
		this.addComponent(new UIText("Mots de passe",new UIColor("#5C5D5F"),1f).setPosition(fieldPassword.getX()+2, fieldPassword.getY()-15, 0, 0));

	
		
		this.addComponent(new UIButton(Type.SQUARE, "Close", new ResourceLocation("craftyourliferp","gui/mainmenu/close.png"),null,false, new CallBackObject()
		{
		
			@Override
			public void call()
			{
				fieldPassword.setText("");
				setVisible(false);
			}
			
		}).setPosition(guiRect.getX2()-20, guiRect.getY(), 20, 20));
		
		UIButton connectBtn = (UIButton)this.addComponent(new UIButton(Type.SQUARE, "Connect", new ResourceLocation("craftyourliferp","gui/mainmenu/connectBtn.png"),null,false, new CallBackObject()
		{
			@Override
			public void call()
			{
				if(CraftYourLifeRPMod.getClientData().currentSession == null)
				{
					Object[] requestDetails = connectAccount(fieldUsername.getText(),fieldPassword.getText());
					
					if(box != null)
					{
						offsetYComponent(-15);
					}
					
					if(!(boolean)requestDetails[3])
					{
						displayInformationsBox((String)requestDetails[2],fieldUsername.getX(), fieldUsername.getY() - 10,fieldUsername.getWidth(),15);
					}
					else
					{
						box = null;
					}
				}
				else
				{
					disconnectAccount();
				}
			}
			
		}).setPosition(fieldPassword.getX(), guiRect.getY2()-25, 100, 20));
		
		
		
		
		UICheckBox checkBox = (UICheckBox) this.addComponent(new UICheckBox(Type.SQUARE,CraftYourLifeRPMod.getClientData().currentSession != null ? true : false).setPosition(fieldPassword.getX(), fieldPassword.getY2()+8,8,8));
				
		this.addComponent(new UIText("Mémoriser mes identifiants", new UIColor("#5C5D5F"),0.9f).setPosition(checkBox.getX2()+4, checkBox.getY()+1, 0, 0));
		


		
		super.initializeComponent();
	}
	
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);
		if(box != null)
		{
			box.draw(x, y);
		}
	}
	
	public void onChildClose()
	{
		((UICheckBox)getComponent(9)).checked = false;
		
		if(box != null)
		{
			offsetYComponent(-15);
			box = null;
		}
	}

	public Object[] connectAccount(String username, String password)
	{
		
		 String cryptedPassword = DigestUtils.md5Hex(password);
		 		 
		 Object[] cData = UserSession.connectUser(username, cryptedPassword);
		 
		 if(!(boolean)cData[3]) return cData;
		 
		 try {
			CraftYourLifeRPMod.getClientData().currentSession = UserSession.createSession(username, cryptedPassword,((UICheckBox)getComponent(9)).checked);

			setVisible(false);
		 } 
		 catch (Exception e) 
		 {
			e.printStackTrace();
		 }
		return cData;
	}
	
	public static void disconnectAccount()
	{
		if(CraftYourLifeRPMod.getClientData().currentSession != null)
		{
			try {
				CraftYourLifeRPMod.getClientData().currentSession.destroySession();
			} catch (Exception e) {
				e.printStackTrace();
			}
			CraftYourLifeRPMod.getClientData().currentSession = null;
		}
	}
	
	public void offsetYComponent(int y)
	{
		setWindowSize(getWindowWidth(),getWindowHeight()+y*2);
		
		getComponent(3).setY(getComponent(3).getY()+y*2);
		getComponent(4).setY(getComponent(4).getY()+y*2);
		getComponent(5).setY(getComponent(5).getY()+y*2);
		getComponent(6).setY(getComponent(6).getY()+y*2);
		getComponent(8).setY(getComponent(8).getY()+y*2);
		getComponent(9).setY(getComponent(9).getY()+y*2);
		getComponent(10).setY(getComponent(10).getY()+y*2);
	}
	
	class InformationBox extends GraphicObject
	{
		
		public UIRect rect;
		
		public UIText text;
		
		public UIButton closeBtn;
		
		public InformationBox(UIRect rect, UIText text, UIButton closeBtn)
		{
			this.rect = rect;
			this.text = text;
			this.closeBtn = closeBtn;
		}
		
		public void draw(int x, int y)
		{
			if(!visible) return;
			
			rect.draw(x, y);
			text.draw(x, y);
			closeBtn.draw(x, y);
			super.draw(x, y);
		}
		
		public GraphicObject setPosition(int x, int y, int width, int height)
		{
			rect.setPosition(x, y, width, height);
			text.setPosition(x+5, y+(height-mc.fontRenderer.FONT_HEIGHT)/2, 0, 0);
			closeBtn.setPosition(x+width-8, y+4);
			super.setPosition(x, y, width, height);
			return this;
		}
		
		
	}
	

}

    

