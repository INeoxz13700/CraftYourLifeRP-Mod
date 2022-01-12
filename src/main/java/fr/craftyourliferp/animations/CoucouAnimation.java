package fr.craftyourliferp.animations;

import com.flansmod.client.model.ModelCustomArmour;

import api.player.model.ModelPlayer;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.network.PacketAnimation;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.entity.player.EntityPlayer;

public class CoucouAnimation extends PlayerAnimator {

	private float bipedRightArmRotateZ;	
	
	private int repeatTime = 0;
	
	/*@Override
	public void playAnimation(ModelBiped model, PlayerCachedData data, EntityPlayer player) 
	{
		
		if(getCurrentState() == 0)
		{			
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -60f, 0.0098f * deltaTime);
			if(Math.abs(-60 - bipedRightArmRotateZ) <= 0.01)
			{

				nextState();
			}
		}
		else if(getCurrentState() == 1)
		{
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -10f, 0.0098f * deltaTime);

			if(Math.abs(-10 - bipedRightArmRotateZ) <= 0.01)
			{

				if(repeatTime == 3)
				{
					nextState();
					
					return;
				}
					
				repeatTime++;
					
				previousState();
			}		
		}
		else if(getCurrentState() == 2)
		{
			stopAnimation(model, data);	
		}
				
	}
	
	@Override
	public void applyAnimation(ModelBiped model, PlayerCachedData thePlayerCacheData, EntityPlayer player)
	{
		model.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * -170;
		model.bipedRightArm.rotateAngleZ = MathsUtils.Deg2Rad * bipedRightArmRotateZ;
	}

	@Override
	public void startAnimation(ModelBiped model, PlayerCachedData data)
	{
		super.startAnimation(model, data);
		bipedRightArmRotateZ = -10;
	}

	@Override
	public void stopAnimation(ModelBiped model, PlayerCachedData data) 
	{
		super.stopAnimation(model, data);
		PacketAnimation packet = new PacketAnimation();
		packet.animation = 0;
		CraftYourLifeRPMod.packetHandler.sendToServer(packet);
	}*/
	
	@Override
	public void playAnimation(ModelBiped model, ExtendedPlayer data, EntityPlayer player) 
	{
		
		if(getCurrentState() == 0)
		{			
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -60f, 0.0098f * deltaTime);
			if(Math.abs(-60 - bipedRightArmRotateZ) <= 0.01)
			{

				nextState();
			}
		}
		else if(getCurrentState() == 1)
		{
			bipedRightArmRotateZ = MathsUtils.Lerp(bipedRightArmRotateZ, -10f, 0.0098f * deltaTime);

			if(Math.abs(-10 - bipedRightArmRotateZ) <= 0.01)
			{

				if(repeatTime == 3)
				{
					nextState();
					
					return;
				}
					
				repeatTime++;
					
				previousState();
			}		
		}
		else if(getCurrentState() == 2)
		{
			stopAnimation(model, data);	
		}
				
	}
	
	@Override
	public void applyAnimation(ModelBiped model, ExtendedPlayer thePlayerCacheData, EntityPlayer player)
	{
		model.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * -170;
		model.bipedRightArm.rotateAngleZ = MathsUtils.Deg2Rad * bipedRightArmRotateZ;
	}

	@Override
	public void startAnimation(ModelBiped model, ExtendedPlayer data)
	{
		super.startAnimation(model, data);
		bipedRightArmRotateZ = -10;
	}

	@Override
	public void stopAnimation(ModelBiped model, ExtendedPlayer data) 
	{
		super.stopAnimation(model, data);
		PacketAnimation packet = new PacketAnimation();
		packet.animation = 0;
		CraftYourLifeRPMod.packetHandler.sendToServer(packet);
	}
	
	

}
