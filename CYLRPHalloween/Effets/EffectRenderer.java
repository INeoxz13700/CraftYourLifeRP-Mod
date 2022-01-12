package fr.craftyourliferp.effects;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.utils.GuiUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.audio.PositionedSoundRecord;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;

@SideOnly(Side.CLIENT)
public class EffectRenderer {

    
    @SubscribeEvent(priority = EventPriority.HIGHEST, receiveCanceled=true)
	public void effectRender(RenderGameOverlayEvent.Post event)
	{
    	if(event.type == ElementType.ALL)
    	{
    		if(CraftYourLifeRPClient.currentEffect != null)
    		{

    			Effect effect = CraftYourLifeRPClient.currentEffect;
    			if(effect.getEffectTexture() != null)
    			{
    				GL11.glPushMatrix();
    				GL11.glColor4f(1f, 1f, 1f, 0.8f);
    				GL11.glTranslatef(0, 0, 800);
    				GuiUtils.drawImageWithTransparency(0, 0, effect.getEffectTexture(), event.resolution.getScaledWidth(),event.resolution.getScaledHeight());
    				GL11.glPopMatrix();
    			}
    			
    			if((System.currentTimeMillis() - effect.displayStartedTimestamp) / 1000 >= effect.displayTime)
    			{
    				CraftYourLifeRPClient.currentEffect = null;
    			}
    		}
    	}
	}
	
}
