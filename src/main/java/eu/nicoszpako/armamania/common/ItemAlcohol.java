package eu.nicoszpako.armamania.common;

import java.util.List;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.MinecraftUtils;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class ItemAlcohol extends Item {
	
	private float gPerLAlcol;

	public ItemAlcohol(float gPerLAlcol)
	{
		this.gPerLAlcol = gPerLAlcol;
		this.setMaxStackSize(1);
	}
	
	@Override
    public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer player)
    {		
		player.setItemInUse(itemstack, this.getMaxItemUseDuration(itemstack));
		return super.onItemRightClick(itemstack, world, player);
	}
	
	@Override
    public ItemStack onEaten(ItemStack is, World world, EntityPlayer player)
    {   
        if (!world.isRemote)
        {
		    if (!player.capabilities.isCreativeMode)
		    {
		    	is.stackSize--;
		    }
	        
		    ExtendedPlayer exPlayer = ExtendedPlayer.get(player);
		    exPlayer.setgAlcolInBlood(exPlayer.getgAlcolInBlood() + gPerLAlcol);
		    if(exPlayer.shouldBeInEthylicComa())
		    {
		    	ServerUtils.sendChatMessage(player, "§cVous êtes sur le point de faire un coma éthylique");
		    	ServerUtils.sendChatMessage(player, "§callez vite à l'hôpital!");
		    }
	        player.setItemInUse(null, 0);
        }

        return is;
    }
	
    public int getMaxItemUseDuration(ItemStack p_77626_1_)
    {
        return 20*4;
    }
    
    public EnumAction getItemUseAction(ItemStack p_77661_1_)
    {
        return EnumAction.drink;
    }
    
    @SideOnly(Side.CLIENT)
    public void addInformation(ItemStack is, EntityPlayer player, List list, boolean bool)
    {
        list.add(new String("§61 verre = §e" + this.gPerLAlcol + "g dans le sang"));
    }
	
}
