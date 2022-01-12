package fr.craftyourliferp.items;

import java.util.Arrays;
import java.util.List;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.projectile.EntityPotion;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

public class ItemWaterBottle extends Item {
	
	private int waterGive;
	
	private float waterLiter;
		
	public ItemWaterBottle(float waterLiter, int waterGive)
	{
		Items.waterBottle = this;
        this.setHasSubtypes(true);
		this.setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		this.setMaxDamage((int) (waterLiter*1000));
		maxStackSize = 1;
		this.waterGive = waterGive;
		this.waterLiter = waterLiter;
	}
	
	@Override
    public EnumAction getItemUseAction(ItemStack itemstack)
    {
        return EnumAction.drink;
    }
	
	@Override
    public int getMaxItemUseDuration(ItemStack itemstack)
    {
        return 20*3;
    }
	
	@Override
    public ItemStack onEaten(ItemStack itemstack, World world, EntityPlayer p_77654_3_)
    {
		if(!world.isRemote)
		{
	        if(itemstack.getItem() == this)
	        {
	        	ExtendedPlayer ep = ExtendedPlayer.get(p_77654_3_);
	        	ep.thirst.setThirst(ep.thirst.getThirst() + 10);
	        	itemstack.damageItem(waterGive, p_77654_3_);
	        }
		}
        return itemstack;
    }
	
    public ItemStack onItemRightClick(ItemStack p_77659_1_, World p_77659_2_, EntityPlayer p_77659_3_)
    {
    	ExtendedPlayer ep = ExtendedPlayer.get(p_77659_3_);
    	if(ep.thirst.playerLookWater(p_77659_3_.worldObj))
    	{
    		p_77659_1_.setItemDamage(0);
    	}
    	else if(p_77659_1_.getItemDamage() <= waterLiter*1000)
    	{
    		p_77659_3_.setItemInUse(p_77659_1_, this.getMaxItemUseDuration(p_77659_1_));
    	}
        return p_77659_1_;
    }
    
    
    @Override
    public void addInformation(ItemStack p_77624_1_, EntityPlayer p_77624_2_, List p_77624_3_, boolean p_77624_4_) 
    {
    	p_77624_3_.add("§bBouteille de " + waterLiter + "L d'eau");
    	
    	
    	float literLeft = (float) (p_77624_1_.getMaxDamage() - p_77624_1_.getItemDamage()) / 1000;
    	p_77624_3_.add(literLeft + "L d'eau restant");
    	
    	super.addInformation(p_77624_1_, p_77624_2_, p_77624_3_, p_77624_4_);
    }

    


}
