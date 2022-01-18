package fr.craftyourliferp.inventory;

import fr.craftyourliferp.items.ItemBulletproofShield;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.ItemStack;

public class SlotBulletproofShield extends Slot {

	public SlotBulletproofShield(IInventory inventory, int slotIndex, int displayX, int displayY)
	{
		super(inventory, slotIndex, displayX, displayY);
	}
	
	@Override
	public int getSlotStackLimit()
	{
		return 1;
	}


	@Override
	public boolean isItemValid(ItemStack itemstack)
	{
		if(itemstack != null && itemstack.getItem() instanceof ItemBulletproofShield)
		{
			return true;
		}
		return false;
	}

}
