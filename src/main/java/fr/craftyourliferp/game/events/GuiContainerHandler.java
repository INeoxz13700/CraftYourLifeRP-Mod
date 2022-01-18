package fr.craftyourliferp.game.events;

import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.ingame.gui.GuiInventoryRP;
import fr.craftyourliferp.inventory.ContainerRP;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.world.World;

public class GuiContainerHandler implements IGuiHandler {

	@Override
	public Object getServerGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0)
		{
			return new ContainerRP(player.inventory, false);
		}
		return null;
	}

	@Override
	@SideOnly(Side.CLIENT)
	public Object getClientGuiElement(int ID, EntityPlayer player, World world, int x, int y, int z) {
		if(ID == 0)
		{
			return new GuiInventoryRP(player);
		}
		return null;
	}

}
