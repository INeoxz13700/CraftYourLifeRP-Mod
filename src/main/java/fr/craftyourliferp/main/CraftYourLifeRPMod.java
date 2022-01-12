package fr.craftyourliferp.main;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.lang.management.ManagementFactory;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;

import javax.management.InstanceNotFoundException;
import javax.management.MBeanException;
import javax.management.MalformedObjectNameException;
import javax.management.ObjectName;
import javax.management.ReflectionException;
import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;

import com.google.gson.Gson;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.blocks.BlockAtm;
import fr.craftyourliferp.blocks.BlockCashRegister;
import fr.craftyourliferp.blocks.BlockCorpseFreezer;
import fr.craftyourliferp.blocks.BlockInvisible;
import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.blocks.BlockRadar;
import fr.craftyourliferp.blocks.BlockRoad;
import fr.craftyourliferp.blocks.BlockRoadBalise;
import fr.craftyourliferp.blocks.BlockTrashCan;
import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityCashRegister;
import fr.craftyourliferp.blocks.tileentity.TileEntityCorpseFreezer;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import fr.craftyourliferp.blocks.tileentity.TileEntityRadar;
import fr.craftyourliferp.blocks.tileentity.TileEntityRoadBalise;
import fr.craftyourliferp.blocks.tileentity.TileEntityTrashCan;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityAtmRenderer;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityCashRegisterRenderer;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityRoadBaliseRenderer;
import fr.craftyourliferp.blocks.tileentity.renderer.TileEntityTrashCanRenderer;
import fr.craftyourliferp.capture.CaptureHandler;
import fr.craftyourliferp.capture.CaptureProcess;
import fr.craftyourliferp.commands.CommandApi;
import fr.craftyourliferp.commands.CommandBlackMarket;
import fr.craftyourliferp.commands.CommandBlacklist;
import fr.craftyourliferp.commands.CommandCosmetics;
import fr.craftyourliferp.commands.CommandIdentity;
import fr.craftyourliferp.commands.CommandMessage;
import fr.craftyourliferp.commands.CommandPenalty;
import fr.craftyourliferp.commands.CommandVehicle;
import fr.craftyourliferp.cosmetics.CosmeticManager;
import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.cosmetics.CosmeticsItem;
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
import fr.craftyourliferp.data.UserSession;
import fr.craftyourliferp.entities.EntityFootballBall;
import fr.craftyourliferp.entities.EntityItemCollider;
import fr.craftyourliferp.entities.EntityLootableBody;
import fr.craftyourliferp.entities.EntityStopStick;
import fr.craftyourliferp.fire.FireHandler;
import fr.craftyourliferp.game.events.EntityTrackerHandler;
import fr.craftyourliferp.game.events.EventsListener;
import fr.craftyourliferp.game.events.RendererListener;
import fr.craftyourliferp.items.*;
import fr.craftyourliferp.items.renderer.ItemAtmRenderer;
import fr.craftyourliferp.items.renderer.ItemCashRegisterRenderer;
import fr.craftyourliferp.items.renderer.ItemTrashCanRenderer;
import fr.craftyourliferp.network.PacketHandler;
import fr.craftyourliferp.pedagogical.PedagogicalManager;
import fr.craftyourliferp.radio.RadioHandler;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.LoadingCallback;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.Configuration;

@Mod(modid = CraftYourLifeRPMod.MODID, version = CraftYourLifeRPMod.VERSION, dependencies="after:flansmod")
public class CraftYourLifeRPMod {
	
	    public static CraftYourLifeRPMod instance;
	  
		public static final String name = "craftyourliferp";

	    public static Configuration configFile;
	    
	    public static String apiIp = "http://api.craftyourliferp.fr";

	    	      
		public static CreativeTabs CRPCreativeTabs;
	    

