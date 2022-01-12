package fr.craftyourliferp.phone;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.util.ResourceLocation;

public class AppsButton extends UIButton 
{
		
	
	public Apps attribuatedApp;

	public AppsButton(Apps attribuatedApp, Type type , String text, ResourceLocation texture, ResourceLocation hoverTexture, boolean displayText) {
		super(type, text, texture, hoverTexture, displayText,new CallBackObject() 
		{
			@Override
			public void call()
			{
				GuiPhone.getPhone().openApp(attribuatedApp);
			}
		});
		
		this.attribuatedApp = attribuatedApp;
	}
	
	@Override
	public void draw(int x, int y)
	{
		super.draw(x, y);
		GuiUtils.renderCenteredText(attribuatedApp.name, posX+1+width/2 , posY+22,0.7f);
	}
	
	

}
