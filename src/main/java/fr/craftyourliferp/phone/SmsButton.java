package fr.craftyourliferp.phone;

import java.util.ArrayList;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.SmsData;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketUpdateSmsData;
import fr.craftyourliferp.utils.FontUtils;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class SmsButton extends UIButton {

	
	public String name;
	
	public String number;
	
	public List<SmsData> attribuatedData;
	
	public int notReadedSmsCount = 0;
	
	private UIImage profileImg;
	
	private UIImage notificationImg;

		
	public SmsButton(List datas,String name, String number, Type type , ResourceLocation texture, ResourceLocation hoverTexture)
	{
		super(type,"",texture,hoverTexture,false,null);
		
		profileImg = new UIImage(new ResourceLocation("craftyourliferp","gui/phone/profil.png"));

		notificationImg = new UIImage(new ResourceLocation("craftyourliferp","gui/phone/notification_circle.png"));
		notificationImg.setColor(new UIColor(255,255,255,190));
				
		attribuatedData = datas;
		
		GuiPhone phone = GuiPhone.getPhone();
		
		this.name = name;
		this.number = number;
		
		SmsButton instance = this;
		
		callback = new CallBackObject()
		{
			@Override
			public void call()
			{
				phone.displayConversation(instance);
			}
		};
	}
	
	@Override
	public void draw(int x, int y)
	{
		super.draw(x, y);
		int charCount = FontUtils.getNumberCharacterInWidth(width-40, 1f);

		String displayedName = "";
		if(name.length() == 0)
		{
			displayedName = number.length() > charCount ? number.substring(0, charCount) + "..." : number;
		}
		else
		{
			displayedName = name.length() > charCount ? name.substring(0, charCount) + "..." : name;
		}
		
		GuiUtils.renderText(displayedName, posX + 25, (posY + (height - mc.fontRenderer.FONT_HEIGHT) / 2) - 2);
		profileImg.draw(x, y);
		if(notReadedSmsCount > 0)
		{
			notificationImg.draw(x, y);
			GuiUtils.renderCenteredText(notReadedSmsCount + "", notificationImg.getX()+8, notificationImg.getY()+5,0.7f);
		}
		
		SmsData data = attribuatedData.get(attribuatedData.size()-1);
		
		charCount = FontUtils.getNumberCharacterInWidth(width-30, 0.8f);
		
		String message = data.message.length() > charCount ? data.message.substring(0, charCount) + "..." : data.message;


		GuiUtils.renderText(message, posX + 25, posY + 12, GuiUtils.gameColor,0.8f);
	}
	
	@Override
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		profileImg.setPosition(x + 2, y + (height - 15) / 2, 15, 15);
		notificationImg.setPosition(x + width - 20, y + (height - 15) / 2, 15, 15);
		return this;
	}
	
	public SmsData getData(String number)
	{
		for(SmsData sData : attribuatedData)
		{
			if(sData.senderNumber.equalsIgnoreCase(number))
				return sData;
		}
		return null;
	}
	
	public String getLastMessage()
	{
		if(attribuatedData.size() > 0)
			return attribuatedData.get(attribuatedData.size()-1).message;
		return null;
	}
	
	public static boolean Contains(SmsButton sb, List<GraphicObject> l) {
		for(Object obj : l)
		{
			SmsButton sbt = (SmsButton) obj;
			if(sbt.number.equalsIgnoreCase(sb.number)) return true;
		}
		return false;
	}
	
	public void updateCountSmsNotReaded(List<SmsData> sms)
	{
		int count = 0;
		for(SmsData s : sms)
		{
			if(!s.readed && !s.senderNumber.equalsIgnoreCase(ExtendedPlayer.get(Minecraft.getMinecraft().thePlayer).phoneData.getNumber()))
			{
				count++;
			}
		}
		this.notReadedSmsCount = count;
	}
	
	public void setReaded()
	{
		GuiPhone phone = GuiPhone.getPhone();
		for(SmsData sms : this.attribuatedData)
		{
			if(!sms.readed)
			{
				sms.readed = true;
			}
		}
		phone.playerData.phoneData.setSmsReaded(number);
		updateCountSmsNotReaded(attribuatedData);
	}
	
}
