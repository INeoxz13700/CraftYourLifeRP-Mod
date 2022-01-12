package fr.craftyourliferp.entities.renderer;

import org.lwjgl.opengl.GL11;

import api.player.model.ModelPlayerBase;
import api.player.render.RenderPlayerAPI;
import api.player.render.RenderPlayerBase;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.game.events.TicksHandler;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.models.CustomModelPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;

@SideOnly(Side.CLIENT)
public class RenderCustomPlayer extends RenderPlayerBase  {

	public static RenderCustomPlayer instance;

	
	public RenderCustomPlayer(RenderPlayerAPI arg0) {
		super(arg0);
		instance = this;
	}
	
	
	 public void renderSpecials(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat) {
		 //PlayerCachedData cachedData = PlayerCachedData.getData(paramAbstractClientPlayer);
		 ExtendedPlayer cachedData = ExtendedPlayer.get(paramAbstractClientPlayer);

		 
		 if(cachedData != null && cachedData.isProning())
		 {
	         float f6 = paramAbstractClientPlayer.prevLimbSwingAmount + (paramAbstractClientPlayer.limbSwingAmount - paramAbstractClientPlayer.prevLimbSwingAmount) * paramFloat;
			 GL11.glTranslatef(0f, 1.2f, (float) (0.05F + Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * f6));
		 }
		 super.renderSpecials(paramAbstractClientPlayer, paramFloat);
	 }
	 
	 @Override
	 public void renderFirstPersonArm(EntityPlayer paramEntityPlayer) 
	 {
		 //PlayerCachedData cachedData = PlayerCachedData.getData(paramEntityPlayer);
		 ExtendedPlayer cachedData = ExtendedPlayer.get(paramEntityPlayer);
		 if(Minecraft.getMinecraft().currentScreen != null && cachedData != null)
		 {
			 if(cachedData.isProning())
			 {
				 GL11.glRotatef(90, 1f, 0f, 0f);

				 GL11.glTranslatef(0.4f, -1F, -0.5f);
				 GL11.glScalef(1.2f, 1.2f, 1.2f);
			 }
			 else if(cachedData.isSleeping())
			 {
				 return;
			 }
		 }
		 super.renderFirstPersonArm(paramEntityPlayer);
	 }
	 
	 public void rotatePlayer(AbstractClientPlayer paramAbstractClientPlayer, float paramFloat1, float paramFloat2, float paramFloat3) 
	 {
		 ExtendedPlayer extendedPlayer = ExtendedPlayer.get(paramAbstractClientPlayer);
		 if(paramAbstractClientPlayer.isEntityAlive() && extendedPlayer.isSleeping())
		 {
	            GL11.glRotatef(paramAbstractClientPlayer.getBedOrientationInDegrees(), 0.0F, 1.0F, 0.0F);
	            GL11.glRotatef(90.0F, 0.0F, 0.0F, 1.0F);
	            GL11.glRotatef(270.0F, 0.0F, 1.0F, 0.0F);
		 }
		 else
		 {
			 super.rotatePlayer(paramAbstractClientPlayer, paramFloat1, paramFloat2, paramFloat3);
		 }
	 }
	 
	 public void renderPlayer(AbstractClientPlayer paramAbstractClientPlayer, double paramDouble1, double paramDouble2, double paramDouble3, float paramFloat1, float paramFloat2) {
		 ExtendedPlayer expPlayer = ExtendedPlayer.get(paramAbstractClientPlayer);
		 if(paramAbstractClientPlayer.isEntityAlive() && expPlayer.isSleeping())
		 {
			 super.renderPlayer(paramAbstractClientPlayer, paramDouble1 + (double)paramAbstractClientPlayer.field_71079_bU, paramDouble2, paramDouble3 + (double)paramAbstractClientPlayer.field_71089_bV, paramFloat1, paramFloat2);
		 }
		 else
		 {
			 super.renderPlayer(paramAbstractClientPlayer, paramDouble1, paramDouble2, paramDouble3, paramFloat1, paramFloat2);
		 }
	 }
	 


	


}
