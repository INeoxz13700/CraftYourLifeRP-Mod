package fr.craftyourliferp.guicomponents;

import fr.craftyourliferp.ingame.gui.IGuiClickableElement;
import fr.craftyourliferp.utils.GuiUtils;

public class UIDialogBox extends GraphicObject implements IGuiClickableElement {

	private UIButton confirmBtn;
	
	private UIButton cancelBtn;
	
	private GraphicObject rect;
	
	private String text;
	
	public UIDialogBox(UIButton confirmBtn, UIButton cancelBtn, GraphicObject rect)
	{
		this.confirmBtn = confirmBtn;
		this.cancelBtn = cancelBtn;
		this.rect = rect;
	}
	

	@Override
	public void draw(int x, int y)
	{
		rect.draw(x, y);
		confirmBtn.draw(x, y);
		cancelBtn.draw(x, y);
		int i = 0;
		String[] texts = text.split("\\n");
		for(String str : texts)
		{
			GuiUtils.renderCenteredText(str, posX + width / 2, posY + 50 + 9 * i,0.8f);
			i++;
		}
		super.draw(x, y);
	}
	
	@Override
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		rect.setPosition(x, y, width, height);
		
		if(cancelBtn.callback == null)
		{
			confirmBtn.setPosition(x + (width - 50)/ 2, y + height-30,50,20);
		}
		else
		{
			confirmBtn.setPosition(x+(int)(width*0.2f), y + height - 30, 50, 20);
			cancelBtn.setPosition(x+(int)(width*0.6f), y + height - 30, 50, 20);
		}
		return this;
	}

	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}

	@Override
	public boolean onLeftClick(int x, int y) {
		
		if(confirmBtn.onLeftClick(x, y))
		{
			return true;
		}
		else if(cancelBtn.onLeftClick(x, y))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}
	
	public GraphicObject setText(String text)
	{
		this.text = text;
		return this;
	}

}
