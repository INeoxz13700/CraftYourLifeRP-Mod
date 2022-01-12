package astrotibs.notenoughpets.client.model;

import org.lwjgl.opengl.GL11;

import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelOcelot;
import net.minecraft.client.model.ModelRenderer;
import net.minecraft.entity.Entity;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.util.MathHelper;

//Added in v1.2
@SideOnly(Side.CLIENT)
public class ModelOcelotNEP extends ModelOcelot
{
    ModelRenderer ocelotBackLeftLeg = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotBackLeftLeg", "field_78161_a"} );
    ModelRenderer ocelotBackRightLeg = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotBackRightLeg", "field_78159_b"} );
    ModelRenderer ocelotFrontLeftLeg = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotFrontLeftLeg", "field_78160_c"} );
    ModelRenderer ocelotFrontRightLeg = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotFrontRightLeg", "field_78157_d"} );
    ModelRenderer ocelotTail = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotTail", "field_78158_e"} );
    ModelRenderer ocelotTail2 = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotTail2", "field_78155_f"} );
    ModelRenderer ocelotHead = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotHead", "field_78156_g"} );
    ModelRenderer ocelotBody = ReflectionHelper.getPrivateValue( ModelOcelot.class, this, new String[]{"ocelotBody", "field_78162_h"} );
    
    /**
     * Sets the models various rotation angles then renders the model.
     */
    @Override
    public void render(Entity entityIn, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch, float scale)
    {
    	//LogHelper.info("ageInTicks:" + ageInTicks);
    	
    	float agescale = this.isChild ? 1F-(((float)((EntityOcelot)entityIn).getGrowingAge())/-24000) : 1F;
    	
    	// Modify limb swing depending on age
    	if (this.isChild)
    	{
    		limbSwing /= 3F; // Remove the 3x speed assigned to children
    		limbSwing *= (2F - agescale);
    	}
    	
        this.setRotationAngles(limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale, entityIn);
        
        // summon Ocelot_NEC ~ ~ ~ {Owner:AstroTibs, CatType:0}
     	// summon Ocelot_NEC ~ ~ ~ {Owner:AstroTibs, CatType:0, Age:-24000}
     	// summon Ocelot_NEC ~ ~ ~ {CatType:16}
        
        if (this.isChild)
        {
            // Render the head
            // Kitten head is 3/4 of adult size
            
            GL11.glPushMatrix();
            float headscale = MathHelper.clamp_float(0.75F + agescale*0.25F, 0.75F, 1F);
            GL11.glScalef(headscale, headscale, headscale);
            float headYpos = MathHelper.clamp_float( (11F*(1F-agescale)) * scale, 0F, 11F * scale);
            float headZpos = MathHelper.clamp_float( (3F*(1F-agescale)) * scale, 0F, 3F * scale);
            GL11.glTranslatef(0.0F, headYpos, headZpos);
            this.ocelotHead.render(scale);
            GL11.glPopMatrix();
            
            // Render the body
            // Kitten body is half of adult size
            
            GL11.glPushMatrix();
            float bodyscale = MathHelper.clamp_float(0.5F + agescale*0.5F, 0.5F, 1F);
            GL11.glScalef(bodyscale, bodyscale, bodyscale);
            
            float bodypospow = 0.65F; //
            
            float bodyYpos = MathHelper.clamp_float(
            		(float)(24F*(
            				1-agescale + (0.6*((agescale-0.5)*(agescale-0.5))-0.15)
            				))
            		* scale, 0F, 24.0F * scale);
            GL11.glTranslatef(0.0F, bodyYpos, 0.0F);
            
            this.ocelotBody.render(scale);
            this.ocelotBackLeftLeg.render(scale);
            this.ocelotBackRightLeg.render(scale);
            this.ocelotFrontLeftLeg.render(scale);
            this.ocelotFrontRightLeg.render(scale);
            this.ocelotTail.render(scale);
            this.ocelotTail2.render(scale);
            GL11.glPopMatrix();
        }
        else
        {
            super.render(entityIn, limbSwing, limbSwingAmount, ageInTicks, netHeadYaw, headPitch, scale);
        }
    }

    
}