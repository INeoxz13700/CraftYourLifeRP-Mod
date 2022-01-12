package fr.craftyourliferp.ingame.gui;

import java.util.ArrayList;

import org.lwjgl.opengl.GL11;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.DriveableData;
import com.flansmod.common.driveables.DriveablePart;
import com.flansmod.common.driveables.DriveableType;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.EnumDriveablePart;
import com.flansmod.common.network.PacketVehicleInteraction;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderGameOverlayEvent;

@SideOnly(Side.CLIENT)
public class VehicleInteractionGui extends GuiBase
{
	private EntityDriveable driveable;
	
	private byte guiType = 0;
	
	private UIButton lockButton = new UIButton(Type.SQUARE, "lock", new ResourceLocation("cylrp", "textures/gui/Button_lock.png"),null, false,null);
	private UIButton unlockButton  = new UIButton(Type.SQUARE, "unlock", new ResourceLocation("cylrp", "textures/gui/Button_unlock.png"),null, false,null);
    
    public VehicleInteractionGui(int EntityID, byte type)
    {
       guiType = type;
       driveable = (EntityDriveable) Minecraft.getMinecraft().theWorld.getEntityByID(EntityID);
    }
    
    @Override
    public void initGui()
    {
    	super.initGui();
    }
    
    @Override
	public void initializeComponent() 
	{ 
    	setWindowPosition(20, (height - 150) / 2);
    	setWindowSize(100,150);
		addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/vehiclecontroller/container.png")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
		addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/vehiclecontroller/title.png")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), 20));

		
		if(guiType == 0)
		{
			addComponent(new UIButton(Type.SQUARE, new UIRect(new UIColor(36, 34, 34)), "Verouiller",new UIRect(new UIColor(30, 28, 28)) ,new CallBackObject()
			{
				@Override
				public void call()
				{
					if(!driveable.getDriveableData().varDoor)
						return;
					driveable.seats[0].OpenDoor(Minecraft.getMinecraft().thePlayer,false);
				}
				
			}).setPosition(this.getWindowPosX() + (getWindowWidth() - 65) / 2,this.getWindowPosY()+60,65,18));
			
			addComponent(new UIButton(Type.SQUARE, new UIRect(new UIColor(36, 34, 34)), "Déverouiller",new UIRect(new UIColor(30, 28, 28)),new CallBackObject()
			{
				@Override
				public void call()
				{
					if(driveable == null) return;
					
					if(driveable.getDriveableData().varDoor)
						return;
					driveable.seats[0].OpenDoor(Minecraft.getMinecraft().thePlayer,true);
				}
				
			}).setPosition(this.getWindowPosX() + (getWindowWidth() - 65) / 2,this.getWindowPosY()+90,65,18));
		}
		else if(guiType == 1)
		{
			addComponent(new UIButton(Type.SQUARE, new UIRect(new UIColor(36, 34, 34)), "Placer en Fourrière",new UIRect(new UIColor(30, 28, 28)),new CallBackObject()
			{
				@Override
				public void call()
				{
					FlansMod.getPacketHandler().sendToServer(PacketVehicleInteraction.impoundVehicle(driveable.getEntityId()));
					Minecraft.getMinecraft().displayGuiScreen(null);
				}
				
			}).setPosition(this.getWindowPosX() + (getWindowWidth() - 90) / 2,this.getWindowPosY()+(getWindowHeight())/2,90,18));
		}
		else
		{
			addComponent(new UIButton(Type.SQUARE, new UIRect(new UIColor(36, 34, 34)), "Sortir l'individu du véhicule",new UIRect(new UIColor(30, 28, 28)),new CallBackObject()
			{
				@Override
				public void call()
				{
					FlansMod.getPacketHandler().sendToServer(PacketVehicleInteraction.extractDriverFromVehicle(driveable.getEntityId()));
					Minecraft.getMinecraft().displayGuiScreen(null);
				}
				
			}).setPosition(this.getWindowPosX() + (getWindowWidth() - 90) / 2,this.getWindowPosY()+(getWindowHeight())/2,90,18));
		}
	}
    
    @Override
    public void drawScreen(int x, int y, float f)
    {
        super.drawScreen(x, y, f);
        
        if(driveable == null) return;
        
    	String name = driveable.driveableType == null ? driveable.driveableData.type != null ? driveable.driveableData.type : "" : driveable.driveableType;
    	
    	if(name == null) return;
    	
    	if(name.length() >= 20)
    		name = name.subSequence(0, 20).toString();
    	
    	GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    	GuiUtils.renderText(name,getWindowPosX() + (getWindowWidth() - (int) (mc.fontRenderer.getStringWidth(name) * 0.9f))/2,getWindowPosY() + 30,GuiUtils.gameColor,0.9f);
    }
    
    @Override
    public boolean doesGuiPauseGame()
    {
        return false;
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

}

