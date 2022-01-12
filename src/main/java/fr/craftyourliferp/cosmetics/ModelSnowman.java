package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelSnowman extends ModelBaseBody {
	

	public IModelCustom model;
	
	private long lastSoundTime;
	
	
	public ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/snowman.png");
	

	public ModelSnowman()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/snowman.obj"));
	}
	
	 @Override
	 public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks) {
		 
		if(entity.isInvisible()) return;

		 
		 GL11.glPushMatrix();
		 GL11.glColor4f(1f, 1f, 1f, 1f);
		 super.render(entity, limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks);
		 this.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
		 GL11.glScalef(1f, 1f, 1f);
		 GL11.glRotatef(180, 1, 0, 0);
		 GL11.glTranslatef(0.4f, 0.3f, 0f);
		 render();
		 GL11.glPopMatrix();
	 }
	 
	 
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks, Entity entity)
	{ 

		EntityPlayer player = (EntityPlayer)entity;
		if(player.inventory.armorInventory[2] != null)
		{
			GL11.glTranslatef(0, -0.04f, 0);
		}
		
		super.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
		
	}
	
	public void render()
	{
		 super.render();
		 Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		 model.renderAll();
	}
	 
		
		
}



