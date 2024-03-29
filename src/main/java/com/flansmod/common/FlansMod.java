package com.flansmod.common;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import net.minecraft.block.material.Material;
import net.minecraft.command.CommandHandler;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntitySkeleton;
import net.minecraft.entity.monster.EntityZombie;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.common.config.ConfigCategory;
import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.PlayerDropsEvent;

import com.flansmod.client.model.GunAnimations;
import com.flansmod.common.driveables.EntityPlane;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.EntityWheel;
import com.flansmod.common.driveables.ItemPlane;
import com.flansmod.common.driveables.ItemVehicle;
import com.flansmod.common.driveables.PlaneType;
import com.flansmod.common.driveables.VehicleType;
import com.flansmod.common.driveables.mechas.EntityMecha;
import com.flansmod.common.driveables.mechas.ItemMecha;
import com.flansmod.common.driveables.mechas.ItemMechaAddon;
import com.flansmod.common.driveables.mechas.MechaItemType;
import com.flansmod.common.driveables.mechas.MechaType;
import com.flansmod.common.eventhandlers.PlayerDeathEventListener;
import com.flansmod.common.guns.AAGunType;
import com.flansmod.common.guns.AttachmentType;
import com.flansmod.common.guns.BulletType;
import com.flansmod.common.guns.EntityAAGun;
import com.flansmod.common.guns.EntityBullet;
import com.flansmod.common.guns.EntityGrenade;
import com.flansmod.common.guns.EntityMG;
import com.flansmod.common.guns.GrenadeType;
import com.flansmod.common.guns.GunType;
import com.flansmod.common.guns.ItemAAGun;
import com.flansmod.common.guns.ItemAttachment;
import com.flansmod.common.guns.ItemBullet;
import com.flansmod.common.guns.ItemGrenade;
import com.flansmod.common.guns.ItemGun;
import com.flansmod.common.guns.boxes.BlockGunBox;
import com.flansmod.common.guns.boxes.GunBoxType;
import com.flansmod.common.network.PacketHandler;
import com.flansmod.common.parts.ItemPart;
import com.flansmod.common.parts.PartType;
import com.flansmod.common.teams.ArmourBoxType;
import com.flansmod.common.teams.ArmourType;
import com.flansmod.common.teams.BlockArmourBox;
import com.flansmod.common.teams.CommandTeams;
import com.flansmod.common.teams.EntityGunItem;
import com.flansmod.common.teams.ItemTeamArmour;
import com.flansmod.common.teams.Team;
import com.flansmod.common.tools.EntityParachute;
import com.flansmod.common.tools.ItemTool;
import com.flansmod.common.tools.ToolType;
import com.flansmod.common.types.EnumType;
import com.flansmod.common.types.InfoType;
import com.flansmod.common.types.TypeFile;

import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.Mod.Instance;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.event.FMLServerStartedEvent;
import cpw.mods.fml.common.event.FMLServerStartingEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.common.registry.EntityRegistry;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;


@Mod(modid = FlansMod.MODID, name = "Flan's Mod", version = FlansMod.VERSION, acceptableRemoteVersions = "@ALLOWEDVERSIONS@", dependencies="after:mw"
, guiFactory = "com.flansmod.client.gui.config.ModGuiFactory")
public class FlansMod
{
	//Core mod stuff
	public static boolean DEBUG = false;
    public static Configuration configFile;
	public static final String MODID = "flansmod";
	public static final String VERSION = "@VERSION@";
	@Instance(MODID)
	public static FlansMod INSTANCE;
    public static int generalConfigInteger = 32;
    public static String generalConfigString = "Hello!";
    public static boolean printDebugLog = true;
    public static boolean printStackTrace = false;
    public static int noticeSpawnKillTime = 10;
    public static float hitCrossHairColor[] = new float[]{ 1.0F, 1.0F, 1.0F, 1.0F };
    public static boolean addGunpowderRecipe = true;
    public static int teamsConfigInteger = 32;
    public static String teamsConfigString = "Hello!";
    public static boolean teamsConfigBoolean = false;
	@SidedProxy(clientSide = "com.flansmod.client.ClientProxy", serverSide = "com.flansmod.common.CommonProxy")
	public static CommonProxy proxy;
	//A standardised ticker for all bits of the mod to call upon if they need one
	public static int ticker = 0;
	public static long lastTime;
	public static File flanDir;
	public static File impoundLogFile;
	public static final float soundRange = 50F;
	public static final float driveableUpdateRange = 400F;
	public static final int numPlayerSnapshots = 20;

