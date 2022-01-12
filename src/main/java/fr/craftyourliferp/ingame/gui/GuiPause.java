package fr.craftyourliferp.ingame.gui;

import cpw.mods.fml.client.FMLClientHandler;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.mainmenu.gui.GuiCheckUpdate;
import fr.craftyourliferp.network.PacketCosmetic;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.resources.I18n;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ClientCommandHandler;

public class GuiPause extends GuiScreen {
	
    private int field_146445_a;
    private int field_146444_f;

	@Override
    public void initGui()
    {
        this.field_146445_a = 0;
        this.buttonList.clear();
        byte b0 = -16;
        boolean flag = true;
        this.buttonList.add(new GuiButton(1, this.width / 2 - 100, this.height / 4 + 120 + b0, I18n.format("menu.returnToMenu", new Object[0])));

        if (!this.mc.isIntegratedServerRunning())
        {
            ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("menu.disconnect", new Object[0]);
        }

        this.buttonList.add(new GuiButton(4, this.width / 2 - 100, this.height / 4 + 24 + b0, I18n.format("menu.returnToGame", new Object[0])));
        this.buttonList.add(new GuiButton(0, this.width / 2 - 100, this.height / 4 + 96 + b0, 200, 20, I18n.format("menu.options", new Object[0])));
        GuiButton guibutton;
        this.buttonList.add(guibutton = new GuiButton(7, this.width / 2 - 100, this.height / 4 + 72 + b0, 200, 20, I18n.format("menu.shareToLan", new Object[0])));
        this.buttonList.add(new GuiButton(5, this.width / 2 - 100, this.height / 4 + 48 + b0, 98, 20, I18n.format("gui.achievements", new Object[0])));
        this.buttonList.add(new GuiButton(6, this.width / 2 + 2, this.height / 4 + 48 + b0, 98, 20, I18n.format("gui.stats", new Object[0])));
        guibutton.enabled = this.mc.isSingleplayer() && !this.mc.getIntegratedServer().getPublic();
        
        //if(!Minecraft.getMinecraft().isIntegratedServerRunning())
        //{
            this.buttonList.add(new GuiButton(8, this.width / 2 + 2, this.height / 2 + 66 , 98, 20, "§6Cosmétiques"));
            this.buttonList.add(new GuiButton(9, this.width / 2 - 100, this.height / 2 + 66 , 98, 20, "§6Entreprise"));
            this.buttonList.add(new GuiButton(10, this.width / 2 - 100, this.height / 2 + 87 , 200, 20, "§bGestion avatar"));
        //}

    }
    
    @Override
    protected void actionPerformed(GuiButton btn)
    {
    	
    	switch (btn.id)
        {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
            	btn.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                GuiCheckUpdate.checkSuccess = false;
                this.mc.loadWorld((WorldClient)null);
                this.mc.displayGuiScreen(new GuiCheckUpdate());
            case 2:
            case 3:
            default:
                break;
            case 4:
                this.mc.displayGuiScreen((GuiScreen)null);
                this.mc.setIngameFocus();
                break;
            case 5:
                if (this.mc.thePlayer != null)
                this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
                break;
            case 6:
                if (this.mc.thePlayer != null)
                this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
                break;
            case 7:
                this.mc.displayGuiScreen(new GuiShareToLan(this));
                break;
            case 8:
            	this.mc.displayGuiScreen(null);
            	CraftYourLifeRPMod.packetHandler.sendToServer(new PacketCosmetic((byte)3));
            	break;
            case 9:
            	this.mc.displayGuiScreen(null);
            	Minecraft.getMinecraft().thePlayer.sendChatMessage("/entreprise");
            	break;
            case 10:
            	this.mc.displayGuiScreen(null);
            	mc.displayGuiScreen(new GuiSkin());
            	break;
        }
    }
    
    public void updateScreen()
    {
        super.updateScreen();
        ++this.field_146444_f;
    }

    /**
     * Draws the screen and all the components in it.
     */
    public void drawScreen(int p_73863_1_, int p_73863_2_, float p_73863_3_)
    {
        
        this.drawDefaultBackground();
        this.drawCenteredString(this.fontRendererObj, I18n.format("menu.game", new Object[0]), this.width / 2, 40, 16777215);
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        
    }
	
}
