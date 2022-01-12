package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelRabbitEars extends ModelBaseHead {
	

	public IModelCustom model;
		
	
	public ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/RabbitEars.png");
	

	public ModelRabbitEars()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/RabbitEars.obj"));
		this.modelScaleY = 1F;
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 0.9F;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		
		if(entity.isInvisible()) return;
		
		GL11.glPushMatrix();
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
				
		GL11.glRotatef(180, 1, 0, 0);		
		GL11.glTranslatef(0F, 0.01F, 0F);
			
		if(entity.isSneaking())
		{
			GL11.glTranslatef(0, -0.02F, 0F);
		}
		GL11.glScalef(0.95F, 0.95F, 0.95F);
		
		render();
		GL11.glPopMatrix();
	}
	
	public void render()
	{
		super.render();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		model.renderAll();
	}


	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		EntityPlayer player = (EntityPlayer)entity;
		
	}
}


