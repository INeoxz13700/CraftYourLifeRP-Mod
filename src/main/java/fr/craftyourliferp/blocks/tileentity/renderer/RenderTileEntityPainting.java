package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class RenderTileEntityPainting extends TileEntitySpecialRenderer  {

	public static ResourceLocation[] texture = new ResourceLocation[TileEntityPainting.paintTypeCount];
	
	public IModelCustom model;
	
	public Minecraft mc;
	

	public RenderTileEntityPainting()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/painting.obj"));
		mc = Minecraft.getMinecraft();
		
		for(int i = 0; i < texture.length; i++)
		{
	        texture[i] = new ResourceLocation("craftyourliferp", "blocks/models/painting_texture_" + i + ".png");

		}
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTicks) {
		
		TileEntityPainting tile = (TileEntityPainting) te;

		GL11.glColor4f(1f, 1f, 1f, 1f);
		bindTexture(texture[tile.type]);


		GL11.glPushMatrix();	
		GL11.glTranslated(posX+0.5D, posY+0.4D, posZ+0.5D);
		GL11.glScalef(1f, 0.76f, 1f);
		GL11.glRotatef(-tile.direction * 90f, 0.0F, 1.0F, 0.0F);
		model.renderAll();
		GL11.glPopMatrix();
 
        
	}
	


}