	public static int armourSpawnRate = 20;

	/** The spectator team. Moved here to avoid a concurrent modification error */
	public static Team spectators = new Team("spectators", "Spectators", 0x404040, '7');

	//Handlers
	public static final PacketHandler packetHandler = new PacketHandler();
	public static final PlayerHandler playerHandler = new PlayerHandler();
	public static final CommonTickHandler tickHandler = new CommonTickHandler();

	public final FlansModSettings flansmodSettings = new FlansModSettings();
	public static FlansHooks hooks = new FlansHooks();

	public static boolean isInFlash = false;
	public static int flashTime = 10;

	//Items and creative tabs
	public static BlockFlansWorkbench workbench;
	public static ArrayList<BlockGunBox> gunBoxBlocks = new ArrayList<BlockGunBox>();
	public static ArrayList<ItemBullet> bulletItems = new ArrayList<ItemBullet>();
	public static ArrayList<ItemGun> gunItems = new ArrayList<ItemGun>();
	public static ArrayList<ItemAttachment> attachmentItems = new  ArrayList<ItemAttachment>();
	public static ArrayList<ItemPart> partItems = new ArrayList<ItemPart>();
	public static ArrayList<ItemPlane> planeItems = new ArrayList<ItemPlane>();
	public static ArrayList<ItemVehicle> vehicleItems = new ArrayList<ItemVehicle>();
	public static ArrayList<ItemMechaAddon> mechaToolItems = new ArrayList<ItemMechaAddon>();
	public static ArrayList<ItemMecha> mechaItems = new ArrayList<ItemMecha>();
	public static ArrayList<ItemAAGun> aaGunItems = new ArrayList<ItemAAGun>();
	public static ArrayList<ItemGrenade> grenadeItems = new ArrayList<ItemGrenade>();
	public static ArrayList<ItemTool> toolItems = new ArrayList<ItemTool>();
	public static ArrayList<ItemTeamArmour> armourItems = new ArrayList<ItemTeamArmour>();
	public static ArrayList<BlockArmourBox> armourBoxBlocks = new ArrayList<BlockArmourBox>();
	public static CreativeTabFlan tabFlanGuns = new CreativeTabFlan(0), tabFlanDriveables = new CreativeTabFlan(1),
			tabFlanParts = new CreativeTabFlan(2), tabFlanTeams = new CreativeTabFlan(3), tabFlanMechas = new CreativeTabFlan(4);

	//Gun animations
	/** Gun animation variables for each entity holding a gun. Currently only applicable to the player */
	public static HashMap<EntityLivingBase, GunAnimations> gunAnimationsRight = new HashMap<EntityLivingBase, GunAnimations>(), gunAnimationsLeft = new HashMap<EntityLivingBase, GunAnimations>();


