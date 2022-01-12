package fr.craftyourliferp.utils;

import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.crash.CrashReport;
import net.minecraft.crash.CrashReportCategory;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ReportedException;
import net.minecraft.util.ResourceLocation;
import scala.Char;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.util.glu.Project;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

import static net.minecraftforge.client.IItemRenderer.ItemRenderType.INVENTORY;

import java.awt.*;
import java.util.ArrayList;

@SideOnly(Side.CLIENT)
public class GuiUtils {

    public static int gameColor = 0xF3F3F3;

    public static void drawScissorBox(int x, int y, int width, int height) {
        GL11.glEnable(GL11.GL_SCISSOR_TEST);
        GL11.glScissor(x, y, width, height);
    }

    public static void disableScissorBox() {
        GL11.glDisable(GL11.GL_SCISSOR_TEST);
    }

    public static void renderCenteredText(String text, int posX, int posY) {
        renderCenteredText(text, posX, posY, gameColor);
    }
    
    public static void setClippingRegion(int x, int y, int width, int height)
	{
    	Minecraft mc = Minecraft.getMinecraft();
    
    	if(mc.currentScreen == null) 
    	{
    		return;
    	}
    	
    	
	    float rx = (float)mc.displayWidth  / (float)mc.currentScreen.width;
	    float ry = (float)mc.displayHeight / (float)mc.currentScreen.height;

		GL11.glEnable(GL11.GL_SCISSOR_TEST);
		
		GL11.glScissor((int)(x * rx), (int)(mc.displayHeight - (y+height) * ry), (int)(width * rx), (int) (height * ry));
	}
    

	public static void clearClippingRegion()
	{
		GL11.glDisable(GL11.GL_SCISSOR_TEST);
	}
    
    public static void renderCenteredText(String text, int posX, int posY, float scale) {
    	GL11.glPushMatrix();
    	GL11.glScalef(scale, scale, scale);
    	renderCenteredText(text, (int) (posX / scale), (int) (posY / scale), gameColor);
    	GL11.glPopMatrix();
    }
    
    public static void renderCenteredText(String text, int posX, int posY, float scale, int color) {
    	GL11.glPushMatrix();
    	GL11.glScalef(scale, scale, scale);
    	renderCenteredText(text, (int) (posX / scale), (int) (posY / scale), color);
    	GL11.glPopMatrix();
    }

    public static void renderCenteredTextWithShadow(String text, int posX, int posY) {
        renderCenteredTextWithShadow(text, posX, posY, gameColor);
    }