		public static final String MODID = "craftyourliferpmod";
		public static final String VERSION = "4.0.1.7";

		
		public static String[] startItem;
		public static int ATM = 1;
		public static int MASK_ID = 9401;
				
			 					
	    public static RadioHandler radioHandler = new RadioHandler();

		
		public static CosmeticManager cosmeticManager;
		
		
		public static final PacketHandler packetHandler = new PacketHandler();	
		
		public static final FireHandler fireHandler = new FireHandler();
		public static final EntityTrackerHandler entityTrackerHandler = new EntityTrackerHandler();
		public static final CaptureHandler captureHander = new CaptureHandler();
		
		//public static HalloweenEvent halloweenEvent;
				
		private static CraftYourLifeRPClient cylrpClient;
		
		public static boolean localhost = false;
		
		
		
		
		
		@SidedProxy(clientSide = "fr.craftyourliferp.main.ClientProxy", serverSide = "fr.craftyourliferp.main.CommonProxy")
	   	public static CommonProxy proxy;
		
	    
	    @Mod.EventHandler
		public void preInit(FMLPreInitializationEvent event) {
	    	
	    	instance = this;
	    	
	    	if(event.getSide().isClient())
	    	{
	    		cylrpClient = new CraftYourLifeRPClient();
	    	}
    		captureHander.registerCapturesType();

	    	
    		//Effect.registeredEffects.add(new ScreamEffect());
	    	CRPCreativeTabs = new CRPTab("crp_creative_tabs"){}.setNoTitle().setBackgroundImageName("c_creativetabs.png");
	        
	    	
	    	ModdedItems.registerItems();
	        ModdedItems.registerBlocks();
	        
	        registerItemsCraft();
	        registerCosmetics(event.getSide());
	        
	         
	    	configFile = new Configuration(event.getSuggestedConfigurationFile());
	        
	    	syncConfig(event.getSide());
		}
		
		
	    private void registerCosmetics(Side side) {
			cosmeticManager = new CosmeticManager();
			
			ModdedItems.birdCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.birdCosmetic, "item_bird");
			CosmeticObject bird = cosmeticManager.registerCosmetic("Bird", false, (byte)2,0,new ItemStack(ModdedItems.birdCosmetic,1));
			
