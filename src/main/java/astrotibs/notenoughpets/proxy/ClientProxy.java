package astrotibs.notenoughpets.proxy;

import astrotibs.notenoughpets.client.model.ModelOcelotNEP;
import astrotibs.notenoughpets.client.model.ModelParrotNEP;
import astrotibs.notenoughpets.client.model.ModelWolfNEP;
import astrotibs.notenoughpets.client.render.RenderOcelotNEP;
import astrotibs.notenoughpets.client.render.RenderParrotNEP;
import astrotibs.notenoughpets.client.render.RenderWolfNEP;
import astrotibs.notenoughpets.entity.EntityOcelotNEP;
import astrotibs.notenoughpets.entity.EntityParrotNEP;
import astrotibs.notenoughpets.entity.EntityWolfNEP;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import net.minecraft.client.model.ModelCow;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {

	@Override
	public void preInit(FMLPreInitializationEvent e) {
		super.preInit(e);
	}
	
	@Override
	public void init(FMLInitializationEvent e) {
		super.init(e);
	}
	
	@Override
	public void registerRender()
	{
		// Ocelots
		RenderingRegistry.registerEntityRenderingHandler(EntityOcelotNEP.class, new RenderOcelotNEP(new ModelOcelotNEP(), new ModelOcelotNEP(), 0.4F));
		
		// Wolves
		RenderingRegistry.registerEntityRenderingHandler(EntityWolfNEP.class, new RenderWolfNEP(new ModelWolfNEP(), new ModelWolfNEP(), 0.5F));
		
		// Parrots
		RenderingRegistry.registerEntityRenderingHandler(EntityParrotNEP.class, new RenderParrotNEP(new ModelParrotNEP(0), 0.3F));
	}
	
}
