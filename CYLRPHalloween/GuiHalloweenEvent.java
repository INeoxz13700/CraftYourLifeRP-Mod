package fr.craftyourliferp.ingame.gui;

import java.util.Arrays;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.halloween.HalloweenEvent;
import fr.craftyourliferp.halloween.HalloweenEvent.HalloweenItem;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketHalloweenEvent;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;

public class GuiHalloweenEvent extends GuiGridView 
{
	
	private String[] displayHoverString;
	
	public GuiHalloweenEvent()
	{
	   CraftYourLifeRPMod.halloweenEvent.items.clear();
       CraftYourLifeRPMod.halloweenEvent = new HalloweenEvent(Side.CLIENT);
	}
		
	public void initGui()
	{
		elementSize = 100; 
		spacing = 30;
		this.setWindowSize(180, 150);
		this.setWindowPosition((width - 180)/2, (height-150)/2);
		super.initGui();
		this.updateScrollviewContents();
	}
	
	@Override
	public void initializeComponent()
	{
		addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/halloween/guicontainer.png")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
		addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/halloween/guitittle.png")).setPosition(getWindowPosX() + (getWindowWidth() - 140) / 2, getWindowPosY() - 48, 140, 50));
		addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/halloween/skull.png")).setPosition(getWindowPosX() + 75, getWindowPosY() - 40, 30, 30));

		
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		
		this.viewport = new UIRect(new UIColor(0,0,0,0));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor(29, 102, 51),new UIColor(77, 177, 114));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor(29, 102, 51),new UIColor(77, 177, 114));
		this.selectedScrollBar = scrollBarVertical;

		setScrollViewPosition(getWindowPosX() + 35, getWindowPosY() + 20, 110, 90);
		parameterVerticalScrollbar(getWindowPosX() + getWindowWidth() - 40, getWindowPosY() + 20, 4, 90);
	}
	
	@Override
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 1)
        {
            this.mc.displayGuiScreen((GuiScreen)null);
            this.mc.setIngameFocus();
            return;
        }
        super.keyTyped(p_73869_1_, p_73869_2_);
    }
	
	public void updateScrollviewContents()
	{
		contentRect.childs.clear();
		this.resetContainerLayout();
		
		for(HalloweenItem item : HalloweenEvent.items)
		{
			HalloweenGuiItem guiItem = new HalloweenGuiItem(new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{
					int itemIndex = HalloweenEvent.items.indexOf(item);
					CraftYourLifeRPMod.packetHandler.sendToServer(new PacketHalloweenEvent(itemIndex));
				}
				
			}, item);
			addToContainer(guiItem.setPosition(0, 0,elementSize,30));
		}
	
	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);
		
		GuiUtils.renderCenteredText("Fin : " + getDisplayTime(), getWindowPosX() + getWindowWidth() / 2, getWindowPosY() + getWindowHeight() - 32);

		if(displayHoverString != null && displayHoverString.length != 0)
		{
			drawHoveringText(Arrays.asList(displayHoverString), x, y, Minecraft.getMinecraft().fontRenderer);
		}
		
		displayHoverString = new String[0];
		

	}
	
	public String getDisplayTime()
	{
		long leftTime = CraftYourLifeRPMod.halloweenEvent.eventExpireTime - System.currentTimeMillis();
		
		if(leftTime < 0)
		{
			leftTime = 0;
		}
		
		int hours = (int) (leftTime / 1000 / 60 / 60); 
		int minutes = (int) (leftTime / 1000 / 60) % 60;
		int seconds = (int)(leftTime / 1000 % 60);


		String hoursStr = hours + "";
		if(hours <= 9)
		{
			hoursStr = "0" + hours;
		}
		
		String minutesStr = minutes + "";

		if(minutes <= 9)
		{
			minutesStr = "0" + minutes;
		}

		String secondsStr = seconds + "";

		if(seconds <= 9)
		{
			secondsStr = "0" + seconds;
		}
		
		return hoursStr + ":" + minutesStr + ":" + secondsStr;
	}
	
	@Override
	protected void positionElement(GraphicObject object)
	{
		if(contentRect.getChilds().size() == 0) 
		{
			 lastElementposX = object.localPosX += lastElementposX + 5;
			 lastElementposY = object.localPosY;
			 contentRect.setHeight(contentRect.getHeight() + 30);
		 }
		 else
		 {
			 lastElementposX = object.localPosX += lastElementposX + elementSize;
			 object.localPosY = lastElementposY;
		 }
			 
		 if(lastElementposX + object.getWidth() > viewport.getWidth())
		 {
			 lastElementposY = object.localPosY += spacing;
			 object.localPosX = lastElementposX = 5;
			 contentRect.setHeight(contentRect.getHeight() + spacing);
		 }		 
	}

	
	class HalloweenGuiItem extends GraphicObject implements IGuiClickableElement
	{
		private HalloweenItem item;
		
		private UIButton boughtItem;
		private UIButton moneyItem;


		public HalloweenGuiItem(CallBackObject callback, HalloweenItem item) 
		{
			boughtItem = new UIButton(Type.SQUARE, "", new ResourceLocation("craftyourliferp","gui/halloween/guislot.png"), new ResourceLocation("craftyourliferp","gui/halloween/guislot_hover.png"),false, callback);
			moneyItem = new UIButton(Type.SQUARE, "", new ResourceLocation("craftyourliferp","gui/halloween/guislot.png"), new ResourceLocation("craftyourliferp","gui/halloween/guislot_hover.png"),false, null);

			this.item = item;
		}
		
		@Override
		public GraphicObject setPosition(int x, int y, int width, int height)
		{
			super.setPosition(x, y, width, height);
			moneyItem.setPosition(x + 1, y + (height - 30) / 2, 30, 30);

			boughtItem.setPosition(moneyItem.getX2() + 35, y + (height - 30) / 2, 30, 30);

			return this;
		}
		
		@Override
		public void draw(int x, int y)
		{
			super.draw(x, y);

			boughtItem.draw(x, y);
			
			moneyItem.draw(x, y);
			GuiUtils.renderItemStackIntoGUI(new ItemStack(HalloweenEvent.moneyItem,1), moneyItem.getX() + (moneyItem.getWidth() - 16) / 2, moneyItem.getY() + (moneyItem.getHeight() - 16) / 2, "", new RenderItem());
			
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, 100);
			GuiUtils.renderText("x" + item.price, moneyItem.getX2()-20, moneyItem.getY2()-10,GuiUtils.gameColor,0.7f);
			GL11.glPopMatrix();
			
			GuiUtils.renderText("=", moneyItem.getX2() + 15, (moneyItem.getY() + moneyItem.getHeight() / 2) - 4);
			
			if(item.texture.equals("ItemStack"))
			{
				ItemStack is = (ItemStack)item.itemType;
				GuiUtils.renderItemStackIntoGUI(is, boughtItem.getX() + (boughtItem.getWidth() - 16) / 2, boughtItem.getY() + (boughtItem.getHeight() - 16) / 2, "", new RenderItem());
			}
			else if(item.texture.contains("ResourceLocation"))
			{
				String location = item.texture.split("=")[1];
				ResourceLocation texture = new ResourceLocation(location);
				GuiUtils.drawImage(boughtItem.getX() + (boughtItem.getWidth() - 16) / 2, boughtItem.getY() + (boughtItem.getHeight() - 16) / 2, texture, 16, 16);
			}
			
			if(boughtItem.isHover(x, y))
			{
				displayHoverString = new String[] { item.displayName, " §bClique pour échanger" };
			}
			else if(moneyItem.isHover(x, y))
			{
				displayHoverString = new String[] { "§cx" + item.price + " jeton d'halloween" };
			}

		}

		@Override
		public boolean onRightClick(int x, int y) {
			return false;
		}

		@Override
		public boolean onLeftClick(int x, int y) 
		{
			if(boughtItem.isClicked(x, y))
			{
				boughtItem.callback.call();
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
