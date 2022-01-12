package fr.craftyourliferp.entities.renderer;

import org.lwjgl.opengl.GL11;

import com.mojang.authlib.GameProfile;

import fr.craftyourliferp.entities.EntityLootableBody;
import fr.craftyourliferp.models.ModelPlayerCorpse;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.entity.RenderBiped;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.common.MinecraftForge;

public class RenderSkinnedBody extends RenderBiped {
	

	public RenderSkinnedBody(RenderManager rm) {
		super(new ModelPlayerCorpse(), 0.5f);
	}
	
	@Override
	public void doRender(final EntityLiving p_doRender_1_, final double p_doRender_2_, final double p_doRender_4_,final double p_doRender_6_, final float p_doRender_8_, final float p_doRender_9_) {

		double d3 = p_doRender_4_ - p_doRender_1_.yOffset;
		doPlayerRender((EntityLootableBody) p_doRender_1_, p_doRender_2_, d3, p_doRender_6_, p_doRender_8_,p_doRender_9_);
	}
	
	
	public void doLivingRender(final EntityLivingBase p_doRender_1_, final double p_doRender_2_,final double p_doRender_4_, final double p_doRender_6_, final float p_doRender_8_,final float p_doRender_9_) throws Exception {
		if (MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Pre(p_doRender_1_, this, p_doRender_2_, p_doRender_4_, p_doRender_6_))) {
			return;
		}
		GL11.glPushMatrix();
		GL11.glDisable(2884);


		this.mainModel.isRiding = p_doRender_1_.isRiding();
		if (this.renderPassModel != null) {
			this.renderPassModel.isRiding = this.mainModel.isRiding;
		}
		this.mainModel.isChild = p_doRender_1_.isChild();
		if (this.renderPassModel != null) {
			this.renderPassModel.isChild = this.mainModel.isChild;
		}

