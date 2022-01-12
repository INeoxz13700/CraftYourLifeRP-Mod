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

public class ModelBird extends ModelBaseBody {
	
	
	
	public IModelCustom model;
	
	private long lastSoundTime;
	
	
	public ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/bird.png");
	

	public ModelBird()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/bird.obj"));
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		
		if(entity.isInvisible()) return;
		
		GL11.glPushMatrix();
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glTranslatef(-0.225f, -0.2f, 0);
		GL11.glScalef(1.5f, 1.5f, 1.5f);
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
		
		if(player.inventory.armorInventory[2] != null)
		{
			GL11.glTranslatef(0, -0.05f, 0);
		}
	
		
		int rand = MathHelper.getRandomIntegerInRange(new Random(), 0, 1000);
		if(rand == 50)
		{
			if((System.currentTimeMillis() - this.lastSoundTime) / 1000 >= 2L)
			{
				lastSoundTime = System.currentTimeMillis();
				entity.worldObj.playSound(entity.posX, entity.posY, entity.posZ, "craftyourliferp:bird", 1.0F, 1.0F, false);
			}
		}
		
		
	}
}


