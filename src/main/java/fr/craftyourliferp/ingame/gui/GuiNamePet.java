package fr.craftyourliferp.ingame.gui;

import astrotibs.notenoughpets.entity.IPetData;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.network.PacketAnimation;
import fr.craftyourliferp.network.PacketPet;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.EntityLiving;

public class GuiNamePet extends GuiBase
{
	
	private EntityLiving livingEntity;
	
	public GuiNamePet(EntityLiving livingEntity)
	{	
		this.livingEntity = livingEntity;
	}

	@Override
	public void initGui() 
	{
		setWindowSize(width, height);
		setWindowPosition(0, 0);
		super.initGui();
	}

	@Override
	public void initializeComponent() 
	{ 
		
		guiRect = (UIRect) new UIRect(new UIColor(0,0,0,100)).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight());
		
		addComponent(new UIText("Donnez un nom à votre animal de compagnie!",new UIColor(255,255,255),1.1f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY() + 10));
		
		addComponent(new UIRect(new UIColor(36, 46, 227,180)).setPosition(getWindowPosX() + (getWindowWidth() - (getWindowWidth()-20))  / 2,  getWindowPosY() + 30 , getWindowWidth()-20, 1));
		UITextField textField = (UITextField)addComponent(new UITextField(new UIRect(new UIColor(0, 0, 0,180)), 1f, UITextField.Type.TEXT).setPosition((width-200)/2, getWindowPosY() + 60, 200, 20));
		
		addComponent(new UIButton(UIButton.Type.SQUARE,new UIRect(new UIColor(0, 0, 0,180)),"Valider",new UIRect(new UIColor(30, 30, 30,150)),new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				CraftYourLifeRPMod.packetHandler.sendToServer(PacketPet.changeName(textField.getText()));
				mc.displayGuiScreen(null);
			}
			
		}).setPosition((width-150)/2, getWindowPosY() + 120, 150, 20));

	}
	
	@Override
    protected void keyTyped(char character, int keycode)
    {
        if (keycode == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
            return;
        }
        
        super.keyTyped(character, keycode);
    }
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();

		if(getGuiTicks() % 20 == 0 && mc.thePlayer.getDistanceSqToEntity(livingEntity) >= 36)
		{
			mc.displayGuiScreen(null);
		}
	}
	
	
	
}
