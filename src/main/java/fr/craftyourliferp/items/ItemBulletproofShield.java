package fr.craftyourliferp.items;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class ItemBulletproofShield extends Item implements IItemCollider,IItemPress
{
	public ItemBulletproofShield()
	{
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		setUnlocalizedName("BulletproofShield");
		setMaxStackSize(1);
		setMaxDamage(700);
	}
    
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return Integer.MAX_VALUE;
    }

	
    /**
     * Callback for item usage. If the item does something special on right clicking, he will have one of those. Return
     * True if something happen and false if it don't. This is for ITEMS, not BLOCKS
     */
    public boolean onItemUse(ItemStack is, EntityPlayer player, World world, int x, int y, int z, int p_77648_7_, float p_77648_8_, float p_77648_9_, float p_77648_10_)
    {
        return false;
    }

	@Override
	public float[] getSize() {
		return new float[] {0.5f,2f};
	}

	@Override
	public Vec3 getRelativePositionFromPlayer() {
		return Vec3.createVectorHelper(0, 0, 1.2f);
	}

	@Override
	public void onItemRightClicked(EntityPlayer player, World worldObj, ItemStack heldItem) { }

	@Override
	public void onItemUsing(EntityPlayer player, World worldObj, ItemStack heldItem, int itemPressedTicksCount) { }

	@Override
	public void onItemStopUsing(EntityPlayer player, World worldObj, ItemStack heldItem, int itemPressedTicksCount) { }

	@Override
	public void onItemAttacked(ItemStack is,EntityPlayer player,Entity collidedWith, float damage)
	{
		if(!is.hasTagCompound())
		{
			is.stackTagCompound = new NBTTagCompound();	
			is.stackTagCompound.setInteger("LifeTime", getMaxDamage());
		}
		
		is.stackTagCompound.setInteger("LifeTime", is.stackTagCompound.getInteger("LifeTime")-(int)(damage*1.5F));
		if(is.stackTagCompound.getInteger("LifeTime") <= 0)
		{
			int index = player.inventory.currentItem;
			player.inventory.setInventorySlotContents(index, null);
		}
		player.worldObj.playSoundAtEntity(player, "craftyourliferp:shield-hit", 1.0F, 1.0F);
	}
	
    public boolean onEntitySwing(EntityLivingBase entityLiving, ItemStack stack)
    {
        return true;
    }
	


}
