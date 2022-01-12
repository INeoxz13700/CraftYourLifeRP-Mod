package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import fr.craftyourliferp.blocks.tileentity.TileEntityCorpseFreezer;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

public class TileEntityCorpseFreezerRenderer extends TileEntitySpecialRenderer {

	public IModelCustom model_open;
	public IModelCustom model_close;
	
	public ResourceLocation texture;
	
	public TileEntityCorpseFreezerRenderer()
	{
		model_close = AdvancedModelLoader.loadModel(new ResourceLocation(CraftYourLifeRPMod.name,"blocks/models/corpse_freezer_closed.obj"));
		model_open = AdvancedModelLoader.loadModel(new ResourceLocation(CraftYourLifeRPMod.name,"blocks/models/corpse_freezer_open.obj"));
		texture = new ResourceLocation("craftyourliferp","textures/blocks/corpse_freezer_texture.png");
	}
	
	@Override
	public void renderTileEntityAt(TileEntity p_147500_1_, double x, double y, double z, float p_147500_8_) 
	{		
		TileEntityCorpseFreezer tileEntity = (TileEntityCorpseFreezer)p_147500_1_;
		
		Minecraft.getMinecraft().getTextureManager().bindTexture(texture);
		
		GL11.glPushMatrix();

		
        if (tileEntity.direction == 0)
        {
    		GL11.glTranslated(x+0.5D, y+0.5D, z+0.55D);
            GL11.glRotatef(360, 0.0F, 1.0F, 0.0F);
        }
        else if (tileEntity.direction == 1)
        {
    		GL11.glTranslated(x+0.45D, y+0.5D, z+0.5D);

            GL11.glRotatef(tileEntity.direction * (-90F), 0.0F, 1.0F, 0.0F);
        }
        else if (tileEntity.direction == 2)
        {
    		GL11.glTranslated(x+0.50D, y+0.5D, z+0.45D);

            GL11.glRotatef(tileEntity.direction * -90, 0.0F, 1.0F, 0.0F);
        }
        else
        {
    		GL11.glTranslated(x+0.55D, y+0.5D, z+0.5D);
            GL11.glRotatef(tileEntity.direction * -90, 0.0F, 1.0F, 0.0F);
        }
		GL11.glScalef(1f, 1f, 0.96f);

        
        GL11.glPushMatrix();
		if(tileEntity.state == 0)
		{
			model_close.renderAll();
		}
		else
		{
			model_open.renderAll();
		}
		GL11.glPopMatrix();

		GL11.glPopMatrix();
		
	}

}
