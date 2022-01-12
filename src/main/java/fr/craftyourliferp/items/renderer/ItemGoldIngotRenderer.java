package fr.craftyourliferp.items.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import fr.craftyourliferp.blocks.tileentity.renderer.RenderTileEntityGoldIngot;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityCorpseFreezerRenderer;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.main.Main;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ItemGoldIngotRenderer implements IItemRenderer {

	private IModelCustom model;
	
	private ResourceLocation texture;
	
	
	public ItemGoldIngotRenderer(RenderTileEntityGoldIngot renderer)
	{
        model = renderer.model;
        texture = renderer.texture;
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
		 FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		 GL11.glColor4f(1f, 1f, 1f, 1f);
		switch (type) {
		 case ENTITY: {
			 renderItem(0.5F, 2.5F, 0.5F);
			 break;
		 }
		 case EQUIPPED: {
			 renderItemThirdPerson(0.5F, 1.5F, 0.5F);
			 break;
		 }
		 case EQUIPPED_FIRST_PERSON: {
			 renderItemFirstPerson(2.0F, 2.0F, 1.0F);
			 break;
		 }
		 case INVENTORY: {
			 renderItemInInventory(0F, 0.4F, 0F);
			 break;
		 }
		 default:
			 break;
		}
	}
	
	private void renderItem(float x, float y, float z) {
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x, y, z); //size
		 GL11.glScalef(3f, 3f, 3f);

		 GL11.glTranslatef(-0f, -0.4f, 0);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
	}
	
	private void renderItemFirstPerson(float x, float y, float z) {
		 GL11.glColor4f(1f, 1f, 1f, 1f);
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x-0.8f, y, z); //size
		 GL11.glScalef(2.5f, 2.5f, 2.5f);
		 GL11.glRotatef(50, 0, 1, 0);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
		 GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	
	private void renderItemThirdPerson(float x, float y, float z) {
		 GL11.glColor4f(1f, 1f, 1f, 1f);
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x, y, z); //size
		 GL11.glScalef(2.5f, 2.5f, 2.5f);
		 GL11.glRotatef(-50, 0, 1, 0);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
		 GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	
	private void renderItemInInventory(float x, float y, float z) {
		 GL11.glPushMatrix(); //start
		 GL11.glScalef(3f, 3f, 3f);
		 GL11.glTranslatef(x, y, z); //size
		 model.renderAll();
		 GL11.glPopMatrix(); //end
	}
	
	

}
