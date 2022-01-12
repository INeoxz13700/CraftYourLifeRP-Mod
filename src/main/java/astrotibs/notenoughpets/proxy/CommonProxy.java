package astrotibs.notenoughpets.proxy;

import astrotibs.notenoughpets.config.GeneralConfig;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {

	public void preInit(FMLPreInitializationEvent e) {
		
    }

	public void init(FMLInitializationEvent e) {
		
		// Event listeners
		MinecraftForge.EVENT_BUS.register(new GeneralConfig());
		
	}

	public void postInit(FMLPostInitializationEvent e)  {
		
	}
	
	public void registerRender() {
	}
	
}
