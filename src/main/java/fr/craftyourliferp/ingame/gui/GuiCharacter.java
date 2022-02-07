package fr.craftyourliferp.ingame.gui;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.IdentityData;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIRect;
import fr.craftyourliferp.guicomponents.UITextField;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.mainmenu.gui.GuiCustomButton;
import fr.craftyourliferp.network.PacketAtm;
import fr.craftyourliferp.network.PacketUpdateIdentity;
import fr.craftyourliferp.utils.GuiUtils;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiListExtended;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiSlot;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCharacter extends GuiScreen {
	
	private float ticks;
	private float displayError;
	private boolean isDictatel;
	
	private String displayedMessage;
	private int messageIndex = 0;
	private int messageCounter = 0;
	private boolean countMessage = false;
	private boolean stopEventExecution = false;
	private List<String> dictatelMessage = new ArrayList<String>();	
	
	public UITextField lastname;
	public UITextField name;
	public UITextField birthdayDay;
	public UITextField birthdayMonth;
	public UITextField birthdayYear;

	public UITextField gender;
	
	private boolean isError;
	private String errorMessage;
	
	private EntityPlayer p;
	
	private String id;

	
	public GuiCharacter(EntityPlayer p) {
		this.p = p;
		Random random = new Random();
		id = "IDCRAFTYOURL<<<<<<<<<<<<<<<<<<<<";
		for(int i = 0; i < 18; i++)
		{
			id += ThreadLocalRandom.current().nextInt(1, 11);
		}
		id += "CYL<<<<<<<<<<<<";
		for(int i = 0; i < 11; i++)
		{
			id += ThreadLocalRandom.current().nextInt(0, 11);
		}
		ExtendedPlayer pData = ExtendedPlayer.get(p);
		isDictatel = true;
		this.countMessage = true;
		this.dictatelMessage.add("Bonjour il semblerait que vous êtes nouveau dans cette ville . Je me présente je suis le maire de cette belle ville.");
		this.dictatelMessage.add("Pour commencer , comme tout le monde tu vas procéder à la création de ta carte d'identité.");
		this.dictatelMessage.add("Vous êtes maintenant un citoyen de notre ville %gender% %name% et comme chaque citoyen vous devez à tous pris respecter les lois ne l'oubliez pas.");
		this.dictatelMessage.add("Pour votre arrivée en ville je vous offre ce dont vous avez besoin pour bien débuter , il est temps pour moi de vous laisser à bientôt !");
	}
	
	
	public void initGui() {
		this.buttonList.clear();
		this.lastname = new UITextField(new UIRect(new UIColor(0,0,0,100)), 1F, UITextField.Type.TEXT);
		this.lastname.setMaxStringLength(35);
		this.name = new UITextField(new UIRect(new UIColor(0,0,0,100)), 1F, UITextField.Type.TEXT);
		this.name.setMaxStringLength(35);

		this.birthdayDay = new UITextField(new UIRect(new UIColor(0,0,0,100)), 1F, UITextField.Type.NUMBER);
		this.birthdayDay.setMaxStringLength(2);
		
		this.birthdayMonth = new UITextField(new UIRect(new UIColor(0,0,0,100)), 1F, UITextField.Type.NUMBER);
		this.birthdayMonth.setMaxStringLength(2);
		
		this.birthdayYear = new UITextField(new UIRect(new UIColor(0,0,0,100)), 1F, UITextField.Type.NUMBER);
		this.birthdayYear.setMaxStringLength(4);
		
		this.gender = new UITextField(new UIRect(new UIColor(0,0,0,100)), 1F, UITextField.Type.TEXT);
		this.gender.setMaxStringLength(1);
		
		this.buttonList.add(new GuiButton(0,(this.width/2) - 50,this.height - 15, 80,15,"Confirmer"));
	}
	
	protected void actionPerformed(GuiButton button) {
		if(button.id == 0 && !this.isDictatel)
		{
			if(this.lastname.getText().isEmpty() || this.name.getText().isEmpty() || this.birthdayDay.getText().isEmpty() || this.birthdayMonth.getText().isEmpty() || this.birthdayYear.getText().isEmpty() || this.gender.getText().isEmpty())
			{
				this.isError = true;
				this.errorMessage = "Veuillez remplir tous les champs";
				return;
			}
			else if(!this.gender.getText().equalsIgnoreCase("M") && !this.gender.getText().equalsIgnoreCase("F"))
			{
				this.isError = true;
				this.errorMessage = "Veuillez entrer un genre valide Ex : M (Pour masculin)";
				return;
			}
			else if(Integer.parseInt(birthdayYear.getText()) > Year.now().getValue())
			{
				this.isError = true;
				this.errorMessage = "Votre date de naissance est fausse > " + Year.now().getValue();
				return;
			}
			else if(Integer.parseInt(birthdayMonth.getText()) < 1 || Integer.parseInt(birthdayMonth.getText()) > 12)
			{
				this.isError = true;
				this.errorMessage = "Votre mois de naissance doit être compris entre 1 et 12";
				return;	
			}
			else if(Integer.parseInt(birthdayDay.getText()) < 1 || Integer.parseInt(birthdayDay.getText()) > 31)
			{
				this.isError = true;
				this.errorMessage = "Votre jour de naissance doit être compris entre 1 et 31";
				return;	
			}
			else
			{
				String gender = this.gender.getText().equals("M") ? "Masculin" : "Feminin";
				CraftYourLifeRPMod.packetHandler.sendToServer(new PacketUpdateIdentity(this.lastname.getText(), this.name.getText(), this.birthdayDay.getText() + "/" + this.birthdayMonth.getText() + "/" + this.birthdayYear.getText(), gender));
				ExtendedPlayer pData = ExtendedPlayer.get(this.p);
				if(pData.identityData == null)
				{
					pData.identityData = new IdentityData();
				}
					
				pData.identityData.lastname = this.lastname.getText();
			    pData.identityData.name = this.name.getText();
			    pData.identityData.birthday = this.birthdayDay.getText() + "/" + this.birthdayMonth.getText() + "/" + this.birthdayYear.getText();
			    pData.identityData.gender = gender;
				this.dictatelMessage.set(2, "Vous êtes maintenant " + (pData.identityData.gender.equalsIgnoreCase("Masculin") ? "un citoyen" : "une citoyenne") + " de notre ville " + (pData.identityData.gender.equalsIgnoreCase("Masculin") ? "monsieur" : "madame") + " " + pData.identityData.name + " et comme chaque citoyen vous devez à tous pris respecter les lois ne l'oubliez pas."); 
				   		
        		this.countMessage = true;
				this.isDictatel = true;
        		this.messageCounter = 0;
        		ticks = 0;
        		this.messageIndex++;
			}
		}
	}
	
	@Override
    protected void keyTyped(char ch, int key)
    {
		if(!this.isDictatel)
		{
			this.name.keyTyped(ch, key);
			this.birthdayDay.keyTyped(ch, key);
			this.birthdayMonth.keyTyped(ch, key);
			this.birthdayYear.keyTyped(ch, key);
			this.lastname.keyTyped(ch, key);
			this.gender.keyTyped(ch, key);
		}
	}
	
    public void drawScreen(int x, int y, float fl)
    {
    	if(this.isDictatel)
    	{
    		GuiUtils.drawImage(20, this.height - 80, new ResourceLocation("craftyourliferp", "gui/character/president.png"), 42, 80);
    		this.drawDialogBox(70, this.height - 50, (this.width - 10) - 70, 40);
    	}
    	else
    	{
    		GuiUtils.drawRect(0, 0, width, height, "#000000", 0.6F);
			
        	if(this.isError)
        	{
        		GuiUtils.drawRect((width-300)/2, ((height-180)/2)+182, 300, 15, "#fc0303", 1f);
        		GuiUtils.renderCenteredText(this.errorMessage, width/2, (int) ((((height-180)/2)+185)));
        	}
			
        	
        	GuiUtils.renderCenteredText("Créer votre identité !", this.width / 2, 10);
        	
        	GuiUtils.drawImage((width-300)/2, (height-180)/2, new ResourceLocation("craftyourliferp", "gui/cardidentity/background.png"), 300, 180 );
	    	GuiUtils.drawImage((width-300)/2, (height-180)/2, new ResourceLocation("craftyourliferp", "gui/cardidentity/logo.png"), 300, 30);
	    	
	    	GuiUtils.drawImage(((width-300)/2) + 5, ((height-180)/2)+35, new ResourceLocation("craftyourliferp", "gui/cardidentity/container.png"), 225, 115);
        	
			GuiUtils.renderTextWithShadow("Nom : ", ((width-300)/2) + 10, ((height-180)/2)+60);
    		GuiUtils.renderTextWithShadow("Prenom : ", ((width-300)/2) + 10, ((height-180)/2)+80);
    		GuiUtils.renderTextWithShadow("Genre (M/F) : ", ((width-300)/2) + 10, ((height-180)/2)+100);
    		GuiUtils.renderTextWithShadow("Date de naissance : ", ((width-300)/2) + 10, ((height-180)/2)+120);
    		
    		this.name.setPosition(((width-300)/2) + 45, ((height-180)/2)+57, 100, 15);
        	this.name.draw(x, y);
        	
    		this.lastname.setPosition(((width-300)/2) + 62, ((height-180)/2)+77, 100, 15);
			this.lastname.draw(x, y);

			this.gender.setInputInset(5);
    		this.gender.setPosition(((width-300)/2) + 85, ((height-180)/2)+97, 20, 15);
			this.gender.draw(x, y);

			this.birthdayDay.setInputInset(3);
    		this.birthdayDay.setPosition(((width-300)/2) + 115, ((height-180)/2)+117, 22, 15);
    		this.birthdayDay.draw(x, y);
    		GuiUtils.renderTextWithShadow("/", ((width-300)/2) + 140, ((height-180)/2)+120);

			this.birthdayMonth.setInputInset(3);
    		this.birthdayMonth.setPosition(((width-300)/2) + 148, ((height-180)/2)+117, 22, 15);
    		this.birthdayMonth.draw(x, y);
    		GuiUtils.renderTextWithShadow("/", ((width-300)/2) + 174, ((height-180)/2)+120);
    		
			this.birthdayYear.setInputInset(3);
    		this.birthdayYear.setPosition(((width-300)/2) + 183, ((height-180)/2)+117, 34, 15);
    		this.birthdayYear.draw(x, y);
			
			for(Object obj : this.buttonList)
			{
				((GuiButton) obj).drawButton(mc, x, y);
			}
			

	    	mc.fontRenderer.drawSplitString(this.id, ((width-300)/2)+5, ((height-180)/2)+155, 295, 0);

    	}
    }
    
    protected void mouseClicked(int x, int y, int btn) 
    {
    	if(this.isDictatel)
    	{
	    	if (btn == 0)
	        {

		        if(!this.displayedMessage.equalsIgnoreCase(this.dictatelMessage.get(this.messageIndex)))
		        {
		        	this.countMessage = false;
		        	this.displayedMessage = this.dictatelMessage.get(this.messageIndex);
		        }
		        else
		        {
		        	
		    		        		
			    	if(this.messageIndex == 1)
			    	{
			    	   this.isDictatel = false;
			    	    return;
			    	}
			    	else if(this.messageIndex == 3)
			    	{
			    	    this.isDictatel = false;
			    	    this.mc.displayGuiScreen(new GuiSkin());
			    	    return;
			    	}
			    	    	
			        this.messageIndex++;
			        this.messageCounter = 0;
			        this.countMessage = true;	
		        }
	    		
	       }  	
    	}
    	else
    	{
    	   	if (btn == 0)
        	{
        		this.name.onLeftClick(x, y);
        		this.birthdayDay.onLeftClick(x, y);
        		this.birthdayMonth.onLeftClick(x, y);
        		this.birthdayYear.onLeftClick(x, y);
        		this.lastname.onLeftClick(x, y);
        		this.gender.onLeftClick(x, y);
        	}
    	}
    	super.mouseClicked(x, y, btn);
 
    }
    
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    public void updateScreen() {
    	
    	if(this.isError && this.displayError++ == 40)
    	{
    		this.displayError = 0;
    		this.isError = false;
    		this.errorMessage = "";
    	}
    	if(this.isDictatel && (ticks+= 0.001f) >= 0.001f && this.countMessage)
    	{
    		ticks = 0;
    		this.displayedMessage = this.dictatelMessage.get(Math.min(this.messageIndex, this.dictatelMessage.size() - 1)).substring(0, Math.min(this.dictatelMessage.get(this.messageIndex).length(), this.messageCounter));
    		this.messageCounter++;
    	}
    	else
    	{
    		this.name.updateCursorCounter();
    		this.birthdayDay.updateCursorCounter();
    		this.birthdayMonth.updateCursorCounter();
    		this.birthdayYear.updateCursorCounter();
    		this.lastname.updateCursorCounter();
    		this.gender.updateCursorCounter();
    	}
    }
    
    public void drawDialogBox(int x, int y, int width, int height) {
    	GuiUtils.drawImage(x, y-9, new ResourceLocation("craftyourliferp", "gui/character/anchor.png"), 50, 10);
    	GuiUtils.drawImage(x, y, new ResourceLocation("craftyourliferp", "gui/character/textbox.png"), width, height);
    	GuiUtils.renderText("Mr le maire", x + 5, (y-10) + 3,GuiUtils.gameColor,0.6f);
    	
    	if(this.displayedMessage != null)
    		mc.fontRenderer.drawSplitString(this.displayedMessage, x+5, y + 5, width-10, GuiUtils.gameColor);
    }

}
