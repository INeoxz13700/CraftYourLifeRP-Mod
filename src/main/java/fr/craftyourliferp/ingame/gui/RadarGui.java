package fr.craftyourliferp.ingame.gui;

import com.flansmod.common.FlansMod;
import com.flansmod.common.network.PacketOpenRadarGui;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityRadar;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiTextField;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;

@SideOnly(Side.CLIENT)
public class RadarGui extends GuiScreen{
	
	public ResourceLocation background = new ResourceLocation("flansmod", "gui/background.jpg");
	
	public GuiButton confirmButton;
	
	public GuiTextField penaltyValue;
	
	public GuiTextField speedLimit;
	
	public GuiButton activated; 
	
	public GuiTextField radius;
	
	private TileEntityRadar te;
	
	public RadarGui(TileEntityRadar te) {
		this.te = te;
	}

	public void initGui()
	{
		this.buttonList.clear();
		
		confirmButton = new GuiButton(0,this.width / 2 - 80/2,this.height - this.height/4,  80,20,"Confirmer");
		activated  = new GuiButton(1, this.width / 3, this.height / 3,80,20,!te.isOpen ? "Désactiver" : "Activer");
		speedLimit = new GuiTextField(this.fontRendererObj, (this.width - this.width / 3) - 30 , this.height / 3,30,20);
		penaltyValue = new GuiTextField(this.fontRendererObj, this.width / 3 , this.height / 2,80,20);		
		radius = new GuiTextField(this.fontRendererObj, (this.width - this.width / 3) - 30, this.height / 2, 30 , 20);
		
		speedLimit.setText("" + te.limitSpeed);
		penaltyValue.setText("" + te.penaltyValue);
		radius.setText("" + te.radius);
		
		this.buttonList.add(activated);
		this.buttonList.add(confirmButton);
		
		
	}
	
	@Override
	public void drawScreen(int x, int y, float ticks)
	{
		GuiUtils.drawImage(this.width / 4, this.height / 8, background, (this.width - this.width/4) - (this.width / 4), this.height * 6/8);
				
		for(Object obj : this.buttonList)
		{
			((GuiButton) obj).drawButton(mc, x, y);
		}
		
		GuiUtils.renderCenteredText("Etat :", ((this.width / 3) + 80 / 2), (this.height / 3) - 10);
		
		GuiUtils.renderCenteredText("Limite Vitesse (km/h) :", (this.width - this.width / 3) - 30 / 2, (this.height / 3) - 10);

		GuiUtils.renderCenteredText("Valeur Amende (€) :", this.width / 3 + 80 / 2, this.height / 2 - 10);

		GuiUtils.renderCenteredText("Rayon : ", (this.width - this.width / 3) - 30 / 2, this.height / 2 - 10);

		
		speedLimit.drawTextBox();
		penaltyValue.drawTextBox();
		radius.drawTextBox();
		
	}
	
	@Override
	public void updateScreen()
	{
		speedLimit.updateCursorCounter();
		penaltyValue.updateCursorCounter();
		radius.updateCursorCounter();
	}
	
	@Override
    protected void keyTyped(char ch, int key)
    {
	   super.keyTyped(ch, key);
	   if(speedLimit.isFocused())
	   {
		   if(((speedLimit.getText().length() > 2) || (ch < 48 || ch > 57)) && key != 14)
			   return;
		   
	       speedLimit.textboxKeyTyped(ch, key);
	   }
	   else if(penaltyValue.isFocused())
	   {   
		   if((ch < 48 || ch > 57) && key != 14)
			   return;
		   
		   
	       penaltyValue.textboxKeyTyped(ch, key);
	   }
	   else if(radius.isFocused())
	   {
		   if((ch < 48 || ch > 57) && key != 14)
			   return;
		   
	       radius.textboxKeyTyped(ch, key);
	   }

    }
	
	@Override
	protected void mouseClicked(int x, int y, int btn)
	{
		speedLimit.mouseClicked(x, y, btn);
		penaltyValue.mouseClicked(x, y, btn);
		radius.mouseClicked(x, y, btn);
		super.mouseClicked(x, y, btn);
	}
	
	@Override
    protected void actionPerformed(GuiButton btn) 
    {
    	if(btn.id == 1)
    	{
    		te.isOpen = !te.isOpen;
    		
    		
    		if(!te.isOpen)
    		{
    			activated.displayString = "Désactiver";
    		}
    		else
    		{
    			activated.displayString = "Activer";
    		}
    		
    	}
    	else if(btn.id == 0)
    	{
    		te.limitSpeed = Integer.parseInt(speedLimit.getText());
    		te.radius = Integer.parseInt(radius.getText());
    		te.penaltyValue = Integer.parseInt(penaltyValue.getText());
    		FlansMod.INSTANCE.packetHandler.sendToServer(new PacketOpenRadarGui(this.te));
    		mc.displayGuiScreen(null);
    	}
    }

	 
	@Override
	public boolean doesGuiPauseGame()
	{
	     return false;
	}

}