    public static void renderCenteredText(String text, int posX, int posY, int par4) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawString(text, posX - mc.fontRenderer.getStringWidth(text) / 2, posY, par4);
    }

    public static void renderCenteredTextWithShadow(String text, int posX, int posY, int par4) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawStringWithShadow(text, posX - mc.fontRenderer.getStringWidth(text) / 2, posY, par4);
    }

    public static void renderText(String text, int posX, int posY) {
        renderText(text, posX, posY, gameColor);
    }

    public static void renderTextWithShadow(String text, int posX, int posY) {
        renderTextWithShadow(text, posX, posY, gameColor);
    }
    
    public static void renderTextWithShadow(String text, int posX, int posY, int color, float font_scale) {
    	GL11.glPushMatrix();
    	GL11.glScalef(font_scale, font_scale, font_scale);
        renderTextWithShadow(text, (int) (posX / font_scale), (int) (posY / font_scale), color);
        GL11.glPopMatrix();
    }

    public static void renderText(String text, int posX, int posY, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawString(text, posX, posY, color);
    }
    
    public static void renderText(String text, int posX, int posY, int color, float scale) {
        Minecraft mc = Minecraft.getMinecraft();
        GL11.glPushMatrix();
        GL11.glScalef(scale, scale, scale);
        mc.fontRenderer.drawString(text, (int) (posX / scale), (int) (posY / scale), color);
        GL11.glPopMatrix();
    }

    public static void renderTextWithShadow(String text, int posX, int posY, int color) {
        Minecraft mc = Minecraft.getMinecraft();
        mc.fontRenderer.drawStringWithShadow(text, posX, posY, color);
    }

    public static void renderItemStackIntoGUI(ItemStack itemstack, int posX, int posY, String text, RenderItem renderer) 
    {
    	if(itemstack == null) return;
    	
        RenderHelper.enableGUIStandardItemLighting();
        
        
        renderer.renderItemAndEffectIntoGUI(Minecraft.getMinecraft().fontRenderer, Minecraft.getMinecraft().getTextureManager(), itemstack, posX, posY);
        GL11.glDisable(2896);
        
        GL11.glPushMatrix();
        GL11.glTranslatef(0, 0, 999);
        GuiUtils.renderText(text, posX+14, posY+12,GuiUtils.gameColor,0.8f);
        GL11.glPopMatrix();
    }

    public static void renderColor(int par1) {
        Color color = Color.decode("" + par1);
        float red = color.getRed() / 255.0F;
        float green = color.getGreen() / 255.0F;
        float blue = color.getBlue() / 255.0F;
        GL11.glColor3f(red, green, blue);
    }

    public static void drawImage(double par0, double par1, ResourceLocation image, double par2Width, double par3Height) {
        Minecraft.getMinecraft().renderEngine.bindTexture(image);
        
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(par0, par1 + par3Height, 0.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(par0 + par2Width, par1 + par3Height, 0.0D, 1.0D, 1.0D);
        tess.addVertexWithUV(par0 + par2Width, par1, 0.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(par0, par1, 0.0D, 0.0D, 0.0D);
        tess.draw();
    }
    
    public static void drawImageWithTransparency(double par0, double par1, ResourceLocation image, double par2Width, double par3Height) {

    	Minecraft mc = Minecraft.getMinecraft();
    	
    	mc.renderEngine.bindTexture(image);
        
    	GL11.glEnable(GL11.GL_BLEND);
    	GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
    	GL11.glClearColor(1.0f, 1.0f, 1.0f, 1.0f);
        Tessellator tess = Tessellator.instance;
        tess.startDrawingQuads();
        tess.addVertexWithUV(par0, par1 + par3Height, 0.0D, 0.0D, 1.0D);
        tess.addVertexWithUV(par0 + par2Width, par1 + par3Height, 0.0D, 1.0D, 1.0D);
        tess.addVertexWithUV(par0 + par2Width, par1, 0.0D, 1.0D, 0.0D);
        tess.addVertexWithUV(par0, par1, 0.0D, 0.0D, 0.0D);
        tess.draw();
    	GL11.glDisable(GL11.GL_BLEND);

    }
    
    public static void drawRect(double par0, double par1, double par2, double par3, String par4Hex, float par5Alpha) {
        Color color = Color.decode(par4Hex);
        float red = color.getRed() / 255.0F;
        float green = color.getGreen() / 255.0F;
        float blue = color.getBlue() / 255.0F;
        drawRect(par0, par1, par0 + par2, par1 + par3, red, green, blue, par5Alpha);
    }
    
    

    public static void drawRect(double par0, double par1, double par2, double par3, float par4Red, float par5Green, float par6Blue, float par7Alpha) {
        Tessellator tessellator = Tessellator.instance;
        GL11.glPushMatrix();
        GL11.glEnable(3042);
        GL11.glBlendFunc(770, 771);
        GL11.glDisable(3553);
        GL11.glColor4f(par4Red, par5Green, par6Blue, par7Alpha);
        tessellator.startDrawingQuads();
        tessellator.addVertex(par0, par3, 0.0D);
        tessellator.addVertex(par2, par3, 0.0D);
        tessellator.addVertex(par2, par1, 0.0D);
        tessellator.addVertex(par0, par1, 0.0D);
        tessellator.draw();
        GL11.glEnable(3553);
        GL11.glDisable(3042);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
    }

    public static void drawRectWithShadow(int par0, int par1, int par2, int par3, String par4Hex, float par5Alpha) {
        drawRect(par0 - 1, par1 - 1, par2 + 2, par3 + 2, "0x000000", 0.2F);
        drawRect(par0, par1, par2, par3, par4Hex, par5Alpha);
    }

    public static void drawRectWithShadow(int par0, int par1, int par2, int par3, String par4Hex, float par5Alpha, float par6Alpha) {
        drawRect(par0 - 1, par1 - 1, par2 + 2, par3 + 2, "0x000000", par6Alpha);
        drawRect(par0, par1, par2, par3, par4Hex, par5Alpha);
    }
    
    public static void renderGradientRect(int x1, int y1, int x2, int y2, int color1, int color2)
    {
            float f = (float)(color1 >> 24 & 255) / 255.0F;
            float f1 = (float)(color1 >> 16 & 255) / 255.0F;
            float f2 = (float)(color1 >> 8 & 255) / 255.0F;
            float f3 = (float)(color1 & 255) / 255.0F;
            float f4 = (float)(color2 >> 24 & 255) / 255.0F;
            float f5 = (float)(color2 >> 16 & 255) / 255.0F;
            float f6 = (float)(color2 >> 8 & 255) / 255.0F;
            float f7 = (float)(color2 & 255) / 255.0F;
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glEnable(GL11.GL_BLEND);
            GL11.glDisable(GL11.GL_ALPHA_TEST);
            OpenGlHelper.glBlendFunc(770, 771, 1, 0);
            GL11.glShadeModel(GL11.GL_SMOOTH);
            Tessellator tessellator = Tessellator.instance;
            tessellator.startDrawingQuads();
            tessellator.setColorRGBA_F(f1, f2, f3, f);
            tessellator.addVertex((double)x2, (double)y1, 0);
            tessellator.addVertex((double)x1, (double)y1, 0);
            tessellator.setColorRGBA_F(f5, f6, f7, f4);
            tessellator.addVertex((double)x1, (double)y2, 0);
            tessellator.addVertex((double)x2, (double)y2, 0);
            tessellator.draw();
            GL11.glShadeModel(GL11.GL_FLAT);
            GL11.glDisable(GL11.GL_BLEND);
            GL11.glEnable(GL11.GL_ALPHA_TEST);
            GL11.glEnable(GL11.GL_TEXTURE_2D);
    }
    
    public static void renderSplitedText(String text, int posX , int posY, int maxWidth)
    {
    	ArrayList<String> display = SplitText(text,posX,maxWidth);
    	for(int i = 0; i < display.size(); i++)
    	{
    		GuiUtils.renderCenteredText(display.get(i), posX, posY + ((i + 1) * 5));
    	}
    }
    
    public static ArrayList<String> SplitText(String text, int posX, int maxWidth)
    {
    	ArrayList<String> Split = new ArrayList<String>();
    	int pos = posX;
    	int line = 0;
    	int i;
    	int index = 0;
    	for(i = 0; i < text.length(); i++)
    	{
    		if((pos += Minecraft.getMinecraft().fontRenderer.getCharWidth(text.charAt(i))) >= maxWidth)
    		{
    			Split.add(text.substring(index, i));
    			index = i;
    		}
    	}
    	
    	if(Split.size() < 1)
    		Split.add(text);
    	
    	return Split;
    }
    
    public static void drawPlayer(int x, int y, int scale, float mousex, float mousey, EntityLivingBase entity)
    {
        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, 50.0F);
        GL11.glScalef((float)(-scale), (float)scale, (float)scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        float f2 = entity.renderYawOffset;
        float f3 = entity.rotationYaw;
        float f4 = entity.rotationPitch;
        float f5 = entity.prevRotationYawHead;
        float f6 = entity.rotationYawHead;
        GL11.glRotatef(135.0F, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glRotatef(-((float)Math.atan((double)(mousey / 40.0F))) * 20.0F, 1.0F, 0.0F, 0.0F);
        entity.renderYawOffset = (float)Math.atan((double)(mousex / 40.0F)) * 20.0F;
        entity.rotationYaw = (float)Math.atan((double)(mousex / 40.0F)) * 40.0F;
        entity.rotationPitch = -((float)Math.atan((double)(mousey / 40.0F))) * 20.0F;
        entity.rotationYawHead = entity.rotationYaw;
        entity.prevRotationYawHead = entity.rotationYaw;
        GL11.glTranslatef(0.0F, entity.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntityWithPosYaw(entity, 0.0D, 0.0D, 0.0D, 0.0F, 1.0F);
        entity.renderYawOffset = f2;
        entity.rotationYaw = f3;
        entity.rotationPitch = f4;
        entity.prevRotationYawHead = f5;
        entity.rotationYawHead = f6;
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }
    
    public static void drawPlayerStatic(int x, int y, int scale, EntityLivingBase entity, int rotation)
    {

        GL11.glEnable(GL11.GL_COLOR_MATERIAL);
        GL11.glPushMatrix();
        GL11.glTranslatef((float)x, (float)y, 50.0F);
        GL11.glScalef((float)(-scale), (float)scale, (float)scale);
        GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
        GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
        RenderHelper.enableStandardItemLighting();
        GL11.glRotatef(-135.0F, 0.0F, 1.0F, 0.0F);
        GL11.glTranslatef(0.0F, entity.yOffset, 0.0F);
        RenderManager.instance.playerViewY = 180.0F;
        RenderManager.instance.renderEntitySimple(entity, 80.0F);
        GL11.glPopMatrix();
        RenderHelper.disableStandardItemLighting();
        GL11.glDisable(GL12.GL_RESCALE_NORMAL);
        OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
        GL11.glDisable(GL11.GL_TEXTURE_2D);
        OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
    }

    
    public static int getStringWidth(Minecraft mc, String string, float scale)
    {
    	return (int)(mc.fontRenderer.getStringWidth(string) * scale)+4;
    }
    
    public static void StartRotation(int drawX, int drawY, int drawWidth, int drawHeight , int rotateAngle)
    {
    	GL11.glPushMatrix();
    	GL11.glTranslatef(drawX + drawWidth / 2, drawY + drawHeight / 2, 0);
    	GL11.glRotatef(rotateAngle, 0, 0, 1);
    	GL11.glTranslatef(-(drawX + drawWidth / 2), -(drawY + drawHeight / 2), 0);
    }
    
    public static void StopRotation()
    {
    	GL11.glPopMatrix();
    }

    
    
}
