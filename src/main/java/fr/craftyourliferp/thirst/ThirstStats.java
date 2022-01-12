package fr.craftyourliferp.thirst;

import java.util.Random;

import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.MathsUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.world.EnumDifficulty;
import net.minecraft.world.World;

public class ThirstStats {
	
	  public static final float maxThirst = 20f;
	  public static final float initialThirst = 20f;
	    
	  private EntityPlayer player;
	    
	  private float thirst;
	  
	  public float prevThirst;
	  
	  
	    
	    public ThirstStats(float thirst, EntityPlayer p) {
	    	this.player = p;
	    	this.setThirst(thirst);
	    }
	    
	    public float getThirst() 
	    {
	    	if(!player.worldObj.isRemote)
	    	{
	    		return this.thirst;
	    	}
	    	return this.thirst;
	    }
	    
	    public float getThirstNormalized() 
	    {
	    	if(!player.worldObj.isRemote)
	    	{
	    		return this.thirst / maxThirst;
	    	}
	    	return this.thirst  / maxThirst;
	    }
	    
	    
	    public void setThirst(float value)
	    {
	    	if(!player.worldObj.isRemote)
	    	{
	    		thirst = MathsUtils.Clamp(value, 0.0F, maxThirst);
	    		if(ExtendedPlayer.get(player)!= null)
	    		{
	    			ExtendedPlayer.get(player).syncThirst();
	    		} 
	    	}
	    	else {
	    		thirst = MathsUtils.Clamp(value, 0.0F, maxThirst);
	    	}
	    }
	    
	    //Thirst logic
	    public void onUpdate(EntityPlayer p_75118_1_)
	    {
	        EnumDifficulty enumdifficulty = p_75118_1_.worldObj.difficultySetting;
	        this.prevThirst = this.thirst;
	        int difficulty = enumdifficulty.ordinal();


	        if(difficulty == 0 || p_75118_1_.capabilities.isCreativeMode || p_75118_1_.capabilities.isFlying)
	        {
	        	return;
	        }
	        
	        
	        float ratioThirstGain = ((difficulty) * (player.isSprinting() ? 2 : 1)) * 0.25f;
	        if(MathHelper.getRandomIntegerInRange(new Random(), 0, player.isSprinting() ? 300 : 700) == 0)
	        {
		        setThirst(getThirst() - ratioThirstGain);
	        }
	        
	        
	        if(thirst <= 5.0D)
	        {
	        	player.setSprinting(false);
	        }
	        
	        if(thirst <= 0 && p_75118_1_.ticksExisted % 20 == 0)
	        {
	        	p_75118_1_.attackEntityFrom(DamageSource.starve, 0.5F);
	        	p_75118_1_.addPotionEffect(new PotionEffect(9, 20*15,3));
	        }
	    }
	    
	    //ClientSide
	    public boolean playerLookWater(World worldObj)
	    {
	    	if(worldObj.isRemote)
	    	{
			    MovingObjectPosition mop = (Minecraft.getMinecraft()).renderViewEntity.rayTrace(2.0D, 1.0F);
			    if (mop != null) {
	
				      int blockHitSide = mop.sideHit;
				      Block blockLookingAt = (Minecraft.getMinecraft()).theWorld.getBlock(mop.blockX, mop.blockY, mop.blockZ);
				      if (blockLookingAt == Blocks.water) 
				      {
				    	  return true;
				      }
			    }
	    	}
	    	else
	    	{
	    	    MovingObjectPosition mop = ExtendedPlayer.rayTraceServer(player, 2.0D, 1.0f);
			    if (mop != null) {
			      int blockHitSide = mop.sideHit;
			      Block blockLookingAt = player.worldObj.getBlock(mop.blockX, mop.blockY, mop.blockZ);
			      if (blockLookingAt == Blocks.water) 
			      {
			    	  return true;
			      }
			    }
	    	}
		    return false;
	   }
	    



}
