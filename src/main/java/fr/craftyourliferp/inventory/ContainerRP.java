package fr.craftyourliferp.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.inventory.SlotGun.GunType;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;

public class ContainerRP extends Container
{

    public boolean isLocalWorld;
    
    private final EntityPlayer thePlayer;
    

	public ContainerRP(final InventoryPlayer inventoryPlayer, boolean localWorld)
    {
        this.isLocalWorld = localWorld;
        this.thePlayer = inventoryPlayer.player;

        int i;
        int j;
        
        for (i = 0; i < 4; ++i)
        {
            final int k = i;
            this.addSlotToContainer(new Slot(inventoryPlayer, inventoryPlayer.getSizeInventory() - 1 - i, 8, 10 + i * 18)
            {
              
                public int getSlotStackLimit()
                {
                    return 1;
                }
                /**
                 * Check if the stack is a valid item for this slot. Always true beside for the armor slots.
                 */
                public boolean isItemValid(ItemStack p_75214_1_)
                {
                    if (p_75214_1_ == null) return false;
                    return p_75214_1_.getItem().isValidArmor(p_75214_1_, k, thePlayer);
                }
                /**
                 * Returns the icon index on items.png that is used as background image of the slot.
                 */
                @SideOnly(Side.CLIENT)
                public IIcon getBackgroundIconIndex()
                {
                    return ItemArmor.func_94602_b(k);
                }
            });
        }

        for (i = 0; i < 3; ++i)
        {
            for (j = 0; j < 9; ++j)
            {
                this.addSlotToContainer(new Slot(inventoryPlayer, j + (i + 1) * 9, 8 + j * 18, 97 + i * 18));
            }
        }

        this.addSlotToContainer(new SlotGun(inventoryPlayer, GunType.HEAVY_WEAPON, 0, 116, 22));
        this.addSlotToContainer(new SlotGun(inventoryPlayer, GunType.PISTOL, 1, 116, 55));
        this.addSlotToContainer(new SlotBulletproofShield(inventoryPlayer, 2, 82, 64));
         
        for (i = 3; i < 9; ++i)
        {
            this.addSlotToContainer(new Slot(inventoryPlayer, i, 8 + i * 18, 151));
        }
        

    }

	/**
	 * This should always return true, since custom inventory can be accessed from anywhere
	 */
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		return true;
	}

	/**
	 * Called when a player shift-clicks on a slot. You must override this or you will crash when someone does that.
	 * Basically the same as every other container I make, since I define the same constant indices for all of them 
	 */
	public ItemStack transferStackInSlot(EntityPlayer p_82846_1_, int p_82846_2_)
    {
        ItemStack itemstack = null;
        Slot slot = (Slot)this.inventorySlots.get(p_82846_2_);

        if (slot != null && slot.getHasStack())
        {
            ItemStack itemstack1 = slot.getStack();
            itemstack = itemstack1.copy();

            if (p_82846_2_ == 0)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, true))
                {
                    return null;
                }

                slot.onSlotChange(itemstack1, itemstack);
            }
            else if (p_82846_2_ >= 1 && p_82846_2_ < 5)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 5 && p_82846_2_ < 9)
            {
                if (!this.mergeItemStack(itemstack1, 9, 45, false))
                {
                    return null;
                }
            }
            else if (itemstack.getItem() instanceof ItemArmor && !((Slot)this.inventorySlots.get(5 + ((ItemArmor)itemstack.getItem()).armorType)).getHasStack())
            {
                int j = 5 + ((ItemArmor)itemstack.getItem()).armorType;

                if (!this.mergeItemStack(itemstack1, j, j + 1, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 9 && p_82846_2_ < 36)
            {
                if (!this.mergeItemStack(itemstack1, 36, 45, false))
                {
                    return null;
                }
            }
            else if (p_82846_2_ >= 36 && p_82846_2_ < 45)
            {
                if (!this.mergeItemStack(itemstack1, 9, 36, false))
                {
                    return null;
                }
            }
            else if (!this.mergeItemStack(itemstack1, 9, 45, false))
            {
                return null;
            }

            if (itemstack1.stackSize == 0)
            {
                slot.putStack((ItemStack)null);
            }
            else
            {
                slot.onSlotChanged();
            }

            if (itemstack1.stackSize == itemstack.stackSize)
            {
                return null;
            }

            slot.onPickupFromSlot(p_82846_1_, itemstack1);
        }

        return itemstack;
    }
}
