package fr.craftyourliferp.items.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.main.Main;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ItemTrashCanRenderer implements IItemRenderer {

	private IModelCustom model;
	private ResourceLocation texture;
	
	
	public ItemTrashCanRenderer()
	{
        texture = new ResourceLocation("craftyourliferp","textures/blocks/TrashCan.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/TrashCan.obj"));
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
		switch (type) {
		 case ENTITY: {
			 renderItem(0.5F, 2.5F, 0.5F);
			 break;
		 }
		 case EQUIPPED: {
			 renderItem(0F, 1.5F, 1.0F);
			 break;
		 }
		 case EQUIPPED_FIRST_PERSON: {
			 renderItemFirstPerson(2.0F, 2.0F, 1.0F);
			 break;
		 }
		 case INVENTORY: {
			 renderItemInInventory(0F, -0.7F, 0F);
			 break;
		 }
		 default:
			 break;
		}
	}
	
	private void renderItem(float x, float y, float z) {
		 FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x, y, z); //size
		 GL11.glScalef(3f, 3f, 3f);
		 GL11.glRotatef(150, 0, 1, 0);
		 GL11.glRotatef(-10, 1, 0, 0);

		 GL11.glTranslatef(-0.2f, -0.8f, 0.5f);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
	}
	
	private void renderItemFirstPerson(float x, float y, float z) {
		 FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		 GL11.glColor4f(1f, 1f, 1f, 1f);
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x, y, z); //size
		 GL11.glRotatef(15, 0, 1, 0);
		 GL11.glScalef(2.5f, 2.5f, 2.5f);
		 GL11.glTranslatef(0, -0.8f, 0);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
		 GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	
	private void renderItemInInventory(float x, float y, float z) {
		 FMLClientHandler.instance().getClient().renderEngine.bindTexture(texture);
		 GL11.glPushMatrix(); //start
		 GL11.glScalef(1.0f, 1.0f, 1.0f);
		 GL11.glTranslatef(x, y, z); //size
		 model.renderAll();
		 GL11.glPopMatrix(); //end
	}
	
	

}
