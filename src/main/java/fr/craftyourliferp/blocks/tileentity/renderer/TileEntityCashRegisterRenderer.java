package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityCashRegister;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class TileEntityCashRegisterRenderer extends TileEntitySpecialRenderer {

	public IModelCustom model;
	
	public ResourceLocation texture;
	
	public TileEntityCashRegisterRenderer(){
        texture = new ResourceLocation("craftyourliferp", "textures/blocks/CashRegister.png");
        model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/CashRegister.obj"));
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTick) {
		TileEntityCashRegister tcashregister = (TileEntityCashRegister)te;
		
		
		GL11.glColor4f(1f, 1f, 1f, 1f);

		bindTexture(texture);

		GL11.glPushMatrix();
		GL11.glTranslated(posX + 0.5, posY, posZ + 0.5);
		GL11.glScalef(-1.5f, 1, -1.5f);
		GL11.glPushMatrix();
		GL11.glRotatef(-(tcashregister.direction * 90), 0F, 1F, 0F);
		model.renderAll();
		GL11.glPopMatrix();
		GL11.glPopMatrix();
	}
	
}
