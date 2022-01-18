package fr.craftyourliferp.ingame.gui;

import java.util.ArrayList;
import java.util.List;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.achievement.GuiAchievements;
import net.minecraft.client.gui.achievement.GuiStats;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.gui.inventory.GuiInventory;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.GuiScreenEvent.ActionPerformedEvent;
import net.minecraftforge.common.MinecraftForge;
import tconstruct.client.tabs.AbstractTab;
import tconstruct.client.tabs.InventoryTabVanilla;
import tconstruct.client.tabs.TabRegistry;

public class GuiInventoryRP extends GuiContainer {


    /** x size of the inventory window in pixels. Defined as  float, passed as int */
    private float xSizeFloat;
    /** y size of the inventory window in pixels. Defined as  float, passed as int. */
    private float ySizeFloat;
    private static final String __OBFID = "CL_00000761";

    private List<AbstractTab> buttons = new ArrayList<AbstractTab>();
    
    private static final ResourceLocation containerTexture = new ResourceLocation("craftyourliferp","textures/gui/container/inventory.png");

	public GuiInventoryRP(EntityPlayer player)
	{
		super(player.inventoryContainer);
		ySize = ySize + 7;
	}
    
    /**
     * Called from the main game loop to update the screen.
     */
    @Override
    public void updateScreen()
    {
    	this.buttonList.clear();
        if (this.mc.playerController.isInCreativeMode())
        {
            this.mc.displayGuiScreen(new CustomGuiContainerCreative(this.mc.thePlayer));
        }
    }

    /**
     * Adds the buttons (and other controls) to the screen in question.
     */
    @Override
    public void initGui()
    {
    	this.buttons.clear();
    	this.buttonList.clear();
    	int xSize = 176;
    	int ySize = 166;
    	int guiLeft = (width - xSize) / 2;
    	int guiTop = (height - ySize) / 2;
    	TabRegistry.updateTabValues(guiLeft, guiTop, InventoryTabVanilla.class);
    	TabRegistry.addTabsToList(buttons);
    	if (this.mc.playerController.isInCreativeMode())
        {
            this.mc.displayGuiScreen(new CustomGuiContainerCreative(this.mc.thePlayer));
        }
        else
        {
            super.initGui();
        }
    }

    /**
     * Draw the foreground layer for the GuiContainer (everything in front of the items)
     */
    @Override
    protected void drawGuiContainerForegroundLayer(int p_146979_1_, int p_146979_2_)
    {
        this.fontRendererObj.drawString("Armement", 118, 8, 4210752);
    }
    
    @Override
    protected void drawGuiContainerBackgroundLayer(float p_146976_1_, int p_146976_2_, int p_146976_3_)
    {
    	for(GuiButton button : buttons)
    	{
    		button.drawButton(this.mc, button.xPosition, button.yPosition);
    	}
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.mc.getTextureManager().bindTexture(containerTexture);
        int k = this.guiLeft;
        int l = this.guiTop+2;
        this.drawTexturedModalRect(k, l, 0, 0, this.xSize, this.ySize);
        func_147046_a(k + 51, l + 75, 30, (float)(k + 51) - this.xSizeFloat, (float)(l + 75 - 50) - this.ySizeFloat, this.mc.thePlayer);

    }


