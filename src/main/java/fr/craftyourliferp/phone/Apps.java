package fr.craftyourliferp.phone;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.ingame.gui.GuiBase;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.mainmenu.gui.GuiCustomButton;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public abstract class Apps extends GuiBase  {
			
	public GuiPhone phone;
	
	public ResourceLocation ico;
	
	public String name;


	public Apps(String name, ResourceLocation ico, GuiPhone phone) {
		this.phone = phone;
		this.ico = ico;
		this.name = name;
	}

	
	public Apps getApp() {
		return this;
	}
	
	public ResourceLocation getIcoTexture()
	{
		return ico;
	}
	
	public abstract void back();	
	
	public abstract void openApps();
	
	public abstract void updateGuiState();

	

}
