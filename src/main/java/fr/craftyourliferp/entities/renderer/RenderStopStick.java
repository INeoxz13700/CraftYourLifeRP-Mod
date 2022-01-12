package fr.craftyourliferp.entities.renderer;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.craftyourliferp.entities.EntityStopStick;
import fr.craftyourliferp.models.ModelStopStick;
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

public class RenderStopStick extends Render {

	private ModelStopStick model;
	
	public RenderStopStick()
	{
		model = new ModelStopStick();
		this.shadowSize = 0.3f;
	}

	@Override
	public void doRender(Entity p_76986_1_, double p_76986_2_, double p_76986_4_, double p_76986_6_, float p_76986_8_,float p_76986_9_) 
	{
        doRender((EntityStopStick)p_76986_1_,p_76986_2_,p_76986_4_,p_76986_6_,p_76986_8_,p_76986_9_);
	}
	
	public void doRender(EntityStopStick entity, double x, double y, double z, float p_76986_8_,float p_76986_9_) {
		
        GL11.glColor4f(1f, 1f, 1f, 1f);
	
			if(entity.rotationYaw == -90)
			{
				double position = z+0.3D;
				for(int i = 0; i < entity.getSize(); i++)
				{
			        model.renderModel(entity,x-0.3D,y,position);
			        position+=2D;
				}
			}
			else if(entity.rotationYaw == 0)
			{
				double position = x-1.3D;
				for(int i = 0; i < entity.getSize(); i++)
				{
			        model.renderModel(entity,position,y,z-0.6F);
			        position-=2D;
				}
			}
			else if(entity.rotationYaw == 90)
			{
				double position = z-1.2;
				for(int i = 0; i < entity.getSize(); i++)
				{
			        model.renderModel(entity,x-0.5D,y,position);
			        position-=2D;
				}
			}
			else
			{
				double position = x+0.25D;
				for(int i = 0; i < entity.getSize(); i++)
				{
			        model.renderModel(entity,position,y,z-0.5D);
			        position+=2D;
				}
			}
			
		
	}

	@Override
	protected ResourceLocation getEntityTexture(Entity p_110775_1_) {
		return model.texture;
	}
	



}
