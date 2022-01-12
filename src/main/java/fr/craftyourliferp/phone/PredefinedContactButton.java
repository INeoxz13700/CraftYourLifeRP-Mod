package fr.craftyourliferp.phone;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.ContactData;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class PredefinedContactButton extends ContactButton {
	
	private String commandExecuted;	
	
	public PredefinedContactButton(String attribuatedCommand, ContactData attribuatedData, Type type , ResourceLocation texture, ResourceLocation hoverTexture)
	{
		super(attribuatedData, type, texture, hoverTexture);
		
		PredefinedContactButton btn = this;
		commandExecuted = attribuatedCommand;
		
		callback = new CallBackObject()
		{
			@Override
			public void call()
			{
				GuiPhone phone = GuiPhone.getPhone();
				phone.displayContact(btn);
			}
		};
	}
	
	public void executeCommand()
	{
    	Minecraft.getMinecraft().thePlayer.sendChatMessage(commandExecuted);
	}
}
