package fr.craftyourliferp.ingame.gui;

import java.awt.Color;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

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
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

public class GuiCosmetics extends GuiGridView {

	private List<CosmeticObject> contents = new ArrayList();
	
	private List<CosmetiqueSlot> slots = new ArrayList();
	
	private String lastSelectedElement;
	
	public static ExtendedPlayer pe;
	
	public static EntityPlayer player;
	
	public GuiCosmetics(ExtendedPlayer pe)
	{
		super(new UIColor("#1f1f1f"),new UIColor("#3A3B37"), false);
		elementSize = 23;
		spacing = 7;
		GuiCosmetics.pe = pe;
		player = Minecraft.getMinecraft().thePlayer;
	}
	
	
	@Override
	public void initGui() 
	{
		slots.clear();
		updateContents();
		setWindowSize(250, 200);
		setWindowPosition((this.width-250) / 2, (this.height - 200)/2);
		super.initGui();
	}
	
	public void updateContents()
	{
		List<CosmeticObject> updateContent = new ArrayList();

		for(int i = 0; i < pe.cosmeticsData.size(); i++)
		{
			CosmeticObject cosmetic = pe.cosmeticsData.get(i);
			updateContent.add(cosmetic);
		}
		
		contents = updateContent;
	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);
		GuiUtils.renderCenteredText("Cosmetiques", guiRect.getX() + guiRect.getWidth() / 2, guiRect.getY() + 10,1f);
		if(this.mouseOnDropdown(x, y)) return;
		for(GraphicObject component : contentRect.visibleChilds)
		{
			if(component.isHover(x, y))
			{
				CosmetiqueSlot slot = (CosmetiqueSlot) component;
 				drawHoveringText(Arrays.asList(new String[] {slot.getCosmeticObj().getName()}),x,y,mc.fontRenderer);
			}
		}
	}
	
	@Override
	public void initializeComponent() 
	{ 
		this.addComponent(new UIButton(Type.SQUARE,null,new ResourceLocation("craftyourliferp","gui/close.png"),null,false,new UIButton.CallBackObject()
		{
			@Override
			public void call()
			{
				mc.currentScreen = null;
			}
			
		}).setPosition(guiRect.getX2()-15, guiRect.getY()+5,10,10));
		
		this.addComponent(new UIDropdown(10, Arrays.asList(new String[] {"tout","équipé","débloqué","tête","visage","corps"}),new UIColor("#282828")).setPosition(guiRect.getX() + (guiRect.getWidth() - 222) / 2, guiRect.getY() + 22 , 222,10));
		
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		
		this.viewport = new UIRect(new UIColor("#282828"));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#3A3B37"));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#3A3B37"));
		this.selectedScrollBar = scrollBarVertical;
		
		guiRect.color = new UIColor("#1f1f1f");
		guiRect.contourColor = new UIColor("#66a0c6");
		
		setScrollViewPosition(guiRect.getX() + (guiRect.getWidth() - 222) / 2, guiRect.getY()+35, 222, 160);
		

		updateScrollviewContents();

		
		updateContentElements();
	}
	
	public void updateScrollviewContents()
	{
		slots.clear();
		UIDropdown dropdown = (UIDropdown) getComponent(1);
		for(CosmeticObject co : getCosmeticsByFilter(dropdown.getSelectedElement()))
		{

				CosmetiqueSlot slot = new CosmetiqueSlot(co,Type.SQUARE,new UIRect(new UIColor("#373737")), null, new UIRect(new UIColor(255,255,255,20),new UIColor("#66a0c6")), new UIButton.CallBackObject()
				{
					@Override
					public void call()
					{
						if(co.getIsEquipped())
						{
							co.unequipCosmetic(player, co.getId());
						}
						else
						{
							List<CosmeticObject> toUnEquip = CosmeticObject.getEquippedCosmeticFromSameType(player, co.getType());
							for(CosmeticObject obj : toUnEquip)
							{
								co.unequipCosmetic(player, obj.getId());
							}
							co.equipCosmetic(player,co.getId());
						}
					}				
				});
				slots.add(slot);
		}
		this.updateContentElements();
	}
	
	public List<CosmeticObject> getCosmeticsByFilter(String filter)
	{
		if(filter.equalsIgnoreCase("tout"))
		{
			return contents;
		}
		else if(filter.equalsIgnoreCase("débloqué"))
		{
			return contents.stream().filter(x -> !x.getIsLocked()).collect(Collectors.toList());
		}
		else if(filter.equalsIgnoreCase("équipé"))
		{
			return contents.stream().filter(x -> x.getIsEquipped()).collect(Collectors.toList());
		}
		else if(filter.equalsIgnoreCase("tête"))
		{
			return contents.stream().filter(x -> x.getType() == 0).collect(Collectors.toList());
		}
		else if(filter.equalsIgnoreCase("visage"))
		{
			return contents.stream().filter(x -> x.getType() == 1).collect(Collectors.toList());
		}
		else if(filter.equalsIgnoreCase("corps"))
		{
			return contents.stream().filter(x -> x.getType() == 2).collect(Collectors.toList());
		}
		return null;
	}
	
	public void updateContentElements()
	{
		contentRect.childs.clear();
		this.resetContainerLayout();
		for(GraphicObject obj : slots)
		{
			obj.localPosX = 0;
			obj.localPosY = 0;
			addToContainer(obj.setPosition(0, 0, elementSize,elementSize));
		}
		contentRect.setHeight(contentRect.getHeight() + spacing + 1);
	}
	
	public GraphicObject addToContainer(GraphicObject object)
	{
		 if(object == null)  return null;
		 
		 if(contentRect.getChilds().size() == 0) 
		 {
			 lastElementposX = object.localPosX += 1+lastElementposX + spacing;
			 lastElementposY = object.localPosY += 1+spacing;
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
			 object.localPosX = lastElementposX = spacing+1;
			 contentRect.setHeight(contentRect.getHeight() + elementSize + spacing);
		 }
		 		 
		 contentRect.addChild(object);
		 
		 return object;
	 }
	
	 @Override
	 public void updateScreen()
	 {
		 UIDropdown dropdown = (UIDropdown)getComponent(1);
		 if(this.lastSelectedElement != dropdown.getSelectedElement())
		 {
			 this.lastSelectedElement = dropdown.getSelectedElement();
			 this.updateScrollviewContents();
		 }
	 }
	 
	 @Override
	 public void onGuiClosed() 
	 {
		 
	 }

}

