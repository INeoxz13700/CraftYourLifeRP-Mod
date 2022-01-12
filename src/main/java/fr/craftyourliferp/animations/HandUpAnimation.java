package fr.craftyourliferp.animations;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;

public class HandUpAnimation extends PlayerAnimator {

	
	/*@Override
	public void applyAnimation(ModelBiped model, PlayerCachedData data, EntityPlayer player) 
	{
		super.applyAnimation(model, data, player);
		model.bipedLeftArm.rotateAngleX = -(MathsUtils.Deg2Rad * 180);
		model.bipedLeftArm.rotateAngleZ = MathsUtils.Deg2Rad * 10;
		
		model.bipedRightArm.rotateAngleX = -(MathsUtils.Deg2Rad * 180);
		model.bipedRightArm.rotateAngleZ =  -MathsUtils.Deg2Rad * 10;
	}*/
	
	@Override
	public void applyAnimation(ModelBiped model, ExtendedPlayer data, EntityPlayer player) 
	{
		super.applyAnimation(model, data, player);
		model.bipedLeftArm.rotateAngleX = -(MathsUtils.Deg2Rad * 180);
		model.bipedLeftArm.rotateAngleZ = MathsUtils.Deg2Rad * 10;
		
		model.bipedRightArm.rotateAngleX = -(MathsUtils.Deg2Rad * 180);
		model.bipedRightArm.rotateAngleZ =  -MathsUtils.Deg2Rad * 10;
	}


}
