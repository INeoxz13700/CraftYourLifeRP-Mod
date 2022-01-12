package fr.craftyourliferp.animations;

import api.player.model.ModelPlayer;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;

public class DanceAnimation extends PlayerAnimator {

	private float bipedBodyRotateZ;
	private float bipedLeftLegRotateZ;
	private float bipedRightLegRotateZ;
	private float bipedHeadRotateZ;
	
	private float bipedLeftArmRotateZ;
	private float bipedRightArmRotateZ;
	private float bipedLeftArmRotateX;
	private float bipedRightArmRotateX;
	
	/*@Override
	public void stopAnimation(ModelBiped model, PlayerCachedData data) 
	{
		super.stopAnimation(model, data);
	}
	
	@Override
	public void startAnimation(ModelBiped model, PlayerCachedData data) 
	{
		super.startAnimation(model, data);
	}

	
	public void applyAnimation(ModelBiped model, PlayerCachedData data,EntityPlayer player) 
	{
	    model.bipedBody.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedBodyRotateZ;
		model.bipedLeftLeg.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedLeftLegRotateZ;
		model.bipedRightLeg.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedRightLegRotateZ;
		model.bipedHead.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedHeadRotateZ;
		
		model.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * this.bipedLeftArmRotateX;
		model.bipedLeftArm.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedLeftArmRotateZ;
		model.bipedRightArm.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedRightArmRotateZ;
		model.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * this.bipedRightArmRotateX;
	
		if(this.getCurrentState() == 1)
		{
			model.bipedBody.offsetX = -0.02f;
		}
		else if(this.getCurrentState() == 2)
		{
			model.bipedBody.offsetX = 0.02f;
		}
		else if(this.getCurrentState() == 3)
		{
			model.bipedBody.offsetX = -0.05f;
		}
		else if(this.getCurrentState() == 4)
		{
			model.bipedBody.offsetX = 0.015f;
		}
		else if(this.getCurrentState() == 5)
		{
			model.bipedBody.offsetX = -0.05f;
		}
		else if(this.getCurrentState() == 6)
		{
			model.bipedBody.offsetX = 0.015f;
		}
		else if(this.getCurrentState() == 7)
		{
			model.bipedBody.offsetX = -0.05f;
		}
		else if(this.getCurrentState() == 8)
		{
			model.bipedBody.offsetX = 0.02f;
		}
		
	}
	
	@Override
	public void playAnimation(ModelBiped model, PlayerCachedData data,EntityPlayer player) 
	{
		if(this.getCurrentState() == 0)
		{
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 15, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 15, deltaTime * 0.020f);
			
			
			if(Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01
					&& Math.abs(15-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(15-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 1)
		{
		    bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 30, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 30, deltaTime * 0.020f);
			
			if(Math.abs(-5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(30-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(30-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
	
		}
		else if(this.getCurrentState() == 2)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
		

			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -15, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -15, deltaTime * 0.020f);

			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedRightArmRotateZ) <= 0.01)
			nextState();

		}
		else if(this.getCurrentState() == 3)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -10, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 15, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 15, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -7, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 40, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 40, deltaTime * 0.020f);
			
			if(Math.abs(-10-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(15-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(15-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-7-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 4)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, 20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -30, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, -20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -30, deltaTime * 0.020f);
			
			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(-20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 5)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, 20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 20, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, -20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 20, deltaTime * 0.020f);
			
			if(Math.abs(-5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(20-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(-20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 6)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, 20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -30, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, -20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -30, deltaTime * 0.020f);
			
			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(-20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 7)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -10, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 15, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 15, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -10, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 40, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 40, deltaTime * 0.020f);
			
			if(Math.abs(-10-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(15-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(15-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 8)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
		

			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -15, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -15, deltaTime * 0.020f);

			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedRightArmRotateZ) <= 0.01)
			this.setState(2);
		}
	}*/
	
	@Override
	public void stopAnimation(ModelBiped model, ExtendedPlayer data) 
	{
		super.stopAnimation(model, data);
	}
	
