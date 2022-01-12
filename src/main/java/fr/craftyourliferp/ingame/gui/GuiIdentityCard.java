package fr.craftyourliferp.ingame.gui;

import java.util.Random;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.IdentityData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.GuiUtils;
import io.netty.util.internal.ThreadLocalRandom;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiIdentityCard extends GuiScreen{
	
		private String id;
		
		private EntityPlayer p;
		
		private ExtendedPlayer ep;
		
		private IdentityData identityData;
		

		public GuiIdentityCard(EntityPlayer p) {
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
			this.p = p;
			this.ep = ExtendedPlayer.get(p);
		}
		
		public GuiIdentityCard(IdentityData identity) {
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
			this.identityData = identity;
		}
		
		public void initGui() {
			
		}
		
	    public void drawScreen(int x, int y, float fl)
	    {
	    	GuiUtils.drawImage(this.width/4, this.height/4, new ResourceLocation("craftyourliferp", "gui/cardidentity/background.png"), (this.width - this.width/4) - (this.width / 4), (this.height - this.height/4) - (this.height / 4));
	    	GuiUtils.drawImage(this.width/4, this.height/6, new ResourceLocation("craftyourliferp", "gui/cardidentity/logo.png"), (this.width - this.width/4) - (this.width / 4), ((this.height / 4) - this.height / 6) + 10);
	    	
	    	GuiUtils.drawImage((this.width/4) + 5, (this.height/4) + 12, new ResourceLocation("craftyourliferp", "gui/cardidentity/container.png"), 170, (this.height - this.height/3) - (this.height / 3));
	    	
	    	if(this.identityData != null)
	    	{
	    		GuiUtils.renderTextWithShadow("Nom : " + identityData.name, (this.width/4) + 8, (this.height/4) + 30);
	    		GuiUtils.renderTextWithShadow("Prenom : " + identityData.lastname , (this.width/4) + 8, (this.height/4) + 40);
	    		GuiUtils.renderTextWithShadow("Genre : " + (identityData.gender.equalsIgnoreCase("Masculin") ? "M" : "F"), (this.width/4) + 8, (this.height/4) + 50);
	    		GuiUtils.renderTextWithShadow("Date de naissance : " + identityData.birthday , (this.width/4) + 8, (this.height/4) + 60);
	    	}
	    	else
	    	{
	    		GuiUtils.renderTextWithShadow("Nom : " + ep.identityData.name, (this.width/4) + 8, (this.height/4) + 15);
	    		GuiUtils.renderTextWithShadow("Prenom : " + ep.identityData.lastname , (this.width/4) + 8, (this.height/4) + 25);
	    		GuiUtils.renderTextWithShadow("Genre : " + (ep.identityData.gender.equalsIgnoreCase("Masculin") ? "M" : "F"), (this.width/4) + 8, (this.height/4) + 35);
	    		GuiUtils.renderTextWithShadow("Date de naissance : " + ep.identityData.birthday , (this.width/4) + 8, (this.height/4) + 45);
	    	}
	    	
	    	mc.fontRenderer.drawSplitString(this.id, this.width/4 + 4, this.height/4 + (this.height - this.height/4) - (this.height / 4) - 27, ((this.width - this.width/4) - (this.width / 4)) - 5, 0);
	    }
	    
	    protected void mouseClicked(int x, int y, int btn) 
	    {
	    	
	    }
	    
	    public void updateScreen()
	    {

	    }
	    
	    public boolean doesGuiPauseGame()
	    {
	        return false;
	    }
	    
	   

}
