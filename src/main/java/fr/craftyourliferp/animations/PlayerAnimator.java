package fr.craftyourliferp.animations;

import com.flansmod.client.model.ModelCustomArmour;
import com.flansmod.common.teams.ItemTeamArmour;

import api.player.model.ModelPlayer;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.models.CustomModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public abstract class PlayerAnimator {

	private int currentState = 0;
	
	protected boolean animationIsPlaying = false;
	
	public long deltaTime;
	
	public long oldTime;
	public long newTime;
			
	
	public void nextState()
	{
		currentState++;
	}
	
	public void previousState()
	{
		currentState--;
	}
	
	public int getCurrentState()
	{
		return currentState;
	}
	
	public void setState(int state)
	{
		this.currentState = state;
	}
	
	/*public  void playAnimation(ModelBiped model, PlayerCachedData thePlayerCacheData, EntityPlayer player)
	{
		
	}*/

	/*public void startAnimation(ModelBiped model, PlayerCachedData thePlayerCacheData)
	{
		animationIsPlaying = true;
	}*/
	
	/*public void stopAnimation(ModelBiped model, PlayerCachedData thePlayerCacheData)
	{
		animationIsPlaying = false;
		thePlayerCacheData.currentPlayingAnimation = null;
	}*/
	
	public  void playAnimation(ModelBiped model, ExtendedPlayer extendedPlayer, EntityPlayer player)
	{
		
	}
	
	public void startAnimation(ModelBiped model, ExtendedPlayer extendedPlayer)
	{
		animationIsPlaying = true;
	}
	
	public void stopAnimation(ModelBiped model, ExtendedPlayer extendedPlayer)	{
		animationIsPlaying = false;
		extendedPlayer.currentPlayingAnimation = null;
	}
	
	/*public void applyAnimation(ModelBiped model, PlayerCachedData thePlayerCacheData, EntityPlayer player)
	{
		
	}*/
	public void applyAnimation(ModelBiped model, ExtendedPlayer thePlayerCacheData, EntityPlayer player)
	{
		
	}
	
	public boolean getAnimationisPlaying()
	{
		return animationIsPlaying;
	}
	
    public  void copyModelAngles(ModelRenderer source, ModelRenderer dest)
    {
        dest.rotateAngleX = source.rotateAngleX;
        dest.rotateAngleY = source.rotateAngleY;
        dest.rotateAngleZ = source.rotateAngleZ;
        dest.rotationPointX = source.rotationPointX;
        dest.rotationPointY = source.rotationPointY;
        dest.rotationPointZ = source.rotationPointZ;
    }
	
}
