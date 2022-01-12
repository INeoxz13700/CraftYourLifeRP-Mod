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
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class ContactButton extends UIButton {

	
	public ContactData attribuatedData;
	
	protected UIImage profileImg;
		
	public ContactButton(ContactData attribuatedData, Type type , ResourceLocation texture, ResourceLocation hoverTexture)
	{
		super(type,"",texture,hoverTexture,false,null);
		this.attribuatedData = attribuatedData;
		this.profileImg = new UIImage(new ResourceLocation("craftyourliferp","gui/phone/profil.png"));
		
		ContactButton btn = this;
		
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
	
	@Override
	public void draw(int x, int y)
	{
		super.draw(x, y);
		profileImg.draw(x, y);
		GuiUtils.renderText(attribuatedData.name, posX+30, (posY+(height-mc.fontRenderer.FONT_HEIGHT)/2)+1);
	}
	
	
	@Override
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		profileImg.setPosition(x+5, y+(height-15)/2, 15, 15);
		return this;
	}
	
}
