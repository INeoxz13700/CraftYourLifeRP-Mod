package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityRoadBalise;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class TileEntityRoadBaliseRenderer extends TileEntitySpecialRenderer {

	public IModelCustom model;
	
	public ResourceLocation texture;
	
	public TileEntityRoadBaliseRenderer(){
        texture = new ResourceLocation("craftyourliferp", "textures/blocks/RoadBalise.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/RoadBalise.obj"));
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTick) {
		TileEntityRoadBalise trb = (TileEntityRoadBalise)te;
		
		
		GL11.glColor4f(1f, 1f, 1f, 1f);

		bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glTranslated(posX + 0.5, posY, posZ + 0.5);
		GL11.glScalef(1.2f, 1, 1.2f);
		GL11.glPushMatrix();
		GL11.glRotatef(trb.direction * 90, 0F, 1F, 0F);
		model.renderAll();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
}
