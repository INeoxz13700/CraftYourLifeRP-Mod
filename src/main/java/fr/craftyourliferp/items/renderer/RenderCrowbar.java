package fr.craftyourliferp.items.renderer;

import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.RenderItem;
import net.minecraft.entity.Entity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import fr.craftyourliferp.models.ModelCrowbar;

public class RenderCrowbar implements IItemRenderer {
	
	protected static final ResourceLocation texture = new ResourceLocation("cylrp:textures/items/models/Crowbar.png");

	
	protected ModelCrowbar model;
	
	public RenderCrowbar()
	{
		this.model = new ModelCrowbar();
	}

	@Override
	public boolean handleRenderType(ItemStack item, ItemRenderType type) {
		switch (type) {
		case ENTITY:
			return true;
		case EQUIPPED:
			return true;
		case EQUIPPED_FIRST_PERSON:
			return true;
		case INVENTORY:
			return false;
		default:
			return false;
		}
	}

	@Override
	public boolean shouldUseRenderHelper(ItemRenderType type, ItemStack item, ItemRendererHelper helper) {
		return false;
	}

	  public void renderItem(IItemRenderer.ItemRenderType type, ItemStack item, Object... data)
	  {
	    switch (type)
	    {
	    case EQUIPPED: 
	      GL11.glPushMatrix();
	      FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
	      GL11.glRotatef(180.0F, 1.00F, 0.0F, 1.00F);
	      GL11.glTranslatef(0.00F, -0.10F, +0.50F);
	      GL11.glScalef(0.9F, 0.9F, 0.9F);
	      this.model.render();
	      GL11.glPopMatrix();
	      break;
	    case EQUIPPED_FIRST_PERSON: 
	      FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
	      GL11.glRotatef(180.0F, 5.50F, 0.0F, 1.00F);
	      GL11.glTranslatef(0.00F, -0.10F, +0.50F);
	      GL11.glScalef(0.9F, 0.9F, 0.9F);
	      this.model.render();
	      break;
	    case ENTITY: 
	      GL11.glPushMatrix();
	      FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
	      if (RenderItem.renderInFrame)
	      {
	        GL11.glScalef(0.7F, 0.7F, 0.7F);
	        GL11.glTranslatef(0.5F, 0.5F, -0.05F);
	        GL11.glRotatef(180.0F, 0.0F, 90.0F, 90.0F);
	      }
	      GL11.glRotatef(90.0F, 0.0F, 0.0F, -90.0F);
	      GL11.glTranslatef(-0.1F, -0.4F, 0.3F);
	      GL11.glScalef(1.8F, 1.8F, 1.8F);
	      this.model.render();
	      GL11.glPopMatrix();
		case FIRST_PERSON_MAP:
			break;
		case INVENTORY:
			break;
		default:
			break;
	    }
	  }
	}
