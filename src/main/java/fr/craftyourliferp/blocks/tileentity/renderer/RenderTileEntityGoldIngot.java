package fr.craftyourliferp.blocks.tileentity.renderer;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.tileentity.TileEntityGoldIngot;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.tileentity.TileEntitySpecialRenderer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.AdvancedModelLoader;
import net.minecraftforge.client.model.IModelCustom;

@SideOnly(Side.CLIENT)
public class RenderTileEntityGoldIngot extends TileEntitySpecialRenderer  {

	public ResourceLocation texture;
	
	public IModelCustom model;
	
	public Minecraft mc;
	

	public RenderTileEntityGoldIngot()
	{
		model = AdvancedModelLoader.loadModel(new ResourceLocation("craftyourliferp", "blocks/models/goldIngot.obj"));
		texture = new ResourceLocation("craftyourliferp","textures/blocks/goldIngot.png");
		mc = Minecraft.getMinecraft();
		
	
	}

	@Override
	public void renderTileEntityAt(TileEntity te, double posX, double posY, double posZ, float timeSinceLastTicks) 
	{
		TileEntityGoldIngot tile = (TileEntityGoldIngot) te;

		GL11.glColor4f(1f, 1f, 1f, 1f);
		bindTexture(texture);


		GL11.glPushMatrix();	
		GL11.glTranslated(posX+0.5D, posY+0.5D, posZ+0.5D);
		GL11.glScalef(1.5f, 1f, 1.5f);
		GL11.glRotatef(-tile.direction * 90f, 0.0F, 1.0F, 0.0F);
		model.renderAll();
		GL11.glPopMatrix();   
	}
	


}
