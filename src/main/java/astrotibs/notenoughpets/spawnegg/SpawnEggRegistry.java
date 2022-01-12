package astrotibs.notenoughpets.spawnegg;

import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Random;

import astrotibs.notenoughpets.name.BlackCatNames;
import astrotibs.notenoughpets.util.LogHelper;
import astrotibs.notenoughpets.util.Reference;
import net.minecraft.nbt.NBTTagCompound;

/**
 * Adapted from Better Spawn Eggs:
 * https://github.com/DavidGoldman/BetterSpawnEggs/blob/master/com/mcf/davidee/spawneggs/SpawnEggRegistry.java
 * https://github.com/DavidGoldman/BetterSpawnEggs/blob/master/com/mcf/davidee/spawneggs/DefaultFunctionality.java
 * https://github.com/DavidGoldman/BetterSpawnEggs/blob/master/com/mcf/davidee/spawneggs/CustomTags.java
 * @author AstroTibs
 */

public class SpawnEggRegistry {
	
	private static final Map<Short, SpawnEggInfo> eggs = new LinkedHashMap<Short, SpawnEggInfo>();
	private static final int MOD_MOBS_IDS_START = 1;
	
	public static final Object[][] spawnEggData = new Object[][]{
		// Cats are index 0 of mobStrings
		{0, "cat.ocelot",           catTypeNEC(0), 0xffdd74, 0x8e552a},
		{0, "cat.tuxedo",           catTypeNEC(1), 0x1c1827, 0xeaeaea},
		{0, "cat.redtabby",         catTypeNEC(2), 0xeaa939, 0xdb7920},
		{0, "cat.siamese",          catTypeNEC(3), 0xe1d6c3, 0x554c3b},
		{0, "cat.britishshorthair", catTypeNEC(4), 0xbcbcbc, 0x868a8a},
		{0, "cat.calico",           catTypeNEC(5), 0xdb9c3e, 0x423b32},
		{0, "cat.persian",          catTypeNEC(6), 0xffeacb, 0xf3ca8d},
		{0, "cat.ragdoll",          catTypeNEC(7), 0xf7f7f7, 0xab9e94},
		{0, "cat.white",            catTypeNEC(8), 0xfdf9fb, 0xf3f5f6},
		{0, "cat.jellie",           catTypeNEC(9), 0xf0f0f0, 0x4b4a4a},
		{0, "cat.black",            catTypeNEC(10), 0x161524, 0x12101d},
		{0, "cat.black_named",      catTypeNEC(10), 0x161524, 0xc07832},
		{0, "cat.browntabby",       catTypeNEC(11), 0x87654a, 0x5e422d},
		{0, "cat.graytabby",        catTypeNEC(12), 0x817a74, 0x423c38},
		{0, "cat.juno",             catTypeNEC(13), 0x382c24, 0xfcfbfb},
		{0, "cat.willie",           catTypeNEC(14), 0x5f5a55, 0xb7a897},
		{0, "cat.lily",             catTypeNEC(15), 0x1c100a, 0x6b442b},
		{0, "cat.boo",              catTypeNEC(16), 0x654827, 0xbc8851},
		{0, "cat.winslow",          catTypeNEC(17), 0xe28726, 0x363330},
		{0, "cat.blitzkrieg",       catTypeNEC(18), 0x505050, 0x3d3d3d},
		{0, "cat.princess",         catTypeNEC(19), 0xfcfbfb, 0x4f4d4f},
		{0, "cat.cocoa",            catTypeNEC(20), 0x9a715b, 0xb2846c},
		// Wolves and dogs are index 1
		{1, "dog.wolf",             dogTypeNEC(0), 0xdddadb, 0xd4b5a4},
		{1, "dog.blocco",           dogTypeNEC(1), 0x413b47, 0xf3f1f2},
		{1, "dog.brown",            dogTypeNEC(2), 0x8c4e2e, 0x49200d}, // Colors updated -- v2.1.0
		{1, "dog.cream",            dogTypeNEC(3), 0xe9e1c5, 0xd29e7c}, // Colors updated -- v2.1.0
		{1, "dog.white_terrier",    dogTypeNEC(4), 0xf5f1f2, 0xd3cfd1},
		{1, "dog.red_shiba_inu",    dogTypeNEC(5), 0xc68c65, 0xebe6e6},
		{1, "dog.beagle",           dogTypeNEC(6), 0xad6a2d, 0x3b383a},
		{1, "dog.pug",              dogTypeNEC(7), 0xdac09b, 0x3e3a3a},
		{1, "dog.border_collie",    dogTypeNEC(8), 0x2e2a26, 0x866a47},
		{1, "dog.boston_terrier",   dogTypeNEC(9), 0x292724, 0xffffff},
		{1, "dog.st_bernard",       dogTypeNEC(10), 0x9b7354, 0xf3f2ee}, // Added v2.1.0
		{1, "dog.golden_retriever", dogTypeNEC(11), 0xdcb04d, 0xc99727},
		{1, "dog.siberian_husky",   dogTypeNEC(12), 0xf3f0f1, 0x3f3945},
		// Parrots are index 2 - v2.0.0
		{2, "parrot.red",           parrotType(0), 0xfb0d0d, 0x037cf7},
		{2, "parrot.blue",          parrotType(1), 0x0c20dc, 0xfde803},
		{2, "parrot.green",         parrotType(2), 0x8ccf00, 0xf1eebf},
		{2, "parrot.cyan",          parrotType(3), 0x1ab9f7, 0xffd400},
		{2, "parrot.gray",          parrotType(4), 0xb0b0b0, 0xffe533},
		{2, "parrot.golden",        parrotType(5), 0xfee400, 0x53881a},
		{2, "parrot.white",         parrotType(6), 0xf8f7f3, 0xe7e2d6},
		{2, "parrot.black",         parrotType(7), 0x332b47, 0x201c32},
		{2, "parrot.rosy_faced",    parrotType(8), 0x67c72c, 0xff714d},
		{2, "parrot.dusky",         parrotType(9), 0x514d4c, 0xea3e21},
		{2, "parrot.african_grey",  parrotType(10), 0x656565, 0x7e7e7e},
		{2, "parrot.galah",         parrotType(11), 0xe15769, 0x938e90},
		{2, "parrot.rainbow",       parrotType(12), 0x3012ef, 0xfc7005},
	};
	
	
	public static void registerSpawnEgg(SpawnEggInfo info) throws IllegalArgumentException
	{
		if (info == null)
		{
			throw new IllegalArgumentException("SpawnEggInfo cannot be null");
		}
		
		if (!isValidSpawnEggID(info.eggID))
		{
			throw new IllegalArgumentException("Duplicate spawn egg with id " + info.eggID);
		}
		
		eggs.put(info.eggID, info);
	}
	