	@Override
	public void startAnimation(ModelBiped model, ExtendedPlayer data) 
	{
		super.startAnimation(model, data);
	}

	
	public void applyAnimation(ModelBiped model, ExtendedPlayer data,EntityPlayer player) 
	{
	    model.bipedBody.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedBodyRotateZ;
		model.bipedLeftLeg.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedLeftLegRotateZ;
		model.bipedRightLeg.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedRightLegRotateZ;
		model.bipedHead.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedHeadRotateZ;
		model.bipedHeadwear.rotateAngleZ = model.bipedHead.rotateAngleZ;
		
		model.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * this.bipedLeftArmRotateX;
		model.bipedLeftArm.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedLeftArmRotateZ;
		model.bipedRightArm.rotateAngleZ = MathsUtils.Deg2Rad * this.bipedRightArmRotateZ;
		model.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * this.bipedRightArmRotateX;
	
		if(this.getCurrentState() == 1)
		{
			model.bipedBody.offsetX = -0.02f;
		}
		else if(this.getCurrentState() == 2)
		{
			model.bipedBody.offsetX = 0.02f;
		}
		else if(this.getCurrentState() == 3)
		{
			model.bipedBody.offsetX = -0.05f;
		}
		else if(this.getCurrentState() == 4)
		{
			model.bipedBody.offsetX = 0.015f;
		}
		else if(this.getCurrentState() == 5)
		{
			model.bipedBody.offsetX = -0.05f;
		}
		else if(this.getCurrentState() == 6)
		{
			model.bipedBody.offsetX = 0.015f;
		}
		else if(this.getCurrentState() == 7)
		{
			model.bipedBody.offsetX = -0.05f;
		}
		else if(this.getCurrentState() == 8)
		{
			model.bipedBody.offsetX = 0.02f;
		}
		
	}
	
	@Override
	public void playAnimation(ModelBiped model, ExtendedPlayer data,EntityPlayer player) 
	{
		if(this.getCurrentState() == 0)
		{
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 15, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 15, deltaTime * 0.020f);
			
			
			if(Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01
					&& Math.abs(15-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(15-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 1)
		{
		    bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 30, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 30, deltaTime * 0.020f);
			
			if(Math.abs(-5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(30-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(30-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
	
		}
		else if(this.getCurrentState() == 2)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
		

			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -15, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -15, deltaTime * 0.020f);

			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedRightArmRotateZ) <= 0.01)
			nextState();

		}
		else if(this.getCurrentState() == 3)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -10, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 15, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 15, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -7, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 40, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 40, deltaTime * 0.020f);
			
			if(Math.abs(-10-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(15-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(15-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-7-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 4)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, 20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -30, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, -20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -30, deltaTime * 0.020f);
			
			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(-20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 5)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, 20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 20, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, -20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 20, deltaTime * 0.020f);
			
			if(Math.abs(-5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(20-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(-20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 6)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, 20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -30, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, -20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -30, deltaTime * 0.020f);
			
			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(-20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-30-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 7)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, -10, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, 15, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, 15, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, -10, deltaTime * 0.020f);
			
			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, 40, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, 40, deltaTime * 0.020f);
			
			if(Math.abs(-10-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(15-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(15-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(40-this.bipedRightArmRotateZ) <= 0.01)
			nextState();
		}
		else if(this.getCurrentState() == 8)
		{
			bipedBodyRotateZ = MathsUtils.Lerp(bipedBodyRotateZ, 5, deltaTime * 0.020f);
			bipedLeftLegRotateZ =  MathsUtils.Lerp(bipedLeftLegRotateZ, -10, deltaTime * 0.020f);
			bipedRightLegRotateZ =  MathsUtils.Lerp(bipedRightLegRotateZ, -10, deltaTime * 0.020f);
			bipedHeadRotateZ = MathsUtils.Lerp(bipedHeadRotateZ, 5, deltaTime * 0.020f);
		

			bipedLeftArmRotateX = MathsUtils.Lerp(bipedLeftArmRotateX, -20, deltaTime * 0.020f);
			bipedLeftArmRotateZ = MathsUtils.Lerp(bipedLeftArmRotateZ, -15, deltaTime * 0.020f);
			bipedRightArmRotateX = MathsUtils.Lerp(bipedRightArmRotateX, 20, deltaTime * 0.020f);
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -15, deltaTime * 0.020f);

			if(Math.abs(5-bipedBodyRotateZ) <= 0.01 
					&& Math.abs(-10-bipedLeftLegRotateZ) <= 0.01
					&& Math.abs(-10-this.bipedRightLegRotateZ) <= 0.01
					&& Math.abs(5-this.bipedHeadRotateZ) <= 0.01
					&& Math.abs(-20-this.bipedLeftArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedLeftArmRotateZ) <= 0.01 
					&& Math.abs(20-this.bipedRightArmRotateX) <= 0.01 
					&& Math.abs(-15-this.bipedRightArmRotateZ) <= 0.01)
			this.setState(2);
		}
	}

}
