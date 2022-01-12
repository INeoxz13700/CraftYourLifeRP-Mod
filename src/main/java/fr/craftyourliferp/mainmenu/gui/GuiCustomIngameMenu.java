package fr.craftyourliferp.mainmenu.gui;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.sql.Date;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import com.ibm.icu.text.SimpleDateFormat;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.GuiOptions;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.GuiShareToLan;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.multiplayer.WorldClient;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiCustomIngameMenu extends GuiScreen
{
    private static final ResourceLocation crpmenu = new ResourceLocation("craftyourlifemenu", "textures/gui/crpmenu.png");
    private int field_146445_a;
    private int field_146444_f;
    private static final String __OBFID = "CL_00000703";

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    
    public void initGui()
    {
        this.field_146445_a = 0;
        this.buttonList.clear();
        byte var1 = -16;
        boolean var2 = true;
        int var3 = this.height / 3 + 62;
        int var4 = this.height / 3 + 38;
        int var5 = this.height / 3 + 14;
        int var6 = this.height / 3 - 10;
        int var7 = this.height / 3 - 34;
        int var8 = this.height / 3 - 58;
        int var9 = this.height / 3 - 82;
        int var10 = this.height / 3 - 106;
        this.buttonList.add(new GuiButton(1, 2 + 15, var3 + 45 + 12, 108, 20, I18n.format("D\u00e9connexion", new Object[0])));
        if (!this.mc.isIntegratedServerRunning())
        {
            ((GuiButton)this.buttonList.get(0)).displayString = I18n.format("D\u00e9connexion", new Object[0]);
        }

        this.buttonList.add(new GuiButton(4, 2 + 15, var10 + 45 + 12, 108, 20, I18n.format("\u00a72Reprendre le jeu", new Object[0])));
        this.buttonList.add(new GuiButton(8, 2 + 20, var7 + 45 + 12, 98, 20, I18n.format("Site internet", new Object[0])));
        this.buttonList.add(new GuiButton(0, 2 + 20, var9 + 45 + 12, 98, 20, I18n.format("R\u00e9glages...", new Object[0])));
        this.buttonList.add(new GuiButton(8, 2 + 20, var8 + 45 + 12, 98, 20, I18n.format("Teamspeak 3", new Object[0])));
        //this.buttonList.add(new GuiButton(2, 2 + 20, var6 + 45 + 12, 98, 20, I18n.format("Reglement", new Object[0])));
    }

    protected void actionPerformed(GuiButton p_146284_1_)
    {
        switch (p_146284_1_.id)
        {
            case 0:
                this.mc.displayGuiScreen(new GuiOptions(this, this.mc.gameSettings));
                break;
            case 1:
                p_146284_1_.enabled = false;
                this.mc.theWorld.sendQuittingDisconnectingPacket();
                this.mc.loadWorld((WorldClient)null);
                this.mc.displayGuiScreen(new GuiMainMenu());
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
                URI uri = URI.create("https://discord.gg/B7YdUm4");
                try { Desktop.getDesktop().browse(uri);
                } catch (IOException e) { 
                    // TODO Auto-generated catch block e.printStackTrace(); } }
                }
                break;
            case 9:
            	URI url = URI.create("https://craftyourliferp.fr");
                try { Desktop.getDesktop().browse(url);
                } catch (IOException e) { 
                    // TODO Auto-generated catch block e.printStackTrace(); } }
                }
                break;
                }
        }  
    




    
    

public void drawTextureWithOptionalSize(int x, int y, int u, int v, int width, int height, int uSize, int vSize)
{
	float scaledX = (float)1/uSize;
	float scaledY = (float)1/vSize;
	Tessellator tessellator = Tessellator.instance;
	tessellator.startDrawingQuads();
	tessellator.addVertexWithUV((double)(x + 0), (double)(y + height), (double)this.zLevel, (double)((float)(u + 0) * scaledX), (double)((float)(v + height) * scaledY));
	tessellator.addVertexWithUV((double)(x + width), (double)(y + height), (double)this.zLevel, (double)((float)(u + width) * scaledX), (double)((float)(v + height) * scaledY));
	tessellator.addVertexWithUV((double)(x + width), (double)(y + 0), (double)this.zLevel, (double)((float)(u + width) * scaledX), (double)((float)(v + 0) * scaledY));
	tessellator.addVertexWithUV((double)(x + 0), (double)(y + 0), (double)this.zLevel, (double)((float)(u + 0) * scaledX), (double)((float)(v + 0) * scaledY));
	tessellator.draw();
}


    /**
     * Called from the main game loop to update the screen.
     */
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
        this.drawCenteredString(this.fontRendererObj, "\u00a7e- Bienvenue \u00a74" + this.mc.getSession().getUsername() + "\u00a7e -", 70, 5, 16777215);
        
        super.drawScreen(p_73863_1_, p_73863_2_, p_73863_3_);
        this.drawCenteredString(this.fontRendererObj, "\u00a7eCraftYourLifeRP \u00a7f2018 \u00a9", 70, this.height - 14, -1);
    }
}