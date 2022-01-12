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

public class ModelSwordOnHead extends ModelBaseHead {
	
	private static ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/sword_on_head.png");

	private final ModelRenderer bone;

	public ModelSwordOnHead() {
		textureWidth = 32;
		textureHeight = 32;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(9.0F, -7.0F, 0.0F);
		bone.cubeList.add(new ModelBox(bone, 0, 4, -1.0F, -1.0F, 0.0F, 5, 1, 1, 0.0F));
		bone.cubeList.add(new ModelBox(bone, 6, 6, 2.75F, -1.5F, -0.5F, 1, 2, 2, 0.0F));
		bone.cubeList.add(new ModelBox(bone, 0, 6, -2.0F, -2.5F, -0.5F, 1, 4, 2, 0.0F));
		bone.cubeList.add(new ModelBox(bone, 0, 0, -16.0F, -2.0F, 0.5F, 14, 4, 0, 0.0F));
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 5F;
		this.modelTranslationX = -0.05F;
		this.modelTranslationZ = -0.07F;
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}

	 @Override
	 public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks)
	 {	 
			if(entity.isInvisible()) return;
						
			GL11.glPushMatrix();
			super.render(entity, limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks);
			this.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
			
		    GL11.glTranslatef(-0.05F, 0.0F, -0.07F);
		    GL11.glScalef(5.0F, 5.0F, 5.0F);
			render();
			
			GL11.glPopMatrix();
	}
	 
	public void render()
	{
		 super.render();
		 Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		 bone.render(0.01f);
	}
	 

}


