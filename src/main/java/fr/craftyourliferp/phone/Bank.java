package fr.craftyourliferp.phone;

import java.util.ArrayList;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.ContactData;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.SmsData;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.ingame.gui.GuiScrollableView;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketAtm;
import fr.craftyourliferp.phone.Contacts.ContactList;
import fr.craftyourliferp.phone.Contacts.State;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class Bank extends Apps {
	
	
	public enum State {
		HOME,
		PAYMENT,
		WIDTHDRAWAL;
	}
	
	private State state = State.HOME;
	
	//private PlayerCachedData tempData;
	
	private ExtendedPlayer extendedPlayer;
			

	public Bank(String name, ResourceLocation ico, GuiPhone phone) 
	{
		super(name, ico, phone);
		
		//tempData = PlayerCachedData.getData(Minecraft.getMinecraft().thePlayer);
		extendedPlayer = ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer);
	}
	
	@Override
	public void initializeComponent()
	{
		super.initializeComponent();
		
		
		
		addComponent(new UIRect(new UIColor(255,255,255)).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
		
		addComponent(new UIRect(new UIColor(40,68,209)).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), 5));
		addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/phone/bank_logo.png")).setPosition(getWindowPosX() + 5, getWindowPosY() + 10, 80, 15));
		addComponent(new UIRect(new UIColor(40,68,209)).setPosition(getWindowPosX() + (getWindowWidth() - 110) / 2, getWindowPosY() + 28, 110, 2));
		
		if(state == State.HOME)
		{

			addComponent(new UIText("Bienvenue",new UIColor(80,80,80),1f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY()+50,0,0));
			addComponent(new UIText(phone.playerData.identityData.name + " " + phone.playerData.identityData.lastname,new UIColor(80,80,80),1f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY()+62,0,0));

			addComponent(new UIButton(UIButton.Type.SQUARE, new UIRect(new UIColor(40,68,209)), "Versement", new UIRect(new UIColor(20,20,209)),new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{
					state = State.PAYMENT;
					updateGuiState();
				}
				
			}).setPosition(getWindowPosX() + (getWindowWidth() - 60) / 2, getWindowPosY() + 90, 60, 15).setZIndex(900));

			addComponent(new UIButton(UIButton.Type.SQUARE, new UIRect(new UIColor(40,68,209)), "Retrait", new UIRect(new UIColor(20,20,209)),new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{
					state = State.WIDTHDRAWAL;
					updateGuiState();
				}
				
			}).setPosition(getWindowPosX() + (getWindowWidth() - 60) / 2, getWindowPosY() + 110, 60, 15).setZIndex(900));

		}
		else if(state == State.PAYMENT)
		{
			
			addComponent(new UIRect(new UIColor(40,68,209)).setPosition(getWindowPosX(), getWindowPosY()+55, getWindowWidth(), 15));
			addComponent(new UIText("Entrez l'argent à verser",new UIColor(255,255,255),0.7f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY()+60,0,0));


			UIRect textfieldRect = new UIRect(new UIColor(255,255,255));
			textfieldRect.contourColor = new UIColor(40,68,209);
			
			addComponent(new UITextField(textfieldRect,0.9f,UITextField.Type.NUMBER).setTextColor(new UIColor(80,80,80)).setPosition((getWindowPosX() + (getWindowWidth() - 100) / 2) , getWindowPosY() + 80,100,15));

			addComponent(new UIButton(UIButton.Type.SQUARE, new UIRect(new UIColor(40,68,209)), "Verser", new UIRect(new UIColor(20,20,209)),new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{
					UITextField textField = (UITextField)getComponent(6);
					
					if(textField.getText().isEmpty())
					{
						return;
					}
					
					
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAtm.packetMoneyInterraction((byte)0, Integer.parseInt(textField.getText())));
				}
				
			}).setPosition(getWindowPosX() + (getWindowWidth() - 60) / 2, getWindowPosY() + 120, 60, 15).setZIndex(900));

			addComponent(new UIText("§c20% de taxe appliqué",new UIColor(255,255,255),0.7f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY()+145,0,0));

		}
		else if(state == State.WIDTHDRAWAL)
		{
			
			addComponent(new UIRect(new UIColor(40,68,209)).setPosition(getWindowPosX(), getWindowPosY()+55, getWindowWidth(), 15));
			addComponent(new UIText("Entrez l'argent à retirer",new UIColor(255,255,255),0.7f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY()+60,0,0));


			UIRect textfieldRect = new UIRect(new UIColor(255,255,255));
			textfieldRect.contourColor = new UIColor(40,68,209);
			
			addComponent(new UITextField(textfieldRect,0.9f,UITextField.Type.NUMBER).setTextColor(new UIColor(80,80,80)).setPosition((getWindowPosX() + (getWindowWidth() - 100) / 2) , getWindowPosY() + 80,100,15));

			addComponent(new UIButton(UIButton.Type.SQUARE, new UIRect(new UIColor(40,68,209)), "Retirer", new UIRect(new UIColor(20,20,209)),new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{

					UITextField textField = (UITextField)getComponent(6);
					
					
					if(textField.getText().isEmpty())
					{
						return;
					}
					
					
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAtm.packetMoneyInterraction((byte)1, Integer.parseInt(textField.getText())));
				}
				
			}).setPosition(getWindowPosX() + (getWindowWidth() - 60) / 2, getWindowPosY() + 120, 60, 15).setZIndex(900));

			addComponent(new UIText("§c20% de taxe appliqué",new UIColor(255,255,255),0.7f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY()+145,0,0));
		}
		
		
		
	}
	
	@Override
	public void initGui()
	{
		GuiPhone.GuiApps guiApps = (GuiPhone.GuiApps) phone.getChild(0);
		this.setWindowSize(guiApps.getWindowWidth()+3, guiApps.getWindowHeight()+2);
		this.setWindowPosition(guiApps.getWindowPosX()-2, guiApps.getWindowPosY()-1);
		super.initGui();
	}
	
	@Override
	public void registerChilds()
	{
		super.registerChilds();
	}


	@Override
    public void mouseClicked(int x, int y, int mouseBtn)
    {
		super.mouseClicked(x, y, mouseBtn);
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
		initGui();
	}

	@Override
	public void back() 
	{
		if(state == State.PAYMENT || state == State.WIDTHDRAWAL)
		{
			state = State.HOME;
		}
		else
		{
			phone.currentApp = null;
		}
		
		updateGuiState();
	}

	@Override
	public void openApps() {
		// TODO Auto-generated method stub
		
	}
	

}