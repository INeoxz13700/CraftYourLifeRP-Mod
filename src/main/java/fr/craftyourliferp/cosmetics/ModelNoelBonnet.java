package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelNoelBonnet extends ModelBaseHead {
	

	public IModelCustom model;
	
	private long lastSoundTime;
	
	
	public ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/noelBonnet.png");
	

	public ModelNoelBonnet()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/noelBonnet.obj"));
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 1.5F;
		this.modelTranslationY = 0.82f;
	}
	
	 @Override
	 public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks) {
		 
		if(entity.isInvisible()) return;

		 
		 GL11.glPushMatrix();
		 super.render(entity, limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks);
		 this.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
		
		 GL11.glRotatef(180, 0, 0, 1);
		 GL11.glTranslatef(-0.015F, 1.235F, -0.045F);
		 
		 GL11.glScalef(1.5F,1.5F,1.5F);

	
		 render();
		 GL11.glPopMatrix();
	 }
	 
	 
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks, Entity entity)
	{ 
		super.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
	}
	
	public void render()
	{
		super.render();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		model.renderAll();
	}
	 
		
		
}



