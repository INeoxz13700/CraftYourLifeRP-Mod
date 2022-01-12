package fr.craftyourliferp.ingame.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIDropdown;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.network.PacketAnimation;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiAnimation extends GuiScrollableView {
	
	@Override
	public void initGui() 
	{
		setWindowSize(80, 120);
		setWindowPosition(5, 5);
		super.initGui();
	}

	@Override
	public void initializeComponent() 
	{ 
		
		guiRect = (UIRect) new UIRect(new UIColor("#2d2d2e")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight());
		
		addComponent(new UIText("Animations",new UIColor(255,255,255),1.1f).setTextCenter(true).setPosition(getWindowPosX() + getWindowWidth() / 2, getWindowPosY() + 4));
		
		addComponent(new UIRect(new UIColor(36, 46, 227,180)).setPosition(getWindowPosX() + (getWindowWidth() - (getWindowWidth()-20))  / 2,  getWindowPosY() + 14 , getWindowWidth()-20, 1));
		
		contentRect = new UIRect(new UIColor(0,0,0,0));
		viewport = new UIRect(new UIColor(0,0,0,0));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#3A3B37"));
		
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#3A3B37"));
		
		this.selectedScrollBar = scrollBarVertical;
		
		guiRect.color = new UIColor(32,32,32,100);
		
		setScrollViewPosition(getWindowPosX(), getWindowPosY() + 25,getWindowWidth(),130);

		updateScrollviewContents();
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
	
	public void updateScrollviewContents()
	{
		contentRect.childs.clear();
		
		addToContainer(new UIButton(Type.SQUARE,new UIRect(new UIColor(22, 22, 23,120)),"Stop animation", new UIRect(new UIColor(51, 51, 156)), new UIButton.CallBackObject()
		{
			
			public void call()
			{
				if(CraftYourLifeRPClient.cachedData.currentAnimation != 0)
				{
					//CraftYourLifeRPClient.cachedData.resetTransformations = true;
					CraftYourLifeRPClient.cachedData.currentAnimation = 0;
	
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)0));
				}
			}
			
		}).setPosition(0,0,getWindowWidth(), 10));
		
		addToContainer(new UIButton(Type.SQUARE,new UIRect(new UIColor(22, 22, 23,120)),"Lever les mains", new UIRect(new UIColor(51, 51, 156)), new UIButton.CallBackObject()
		{
			
			public void call()
			{
				if(CraftYourLifeRPClient.cachedData.currentAnimation != 1)
				{
					CustomModelPlayer.instance.resetTransformations();
					
					CraftYourLifeRPClient.cachedData.currentAnimation = 0;
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)0));
					
					CraftYourLifeRPClient.cachedData.currentAnimation = 1;
	
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)1));
				}
			}
			
		}).setPosition(0,0,getWindowWidth(), 10));
		
		addToContainer(new UIButton(Type.SQUARE,new UIRect(new UIColor(22, 22, 23,120)),"Coucou", new UIRect(new UIColor(51, 51, 156)), new UIButton.CallBackObject()
		{
			
			public void call()
			{
				if(CraftYourLifeRPClient.cachedData.currentAnimation != 3)
				{
					CustomModelPlayer.instance.resetTransformations();

					CraftYourLifeRPClient.cachedData.currentAnimation = 0;
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)0));

					
					CraftYourLifeRPClient.cachedData.currentAnimation = 3;
	
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)3));
				}
			}
			
		}).setPosition(0,0,getWindowWidth(), 10));
		
		addToContainer(new UIButton(Type.SQUARE,new UIRect(new UIColor(22, 22, 23,120)),"Danse 1", new UIRect(new UIColor(51, 51, 156)), new UIButton.CallBackObject()
		{
			
			public void call()
			{
				if(CraftYourLifeRPClient.cachedData.currentAnimation != 4)
				{
					CustomModelPlayer.instance.resetTransformations();

					CraftYourLifeRPClient.cachedData.currentAnimation = 0;
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)0));
					
					CraftYourLifeRPClient.cachedData.currentAnimation = 4;
	
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)4));
				}
			}
			
		}).setPosition(0,0,getWindowWidth(), 10));
		
		/*addToContainer(new UIButton(Type.SQUARE,new UIRect(new UIColor(22, 22, 23,120)),"Bras d'honneur", new UIRect(new UIColor(51, 51, 156)), new UIButton.CallBackObject()
		{
			
			public void call()
			{
				if(CraftYourLifeRPClient.cachedData.currentAnimation != 1)
				{
					CraftYourLifeRPClient.cachedData.currentAnimation = 1;
	
					CraftYourLifeRPMod.packetHandler.sendToServer(PacketAnimation.setAnimation((byte)5));
				}
			}
			
		}).setPosition(0,0,getWindowWidth(), 10));*/
		
		
		
		super.updateScrollviewContents();
	}
	
	
}



	

