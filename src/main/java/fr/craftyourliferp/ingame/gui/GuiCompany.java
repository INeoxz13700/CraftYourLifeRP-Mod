package fr.craftyourliferp.ingame.gui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.company.Company;
import fr.craftyourliferp.company.CompanyManager;
import fr.craftyourliferp.company.CompanyUser;
import fr.craftyourliferp.guicomponents.GraphicObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIDropdown;
import fr.craftyourliferp.guicomponents.UIImage;
import fr.craftyourliferp.guicomponents.UIProgressBar;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UIScrollbarHorizontal;
import fr.craftyourliferp.guicomponents.UIScrollbarVertical;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.guicomponents.UIButton.CallBackObject;
import fr.craftyourliferp.guicomponents.UIButton.Type;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;

public class GuiCompany extends GuiBase {

	public static Company company;
	
	public GuiCompany(Company company)
	{
		this.company = company;
	}
	
	@Override
	public void initGui()
	{
		setWindowSize(400,220);
		setWindowPosition((width-400) / 2, (height-220)/2);
		super.initGui();
	}
	

	@Override
	public void registerChilds()
	{
		this.addChild(new CompanyAchievements());
		this.addChild(new CompanyInformations());
		this.addChild(new CompanyMembers());
		this.addChild(new CompanyRepartition());
	}
	
