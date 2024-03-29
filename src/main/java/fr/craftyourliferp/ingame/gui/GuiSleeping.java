package fr.craftyourliferp.ingame.gui;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketSleeping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiChat;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.network.NetHandlerPlayClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.network.play.client.C0BPacketEntityAction;

public class GuiSleeping extends GuiScreen {
 
	 /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    public void initGui()
    {
        super.initGui();
        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(mc.thePlayer);
        if(extendedPlayer.getShouldBeReanimate() || extendedPlayer.shouldBeInEthylicComa())
        {
            this.buttonList.add(new GuiButton(1, (this.width / 2) - 200, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
            this.buttonList.add(new GuiButton(2, this.width / 2, this.height - 40, "§cMort subite"));
        }
        else
        {
            this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height - 40, I18n.format("multiplayer.stopSleeping", new Object[0])));
        }
    }

    /**
     * Fired when a key is typed. This is the equivalent of KeyListener.keyTyped(KeyEvent e).
     */
    protected void keyTyped(char p_73869_1_, int p_73869_2_)
    {
        if (p_73869_2_ == 1)
        {
        	unSleep();
        }
        else if (p_73869_2_ != 28 && p_73869_2_ != 156)
        {
            super.keyTyped(p_73869_1_, p_73869_2_);
        }
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.id == 1)
        {
        	unSleep();
        }
        else if(p_146284_1_.id == 2)
        {
        	subitDeath();
        }
        else
        {
            super.actionPerformed(p_146284_1_);
        }
    }
    
    private void unSleep()
    {
        CraftYourLifeRPMod.packetHandler.sendToServer(PacketSleeping.unSleep());
    }
    
    private void subitDeath()
    {
        CraftYourLifeRPMod.packetHandler.sendToServer(PacketSleeping.subitDeath());
    }
    
    public boolean doesGuiPauseGame()
    {
        return false;
    }
    
    public void updateGui()
    {
    	this.buttonList.clear();
    	this.initGui();
    }

	
}
