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
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.ingame.gui.GuiScrollableView;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketSendSms;
import fr.craftyourliferp.network.PacketSendVoice;
import fr.craftyourliferp.phone.Contacts.State;
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
public class Sms extends Apps {
	
	public static final int bubble_max_width = 70;
	public static final float font_scale = 0.8f;
	
	public SmsButton selectedContact = null;

	public UIButton sendMessage;
	
	public UITextField messageInput;	

	public Sms(String name, ResourceLocation ico, GuiPhone phone) {
		super(name, ico, phone);
	}
	
	@Override
	public void initializeComponent()
	{
		super.initializeComponent();
		
		addComponent(new UIButton(UIButton.Type.ROUNDED, "send", new ResourceLocation("craftyourliferp", "gui/phone/send_button.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				if(selectedContact != null)
				{
					if(messageInput.getText().length() > 0)
					{

						
						//Send messsage
						CraftYourLifeRPMod.packetHandler.sendToServer(new PacketSendSms(messageInput.getText(),selectedContact.number,phone.playerData.phoneData.getNumber()));
						
						SmsData data = phone.playerData.phoneData.addSms(messageInput.getText(), phone.playerData.phoneData.getNumber(), selectedContact.number);
						
						messageInput.setText("");
						
						GuiScrollableView scrollview = (GuiScrollableView)getChild(1);
						if(scrollview.scrollBarVertical.isVisible()) scrollview.scrollBarVertical.setValue(1f);
			
						
					}
				}
			}
			
		}).setPosition(getWindowPosX() + getWindowWidth() - 21, getWindowPosY() + getWindowHeight() - 15, 20, 11));
		
		messageInput = (UITextField) addComponent(new UITextField(new UIImage(new ResourceLocation("craftyourliferp", "gui/phone/message_input.png")),0.9f,UITextField.Type.TEXT).setPosition(getWindowPosX(), getWindowPosY() + getWindowHeight() - 17,95,15));
		messageInput.setTextColor(new UIColor("#434543"));
		
