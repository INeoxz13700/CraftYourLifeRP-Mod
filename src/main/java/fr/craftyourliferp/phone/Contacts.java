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
public class Contacts extends Apps {
	
	
	public enum State {
		CONTACT_LIST,
		ADD_CONTACT,
	}
	
	private State state = State.CONTACT_LIST;
	
	public ContactButton selectedContact;
	
	private boolean editContact;
		

	public Contacts(String name, ResourceLocation ico, GuiPhone phone) {
		super(name, ico, phone);
	}
	
	@Override
	public void initializeComponent()
	{
		super.initializeComponent();
		
		addComponent(new UIButton(UIButton.Type.ROUNDED, "add_contact", new ResourceLocation("craftyourliferp", "gui/phone/add_contact.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				if(selectedContact == null)
				{
					state = State.ADD_CONTACT;
				}
			}
			
		}).setPosition(getWindowPosX() + getWindowWidth() - 19, getWindowPosY() + getWindowHeight() - 20, 18, 18).setZIndex(900));
		
		addComponent(new UIButton(UIButton.Type.SQUARE, "save", new ResourceLocation("craftyourliferp", "gui/phone/button_save.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				state = State.CONTACT_LIST;
				
				UITextField contactName = (UITextField)getComponent(7);
				UITextField contactNumber = (UITextField)getComponent(8);
				
				if(!editContact)
				{
					ExtendedPlayer.get(phone.mc.thePlayer).phoneData.addContact(contactName.getText(), contactNumber.getText());
					GuiScrollableView scrollView = (GuiScrollableView) getChild(0);
					scrollView.updateScrollviewContents();
				}
				else
				{
					phone.playerData.phoneData.editContact(selectedContact.attribuatedData, contactName.getText(), contactNumber.getText());

					editContact = false;
		
					selectedContact = null;					
				}
				
				contactName.setText("");
				contactNumber.setText("");		
			}
			
		}).setPosition((getWindowPosX() + (getWindowWidth() - 50) / 2) - 20, getWindowPosY() + 15,40,12));
		
		addComponent(new UIButton(UIButton.Type.SQUARE, "cancel", new ResourceLocation("craftyourliferp", "gui/phone/button_cancel.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				if(state == State.ADD_CONTACT)
				{
					((UITextField)getComponent(7)).setText("");
					((UITextField)getComponent(8)).setText("");					
					state = State.CONTACT_LIST;
				}
			}
			
		}).setPosition((getWindowPosX() + (getWindowWidth() - 50) / 2) + 30, getWindowPosY() + 15,40,12));
		
		addComponent(new UIButton(UIButton.Type.SQUARE, "edit", new ResourceLocation("craftyourliferp", "gui/phone/selected_contact_edit.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				if(state == State.CONTACT_LIST)
				{
					((UITextField)getComponent(7)).setText(selectedContact.attribuatedData.name);
					((UITextField)getComponent(8)).setText(selectedContact.attribuatedData.number);			
					state = State.ADD_CONTACT;
					editContact = true;
				}
			}
			
		}).setPosition(getWindowPosX() + getWindowWidth() - 31, getWindowPosY() + 2,30,6));
		
		addComponent(new UIButton(UIButton.Type.ROUNDED, "call", new ResourceLocation("craftyourliferp", "gui/phone/selected_contact_call.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				if(selectedContact != null)
				{
					phone.openApp(0);
					
					if(selectedContact instanceof PredefinedContactButton)
					{
						Call app = (Call) phone.currentApp;
						app.updateGuiState();

						PredefinedContactButton btn = (PredefinedContactButton) selectedContact;
						btn.executeCommand();
					}
					else
					{
						Call app = (Call) phone.currentApp;
						app.call(selectedContact.attribuatedData.number);
						app.updateGuiState();

					}
					
				}
			}
			
		}).setPosition(getWindowPosX() + getWindowWidth()-20, getWindowPosY() + 95,9,8));
		
		addComponent(new UIButton(UIButton.Type.SQUARE, "delete", new ResourceLocation("craftyourliferp", "gui/phone/delete_button.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				if(selectedContact != null)
				{
					ExtendedPlayer.get(phone.mc.thePlayer).phoneData.deleteContact(selectedContact.attribuatedData);
					selectedContact = null;
					GuiScrollableView scrollView = (GuiScrollableView)getChild(0);
					scrollView.updateScrollviewContents();
				}
			}
			
		}).setPosition(getWindowPosX() + 1, getWindowPosY() + 2,30,6));
		
		addComponent(new UIButton(UIButton.Type.ROUNDED, "send_message", new ResourceLocation("craftyourliferp", "gui/phone/selected_contact_message.png"), null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				phone.openApp(2);
				Sms currentApp = (Sms)phone.currentApp;
				SmsButton b = currentApp.getElement(selectedContact.attribuatedData.number);
				GuiScrollableView scrollView =  (GuiScrollableView) currentApp.getChild(0);
				if(b == null || !scrollView.contentRect.childs.contains(b))
				{
					currentApp.selectedContact = new SmsButton(new ArrayList<SmsData>(),selectedContact.attribuatedData.name, selectedContact.attribuatedData.number,UIButton.Type.SQUARE,new ResourceLocation("craftyourliferp","gui/phone/contact_container.png"),null);
				}
				else
				{
					currentApp.selectedContact = b;
					((GuiScrollableView) currentApp.getChild(1)).updateScrollviewContents();
				}
				selectedContact = null;
				currentApp.updateGuiState();
			}
			
		}).setPosition(getWindowPosX() + getWindowWidth()-40, getWindowPosY() + 95,10,8));
		
		addComponent(new UITextField(new UIImage(new ResourceLocation("craftyourliferp","gui/phone/contact_container.png")),0.9f,UITextField.Type.TEXT).setMaxStringLength(20).setPosition((getWindowPosX() + (getWindowWidth() - 100) / 2) , getWindowPosY() + 60,100,15));
		addComponent(new UITextField(new UIImage(new ResourceLocation("craftyourliferp","gui/phone/contact_container.png")),0.9f,UITextField.Type.NUMBER).setMaxStringLength(10).setPosition((getWindowPosX() + (getWindowWidth() - 100) / 2), getWindowPosY() + 100,100,15));
		
		updateGuiState();
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
		addChild(new ContactList());
		super.registerChilds();
	}

	
	@Override
	public void drawScreen(int x, int y, float partialTicks) 
	{
		GuiUtils.drawImage(getWindowPosX()-2, getWindowPosY()-1, this.phone.background_apps, getWindowWidth()+3, getWindowHeight()+2);
		
		if(state == State.ADD_CONTACT)
		{
			UITextField contactNameTextField = (UITextField) getComponent(7);
			UITextField numberTextField = (UITextField) getComponent(8);
				
			GuiUtils.renderCenteredText("Nom du contact", contactNameTextField.getX() + contactNameTextField.getWidth() / 2 - 5, contactNameTextField.getY() - 12, 0.9f,4210752);
			GuiUtils.renderCenteredText("Numéro", numberTextField.getX() + numberTextField.getWidth() / 2 - 5, numberTextField.getY() - 12, 0.9f,4210752);

		}
		else
		{
			if(selectedContact != null)
			{
				new UIRect(new UIColor("#ababab")).setPosition(getWindowPosX()-2, getWindowPosY()-1, getWindowWidth()+3, 78).draw(x, y);
				GuiUtils.drawImage(getWindowPosX()-2, getWindowPosY()+getWindowHeight()/2, new ResourceLocation("craftyourliferp","gui/phone/numero_container.png"), getWindowWidth()+3, 40);
				GuiUtils.drawImage(getWindowPosX()+(getWindowWidth() - 30) / 2, getWindowPosY()+20, new ResourceLocation("craftyourliferp","gui/phone/profil.png"), 30, 30);
				GuiUtils.renderText("§l" + selectedContact.attribuatedData.name, getWindowPosX() + 2, getWindowPosY()+65,GuiUtils.gameColor, 0.9f);
				GuiUtils.renderCenteredText("Numéro téléphone", getWindowPosX() + 42, getWindowPosY()+80, 0.9f,4210752);
				GuiUtils.renderText(selectedContact.attribuatedData.number, getWindowPosX() + 2, getWindowPosY()+95, 4210752,0.9f);
			}
			else
			{
				GraphicObject obj = getComponent(0);
				
				if(!obj.isHover(x, y))
				{
				
					GuiScrollableView scrollView = (GuiScrollableView) this.getChild(0);
					if(scrollView.scrollBarVertical.isVisible() && (scrollView.scrollBarVertical.isHover(x, y) || scrollView.scrollBarVertical.dragging))
					{
						if(obj.isVisible())
						{
							obj.setVisible(false);
						}	
					}
					else
					{
						if(!obj.isVisible())
						{
							obj.setVisible(true);
						}	
					}
				}
			}
		}
	
		
		super.drawScreen(x, y, partialTicks);
	}


	@Override
    public void mouseClicked(int x, int y, int mouseBtn)
    {
		UIButton addContactBtn = (UIButton) getComponent(0);
		
		if(addContactBtn.isClicked(x, y))
		{
			addContactBtn.callback.call();
	        updateGuiState();
			return;
		}
		
		super.mouseClicked(x, y, mouseBtn);
        updateGuiState();
    }
	
	@Override
	public void mouseClickMove(int x, int y, int buttonId, long timeSinceLastClick) {
	}
	

	@Override
	public void onGuiClosed() {
	
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
		if(state == State.CONTACT_LIST)
		{
			if(selectedContact == null)
			{
				for(int i = 0; i < getComponents().size(); i++)
				{
					if(i == 0)
					{
						getComponent(i).setVisible(true);
					}
					else
					{
						getComponent(i).setVisible(false);
					}
				}
				getChild(0).setVisible(true);
			}
			else
			{
				if(selectedContact instanceof PredefinedContactButton)
				{
					for(int i = 0; i < getComponents().size(); i++)
					{
						if(i == 4)
						{
							getComponent(i).setVisible(true);
						}
						else
						{
							getComponent(i).setVisible(false);
						}
					}
				}
				else
				{
					for(int i = 0; i < getComponents().size(); i++)
					{
						if(i >= 0 && i <= 2 || i>=7 && i<=8)
						{
							getComponent(i).setVisible(false);
						}
						else
						{
							getComponent(i).setVisible(true);
						}
					}
				}
				getChild(0).setVisible(false);

			}
		}
		else
		{
			for(int i = 0; i < getComponents().size(); i++)
			{
				if(i == 1 || i == 2 || i == 7 || i == 8)
				{
					getComponent(i).setVisible(true);
				}
				else
				{
					getComponent(i).setVisible(false);
				}
			}
			getChild(0).setVisible(false);
		}
	}
	
	
	class ContactList extends GuiScrollableView
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
			
			addToContainer(new PredefinedContactButton("/call policier", new ContactData("Police","17"),Type.SQUARE,new ResourceLocation("craftyourliferp","gui/phone/contact_container.png"),null).setPosition(0, 0, 100, 20));
			addToContainer(new PredefinedContactButton("/call pompier", new ContactData("Pompier","18"),Type.SQUARE,new ResourceLocation("craftyourliferp","gui/phone/contact_container.png"),null).setPosition(0, 0, 100, 20));
			addToContainer(new PredefinedContactButton("/call taxi", new ContactData("Taxi","118"),Type.SQUARE,new ResourceLocation("craftyourliferp","gui/phone/contact_container.png"),null).setPosition(0, 0, 100, 20));

			for(ContactData data : ExtendedPlayer.get(phone.mc.thePlayer).phoneData.contacts)
			{
				addToContainer(new ContactButton(data,Type.SQUARE,new ResourceLocation("craftyourliferp","gui/phone/contact_container.png"),null).setPosition(0, 0, 100, 20));
			}
			
		}

	}


	@Override
	public void back() {
		if(selectedContact != null)
		{
			selectedContact = null;
		}
		else if(state == State.ADD_CONTACT)
		{
			state = State.CONTACT_LIST;
		}
		else
		{
			phone.currentApp = null;
			return;
		}
		updateGuiState();
	}

	@Override
	public void openApps() {
		// TODO Auto-generated method stub
		
	}

}
