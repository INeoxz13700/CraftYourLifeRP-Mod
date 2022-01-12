package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

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

public class ModelPropellerHat extends ModelBaseHead {
	

	public IModelCustom[] models = new IModelCustom[] { AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/HeliceHat.obj")), AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/Helice.obj")) };
		
	
	public ResourceLocation[] textures = new ResourceLocation[] { new ResourceLocation("craftyourliferp","cosmetics/HeliceHat.png"),   new ResourceLocation("craftyourliferp","cosmetics/Helice.png")};
	
	private int rotation;

	public ModelPropellerHat()
	{
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 1F;
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		
		if(entity.isInvisible()) return;
		
		GL11.glPushMatrix();
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		GL11.glRotatef(180, 1, 0, 0);		
		render();
		GL11.glPopMatrix();
	}
	
	public void render()
	{
		super.render();
		Minecraft.getMinecraft().getTextureManager().bindTexture(textures[0]);
		models[0].renderAll();
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(textures[1]);
		
		GL11.glPushMatrix();
		GL11.glTranslatef(0, -0.55f, 0);
		GL11.glScalef(2f, 2f, 2f);
	
		GL11.glRotatef(rotation++,0f,1f, 0f);
		models[1].renderAll();
		GL11.glPopMatrix();
	}


	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		EntityPlayer player = (EntityPlayer)entity;
		
	}
}