	@Override
	public void initializeComponent()
	{
		addComponent(new UIImage(new ResourceLocation("craftyourliferp","gui/company/background.png")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
		UIText text = (UIText)addComponent(new UIText(company.getData().companyName,new UIColor("#FFFFFF"),1.5f));
		text.setX(getWindowPosX() + (getWindowWidth() - text.getTextWidth())/ 2);
		text.setY(getWindowPosY() + 15);
		
		GraphicObject progessBar = addComponent(new UIProgressBar(new UIRect(new UIColor("#1f1f1f")), new UIImage(new ResourceLocation("craftyourliferp","gui/company/bar_fill.png"))).setValue((float)company.getData().currentXp/(float)company.getData().levelupXp)
		.setPosition(getWindowPosX(), getWindowPosY()+getWindowHeight()-13, getWindowWidth(), 13));
		addComponent(new UIText("§6XP :",new UIColor("#FFFFFF"),1f).setPosition(getWindowPosX() + getWindowWidth() / 2, progessBar.getY2()-25,0,0));
		
		addComponent(new UIButton(Type.SQUARE,"close_btn",new ResourceLocation("craftyourliferp","gui/company/close.png"),null,false, new CallBackObject()
		{
			@Override
			public void call()
			{
				mc.currentScreen = null;
				mc.setIngameFocus();
			}
			
		}).setPosition(getWindowPosX()+getWindowWidth()-50, getWindowPosY()+1, 50, 20));

		super.initializeComponent();
	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);
		GuiScrollableView membersGui = (GuiScrollableView) getChild(2);
		GuiScrollableView achievementsGui = (GuiScrollableView) getChild(0);
		GuiScrollableView repartitionsGui = (GuiScrollableView) getChild(3);
		GuiBase statsGui = getChild(1);

		if(getComponent(2).isHover(x, y))
		{
			GL11.glColor4f(1f, 1f, 1f, 1f);
			((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§aXP: " + company.getData().currentXp + "/" + company.getData().levelupXp,"§aNiveau : " + company.getData().level}),x, y, Minecraft.getMinecraft().fontRenderer);	
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
		
		if(dialogBoxActive()) return;
		
		if(membersGui.mouseHoverGui(x, y))
		{
			for(int i = 0; i < membersGui.getContentRect().visibleChilds.size(); i++)
			{
				MembersObject obj = (MembersObject) membersGui.getContentRect().visibleChilds.get(i);
				
				if(obj.kickButton != null && obj.kickButton.isHover(x, y))
				{
					GL11.glColor4f(1f, 1f, 1f, 1f);
					((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§cKick :",obj.user.username}),x, y, Minecraft.getMinecraft().fontRenderer);	
					GL11.glColor4f(1f, 1f, 1f, 1f);
					return;
				}
				
				if(obj.exitButton != null && obj.exitButton.isHover(x, y))
				{
					GL11.glColor4f(1f, 1f, 1f, 1f);
					((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§cQuitter ou Disband l'entreprise"}),x, y, Minecraft.getMinecraft().fontRenderer);	
					GL11.glColor4f(1f, 1f, 1f, 1f);
					return;
				}
				
				if(obj.isHover(x, y))
				{
					GL11.glColor4f(1f, 1f, 1f, 1f);
					((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {obj.user.username,"Rang : §6" + obj.user.rank}),x, y, Minecraft.getMinecraft().fontRenderer);	
					GL11.glColor4f(1f, 1f, 1f, 1f);
					return;
				}
			}
		}
		else if(achievementsGui.mouseHoverGui(x, y))
		{
			GL11.glColor4f(1f, 1f, 1f, 1f);
			((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§aSuccès débloqués"}),x, y, Minecraft.getMinecraft().fontRenderer);	
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
		else if(repartitionsGui.mouseHoverGui(x, y))
		{
			GL11.glColor4f(1f, 1f, 1f, 1f);
			((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§bRépartitions"}),x, y, Minecraft.getMinecraft().fontRenderer);	
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
		else if(statsGui.mouseHoverGui(x, y))
		{
			GL11.glColor4f(1f, 1f, 1f, 1f);
			((GuiBase)Minecraft.getMinecraft().currentScreen).drawHoveringText(Arrays.asList(new String[] {"§6Statistiques"}),x, y, Minecraft.getMinecraft().fontRenderer);	
			GL11.glColor4f(1f, 1f, 1f, 1f);
		}
	}
	
	
	
}

class CompanyRepartition extends GuiScrollableView
{
	

	@Override
	public void initGui()
	{
		setWindowSize(170,75);
		setWindowPosition(parent.getWindowPosX()+parent.getWindowWidth()-180,parent.getWindowPosY()+42);
		super.initGui();
	}
	
	@Override
	public void initializeComponent()
	{
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		this.viewport = new UIRect(new UIColor("#1f1f1f"));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.selectedScrollBar = scrollBarVertical;
		
		addComponent(new UIRect(new UIColor("#1f1f1f"),new UIColor("#66a0c6")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
				
		setScrollViewPosition(getWindowPosX(), getWindowPosY()+1, getWindowWidth()-7, getWindowHeight()-3);
		
		updateScrollviewContents();
	}
	
	public void updateScrollviewContents()
	{
		updateContentElements();
	}
	
	public void updateContentElements()
	{
		contentRect.childs.clear();

		List<String> ranks = CompanyManager.getRanks();
		for(int i = 0; i < ranks.size(); i++)
		{
			RepartitionObject repartitionObj = new RepartitionObject(new UIRect(new UIColor(0,0,0,0)),1f,fr.craftyourliferp.guicomponents.UITextField.Type.NUMBER,ranks.get(i),CompanyManager.getRepartitionForRank(ranks.get(i), GuiCompany.company));
			repartitionObj.localPosX = 0;
			repartitionObj.localPosY = 0;
			addToContainer(repartitionObj.setPosition(0, 0, getWindowWidth(),20));
		}
		
		contentRect.setHeight(contentRect.getHeight() - 2);
	}
	
	 public GraphicObject addToContainer(GraphicObject object)
	 {
		 if(object == null)  return null;
		 
		 contentRect.addChild(object);
		 
		 object.localPosX = 0;
		 object.localPosY = contentRect.getHeight();
		 
		 if(object.localPosY + object.getHeight() > contentRect.getHeight())
		 {
			 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
		 }
		 
		 return object;
	 }

}

class RepartitionObject extends GraphicObject implements IGuiClickableElement, IGuiKeytypeElement
{
	private static UITextField selectedTextField;
	
	private UIRect objectBackground = new UIRect(new UIColor("#1f1f1f"));
	
	private UIText rankName;
	
	private UIRect rect = new UIRect(new UIColor("#282828"));
	
	private UITextField textField;
		
	private String previousText;
	
	
	public RepartitionObject(UIRect rect, float font_scale, fr.craftyourliferp.guicomponents.UITextField.Type number, String rankName, int defaultValue) {
		this.rankName = new UIText(rankName,new UIColor("#FFFFFF"),1f);
		textField = new UITextField(new UIRect(new UIColor(0,0,0,0)),1f,fr.craftyourliferp.guicomponents.UITextField.Type.NUMBER);
		textField.setText(defaultValue + "%");
		previousText = defaultValue + "%";
	}

	
	@Override
	public GraphicObject setPosition(int x ,int y, int width, int height)
	{
		objectBackground.setPosition(x, y, width, height);
		rankName.setPosition(x+5, (y+height/2)-2, width, height);
		rect.setPosition(x, y+height+1, width, 1);
		textField.setPosition(x+rankName.getTextWidth()+6, y+3, 50, 20);
		super.setPosition(x+20, y, width, height);
		return this;
	}
	
	@Override
	public void setY(int y)
	{
		setPosition(posX,y,width,height);
	}
	
	@Override
	public void setX(int x)
	{
		setPosition(x,posY,width,height);
	}
	
	@Override
	public void setWidth(int width)
	{
		setPosition(posX,posY,width,height);
	}
	
	@Override
	public void setHeight(int height)
	{
		setPosition(posX,posY,width,height);
	}
	
	@Override
	public void draw(int x, int y)
	{
		objectBackground.draw(x, y);
		rankName.draw(x, y);
		rect.draw(x, y);
		textField.draw(x, y);
		
		
		if(!textField.getText().equalsIgnoreCase(previousText) && !textField.isFocused()) 
		{		
			CompanyManager.setRepartitionForRank(rankName.getText(), Integer.parseInt(textField.getText().replace("%", "")), GuiCompany.company);
			textField.setText(CompanyManager.getRepartitionForRank(rankName.getText(), GuiCompany.company) + "%");
			previousText = textField.getText();
			textField.setFocused(false);
		}		
		super.draw(x, y);
	}

	@Override
	public boolean keyTyped(char character, int keycode) {
		return textField.keyTyped(character, keycode);
	}


	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}


	@Override
	public boolean onLeftClick(int x, int y) {
		
		if(selectedTextField != null) selectedTextField.setFocused(false);
		
		selectedTextField = textField;
		
		if(CompanyManager.getClientUser().rank.equalsIgnoreCase("Gerant")) return textField.onLeftClick(x, y);
		
		return false;
	}


	@Override
	public boolean onWheelClick(int x, int y) {
		
		return false;
	}
	 
}

class CompanyMembers extends GuiScrollableView
{
	
	final static ResourceLocation[] crownTextures = new ResourceLocation[4];
		
	public CompanyMembers()
	{
		super(new UIColor("#303030"), false);
		spacing = 1;
		for(int i = 0; i < crownTextures.length; i++)
		{
			crownTextures[i] = new ResourceLocation("craftyourliferp", "gui/company/c-" + i + ".png");
		}
	}
	
	@Override
	public void initGui()
	{
		setWindowSize(170,75);
		setWindowPosition(parent.getWindowPosX()+5, parent.getWindowPosY() + 42);
		super.initGui();
	}

	
	@Override
	public void initializeComponent() 
	{ 
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		this.viewport = new UIRect(new UIColor("#1f1f1f"));

		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#66a0c6")).setHoverColor(new UIColor(190,190,190));
		this.scrollBarVertical.setButtonColor(new UIColor(100,100,100));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#66a0c6")).setHoverColor(new UIColor(190,190,190));
		this.scrollBarHorizontal.setButtonColor(new UIColor(100,100,100));

		
		this.selectedScrollBar = scrollBarVertical;
		
		addComponent(new UIRect(new UIColor("#1f1f1f"),new UIColor("#66a0c6")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
				
		setScrollViewPosition(getWindowPosX(), getWindowPosY()+1, getWindowWidth()-7, getWindowHeight()-3);
		

		updateScrollviewContents();
	}
	
	public void updateScrollviewContents()
	{
		super.updateScrollviewContents();
		updateContentElements();
	}
	
	public void updateContentElements()
	{
		contentRect.childs.clear();
		for(CompanyUser user : GuiCompany.company.getUsers())
		{
			MembersObject obj = new MembersObject(this,user);
			obj.localPosX = 0;
			obj.localPosY = 0;
			addToContainer(obj.setPosition(0, 0,getWindowWidth(),20));
		}
		contentRect.setHeight(contentRect.getHeight() + spacing + 1);
	}
	
	 public GraphicObject addToContainer(GraphicObject object)
	 {
		 if(object == null)  return null;
		 
		 contentRect.addChild(object);
		 
		 object.localPosX = 0;
		 object.localPosY = contentRect.getHeight()+2;
		 
		 if(object.localPosY + object.getHeight() > contentRect.getHeight())
		 {
			 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
		 }
		 
		 return object;
	 }
	
	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);	
		for(int i = 0; i < contentRect.visibleChilds.size(); i++)
		{
			MembersObject obj = (MembersObject) contentRect.visibleChilds.get(i);

	

		}
	}
	
	@Override
	public void updateScreen()
	{
		if(getGuiTicks() % 5 == 0)
		{
			for(GraphicObject obj : contentRect.visibleChilds)
			{
				MembersObject memberObj = (MembersObject)obj;
				if(memberObj.crown != null) memberObj.crownIndex = memberObj.crownIndex + 1 > 3 ? 0 : memberObj.crownIndex+1;	
			}
		}
		super.updateScreen();
	}
	 
}

class MembersObject extends GraphicObject implements IGuiClickableElement
{
	 		 
	 public CompanyUser user;
	 
	 private final static ResourceLocation userBackground = new ResourceLocation("craftyourliferp","gui/company/userBackground.png");
	 
	 private UIImage userBackgroundObj;
	 
	 private UIText usernameText;
	 
	 public UIImage crown;
	 public int crownIndex = 0;
	 
	 public UIButton exitButton;
	 public UIButton kickButton;
	 


	 public MembersObject(GuiBase gui,CompanyUser user)
	 {
		this.user = user;
		CompanyUser clientUser = CompanyManager.getClientUser();
		boolean hasKickPermission = clientUser.rank.equalsIgnoreCase("Gérant") || clientUser.rank.equalsIgnoreCase("CoGerant");
		userBackgroundObj = new UIImage(userBackground);
		String displayUsername  = user.username.substring(0, Math.min(8, user.username.length()));
		displayUsername += displayUsername.equalsIgnoreCase(user.username) ? "" : "...";
		usernameText = new UIText(displayUsername, new UIColor("#FFFFFF"),1.0f);
		if(CompanyManager.isClientPlayer(user))
		{
			if(CompanyManager.PlayerHasRank("Gerant", user))
			{
				exitButton = new UIButton(Type.SQUARE,"exit_btn",new ResourceLocation("craftyourliferp","gui/company/exit.png"),null,false,new CallBackObject()
				{
					
					 @Override
					 public void call()
					 {
						 gui.displayDialogBox("Voulez-vous disband l'entreprise ?", 
						 new CallBackObject()
						 {
							 @Override
							 public void call()
							 {
								 mc.thePlayer.sendChatMessage("/entreprise disband");
								 mc.currentScreen = null;
								 mc.setIngameFocus();
							 }
						 },
						 new CallBackObject()
						 {
							 @Override
							 public void call()
							 {
								 gui.destroyDialogBox(0);
							 }
						 });
					 }
					 
				});
			}
			else
			{
				exitButton = new UIButton(Type.SQUARE,"exit_btn",new ResourceLocation("craftyourliferp","gui/company/exit.png"),null,false,new CallBackObject()
				{
					
					 @Override
					 public void call()
					 {
						 gui.displayDialogBox("Voulez-vous quitter l'entreprise ?", 
								 new CallBackObject()
								 {
									 @Override
									 public void call()
									 {
										 mc.thePlayer.sendChatMessage("/entreprise leave");
										 mc.currentScreen = null;
										 mc.setIngameFocus();
									 }
								 },
								 new CallBackObject()
								 {
									 @Override
									 public void call()
									 {
										 gui.destroyDialogBox(0);
									 }
								 });
					 }
					
				});
			}
			
			
		}
		else
		{
			if(hasKickPermission)
			{
				kickButton = new UIButton(Type.SQUARE,"kick_btn",new ResourceLocation("craftyourliferp","gui/company/kick.png"),null,false,new CallBackObject()
				{
					
					 @Override
					 public void call()
					 {
						 gui.displayDialogBox("Voulez-vous virer §6" + user.username + " §fl'entreprise ?", 
								 new CallBackObject()
								 {
									 @Override
									 public void call()
									 {
										 mc.thePlayer.sendChatMessage("/entreprise kick " + user.username);
										 GuiCompany.company.getUsers().remove(user);
										 GuiScrollableView membersGui = (GuiScrollableView) gui;
										 membersGui.updateScrollviewContents();
										 gui.destroyDialogBox(0);
									 }
								 },
								 new CallBackObject()
								 {
									 @Override
									 public void call()
									 {
										 gui.destroyDialogBox(0);
									 }
								 });
					 }
					 
				});
			}
		}
		
		if(CompanyManager.PlayerHasRank("Gerant", user) || CompanyManager.PlayerHasRank("CoGerant", user))
		{
			crown = new UIImage(CompanyMembers.crownTextures[crownIndex]);
		}
	}
	 
	@Override
	public void draw(int x, int y)
	{
		userBackgroundObj.draw(x, y);
		
		usernameText.draw(x, y);
		if(crown != null)
		{
			crown.texture = CompanyMembers.crownTextures[crownIndex];
			crown.draw(x, y);
		}
		if(exitButton != null)exitButton.draw(x, y);
		if(kickButton != null)kickButton.draw(x, y);
		super.draw(x, y);
	}
	 
	@Override
	public GraphicObject setPosition(int x, int y, int width, int height)
	{
		super.setPosition(x, y, width, height);
		userBackgroundObj.setPosition(x, y, width, height);
		
		usernameText.setPosition(x + 2, (y+height/2)-3, 0, 0);
		if(crown != null) crown.setPosition(x + 65, y+(height-10)/2, 15, 10);
		
		if(exitButton != null)exitButton.setPosition(crown == null ? x + 65 : x + 90, y+(height-8)/2, 10, 10);
		if(kickButton != null)kickButton.setPosition(crown == null ? x + 65 : x + 90, y+(height-8)/2, 10, 10);
		

		return this;
	}
	
	@Override
	public void setY(int y)
	{
		setPosition(posX,y,width,height);
	}
	
	@Override
	public void setX(int x)
	{
		setPosition(x,posY,width,height);
	}
	
	@Override
	public void setWidth(int width)
	{
		setPosition(posX,posY,width,height);
	}
	
	@Override
	public void setHeight(int height)
	{
		setPosition(posX,posY,width,height);
	}

	@Override
	public boolean onRightClick(int x, int y) {
		return false;
	}

	@Override
	public boolean onLeftClick(int x, int y) {
		if(exitButton != null)
		{
			if(exitButton.isClicked(x, y))
			{
				exitButton.callback.call();
				return true;
			}
		}
		
		if(kickButton != null)
		{
			if(kickButton.isClicked(x, y))
			{
				kickButton.callback.call();
				return true;
			}
		}
		return false;
	}

	@Override
	public boolean onWheelClick(int x, int y) {
		return false;
	}
	 
	 
}

class CompanyAchievements extends GuiScrollableView
{
	

	@Override
	public void initGui()
	{
		setWindowSize(170,75);
		setWindowPosition(parent.getWindowPosX()+5, parent.getWindowPosY() + parent.getWindowHeight() - 95);
		super.initGui();
	}
	
	@Override
	public void initializeComponent() 
	{ 
		this.contentRect = new UIRect(new UIColor(0,0,0,0));
		this.viewport = new UIRect(new UIColor("#1f1f1f"));
		
		this.scrollBarVertical = new UIScrollbarVertical(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.scrollBarHorizontal = new UIScrollbarHorizontal(new UIColor("#3A3B37"),new UIColor("#66a0c6"));
		this.selectedScrollBar = scrollBarVertical;
		
				
		setScrollViewPosition(getWindowPosX()+1, getWindowPosY()+1, getWindowWidth()-10, getWindowHeight()-5);
		

		updateScrollviewContents();
		
		contentRect.setHeight(contentRect.getHeight()-2);
	}
	
	public void updateScrollviewContents()
	{
		updateContentElements();
	}
	
	public void updateContentElements()
	{
		contentRect.childs.clear();

		for(String achievements : GuiCompany.company.getData().achievements)
		{
			AchievementsObject obj = new AchievementsObject(achievements);
			obj.localPosX = 0;
			obj.localPosY = 0;
			addToContainer(obj.setPosition(0, 0, getWindowWidth(),10));
		}
		contentRect.setHeight(contentRect.getHeight() + spacing + 1);
	}
	
	@Override
	public void drawScreen(int x, int y, float partialTicks)
	{
		super.drawScreen(x, y, partialTicks);		
	}
	
	public GraphicObject addToContainer(GraphicObject object)
	{
		 if(object == null)  return null;
		 
		 contentRect.addChild(object);
		 
		 object.localPosX = 1;
		 object.localPosY = contentRect.getHeight()+1;
		 
		 if(object.localPosY + object.getHeight() > contentRect.getHeight())
		 {
			 contentRect.setHeight(contentRect.getHeight() + object.getHeight() + spacing);
		 }
		 
		 return object;
	 }
	
	
	 
}

class AchievementsObject extends GraphicObject
{
	 
	 public UIText achievementsNameText;
	 
	 private UIRect rect;
	 		 
	 public AchievementsObject(String achievementName)
	 {
		 achievementsNameText = new UIText(achievementName, new UIColor("#FFFFFF"),0.9f);
		 rect = new UIRect(new UIColor("#282828"));
	 }
	 
	 @Override
	 public GraphicObject setPosition(int x, int y, int width, int height)
	 {
		 super.setPosition(x, y, width, height);
		 achievementsNameText.setPosition(x + (width - achievementsNameText.getTextWidth()) / 2, (y + height / 2)-3, width, height);
		 rect.setPosition(x, y+height+1, width, 1);
		 return this;
	 }
	 
	 @Override
	 public void draw(int x, int y)
	 {
		 achievementsNameText.draw(x, y);
		 rect.draw(x, y);	
		 super.draw(x, y);
	 }
	 
	@Override
	public void setY(int y)
	{
		setPosition(posX,y,width,height);
	}
		
	@Override
	public void setX(int x)
	{
		setPosition(x,posY,width,height);
	}
		
	@Override
	public void setWidth(int width)
	{
		setPosition(posX,posY,width,height);
	}
		
	@Override
	public void setHeight(int height)
	{
		setPosition(posX,posY,width,height);
	}
}


class CompanyInformations extends GuiBase
{
	

	
	@Override
	public void initGui()
	{
		setWindowSize(160,75);
		setWindowPosition(parent.getWindowPosX() + parent.getWindowWidth() - 170,parent.getWindowPosY() + parent.getWindowHeight()-95);
		super.initGui();
	}
	
	@Override
	public void initializeComponent() 
	{ 
		addComponent(new UIRect(new UIColor("#1f1f1f")).setPosition(getWindowPosX(), getWindowPosY(), getWindowWidth(), getWindowHeight()));
		addComponent(new UIText("Revenues : §6" + GuiCompany.company.getData().revenues + "§f $" ,new UIColor("#FFFFFF"),0.98f)).setPosition(getWindowPosX() + 6, getWindowPosY() + 5, getWindowWidth(), getWindowHeight());
		addComponent(new UIText("Niveau : §6" + GuiCompany.company.getData().level,new UIColor("#FFFFFF"),0.98f)).setPosition(getWindowPosX() + 6, getWindowPosY() + 35, getWindowWidth(), getWindowHeight());
		addComponent(new UIText("Ventes : §6" + GuiCompany.company.getData().sells,new UIColor("#FFFFFF"),0.98f)).setPosition(getWindowPosX() + 6, getWindowPosY() + 20, getWindowWidth(), getWindowHeight());
		addComponent(new UIText("Membres : §6" + GuiCompany.company.getData().userCount,new UIColor("#FFFFFF"),0.98f)).setPosition(getWindowPosX() + 6, getWindowPosY() + 45, getWindowWidth(), getWindowHeight());
		
		addComponent(new UIText("Succès : §6" + GuiCompany.company.getData().achievements.size(),new UIColor("#FFFFFF"),0.98f)).setPosition(getWindowPosX() + 6, getWindowPosY() + 65, getWindowWidth(), getWindowHeight());
	}
}


