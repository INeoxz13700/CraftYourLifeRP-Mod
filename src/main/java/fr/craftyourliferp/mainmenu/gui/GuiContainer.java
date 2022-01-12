package fr.craftyourliferp.mainmenu.gui;

import javax.vecmath.Vector2d;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.util.ResourceLocation;

@SideOnly(Side.CLIENT)
public class GuiContainer {

    public int containerID;
    protected int posX1;
    protected int posY1;
    protected int posX2;
    protected int posY2;
    
    protected ResourceLocation background;

    public GuiContainer(int containerID, ResourceLocation background) {
        this.containerID = containerID;
        this.background = background;
    }

    public void initGui() {
    }

    public void actionPerformed(GuiButton guiButton) {
    }

    public void mouseClicked(int mouseX, int mouseY, int mouseButton) {
    }

    public void updateScreen() {
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawBackground();
    }

    public void drawBackground() {
    	GuiUtils.drawImage(posX1, posY1, background, posX2-posX1, posY2-posY1);
    }
    
    public void setPositionAndDraw(int posX1, int posY1, int posX2, int posY2) {
        this.posX1 = posX1;
        this.posY1 = posY1;
        this.posX2 = posX2;
        this.posY2 = posY2;
        this.drawBackground();
    }
    
    public int getWidth()
    {
    	return this.posX2 - posX1;
    }
}
