package fr.craftyourliferp.items;

import java.util.List;

import com.mojang.realmsclient.gui.ChatFormatting;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketSyncShield;
import fr.craftyourliferp.shield.ShieldStats;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.world.World;

public class ItemVestBullet extends Item {
	
	int ticks = 0;
	
	public ItemVestBullet() {
		this.setCreativeTab(CreativeTabs.tabCombat);
		this.setMaxStackSize(1);
	}
	
	public void addInformation(ItemStack par1ItemStack, EntityPlayer par2EntityPlayer, List par3List, boolean par4) {

		par3List.add(ChatFormatting.BLUE + "Réduit les dégâts de 50%");
		par3List.add(ChatFormatting.GREEN + "Clique droit pour équiper");

	}

    public ItemStack onItemRightClick(ItemStack it, World world, EntityPlayer p)
    {
    	
    	if(!world.isRemote && p.inventory.mainInventory[p.inventory.currentItem].equals(it))
    	{
    		NBTTagCompound itemData = getNBT(it);
    		
    		ExtendedPlayer pData = ExtendedPlayer.get(p);
    		
    		if(pData.isUsingVestBullet)
    		{
    			p.addChatComponentMessage(new ChatComponentText("§cVous êtes déjà entrain d'équipper un gilet par balle"));
    			return it;
    		}
    	
    		if(!itemData.hasKey("ammount"))
    		{
    			itemData.setBoolean("rightclicked", true);
        		pData.isUsingVestBullet = true;
    			double pPreviousPosX = p.posX;
    			double pPreviousPosZ = p.posZ;
    	    	
    			itemData.setDouble("PreviousPosX", pPreviousPosX);
    			itemData.setDouble("PreviousPosZ", pPreviousPosZ);
    			p.addChatComponentMessage(new ChatComponentText("§aVous êtes entrain d'équipper un gilet par balle ne bougez pas !"));
    			return it;
    		}
    	
    	}
        return it;
    }
    
    public void onUpdate(ItemStack it, World world, Entity entity, int slot, boolean p_77663_5_) 
    {

    	if(!world.isRemote)
    	{
    		ticks++;
    		NBTTagCompound itemData = getNBT(it);
    	
	    	if(ticks % 20 != 0 || !(entity instanceof EntityPlayer))
	    	{
	    		return;
	    	}
	    	
	    	if(!itemData.hasKey("rightclicked") || !itemData.getBoolean("rightclicked"))
	    	{
	    		return;
	    	}
	    	
			EntityPlayer p = (EntityPlayer) entity;
			ExtendedPlayer pData = ExtendedPlayer.get(p);
			
		
	    	if(itemData.getDouble("PreviousPosX") != p.posX || itemData.getDouble("PreviousPosZ") != p.posZ)
	    	{
	    		itemData.setBoolean("rightclicked", false);
	    		itemData.removeTag("ammount");
	    		pData.isUsingVestBullet = false;
	    		p.addChatMessage(new ChatComponentText("§cAction annulé vous venez de bouger!"));
	    		return;
	    	}
			
	    	if(p.inventory.mainInventory[p.inventory.currentItem] == null || !p.inventory.mainInventory[p.inventory.currentItem].equals(it))
	    	{
	    		itemData.setBoolean("rightclicked", false);
	    		itemData.removeTag("ammount");
	    		pData.isUsingVestBullet = false;
	    		p.addChatMessage(new ChatComponentText("§cAction annulé vous n'avez plus l'item en main!"));
	    		return;
	    	}
	
			
	    	if(itemData.getBoolean("rightclicked") && itemData.getInteger("ammount") >= 100)
			{
				p.inventory.mainInventory[p.inventory.currentItem] = null;
	    		pData.isUsingVestBullet = false;
	    		p.addChatMessage(new ChatComponentText("§aGilet par balle équipé avec succès!"));
	    		pData.shield.setShield(ShieldStats.maxShield);
		    	ServerUtils.syncShieldToSpigot(p.getCommandSenderName(),pData.shield.getShield());
	    		CraftYourLifeRPMod.packetHandler.sendTo(new PacketSyncShield(pData.shield.getShield()), (EntityPlayerMP) p);
			}
			else
			{
				int ammount = itemData.getInteger("ammount");
				itemData.setInteger("ammount", ammount + 10);
				p.addChatMessage(new ChatComponentText("§aVous etes entrain d'équiper un gilet par balle : §c" + (ammount + 10) + "§a%"));
			}
    	}
    
    }
    
    private static NBTTagCompound getNBT(ItemStack stack) {
        if (stack.stackTagCompound == null) {
            stack.stackTagCompound = new NBTTagCompound();
        }
        return stack.stackTagCompound;
    }

}