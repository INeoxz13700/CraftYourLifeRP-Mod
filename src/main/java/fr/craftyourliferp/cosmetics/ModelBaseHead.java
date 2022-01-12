package fr.craftyourliferp.cosmetics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelBiped;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ModelBaseHead extends ModelBase {

	 
	 protected float modelScaleX;
	 protected float modelScaleY;
	 protected float modelScaleZ;
	 
	 protected float modelTranslationX;
	 protected float modelTranslationY;
	 protected float modelTranslationZ;
	 
	 protected float sleepingModifierY;

	
	 public void setRotationAngle(ModelRenderer modelRenderer, float x, float y, float z) {
		modelRenderer.rotateAngleX = x;
		modelRenderer.rotateAngleY = y;
		modelRenderer.rotateAngleZ = z;
	 }
	 
	@Override
	public void setRotationAngles(float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks, Entity entity)
	{
		super.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
	}
	 
	@Override
	public void render(Entity entity, float limbSwing, float limbSwingAmount, float rotFloat, float rotYaw, float rotPitch, float partTicks) {


		super.setRotationAngles(limbSwing, limbSwingAmount, rotFloat, rotYaw, rotPitch, partTicks, entity);
		
		EntityPlayer player = (EntityPlayer) entity;

		//PlayerCachedData cachedData = PlayerCachedData.getData(player);
		ExtendedPlayer cachedData = ExtendedPlayer.get(player);
	        
	        
		float yawHead = player.prevRotationYawHead + (player.rotationYawHead - player.prevRotationYawHead) * partTicks;
		float pitch = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * partTicks;
		float yawOffset = player.prevRenderYawOffset + (player.renderYawOffset - player.prevRenderYawOffset) * partTicks;

		EntityPlayer localPlayer = Minecraft.getMinecraft().thePlayer;
		EntityPlayer renderTarget = player;
	        
		double tarX1 = renderTarget.prevPosX + (renderTarget.posX - renderTarget.prevPosX) * partTicks;
		double tarX2 = localPlayer.prevPosX + (localPlayer.posX - localPlayer.prevPosX) * partTicks;
	        
		double tarY1 = renderTarget.prevPosY + (renderTarget.posY - renderTarget.prevPosY) * partTicks;
		double tarY2 = localPlayer.prevPosY + (localPlayer.posY - localPlayer.prevPosY) * partTicks;
	        
		double tarZ1 = renderTarget.prevPosZ + (renderTarget.posZ - renderTarget.prevPosZ) * partTicks;
		double tarZ2 = localPlayer.prevPosZ + (localPlayer.posZ - localPlayer.prevPosZ) * partTicks;
	        
		double x = tarX1 - tarX2;
		double y = tarY1 - tarY2;
		double z = tarZ1 - tarZ2;
	     
	        
		if (localPlayer.getUniqueID() != renderTarget.getUniqueID()) {
			y += 26D * 0.0625F;
			
			if(renderTarget.isSneaking())
			{
				y-= 0.18D;
			}
		}
		else
		{
			if(localPlayer.isSneaking()) y-=0.03D;
		}


		GL11.glTranslated(x, y, z);
	     
	        
		GL11.glScalef(1, -1, -1);

		
		GL11.glTranslatef(0, 3F * 0.0625F, 0F);

	        
		GL11.glTranslatef(0, 0.6F * 0.0625F, 0);
		
		if(cachedData.isProning())
		{
			GL11.glTranslated(0F, 1.31D, 0F);
		}
	
		//ExtendedPlayer expPlayer = ExtendedPlayer.get(player);

		if(!cachedData.isSleeping())
		{
			GL11.glRotatef(yawHead, 0.0F, 1.0F, 0.0F);
			
			GL11.glRotatef(pitch, 1F, 0F, 0F);
		}
		else
		{

			GL11.glRotatef(yawHead+180, 0, 1, 0);	
			GL11.glRotatef(-90, 1, 0, 0);	
			GL11.glTranslatef(0.0F, -0.4F, 1.4F);
			GL11.glRotatef(yawOffset - MinecraftUtils.getPlayerYawFromBedDirection(player), 0f, 1f, 0f);
			GL11.glRotatef(180F, 0, 0, 1);

		}

	 }
	
	 public void render()
	 {
		 GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	     RenderHelper.enableGUIStandardItemLighting();
	 }
	 
	
}
