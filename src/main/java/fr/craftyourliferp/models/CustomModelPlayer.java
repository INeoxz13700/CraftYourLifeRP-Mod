package fr.craftyourliferp.models;

import org.lwjgl.opengl.GL11;

import com.flansmod.common.guns.ItemGun;

import api.player.model.ModelPlayer;
import api.player.model.ModelPlayerAPI;
import api.player.model.ModelPlayerBase;
import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import fr.craftyourliferp.animations.AnimationManager;
import fr.craftyourliferp.animations.BulletproofShieldAnimation;
import fr.craftyourliferp.animations.BulletproofShieldAnimation.AnimationType;
import fr.craftyourliferp.animations.CoucouAnimation;
import fr.craftyourliferp.animations.PlayerAnimator;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.game.events.TicksHandler;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.renderer.entity.RenderPlayer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.ItemBow;

public class CustomModelPlayer extends ModelPlayerBase 
{

	public static CustomModelPlayer instance;
	
	public float rotationX;
	public float rotationY = 40;
	public float rotationZ;
			
	
	private BulletproofShieldAnimation animation = new BulletproofShieldAnimation(AnimationType.THIRD_PERSON);
	
	public CustomModelPlayer(ModelPlayerAPI api, String modid)
	{
		super(api);
		instance = this;

	}
	
