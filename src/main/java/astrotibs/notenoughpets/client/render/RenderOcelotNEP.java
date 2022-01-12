package astrotibs.notenoughpets.client.render;

import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import astrotibs.notenoughpets.entity.EntityOcelotNEP;
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
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

/**
 * v2.0.0
 * Copied directly from RenderWolf just to be safe.
 * @author AstroTibs
 */
@SideOnly(Side.CLIENT)
public class RenderOcelotNEP extends RenderLiving
{
    public RenderOcelotNEP(ModelBase baseModel, ModelBase passModel, float shadowSize)
    {
        super(baseModel, shadowSize);
        this.setRenderPassModel(passModel);
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    protected ResourceLocation getEntityTexture(EntityOcelotNEP ocelot)
    {
    	// First, check for manual overrides!
		if (
				ocelot.getCustomNameTag().equals("Babou") // Archer reference? No! This was SALVADOR DALI'S PET!
				|| ocelot.getCustomNameTag().equals("Murphy Jr.") // I was trying to get the cat in the tub, but... he's crazy. He hates it.
				|| ocelot.getCustomNameTag().equals("Little Murphy") // Don't you need a license to keep endangered species?
				)
		{
			return SkinVariations.catSkinArray[0];
		}
		// Added in v 1.1
		else if (ocelot.getCustomNameTag().equals("Newton")) // Late cat belonging to Jeb
		{
			return SkinVariations.catSkinArray[1];
		}
		else if (ocelot.getCustomNameTag().equals("Jellie")) // Cat belonging to YouTube user GoodTimesWithScar
		{
			return SkinVariations.catSkinArray[9];
		}
		else if (ocelot.getCustomNameTag().equals("Juno"))
		{
			return SkinVariations.catSkinArray[13];
		}
		else if (ocelot.getCustomNameTag().equals("Willie"))
		{
			return SkinVariations.catSkinArray[14];
		}
		// Added in v 1.1
		else if (ocelot.getCustomNameTag().equals("Lily"))
		{
			return SkinVariations.catSkinArray[15];
		}
		else if (ocelot.getCustomNameTag().equals("Boo")) // Cat belonging to Asher Applegate
		{
			return SkinVariations.catSkinArray[16];
		}
		// Added in v 1.3.0
		else if (ocelot.getCustomNameTag().equals("Winslow")) // Cassie Rose's cat from Minecraft: Story Mode
		{
			return SkinVariations.catSkinArray[17];
		}
		else if (ocelot.getCustomNameTag().equals("Blitzkrieg")) // Cat belonging to Shahayhay
		{
			return SkinVariations.catSkinArray[18];
		}
		else if (ocelot.getCustomNameTag().equals("Princess")) // Cat belonging to Chara Violet
		{
			return SkinVariations.catSkinArray[19];
		}
		
		// If there are no manual overrides, refer to the cat's skin int
		return SkinVariations.catSkinArray[Math.max(ocelot.getTameSkin() >= SkinVariations.catSkinArray.length ? 0 : ocelot.getTameSkin(), 0)];
    }
    
    
    
    // -------------------- //
    // --- COLLAR STUFF --- //
    // -------------------- //
    
 	private static final ResourceLocation ocelotCollarTextures = new ResourceLocation("textures/entity/cat/cat_collar.png");
 	
    /**
     * Queries whether should render the specified pass or not.
     */
    protected int shouldRenderPass(EntityOcelotNEP ocelot, int pass, float animationProgress)
    {
    	// Collar layer
        if (pass == 1 && ocelot.isTamed())
        {
            this.bindTexture(ocelotCollarTextures);
            int j = ocelot.getCollarColor();
            GL11.glColor3f(EntitySheep.fleeceColorTable[j][0], EntitySheep.fleeceColorTable[j][1], EntitySheep.fleeceColorTable[j][2]);
            return 1;
        }
        else
        {
            return -1;
        }
    }

    
    
    // ------------------------- //
    // ------ SCALE STUFF ------ //
    // --- From RenderOcelot --- //
    // ------------------------- //
    
    protected void preRenderOcelotNEC(EntityOcelotNEP ocelot, float animationProgress)
    {
        super.preRenderCallback(ocelot, animationProgress);

        if (ocelot.getTameSkin()!=0) {GL11.glScalef(0.8F, 0.8F, 0.8F);}
    }
    @Override
    protected void preRenderCallback(EntityLivingBase ocelot, float animationProgress)
    {
        this.preRenderOcelotNEC((EntityOcelotNEP)ocelot, animationProgress);
    }
    
    
    
    // --------------------------------- //
    // ---------- OTHER STUFF ---------- //
    // --- Left over from RenderWolf --- //
    // --------------------------------- //
    
    /**
     * Queries whether should render the specified pass or not.
     */
    @Override
    protected int shouldRenderPass(EntityLivingBase entityBase, int pass, float animationProgress)
    {
        return this.shouldRenderPass((EntityOcelotNEP)entityBase, pass, animationProgress);
    }
    
    /**
     * Returns the location of an entity's texture. Doesn't seem to be called unless you call Render.bindEntityTexture.
     */
    @Override
    protected ResourceLocation getEntityTexture(Entity ocelot)
    {
        return this.getEntityTexture((EntityOcelotNEP)ocelot);
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
            	this.renderShadow((EntityOcelotNEP)entityIn, x, y, z, f, partialTicks);
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
	
    private void renderShadow(EntityOcelotNEP entityIn, double x, double y, double z, float shadowAlpha, float partialTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.renderManager.renderEngine.bindTexture((ResourceLocation)ReflectionHelper.getPrivateValue(Render.class, this, new String[]{"shadowTextures", "field_110778_a"}));
        World world = this.renderManager.worldObj;
        GL11.glDepthMask(false);
        float f2 = this.shadowSize;

        f2 *= entityIn.getRenderSizeModifier();
        
        // Scale based on breed type and age
        f2 *= entityIn.isChild() ? 1F-0.5F*(((float)((EntityOcelot)entityIn).getGrowingAge())/-24000) : 1F;
        
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