class CosmetiqueSlot extends UIButton
{
	private CosmeticObject attribuatedCosmetic;
	
	private final static ResourceLocation lockedTexture = new ResourceLocation("craftyourliferp","gui/cosmetics/locked.png");
	private final static ResourceLocation selectedTexture = new ResourceLocation("craftyourliferp","gui/cosmetics/selected.png");
	
	
	private UIImage lockedImage = new UIImage(lockedTexture);
	private UIImage selectedImage = new UIImage(selectedTexture);

	public CosmetiqueSlot(CosmeticObject co,Type type, String text, ResourceLocation resource,ResourceLocation hoverTexture, boolean displayText, CallBackObject callback) {
		super(type, text, resource,null, displayText, callback);
		lockedImage.color = new UIColor(255,215,0,255);
		selectedImage.color = new UIColor("#5ee609");
		this.attribuatedCosmetic = co;
		this.getRect().contourColor = new UIColor("#66a0c6");
	}
	
	public CosmetiqueSlot(CosmeticObject co,Type type , UIRect rect, String text, UIRect hoverRect, CallBackObject callback) {
		super(type, rect, text, hoverRect, callback);
		lockedImage.color = new UIColor(255,215,0,255);
		selectedImage.color = new UIColor("#5ee609");
		this.attribuatedCosmetic = co;
		this.getRect().contourColor = new UIColor("#66a0c6");
	}
	
	public CosmeticObject getCosmeticObj()
	{
		return attribuatedCosmetic;
	}
	
	public void setCosmeticObj(CosmeticObject co)
	{
		attribuatedCosmetic = co;
	}
	
	public boolean isLocked()
	{
		return attribuatedCosmetic.getIsLocked();
	}
	
	public boolean isEquipped()
	{
		return attribuatedCosmetic.getIsEquipped();
	}
	
	@Override
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		lockedImage.setPosition(x+(width-10)/2, y+(height-10)/2, 10, 10);
		selectedImage.setPosition(x+width-5, y+height-5, 5, 5);
		return this;
	}

	@Override
	public void draw(int x, int y)
	{
		super.draw(x, y);
		if(attribuatedCosmetic != null)
		{
			
			GL11.glPushMatrix();
			GL11.glColor4f(1f, 1f, 1f, 1f);
			
			GL11.glEnable(GL12.GL_RESCALE_NORMAL);
		    RenderHelper.enableGUIStandardItemLighting();
			GuiUtils.renderItemStackIntoGUI(attribuatedCosmetic.is, posX+(width-13)/2, posY+(height-16)/2, "", this.itemRender);
	        RenderHelper.disableStandardItemLighting();
	        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
	        GL11.glDisable(GL11.GL_BLEND);

			if(isEquipped())
			{
				selectedImage.draw(x, y);
			}
			
			GuiBase gui = (GuiBase) Minecraft.getMinecraft().currentScreen;
			if(gui.mouseOnDropdown(x,y))
			{
				GL11.glPopMatrix();
				return;
			}
			
			if(isHover(x,y))
			{
				GL11.glTranslatef(0, 0, 999);
				GuiUtils.drawRect(posX, posY, width, height, "#FFFFFF", 0.2f);
				if(isLocked())
				{
					lockedImage.draw(x, y);
				}
			}
			GL11.glPopMatrix();
		}

	}

}
	

