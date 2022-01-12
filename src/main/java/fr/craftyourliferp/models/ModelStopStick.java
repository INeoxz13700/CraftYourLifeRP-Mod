package fr.craftyourliferp.models;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.entities.EntityStopStick;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelStopStick {

	public IModelCustom model;
	
	public ResourceLocation texture;	
	
	public ModelStopStick()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","models/herse.obj"));
		texture = new ResourceLocation("craftyourliferp","models/herse.png");
	}
	
	public void render()
	{
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		model.renderAll();
	}
	
	public void renderModel(EntityStopStick entity, double x, double y, double z)
	{
		GL11.glPushMatrix(); 
		
		
	    GL11.glTranslated(((double)x) + 0.5D,((double)y), ((double)z + 0.5D));

		

        GL11.glScalef(2f, 2f,2f);  
        
        GL11.glRotated(entity.rotationYaw-90, 0, 1, 0);
        

        
        render();
        GL11.glPopMatrix();
	}
	
}
