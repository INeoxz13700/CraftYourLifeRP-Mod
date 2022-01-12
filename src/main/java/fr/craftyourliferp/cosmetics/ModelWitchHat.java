package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.MinecraftUtils;
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

public class ModelWitchHat extends ModelBaseHead {
	

	public IModelCustom model;
	
	private long lastSoundTime;
	
	
	public ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/WitchHat.png");
	

	public ModelWitchHat()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/WitchHat.obj"));
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 1F;
		this.modelTranslationY = -0.770F;
	}
	
	 @Override
	 public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks) {
		 
		   if(entity.isInvisible()) return;

		 
		    GL11.glPushMatrix();

		    super.render(entity, limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks);
			super.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
						
			
			GL11.glRotatef(180.0F, 0.0F, 0.0F, 1.0F);
			GL11.glTranslatef(0.0F, 0.77F, 0.07F);
			
			
			GL11.glScalef(1F, 1F, 1F);

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



