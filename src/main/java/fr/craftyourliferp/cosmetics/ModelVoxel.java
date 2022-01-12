package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
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

public class ModelVoxel extends ModelBaseBody {
	

	public IModelCustom model;
		
	
	public ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/voxel.png");
	

	public ModelVoxel()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","cosmetics/voxel.obj"));
	}

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{
		
		if(entity.isInvisible()) return;

		//PlayerCachedData cachedData = PlayerCachedData.getData((EntityPlayer)entity);
		ExtendedPlayer cachedData = ExtendedPlayer.get((EntityPlayer)entity);

		GL11.glPushMatrix();
		super.render(entity, f, f1, f2, f3, f4, f5);
		setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		GL11.glScalef(0.025f, 0.025f, 0.025f);
		GL11.glRotatef(180, 1, 0, 0);
		GL11.glRotatef(180, 0, 1, 0);
		GL11.glTranslatef(-0.35f / 0.025f, -0.33f / 0.025f, 0);
		if(cachedData.isProning())
		{
			GL11.glTranslatef(0, 0.03f / 0.025f, 0);
		}
		render();
		GL11.glPopMatrix();
	}


	public void setRotationAngles(float f, float f1, float f2, float f3, float f4, float f5, Entity entity)
	{
		super.setRotationAngles(f, f1, f2, f3, f4, f5, entity);
		
		EntityPlayer player = (EntityPlayer)entity;
		
		if(player.inventory.armorInventory[2] != null)
		{
			GL11.glTranslatef(0, -0.05f, 0);
		}
		
		

	}
	
	public void render()
	{
		super.render();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		model.renderAll();
	}
}


