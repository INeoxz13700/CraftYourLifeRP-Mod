package fr.craftyourliferp.main;

import java.awt.Color;
import java.awt.event.KeyEvent;
import java.io.IOException;
import java.net.URL;

import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.Display;
import org.lwjgl.opengl.GL11;


import api.player.model.ModelPlayerAPI;
import api.player.render.RenderPlayerAPI;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.network.simpleimpl.MessageContext;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.nicoszpako.armamania.common.ArmaMania;
import fr.craftyourliferp.animations.AnimationManager;
import fr.craftyourliferp.animations.CoucouAnimation;
import fr.craftyourliferp.animations.DanceAnimation;
import fr.craftyourliferp.animations.HandCuffedAnimation;
import fr.craftyourliferp.animations.HandUpAnimation;
import fr.craftyourliferp.blocks.render.RenderRoadBlock;
import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityCashRegister;
import fr.craftyourliferp.blocks.tileentity.TileEntityComputer;
import fr.craftyourliferp.blocks.tileentity.TileEntityCorpseFreezer;
import fr.craftyourliferp.blocks.tileentity.TileEntityGoldIngot;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import fr.craftyourliferp.blocks.tileentity.TileEntityRadar;
import fr.craftyourliferp.blocks.tileentity.TileEntityRoadBalise;
import fr.craftyourliferp.blocks.tileentity.TileEntityTrashCan;
import fr.craftyourliferp.blocks.tileentity.renderer.RenderTileEntityComputer;
import fr.craftyourliferp.blocks.tileentity.renderer.RenderTileEntityGoldIngot;
import fr.craftyourliferp.blocks.tileentity.renderer.RenderTileEntityPainting;
import fr.craftyourliferp.blocks.tileentity.renderer.RenderTileEntityRadar;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityAtmRenderer;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityCashRegisterRenderer;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityCorpseFreezerRenderer;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityRoadBaliseRenderer;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityTrashCanRenderer;
import fr.craftyourliferp.cosmetics.Model3DGlasses;
import fr.craftyourliferp.cosmetics.ModelAlienMask;
import fr.craftyourliferp.cosmetics.ModelAureole;
import fr.craftyourliferp.cosmetics.ModelBird;
import fr.craftyourliferp.cosmetics.ModelClownHead;
import fr.craftyourliferp.cosmetics.ModelCylrpGlasses;
import fr.craftyourliferp.cosmetics.ModelDevilHorn;
import fr.craftyourliferp.cosmetics.ModelJesterHat;
import fr.craftyourliferp.cosmetics.ModelMarioHat;
import fr.craftyourliferp.cosmetics.ModelMincinno;
import fr.craftyourliferp.cosmetics.ModelNoelBonnet;
import fr.craftyourliferp.cosmetics.ModelPropellerHat;
import fr.craftyourliferp.cosmetics.ModelRabbitEars;
import fr.craftyourliferp.cosmetics.ModelSmithyHat;
import fr.craftyourliferp.cosmetics.ModelSnowman;
import fr.craftyourliferp.cosmetics.ModelSwordOnHead;
import fr.craftyourliferp.cosmetics.ModelVoxel;
import fr.craftyourliferp.cosmetics.ModelWitchHat;
import fr.craftyourliferp.cosmetics.ModelWitchHat2;
import fr.craftyourliferp.entities.EntityFootballBall;
import fr.craftyourliferp.entities.EntityItemCollider;
import fr.craftyourliferp.entities.EntityLootableBody;
import fr.craftyourliferp.entities.EntityStopStick;
import fr.craftyourliferp.entities.renderer.RenderCollisionEntity;
import fr.craftyourliferp.entities.renderer.RenderCustomPlayer;
import fr.craftyourliferp.entities.renderer.RenderFootballBall;
import fr.craftyourliferp.entities.renderer.RenderSkinnedBody;
import fr.craftyourliferp.entities.renderer.RenderStopStick;
import fr.craftyourliferp.game.events.CollisionListener;
import fr.craftyourliferp.game.events.EventsListener;
import fr.craftyourliferp.game.events.GuiContainerHandler;
import fr.craftyourliferp.game.events.MouseListener;
import fr.craftyourliferp.game.events.OverlayRendererListener;
import fr.craftyourliferp.game.events.PlayerItemInteractionListener;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.game.events.TicksHandler;
import fr.craftyourliferp.ingame.gui.CylrpMessageHUD;
import fr.craftyourliferp.items.ItemBulletproofShield;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.items.renderer.ItemAtmRenderer;
import fr.craftyourliferp.items.renderer.ItemBulletproofShieldRenderer;
import fr.craftyourliferp.items.renderer.ItemCashRegisterRenderer;
import fr.craftyourliferp.items.renderer.ItemCigaretteRenderer;
import fr.craftyourliferp.items.renderer.ItemCorpseFreezerRenderer;
import fr.craftyourliferp.items.renderer.ItemCosmeticRenderer;
import fr.craftyourliferp.items.renderer.ItemExtincteurRenderer;
import fr.craftyourliferp.items.renderer.ItemGoldIngotRenderer;
import fr.craftyourliferp.items.renderer.ItemPaintingRenderer;
import fr.craftyourliferp.items.renderer.ItemRoadBaliseRenderer;
import fr.craftyourliferp.items.renderer.ItemStopStickRenderer;
import fr.craftyourliferp.items.renderer.ItemTileEntityComputerRenderer;
import fr.craftyourliferp.items.renderer.ItemTrashCanRenderer;
import fr.craftyourliferp.mainmenu.gui.GuiCustomIngameMenu;
import fr.craftyourliferp.models.CustomModelPlayer;
import fr.craftyourliferp.pedagogical.PedagogicalManager;
import fr.craftyourliferp.thirst.ThirstEventHandler;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.MathsUtils;
import fr.craftyourliferp.utils.MinecraftUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityClientPlayerMP;
import net.minecraft.client.gui.FontRenderer;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiIngameMenu;
import net.minecraft.client.gui.GuiScreen;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.client.renderer.Tessellator;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.client.settings.GameSettings;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;

