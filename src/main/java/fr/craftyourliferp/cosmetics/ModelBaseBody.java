package fr.craftyourliferp.cosmetics;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.game.events.TicksHandler;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.MinecraftUtils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.renderer.RenderHelper;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;

public class ModelBaseBody extends ModelBase {

	
	
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
	        ExtendedPlayer cachedData = ExtendedPlayer.get(player);
	        //PlayerCachedData cachedData = PlayerCachedData.getData(player);
	        
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
	            	y -= 0.18D;
	            }
	        }
	        else
	        {
	        	if(localPlayer.isSneaking()) y -= 0.05D;
	        }
	        
	        GL11.glTranslated(x, y+0.6f, z);
	        
	        GL11.glScalef(1, -1,-1);
	        
	        
	      

	        
	        GL11.glTranslatef(0, 3F * 0.0625F, 0F);

	        
	        GL11.glTranslatef(0, 0.6F * 0.0625F, 0F);
	        
	        GL11.glTranslatef(0, 6 * 0.0625F, 0F);
	        
	 		if(cachedData.isSleeping())
	 		{
		        GL11.glRotatef(MinecraftUtils.getPlayerYawFromBedDirection(player), 0, 1, 0);

	 			GL11.glRotatef(-90, 1, 0, 0);	
				GL11.glTranslatef(0.0F, 0.18F, 1.6F);
	 		}
	 		else
	 		{
		        GL11.glRotatef(yawOffset, 0, 1, 0);
		        if (player.isSneaking()) {
		            GL11.glRotatef(5.6F, 1, 0, 0);
		            GL11.glTranslatef(0, -0.03F, -0.08F);
		        }
		        
		        
		 		if(cachedData.isProning())
		 		{
		 			GL11.glTranslatef(0, 1.2F, 0);
		 			float anim = (float) (0.05F - Math.sin(TicksHandler.ticks * 0.25041F) * 0.2025F * limbSwingAmount);
		 			GL11.glTranslatef(0f, 0f, anim);
		 		}


		        
		        
		        if(cachedData.currentAnimation == 2)
				{
					GL11.glRotatef(25f, 1f, 0f, 25f);
					GL11.glTranslatef(0.1F, -0.15F, 0F);
				}
	 		}

	        

	    

	 }
	 
	 public void render()
	 {
		 GL11.glEnable(GL12.GL_RESCALE_NORMAL);
	     RenderHelper.enableGUIStandardItemLighting();
	 }
	 
	 public void postRender()
	 {
         RenderHelper.disableStandardItemLighting();
         GL11.glDisable(GL12.GL_RESCALE_NORMAL);
         GL11.glDisable(GL11.GL_BLEND);
	 }
	
}
