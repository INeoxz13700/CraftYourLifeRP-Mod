package astrotibs.notenoughpets.config;

import java.io.File;

import astrotibs.notenoughpets.util.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

public class GeneralConfig
{
	public static final String CAT_GENERAL = "general";
	public static final String CAT_MISC = "miscellaneous";
	public static final String CAT_EMERGENCY_UNDO = "emergency undo";
	public static final String CAT_ENABLE_NEC_ANIMALS = "enable nec animals";
	public static final String CAT_PARROT_IMITATION = "parrot imitation";
	
	public static Configuration config;
	
	public static int soundOccurrenceCat;
	public static int soundOccurrenceDog;
	public static int soundOccurrenceParrotAmbient;
	public static int soundOccurrenceParrotImitate;
	
	public static int villageSpawnCapCat;
	public static int villageSpawnCapDog;
	public static boolean strayCapFromVillageRadius;
	
	public static boolean versionChecker;
	public static boolean debugMessages;
	public static boolean swampHutCat;
	public static boolean swampHutCatName;
	
	public static int villageSpawnRatePercent;

	public static float strayFractionCat;
	
	public static int[] strayDimensions;

	public static String[] parrotMimicSounds;
	
	public static boolean followOwnerCat;
	public static boolean followOwnerDog;
	public static boolean followOwnerParrot;

	public static float followTeleportCat;
	public static float followTeleportDog;
	public static float followTeleportParrot;

	public static boolean healingFoodCat;
	public static boolean healingFoodParrot;
	public static boolean cookiesKillParrots;
	public static float parrotDismountFallHeight;
	
	public static boolean enableParrotMimicry;
	
	public final static float MAX_FOOD = 20f;
	public final static float MAX_HYGIENE = 20f;
	public final static float MAX_ENERGY = 20f;

	public static void init(File configFile)
	{
		if (config == null)
		{
			config = new Configuration(configFile);
			loadConfiguration();
		}
	}

