package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBox;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemArmor;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ModelAureole extends ModelBaseHead {
	
	private final ModelRenderer bone2;
	
	private static ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/aureole.png");

	public ModelAureole() {
		textureWidth = 32;
		textureHeight = 32;

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, -11.0F, -5.0F);
		bone2.cubeList.add(new ModelBox(bone2, 18, 2, -3.0F, -1.0F, 0.0F, 6, 1, 1, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 18, 0, -3.0F, -1.0F, 9.0F, 6, 1, 1, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, 3.0F, -1.0F, 1.0F, 1, 1, 1, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, 3.0F, -1.0F, 8.0F, 1, 1, 1, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, -4.0F, -1.0F, 1.0F, 1, 1, 1, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, -4.0F, -1.0F, 8.0F, 1, 1, 1, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, -5.0F, -1.0F, 2.0F, 1, 1, 6, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, 4.0F, -1.0F, 2.0F, 1, 1, 6, 0.0F));
		
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 5F;
		this.modelTranslationY = -0.05F;
	}

	 @Override
	 public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks)
	 { 
			if(entity.isInvisible()) return;
			
			GL11.glPushMatrix();
			super.render(entity, limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks);
			this.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);	
			
			GL11.glTranslatef(0, -0.1F, 0F);
			GL11.glScalef(5F, 5F, 5F);
			render();
			
			GL11.glPopMatrix();
	}
	 
	public void render()
	{
		 super.render();

		 Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		 bone2.render(0.01f);
	}
	 
	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}