	public static boolean isValidSpawnEggID(short id)
	{
		return !eggs.containsKey(id);
	}
	
	public static SpawnEggInfo getEggInfo(short id)
	{
		return eggs.get(id);
	}
	
	public static Collection<SpawnEggInfo> getEggInfoList()
	{
		return Collections.unmodifiableCollection(eggs.values());
	}
	
	public static void addAllSpawnEggs()// throws RuntimeException
	{
		// Use this to index the mob strings
		String[] mobStrings = new String[]{
				
				Reference.MOD_ID + "." + Reference.MOB_OCELOT_NEP, // Index 0
				Reference.MOD_ID + "." + Reference.MOB_WOLF_NEP, // Index 1
				Reference.MOD_ID + "." + Reference.MOB_PARROT_NEP, // Index 3 - v2.0.0
				
				};
		
		int currentID;
		
		for (int i=0; i < spawnEggData.length; i++)
		{
			currentID = i + MOD_MOBS_IDS_START;
			
			try {
				SpawnEggRegistry.registerSpawnEgg(new SpawnEggInfo(
						(short) currentID, // Meta ID that's going to try to load
						(mobStrings[(Integer)spawnEggData[i][0]]), // Mob's internal name (e.g. Ocelot_NEC)
						(String)(spawnEggData[i][1]), // The name to be displayed on the spawn egg item
						(NBTTagCompound)(spawnEggData[i][2]), // Extra data to be applied to the mob
						(Integer)(spawnEggData[i][3]), (Integer)(spawnEggData[i][4]))); // The base and speckle colors of the spawn egg
			}
			catch(IllegalArgumentException e)
			{
				LogHelper.warn("Could not register spawn egg for entity " + (String)(spawnEggData[i][1]) + " into ID " + currentID);
			}
		}
	}
	
	
	/*
	 * These methods attack custom NBT data to allow subtypes with the spawn egg
	 * I mean, otherwise, there's no point in having the eggs.
	 */
	
	public static NBTTagCompound addCatName(NBTTagCompound tag) {
		tag.setString("CustomName", BlackCatNames.newRandomName(new Random()));
		return tag;
	}
	
	public static NBTTagCompound catTypeNEC(int type) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("CatType", type);
		return tag;
	}
	
	public static NBTTagCompound dogTypeNEC(int type) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("DogType", type);
		return tag;
	}
	
	public static NBTTagCompound mooshroomTypeNEC(int type) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("MooshroomType", type);
		return tag;
	}

	//v2.0.0
	public static NBTTagCompound parrotType(int type) {
		NBTTagCompound tag = new NBTTagCompound();
		tag.setInteger("Variant", type);
		return tag;
	}
	
}