package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class TileEntityAtmRenderer extends TileEntitySpecialRenderer {

	private IModelCustom model;
	
	private ResourceLocation texture;
	
	public TileEntityAtmRenderer(){
        texture = new ResourceLocation("craftyourliferp", "textures/blocks/ATM.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/ATM.obj"));
	}


	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTick) {
		TileEntityAtm tatm = (TileEntityAtm)te;
		float rotation = tatm.rotation + (timeSinceLastTick / 2F);
		float scale = tatm.scale;
		
		
		GL11.glColor4f(1f, 1f, 1f, 1f);

		bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glTranslated(posX + 0.5, posY, posZ + 0.5);
		GL11.glScalef(scale, scale, scale);
		GL11.glPushMatrix();
		GL11.glRotatef(rotation, 0F, 1F, 0F);
		model.renderAll();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
}
