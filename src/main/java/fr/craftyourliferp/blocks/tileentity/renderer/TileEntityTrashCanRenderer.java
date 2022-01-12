package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityTrashCan;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class TileEntityTrashCanRenderer extends TileEntitySpecialRenderer {

	private IModelCustom model;
	
	private ResourceLocation texture;
	
	public TileEntityTrashCanRenderer(){
        texture = new ResourceLocation("craftyourliferp", "textures/blocks/TrashCan.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/TrashCan.obj"));
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTick) {
		TileEntityTrashCan ttb = (TileEntityTrashCan)te;
		float rotation = ttb.rotation + (timeSinceLastTick / 2F);
		float scale = ttb.scale;
		
		
		GL11.glColor4f(1f, 1f, 1f, 1f);

		bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glTranslated(posX + 0.5, posY, posZ + 0.5);
		GL11.glScalef(scale, scale, scale);
		GL11.glPushMatrix();
		GL11.glRotatef(rotation+90, 0F, 1F, 0F);
		model.renderAll();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
}
