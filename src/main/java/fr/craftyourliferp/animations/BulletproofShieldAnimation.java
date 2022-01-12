package fr.craftyourliferp.animations;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.game.events.OverlayRendererListener;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.items.ItemBulletproofShield;
import fr.craftyourliferp.items.renderer.ItemBulletproofShieldRenderer;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class BulletproofShieldAnimation {
		
	
	public enum AnimationType
	{
		FIRST_PERSON,
		THIRD_PERSON
	};
	
	private int animationState = 0;
	
	private Vec3 previousRotation;
	
	private Vec3 previousTranslation;
	
	private int startedTicks;
	
	private AnimationType type;
	
	public boolean animationIsPlaying;

	
	public BulletproofShieldAnimation(AnimationType type)
	{
		this.type = type;
	}
	

	public void playAnimation(ItemBulletproofShieldRenderer renderer,ItemStack is, EntityPlayer theClientPlayer)
	{
		if(animationIsPlaying)
		{
			
			Vec3 targetRotation = getTargetRotationForState();
			Vec3 targetTranslation = getTargetTranslationForState();
			
			Vec3 deltaRotation = Vec3.createVectorHelper((float)targetRotation.xCoord - renderer.firstPersonRotationX,(float)targetRotation.yCoord - renderer.firstPersonRotationY, (float)targetRotation.zCoord - renderer.firstPersonRotationZ);
			Vec3 deltaTranslation = Vec3.createVectorHelper((float)targetTranslation.xCoord - renderer.firstPersonTranslationX,(float)targetTranslation.yCoord - renderer.firstPersonTranslationY, (float)targetTranslation.zCoord - renderer.firstPersonTranslationZ);

			int duration = is.getMaxItemUseDuration();
			
			float speed = 0.0098f * renderer.deltaTime;
	
			renderer.firstPersonRotationX = MathsUtils.Lerp(renderer.firstPersonRotationX, (float) targetRotation.xCoord, speed);
			renderer.firstPersonRotationY = MathsUtils.Lerp(renderer.firstPersonRotationY, (float) targetRotation.yCoord, speed);
			renderer.firstPersonRotationZ = MathsUtils.Lerp(renderer.firstPersonRotationZ, (float) targetRotation.zCoord, speed);
			
			renderer.firstPersonTranslationX = MathsUtils.Lerp(renderer.firstPersonTranslationX, (float) targetTranslation.xCoord, speed);
			renderer.firstPersonTranslationY = MathsUtils.Lerp(renderer.firstPersonTranslationY, (float) targetTranslation.yCoord, speed);
			renderer.firstPersonTranslationZ = MathsUtils.Lerp(renderer.firstPersonTranslationZ, (float) targetTranslation.zCoord, speed);
		}
		else
		{
			startedTicks = theClientPlayer.ticksExisted;
			if(type == AnimationType.FIRST_PERSON)
			{
				previousRotation = Vec3.createVectorHelper(renderer.firstPersonRotationX, renderer.firstPersonRotationY, renderer.firstPersonRotationZ);
				previousTranslation = Vec3.createVectorHelper(renderer.firstPersonTranslationX, renderer.firstPersonTranslationY, renderer.firstPersonTranslationZ);
			}

			
			
			animationIsPlaying = true;
		}
	}
	
	public void playAnimation(CustomModelPlayer model,ItemStack is, EntityPlayer theClientPlayer)
	{
		if(animationIsPlaying)
		{
			
			Vec3 targetRotation = getTargetRotationForState();
			Vec3 targetTranslation = getTargetTranslationForState();
			
			Vec3 deltaRotation = Vec3.createVectorHelper((float)targetRotation.xCoord - model.rotationX,(float)targetRotation.yCoord - model.rotationY, (float)targetRotation.zCoord - model.rotationZ);

			int duration = is.getMaxItemUseDuration();
			
			float speed = 0.000098f * 1;

			model.rotationX = MathsUtils.Lerp(model.rotationX, (float) targetRotation.xCoord, speed);
			model.rotationY = MathsUtils.Lerp(model.rotationY, (float) targetRotation.yCoord, speed);
			model.rotationZ = MathsUtils.Lerp(model.rotationZ, (float) targetRotation.zCoord, speed);
			

			if((theClientPlayer.ticksExisted - startedTicks) % duration == 0)
			{
				this.setInitial(model);
			}

		}
		else
		{
			startedTicks = theClientPlayer.ticksExisted;
			
			if(type == AnimationType.THIRD_PERSON)
			{
				previousRotation = Vec3.createVectorHelper(model.rotationX, model.rotationY, model.rotationZ);
			}

			animationIsPlaying = true;
		}
	}
	
	public boolean animationIsPlaying()
	{
		return this.animationIsPlaying;
	}
	
	public Vec3 getTargetRotationForState()
	{
		if(type == AnimationType.FIRST_PERSON)
		{
			if(animationState == 0)
			{
				return Vec3.createVectorHelper(0f, 42f, 0f);
			}
			else
			{
				return Vec3.createVectorHelper(0f, 0f, 0f);
			}
		}
		else
		{
			if(animationState == 0)
			{
				return Vec3.createVectorHelper(0, 0, 0);
			}
			else
			{
				return Vec3.createVectorHelper(0, 40, 0);
			}
		}
	}
	
	public Vec3 getTargetTranslationForState()
	{
		if(type == AnimationType.FIRST_PERSON)
		{
			if(animationState == 0)
			{
				return Vec3.createVectorHelper(-0.4f, -0.8f, -0.4f);
			}
			else
			{
				return Vec3.createVectorHelper(0.4f, -0.8f, 0.4f);
			}
		}
		else
		{
			if(animationState == 0)
			{
				return Vec3.createVectorHelper(0, 0, 0f);
			}
			else
			{
				return Vec3.createVectorHelper(0, 0, 0f);
			}
		}
	}
	
	public void setState(int state)
	{
		animationState = state;
	}
	
	public int getAnimationState()
	{
		return animationState;
	}
	
	
	public void setInitial(ItemBulletproofShieldRenderer item)
	{		
		if(type == AnimationType.FIRST_PERSON)
		{
			item.firstPersonTranslationX = (float)previousTranslation.xCoord;
			item.firstPersonTranslationY = (float)previousTranslation.yCoord;
			item.firstPersonTranslationZ = (float)previousTranslation.zCoord;
			
			item.firstPersonRotationX = (float)previousRotation.xCoord;
			item.firstPersonRotationY = (float)previousRotation.yCoord;
			item.firstPersonRotationZ = (float)previousRotation.zCoord;
		}

	}
	
	public void setInitial(CustomModelPlayer model)
	{		
		if(type == AnimationType.THIRD_PERSON)
		{
			model.rotationX = 0;
			model.rotationY = 40;
			model.rotationZ = 0;
		}

	}


	


	
}