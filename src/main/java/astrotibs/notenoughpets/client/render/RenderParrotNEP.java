package astrotibs.notenoughpets.client.render;

import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import astrotibs.notenoughpets.entity.EntityParrotNEP;
import astrotibs.notenoughpets.util.SkinVariations;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * Pasted in from 1.12
 */
@SideOnly(Side.CLIENT)
public class RenderParrotNEP extends RenderLiving
{
	//public RenderParrotNEC(RenderManager rendermanagerIn)
	public RenderParrotNEP(ModelBase baseModel, float shadowSize)
	{
        //super(rendermanagerIn, new ModelParrotNEC(0), 0.3F);
        super(baseModel, shadowSize);
        //this.setRenderPassModel(passModel);
	}
	
	
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity parrot)
    {
        return this.getEntityTexture((EntityParrotNEP)parrot);
    }
    
    protected ResourceLocation getEntityTexture(EntityParrotNEP parrot)
    {
    	// REMEMBER to change the rendering if conditions in ShoulderRiding.determineShoulderVariantInt !!!
    	
		if (parrot.getCustomNameTag().equals("Duster")) // Showbird born in 1925
		{
			return SkinVariations.parrotSkinArray[6];
		}
		else if (parrot.getCustomNameTag().equals("Peaches")) // Erin's lovebird
		{
			return SkinVariations.parrotSkinArray[8];
		}
		else if (parrot.getCustomNameTag().equals("Rex")) // MineKynoMine's rainbow lorikeet
		{
			return SkinVariations.parrotSkinArray[12];
		}
		
		// If there are no manual overrides, refer to the parrot's skin int
		return SkinVariations.parrotSkinArray[parrot.getVariant() % SkinVariations.parrotSkinArray.length];
    }

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     */
    @Override
    public float handleRotationFloat(EntityLivingBase livingBase, float partialTicks)
    {
        return this.getCustomBob((EntityParrotNEP)livingBase, partialTicks);
    }
    
    private float getCustomBob(EntityParrotNEP parrot, float partialAnimationTicks)
    {
        float f = parrot.oFlap + (parrot.flap - parrot.oFlap) * partialAnimationTicks;
        float f1 = parrot.oFlapSpeed + (parrot.flapSpeed - parrot.oFlapSpeed) * partialAnimationTicks;
        return (MathHelper.sin(f) + 1.0F) * f1;
    }

	// v2.0.0 - Scale shadow based on entity size
	@Override
	public void doRenderShadowAndFire(Entity entityIn, double x, double y, double z, float yaw, float partialTicks)
	{
        if (this.renderManager.options.fancyGraphics && this.shadowSize > 0.0F && !entityIn.isInvisible())
        {
            double d0 = this.renderManager.getDistanceToCamera(entityIn.posX, entityIn.posY, entityIn.posZ);
            float f = (float)((1.0D - d0 / 256.0D) * (double)this.shadowOpaque);
            
            if (f > 0.0F)
            {
            	this.renderShadow((EntityParrotNEP)entityIn, x, y, z, f, partialTicks);
            }
        }
        
        if (entityIn.canRenderOnFire())
        {
        	Method renderEntityOnFire_reflected = ReflectionHelper.findMethod(Render.class, this, new String[]{"renderEntityOnFire", "func_76977_a"}, Entity.class, Double.TYPE, Double.TYPE, Double.TYPE, Float.TYPE);
        	
        	try
        	{
        		renderEntityOnFire_reflected.invoke(this, entityIn, x, y, z, partialTicks);
			}
        	catch (Exception e) {}
        }
	}
	
    private void renderShadow(EntityParrotNEP entityIn, double x, double y, double z, float shadowAlpha, float partialTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.renderManager.renderEngine.bindTexture((ResourceLocation)ReflectionHelper.getPrivateValue(Render.class, this, new String[]{"shadowTextures", "field_110778_a"}));
        World world = this.renderManager.worldObj;
        GL11.glDepthMask(false);
        float f2 = this.shadowSize;

        f2 *= entityIn.getRenderSizeModifier();
        
        // Scale based on breed type and age
        f2 *= entityIn.isChild() ? 1F-0.5F*(((float)((EntityParrotNEP)entityIn).getGrowingAge())/-24000) : 1F;
        
        double d8 = entityIn.lastTickPosX + (entityIn.posX - entityIn.lastTickPosX) * (double)partialTicks;
        double d3 = entityIn.lastTickPosY + (entityIn.posY - entityIn.lastTickPosY) * (double)partialTicks + (double)entityIn.getShadowSize();
        double d4 = entityIn.lastTickPosZ + (entityIn.posZ - entityIn.lastTickPosZ) * (double)partialTicks;
        int i = MathHelper.floor_double(d8 - (double)f2);
        int j = MathHelper.floor_double(d8 + (double)f2);
        int k = MathHelper.floor_double(d3 - (double)f2);
        int l = MathHelper.floor_double(d3);
        int i1 = MathHelper.floor_double(d4 - (double)f2);
        int j1 = MathHelper.floor_double(d4 + (double)f2);
        double d5 = x - d8;
        double d6 = y - d3;
        double d7 = z - d4;
        Tessellator tessellator = Tessellator.instance;
        tessellator.startDrawingQuads();

        for (int k1 = i; k1 <= j; ++k1)
        {
            for (int l1 = k; l1 <= l; ++l1)
            {
                for (int i2 = i1; i2 <= j1; ++i2)
                {
                    Block block = world.getBlock(k1, l1 - 1, i2);

                    if (block.getMaterial() != Material.air && world.getBlockLightValue(k1, l1, i2) > 3)
                    {
                    	Method renderShadowBlock_reflected = ReflectionHelper.findMethod(Render.class, this, new String[]{"func_147907_a", "func_147907_a"}, Block.class, Double.TYPE, Double.TYPE, Double.TYPE, Integer.TYPE, Integer.TYPE, Integer.TYPE, Float.TYPE, Float.TYPE, Double.TYPE, Double.TYPE, Double.TYPE);
                    	
                    	try
                    	{
                    		renderShadowBlock_reflected.invoke(this, block, x, y + (double)entityIn.getShadowSize(), z, k1, l1, i2, shadowAlpha, f2, d5, d6 + (double)entityIn.getShadowSize(), d7);
            			}
                    	catch (Exception e) {}
                    }
                }
            }
        }

        tessellator.draw();
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glDisable(GL11.GL_BLEND);
        GL11.glDepthMask(true);
    }
}