public class ClientProxy extends CommonProxy {
		
	public static KeyBinding[] keyBindings = new KeyBinding[3];
	
	public static KeyBinding proningKeyBinding;
	
	public static boolean forceExit = false;
	
	public static int renderRoadBlock;

	 
	public void init()
	{    	
    	Display.setTitle("CraftYourLifeRP v4.5");
		
		registerKeyBindings();
	    registerHandlers();
	    
	    registerRenderers();
	    registerAnimations();
	    registerTileEntitiesRenderers();
	     
		RenderManager rm = RenderManager.instance;

	    ModelPlayerAPI.register(CraftYourLifeRPMod.MODID, CustomModelPlayer.class);    
	    RenderPlayerAPI.register(CraftYourLifeRPMod.MODID, RenderCustomPlayer.class);    

	    
		RenderingRegistry.registerEntityRenderingHandler(EntityLootableBody.class, new RenderSkinnedBody(rm));
		RenderingRegistry.registerEntityRenderingHandler(EntityFootballBall.class, new RenderFootballBall());
		RenderingRegistry.registerEntityRenderingHandler(EntityItemCollider.class, new RenderCollisionEntity());
		RenderingRegistry.registerEntityRenderingHandler(EntityStopStick.class, new RenderStopStick());

		new AntiCheatHelper().startAntiCheat();
	}

	private void registerAnimations()
	{
		AnimationManager.registerAnimation(HandUpAnimation.class);
		AnimationManager.registerAnimation(HandCuffedAnimation.class);
		AnimationManager.registerAnimation(CoucouAnimation.class);
		AnimationManager.registerAnimation(DanceAnimation.class);
	}

	private void registerHandlers()
    {		
		CylrpMessageHUD messageHandler = new CylrpMessageHUD();
		FMLCommonHandler.instance().bus().register(messageHandler);
        MinecraftForge.EVENT_BUS.register(messageHandler);

		
        EventsListener listener = new EventsListener();
    	MinecraftForge.EVENT_BUS.register(listener);
        FMLCommonHandler.instance().bus().register(listener);
        
        ThirstEventHandler thirstListener = new ThirstEventHandler();
    	MinecraftForge.EVENT_BUS.register(thirstListener);
        FMLCommonHandler.instance().bus().register(thirstListener);
        
    	RendererListener rendererListener = new RendererListener();
    	FMLCommonHandler.instance().bus().register(rendererListener);
        MinecraftForge.EVENT_BUS.register(rendererListener);
        
        OverlayRendererListener overlayListener = new OverlayRendererListener();
    	FMLCommonHandler.instance().bus().register(overlayListener);
        MinecraftForge.EVENT_BUS.register(overlayListener);
        
        MouseListener mouseListener = new MouseListener();
    	FMLCommonHandler.instance().bus().register(mouseListener);
        MinecraftForge.EVENT_BUS.register(mouseListener);
        
        PlayerItemInteractionListener interactionListener = new PlayerItemInteractionListener();
    	FMLCommonHandler.instance().bus().register(interactionListener);
        MinecraftForge.EVENT_BUS.register(interactionListener);
        
        CollisionListener collisionListener = new CollisionListener();
    	FMLCommonHandler.instance().bus().register(collisionListener);
        MinecraftForge.EVENT_BUS.register(collisionListener);
        
        TicksHandler ticksListener = new TicksHandler();
    	FMLCommonHandler.instance().bus().register(ticksListener);
        MinecraftForge.EVENT_BUS.register(ticksListener);
        
        /*EffectRenderer effectsListener = new EffectRenderer();
    	FMLCommonHandler.instance().bus().register(effectsListener);
        MinecraftForge.EVENT_BUS.register(effectsListener);*/
        
    	FMLCommonHandler.instance().bus().register(CraftYourLifeRPMod.entityTrackerHandler);
        MinecraftForge.EVENT_BUS.register(CraftYourLifeRPMod.entityTrackerHandler);
        
      	FMLCommonHandler.instance().bus().register(CraftYourLifeRPMod.fireHandler);
        MinecraftForge.EVENT_BUS.register(CraftYourLifeRPMod.fireHandler); 
        
        
      	FMLCommonHandler.instance().bus().register(CraftYourLifeRPMod.captureHander);
        MinecraftForge.EVENT_BUS.register(CraftYourLifeRPMod.captureHander); 
    
        NetworkRegistry.INSTANCE.registerGuiHandler(CraftYourLifeRPMod.instance, new GuiContainerHandler());
    }
	
