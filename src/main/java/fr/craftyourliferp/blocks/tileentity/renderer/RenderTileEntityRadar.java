package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityRadar;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class RenderTileEntityRadar extends TileEntitySpecialRenderer  {

	private ResourceLocation texture;
	
	private IModelCustom model;
	
	public Minecraft mc;
	
		
	
	public RenderTileEntityRadar()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/ModelRadar.obj"));
		mc = Minecraft.getMinecraft();
        texture = new ResourceLocation("craftyourliferp", "blocks/models/RadarTexture.png");
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTicks) {
		
		TileEntityRadar tile = (TileEntityRadar) te;
		float rotation = tile.rotation;

		GL11.glColor4f(1f, 1f, 1f, 1f);
		bindTexture(texture);


		GL11.glPushMatrix();	
		GL11.glTranslated(posX+0.5D, posY, posZ+0.5D);
		GL11.glScalef(1.2f, 1.2f, 1.2f);
		GL11.glRotatef(rotation, 0.0F, 1.0F, 0.0F);
		model.renderAll();
		GL11.glPopMatrix();
 
        
	}
	


}