	/** The mod pre-initialiser method */
	@EventHandler
	public void preInit(FMLPreInitializationEvent event)
	{
		log("Preinitialising Flan's mod.");
        configFile = new Configuration(event.getSuggestedConfigurationFile());
        syncConfig(event.getSide());

		//TODO : Load properties
		//configuration = new Configuration(event.getSuggestedConfigurationFile());
		//loadProperties();

		flanDir = new File(event.getModConfigurationDirectory().getParentFile(), "/CYLFlan/");
		
		if (!flanDir.exists())
		{
			log("Flan folder not found. Creating empty folder.");
			log("You should get some content packs and put them in the Flan folder.");
			flanDir.mkdirs();
			flanDir.mkdir();
		}
		
		try
		{
			newImpoundFile();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		


		//Set up mod blocks and items
		workbench = (BlockFlansWorkbench)(new BlockFlansWorkbench(1, 0).setBlockName("flansWorkbench").setBlockTextureName("flansWorkbench"));
		GameRegistry.registerBlock(workbench, ItemBlockManyNames.class, "flansWorkbench");
		GameRegistry.addRecipe(new ItemStack(workbench, 1, 0), "BBB", "III", "III", 'B', Items.bowl, 'I', Items.iron_ingot );
		GameRegistry.addRecipe(new ItemStack(workbench, 1, 1), "ICI", "III", 'C', Items.cauldron, 'I', Items.iron_ingot );


		proxy.registerRenderers();

		//Read content packs
		readContentPacks(event);

		if(gunItems.size() >= 1)
		{
			MinecraftForge.EVENT_BUS.register(gunItems.get(0));
		}

		//Do proxy loading
		proxy.load();
		//Force Minecraft to reload all resources in order to load content pack resources.
		proxy.forceReload();

		log("Preinitializing complete.");
	}
	

	/** The mod initialiser method */
	@EventHandler
	public void init(FMLInitializationEvent event)
	{
		log("Initialising Flan's Mod.");

		//Initialising handlers
		packetHandler.initialise();
		NetworkRegistry.INSTANCE.registerGuiHandler(this, new CommonGuiHandler());

		// Recipes
		for (InfoType type : InfoType.infoTypes)
		{
			type.addRecipe();
		}
		if(addGunpowderRecipe)
		{
			ItemStack charcoal = new ItemStack(Items.coal, 1, 1);
			GameRegistry.addShapelessRecipe(new ItemStack(Items.gunpowder), charcoal, charcoal, charcoal, new ItemStack(Items.glowstone_dust));
		}
		log("Loaded recipes.");

		//Register teams mod entities
		EntityRegistry.registerGlobalEntityID(EntityGunItem.class, "GunItem", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityGunItem.class, "GunItem", 98, this, 100, 20, true);

		//Register driveables
		EntityRegistry.registerGlobalEntityID(EntityPlane.class, "Plane", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityPlane.class, "Plane", 90, this, 200, 3, true);
		EntityRegistry.registerGlobalEntityID(EntityVehicle.class, "Vehicle", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityVehicle.class, "Vehicle", 95, this, 400, 10, true);
		EntityRegistry.registerGlobalEntityID(EntitySeat.class, "Seat", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntitySeat.class, "Seat", 99, this, 250, 10, true);
		EntityRegistry.registerGlobalEntityID(EntityWheel.class, "Wheel", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityWheel.class, "Wheel", 103, this, 200, 20, true);
		EntityRegistry.registerGlobalEntityID(EntityParachute.class, "Parachute", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityParachute.class, "Parachute", 101, this, 40, 20, false);
		EntityRegistry.registerGlobalEntityID(EntityMecha.class, "Mecha", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityMecha.class, "Mecha", 102, this, 250, 20, false);

		//Register bullets and grenades
		EntityRegistry.registerGlobalEntityID(EntityBullet.class, "Bullet", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityBullet.class, "Bullet", 96, this, 200, 20, false);
		EntityRegistry.registerGlobalEntityID(EntityGrenade.class, "Grenade", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityGrenade.class, "Grenade", 100, this, 40, 100, true);

		//Register MGs and AA guns
		EntityRegistry.registerGlobalEntityID(EntityMG.class, "MG", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityMG.class, "MG", 91, this, 40, 5, true);
		EntityRegistry.registerGlobalEntityID(EntityAAGun.class, "AAGun", EntityRegistry.findGlobalUniqueEntityId());
		EntityRegistry.registerModEntity(EntityAAGun.class, "AAGun", 92, this, 40, 500, false);

		//Config
        FMLCommonHandler.instance().bus().register(INSTANCE);
        //Starting the EventListener
        new PlayerDeathEventListener();
		log("Loading complete.");
	}

	/** The mod post-initialisation method */
	@EventHandler
	public void postInit(FMLPostInitializationEvent event)
	{
		packetHandler.postInitialise();

		hooks.hook();
	}


	@SubscribeEvent
	public void playerDrops(PlayerDropsEvent event)
	{
		for(int i = event.drops.size() - 1; i >= 0; i--)
		{
			EntityItem ent = event.drops.get(i);
			InfoType type = InfoType.getType(ent.getEntityItem());
			if(type != null && !type.canDrop)
				event.drops.remove(i);
		}
	}

	@SubscribeEvent
	public void playerDrops(ItemTossEvent event)
	{
		InfoType type = InfoType.getType(event.entityItem.getEntityItem());
		if(type != null && !type.canDrop)
			event.setCanceled(true);
	}

	/** Teams command register method */
	@EventHandler
	public void registerCommand(FMLServerStartedEvent e)
	{
		CommandHandler handler = ((CommandHandler)FMLCommonHandler.instance().getSidedDelegate().getServer().getCommandManager());
		handler.registerCommand(new CommandTeams());
	}

    @SubscribeEvent
    public void onConfigChanged(ConfigChangedEvent.OnConfigChangedEvent eventArgs) {
        if(eventArgs.modID.equals(MODID))
            syncConfig(FMLCommonHandler.instance().getSide());
    }

	@SubscribeEvent
	public void onLivingSpecialSpawn(LivingSpawnEvent.CheckSpawn event)
	{
		int chance = event.world.rand.nextInt(101);

		if(chance < armourSpawnRate && (event.entityLiving instanceof EntityZombie || event.entityLiving instanceof EntitySkeleton))
		{
			if(event.world.rand.nextBoolean() && ArmourType.armours.size() > 0)
			{
				//Give a completely random piece of armour
				ArmourType armour = ArmourType.armours.get(event.world.rand.nextInt(ArmourType.armours.size()));
				if(armour != null && armour.type != 2)
					event.entityLiving.setCurrentItemOrArmor(armour.type + 1, new ItemStack(armour.item));
			}
			else if(Team.teams.size() > 0)
			{
				//Give a random set of armour
				Team team = Team.teams.get(event.world.rand.nextInt(Team.teams.size()));
				if(team.hat != null)
					event.entityLiving.setCurrentItemOrArmor(1, team.hat.copy());
				if(team.chest != null)
					event.entityLiving.setCurrentItemOrArmor(2, team.chest.copy());
				//if(team.legs != null)
				//	event.entityLiving.setCurrentItemOrArmor(3, team.legs.copy());
				if(team.shoes != null)
					event.entityLiving.setCurrentItemOrArmor(4, team.shoes.copy());
			}
		}
	}

	/** Reads type files from all content packs */
	private void getTypeFiles(List<File> contentPacks)
	{
		for (File contentPack : contentPacks)
		{
			if(contentPack.isDirectory())
			{
				for(EnumType typeToCheckFor : EnumType.values())
				{
					File typesDir = new File(contentPack, "/" + typeToCheckFor.folderName + "/");
					if(!typesDir.exists())
						continue;
					for(File file : typesDir.listFiles())
					{
						try
						{
							BufferedReader reader = new BufferedReader(new FileReader(file));
							String[] splitName = file.getName().split("/");
							TypeFile typeFile = new TypeFile(typeToCheckFor, splitName[splitName.length - 1].split("\\.")[0], contentPack.getName());
							for(;;)
							{
								String line = null;
								try
								{
									line = reader.readLine();
								}
								catch (Exception e)
								{
									break;
								}
								if (line == null)
									break;
								typeFile.lines.add(line);
							}
							reader.close();
						}
						catch(FileNotFoundException e)
						{
							e.printStackTrace();
						}
						catch(IOException e)
						{
							e.printStackTrace();
						}
					}
				}
			}
			else
			{
				try
				{
					ZipFile zip = new ZipFile(contentPack);
					ZipInputStream zipStream = new ZipInputStream(new FileInputStream(contentPack));
					BufferedReader reader = new BufferedReader(new InputStreamReader(zipStream));
					ZipEntry zipEntry = zipStream.getNextEntry();
					do
					{
						zipEntry = zipStream.getNextEntry();
						if(zipEntry == null)
							continue;
						TypeFile typeFile = null;
						for(EnumType type : EnumType.values())
						{
							if(zipEntry.getName().startsWith(type.folderName + "/") && zipEntry.getName().split(type.folderName + "/").length > 1 && zipEntry.getName().split(type.folderName + "/")[1].length() > 0)
							{
								String[] splitName = zipEntry.getName().split("/");
								typeFile = new TypeFile(type, splitName[splitName.length - 1].split("\\.")[0], contentPack.getName());
							}
						}
						if(typeFile == null)
						{
							continue;
						}
						for(;;)
						{
							String line = null;
							try
							{
								line = reader.readLine();
							}
							catch (Exception e)
							{
								break;
							}
							if (line == null)
								break;
							typeFile.lines.add(line);
						}
					}
					while(zipEntry != null);
					reader.close();
                    zip.close();
					zipStream.close();
				}
				catch(IOException e)
				{
					e.printStackTrace();
				}
			}
		}
	}

	/** Content pack reader method */
	private void readContentPacks(FMLPreInitializationEvent event)
	{
		// Icons, Skins, Models
		// Get the classloader in order to load the images
		ClassLoader classloader = (net.minecraft.server.MinecraftServer.class).getClassLoader();
		Method method = null;
		try
		{
			method = (java.net.URLClassLoader.class).getDeclaredMethod("addURL", java.net.URL.class);
			method.setAccessible(true);
		} catch (Exception e)
		{
			log("Failed to get class loader. All content loading will now fail.");
			if(FlansMod.printStackTrace)
			{
				e.printStackTrace();
			}
		}

		List<File> contentPacks = proxy.getContentList(method, classloader);

		if (!event.getSide().equals(Side.CLIENT))
		{
			//Gametypes (Server only)
			// TODO: gametype loader
		}

		getTypeFiles(contentPacks);

		for(EnumType type : EnumType.values())
		{
			Class<? extends InfoType> typeClass = type.getTypeClass();
			for(TypeFile typeFile : TypeFile.files.get(type))
			{
				try
				{
					InfoType infoType = (typeClass.getConstructor(TypeFile.class).newInstance(typeFile));
					infoType.read(typeFile);
					switch(type)
					{
					case bullet : bulletItems.add((ItemBullet)new ItemBullet((BulletType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case attachment : attachmentItems.add((ItemAttachment)new ItemAttachment((AttachmentType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case gun : gunItems.add((ItemGun)new ItemGun((GunType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case grenade : grenadeItems.add((ItemGrenade)new ItemGrenade((GrenadeType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case part : partItems.add((ItemPart)new ItemPart((PartType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case plane : planeItems.add((ItemPlane)new ItemPlane((PlaneType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case vehicle : vehicleItems.add((ItemVehicle)new ItemVehicle((VehicleType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case aa : aaGunItems.add((ItemAAGun)new ItemAAGun((AAGunType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case mechaItem : mechaToolItems.add((ItemMechaAddon)new ItemMechaAddon((MechaItemType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case mecha : mechaItems.add((ItemMecha)new ItemMecha((MechaType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case tool : toolItems.add((ItemTool)new ItemTool((ToolType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case box : gunBoxBlocks.add((BlockGunBox)new BlockGunBox((GunBoxType)infoType).setBlockName(infoType.shortName)); break;
					case armour : armourItems.add((ItemTeamArmour)new ItemTeamArmour((ArmourType)infoType).setUnlocalizedName(infoType.shortName)); break;
					case armourBox : armourBoxBlocks.add((BlockArmourBox)new BlockArmourBox((ArmourBoxType)infoType).setBlockName(infoType.shortName)); break;
					case playerClass : break;
					case team : break;
					default : log("Unrecognised type for " + infoType.shortName); break;
					}
				}
				catch(Exception e)
				{
					log("Failed to add " + type.name() + " : " + typeFile.name);
					if(FlansMod.printStackTrace)
					{
						e.printStackTrace();
					}
				}
			}
			log("Loaded " + type.name() + ".");
		}
		Team.spectators = spectators;
	}

	public static PacketHandler getPacketHandler()
	{
		return INSTANCE.packetHandler;
	}

    public static void syncConfig(Side side) {
    	printDebugLog = configFile.getBoolean("Print Debug Log", Configuration.CATEGORY_GENERAL, printDebugLog, "");
    	printStackTrace = configFile.getBoolean("Print Stack Trace", Configuration.CATEGORY_GENERAL, printStackTrace, "");
        addGunpowderRecipe = configFile.getBoolean("Gunpowder Recipe", Configuration.CATEGORY_GENERAL, addGunpowderRecipe, "Whether or not to add the extra gunpowder recipe (3 charcoal + 1 lightstone)");


        armourSpawnRate = configFile.getInt("ArmourSpawnRate",	Configuration.CATEGORY_GENERAL,  20, 0, 100, "The rate of Zombie or Skeleton to spawn equipped with armor. [0=0%, 100=100%]");

        noticeSpawnKillTime = configFile.getInt("NoticeSpawnKillTime",	Configuration.CATEGORY_GENERAL,  10, 0, 600, "Min(default=10)");

        FlansModSettings.instance.bulletSnapshotMin		= configFile.getInt("BltSS_Min",	Configuration.CATEGORY_GENERAL,  0, 0, 1000, "Min(default=0)");
        FlansModSettings.instance.bulletSnapshotDivisor	= configFile.getInt("BltSS_Divisor",Configuration.CATEGORY_GENERAL, 50, 0, 1000, "Divisor(default=50)");
        
        FlansModSettings.instance.driveablesBreakBlocks = configFile.getBoolean("Driveable_break_blocks",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.driveablesBreakBlocks, "Driveable break block(default=false)");
        FlansModSettings.instance.shellsEnabled = configFile.getBoolean("Shells_enabled",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.shellsEnabled, "Shells(default=false)");
        FlansModSettings.instance.bulletsEnabled = configFile.getBoolean("Bullet_enabled",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.bulletsEnabled, "Bullet enabled(default=true)");
        FlansModSettings.instance.bombsEnabled = configFile.getBoolean("Bombs_enabled",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.bombsEnabled, "Bombs enabled(default=true)");
        FlansModSettings.instance.vehiclesNeedFuel = configFile.getBoolean("Vehicle_need_fuel",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.vehiclesNeedFuel, "Vehicle need fuel(default=true)");
        FlansModSettings.instance.explosions = configFile.getBoolean("Explosions",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.explosions, "Explosions(default=false)");
        FlansModSettings.instance.canBreakGuns = configFile.getBoolean("Guns_can_break",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.canBreakGuns, "Guns can break(default=true)");
        FlansModSettings.instance.canBreakGlass = configFile.getBoolean("Glass_can_break",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.canBreakGlass, "Glass can break(default=false)");
        FlansModSettings.instance.weaponDrops = configFile.getInt("Weapon_drops",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.weaponDrops,0,3, "Weapon drops(default=false)");
        FlansModSettings.instance.destroyVehicleOnDisconnect = configFile.getBoolean("Destroy_Vehicle_Disconnection",Configuration.CATEGORY_GENERAL, FlansModSettings.instance.destroyVehicleOnDisconnect, "Destroy vehicle on owner disconnection (default=true)");

        
        for(int i=0; i<hitCrossHairColor.length; i++)
        {
        	final String[] COLOR = new String[]{ "Alpha", "Red", "Green", "Blue" };
        	hitCrossHairColor[i] = configFile.getFloat("HitCrossHairColor"+COLOR[i], Configuration.CATEGORY_GENERAL, hitCrossHairColor[i], 0.0F, 1.0F,
        			"Hit cross hair color "+COLOR[i]+"(default=1.0)");
        }
        

        if(configFile.hasChanged())
            configFile.save();
    }
    
    
    public static void updateBltssConfig(int min, int divisor)
    {
    	ConfigCategory category = configFile.getCategory(Configuration.CATEGORY_GENERAL);
    	if(category==null) return;
    	if(category.containsKey("BltSS_Min"))
    	{
    		category.get("BltSS_Min").set(min);
    	}
    	if(category.containsKey("BltSS_Divisor"))
    	{
    		category.get("BltSS_Divisor").set(divisor);
    	}
    	
        FlansModSettings.instance.bulletSnapshotMin = min;
        FlansModSettings.instance.bulletSnapshotDivisor	= divisor;
        configFile.save();
    }

	//TODO : Proper logger
	public static void log(String string)
	{
		if(printDebugLog)
		{
			System.out.println("[Flan's Mod] " + string);
		}
	}
	public static void log(String format, Object ... args)
	{
		log(String.format(format, args));
	}
	
	public static void newImpoundFile() throws IOException
	{
		impoundLogFile = new File(new File("logs"),"latest-impound.log");
		
		if(impoundLogFile.exists())
		{
			
			if(impoundLogFile.length() <= 0) return;
			
			int i = 1;
			Timestamp timestamp = new Timestamp(System.currentTimeMillis());
			File file;
			while(true)
			{
				String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(timestamp);
				file = new File(new File("logs"),"impound-" + formattedDate + "-" + i + ".log");
				
				if(!file.exists())
				{

					impoundLogFile.renameTo(file);
					impoundLogFile.createNewFile();
					return; 
				}
				
				i++;
			}
		}
		else
		{
			impoundLogFile.createNewFile();
		}
	}
}
