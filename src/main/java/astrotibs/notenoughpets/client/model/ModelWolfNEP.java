package astrotibs.notenoughpets.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.client.model.ModelWolf;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.util.MathHelper;

//Added in v1.2
@SideOnly(Side.CLIENT)
public class ModelWolfNEP extends ModelWolf {
	
    ModelRenderer wolfTail = ReflectionHelper.getPrivateValue( ModelWolf.class, this, new String[]{"wolfTail", "field_78180_g"} );
    ModelRenderer wolfMane = ReflectionHelper.getPrivateValue( ModelWolf.class, this, new String[]{"wolfMane", "field_78186_h"} );
	
    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	//LogHelper.info("ageInTicks:" + ageInTicks);
    	
    	float agescale = this.isChild ? 1F-(((float)((EntityWolf)entityIn).getGrowingAge())/-24000) : 1F;
    	
    	// Modify limb swing depending on age
    	if (this.isChild)
    	{
    		limbSwing /= 3F; // Remove the 3x speed assigned to children
    		limbSwing *= (2F - agescale);
    	}
    	
    	this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        
        // summon Wolf_NEC ~ ~ ~ {Owner:AstroTibs}
     	// summon Wolf_NEC ~ ~ ~ {Owner:AstroTibs, Age:-24000}
     	// summon Wolf_NEC ~ ~ ~
        
        if (this.isChild)
        {
            // Render the head
            // Puppy head is same as adult size
            
            GL11.glPushMatrix();
            float headscale = MathHelper.clamp_float(0.75F + agescale*0.25F, 0.75F, 1F);
            GL11.glScalef(headscale, headscale, headscale);
            float headYpos = MathHelper.clamp_float( (11F*(1F-agescale)) * scale, 0F, 11F * scale);
            float headZpos = MathHelper.clamp_float( (2F*(1F-agescale)) * scale, 0F, 3F * scale);
            GL11.glTranslatef(MathHelper.clamp_float(-0.000000833333F*((float)((EntityWolf)entityIn).getGrowingAge()), 0.0F, 0.02F), headYpos, headZpos); // To align child's head on its body
            this.wolfHeadMain.renderWithRotation(scale);
            GL11.glPopMatrix();
            
            // Render the body
            // Puppy body is half of adult size
            
            GL11.glPushMatrix();
            float bodyscale = MathHelper.clamp_float(0.5F + agescale*0.5F, 0.5F, 1F);
            GL11.glScalef(bodyscale, bodyscale, bodyscale);
            
            float bodypospow = 0.65F; //
            
            float bodyYpos = MathHelper.clamp_float(
            		(float)
            		(24F*(
            				1-agescale + (0.6*((agescale-0.5)*(agescale-0.5))-0.15)
            				))
            		* scale, 0F, 24.0F * scale);
            GL11.glTranslatef(0.0F, bodyYpos, 0.0F);
            this.wolfBody.render(scale);
            this.wolfLeg1.render(scale);
            this.wolfLeg2.render(scale);
            this.wolfLeg3.render(scale);
            this.wolfLeg4.render(scale);
            this.wolfTail.renderWithRotation(scale);
            this.wolfMane.render(scale);
            GL11.glPopMatrix();
        }
        else
        {
        	super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }
    
    /**
     * I'm adding this in so that I can toy with leg animation speed.
     */
    @Override
    public void setLivingAnimations(EntityLivingBase wolf, float limbSwing, float limbSwingAmount, float headCock)
    {
    	float agescale = this.isChild ? 1F-(((float)((EntityWolf)wolf).getGrowingAge())/-24000) : 1F;
    	
    	// Modify limb swing depending on age
    	if (this.isChild)
    	{
    		limbSwing /= 3F; // Remove the 3x speed assigned to children
    		limbSwing *= (2F - agescale);
    	}
    	
    	super.setLivingAnimations(wolf, limbSwing, limbSwingAmount, headCock);
    }
}
