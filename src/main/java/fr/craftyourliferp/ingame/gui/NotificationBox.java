package fr.craftyourliferp.ingame.gui;


import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIProgressBar;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIText;
import net.minecraft.util.ResourceLocation;

public class NotificationBox extends GraphicObject implements IGuiClickableElement {

	public enum NotificationType
	{
		ALERT,
		INFORMATIONS,
		ERROR;
	};
	
	private NotificationType type;
	
	public int displayInSeconds;
	
	public int leftSeconds;
	
	private NotificationBox() { }
	
	private UIText tittle;
	private UIText message;
	private UIImage ico;
	private UIProgressBar bar;
	private UIRect container;
	private UIButton closeBtn;


	public static NotificationBox newInstance(String tittle, String message, NotificationType type,CallBackObject closeCallback, int displayInSeconds, boolean displayCloseBtn)
	{
		NotificationBox box = new NotificationBox();
		box.type = type;
		box.tittle = new UIText(tittle,new UIColor("#FFFFFF"),1f);
		box.message = new UIText(message, new UIColor("#FFFFFF"),0.9f);
		box.container = new UIRect(box.getColorFromType());
		box.bar = new UIProgressBar(null,new UIRect(new UIColor("#FFFFFF")));
		box.bar.setValue(1f);
		box.ico = box.getIcoFromType();
		box.displayInSeconds = displayInSeconds;
		box.leftSeconds = displayInSeconds;
		if(displayCloseBtn)box.closeBtn = new UIButton("x",new UIColor("#FFFFFF"),new UIColor("#FFFFFF"),1f, closeCallback);
		return box;
	}
	
	private UIColor getColorFromType()
	{
		if(type == NotificationType.ALERT)
		{
			return new UIColor("#f5d847");
		}
		else if(type == NotificationType.ERROR)
		{
			return new UIColor("#fa5c4d");
		}
		else
		{
			return new UIColor("#64e32d");
		}
		
	}
	
	public UIImage getIcoFromType()
	{
		if(type == NotificationType.ALERT)
		{
			return new UIImage(new ResourceLocation("craftyourliferp","gui/notificationbox/alertIco.png"));
		}
		else if(type == NotificationType.ERROR)
		{
			return new UIImage(new ResourceLocation("craftyourliferp","gui/notificationbox/errorIco.png"));
		}
		else
		{
			return new UIImage(new ResourceLocation("craftyourliferp","gui/notificationbox/successIco.png"));
		}
	}
	
	public NotificationBox setPosition(int x, int y, int width, int height)
	{
		container.setPosition(x, y, width, height);
		ico.setPosition(x+5, y+(height-15)/2+5, 15, 15);
		tittle.setPosition(x+4, y+2, 0, 0);
		message.setPosition(ico.getX2()+4, ico.getY()+(ico.getHeight()-mc.fontRenderer.FONT_HEIGHT)/2, 0, 0);
		bar.setPosition(x, y+height-2, width, 2);
		if(closeBtn != null)closeBtn.setPosition(x+width-7, y+1);
		super.setPosition(x, y, width, height);
		
		return this;
	}
	
	public void draw(int x, int y)
	{
		container.draw(x, y);
		ico.draw(x, y);
		tittle.draw(x, y);
		message.draw(x, y);
		bar.draw(x, y);
		if(closeBtn != null)closeBtn.draw(x, y);
		updateProgressBar();
		super.draw(x, y);
	}
	
	public void updateProgressBar()
	{
		bar.setValue((float)leftSeconds/(float)displayInSeconds);
	}
	
	public String getSound()
	{
		return "craftyourliferp:notification";
	}

	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}

	@Override
	public boolean onLeftClick(int x, int y) {
		if(closeBtn.isHover(x, y))
		{
			closeBtn.callback.call();
			return true;
		}
		return false;
	}

	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}
	
}