	public void registerRenderers() 
	{
		
        renderRoadBlock = RenderingRegistry.getNextAvailableRenderId();
        RenderingRegistry.registerBlockHandler(renderRoadBlock, new RenderRoadBlock());

		
		MinecraftForgeClient.registerItemRenderer(ModdedItems.extincteur, new ItemExtincteurRenderer());
		MinecraftForgeClient.registerItemRenderer(ModdedItems.itemBulletproofShield, new ItemBulletproofShieldRenderer());
		
		
		MinecraftForgeClient.registerItemRenderer(ModdedItems.birdCosmetic, new ItemCosmeticRenderer(new ModelBird(), Vec3.createVectorHelper(-0.4, -0.12, 0F), Vec3.createVectorHelper(5, 5, 5), Vec3.createVectorHelper(0, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.voxelCosmetic, new ItemCosmeticRenderer(new ModelVoxel(), Vec3.createVectorHelper(0.3 / 0.07, -1.2 / 0.07, 0), Vec3.createVectorHelper(0.07, 0.07, 0.07), Vec3.createVectorHelper(0, 180, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.minccinoCosmetic, new ItemCosmeticRenderer(new ModelMincinno(), Vec3.createVectorHelper(3, -0.12, 0), Vec3.createVectorHelper(0.1, 0.1, 0.1), Vec3.createVectorHelper(0, 200, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.smithyHatCosmetic, new ItemCosmeticRenderer(new ModelSmithyHat(), Vec3.createVectorHelper(2.5, -0.12, 0), Vec3.createVectorHelper(0.08, 0.08, 0.08), Vec3.createVectorHelper(0, 180, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.cylrpGlassesCosmetic, new ItemCosmeticRenderer(new ModelCylrpGlasses(), Vec3.createVectorHelper(-0.01, -0.1, 0), Vec3.createVectorHelper(3.5, 3.5, 3.5), Vec3.createVectorHelper(0, 40, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.witchHatCosmetic, new ItemCosmeticRenderer(new ModelWitchHat(), Vec3.createVectorHelper(0, 0F, 0F), Vec3.createVectorHelper(2, 2, 2), Vec3.createVectorHelper(0, 180, 0)));

		MinecraftForgeClient.registerItemRenderer(ModdedItems.noelBonnetCosmetic, new ItemCosmeticRenderer(new ModelNoelBonnet(), Vec3.createVectorHelper(0, 0.4, 0), Vec3.createVectorHelper(4, 4, 4), Vec3.createVectorHelper(0, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.snowmanCosmetic, new ItemCosmeticRenderer(new ModelSnowman(), Vec3.createVectorHelper(0, 0.24, 0), Vec3.createVectorHelper(3.5, 3.5, 3.5), Vec3.createVectorHelper(0, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.heliceHat, new ItemCosmeticRenderer(new ModelPropellerHat(), Vec3.createVectorHelper(0, -0.3, 0), Vec3.createVectorHelper(1.5, 1.5, 1.5), Vec3.createVectorHelper(0, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.rabbitEarsCosmetic, new ItemCosmeticRenderer(new ModelRabbitEars(), Vec3.createVectorHelper(0.05, -0.4, 0), Vec3.createVectorHelper(2, 2, 2), Vec3.createVectorHelper(0, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.threeDGlasses, new ItemCosmeticRenderer(new Model3DGlasses(), Vec3.createVectorHelper(-0.01, -0.17, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.aureole, new ItemCosmeticRenderer(new ModelAureole(), Vec3.createVectorHelper(-0.02, 0.1, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(0, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.marioHat, new ItemCosmeticRenderer(new ModelMarioHat(), Vec3.createVectorHelper(-0.02, -0.15, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.jesterHat, new ItemCosmeticRenderer(new ModelJesterHat(), Vec3.createVectorHelper(-0.01, -0.16, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		
		MinecraftForgeClient.registerItemRenderer(ModdedItems.swordOnHead, new ItemCosmeticRenderer(new ModelSwordOnHead(), Vec3.createVectorHelper(-0.06, 0.08, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.alienMask, new ItemCosmeticRenderer(new ModelAlienMask(), Vec3.createVectorHelper(-0.02, 0.08, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.clownHead, new ItemCosmeticRenderer(new ModelClownHead(), Vec3.createVectorHelper(-0.01, -0.18, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.witchHat2, new ItemCosmeticRenderer(new ModelWitchHat2(), Vec3.createVectorHelper(-0.02, 0.10, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		MinecraftForgeClient.registerItemRenderer(ModdedItems.devilHorn, new ItemCosmeticRenderer(new ModelDevilHorn(), Vec3.createVectorHelper(0, 0.08, 0), Vec3.createVectorHelper(10, 10, 10), Vec3.createVectorHelper(180, 0, 0)));
		

		MinecraftForgeClient.registerItemRenderer(ModdedItems.itemStopStickSize2, new ItemStopStickRenderer());
		MinecraftForgeClient.registerItemRenderer(ModdedItems.itemStopStickSize4, new ItemStopStickRenderer());
		MinecraftForgeClient.registerItemRenderer(ModdedItems.itemStopStickSize6, new ItemStopStickRenderer());
		
		MinecraftForgeClient.registerItemRenderer(ArmaMania.aw, new ItemCigaretteRenderer());
		

	}
	
    public void registerTileEntitiesRenderers()
    {
	    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAtm.class, new TileEntityAtmRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.AtmBlock), new ItemAtmRenderer());
	    	
	    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityTrashCan.class, new TileEntityTrashCanRenderer());
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.TrashCanBlock), new ItemTrashCanRenderer());
		
		TileEntityCorpseFreezerRenderer renderer = new TileEntityCorpseFreezerRenderer();
	    ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCorpseFreezer.class, renderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.CorpseFreezerBlock), new ItemCorpseFreezerRenderer(renderer));
		
		RenderTileEntityGoldIngot rendererGoldIngot = new RenderTileEntityGoldIngot();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityGoldIngot.class, rendererGoldIngot);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.GoldIngotBlock), new ItemGoldIngotRenderer(rendererGoldIngot));
		
		RenderTileEntityPainting rendererPainting = new RenderTileEntityPainting();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityPainting.class, rendererPainting);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.PaintingBlock), new ItemPaintingRenderer(rendererPainting));


		TileEntityRoadBaliseRenderer roadbaliseRenderer = new TileEntityRoadBaliseRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRoadBalise.class, roadbaliseRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.RoadBaliseBlock), new ItemRoadBaliseRenderer(roadbaliseRenderer));


		TileEntityCashRegisterRenderer cashRegisterRenderer = new TileEntityCashRegisterRenderer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityCashRegister.class, cashRegisterRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.CashRegisterBlock), new ItemCashRegisterRenderer(cashRegisterRenderer));
		
		RenderTileEntityComputer computerRenderer = new RenderTileEntityComputer();
		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityComputer.class, computerRenderer);
		MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModdedItems.ComputerBlock), new ItemTileEntityComputerRenderer(computerRenderer));

		ClientRegistry.bindTileEntitySpecialRenderer(TileEntityRadar.class, new RenderTileEntityRadar());
    }
	
	public static void exit()
	{
		Runtime.getRuntime().exit(0);
	}
	
	public void registerKeyBindings()
	{
	    keyBindings[0] = new KeyBinding("Utiliser", Keyboard.KEY_A, "key.cylrp.rp");
	    keyBindings[1] = new KeyBinding("Animations", Keyboard.KEY_B, "key.cylrp.rp");
	    keyBindings[2] = new KeyBinding("Optimisation FPS", Keyboard.KEY_O, "key.cylrp.rp");

	      
	    for (int i = 0; i < keyBindings.length; ++i) 
	    {
	       ClientRegistry.registerKeyBinding(keyBindings[i]);
	    }
	    
	    proningKeyBinding = MinecraftUtils.getKeyBinding("key.proning");
	}
	

     
   
    
}
	