		updateGuiState();
	}
	
	public SmsButton getElement(String number)
	{
		GuiScrollableView scrollView = (GuiScrollableView) getChild(0);
		
		for(Object obj : scrollView.contentRect.childs)
		{
			SmsButton button = (SmsButton) obj;
			if(button.attribuatedData != null && button.number.equalsIgnoreCase(number))
				return button;
		} 
		return null;
	}
	
	public void addMessage(SmsData message)
	{
		GuiScrollableView scrollView = (GuiScrollableView) getChild(0);
		ExtendedPlayer pData = ExtendedPlayer.get(phone.mc.thePlayer);
		
		String username = "";
		
		boolean receiverIsClient = false;
		
		if(message.receiverNumber.equalsIgnoreCase(pData.phoneData.getNumber())) 
		{
			receiverIsClient = true;
		}
		
		for(ContactData data : pData.phoneData.contacts)
		{
						
			if(receiverIsClient && data.number.equalsIgnoreCase(message.senderNumber))
			{
				username = data.name;
			}
			else if(data.number.equalsIgnoreCase(message.receiverNumber)) 
			{
				username = data.name;	
			}
		}
		
		
		SmsButton button = new SmsButton(new ArrayList<SmsData>(),username,receiverIsClient ? message.senderNumber : message.receiverNumber,Type.SQUARE,new ResourceLocation("craftyourliferp", "gui/phone/contact_container.png"),null);
				
		if(!SmsButton.Contains(button, scrollView.contentRect.getChilds())) //SmsButton button de la liste des sms si elle n'existe pas déjà passe par ici pour le créer
		{
			button.attribuatedData.add(message);
			
			scrollView.addToContainer(button.setPosition(0, 0, 100, 20));
			
			if(selectedContact != null && selectedContact.number.equalsIgnoreCase(button.number)) 
			{
				selectedContact = button;	
			}
			
		}
		else
		{
			SmsButton sb = getElement(receiverIsClient ? message.senderNumber : message.receiverNumber);
			if(sb != null)
			{
				sb.attribuatedData.add(message);
				
				if(selectedContact != null && selectedContact.number.equalsIgnoreCase(sb.number))
				{
					selectedContact = sb;
				}
			}
		}
		
		for(Object obj : scrollView.contentRect.childs)
		{
			SmsButton sb = (SmsButton) obj;
			sb.updateCountSmsNotReaded(sb.attribuatedData);
		}
		
		if(selectedContact != null)
		{
			if(selectedContact.number.equalsIgnoreCase(message.senderNumber)) 
			{
				selectedContact.setReaded();
			}
			
			GuiScrollableView scrollview = (GuiScrollableView) getChild(1);
			scrollview.updateScrollviewContents();
		}
		
	}
	

	
	@Override
    public void onScroll(int i)
    {
    	getChild(0).onScroll(i);
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
	public void registerChilds()
	{
		addChild(new SmsList());
		addChild(new ConversationUI());
		super.registerChilds();
	}

	
	@Override
	public void drawScreen(int x, int y, float partialTicks) 
	{
		GuiUtils.drawImage(getWindowPosX()-2, getWindowPosY()-1, this.phone.background_apps, getWindowWidth()+3, getWindowHeight()+2);
				
		super.drawScreen(x, y, partialTicks);
	}


	@Override
    public void mouseClicked(int x, int y, int mouseBtn)
    {
		super.mouseClicked(x, y, mouseBtn);
        updateGuiState();
    }
	
	@Override
	public void mouseClickMove(int x, int y, int buttonId, long timeSinceLastClick) {
	}
	

	@Override
	public void onGuiClosed() {
		selectedContact = null;
	}
	

	@Override
	public void updateScreen() {
		super.updateScreen();
		
	}

	@Override
	public void keyTyped(char par1, int par2) {
		super.keyTyped(par1, par2);
	}


	public void updateGuiState()
	{
		if(selectedContact != null)
		{
			for(GraphicObject object : getComponents())
			{
				object.setVisible(true);
			}
			getChild(0).setVisible(false);
			getChild(1).setVisible(true);
		}
		else
		{
			for(GraphicObject object : getComponents())
			{
				object.setVisible(false);
			}
			getChild(0).setVisible(true);
			getChild(1).setVisible(false);
		}
	}
	
	
	class SmsList extends GuiScrollableView
	{
		
		@Override
		public void initGui()
		{
			this.spacing = 5;
			this.setWindowSize(112, 140);
			this.setWindowPosition(parent.getWindowPosX()+2, parent.getWindowPosY()+5);
		
			super.initGui();
		}
		
		public void initializeComponent()
		{
			this.contentRect = new UIRect(new UIColor(0,0,0,0));
			this.viewport = new UIImage(new ResourceLocation("craftyourliferp","gui/phone/key_container.png"));
			
			this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#47b4c4"),new UIColor("#144f58")).setHoverColor(new UIColor("#196975"));
			this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#47b4c4"),new UIColor("#144f58")).setHoverColor(new UIColor("#196975"));
			
			this.selectedScrollBar = scrollBarVertical;
			
			setScrollViewPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight());
			parameterVerticalScrollbar(getWindowPosX()+getWindowWidth()-5,getWindowPosY(),6,getWindowHeight());
			
			updateScrollviewContents();

		}

		
		public void drawScreen(int x, int y, float pt)
		{
			super.drawScreen(x, y, pt);
			
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
			 object.localPosY = contentRect.getHeight() + 3;
			 
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

			GuiPhone phone = GuiPhone.getPhone();
			
			for(SmsData data : ExtendedPlayer.get(phone.mc.thePlayer).phoneData.sms)
			{
				addMessage(data);
			}	
		}

	}
	
	class ConversationUI extends GuiScrollableView
	{
		
		@Override
		public void initGui()
		{
			this.spacing = 15;
			this.setWindowSize(121, 134);
			this.setWindowPosition(parent.getWindowPosX()-2, parent.getWindowPosY()-1);
		
			super.initGui();
		}
		
		public void initializeComponent()
		{
			
			addComponent(new UIButton(UIButton.Type.SQUARE, "delete", new ResourceLocation("craftyourliferp", "gui/phone/delete_button.png"), null,false,new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{
					if(selectedContact != null)
					{
						phone.playerData.phoneData.deleteSms(selectedContact.number);
						selectedContact = null;
						GuiScrollableView scrollView = (GuiScrollableView)parent.getChild(0);
						scrollView.updateScrollviewContents();
						
					}
				}
				
			}).setZIndex(200).setPosition(getWindowPosX() + 4, getWindowPosY() + 5,30,6));
			
			this.contentRect = new UIRect(new UIColor(0,0,0,0));
			this.viewport = new UIRect(new UIColor(255,255,255,255));
			
			this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#47b4c4"),new UIColor("#144f58")).setHoverColor(new UIColor("#196975"));
			this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#47b4c4"),new UIColor("#144f58")).setHoverColor(new UIColor("#196975"));
			
			this.selectedScrollBar = scrollBarVertical;
			
			setScrollViewPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight());
			parameterVerticalScrollbar(getWindowPosX()+getWindowWidth()-6,getWindowPosY(),6,getWindowHeight());
			
			updateScrollviewContents();

		}

		
		public void drawScreen(int x, int y, float pt)
		{
			super.drawScreen(x, y, pt);
			if(scrollBarVertical.getValue() < 0.002f)
			{
				GuiUtils.renderCenteredText("SMS/MMS", getWindowPosX() + getWindowWidth() / 2, getWindowPosY() + 6, 0.8f,4408643);
				getComponent(0).setVisible(true);
			}
			else
			{
				getComponent(0).setVisible(false);
			}

		}
	
		
		@Override
		public void registerChilds()
		{
			super.registerChilds();
		}
		
		public void addToContainer(GraphicObject object,SmsData data, boolean senderIsClient)
		{
			 if(object == null)  return;
			 
			 if(senderIsClient)
			 {
				 object.localPosX = 3; 
			 }
			 else
			 {
				Timestamp today = new Timestamp(System.currentTimeMillis());
				Timestamp date = new Timestamp(data.date);


				if(today.getDay() == date.getDay() && today.getMonth() == date.getMonth() && today.getYear() == date.getYear())
				{
					object.localPosX = contentRect.getWidth() - bubble_max_width - 28;
				}
				else
				{
					object.localPosX = contentRect.getWidth() - bubble_max_width - 10;
				}
			 }
			 
			 contentRect.addChild(object);
			 
			 object.localPosY = contentRect.getHeight() + 20;
			 
			 if(object.localPosY + object.getHeight() > contentRect.getHeight())
			 {
				 if(senderIsClient)
				 {
					 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing / 3);
				 }
				 else
				 {
					 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
				 }
			 }
		}
		
		

		
		
		
		@Override
		public void updateScrollviewContents()
		{
			contentRect.childs.clear();
			
			contentRect.setHeight(0);

			GuiPhone phone = GuiPhone.getPhone();
			
			Sms sms = (Sms)parent;
			
			ExtendedPlayer ep = ExtendedPlayer.get(phone.mc.thePlayer);
			
			if(sms.selectedContact != null)
			{
				for(SmsData data : sms.selectedContact.attribuatedData)
				{		
					List<String> array = phone.mc.fontRenderer.listFormattedStringToWidth(data.message, (int) (bubble_max_width * 1/font_scale));
	
					boolean senderIsClient = data.senderNumber.equalsIgnoreCase(ep.phoneData.getNumber());
					
					addToContainer(new SmsObject(data,senderIsClient).setPosition(0, 0, bubble_max_width, (int) (array.size() * phone.mc.fontRenderer.FONT_HEIGHT * (font_scale + 0.1f))),data,senderIsClient);
				}
			}
			
			contentRect.setHeight(contentRect.getHeight() + 20);

			if(scrollBarVertical.isVisible()) 
			{
				scrollBarVertical.setValue(1f);
			}
			else
			{
				scrollBarVertical.setValue(0f);
			}
			
		}

	}

	@Override
	public void back() {
		if(selectedContact != null)
		{
			selectedContact = null;
			updateGuiState();
		}
		else
		{
			phone.currentApp = null;
		}
	}

	@Override
	public void openApps() 
	{
		
	}

}
