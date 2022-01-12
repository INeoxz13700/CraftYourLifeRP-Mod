package fr.craftyourliferp.ingame.gui;

import cpw.mods.fml.relauncher.Side;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.mainmenu.gui.GuiCustomButton;
import fr.craftyourliferp.network.PacketAtm;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.ISound;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundManager;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.sound.SoundEvent;

@cpw.mods.fml.relauncher.SideOnly(Side.CLIENT)
public class GuiAtm extends GuiScreen {
	
	ResourceLocation background = new ResourceLocation("craftyourliferp","gui/atm/Background.png");
	ResourceLocation[] keys = new ResourceLocation[11];
	ResourceLocation logo = new ResourceLocation("craftyourliferp","gui/atm/ATM.png");
	ResourceLocation card_insertion = new ResourceLocation("craftyourliferp","gui/atm/Carte.png");
	ResourceLocation container = new ResourceLocation("craftyourliferp","gui/atm/ChiffreBox.png");
	ResourceLocation dispose = new ResourceLocation("craftyourliferp","gui/atm/Deposer.png");
	ResourceLocation take = new ResourceLocation("craftyourliferp","gui/atm/Retirer.png");
	ResourceLocation display = new ResourceLocation("craftyourliferp","gui/atm/EcranBox.png");
	ResourceLocation screen = new ResourceLocation("craftyourliferp","gui/atm/Montant.png");
	
	ResourceLocation atm_sound = new ResourceLocation("craftyourliferp", "atm_press");

	
	int guiPosX1;
	int guiPosX2;
	int guiPosY1;
	int guiPosY2;
	
	private String value = "";
	
	public GuiAtm() {
		for(int i = 0; i < keys.length; i++)
		{
			if(i != 10)
				keys[i] = new ResourceLocation("craftyourliferp","gui/atm/" + i + ".png");
			else
				keys[i] = new ResourceLocation("craftyourliferp","gui/atm/Effacer.png");
		}

	}
	
	public void initGui() {
		guiPosX1 = this.width/4;
		guiPosX2 = this.width - (this.width/4);
		guiPosY1 = 0;
		guiPosY2 = this.height;
		
		this.buttonList.clear();
		
		int i = 0;
		int keystartx = guiPosX1 + 15;
		int keystarty = ((this.height/2) - (guiPosY1 + 5)) + 10;
		for(i = 0; i < keys.length; i++)
		{
			if(i > 0 && i <= 3)
				this.buttonList.add(new GuiCustomButton(i,keystartx,keystarty + (24*(i)),25,20,keys[i],false));
			else if(i > 3 && i <= 6)
				this.buttonList.add(new GuiCustomButton(i,keystartx + 30,keystarty + (24*(i-3)),25,20,keys[i],false));
			else if(i > 6 && i <= 9)
				this.buttonList.add(new GuiCustomButton(i,keystartx + 60,keystarty + (24*(i-6)),25,20,keys[i],false));
			else if(i == 0)
			{
				this.buttonList.add(new GuiCustomButton(i,keystartx + 90,keystarty + (24*(i+3)),25,20,keys[i],false));
			}
			else if(i == 10)
				this.buttonList.add(new GuiCustomButton(i,keystartx + 90,keystarty + (24 * (i-8)),25,20,keys[i],false));
			
		}
		
		//button take
		this.buttonList.add(new GuiCustomButton(i++,guiPosX2-55,guiPosY1 + 35,40,15,take,false));
		
		//button dispose
		this.buttonList.add(new GuiCustomButton(i++,guiPosX2-55,guiPosY1 + 55,40,15,dispose,false));
	
		
	}
	
    protected void actionPerformed(GuiButton button) {
    	
    	if(button.id >= 0 && button.id <= 9 && value.length() != 9)
    	{
    		value += button.id;
    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(atm_sound, 2.5F));

    	}
    	else if(button.id == 10 && !value.equalsIgnoreCase(""))
    	{
    		value = value.subSequence(0, value.length() - 1).toString();
    		Minecraft.getMinecraft().getSoundHandler().playSound(PositionedSoundRecord.func_147674_a(atm_sound, 2.5F));
    	}
    	else if(button.id == 11)
    	{
    		if(value.isEmpty())
    			return;
    		CraftYourLifeRPMod.packetHandler.sendToServer(PacketAtm.packetMoneyInterraction((byte)1, Integer.parseInt(value)));
    	}
    	else if(button.id == 12)
    	{
    		if(value.isEmpty())
    			return;
    		CraftYourLifeRPMod.packetHandler.sendToServer(PacketAtm.packetMoneyInterraction((byte)0, Integer.parseInt(value)));
    	}
    	
    }

	
    public void drawScreen(int x, int y, float fl)
    {  	
	   //background
       GuiUtils.drawImage(guiPosX1, guiPosY1, background, guiPosX2 - guiPosX1, this.height);
   
       //logo
       GuiUtils.drawImage(guiPosX2 - 60, guiPosY1 + 10, logo, 50, 20);
   	   
   	   //card_insertion
   	   GuiUtils.drawImage(guiPosX2-62, guiPosY2 - 60, card_insertion, 55,20);
   	   
   	   //display
   	   
   	   int displayX1 = guiPosX1 + 5;
   	   int displayX2 = (guiPosX1 + 5) + (guiPosX2 - 75) - (guiPosX1 + 5);
   	   int displayY1 = guiPosY1 + 5;
   	   int displayY2 = (guiPosY1 + 5) + (this.height/2) - (guiPosY1 + 5); 
   	   
   	   GuiUtils.drawImage(displayX1, displayY1, display, (guiPosX2 - 75) - (guiPosX1 + 5), (this.height/2) - (guiPosY1 + 5));
   	   
   	   GuiUtils.drawImage(((displayX1 + displayX2) / 2)-50, ((displayY1 + displayY2) / 2)-12, screen, 100, 25);
   	   GuiUtils.renderText(value, ((displayX1 + displayX2) / 2)-45, ((displayY1 + displayY2) / 2)-4,0);
   	   
   	   //keys
   	   GuiUtils.drawImage(guiPosX1 + 5, ((this.height/2) - (guiPosY1 + 5)) + 15, container, (guiPosX2 - 75) - (guiPosX1 + 5), (guiPosY2-10) - (((this.height/2) - (guiPosY1 + 5)) + 15));
   	   for(Object obj : buttonList)
   	   {
   		   GuiButton button = (GuiButton) obj;
   		   button.drawButton(mc,x, y);
   	   }
   	   
   	   
    }
    

    public boolean doesGuiPauseGame()
    {
        return false;
    }
	
}
