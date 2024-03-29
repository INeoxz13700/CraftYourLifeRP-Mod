package fr.craftyourliferp.phone;

import java.awt.Color;
import java.io.IOException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.Line;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.Mixer;
import javax.sound.sampled.SourceDataLine;
import javax.sound.sampled.TargetDataLine;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.ContactData;
import fr.craftyourliferp.data.SmsData;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UICheckBox;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIDropdown;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIProgressBar;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.ingame.gui.GuiScrollableView;
import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketBMGPage;
import fr.craftyourliferp.network.PacketBitcoinPage;
import fr.craftyourliferp.network.PacketSendSms;
import fr.craftyourliferp.network.PacketSendVoice;
import fr.craftyourliferp.phone.Contacts.State;
import fr.craftyourliferp.phone.web.BMGPageUI;
import fr.craftyourliferp.phone.web.BitcoinConverterPageUI;
import fr.craftyourliferp.phone.web.PageLoading;
import fr.craftyourliferp.phone.web.WebPageUI;
import fr.craftyourliferp.phone.web.page.WebPageData;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiOptionsRowList;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class Tor extends Apps {
	
	
	public enum State {
		CONNECTION_TOR,
		PAGE_DISPLAY,
		PAGE_OPENING
	};
	
	private String homeUrl = "https://www.ahnia.onion";
	
	private State state = State.PAGE_DISPLAY;
	
	private PageLoading loadingPage;
	

	public Tor(String name, ResourceLocation ico, GuiPhone phone)
	{
		super(name, ico, phone);
	}
	
	@Override
	public void initGui()
	{
		GuiPhone.GuiApps guiApps = (GuiPhone.GuiApps) phone.getChild(0);
		this.setWindowSize(guiApps.getWindowWidth(), guiApps.getWindowHeight());
		this.setWindowPosition(guiApps.getWindowPosX(), guiApps.getWindowPosY());
		super.initGui();
	}
	
	@Override
	public void initializeComponent()
	{
		super.initializeComponent();
		
		GraphicObject header = addComponent(new UIRect(new UIColor(220,220,220)).setPosition(getWindowPosX()-2, getWindowPosY()-1, getWindowWidth()+3, 20));
		
		addComponent(new UIButton(UIButton.Type.ROUNDED, "back", new ResourceLocation("craftyourliferp", "gui/phone/back.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				back();
			}
			
		}).setPosition(header.getX() + 2, header.getY() + (header.getHeight() - 15) / 2,15,15));
		
		addComponent(new UIButton(UIButton.Type.SQUARE, "next", new ResourceLocation("craftyourliferp", "gui/phone/next.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
			
			}
			
		}).setPosition(header.getX() + 18, header.getY() + (header.getHeight() - 15) / 2,15,15));
		
		addComponent(new UIButton(UIButton.Type.ROUNDED, "refresh", new ResourceLocation("craftyourliferp", "gui/phone/refresh.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				WebPage page = (WebPage)getChild(0);
				page.openPage(page.currentPage == null ? null : page.currentPage);
				
			}
			
		}).setPosition(header.getX() + 34, header.getY() + (header.getHeight() - 12) / 2,12,12));
		
		addComponent(new UITextField(new UIRect(new UIColor("#ffffff")),0.8f,UITextField.Type.TEXT).setTextColor(new UIColor(45,45,45)).setText(homeUrl)).setEnabled(false).setPosition(header.getX()+50, header.getY() + (header.getHeight() - 10) / 2, 68, 10);

		addComponent(new UIProgressBar(new UIRect(new UIColor(0,0,0,0)), new UIRect(new UIColor(71,143,174))).setValue(0f).setPosition(getWindowPosX()-2, getWindowPosY()+17, getWindowWidth()+3, 2));
		
		
	}
	
	public WebPageData getCurrentOpenPageData()
	{
		WebPage page = (WebPage)getChild(0);
		return page.getCurrentPage().getWebPageData();
	}
	
	@Override
    public void onScroll(int i)
    {
    	getChild(0).onScroll(i);
    }
	
	
	@Override
	public void registerChilds()
	{
		addChild(new WebPage());
		super.registerChilds();
	}

	
	@Override
	public void drawScreen(int x, int y, float partialTicks) 
	{

		super.drawScreen(x, y, partialTicks);
	}


	@Override
    public void mouseClicked(int x, int y, int mouseBtn)
    {
		super.mouseClicked(x, y, mouseBtn);
    }
	

	@Override
	public void onGuiClosed() {
		super.onGuiClosed();
	}
	

	@Override
	public void updateScreen() {
		super.updateScreen();
	}

	@Override
	public void keyTyped(char par1, int par2)
	{
		super.keyTyped(par1, par2);
	}

	
	public void updateGuiState()
	{
		if(state == State.PAGE_OPENING)
		{
			GuiScrollableView scrollview =  (GuiScrollableView) getChild(0);
			for(GraphicObject obj : scrollview.getComponents())
			{
				obj.setVisible(false);
			}
			scrollview.contentRect.setVisible(false);
			scrollview.scrollBarVertical.setVisible(false);
			scrollview.scrollBarHorizontal.setVisible(false);
		}
		else if(state == State.PAGE_DISPLAY)
		{
			GuiScrollableView scrollview =  (GuiScrollableView) getChild(0);
			for(GraphicObject obj : scrollview.getComponents())
			{
				obj.setVisible(true);
			}
			scrollview.contentRect.setVisible(true);
			scrollview.scrollBarVertical.setVisible(true);
			scrollview.scrollBarHorizontal.setVisible(true);
		}
	}
	
	public WebPageUI getCurrentPageUI()
	{
		return ((WebPage)getChild(0)).currentPage;
	}
	
	
	public class WebPage extends GuiScrollableView
	{
		
		private WebPageUI currentPage;
		
		@Override
		public void initGui()
		{
			UITextField textField = (UITextField)parent.getComponent(4);
			textField.setText(homeUrl);
			
			this.spacing = 5;
			this.setWindowSize(parent.getWindowWidth()+3, 135);
			this.setWindowPosition(parent.getWindowPosX()-2, parent.getWindowPosY()+19);

			
			super.initGui();
						
			if(currentPage != null)
			{
				currentPage.initGui();
			}

		}
		
		public void initializeComponent()
		{
			this.contentRect = new UIRect(new UIColor(0,0,0,0));
			this.viewport = new UIRect(new UIColor(255,255,255));
			
			this.scrollBarVertical = new UIScrollbarVertical(new UIColor(240,240,240),new UIColor(205,205,205)).setHoverColor(new UIColor(190,190,190));
			this.scrollBarVertical.setButtonColor(new UIColor(80,80,80));
			this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor(240,240,240),new UIColor(205,205,205)).setHoverColor(new UIColor(190,190,190));
			this.scrollBarHorizontal.setButtonColor(new UIColor(80,80,80));

			this.selectedScrollBar = scrollBarVertical;
			
			setScrollViewPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight());
			parameterVerticalScrollbar(getWindowPosX()+getWindowWidth()-5,getWindowPosY(),6,getWindowHeight());
			
			updateScrollviewContents();
			
			contentRect.setHeight(contentRect.getHeight() + 25);

			
			addComponent(new UIRect(new UIColor(48,48,48))).setZIndex(1).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth()-5, 20);

		}
		
		public void updateScreen()
		{
			
			if(currentPage != null)
			{
				currentPage.updateScreen();
			}
			
			Tor tor = (Tor) parent;
			if(tor.loadingPage != null)
			{
				tor.loadingPage.update();
				
				
				UIProgressBar bar = (UIProgressBar) parent.getComponent(5);
				bar.setValue((float) tor.loadingPage.getState() / 100);
				
				if(tor.loadingPage.pageLoaded())
				{
					tor.loadingPage = null;
					UIProgressBar progressBar = (UIProgressBar) tor.getComponent(5);
					progressBar.setValue(0f);
				}
			}
			

			
		}
	

		public WebPageUI getCurrentPage()
		{
			return currentPage;
		}
		
		
		public void drawScreen(int x, int y, float pt)
		{			
			Tor tor = (Tor) parent;

			if(currentPage != null && tor.state != Tor.State.PAGE_OPENING) 
			{
				currentPage.drawScreen(x, y, pt);
			}
			else
			{
				super.drawScreen(x, y, pt);

				if(currentPage == null && tor.state != Tor.State.PAGE_OPENING)
				{
					GraphicObject header = getComponent(0);
					GL11.glPushMatrix();
					GL11.glTranslatef(0, 0, 1);
					GuiUtils.renderText("Ahnia", header.getX() + 3, header.getY() + 6);
					GL11.glPopMatrix();
				}
				
				if(scrollBarVertical.isVisible())
				{
					getComponent(0).setWidth(getWindowWidth()-5);
				}
				else
				{
					getComponent(0).setWidth(getWindowWidth());
				}
			}
			
		}
	
		
		@Override
		public void registerChilds()
		{
			super.registerChilds();
		}
		
		@Override
		public GraphicObject addToContainer(GraphicObject object)
		{
			 if(object == null)  return null;
				
			 contentRect.addChild(object);
			 
			 object.localPosX = (contentRect.getWidth() - object.getWidth()) / 2;
			 object.localPosY = contentRect.getHeight() + 25;
			 
			 if(object.localPosY + object.getHeight() > contentRect.getHeight())
			 {
				 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
			 }
			 
			 return object;
		}
		
		
		
		@Override
		public void updateScrollviewContents()
		{
			contentRect.childs.clear();
			
			super.updateScrollviewContents();
			
			
			addToContainer(new UrlBtn(new BMGPageUI("https://www.abiaz855w.onion", "BMG (Marché noir)", "Boutique armes - drogues")).setPosition(0, 0, getWindowWidth(), 25));
			addToContainer(new UrlBtn(new BitcoinConverterPageUI()).setPosition(0, 0, getWindowWidth(), 25));

			
		}
		
		public void openPage(WebPageUI page)
		{
			Tor tor = (Tor) parent;
			tor.state = State.PAGE_OPENING;
			updateGuiState();
			tor.loadingPage = new PageLoading(page,tor);
		}
		
		public void displayPage(WebPageUI page)
		{
			Tor tor = (Tor) parent;
			tor.state = State.PAGE_DISPLAY;
			
			if(page != null)
			{
				currentPage = page;
				currentPage.setParent(this);
				currentPage.initGui();
			}
			else
			{
				currentPage = null;
				initGui();
			}
			

			updateGuiState();
		}
		
		@Override
		public void onScroll(int i)
		{
			if(currentPage != null)
			{
				currentPage.onScroll(i);
				return;
			}
			super.onScroll(i);
		}

		
		@Override
		public void mouseClicked(int x, int y, int btn)
		{			
			if(currentPage != null)
			{
				currentPage.mouseClicked(x, y, btn);
				return;
			}
			super.mouseClicked(x, y, btn);
		}
		
		 @Override
		 protected void mouseMovedOrUp(int x, int y, int state)	 
		 {
			 
			 if(currentPage != null)
			 {
				 currentPage.mouseMovedOrUp(x, y, state);
			 }
			 super.mouseMovedOrUp(x, y, state);
		 }
		 
		@Override
		public void keyTyped(char par1, int par2)
		{
			if(currentPage != null)
			{
				currentPage.keyTyped(par1, par2);
				return;
			}
			super.keyTyped(par1, par2);
		}
		
		@Override
		public void onGuiClosed()
		{			
			if(currentPage != null)
			{
				currentPage.onGuiClosed();
				currentPage = null;
			}
			super.onGuiClosed();
		}
		
		
	}
	



	@Override
	public void back() {
		if(this.getCurrentPageUI() != null)
		{
			getCurrentPageUI().back();
		}
		else
		{
			phone.currentApp = null;
		}
	}

	@Override
	public void openApps() {
		// TODO Auto-generated method stub
		
	}
	
	public class UrlBtn extends GraphicObject implements IGuiClickableElement
	{
				
		private WebPageUI page;
		
		public UIButton tittleBtn;
		
		
		public UrlBtn(WebPageUI page)
		{
			this.page = page;
			tittleBtn = new UIButton(page.getPageTittle(),new UIColor(69, 142, 172), new UIColor(45, 45, 45),0.9f, new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{

					if(phone.currentApp instanceof Tor)
					{
						Tor tor = (Tor) phone.currentApp;
						WebPage page = (WebPage)tor.getChild(0);
						page.openPage(UrlBtn.this.page);
					}
				}
			});
			
		}
		
		
		public GraphicObject setPosition(int x, int y, int width, int height)
		{
			super.setPosition(x, y, width, height);
			tittleBtn.setPosition(x+5, y+1);
			return this;
		}
		
		public void draw(int x, int y)
		{
			super.draw(x, y);
			tittleBtn.draw(x, y);
			GuiUtils.renderText("§o" + page.getPageUrl(), posX + 5, posY + 18,2435375,0.7f);
			if(!tittleBtn.isHover(x, y))
			{
				GuiUtils.drawRect(posX+4, posY + 8, mc.fontRenderer.getStringWidth(page.getPageTittle()) * 0.9f, 1, "#478fae", 1f);
			}
			GuiUtils.renderText(page.getPageDescription() == null ? "Pas de description fournie" : page.getPageDescription(), posX + 5, posY+11, 2435375,0.6f);
		}


		@Override
		public boolean onRightClick(int x, int y) {
			return false;
		}


		@Override
		public boolean onLeftClick(int x, int y) {
			if(tittleBtn.isClicked(x, y))
			{
				tittleBtn.callback.call();
				return true;
			}
			return false;
		}


		@Override
		public boolean onWheelClick(int x, int y) {
			return false;
		}
		
	}

}