			ModdedItems.minccinoCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.minccinoCosmetic, "item_mincinno");
			CosmeticObject mincinno = cosmeticManager.registerCosmetic("PokemonMinccino", false, (byte)2,1,new ItemStack(ModdedItems.minccinoCosmetic,1));
			
			ModdedItems.smithyHatCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.smithyHatCosmetic, "item_smithyHat");
			CosmeticObject smithyHat = cosmeticManager.registerCosmetic("Smithy Hat", false, (byte)0,2,new ItemStack(ModdedItems.smithyHatCosmetic,1));
			
			ModdedItems.cylrpGlassesCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.cylrpGlassesCosmetic, "item_cylrp_glasses");
			CosmeticObject cylrpGlasses = cosmeticManager.registerCosmetic("Lunette d'anniversaire de CYLRP §62020", false, (byte)1,3,new ItemStack(ModdedItems.cylrpGlassesCosmetic,1));
			
			ModdedItems.witchHatCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.witchHatCosmetic, "item_witch_hat");
			CosmeticObject witchHat = cosmeticManager.registerCosmetic("Chapeau de sorcière", false, (byte)0
					,4,new ItemStack(ModdedItems.witchHatCosmetic,1));
			
			ModdedItems.voxelCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.voxelCosmetic, "item_voxel");
			CosmeticObject voxel = cosmeticManager.registerCosmetic("voxelPet", false, (byte)2,5,new ItemStack(ModdedItems.voxelCosmetic,1));
			
			ModdedItems.snowmanCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.snowmanCosmetic, "item_snowman");
			CosmeticObject snowmanCosmetic = cosmeticManager.registerCosmetic("Bonhomme de neige", false, (byte)2,6,new ItemStack(ModdedItems.snowmanCosmetic,1));
			
			ModdedItems.noelBonnetCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.noelBonnetCosmetic, "item_noelbonnet");
			CosmeticObject noelBonnetCosmetic = cosmeticManager.registerCosmetic("Bonnet de noel", false, (byte)0,7,new ItemStack(ModdedItems.noelBonnetCosmetic,1));
			
			ModdedItems.rabbitEarsCosmetic = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.rabbitEarsCosmetic, "item_rabbitears");
			CosmeticObject rabbitEarsCosmetic = cosmeticManager.registerCosmetic("Oreilles de lapin", false, (byte)0,8,new ItemStack(ModdedItems.rabbitEarsCosmetic,1));
			
			ModdedItems.heliceHat = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.heliceHat, "item_helicehat");
			CosmeticObject heliceHatCosmetic = cosmeticManager.registerCosmetic("Casquette de baseball", false, (byte)0,9,new ItemStack(ModdedItems.heliceHat,1));
			
			ModdedItems.threeDGlasses = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.threeDGlasses, "item_3d_glasses");
			CosmeticObject threeDGlassesCosmetic = cosmeticManager.registerCosmetic("Lunettes 3d", false, (byte)1,10,new ItemStack(ModdedItems.threeDGlasses,1));
			
			ModdedItems.aureole = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.aureole, "item_aureole");
			CosmeticObject aureoleCosmetic = cosmeticManager.registerCosmetic("Auréole", false, (byte)0,11,new ItemStack(ModdedItems.aureole,1));
			
			ModdedItems.marioHat = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.marioHat, "item_mariohat");
			CosmeticObject marioHatCosmetic = cosmeticManager.registerCosmetic("Chapeau Mario", false, (byte)0,12,new ItemStack(ModdedItems.marioHat,1));
			
			ModdedItems.jesterHat = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.jesterHat, "item_jesterhat");
			CosmeticObject jesterHatCosmetic = cosmeticManager.registerCosmetic("Chapeau Fou du roi", false, (byte)0,13,new ItemStack(ModdedItems.jesterHat,1));
			
			ModdedItems.swordOnHead = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.swordOnHead, "item_swordOnHead");
			CosmeticObject swordOnHeadCosmetic = cosmeticManager.registerCosmetic("Epee sur la tête", false, (byte)0,14,new ItemStack(ModdedItems.swordOnHead,1));
			
			ModdedItems.alienMask = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.alienMask, "item_alienMask");
			CosmeticObject alienMaskCosmetic = cosmeticManager.registerCosmetic("Masque d'alien", false, (byte)1,15,new ItemStack(ModdedItems.alienMask,1));
			
			ModdedItems.clownHead = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.clownHead, "item_clownHead");
			CosmeticObject clownHeadCosmetic = cosmeticManager.registerCosmetic("Tête de clown", false, (byte)0,16,new ItemStack(ModdedItems.clownHead,1));
			
			ModdedItems.witchHat2 = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.witchHat2, "item_witchHat2");
			CosmeticObject witchHat2Cosmetic = cosmeticManager.registerCosmetic("Chapeau de sorcière 2", false, (byte)0,17,new ItemStack(ModdedItems.witchHat2,1));
			
			ModdedItems.devilHorn = new CosmeticsItem();
			GameRegistry.registerItem(ModdedItems.devilHorn, "item_devilHorn");
			CosmeticObject devilHornCosmetic = cosmeticManager.registerCosmetic("Cornes de diable", false, (byte)0,18,new ItemStack(ModdedItems.devilHorn,1));
			
			
			
			
			if(side == Side.CLIENT)
			{
				bird.setModel(new ModelBird());
				voxel.setModel(new ModelVoxel());
				mincinno.setModel(new ModelMincinno());
				smithyHat.setModel(new ModelSmithyHat());
				cylrpGlasses.setModel(new ModelCylrpGlasses());
				witchHat.setModel(new ModelWitchHat());
				snowmanCosmetic.setModel(new ModelSnowman());
				noelBonnetCosmetic.setModel(new ModelNoelBonnet());
				rabbitEarsCosmetic.setModel(new ModelRabbitEars());
				heliceHatCosmetic.setModel(new ModelPropellerHat());
				threeDGlassesCosmetic.setModel(new Model3DGlasses());
				aureoleCosmetic.setModel(new ModelAureole());
				marioHatCosmetic.setModel(new ModelMarioHat());
				jesterHatCosmetic.setModel(new ModelJesterHat());
				swordOnHeadCosmetic.setModel(new ModelSwordOnHead());
				alienMaskCosmetic.setModel(new ModelAlienMask());
				clownHeadCosmetic.setModel(new ModelClownHead());
				witchHat2Cosmetic.setModel(new ModelWitchHat2());
				devilHornCosmetic.setModel(new ModelDevilHorn());
			}
			
		}
	    

		@Mod.EventHandler
	    public void init(FMLInitializationEvent event) {
			
	    	packetHandler.initialise();
	        
			registerEntity("lootableBody", EntityLootableBody.class);
			registerEntity("football_ball",EntityFootballBall.class);
			registerEntity("item_collider",EntityItemCollider.class);
			registerEntity("stopstick",EntityStopStick.class);

			
	    	proxy.init();
			
			File dataFolder = new File("NumberData/");
			
			if(!dataFolder.exists()) dataFolder.mkdir();
	    }
	    
		@Mod.EventHandler
		public void serverLoad(FMLServerStartingEvent event)
		{			
		
			event.registerServerCommand(new CommandIdentity());
			event.registerServerCommand(new CommandMessage());
			event.registerServerCommand(new CommandCosmetics());
			event.registerServerCommand(new CommandBlackMarket());
			event.registerServerCommand(new CommandApi());
	        event.registerServerCommand(new CommandVehicle());
	        event.registerServerCommand(new CommandPenalty());
	        event.registerServerCommand(new CommandBlacklist());

	        
	        CraftYourLifeRPMod.fireHandler.fires.clear();
	        
	        //halloweenEvent = new HalloweenEvent(event.getSide());
		}
		
		@SubscribeEvent
		public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
		    if(eventArgs.modID.equals(MODID))
		    {
		       syncConfig();
		    }
		}
		    
		public static void syncConfig() {
		    if(configFile != null && configFile.hasChanged())
		    	configFile.save();
		}
		    
		    public static void syncConfig(Side side) {		   	 	
		    	if(side.isServer())
		    	{
			    	startItem = configFile.getStringList("Item de depart", Configuration.CATEGORY_GENERAL, new String[]{"1-1"}, "ItemId-Quantite (Item de depart)");
			    	ATM = configFile.getInt(name, Configuration.CATEGORY_GENERAL, 0, 0, 999999, "Id atm");    	
			    	if(configFile != null && configFile.hasChanged())configFile.save();
		    	}
		   }
	    

	    
	    private void registerItemsCraft() 
	    {
			GameRegistry.addRecipe(new ItemStack(ModdedItems.KSamsung,1),
					"OSO",
					"DSD",
					"OSO",
					'O', Blocks.obsidian,
					'S', Items.slime_ball,
					'D', Items.diamond
			);
			GameRegistry.addRecipe(new ItemStack(ModdedItems.BulletGilet,1),
					"DSD",
					"SSS",
					"DSD",
					'S', Items.slime_ball,
					'D', Items.diamond
			);
		}
	    
	    
	    
	    private int entityId = 0;
		private void registerEntity(String entityName,Class entityClass) {
			String idName = MODID + "_" + entityName;
			EntityRegistry.registerGlobalEntityID(entityClass, idName, EntityRegistry.findGlobalUniqueEntityId());
			EntityRegistry.registerModEntity(entityClass, idName, entityId++/* id */, this, 64/* trackingRange */,10/* updateFrequency */, true/* sendsVelocityUpdates */);
		}
	    
	    public static CosmeticManager getCosmeticManager() 
	    {
	    	return cosmeticManager;
	    }

	    public static CraftYourLifeRPClient getClientData()
	    {
	    	return cylrpClient;
	    }
	    
	   

}
