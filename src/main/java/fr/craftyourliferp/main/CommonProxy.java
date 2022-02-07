package fr.craftyourliferp.main;

import java.util.HashMap;
import java.util.Map;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.network.IGuiHandler;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import fr.craftyourliferp.fire.FireHandler;
import fr.craftyourliferp.game.events.CollisionListener;
import fr.craftyourliferp.game.events.EntityTrackerHandler;
import fr.craftyourliferp.game.events.EventsListener;
import fr.craftyourliferp.game.events.NuclearCentralEvent;
import fr.craftyourliferp.game.events.PlayerInventoryHandler;
import fr.craftyourliferp.game.events.PlayerItemInteractionListener;
import fr.craftyourliferp.game.events.PlayerLifeHandler;
import fr.craftyourliferp.game.events.ReanimationHandler;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.game.events.TicksHandler;
import fr.craftyourliferp.thirst.ThirstEventHandler;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;

public class CommonProxy {
	
	private static final Map<String, NBTTagCompound> extendedEntityData = new HashMap<String, NBTTagCompound>();
		
	public static void storeEntityData(String name, NBTTagCompound compound) {
		extendedEntityData.put(name, compound);
	}

	public static NBTTagCompound getEntityData(String name) {
		return extendedEntityData.remove(name);
	}

	public void init() {
        EventsListener listener = new EventsListener();
    	MinecraftForge.EVENT_BUS.register(listener);
        FMLCommonHandler.instance().bus().register(listener);
        
        ThirstEventHandler thirstListener = new ThirstEventHandler();
    	MinecraftForge.EVENT_BUS.register(thirstListener);
        FMLCommonHandler.instance().bus().register(thirstListener);
        
      	RendererListener rendererListener = new RendererListener();
    	FMLCommonHandler.instance().bus().register(rendererListener);
        MinecraftForge.EVENT_BUS.register(rendererListener);
        
        PlayerItemInteractionListener interactionListener = new PlayerItemInteractionListener();
    	FMLCommonHandler.instance().bus().register(interactionListener);
        MinecraftForge.EVENT_BUS.register(interactionListener); 
        
        CollisionListener collisionListener = new CollisionListener();
    	FMLCommonHandler.instance().bus().register(collisionListener);
        MinecraftForge.EVENT_BUS.register(collisionListener); 
        
        TicksHandler ticksListener = new TicksHandler();
    	FMLCommonHandler.instance().bus().register(ticksListener);
        MinecraftForge.EVENT_BUS.register(ticksListener);
        
    	FMLCommonHandler.instance().bus().register(CraftYourLifeRPMod.entityTrackerHandler);
        MinecraftForge.EVENT_BUS.register(CraftYourLifeRPMod.entityTrackerHandler);
        
      	FMLCommonHandler.instance().bus().register(CraftYourLifeRPMod.fireHandler);
        MinecraftForge.EVENT_BUS.register(CraftYourLifeRPMod.fireHandler); 
        
      	FMLCommonHandler.instance().bus().register(CraftYourLifeRPMod.captureHander);
        MinecraftForge.EVENT_BUS.register(CraftYourLifeRPMod.captureHander);
       
        PlayerInventoryHandler inventoryHandler = new PlayerInventoryHandler();
        FMLCommonHandler.instance().bus().register(inventoryHandler);
        MinecraftForge.EVENT_BUS.register(inventoryHandler); 
        
        ReanimationHandler reanimationHandler = CraftYourLifeRPMod.reanimationHandler;
        FMLCommonHandler.instance().bus().register(reanimationHandler);
        MinecraftForge.EVENT_BUS.register(reanimationHandler); 
        
        PlayerLifeHandler lifeHandler = new PlayerLifeHandler();
        FMLCommonHandler.instance().bus().register(lifeHandler);
        MinecraftForge.EVENT_BUS.register(lifeHandler); 
        
        NuclearCentralEvent nuclearEvent = new NuclearCentralEvent();
        FMLCommonHandler.instance().bus().register(nuclearEvent);
        MinecraftForge.EVENT_BUS.register(nuclearEvent); 
	}
	
	public EntityPlayer getPlayerEntity(MessageContext ctx) {
		return ctx.getServerHandler().playerEntity;
	}
	
	public String getPlayerName() {
		return Minecraft.getMinecraft().getSession().getUsername();
	}
	
	


}
