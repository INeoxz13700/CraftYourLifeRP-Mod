package fr.craftyourliferp.animations;

import java.util.HashMap;

public class AnimationManager {
	
	private static HashMap<Integer,Class<? extends PlayerAnimator>> registeredAnimations = new HashMap<Integer,Class<? extends PlayerAnimator>>();
	
	private static int animationsIndex = 0;
	
	public static void registerAnimation(Class<? extends PlayerAnimator> anim)
	{
		animationsIndex++;
		System.out.println("animation " + anim.getName() + " registered with id " + animationsIndex);
		registeredAnimations.put(animationsIndex, anim);
	}
	
	public static PlayerAnimator getAnimation(int animationId) throws InstantiationException, IllegalAccessException
	{
		return (PlayerAnimator) registeredAnimations.get(animationId).newInstance();
	}
	
}
