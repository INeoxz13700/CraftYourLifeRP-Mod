package fr.craftyourliferp.inventory;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.renderer.texture.TextureMap;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.util.ResourceLocation;

public class SlotGun extends Slot {
	
	public enum GunType
	{
		HEAVY_WEAPON,
		PISTOL
	};
	
	public GunType type;

	public SlotGun(IInventory inventory, GunType type, int slotIndex, int displayX, int displayY)
	{
		super(inventory, slotIndex, displayX, displayY);
		this.type = type;
		
		if(type == GunType.HEAVY_WEAPON)
		{
			this.setBackgroundIconTexture(new ResourceLocation("craftyourliferp", "textures/gui/container/heavy_weapon.png"));
		}
		else
		{
			this.setBackgroundIconTexture(new ResourceLocation("craftyourliferp", "textures/gui/container/pistol.png"));
		}
	}
	



}
