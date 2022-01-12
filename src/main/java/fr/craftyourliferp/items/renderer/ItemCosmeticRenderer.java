package fr.craftyourliferp.items.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import fr.craftyourliferp.cosmetics.ModelAureole;
import fr.craftyourliferp.cosmetics.ModelBaseBody;
import fr.craftyourliferp.cosmetics.ModelBaseHead;
import fr.craftyourliferp.cosmetics.ModelBird;
import fr.craftyourliferp.cosmetics.ModelMarioHat;
import fr.craftyourliferp.cosmetics.ModelMincinno;
import fr.craftyourliferp.cosmetics.ModelNoelBonnet;
import net.minecraft.client.model.ModelBase;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.IItemRenderer;
import net.minecraftforge.client.IItemRenderer.ItemRenderType;
import net.minecraftforge.client.IItemRenderer.ItemRendererHelper;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class ItemCosmeticRenderer implements IItemRenderer {

	private ModelBase modelbase;
	
	private Vec3 position;
	private Vec3 scale;
	private Vec3 rotation;
	
	
	public ItemCosmeticRenderer(ModelBase cosmeticModel, Vec3 position, Vec3 scale, Vec3 rotation)
	{
		modelbase = cosmeticModel;
		this.position = position;
		this.scale = scale;
		this.rotation = rotation;
		if(rotation == null)
		{
			rotation = Vec3.createVectorHelper(0,0,0);
		}
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
		 case INVENTORY: {
			 renderItemInInventory();
			 break;
		 }
		 default:
			 break;
		}
	}
	
	private void renderItemInInventory() {
		 GL11.glPushMatrix();
		 GL11.glRotated(rotation.xCoord, 1f, 0f, 0f);
		 GL11.glRotated(rotation.yCoord, 0, 1f, 0f);
		 GL11.glRotated(rotation.zCoord, 0f, 0f, 1f);
		 GL11.glScaled(scale.xCoord, scale.yCoord, scale.zCoord);
		 GL11.glTranslated(position.xCoord, position.yCoord, position.zCoord);
		 
		 if(modelbase instanceof ModelBaseHead)
		 {
			 ModelBaseHead modelBaseHead = (ModelBaseHead) modelbase;
			 modelBaseHead.render();
		 }
		 else if(modelbase instanceof ModelBaseBody)
		 {
			 ModelBaseBody modelBaseBody = (ModelBaseBody) modelbase;
			 modelBaseBody.render();
		 }
		 
		 GL11.glPopMatrix(); //end
	}
}
