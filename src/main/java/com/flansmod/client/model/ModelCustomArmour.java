package com.flansmod.client.model;

import org.lwjgl.opengl.GL11;

import com.flansmod.client.tmt.ModelRendererTurbo;
import com.flansmod.common.teams.ArmourType;

import cpw.mods.fml.client.registry.RenderingRegistry;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;

public class ModelCustomArmour extends ModelBiped 
{
	public ArmourType type;
	
	public ModelRendererTurbo[] headModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] bodyModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] leftArmModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] rightArmModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] leftLegModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] rightLegModel = new ModelRendererTurbo[0];
	public ModelRendererTurbo[] skirtFrontModel = new ModelRendererTurbo[0]; //Acts like a leg piece, but its pitch is set to the maximum of the two legs
	public ModelRendererTurbo[] skirtRearModel = new ModelRendererTurbo[0]; //Acts like a leg piece, but its pitch is set to the minimum of the two legs

	public void render(Entity entity, float f, float f1, float f2, float f3, float f4, float f5)
	{ 
		GL11.glPushMatrix();
		GL11.glScalef(type.modelScale, type.modelScale, type.modelScale);
		isSneak = entity.isSneaking();
		ItemStack itemstack = ((EntityLivingBase)entity).getEquipmentInSlot(0);
        heldItemRight = itemstack != null ? 1 : 0;

        aimedBow = false;
        if (itemstack != null && entity instanceof EntityPlayer && ((EntityPlayer)entity).getItemInUseCount() > 0)
        {
        	EnumAction enumaction = itemstack.getItemUseAction();
            if (enumaction == EnumAction.block)
            {
                heldItemRight = 3;
            }
            else if (enumaction == EnumAction.bow)
            {
                aimedBow = true;
            }
        }

		setRotationAngles(f, f1, f2, f3, f4, f5, entity); 

		if(entity instanceof EntityPlayer)
		{
			//PlayerCachedData data = PlayerCachedData.getData((EntityPlayer)entity);
			ExtendedPlayer exp = ExtendedPlayer.get((EntityPlayer)entity);

			/*if(data.currentPlayingAnimation != null)
			{
				data.currentPlayingAnimation.copyModelAngles(RendererListener.modelPlayer.bipedHead,bipedHead);
				data.currentPlayingAnimation.copyModelAngles(RendererListener.modelPlayer.bipedHead,bipedHeadwear);
				data.currentPlayingAnimation.copyModelAngles(RendererListener.modelPlayer.bipedBody,bipedBody);
				data.currentPlayingAnimation.copyModelAngles(RendererListener.modelPlayer.bipedLeftArm,bipedLeftArm);
				data.currentPlayingAnimation.copyModelAngles(RendererListener.modelPlayer.bipedRightArm,bipedRightArm);
				data.currentPlayingAnimation.copyModelAngles(RendererListener.modelPlayer.bipedLeftLeg,bipedLeftLeg);
				data.currentPlayingAnimation.copyModelAngles(RendererListener.modelPlayer.bipedRightLeg,bipedRightLeg);
			}
			else
			{
				copyModelAngles(RendererListener.modelPlayer.bipedHead,bipedHead);
				copyModelAngles(RendererListener.modelPlayer.bipedBody,bipedBody);
				copyModelAngles(RendererListener.modelPlayer.bipedLeftArm,bipedLeftArm);
				copyModelAngles(RendererListener.modelPlayer.bipedRightArm,bipedRightArm);
				copyModelAngles(RendererListener.modelPlayer.bipedLeftLeg,bipedLeftLeg);
				copyModelAngles(RendererListener.modelPlayer.bipedRightLeg,bipedRightLeg);
			
				
			}*/
			
			if(exp.currentPlayingAnimation != null)
			{
				exp.currentPlayingAnimation.copyModel(RendererListener.modelPlayer.bipedHead,bipedHead);
				exp.currentPlayingAnimation.copyModel(RendererListener.modelPlayer.bipedHead,bipedHeadwear);
				exp.currentPlayingAnimation.copyModel(RendererListener.modelPlayer.bipedBody,bipedBody);
				exp.currentPlayingAnimation.copyModel(RendererListener.modelPlayer.bipedLeftArm,bipedLeftArm);
				exp.currentPlayingAnimation.copyModel(RendererListener.modelPlayer.bipedRightArm,bipedRightArm);
				exp.currentPlayingAnimation.copyModel(RendererListener.modelPlayer.bipedLeftLeg,bipedLeftLeg);
				exp.currentPlayingAnimation.copyModel(RendererListener.modelPlayer.bipedRightLeg,bipedRightLeg);
			}
			else
			{
				copyModel(RendererListener.modelPlayer.bipedHead,bipedHead);
				copyModel(RendererListener.modelPlayer.bipedBody,bipedBody);
				copyModel(RendererListener.modelPlayer.bipedLeftArm,bipedLeftArm);
				copyModel(RendererListener.modelPlayer.bipedRightArm,bipedRightArm);
				copyModel(RendererListener.modelPlayer.bipedLeftLeg,bipedLeftLeg);
				copyModel(RendererListener.modelPlayer.bipedRightLeg,bipedRightLeg);
			}
		}
		

		
		
		


		render(headModel, bipedHead, f5, type.modelScale);
		render(bodyModel, bipedBody, f5, type.modelScale);
		render(leftArmModel, bipedLeftArm, f5, type.modelScale);
		render(rightArmModel, bipedRightArm, f5, type.modelScale);
		render(leftLegModel, bipedLeftLeg, f5, type.modelScale);
		render(rightLegModel, bipedRightLeg, f5, type.modelScale);
	
		
		//Skirt front
		{
			for(ModelRendererTurbo mod : skirtFrontModel)
			{
				mod.rotationPointX = (bipedLeftLeg.rotationPointX + bipedRightLeg.rotationPointX) / 2F / type.modelScale;
				mod.rotationPointY = (bipedLeftLeg.rotationPointY + bipedRightLeg.rotationPointY) / 2F / type.modelScale;
				mod.rotationPointZ = (bipedLeftLeg.rotationPointZ + bipedRightLeg.rotationPointZ) / 2F / type.modelScale;
				mod.rotateAngleX = Math.min(bipedLeftLeg.rotateAngleX, bipedRightLeg.rotateAngleX);
				mod.rotateAngleY = bipedLeftLeg.rotateAngleY;
				mod.rotateAngleZ = bipedLeftLeg.rotateAngleZ;
				mod.render(f5);
			}
		}
		//Skirt back
		{
			for(ModelRendererTurbo mod : skirtRearModel)
			{
				mod.rotationPointX = (bipedLeftLeg.rotationPointX + bipedRightLeg.rotationPointX) / 2F / type.modelScale;
				mod.rotationPointY = (bipedLeftLeg.rotationPointY + bipedRightLeg.rotationPointY) / 2F / type.modelScale;
				mod.rotationPointZ = (bipedLeftLeg.rotationPointZ + bipedRightLeg.rotationPointZ) / 2F / type.modelScale;
				mod.rotateAngleX = Math.max(bipedLeftLeg.rotateAngleX, bipedRightLeg.rotateAngleX);
				mod.rotateAngleY = bipedLeftLeg.rotateAngleY;
				mod.rotateAngleZ = bipedLeftLeg.rotateAngleZ;
				mod.render(f5);
			}
		}
		GL11.glPopMatrix();
		

	} 
	
	public void render(ModelRendererTurbo[] models, ModelRenderer bodyPart, float f5, float scale)
	{
		setBodyPart(models, bodyPart, scale);
		for(ModelRendererTurbo mod : models)
		{
			mod.rotateAngleX = bodyPart.rotateAngleX;
			mod.rotateAngleY = bodyPart.rotateAngleY;
			mod.rotateAngleZ = bodyPart.rotateAngleZ;
			mod.offsetX = bodyPart.offsetX;
			mod.offsetY = bodyPart.offsetY;
			mod.offsetZ = bodyPart.offsetZ;
			
			mod.render(f5);
		}
	}
	
	public void setBodyPart(ModelRendererTurbo[] models, ModelRenderer bodyPart, float scale)
	{
		for(ModelRendererTurbo mod : models)
		{
			mod.rotationPointX = bodyPart.rotationPointX / scale;
			mod.rotationPointY = bodyPart.rotationPointY / scale;
			mod.rotationPointZ = bodyPart.rotationPointZ / scale;
		}
	}
	
    public void copyModel(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
        dest.rotationPointX = source.rotationPointX;
        dest.rotationPointY = source.rotationPointY;
        dest.rotationPointZ = source.rotationPointZ;
        
        dest.offsetX = source.offsetX;
        dest.offsetY = source.offsetY;
        dest.offsetZ = source.offsetZ;
    }
    

}