		try {
			float f2 = this.interpolateRotation(p_doRender_1_.prevRenderYawOffset, p_doRender_1_.renderYawOffset,p_doRender_9_);
			
			f2 = this.interpolateRotation(p_doRender_1_.prevRotationYawHead, p_doRender_1_.rotationYawHead,p_doRender_9_);
						
			if (p_doRender_1_.isRiding() && p_doRender_1_.ridingEntity instanceof EntityLivingBase)
			{
				final EntityLivingBase entitylivingbase1 = (EntityLivingBase) p_doRender_1_.ridingEntity;
				
				f2 = this.interpolateRotation(entitylivingbase1.prevRenderYawOffset, entitylivingbase1.renderYawOffset,p_doRender_9_);
				
				float f3 = MathHelper.wrapAngleTo180_float(f2 - f2);
				if (f3 < -85.0f) {
					f3 = -85.0f;
				}
				if (f3 >= 85.0f) {
					f3 = 85.0f;
				}
				f2 -= f3;
				if (f3 * f3 > 2500.0f) {
					f2 += f3 * 0.2f;
				}
			}
			
			final float f4 = p_doRender_1_.prevRotationPitch + (p_doRender_1_.rotationPitch - p_doRender_1_.prevRotationPitch) * p_doRender_9_;
			
			this.renderLivingAt(p_doRender_1_, p_doRender_2_, p_doRender_4_, p_doRender_6_);
			
			float f3 = this.handleRotationFloat(p_doRender_1_, p_doRender_9_);
			
			this.rotateCorpse(p_doRender_1_, f3, f2, p_doRender_9_);
			
			final float f5 = 0.0625f;
			
			GL11.glEnable(32826);
			GL11.glScalef(-1.0f, -1.0f, 1.0f);
			this.preRenderCallback(p_doRender_1_, p_doRender_9_);
			
			GL11.glTranslatef(0.0f, -24.0f * f5 - 0.0078125f, 0.0f);
			
			float f6 = p_doRender_1_.prevLimbSwingAmount + (p_doRender_1_.limbSwingAmount - p_doRender_1_.prevLimbSwingAmount) * p_doRender_9_;
			
			float f7 = p_doRender_1_.limbSwing - p_doRender_1_.limbSwingAmount * (1.0f - p_doRender_9_);
			
			if (p_doRender_1_.isChild())
			{
				f7 *= 3.0f;
			}
			if (f6 > 1.0f) 
			{
				f6 = 1.0f;
			}
			
			GL11.glEnable(3008);

			// rotate face-down
			GL11.glColor3f(0.8f, 0.8f, 0.8f);
			GL11.glRotatef(90f, 1f, 0f, 0f);
			GL11.glTranslatef(0f, -0.5f, -1.375f);

			this.mainModel.setLivingAnimations(p_doRender_1_, f7, f6, p_doRender_9_);
			
			this.renderModel(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
			
			for (int i = 0; i < 4; ++i)
			{
				final int j = this.shouldRenderPass(p_doRender_1_, i, p_doRender_9_);
				if (j > 0)
				{
					this.renderPassModel.setLivingAnimations(p_doRender_1_, f7, f6, p_doRender_9_);
					this.renderPassModel.render(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
					if ((j & 0xF0) == 0x10) 
					{
						this.func_82408_c(p_doRender_1_, i, p_doRender_9_);
						this.renderPassModel.render(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
					}
					if ((j & 0xF) == 0xF) 
					{
						final float f8 = p_doRender_1_.ticksExisted + p_doRender_9_;
						GL11.glEnable(3042);
						final float f9 = 0.5f;
						GL11.glColor4f(f9, f9, f9, 1.0f);
						GL11.glDepthFunc(514);
						GL11.glDepthMask(false);
						for (int k = 0; k < 2; ++k) {
							GL11.glDisable(2896);
							final float f10 = 0.76f;
							GL11.glColor4f(0.5f * f10, 0.25f * f10, 0.8f * f10, 1.0f);
							GL11.glBlendFunc(768, 1);
							GL11.glMatrixMode(5890);
							GL11.glLoadIdentity();
							final float f11 = f8 * (0.001f + k * 0.003f) * 20.0f;
							final float f12 = 0.33333334f;
							GL11.glScalef(f12, f12, f12);
							GL11.glRotatef(30.0f - k * 60.0f, 0.0f, 0.0f, 1.0f);
							GL11.glTranslatef(0.0f, f11, 0.0f);
							GL11.glMatrixMode(5888);
							this.renderPassModel.render(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
						}
						GL11.glColor4f(1.0f, 1.0f, 1.0f, 1.0f);
						GL11.glMatrixMode(5890);
						GL11.glDepthMask(true);
						GL11.glLoadIdentity();
						GL11.glMatrixMode(5888);
						GL11.glEnable(2896);
						GL11.glDisable(3042);
						GL11.glDepthFunc(515);
					}
					GL11.glDisable(3042);
					GL11.glEnable(3008);
				}
			}
			GL11.glDepthMask(true);
			this.renderEquippedItems(p_doRender_1_, p_doRender_9_);
			final float f13 = p_doRender_1_.getBrightness(p_doRender_9_);
			final int j = this.getColorMultiplier(p_doRender_1_, f13, p_doRender_9_);
			OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
			GL11.glDisable(3553);
			OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
			if ((j >> 24 & 0xFF) > 0 || p_doRender_1_.hurtTime > 0 || p_doRender_1_.deathTime > 0) {
				GL11.glDisable(3553);
				GL11.glDisable(3008);
				GL11.glEnable(3042);
				GL11.glBlendFunc(770, 771);
				GL11.glDepthFunc(514);
				if (p_doRender_1_.hurtTime > 0 || p_doRender_1_.deathTime > 0) {
					GL11.glColor4f(f13, 0.0f, 0.0f, 0.4f);
					this.mainModel.render(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
					for (int l = 0; l < 4; ++l) {
						if (this.inheritRenderPass(p_doRender_1_, l, p_doRender_9_) >= 0) {
							GL11.glColor4f(f13, 0.0f, 0.0f, 0.4f);
							this.renderPassModel.render(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
						}
					}
				}
				if ((j >> 24 & 0xFF) > 0) {
					final float f8 = (j >> 16 & 0xFF) / 255.0f;
					final float f9 = (j >> 8 & 0xFF) / 255.0f;
					final float f14 = (j & 0xFF) / 255.0f;
					final float f10 = (j >> 24 & 0xFF) / 255.0f;
					GL11.glColor4f(f8, f9, f14, f10);
					this.mainModel.render(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
					for (int i2 = 0; i2 < 4; ++i2) {
						if (this.inheritRenderPass(p_doRender_1_, i2, p_doRender_9_) >= 0) {
							GL11.glColor4f(f8, f9, f14, f10);
							this.renderPassModel.render(p_doRender_1_, f7, f6, f3, f2 - f2, f4, f5);
						}
					}
				}
				GL11.glDepthFunc(515);
				GL11.glDisable(3042);
				GL11.glEnable(3008);
				GL11.glEnable(3553);
			}
			GL11.glDisable(32826);
		} catch (Exception exception) 
		{
			throw new Exception("Couldn't render entity");
		}
		OpenGlHelper.setActiveTexture(OpenGlHelper.lightmapTexUnit);
		GL11.glEnable(3553);
		OpenGlHelper.setActiveTexture(OpenGlHelper.defaultTexUnit);
		GL11.glEnable(2884);

		GL11.glPopMatrix();
		
		this.passSpecialRender(p_doRender_1_, p_doRender_2_, p_doRender_4_, p_doRender_6_);
		
		MinecraftForge.EVENT_BUS.post(new RenderLivingEvent.Post(p_doRender_1_, this, p_doRender_2_, p_doRender_4_, p_doRender_6_));
	}
	
	
	@Override
	protected void renderModel(final EntityLivingBase p_renderModel_1_, final float p_renderModel_2_,final float p_renderModel_3_, final float p_renderModel_4_, final float p_renderModel_5_,final float p_renderModel_6_, final float p_renderModel_7_) 
	{
		this.bindTexture(this.getEntityTexture(p_renderModel_1_));
		
		if (!p_renderModel_1_.isInvisible()) 
		{
			this.mainModel.render(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_,p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
		} 
		else if (!p_renderModel_1_.isInvisibleToPlayer(Minecraft.getMinecraft().thePlayer))
		{
			GL11.glPushMatrix();
			GL11.glColor4f(1.0f, 1.0f, 1.0f, 0.15f);
			GL11.glDepthMask(false);
			GL11.glEnable(3042);
			GL11.glBlendFunc(770, 771);
			GL11.glAlphaFunc(516, 0.003921569f);
			this.mainModel.render(p_renderModel_1_, p_renderModel_2_, p_renderModel_3_, p_renderModel_4_,
					p_renderModel_5_, p_renderModel_6_, p_renderModel_7_);
			GL11.glDisable(3042);
			GL11.glAlphaFunc(516, 0.1f);
			GL11.glPopMatrix();
			GL11.glDepthMask(true);
		} else 
		{
			this.mainModel.setRotationAngles(p_renderModel_2_, p_renderModel_3_, p_renderModel_4_, p_renderModel_5_,p_renderModel_6_, p_renderModel_7_, p_renderModel_1_);
		}
	}
	
	public void doPlayerRender(final EntityLootableBody body, final double p_doRender_2_, final double p_doRender_4_,final double p_doRender_6_, final float p_doRender_8_, final float p_doRender_9_)
	{
		GL11.glColor3f(1.0f, 1.0f, 1.0f);
		final ItemStack itemstack = body.getHeldItem();
		final ModelBiped modelBipedMain = this.modelBipedMain;
		final boolean heldItemRight;
		final boolean b = heldItemRight = ((itemstack != null) ? 1 : 0) != 0;
		modelBipedMain.heldItemRight = b ? 1 : 0;
		if (itemstack != null) 
		{
			final EnumAction enumaction = itemstack.getItemUseAction();
			if (enumaction == EnumAction.block) {
				final ModelBiped modelBipedMain2 = this.modelBipedMain;
				final int heldItemRight2 = 3;
				modelBipedMain2.heldItemRight = heldItemRight2;
			} 
			else if (enumaction == EnumAction.bow) 
			{
				final ModelBiped modelBipedMain3 = this.modelBipedMain;
				final boolean aimedBow = true;
				modelBipedMain3.aimedBow = aimedBow;
			}
		}
		final ModelBiped modelBipedMain4 = this.modelBipedMain;
		
		final boolean sneaking = body.isSneaking();
		
		modelBipedMain4.isSneak = sneaking;
		
		double d3 = p_doRender_4_ - body.yOffset;

		try {
			doLivingRender(body, p_doRender_2_, d3, p_doRender_6_, p_doRender_8_, p_doRender_9_);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		final ModelBiped modelBipedMain5 = this.modelBipedMain;
		
		final boolean aimedBow2 = false;
		
		modelBipedMain5.aimedBow = aimedBow2;

		final ModelBiped modelBipedMain6 = this.modelBipedMain;
		
		final boolean isSneak = false;
		
		modelBipedMain6.isSneak = isSneak;
	
		final ModelBiped modelBipedMain7 = this.modelBipedMain;
		
		final boolean heldItemRight3 = false;
		
		modelBipedMain7.heldItemRight = heldItemRight3 ? 1 : 0;
	}
	


	private ResourceLocation getTexture(final EntityLootableBody e) {
		
		final Minecraft minecraft = Minecraft.getMinecraft();
		
		if(e.getOwner() == null)
		{
			return AbstractClientPlayer.locationStevePng;
		}
		
		return ((AbstractClientPlayer)e.getOwner()).getLocationSkin();
	}

	private float interpolateRotation(final float p_interpolateRotation_1_, final float p_interpolateRotation_2_,final float p_interpolateRotation_3_) {
		float f3;
		for (f3 = p_interpolateRotation_2_ - p_interpolateRotation_1_; f3 < -180.0f; f3 += 360.0f) {
		}
		while (f3 >= 180.0f) {
			f3 -= 360.0f;
		}
		return p_interpolateRotation_1_ + p_interpolateRotation_3_ * f3;
	}

	@Override
	protected ResourceLocation getEntityTexture(final EntityLiving e) {
		return this.getTexture((EntityLootableBody) e);
	}

	protected ResourceLocation getEntityTexture(final EntityLivingBase e) {
		return this.getTexture((EntityLootableBody) e);
	}


	
}