    /**
     * Draws the screen and all the components in it.
     */
    @Override
    public void drawScreen(int mouseX, int mouseY, float p_73863_3_)
    {


        super.drawScreen(mouseX, mouseY, p_73863_3_);
        int k = this.guiLeft;
        int l = this.guiTop;
        RenderHelper.enableGUIStandardItemLighting();
        GL11.glPushMatrix();
        GL11.glTranslatef((float)k, (float)l, 0.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glEnable(GL12.GL_RESCALE_NORMAL);
        short short1 = 240;
        short short2 = 240;
        OpenGlHelper.setLightmapTextureCoords(OpenGlHelper.lightmapTexUnit, (float)short1 / 1.0F, (float)short2 / 1.0F);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        int k1;
        for (int i1 = 0; i1 < 2; ++i1)
        {
            Slot slot = (Slot)this.inventorySlots.inventorySlots.get(31+i1);

            GL11.glDisable(GL11.GL_LIGHTING);
            GL11.glDisable(GL11.GL_DEPTH_TEST);
            
            if(!slot.getHasStack())
            {
            	if(i1 == 0) GuiUtils.drawImage(slot.xDisplayPosition, slot.yDisplayPosition, slot.getBackgroundIconTexture(), 16*3+3, 16);
                else if(i1 == 1) GuiUtils.drawImage(slot.xDisplayPosition + (16*3+3) / 2 - 10, slot.yDisplayPosition, slot.getBackgroundIconTexture(), 20, 16);
            }
            
            GL11.glColorMask(true, true, true, true);
            GL11.glEnable(GL11.GL_LIGHTING);
            GL11.glEnable(GL11.GL_DEPTH_TEST);
            if (this.isMouseOverSlot(slot, mouseX, mouseY) && slot.func_111238_b())
            {
                ReflectionHelper.setPrivateValue(GuiContainer.class, this, slot, 6);
                GL11.glDisable(GL11.GL_LIGHTING);
                GL11.glDisable(GL11.GL_DEPTH_TEST);
                int j1 = slot.xDisplayPosition;
                k1 = slot.yDisplayPosition;
                GL11.glColorMask(true, true, true, false);

                if(mouseX-k1 > j1+16)
                this.drawGradientRect(j1, k1, j1 + 16*3+4, k1 + 16, -2130706433, -2130706433);

                GL11.glColorMask(true, true, true, true);
                GL11.glEnable(GL11.GL_LIGHTING);
                GL11.glEnable(GL11.GL_DEPTH_TEST);
                
            }

            
        }
        GL11.glDisable(GL11.GL_LIGHTING);
        GL11.glPopMatrix();
        


        this.xSizeFloat = (float)mouseX;
        this.ySizeFloat = (float)mouseY;
    }

    private boolean isMouseOverSlot(Slot slot, int x, int y)
    {
        return this.func_146978_c(slot.xDisplayPosition, slot.yDisplayPosition, 16*3+3, 16, x, y);
    }
    
    public static void func_147046_a(int p_147046_0_, int p_147046_1_, int p_147046_2_, float p_147046_3_, float p_147046_4_, EntityLivingBase p_147046_5_)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)p_147046_0_, (float)p_147046_1_, 50.0F);
        GL11.glScalef((float)(-p_147046_2_), (float)p_147046_2_, (float)p_147046_2_);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = p_147046_5_.renderYawOffset;
        float f3 = p_147046_5_.rotationYaw;
        float f4 = p_147046_5_.rotationPitch;
        float f5 = p_147046_5_.prevRotationYawHead;
        float f6 = p_147046_5_.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        p_147046_5_.renderYawOffset = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 20.0F;
        p_147046_5_.rotationYaw = (float)Math.atan((double)(p_147046_3_ / 40.0F)) * 40.0F;
        p_147046_5_.rotationPitch = -((float)Math.atan((double)(p_147046_4_ / 40.0F))) * 20.0F;
        p_147046_5_.rotationYawHead = p_147046_5_.rotationYaw;
        p_147046_5_.prevRotationYawHead = p_147046_5_.rotationYaw;
        GL11.glTranslatef(0.0F, p_147046_5_.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(p_147046_5_, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        p_147046_5_.renderYawOffset = f2;
        p_147046_5_.rotationYaw = f3;
        p_147046_5_.rotationPitch = f4;
        p_147046_5_.prevRotationYawHead = f5;
        p_147046_5_.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    

    @Override
    protected void actionPerformed(GuiButton p_146284_1_)
    {
        if (p_146284_1_.id == 0)
        {
            this.mc.displayGuiScreen(new GuiAchievements(this, this.mc.thePlayer.getStatFileWriter()));
        }

        if (p_146284_1_.id == 1)
        {
            this.mc.displayGuiScreen(new GuiStats(this, this.mc.thePlayer.getStatFileWriter()));
        }
    }
    
    @Override
    protected void mouseClicked(int p_73864_1_, int p_73864_2_, int p_73864_3_)
    {
    	super.mouseClicked(p_73864_1_, p_73864_2_, p_73864_3_);
        if (p_73864_3_ == 0)
        {
            for (int l = 0; l < this.buttons.size(); ++l)
            {
                GuiButton guibutton = (GuiButton)this.buttons.get(l);

                if (guibutton.mousePressed(this.mc, p_73864_1_, p_73864_2_))
                {
                    ActionPerformedEvent.Pre event = new ActionPerformedEvent.Pre(this, guibutton, this.buttonList);
                    if (MinecraftForge.EVENT_BUS.post(event))
                        break;
                    ReflectionHelper.setPrivateValue(GuiScreen.class, this, event.button, 8);
                    
                    event.button.func_146113_a(this.mc.getSoundHandler());
                    this.actionPerformed(event.button);
                    if (this.equals(this.mc.currentScreen))
                        MinecraftForge.EVENT_BUS.post(new ActionPerformedEvent.Post(this, event.button, this.buttonList));
                }
            }
        }
    }

}
