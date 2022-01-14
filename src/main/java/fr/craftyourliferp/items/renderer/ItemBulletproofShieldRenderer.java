package fr.craftyourliferp.items.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import fr.craftyourliferp.animations.BulletproofShieldAnimation;
import fr.craftyourliferp.animations.BulletproofShieldAnimation.AnimationType;
import fr.craftyourliferp.cosmetics.ModelBird;
import fr.craftyourliferp.cosmetics.ModelMincinno;
import fr.craftyourliferp.cosmetics.ModelSmithyHat;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.ExtendedPlayer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityOtherPlayerMP;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ItemBulletproofShieldRenderer implements IItemRenderer {

	public IModelCustom model;
	public ResourceLocation texture = new ResourceLocation("craftyourliferp","textures/items/BulletproofShield.png");
	public ResourceLocation texture_broken = new ResourceLocation("craftyourliferp","textures/items/BulletproofShield_broken.png");

	public  long deltaTime;
	
	private long oldTime;
	private long newTime;
	
	public float firstPersonTranslationX = 0.4f;
	public float firstPersonTranslationY = -0.8f;
	public float firstPersonTranslationZ = 0.4f;
	
	public float firstPersonRotationX = 0f;
	public float firstPersonRotationY = 0f;
	public float firstPersonRotationZ = 0f;
	
	private BulletproofShieldAnimation firstPersonAnimation = new BulletproofShieldAnimation(AnimationType.FIRST_PERSON);
	
	public ItemBulletproofShieldRenderer()
	{
		
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp","items/models/BulletproofShield.obj"));
	}
	
	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		return true;
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return true;
	}

	@Override
	public void renderItem(ItemRenderType type, ItemStack item, Object... data) {
		 
		 if(item.hasTagCompound() && item.stackTagCompound.getInteger("LifeTime") <= 300)
		 {
			 Minecraft.getMinecraft().getTextureManager().bindTexture(texture_broken);
		 }
		 else
		 {
			 Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		 }
		 
		 switch (type) {
			 case EQUIPPED_FIRST_PERSON:
				 renderEquippedFirstPerson(item);
				 break;
			 case EQUIPPED:
				 renderEquipped(item, (EntityPlayer)data[1]);
				 break;
			 case INVENTORY:
				 renderInventory(item);
				 break;
			 case ENTITY:
				 GL11.glPushMatrix();
				 GL11.glScalef(3f, 3f, 3f);
				 model.renderAll();
				 GL11.glPopMatrix();
				 break;
			 default:
				 break;
	     }
	}
	
	private void renderEquippedFirstPerson(ItemStack is)
	{
		oldTime = newTime;
		newTime = Minecraft.getSystemTime();
		 
		
		deltaTime = newTime - oldTime;
		EntityPlayer player = Minecraft.getMinecraft().thePlayer;
		//PlayerCachedData data = PlayerCachedData.getData(player);
		ExtendedPlayer data = ExtendedPlayer.get(player);
		
		if(data == null) return;
		
		if(data.isUsingItem())
		{
			firstPersonAnimation.setState(0);
			firstPersonAnimation.playAnimation(this,is,player);
		}
		else
		{
			firstPersonAnimation.setState(1);
			firstPersonAnimation.playAnimation(this,is,player);
		}

		
		GL11.glPushMatrix();
		
		GL11.glRotatef(firstPersonRotationY, 0f, 1f, 0f);
		GL11.glTranslatef(firstPersonTranslationX, firstPersonTranslationY, firstPersonTranslationZ);
		
		
		if(data.isProning())
		{
			GL11.glTranslatef(0f, 0.2f, 0f);
			GL11.glRotatef(20f, 0f, 0f, 1f);
		}
		GL11.glScalef(3f, 3.5f, 3f);
		
		model.renderAll();
	
		GL11.glPopMatrix();
	}
	
	private void renderEquipped(ItemStack is, Entity entity) 
	{
		GL11.glPushMatrix();
			

		GL11.glScalef(3.5f, 3.8f, 3.5f);
		if(entity instanceof EntityPlayer)
		{
			EntityPlayer player = (EntityPlayer) entity;
			
			GL11.glRotatef(132, 0, 1, 0);			
			GL11.glRotatef(-40, 0, 0, 1);
			GL11.glRotatef(-1.75f, 1, 0, 0);

			GL11.glTranslatef(-0.3f, -0.5f, -0.14f);
			
		}
		else
		{
			GL11.glRotatef(50, 0, 1, 0);
			GL11.glRotatef(10, 1, 0,0);
			GL11.glRotatef(23, 0, 0, 1);
			GL11.glRotatef(-50, 1, 0, 0);
			GL11.glRotatef(30, 0, 1, 0);
			GL11.glTranslatef(-0.1f, -0.3f, 0.5f);
		}


		model.renderAll();
	
		GL11.glPopMatrix();
	}
	
	
	private void renderInventory(ItemStack is) 
	{
		GL11.glPushMatrix();
			
		GL11.glScalef(1f, 1f, 1f);
		GL11.glTranslatef(-0.05f, -0.24f, 0);
		GL11.glRotatef(-80, 0, 1, 0);
		model.renderAll();
	
		GL11.glPopMatrix();
	}
	
}
