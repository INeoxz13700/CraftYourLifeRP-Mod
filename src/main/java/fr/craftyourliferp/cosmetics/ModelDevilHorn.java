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

public class ModelDevilHorn extends ModelBaseHead {
	
	private static ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/devil_horn.png");
	
	private final ModelRenderer bone;
	private final ModelRenderer bone4;
	private final ModelRenderer bone2;
	private final ModelRenderer bone3;
	private final ModelRenderer bone5;
	private final ModelRenderer bone6;
	private final ModelRenderer bone7;
	private final ModelRenderer bone8;

	public ModelDevilHorn() {
		textureWidth = 32;
		textureHeight = 32;

		bone = new ModelRenderer(this);
		bone.setRotationPoint(-4.0F, -8.0F, -1.0F);
		setRotationAngle(bone, 0.2182F, 0.0F, -0.48F);
		bone.cubeList.add(new ModelBox(bone, 0, 0, -1.25F, -1.0F, -2.5F, 4, 3, 4, 0.0F));

		bone4 = new ModelRenderer(this);
		bone4.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone.addChild(bone4);
		setRotationAngle(bone4, 0.0F, 0.0F, 0.2182F);
		bone4.cubeList.add(new ModelBox(bone4, 0, 7, -1.0F, -3.0F, -2.0F, 3, 3, 3, 0.0F));

		bone2 = new ModelRenderer(this);
		bone2.setRotationPoint(-1.75F, 0.0F, -0.5F);
		bone4.addChild(bone2);
		setRotationAngle(bone2, 0.0F, 0.0F, 0.4363F);
		bone2.cubeList.add(new ModelBox(bone2, 10, 11, 0.0F, -6.0F, -1.0F, 2, 3, 2, 0.0F));

		bone3 = new ModelRenderer(this);
		bone3.setRotationPoint(-2.5F, -4.0F, -0.5F);
		bone2.addChild(bone3);
		setRotationAngle(bone3, 0.0F, 0.0F, 0.6981F);
		bone3.cubeList.add(new ModelBox(bone3, 0, 0, 1.0F, -6.0F, 0.0F, 1, 3, 1, 0.0F));

		bone5 = new ModelRenderer(this);
		bone5.setRotationPoint(4.0F, -8.0F, -1.0F);
		setRotationAngle(bone5, 0.2182F, 0.0F, 0.48F);
		bone5.cubeList.add(new ModelBox(bone5, 0, 0, -2.75F, -1.0F, -2.5F, 4, 3, 4, 0.0F));

		bone6 = new ModelRenderer(this);
		bone6.setRotationPoint(0.0F, 0.0F, 0.0F);
		bone5.addChild(bone6);
		setRotationAngle(bone6, 0.0F, 0.0F, -0.2182F);
		bone6.cubeList.add(new ModelBox(bone6, 0, 7, -2.0F, -3.0F, -2.0F, 3, 3, 3, 0.0F));

		bone7 = new ModelRenderer(this);
		bone7.setRotationPoint(1.75F, 0.0F, -0.5F);
		bone6.addChild(bone7);
		setRotationAngle(bone7, 0.0F, 0.0F, -0.4363F);
		bone7.cubeList.add(new ModelBox(bone7, 10, 11, -2.0F, -6.0F, -1.0F, 2, 3, 2, 0.0F));

		bone8 = new ModelRenderer(this);
		bone8.setRotationPoint(2.5F, -4.0F, -0.5F);
		bone7.addChild(bone8);
		setRotationAngle(bone8, 0.0F, 0.0F, -0.6981F);
		bone8.cubeList.add(new ModelBox(bone8, 0, 0, -2.0F, -6.0F, 0.0F, 1, 3, 1, 0.0F));
		
		modelScaleX = 6F;
		modelScaleY = 6F;
		modelScaleZ = 6F;

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
			
			 GL11.glColor4f(1f, 1f, 1f, 1f);
			
			GL11.glPushMatrix();

			super.render(entity, limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks);
			this.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);

		    GL11.glTranslatef(-0.01F, 0.0F, 0.0F);
		    GL11.glScalef(6.0F, 6.0F, 6.0F);
			render();
			GL11.glPopMatrix();
	}
	 
	public void render()
	{
		super.render();
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		 
		bone.render(0.01f);
		bone5.render(0.01f);
	}
	 

}


