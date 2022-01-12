package fr.craftyourliferp.ingame.gui;

import org.lwjgl.opengl.GL11;

import com.flansmod.client.FlansModResourceHandler;
import com.flansmod.common.driveables.DriveableType;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class VehicleGuiItems extends GraphicObject implements IGuiClickableElement {
	
	private Minecraft mc;
	
	private ResourceLocation selectedImage = new ResourceLocation("craftyourliferp", "gui/vehicles/selected_vehicle.png");
	private ResourceLocation unselectedImage = new ResourceLocation("craftyourliferp", "gui/vehicles/unselected_vehicle.png");
	
	public boolean isSelected = false;
	
	public boolean isUnlockSlot = false;
	
	
	public ItemStack AttribuedItemStack;
	
	private DriveableType driveableType;
	
	private UIButton recoveryButton;
	
	private CallBackObject unlockSlot;
	
	public int spinner = 0;
	
	public VehicleGuiItems(UIButton recoveryButton, ItemStack is) {
		this.AttribuedItemStack = is;
		this.driveableType = (DriveableType) DriveableType.getType(is);
		this.recoveryButton = recoveryButton;
		this.mc = Minecraft.getMinecraft();
	}
	
	public VehicleGuiItems(CallBackObject callbackObject) {
		this.isUnlockSlot = true;
		unlockSlot = callbackObject;
	}
	
	
	@Override
	public void draw(int x ,int y) {
		if(!isUnlockSlot)
		{
			if(isSelected) 
				GuiUtils.drawImage(posX, posY, selectedImage , width, height);
			else 
				GuiUtils.drawImage(posX, posY, unselectedImage , width, height);
			
			if(this.driveableType.model == null)
				GuiUtils.renderItemStackIntoGUI(AttribuedItemStack,posX + (width - 16) / 2, posY + 5, "", this.itemRender);
			else
				renderVehicle(this.driveableType, posX + (width / 2) - 8, posY + 5, spinner++);
			
			recoveryButton.setPosition(posX+(width-20)/2, posY+38,20,8);
			GL11.glPushMatrix();
			GL11.glTranslatef(0, 0, 20);
			recoveryButton.draw(x, y);
			GL11.glPopMatrix();
			
			String displayName = AttribuedItemStack.getDisplayName();
			if(displayName.length() >= 16) {
				displayName = displayName.subSequence(0, 16).toString();
			}
			GuiUtils.renderText("" + displayName, posX + (width - (int) (mc.fontRenderer.getStringWidth(displayName) * 0.5f)+1) / 2, posY + 30, GuiUtils.gameColor,0.5f);
		}
		else
		{
			if(this.isHover(x, y))
			{
				GuiUtils.renderColor(8882055);
			}
			GuiUtils.drawImage(posX, posY, new ResourceLocation("craftyourliferp","gui/vehicles/locked_slot.png") , width, height);
		}
		super.draw(x, y);
	}
	
	 private void renderVehicle(DriveableType type, int posX, int posY, int spinner) 
	 {
	    if(type != null)
		{

			GL11.glPushMatrix();
				
			GL11.glTranslatef(posX + 8, posY + 5, 1);
			GL11.glScalef(-25F * 0.5f / type.cameraDistance,25F * 0.5f / type.cameraDistance,25F * 0.5f / type.cameraDistance);
			GL11.glRotatef(180F, 0F, 0F, 1F);
			GL11.glRotatef(30F, 1F, 0F, 0F);
			GL11.glRotatef(spinner / 5F, 0F, 1F, 0F);
			mc.renderEngine.bindTexture(FlansModResourceHandler.getTexture(type));
			if(type.model != null )
			{
				type.model.render(type);
			}
			GL11.glPopMatrix();
		}
	 }


	@Override
	public boolean onRightClick(int x, int y) 
	{
		if(recoveryButton == null) return false;
		
		return recoveryButton.onRightClick(x, y);
	}


	@Override
	public boolean onLeftClick(int x, int y) {
		if(isUnlockSlot)
		{
			if(isHover(x, y))
			{
				unlockSlot.call();
				return true;
			}
		}
		else
		{
			return recoveryButton.onLeftClick(x, y);
		}
		return false;
	}


	@Override
	public boolean onWheelClick(int x, int y) 
	{
		if(recoveryButton == null) return false;
		
		return recoveryButton.onWheelClick(x, y);
	}
	
	
}
