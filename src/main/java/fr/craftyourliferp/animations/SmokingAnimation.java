package fr.craftyourliferp.animations;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.game.events.TicksHandler;
import fr.craftyourliferp.items.renderer.ItemCigaretteRenderer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.model.ModelBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Vec3;

public class SmokingAnimation {
		
	private int animationState = 0;
	
	private Vec3 previousRotation;
	
	private Vec3 previousTranslation;
	
	private int startedTicks;

	
	
	public SmokingAnimation()
	{
		
	}
	
	public boolean animationIsPlaying;

	public void playAnimation(ItemCigaretteRenderer renderer,ItemStack is, EntityPlayer theClientPlayer)
	{
		if(animationIsPlaying)
		{
			Vec3 targetRotation = getTargetRotationForState();
			Vec3 targetTranslation = getTargetTranslationForState();
			
			Vec3 deltaRotation = Vec3.createVectorHelper((float)targetRotation.xCoord - renderer.rotationX,(float)targetRotation.yCoord - renderer.rotationY, (float)targetRotation.zCoord - renderer.rotationZ);
			Vec3 deltaTranslation = Vec3.createVectorHelper((float)targetTranslation.xCoord - renderer.translationX,(float)targetTranslation.yCoord - renderer.translationY, (float)targetTranslation.zCoord - renderer.translationZ);

			int duration = is.getMaxItemUseDuration();
			
			float state = (float)theClientPlayer.getItemInUseDuration() / is.getMaxItemUseDuration() * renderer.deltaTime * 0.09f;

			renderer.rotationX = MathsUtils.Lerp(renderer.rotationX, (float) targetRotation.xCoord, state);
			renderer.rotationY = MathsUtils.Lerp(renderer.rotationY, (float) targetRotation.yCoord, state);
			renderer.rotationZ = MathsUtils.Lerp(renderer.rotationZ, (float) targetRotation.zCoord, state);
			
			renderer.translationX = MathsUtils.Lerp(renderer.translationX, (float) targetTranslation.xCoord, state);
			renderer.translationY = MathsUtils.Lerp(renderer.translationY, (float) targetTranslation.yCoord, state);
			renderer.translationZ = MathsUtils.Lerp(renderer.translationZ, (float) targetTranslation.zCoord, state);


		}
		else
		{
			startedTicks = TicksHandler.ticks;
			previousRotation = Vec3.createVectorHelper(renderer.rotationX, renderer.rotationY, renderer.rotationZ);
			previousTranslation = Vec3.createVectorHelper(renderer.translationX, renderer.translationY, renderer.translationZ);

			animationIsPlaying = true;
		}
	}
	
	public boolean animationIsPlaying()
	{
		return this.animationIsPlaying;
	}
	
	public Vec3 getTargetRotationForState()
	{
		return Vec3.createVectorHelper(0, 50, -25);
	}
	
	public Vec3 getTargetTranslationForState()
	{
		return Vec3.createVectorHelper(-0.4f, 0.4f, 0f);
	}
	
	
	public void setInitial(ItemCigaretteRenderer item)
	{		
		item.translationX = (float)previousTranslation.xCoord;
		item.translationY = (float)previousTranslation.yCoord;
		item.translationZ = (float)previousTranslation.zCoord;
		
		item.rotationX = (float)previousRotation.xCoord;
		item.rotationY = (float)previousRotation.yCoord;
		item.rotationZ = (float)previousRotation.zCoord;
	}


	


	
}
