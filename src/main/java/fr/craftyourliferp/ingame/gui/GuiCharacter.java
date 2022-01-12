package fr.craftyourliferp.ingame.gui;

import java.time.Year;
import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.IdentityData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.mainmenu.gui.GuiCustomButton;
import fr.craftyourliferp.network.PacketAtm;
import fr.craftyourliferp.network.PacketUpdateIdentity;
import fr.craftyourliferp.utils.GuiUtils;
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
	
	public GuiTextField lastname;
	public GuiTextField name;
	public GuiTextField birthday;
	public GuiTextField gender;
	
	private boolean isError;
	private String errorMessage;
	
	private EntityPlayer p;
	
	public GuiCharacter(EntityPlayer p) {
		this.p = p;
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
		lastname = new GuiTextField(mc.fontRenderer, (this.width / 4) - 50, 60, 100, 20);
		name = new GuiTextField(mc.fontRenderer, this.width - (this.width / 4) - 50, 60, 100, 20);
		birthday = new GuiTextField(mc.fontRenderer, (this.width /4)  - 50, 120, 100, 20);
		gender = new GuiTextField(mc.fontRenderer, this.width - (this.width / 4) - 50, 120, 100, 20);
		this.buttonList.add(new GuiButton(0,(this.width/2) - 50,this.height - 50, 100,20,"Confirmer"));
	}
	
	protected void actionPerformed(GuiButton button) {
		if(button.id == 0 && !this.isDictatel)
		{
			if(this.lastname.getText().isEmpty() || this.name.getText().isEmpty() || this.birthday.getText().isEmpty() || this.gender.getText().isEmpty())
			{
				this.isError = true;
				this.errorMessage = "Veuillez remplir tous les champs";
				return;
			}
			else if(!this.gender.getText().equalsIgnoreCase("Masculin") && !this.gender.getText().equalsIgnoreCase("Feminin"))
			{
				this.isError = true;
				this.errorMessage = "Veuillez entrer un genre valide Ex : Masculin";
				return;
			}
			else if(!(this.birthday.getText().matches("\\d{2}/\\d{2}/\\d{4}"))) {
				this.isError = true;
				this.errorMessage = "Veuillez entrer une date de naissance valide Ex : 04/06/1999";
				return;
			}
			else if(Integer.parseInt(this.birthday.getText().split("/")[2]) > Year.now().getValue())
			{
				this.isError = true;
				this.errorMessage = "Votre date de naissance est fausse > 2019";
				return;
			}
			else if(Integer.parseInt(this.birthday.getText().split("/")[1]) < 1 || Integer.parseInt(this.birthday.getText().split("/")[1]) > 12)
			{
				this.isError = true;
				this.errorMessage = "Votre mois de naissance doit être compris entre 1 et 12";
				return;	
			}
			else if(Integer.parseInt(this.birthday.getText().split("/")[0]) < 1 || Integer.parseInt(this.birthday.getText().split("/")[1]) > 31)
			{
				this.isError = true;
				this.errorMessage = "Votre jour de naissance doit être compris entre 1 et 31";
				return;	
			}
			else
			{
				CraftYourLifeRPMod.packetHandler.sendToServer(new PacketUpdateIdentity(this.lastname.getText(), this.name.getText(), this.birthday.getText(), this.gender.getText()));
				ExtendedPlayer pData = ExtendedPlayer.get(this.p);
				if(pData.identityData == null)
				{
					pData.identityData = new IdentityData();
				}
					
				pData.identityData.lastname = this.lastname.getText();
			    pData.identityData.name = this.name.getText();
			    pData.identityData.birthday = this.birthday.getText();
			    pData.identityData.gender = this.gender.getText();
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
			this.name.textboxKeyTyped(ch, key);
			this.birthday.textboxKeyTyped(ch, key);
			this.lastname.textboxKeyTyped(ch, key);
			this.gender.textboxKeyTyped(ch, key);
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
        	GuiUtils.drawImage(0, 0, new ResourceLocation("craftyourliferp","gui/character/background.png"), width, height);
			
        	if(this.isError)
        	{
        		GuiUtils.drawImage(this.width / 6, this.height - this.height / 3, new ResourceLocation("craftyourliferp","gui/mainmenu/errorMessage.png"), (this.width - this.width/6) - this.width / 6, 20);
    			GL11.glPushMatrix();
    			float scale = 0.8f;
    			GL11.glScalef(scale, scale, scale);
        		GuiUtils.renderCenteredText(this.errorMessage, (int) ((((this.width / 4) + (this.width - this.width/4))/ 2) / scale), (int) (((this.height - this.height / 3) + 7) / scale));
        		GL11.glPopMatrix();
        	}
			
        	
        	GuiUtils.renderCenteredText("Bienvenue sur CraftYourLifeRP !", this.width / 2, 10);
        	this.name.drawTextBox();
			this.birthday.drawTextBox();
			this.lastname.drawTextBox();
			this.gender.drawTextBox();
			
			for(Object obj : this.buttonList)
			{
				((GuiButton) obj).drawButton(mc, x, y);
			}
			
			GuiUtils.renderCenteredText("Prenom", this.width / 4, 45);
			GuiUtils.renderCenteredText("Nom", this.width - (this.width / 4), 45);
			GuiUtils.renderCenteredText("Date de naissance (JJ/MM/AAAA)", this.width / 4, 105);
			GuiUtils.renderCenteredText("Genre (Masculin/Feminin) ", this.width - (this.width / 4), 105);
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
		    	    this.mc.currentScreen = null;
		    	    this.mc.setIngameFocus();
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
        		this.name.mouseClicked(x, y, btn);
        		this.birthday.mouseClicked(x, y, btn);
        		this.lastname.mouseClicked(x, y, btn);
        		this.gender.mouseClicked(x, y, btn);
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
    		this.birthday.updateCursorCounter();
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