	@Override
	public void setRotationAngles(float paramFloat1, float paramFloat2, float paramFloat3, float paramFloat4, float paramFloat5, float paramFloat6, net.minecraft.entity.Entity paramEntity)
	{
		
		super.setRotationAngles(paramFloat1, paramFloat2, paramFloat3, paramFloat4, paramFloat5, paramFloat6, paramEntity);
		
		
		/*if(paramEntity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) paramEntity;
			
			PlayerCachedData data = PlayerCachedData.getData(player);
			
			if(data != null)
			{
				if(data.isProning())
				{
					//Step1
					//Head
					this.modelPlayer.bipedHeadwear.offsetY = 1.4F;

					this.modelPlayer.bipedHead.offsetY = 1.4F;
					
					//Body
					this.modelPlayer.bipedCloak.rotateAngleX = MathsUtils.Deg2Rad * 275;
					this.modelPlayer.bipedBody.rotateAngleX = MathsUtils.Deg2Rad * 90;
					this.modelPlayer.bipedCloak.offsetY = 0.05F;
					this.modelPlayer.bipedBody.offsetY = 1.4F;
					
					//Legs
					this.modelPlayer.bipedLeftLeg.rotateAngleX = MathsUtils.Deg2Rad * 90;
					this.modelPlayer.bipedRightLeg.rotateAngleX = MathsUtils.Deg2Rad * 90;

			
					this.modelPlayer.bipedLeftLeg.offsetZ = 0.75F;
					this.modelPlayer.bipedLeftLeg.offsetY = 0.7F;

					this.modelPlayer.bipedRightLeg.offsetZ = 0.75F;
					this.modelPlayer.bipedRightLeg.offsetY = 0.7F;

					this.modelPlayer.bipedLeftLeg.rotateAngleY = MathsUtils.Deg2Rad * 10;
					this.modelPlayer.bipedRightLeg.rotateAngleY = MathsUtils.Deg2Rad * -10;
					
					//Arms
					Minecraft mc = Minecraft.getMinecraft();
					if(mc.thePlayer == player && mc.gameSettings.thirdPersonView != 0)
					{
						this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
						this.modelPlayer.bipedRightArm.offsetY = 1.25F;
						
						
						this.modelPlayer.bipedLeftArm.offsetZ = (float) (0.05F - Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						this.modelPlayer.bipedRightArm.offsetZ = (float) (0.05F + Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						

						if(!modelPlayer.aimedBow || player.getHeldItem() == null)
						{
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}
						
					}
					else if(mc.thePlayer != player)
					{
						this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
						this.modelPlayer.bipedRightArm.offsetY = 1.25F;
				
						this.modelPlayer.bipedLeftArm.offsetZ = (float) (0.05F - Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						this.modelPlayer.bipedRightArm.offsetZ = (float) (0.05F + Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						

						if(!modelPlayer.aimedBow || player.getHeldItem() == null)
						{
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}

					}
					else if(mc.currentScreen != null)
					{
						if(player.getHeldItem() == null)
						{
							this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
							this.modelPlayer.bipedRightArm.offsetY = 1.25F;
							
							
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}
						else
						{
							this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
							this.modelPlayer.bipedRightArm.offsetY = 1.25F;

							this.modelPlayer.bipedLeftArm.rotateAngleZ = 0f;
							this.modelPlayer.bipedRightArm.rotateAngleZ = 0f;
							
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}

					}
					else
					{

						resetTransformations();
					}

				}
				else if(data.currentAnimation != 0)
				{
					
					if(data.currentPlayingAnimation != null)
					{
						if(!data.currentPlayingAnimation.getAnimationisPlaying())
						{
							if(data.currentPlayingAnimation != null) 
							{
								resetTransformations();

								data.currentPlayingAnimation.startAnimation(modelPlayer,data);
							}
						}
												
						if(data.currentPlayingAnimation != null) data.currentPlayingAnimation.applyAnimation(modelPlayer,data,player);
					}
				}
				else
				{
					if(data.currentPlayingAnimation != null)
					{
						data.currentPlayingAnimation.stopAnimation(modelPlayer,data);
					}
					resetTransformations();
				}
			}

			if(player.getHeldItem() != null)
			{
				if(player.getHeldItem().getItem() == ModdedItems.extincteur)
				{
					this.modelPlayer.bipedLeftArm.rotateAngleZ = MathsUtils.Deg2Rad * 30;
					this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * -50;
					
					this.modelPlayer.heldItemLeft = 0;
					this.modelPlayer.bipedRightArm.rotateAngleZ = MathsUtils.Deg2Rad * -30;
					this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * -50;
				}
				else if(player.getHeldItem().getItem() == ModdedItems.itemBulletproofShield)
				{
					if(data.isUsingItem())
					{
						animation.setState(0);
						animation.playAnimation(this, player.getHeldItem(), player);
						this.modelPlayer.bipedRightArm.rotateAngleZ = rotationZ * MathsUtils.Deg2Rad;
					}
					else
					{
						this.modelPlayer.bipedRightArm.rotateAngleY = rotationY * MathsUtils.Deg2Rad;

						animation.setInitial(this);
					}

					if(data.isProning())
					{
						this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * -120;
					}
					else
					{
						if(player.rotationPitch <= 70) this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * player.rotationPitch - 10 * 2;
						else this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 5.34f * 2;
					}
					
				}

			}
			
		
		}*/
		
		if(paramEntity instanceof EntityPlayer)
		{
			resetTransformations();
			EntityPlayer player = (EntityPlayer) paramEntity;
			
			ExtendedPlayer data = ExtendedPlayer.get(player);
			
			if(data != null)
			{
				if(data.isProning())
				{
					//Step1
					//Head
					this.modelPlayer.bipedHeadwear.offsetY = 1.4F;

					this.modelPlayer.bipedHead.offsetY = 1.4F;
					
					//Body
					this.modelPlayer.bipedCloak.rotateAngleX = MathsUtils.Deg2Rad * 275;
					this.modelPlayer.bipedBody.rotateAngleX = MathsUtils.Deg2Rad * 90;
					this.modelPlayer.bipedCloak.offsetY = 0.05F;
					this.modelPlayer.bipedBody.offsetY = 1.4F;
					
					//Legs
					this.modelPlayer.bipedLeftLeg.rotateAngleX = MathsUtils.Deg2Rad * 90;
					this.modelPlayer.bipedRightLeg.rotateAngleX = MathsUtils.Deg2Rad * 90;

			
					this.modelPlayer.bipedLeftLeg.offsetZ = 0.75F;
					this.modelPlayer.bipedLeftLeg.offsetY = 0.7F;

					this.modelPlayer.bipedRightLeg.offsetZ = 0.75F;
					this.modelPlayer.bipedRightLeg.offsetY = 0.7F;

					this.modelPlayer.bipedLeftLeg.rotateAngleY = MathsUtils.Deg2Rad * 10;
					this.modelPlayer.bipedRightLeg.rotateAngleY = MathsUtils.Deg2Rad * -10;
					
					//Arms
					Minecraft mc = Minecraft.getMinecraft();
					if(mc.thePlayer == player && mc.gameSettings.thirdPersonView != 0)
					{
						this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
						this.modelPlayer.bipedRightArm.offsetY = 1.25F;
						
						
						this.modelPlayer.bipedLeftArm.offsetZ = (float) (0.05F - Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						this.modelPlayer.bipedRightArm.offsetZ = (float) (0.05F + Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						

						if(!modelPlayer.aimedBow || player.getHeldItem() == null)
						{
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}
						
					}
					else if(mc.thePlayer != player)
					{
						this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
						this.modelPlayer.bipedRightArm.offsetY = 1.25F;
				
						this.modelPlayer.bipedLeftArm.offsetZ = (float) (0.05F - Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						this.modelPlayer.bipedRightArm.offsetZ = (float) (0.05F + Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * paramFloat2);
						

						if(!modelPlayer.aimedBow || player.getHeldItem() == null)
						{
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}

					}
					else if(mc.currentScreen != null)
					{
						if(player.getHeldItem() == null)
						{
							this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
							this.modelPlayer.bipedRightArm.offsetY = 1.25F;
							
							
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}
						else
						{
							this.modelPlayer.bipedLeftArm.offsetY = 1.25F;
							this.modelPlayer.bipedRightArm.offsetY = 1.25F;

							this.modelPlayer.bipedLeftArm.rotateAngleZ = 0f;
							this.modelPlayer.bipedRightArm.rotateAngleZ = 0f;
							
							this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
							this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 270;
						}

					}
					/*else
					{

						resetTransformations();
					}*/

				}
				else if(data.currentAnimation != 0)
				{
					
					if(data.currentPlayingAnimation != null)
					{
						if(!data.currentPlayingAnimation.getAnimationisPlaying())
						{
							if(data.currentPlayingAnimation != null) 
							{
								data.currentPlayingAnimation.startAnimation(modelPlayer,data);
							}
						}
												
						if(data.currentPlayingAnimation != null) data.currentPlayingAnimation.applyAnimation(modelPlayer,data,player);
					}
				}
				else
				{
					if(data.currentPlayingAnimation != null)
					{
						data.currentPlayingAnimation.stopAnimation(modelPlayer,data);
					}
				}
			}

			if(player.getHeldItem() != null)
			{
				if(player.getHeldItem().getItem() == ModdedItems.extincteur)
				{
					this.modelPlayer.bipedLeftArm.rotateAngleZ = MathsUtils.Deg2Rad * 30;
					this.modelPlayer.bipedLeftArm.rotateAngleX = MathsUtils.Deg2Rad * -50;
					
					this.modelPlayer.heldItemLeft = 0;
					this.modelPlayer.bipedRightArm.rotateAngleZ = MathsUtils.Deg2Rad * -30;
					this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * -50;
				}
				else if(player.getHeldItem().getItem() == ModdedItems.itemBulletproofShield)
				{
					if(data.isUsingItem())
					{
						animation.setState(0);
						animation.playAnimation(this, player.getHeldItem(), player);
						this.modelPlayer.bipedRightArm.rotateAngleZ = rotationZ * MathsUtils.Deg2Rad;
					}
					else
					{
						this.modelPlayer.bipedRightArm.rotateAngleY = rotationY * MathsUtils.Deg2Rad;

						animation.setInitial(this);
					}

					if(data.isProning())
					{
						this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * -120;
					}
					else
					{
						if(player.rotationPitch <= 70) this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * player.rotationPitch - 10 * 2;
						else this.modelPlayer.bipedRightArm.rotateAngleX = MathsUtils.Deg2Rad * 5.34f * 2;
					}
					
				}

			}
			
		
		}

			
	}

	
	public void resetTransformations()
	{
		
		//Head
		this.modelPlayer.bipedHeadwear.offsetY = 0f;
		this.modelPlayer.bipedHeadwear.offsetZ = 0f;
		this.modelPlayer.bipedHeadwear.rotateAngleZ = 0f;

		this.modelPlayer.bipedHead.offsetZ = 0f;
		this.modelPlayer.bipedHead.offsetY = 0f;
		this.modelPlayer.bipedHead.offsetX = 0f;
		this.modelPlayer.bipedHead.rotateAngleZ = 0;
		
		
		//Body
		//this.modelPlayer.bipedBody.rotateAngleX = 0f;
		this.modelPlayer.bipedBody.rotateAngleZ = 0;
		this.modelPlayer.bipedBody.offsetY = 0f;
		this.modelPlayer.bipedBody.offsetZ = 0f;
		this.modelPlayer.bipedBody.offsetX = 0f;
		
		
		
		//Legs
		this.modelPlayer.bipedLeftLeg.offsetZ = 0f;
		this.modelPlayer.bipedRightLeg.offsetZ = 0f;
		this.modelPlayer.bipedLeftLeg.offsetY = 0f;
		this.modelPlayer.bipedRightLeg.offsetY = 0f;


		this.modelPlayer.bipedLeftLeg.rotateAngleZ = 0;
		this.modelPlayer.bipedRightLeg.rotateAngleZ = 0;
		
		//Cloak
		this.modelPlayer.bipedCloak.rotateAngleX = 0F;
		this.modelPlayer.bipedCloak.offsetY = 0f;
		this.modelPlayer.bipedCloak.offsetZ = 0f;

		//Arms
		this.modelPlayer.bipedLeftArm.offsetY = 0f;
		this.modelPlayer.bipedRightArm.offsetY = 0f;
		this.modelPlayer.bipedLeftArm.rotateAngleZ = 0f;
		this.modelPlayer.bipedRightArm.rotateAngleZ = 0f;
		this.modelPlayer.bipedLeftArm.offsetZ = 0f;
		this.modelPlayer.bipedRightArm.offsetZ = 0f;
	}
		
}

	

