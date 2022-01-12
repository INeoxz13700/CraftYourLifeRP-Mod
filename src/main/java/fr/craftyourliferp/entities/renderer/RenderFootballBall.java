package fr.craftyourliferp.entities.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.craftyourliferp.entities.EntityFootballBall;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityXPOrb;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class RenderFootballBall extends Render {

	private IModelCustom model;
	
	private ResourceLocation texture;
	
	public RenderFootballBall()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","models/football_ballon.obj"));
		texture = new ResourceLocation("craftyourliferp","models/football_ballonTexture.png");
		this.shadowSize = 0.3f;
	}



	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,float p_76986_9_) 
	{
        doRender((EntityFootballBall)p_76986_1_,p_76986_2_,p_76986_4_,p_76986_6_,p_76986_8_,p_76986_9_);
	}
	
	public void doRender(EntityFootballBall p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,float p_76986_9_) {
		GL11.glPushMatrix();
        GL11.glScalef(0.7f, 0.7f, 0.7f);  
        GL11.glTranslated(((double)p_76986_2_) / 0.7D,((double)p_76986_4_+0.3D)/ 0.7D, ((double)p_76986_6_)/ 0.7D);
        
        GL11.glPushMatrix();
     
        
        GL11.glRotated(p_76986_1_.rotationYaw-90, 0, 1, 0);
        GL11.glRotated(p_76986_1_.rotationPitch, 0, 0, -1);
        
        GL11.glColor4f(1f, 1f, 1f, 1f);
        Minecraft.getMinecraft().getTextureManager().bindTexture(texture);

        
        model.renderAll();
        GL11.glPopMatrix();
        GL11.glPopMatrix();
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return null;
	}


}
