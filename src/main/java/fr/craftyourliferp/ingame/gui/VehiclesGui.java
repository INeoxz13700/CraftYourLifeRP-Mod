package fr.craftyourliferp.ingame.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.network.PacketBuySlot;
import com.flansmod.common.network.PacketDeleteVehicle;
import com.flansmod.common.network.PacketInitialiseVehicleGui;
import com.flansmod.common.network.PacketSpawnVehicle;
import com.flansmod.common.network.PacketInitialiseVehicleGui.Category;
import com.ibm.icu.impl.duration.TimeUnit;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIDialogBox;
import fr.craftyourliferp.guicomponents.UIDropdown;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class VehiclesGui extends GuiGridView
{
	
	private VehicleGuiItems selectedVehicle;
	
	public UIDropdown selectedCategory;	
	
	private String previousSelectedCategory;
	
	public ArrayList<VehicleGuiItems> items = new ArrayList<VehicleGuiItems>();
	
	public ArrayList<ItemStack> ownedVehicle = new ArrayList<ItemStack>();
	
	private UIDialogBox currentDisplayedDialogBox;
	
	public ExtendedPlayer playerData;
	
	private int previousScale;
	
	private int vcoins = 0;

    
    public VehiclesGui(EntityPlayer player)
    {
       elementSize = 53;
       spacing = 5;
       playerData = ExtendedPlayer.get(player);
       playerData.getVcoins();
       vcoins = playerData.vCoins;
       
       
       GameSettings settings = Minecraft.getMinecraft().gameSettings;
       this.previousScale = settings.guiScale;
       settings.guiScale = 3;
    }
    
	@Override
	public void initializeComponent() 
	{ 
		setWindowPosition((width - 350)/2,(height-200)/2);
		setWindowSize(350,200);
		addComponent(new UIRect(new UIColor("#212121")).setContourColor(new UIColor("#284bd4")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
		addComponent(new UIImage(new ResourceLocation("craftyourliferp", "gui/vehicles/button_ownedVehicle.png")).setPosition(getWindowPosX()+10, getWindowPosY()+10, 80, 30));
		selectedCategory = (UIDropdown) addComponent(new UIDropdown(10,Arrays.asList(new String[] {"Voiture","Avion/Hélicoptère", "Bateau"}),new UIColor("#2f302f")).setPosition(getWindowPosX()+10, getWindowPosY()+80, 80, 15));
		addComponent(new UIImage(new ResourceLocation("craftyourliferp", "gui/vehicles/vcoins_container.png")).setPosition(getWindowPosX()+getWindowWidth()-90, getWindowPosY()+getWindowHeight()-15, 90, 15));
		addComponent(new UIImage(new ResourceLocation("craftyourliferp", "gui/vehicles/vcoins.png")).setPosition(getWindowPosX()+getWindowWidth()-87, getWindowPosY()+getWindowHeight()-13, 10, 10));
		addComponent(new UIButton(Type.SQUARE,"delete_btn",new ResourceLocation("craftyourliferp", "gui/vehicles/button_delete.png"),null,false,new UIButton.CallBackObject()
		{
			
			@Override
			public void call()
			{
				currentDisplayedDialogBox = displayDialogBox("Voulez-vous supprimez ce véhicule de vos sauvegardes ?", 
				new CallBackObject()
				{
					@Override
					public void call()
					{
						mc.thePlayer.closeScreen();
						FlansMod.packetHandler.sendToServer(new PacketDeleteVehicle(Item.getIdFromItem(selectedVehicle.AttribuedItemStack.getItem())));
					}
				}, 
				new CallBackObject()
				{
					@Override
					public void call()
					{
						destroyDialogBox(currentDisplayedDialogBox);
					}
				}
			   );

			}
			
		}).setPosition(getWindowPosX()+(int)(getWindowWidth()/3.6f), getWindowPosY()+getWindowHeight()-17, 55, 15));
		
		this.previousSelectedCategory = this.selectedCategory.getSelectedElement();
		
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		this.viewport = new UIRect(new UIColor("#2f3030"));

		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor(0,0,0,255),new UIColor(255,255,255,255)).setHoverColor(new UIColor(190,190,190));
		this.scrollBarVertical.setButtonColor(new UIColor(100,100,100));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor(0,0,0,255),new UIColor(255,255,255,255)).setHoverColor(new UIColor(190,190,190));
		this.scrollBarHorizontal.setButtonColor(new UIColor(100,100,100));
		
		this.selectedScrollBar = scrollBarVertical;
		
		this.setScrollViewPosition(getWindowPosX() + (int)(getWindowWidth() / 3.6f), getWindowPosY() + 10, 245, 170);
		
		this.updateScrollviewContents();
	}
	
	public void updateScrollviewContents()
	{
		items.clear();
		for(ItemStack is : ownedVehicle)
		{
			VehicleGuiItems item = new VehicleGuiItems(new UIButton(Type.SQUARE,"button",new ResourceLocation("craftyourliferp","gui/vehicles/button_recovery.png"),null, false,new UIButton.CallBackObject()
			{
				
				@Override
				public void call()
				{
					recoveryVehicle(Item.getIdFromItem(is.getItem()));
				}
				
				
			}), is);
			items.add(item);
		}

		if(playerData.ownedSlot < 11)
		{
			VehicleGuiItems unlock = new VehicleGuiItems(new CallBackObject()
			{
				
				@Override
				public void call()
				{
					currentDisplayedDialogBox = displayDialogBox("Voulez-vous acheter un slot ? vous avez " + vcoins + " vCoins \n " + EnumChatFormatting.RED + "Prix du slot : " + EnumChatFormatting.GREEN + (playerData.ownedSlot * ExtendedPlayer.slotPriceFactor), 
					new CallBackObject()
					{
						@Override
						public void call()
						{
							FlansMod.packetHandler.sendToServer(new PacketBuySlot());
							currentDisplayedDialogBox.setText("Voulez-vous acheter un slot ? vous avez " + vcoins + " vCoins \n " + EnumChatFormatting.RED + "Prix du slot : " + EnumChatFormatting.GREEN + (playerData.ownedSlot * ExtendedPlayer.slotPriceFactor));
							initGui();
							
							if(playerData.ownedSlot >= 11)
							{
								destroyDialogBox(currentDisplayedDialogBox);
							}
						}
					},
					new CallBackObject()
					{
						@Override
						public void call()
						{
							destroyDialogBox(currentDisplayedDialogBox);
						}
					});
				}
				
				
			});
			items.add(unlock);
		}
		
		updateContentElements();
	}

	public void updateContentElements()
	{
		contentRect.childs.clear();
		this.resetContainerLayout();
		for(GraphicObject obj : items)
		{
			obj.localPosX = 0;
			obj.localPosY = 0;
			addToContainer(obj.setPosition(0, 0, elementSize,elementSize));
		}
		contentRect.setHeight(contentRect.getHeight() + spacing);
	}
    
    public void initGui()
    {
    	this.items.clear();
    	
		super.initGui();
    }
    
	 @Override
	 protected void keyTyped(char character, int keycode)
	 {
	    if (keycode == 1)
	    {
	        this.mc.displayGuiScreen((GuiScreen)null);
	        this.mc.setIngameFocus();
	    }	 
	  }
	

    @Override
    public void drawScreen(int x, int y, float f)
    {	
        super.drawScreen(x, y, f);
    	GraphicObject dropdownObject = getComponent(2);
    	GuiUtils.renderCenteredText("Catégorie : ", dropdownObject.getX()+dropdownObject.getWidth()/2, dropdownObject.getY()-12);
    	
    	GraphicObject vcoinsContainerObject = getComponent(3);
    	vcoins = playerData.vCoins;
    	GuiUtils.renderText(""+vcoins, vcoinsContainerObject.getX()+ 20, vcoinsContainerObject.getY()+4);

    	UIButton deleteButton = (UIButton)getComponent(5);
		if(this.selectedVehicle == null) deleteButton.setEnabled(false);
		else deleteButton.setEnabled(true);
		
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

	
	
    @Override
    protected void mouseClicked(int x, int y, int id)
	{
		 super.mouseClicked(x,y,id);
		 
		 for(VehicleGuiItems item : items)
		 {
			 if(!item.isUnlockSlot && item.isHover(x, y))
			 {
				 if(selectedVehicle != null)
				 {
					 selectedVehicle.isSelected = false;
				 }
				 
				 item.isSelected = true;
				 selectedVehicle = item;
				 return;
			 }
		 }

    }
	
	public void recoveryVehicle(int vehicleId) {
		FlansMod.packetHandler.sendToServer(PacketSpawnVehicle.spawnVehicle(vehicleId));
	}
	
	public void payInsurance(int vehicleId)
	{
		FlansMod.packetHandler.sendToServer(PacketSpawnVehicle.payInpound(vehicleId));
	}
	
	@Override
	public void onGuiClosed() 
	{
		super.onGuiClosed();
		Minecraft.getMinecraft().gameSettings.guiScale = this.previousScale;
	}
	
	@Override
	public void updateScreen()
	{
		super.updateScreen();
		if(getGuiTicks() % (20*5) == 0)
		{
			playerData.getVcoins();
		}
		
		if(!selectedCategory.getSelectedElement().equalsIgnoreCase(previousSelectedCategory))
		{
			previousSelectedCategory = selectedCategory.getSelectedElement();
			ownedVehicle.clear();
			contentRect.childs.clear();
			resetContainerLayout();
			Category category = null;
			if(selectedCategory.getSelectedElement().equals("Avion/Hélicoptère"))
			{
				category = Category.Plane;
			}
			else if(selectedCategory.getSelectedElement().equals("Voiture"))
			{
				category = Category.Vehicle;
			}
			else
			{
				category = Category.Boat;
			}
			FlansMod.packetHandler.sendToServer(new PacketInitialiseVehicleGui(category));
		}
	}
	
	@Override
	public void positionElement(GraphicObject object)
	{
		 if(contentRect.getChilds().size() == 0) 
		 {
			 lastElementposX = object.localPosX += 5 + lastElementposX + spacing;
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
			 object.localPosX = lastElementposX = 5 + spacing;
			 contentRect.setHeight(contentRect.getHeight() + elementSize + spacing);
		 }
	}
	

       
    
}
