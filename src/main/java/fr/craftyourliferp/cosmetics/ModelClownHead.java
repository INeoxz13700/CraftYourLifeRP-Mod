package fr.craftyourliferp.cosmetics;

import java.util.Random;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.game.events.RendererListener;
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

public class ModelClownHead extends ModelBaseHead {
	
	private static ResourceLocation texture = new ResourceLocation("craftyourliferp","cosmetics/clown_head.png");
	
	private final ModelRenderer bipedHead;

	public ModelClownHead() {
		textureWidth = 64;
		textureHeight = 64;

		bipedHead = new ModelRenderer(this);
		bipedHead.setRotationPoint(0.0F, 24.0F, 0.0F);
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 33, 3.75F, -7.0F, -2.0F, 1, 3, 6, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 33, -4.75F, -7.0F, -2.0F, 1, 3, 6, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 29, -4.0F, -7.0F, 3.75F, 8, 3, 1, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 42, -5.0F, -9.0F, -5.0F, 10, 2, 10, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 54, -4.0F, -11.0F, -4.0F, 8, 2, 8, 0.0F));
		bipedHead.cubeList.add(new ModelBox(bipedHead, 0, 23, -1.5F, -4.5F, -6.25F, 3, 3, 3, -0.2F));
		
		this.modelScaleX = this.modelScaleY = this.modelScaleZ = 6F;
		this.modelTranslationY = -1.42f;
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
		    
		    if(!RendererListener.modelPlayer.bipedHeadwear.isHidden)
		    {
			    GL11.glTranslatef(0.0F, -1.25F, 0.0F);

			    GL11.glScalef(6.7F, 5.0F, 6.7F);		    	
		    }
		    else
		    {
			    GL11.glTranslatef(0.0F, -1.22F, 0.0F);
			    GL11.glScalef(6.0F, 5.0F, 6.0F);
		    } 
			render();
			GL11.glPopMatrix();
	}
	 
	public void render()
	{
		 super.render();
		 Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		 
		 bipedHead.render(0.01f);
	}
	 

}


