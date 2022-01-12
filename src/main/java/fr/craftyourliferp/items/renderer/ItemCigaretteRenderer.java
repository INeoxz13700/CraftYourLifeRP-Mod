package fr.craftyourliferp.items.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.registry.RenderingRegistry;
import fr.craftyourliferp.animations.SmokingAnimation;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.ItemRenderer;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraftforge.client.IItemRenderer;

public class ItemCigaretteRenderer implements IItemRenderer {
	
	public static int renderId;
	
    private SmokingAnimation animation = new SmokingAnimation();
	
	public float translationX = 0.5f;
	public float translationY = 0f;
	public float translationZ = 0.5f;
	
	public float rotationX;
	public float rotationY = 90f;
	public float rotationZ;
	
	public long deltaTime;
	
	private long oldTime;
	private long newTime;


	

	public ItemCigaretteRenderer()
	{
		renderId = RenderingRegistry.getNextAvailableRenderId();
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return (type == ItemRenderType.EQUIPPED_FIRST_PERSON) ? true : false;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return (type == ItemRenderType.EQUIPPED_FIRST_PERSON) ? true : false;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		oldTime = newTime;
		newTime = Minecraft.getMinecraft().getSystemTime();
		deltaTime = newTime - oldTime;
		
		switch (type) 
		{
			 case EQUIPPED_FIRST_PERSON:
				 renderEquippedFirstPerson(item,0F, 0F, 0F);
				 break;
			 default:
				 break;
	     }
	}
	

	
	
	private void renderEquippedFirstPerson(ItemStack is, float x, float y, float z) {
		Item item = is.getItem();
		
		IIcon icon = item.getIcon(is, 0);
		

		Minecraft mc = Minecraft.getMinecraft();
		
		mc.renderEngine.bindTexture(TextureMap.locationItemsTexture);
		
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		if(player.isEating())
		{
			animation.playAnimation(this,is,player);
			GL11.glPushMatrix();
			
			GL11.glScalef(2f, 2f, 2f);
			
			GL11.glTranslatef(translationX, translationY, translationZ);
			
			GL11.glRotatef(rotationY, 0, 1, 0);
			GL11.glRotatef(rotationZ, 0, 0, 1);
			GL11.glRotatef(rotationX, 1, 0, 0);

			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), 255, 255, 0.0625F);
	
			GL11.glPopMatrix();
		}
		else
		{
			if(animation.animationIsPlaying) animation.setInitial(this);
			animation.animationIsPlaying = false;
			GL11.glPushMatrix();
	
			GL11.glScalef(2f, 2f, 2f);
			GL11.glTranslatef(translationX, translationY, translationZ);
			GL11.glRotatef(rotationX, 1, 0, 0);
			GL11.glRotatef(rotationY, 0, 1, 0);
			GL11.glRotatef(rotationZ, 0, 0, 1);
			ItemRenderer.renderItemIn2D(Tessellator.instance, icon.getMaxU(), icon.getMinV(), icon.getMinU(), icon.getMaxV(), 255, 255, 0.0625F);
	
			GL11.glPopMatrix();

		}
	}
	
	
}