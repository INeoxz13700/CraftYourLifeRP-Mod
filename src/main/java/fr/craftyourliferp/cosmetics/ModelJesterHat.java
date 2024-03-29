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

public class ModelJesterHat extends ModelBaseHead {
	
	
	private static ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/jester_hat.png");

	private final ModelRenderer bipedHead;
	private final ModelRenderer bone;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone4;

	public ModelJesterHat() {
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 22.3F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 31, -4.5F, -3.4F, -4.5F, 9, 3, 9, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 24, -2.5F, -6.4F, -1.5F, 5, 3, 3, 0.0F));

		bone = new ModelRenderer(this);
		bone.setRotationPoint(0.0F, -6.4F, -5.0F);
		bipedHead.addChild(bone);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -1.0F, -1.0F, -2.5F, 2, 2, 4, 0.0F));
		bone.cubeList.add(new ModelBox(bone, 0, 35, -1.0F, 1.0F, -3.4F, 2, 2, 2, 0.2F));
		bone.cubeList.add(new ModelBox(bone, 0, 43, -2.5F, 0.0F, 0.5F, 5, 3, 3, 0.0F));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(0.0F, -6.4F, 5.0F);
		bipedHead.addChild(bone2);
		setRotationAngle(bone2, 0.0F, 3.1416F, 0.0F);
		bone2.cubeList.add(new ModelBox(bone2, 0, 0, -1.0F, -1.0F, -2.5F, 2, 2, 4, 0.0F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 35, -1.0F, 1.0F, -3.4F, 2, 2, 2, 0.2F));
		bone2.cubeList.add(new ModelBox(bone2, 0, 43, -2.5F, 0.0F, 0.5F, 5, 3, 3, 0.0F));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(5.0F, -6.4F, 0.0F);
		bipedHead.addChild(bone3);
		setRotationAngle(bone3, 0.0F, -1.5708F, 0.0F);
		bone3.cubeList.add(new ModelBox(bone3, 0, 53, -1.0F, -1.0F, -2.5F, 2, 2, 4, 0.0F));
		bone3.cubeList.add(new ModelBox(bone3, 0, 35, -1.0F, 1.0F, -3.4F, 2, 2, 2, 0.2F));
		bone3.cubeList.add(new ModelBox(bone3, 0, 59, -2.5F, 0.0F, 0.5F, 5, 3, 2, 0.0F));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(-5.0F, -6.4F, 0.0F);
		bipedHead.addChild(bone4);
		setRotationAngle(bone4, 0.0F, 1.5708F, 0.0F);
		bone4.cubeList.add(new ModelBox(bone4, 0, 53, -1.0F, -1.0F, -2.5F, 2, 2, 4, 0.0F));
		bone4.cubeList.add(new ModelBox(bone4, 0, 35, -1.0F, 1.0F, -3.4F, 2, 2, 2, 0.2F));
		bone4.cubeList.add(new ModelBox(bone4, 0, 59, -2.5F, 0.0F, 0.5F, 5, 3, 2, 0.0F));
		
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 5F;
		this.modelTranslationY = -1.5F;
	}

	 @Override
	 public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks)
	 {	 
			if(entity.isInvisible()) return;
			
			GL11.glPushMatrix();
			super.render(entity, limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks);
			this.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);

		    GL11.glTranslatef(0.0F, -1.5F, 0.02F);
		    GL11.glScalef(5.0F, 5.0F, 5.0F);
			
			render();

			GL11.glPopMatrix();
	}
	
	public void render()
	{
		super.render();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		bipedHead.render(0.01f);
	}

	public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	}
}


