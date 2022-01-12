package fr.craftyourliferp.blocks;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import fr.craftyourliferp.items.CRPTab;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;

public class BlockInvisible extends Block {
	
	public BlockInvisible(String name, boolean luminy) {
	     super(Material.rock);
	     GameRegistry.registerBlock(this, name);
	     setBlockName(name);
	     setBlockTextureName(CraftYourLifeRPMod.name + ":invisibleBlock");
	     setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
	     this.setBlockUnbreakable();
	     this.setResistance(Integer.MAX_VALUE);
	     if(luminy) {
	    	 this.setLightLevel(100f);
	    	 this.setLightOpacity(0);
	     }
	}
	
    public boolean isOpaqueCube() {
    	return false;
    }

    public boolean isSolid()
    {
        return false;
    }
    
    public boolean getUseNeighborBrightness()
    {
        return true;
    }
    
	
}