	// Config file explanations
	public static void loadConfiguration() {
		

		
		// ---------------------------- //
		// --------- General ---------- //
		// ---------------------------- //
		
		soundOccurrenceCat = config.getInt("Idle Sound Occurrence: Cat", CAT_GENERAL, 25, 0, 100, "Determines the frequency of occurrence of the idle sounds that a tamed cat makes as a percentage of typical Minecraft mob sound frequency. "
				+ "Set this to 50 for 50 percent, etc.");
		
		soundOccurrenceDog = config.getInt("Idle Sound Occurrence: Dog", CAT_GENERAL, 50, 0, 100, "Determines the frequency of occurrence of the idle sounds that a tamed dog makes as a percentage of typical Minecraft mob sound frequency. "
				+ "Set this to 50 for 50 percent, etc.");

		
		soundOccurrenceParrotAmbient = config.getInt("Idle Sound Occurrence: Parrot (Ambient)", CAT_GENERAL, 100, 0, 100, "Determines the frequency of occurrence of the idle sounds that a tamed parrot makes as a percentage of typical Minecraft mob sound frequency. "
				+ "Set this to 50 for 50 percent, etc.");
		soundOccurrenceParrotImitate = config.getInt("Idle Sound Occurrence: Parrot (Imitate)", CAT_GENERAL, 100, 0, 100, "The frequency that a parrot makes a sound imitating another mob as a percentage of a typical Minecraft parrot. "
				+ "Set this to 50 for 50 percent, etc.");
		
		villageSpawnCapCat = config.getInt("Village spawn cap: Cat", CAT_GENERAL, 10, 0, 100, "Maximum number of stray cats that can spawn in a village depends on the village's size, but will never be bigger than this value."); // Config value and name changed in v1.3.0
		villageSpawnCapDog = config.getInt("Village spawn cap: Dog", CAT_GENERAL, 10, 0, 100, "Maximum number of stray dogs that can spawn in a village depends on the village's size, but will never be bigger than this value.");
		strayCapFromVillageRadius = config.getBoolean("Use Village Radius To Limit Strays", CAT_GENERAL, false, "Use a cruder determination of the size of a village for the purpose of limiting the number of strays that can spawn. Set to true if you're using a mod that replaces vanilla villagers, or otherwise notice that strays never spawn.");
		villageSpawnRatePercent = config.getInt("Village spawn rate percent", CAT_GENERAL, 50, 0, 100, "Villages will check their stray animal populations against their limits every 1.5 seconds and, if there's room, will attempt to spawn a new stray this percent of the time.");
		strayFractionCat = config.getFloat("Village spawn cat fraction", CAT_GENERAL, 0.75F, 0.0F, 1.0F, "This fraction of village strays are cats. The rest are dogs.");
		
		followOwnerCat = config.getBoolean("Follow owner: Cat", CAT_GENERAL, true, "If false, tamed cats will never follow or teleport to their owners.");
		followOwnerDog = config.getBoolean("Follow owner: Dog", CAT_GENERAL, true, "If false, tamed dogs will never follow or teleport to their owners.");
		followOwnerParrot = config.getBoolean("Follow owner: Parrot", CAT_GENERAL, true, "If false, tamed parrots will never follow or teleport to their owners.");
		
		// Changed minimum values to 0.0F in order to allow teleport disabling -- v2.1.0
		followTeleportCat = config.getFloat("Follow teleport: Cat", CAT_GENERAL, 12.0F, 0.0F, 256.0F, "If not sitting or leashed, your tamed ocelot/cat will teleport to you when you're farther than this distance away. Set this to 0 to disable teleporting entirely.");
		followTeleportDog = config.getFloat("Follow teleport: Dog", CAT_GENERAL, 12.0F, 0.0F, 256.0F, "Same as the above setting, but for tamed wolves/dogs.");
		followTeleportParrot = config.getFloat("Follow teleport: Parrot", CAT_GENERAL, 12.0F, 0.0F, 256.0F, "Same as the above setting, but for tamed parrots.");

		healingFoodCat = config.getBoolean("Healing Food: Cat", CAT_GENERAL, true, "Using the taming food on a tamed cat will heal it.");
		healingFoodParrot = config.getBoolean("Healing Food: Parrot", CAT_GENERAL, true, "Using the taming food on a tamed parrot will heal it.");
		
		swampHutCat = config.getBoolean("Swamp Hut Cats", CAT_GENERAL, true, "Swamp huts often contain a black cat.");
		swampHutCatName = config.getBoolean("Swamp Hut Cat Name", CAT_GENERAL, true, "Black cats found in swamp huts spawn with a name.");
		
		cookiesKillParrots = config.getBoolean("Cool Story, Theobromine", CAT_GENERAL, true, "If false, parrots are only poisoned when fed a cookie, but not killed.");
		parrotDismountFallHeight = config.getFloat("Parrot Dismount Fall Height", CAT_GENERAL, 1.1F, 0.0F, 512.0F, "Shoulder-perching parrots dismount when you fall at least this far. Vanilla is 0.5.");

		strayDimensions = config.get(CAT_GENERAL, "Stray Dimensions", new int[]{0}, "Strays can appear in villages within these dimension IDs (0 is Overworld, -1 is Nether, 1 is The End, etc).").getIntList();
		
		
		// ---------------------------- //
		// ------ Miscellaneous ------- //
		// ---------------------------- //
		
		versionChecker = config.getBoolean("Version Checker", CAT_MISC, true, "Displays a client-side chat message on login if there's an update available.");
		debugMessages = config.getBoolean("Debug Messages", CAT_MISC, false, "Print extra debug messages to the console.");
		
		
		
		// ------------------------ //
		// --- PARROT IMITATION --- //
		// ------------------------ //
		
		enableParrotMimicry = config.getBoolean("Enable Parrot Mimicry", CAT_PARROT_IMITATION, true, "If false, NEP parrots will not make mimic sounds. Do this if you experience issues.");
		
		
		parrotMimicSounds = config.getStringList("Parrot Mimic Sounds", CAT_PARROT_IMITATION, new String[] {
				"net.minecraft.entity.monster.EntityBlaze|minecraft:mob.blaze.breathe|1.8",
				"net.minecraft.entity.monster.EntityCaveSpider|minecraft:mob.spider.say|1.8",
				"net.minecraft.entity.monster.EntityCreeper|minecraft:creeper.primed|1.8",
				//"net.minecraft.entity.monster.EntityElderGuardian|minecraft:mob.guardian.land.idle|1.8", // Introduced in 1.11
				"net.minecraft.entity.boss.EntityDragon|minecraft:mob.enderdragon.growl|1.8",
				"net.minecraft.entity.monster.EntityEnderman|minecraft:mob.endermen.idle|1.7",
				//"net.minecraft.entity.monster.EntityEndermite|minecraft:mob.silverfish.say|1.8", // Introduced in 1.8
				//"net.minecraft.entity.monster.EntityEvoker|minecraft:entity.evocation_illager.ambient|1.8", // Introduced in 1.11
				"net.minecraft.entity.monster.EntityGhast|minecraft:mob.ghast.moan|1.8",
				//"net.minecraft.entity.monster.EntityHusk|minecraft:entity.husk.ambient|1.8", // Introduced in 1.11
				//"net.minecraft.entity.monster.EntityIllusionIllager|minecraft:entity.illusion_illager.ambient|1.8", // Introduced in 1.12
				"net.minecraft.entity.monster.EntityMagmaCube|minecraft:mob.magmacube.big|1.8",
				"net.minecraft.entity.monster.EntityPigZombie|minecraft:mob.zombiepig.zpig|1.8",
				//"net.minecraft.entity.monster.EntityPolarBear|minecraft:entity.polar_bear.ambient|0.7", // Introduced in 1.10
				//"net.minecraft.entity.monster.EntityShulker|minecraft:entity.shulker.ambient|1.7", // Introduced in 1.9
				"net.minecraft.entity.monster.EntitySilverfish|minecraft:mob.silverfish.say|1.8",
				"net.minecraft.entity.monster.EntitySkeleton|minecraft:mob.skeleton.say|1.7",
				"net.minecraft.entity.monster.EntitySlime|minecraft:mob.slime.big|1.8",
				"net.minecraft.entity.monster.EntitySpider|minecraft:mob.spider.say|1.8",
				//"net.minecraft.entity.monster.EntityStray|minecraft:entity.stray.ambient|1.6", // Introduced in 1.11
				//"net.minecraft.entity.monster.EntityVex|minecraft:entity.vex.ambient|1.6", // Introduced in 1.11
				//"net.minecraft.entity.monster.EntityVindicator|minecraft:entity.vindication_illager.ambient|1.7", // Introduced in 1.11
				//"net.minecraft.entity.monster.EntityWitch|minecraft:entity.witch.ambient|1.8", // Sounds not implemented until 1.9
				"net.minecraft.entity.boss.EntityWither|minecraft:mob.wither.idle|1.8",
				//"net.minecraft.entity.monster.EntityWitherSkeleton|minecraft:entity.wither_skeleton.ambient|1.8", // Introduced in 1.11
				//"net.minecraft.entity.passive.EntityWolf|minecraft:mob.wolf.barkt|1.8",
				"net.minecraft.entity.monster.EntityZombie|minecraft:mob.zombie.say|1.8",
				//"net.minecraft.entity.monster.EntityZombieVillager|minecraft:entity.zombie_villager.ambient|1.8", // Introduced in 1.11
	    		},
	    		"List of entities for parrots to mimic. Format is: classPath|soundPath|pitch\n"+
	    				"classPath: The address to the entity class.\n"
	    				+ "soundPath: the sound address, typically in the sounds.json file.\n"
	    				+ "pitch: the average pitch/speed factor, between 0.4 and 2.0, at which this mimicked sound will be played, where 1.0 is unshifted.");
		
		
		
		if (config.hasChanged()) config.save();
	}
	

	@SubscribeEvent
	public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
		if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
			this.loadConfiguration();
		}
	}
	
}
