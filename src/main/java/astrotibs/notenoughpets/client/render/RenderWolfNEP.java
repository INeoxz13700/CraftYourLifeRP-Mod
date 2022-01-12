package astrotibs.notenoughpets.client.render;

import java.lang.reflect.Method;

import org.lwjgl.opengl.GL11;

import astrotibs.notenoughpets.entity.EntityWolfNEP;
import astrotibs.notenoughpets.util.SkinVariations;
import cpw.mods.fml.relauncher.ReflectionHelper;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.model.ModelBase;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderLiving;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;
import net.minecraft.util.ResourceLocation;
import net.minecraft.world.World;

// Added in v1.2.1 - To prepare for custom wolf textures
public class RenderWolfNEP extends RenderLiving
{
    private static final ResourceLocation wolfCollarTextures = new ResourceLocation("textures/entity/wolf/wolf_collar.png"); // Copied from vanilla - v1.3.0
    
	public RenderWolfNEP(ModelBase mainModel, ModelBase renderPassModel, float shadowSize)
    {
		//super(mainModel, new ModelWolfNEP(), 0.5F);
		super(mainModel, shadowSize);
		this.setRenderPassModel(renderPassModel);
	}
	
	// summon Wolf_NEC ~ ~ ~ {Owner:AstroTibs, DogType:0}
	// summon Wolf_NEC ~ ~ ~ {Owner:AstroTibs, DogType:1, Age:-24000}
	// summon Wolf_NEC ~ ~ ~ {DogType:2}
	
    /**
     * Queries whether should render the specified pass or not.
     */
	// Added in v1.3.0
    protected int shouldRenderPass(EntityWolfNEP wolf, int passNumber, float progress)
    {
        if (passNumber == 0 && wolf.getWolfShaking())
        {
            float f1 = wolf.getBrightness(progress) * wolf.getShadingWhileShaking(progress);
            this.bindTexture(SkinVariations.dogSkinArray[wolf.getTameSkin()][0]);
            GL11.glColor3f(f1, f1, f1);
            return 1;
        }
        else if (passNumber == 1 && wolf.isTamed())
        {
            this.bindTexture(wolfCollarTextures);
            int j = wolf.getCollarColor();
            GL11.glColor3f(EntitySheep.fleeceColorTable[j][0], EntitySheep.fleeceColorTable[j][1], EntitySheep.fleeceColorTable[j][2]);
            return 1;
        }
        else
        {
            return -1;
        }
    }
	
    
    // Added in v1.3.0
	@Override
    protected int shouldRenderPass(EntityLivingBase wolfNEC, int passNumber, float progress)
    {
    	if (wolfNEC instanceof EntityWolfNEP)
    	{
    		return this.shouldRenderPass((EntityWolfNEP)wolfNEC, passNumber, progress);
    	}
    	else
    	{
    		return super.shouldRenderPass(wolfNEC, passNumber, progress);
    	}
    }
	
	
    // Re-written in v1.3.0
	protected ResourceLocation getEntityTexture(EntityWolfNEP wolf)
    {
    	int wolfSkinIndex = 0; // Default to 0 in the event of the vanilla wolf
    	
    	if (wolf instanceof EntityWolfNEP)
    	{
    		// First, check for manual overrides!
    		if (wolf.getCustomNameTag().equals("Wink")) // Stacy Plays's dog from Minecraft: Story Mode, referencing YouTuber Stacy Hinojosa
    		{
    			wolfSkinIndex = 0;
    		}
    		else if (wolf.getCustomNameTag().equals("Blocco")) // From Minecraft: Story Mode Season Two. Named after Juan Vaca's dog, Rocco
    		{
    			wolfSkinIndex = 1;
    		}
    		// v2.0.0
    		else if (wolf.getCustomNameTag().equals("Rexbutt")) // A good boi
    		{
    			wolfSkinIndex = 4;
    		}
    		else if (wolf.getCustomNameTag().equals("Doge"))
    		{
    			wolfSkinIndex = 5;
    		}
    		else if (wolf.getCustomNameTag().equals("Mojo")) // Brandt's dog
    		{
    			wolfSkinIndex = 6;
    		}
    		else if (wolf.getCustomNameTag().equals("Buddha")) // Count Dankula's infamous little gremlin
    		{
    			wolfSkinIndex = 7;
    		}
    		else if (wolf.getCustomNameTag().equals("Angelo")) // Rinoa's dog in Final Fantasy 8
    		{
    			wolfSkinIndex = 8;
    		}
    		else
    		{
    			// If there are no manual overrides, refer to the dog's skin int
        		wolfSkinIndex = ((EntityWolfNEP)wolf).getTameSkin();
        		wolfSkinIndex = Math.max(wolfSkinIndex >= SkinVariations.dogSkinArray.length ? 0 : wolfSkinIndex, 0);
    		}
    	}
    	
    	// 0:Normal, 1:Angry, 2:Tame
        return SkinVariations.dogSkinArray[wolfSkinIndex][wolf.isTamed() ? 2 : (wolf.isAngry() ? 1 : 0)];
    }

	@Override
	protected ResourceLocation getEntityTexture(Entity wolf)
	{
		return this.getEntityTexture((EntityWolfNEP)wolf);
	}

    /**
     * Defines what float the third param in setRotationAngles of ModelBase is
     * Added in v2.0.0 to account for the new dogs
     */
    @Override
    protected float handleRotationFloat(EntityLivingBase livingBase, float partialTicks)
    {
        return this.handleRotationFloat((EntityWolfNEP)livingBase, partialTicks);
    }
    protected float handleRotationFloat(EntityWolfNEP livingBase, float partialTicks)
    {
        return livingBase.getTailRotation();
    }
    
    /**
     * Allows the render to do state modifications necessary before the model is rendered.
     */
	// v2.0.0 - scale dog based on breed
    protected void preRenderWolfNEC(EntityWolfNEP entitylivingbaseIn, float partialTickTime)
    {
        super.preRenderCallback(entitylivingbaseIn, partialTickTime);
        
        float dogBreedScale = EntityWolfNEP.dogBreedScale(entitylivingbaseIn);
        
        GL11.glScalef(dogBreedScale, dogBreedScale, dogBreedScale);
    }
    @Override
    protected void preRenderCallback(EntityLivingBase wolf, float animationProgress)
    {
        this.preRenderWolfNEC((EntityWolfNEP)wolf, animationProgress);
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
            	this.renderShadow((EntityWolfNEP)entityIn, x, y, z, f, partialTicks);
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
	
    private void renderShadow(EntityWolfNEP entityIn, double x, double y, double z, float shadowAlpha, float partialTicks)
    {
        GL11.glEnable(GL11.GL_BLEND);
        GL11.glBlendFunc(GL11.GL_SRC_ALPHA, GL11.GL_ONE_MINUS_SRC_ALPHA);
        this.renderManager.renderEngine.bindTexture((ResourceLocation)ReflectionHelper.getPrivateValue(Render.class, this, new String[]{"shadowTextures", "field_110778_a"}));
        World world = this.renderManager.worldObj;
        GL11.glDepthMask(false);
        float f2 = this.shadowSize;

        f2 *= entityIn.getRenderSizeModifier();
        
        // Scale based on breed type and age
        f2 *= EntityWolfNEP.dogBreedScale(entityIn);
        f2 *= entityIn.isChild() ? 1F-0.5F*(((float)((EntityWolf)entityIn).getGrowingAge())/-24000) : 1F;
        
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
