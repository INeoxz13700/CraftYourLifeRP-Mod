package fr.craftyourliferp.guicomponents;

import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.client.audio.SoundHandler;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.ResourceLocation;
import scala.reflect.internal.Trees.This;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;

@SideOnly(Side.CLIENT)
public class UICheckBox extends UIButton {
  
    public boolean checked = false;
    
    private static boolean nightMode;

    public UICheckBox(Type type, boolean defaultValue) {
    	super(type,"checkbox",getTexture(),null,false,null);
    	this.checked = defaultValue;
    }
   
    public UICheckBox(Type type) {
    	super(type,"checkbox",getTexture(),null,false,null);
    }

    @Override
    public void draw(int x, int y) 
    {
    	GL11.glColor4f(1f, 1f, 1f, 1f);
    	if(!checked) 
    	{
    		super.texture.texture = getTexture();
        }
        else {
    		super.texture.texture = getCheckedTexture();
        }
    
    	super.draw(x, y);
    	GL11.glColor4f(1f, 1f, 1f, 1f);
    }

    
    public GraphicObject setPosition(int x, int y, int width, int height)
    {
    	super.setPosition(x, y, width, height);
    	return this;
    }
    
    @Override
    public boolean onLeftClick(int x, int y)
    {
    	if(isClicked(x,y))
    	{
    		checked = !checked;
        	return true;
    	}
    	return false;
    }
    
    public void setNightMode(boolean state)
    {
    	this.nightMode = state;
    }
    
    
    public static ResourceLocation getTexture()
    {
    	if(nightMode)
    	{
        	return new ResourceLocation("craftyourliferp","gui/checkbox_false_nightmode.png");
    	}
    	return new ResourceLocation("craftyourliferp","gui/checkbox_false.png");
    }
    
    public static ResourceLocation getCheckedTexture()
    {
    	if(nightMode)
    	{
        	return new ResourceLocation("craftyourliferp","gui/checkbox_true_nightmode.png");
    	}
    	return new ResourceLocation("craftyourliferp","gui/checkbox_true.png");
    }

}

