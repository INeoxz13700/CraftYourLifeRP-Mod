package fr.craftyourliferp.items.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import fr.craftyourliferp.blocks.tileentity.renderer.RenderTileEntityPainting;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityRoadBaliseRenderer;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.main.Main;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ItemPaintingRenderer implements IItemRenderer {

	private IModelCustom model;
	
	
	public ItemPaintingRenderer(RenderTileEntityPainting renderer)
	{
        model = renderer.model;
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
		
		if(item.hasTagCompound())
		{
			NBTTagCompound compound = item.stackTagCompound;
			if(compound.hasKey("PaintingType"))
			{
				byte paintingType = compound.getByte("PaintingType");
				FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderTileEntityPainting.texture[paintingType]);
			}
		}
		else
		{
			FMLClientHandler.instance().getClient().renderEngine.bindTexture(RenderTileEntityPainting.texture[0]);
		}
		
		switch (type) 
		{
		 case ENTITY: 
		 {
			 renderItem(0.5F, 0F, 0.5F);
			 break;
		 }
		 case EQUIPPED: 
		 {
			 renderItem(0F, 0F,0F);
			 break;
		 }
		 case EQUIPPED_FIRST_PERSON: 
		 {
			 renderItemFirstPerson(0.0F, 0.0F, 0.0F);
			 break;
		 }
		 case INVENTORY: 
		 {
			 renderItemInInventory(0.0F, -0.5F, 0F);
			 break;
		 }
		 default:
			 break;
		}
	}
	
	private void renderItem(float x, float y, float z) {
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x, y, z); //size
		 GL11.glScalef(2f, 2f, 2f);
		 GL11.glRotatef(80, 0, 1, 0);
		 GL11.glRotatef(-10, 1, 0, 0);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
	}
	
	private void renderItemFirstPerson(float x, float y, float z) {
		 GL11.glColor4f(1f, 1f, 1f, 1f);
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x, y, z); //size
		 GL11.glRotatef(15, 0, 1, 0);
		 GL11.glScalef(2f, 2f, 2f);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
		 GL11.glColor4f(1f, 1f, 1f, 1f);
	}
	
	
	private void renderItemInInventory(float x, float y, float z) {
		 GL11.glPushMatrix(); //start
		 GL11.glTranslatef(x, y, z); //size
		 GL11.glScalef(1f, 1f, 1f);
		 GL11.glRotatef(220f, 0, 1, 0);
		 model.renderAll();
		 GL11.glPopMatrix(); //end
	}
	
	

}
