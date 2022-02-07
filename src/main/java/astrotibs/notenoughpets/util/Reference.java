package astrotibs.notenoughpets.util;

import net.minecraft.util.EnumChatFormatting;

public class Reference
{
	// Contains common constants for the mod
	public static final String MOD_ID = "notenoughpets";
	public static final String MOD_NAME = "Not Enough Pets";
	public static final String MOD_NAME_COLORIZED = EnumChatFormatting.YELLOW + MOD_NAME;
	public static final String VERSION = "2.3.1";
	public static final String MOD_CHANNEL = "nep_channel"; 
	
	//public static final String MOD_CHANNEL = "necChannel";
	public static final String CLIENT_PROXY = "astrotibs.notenoughpets.proxy.ClientProxy";
	public static final String SERVER_PROXY = "astrotibs.notenoughpets.proxy.ServerProxy";
	public static final String COMMON_PROXY = "astrotibs.notenoughpets.proxy.CommonProxy";
	public static final String GUI_FACTORY = "astrotibs.notenoughpets.config.NEPGuiFactory";
	
	// Sound stuff
	public static final String SE_CAT_STRAY_IDLE = MOD_ID+":mob.cat.stray.idle";
	public static final String SE_CAT_BEG = MOD_ID+":mob.cat.beg";
	public static final String SE_CAT_EAT = MOD_ID+":mob.cat.eat";
	public static final String SE_CAT_OCELOT_IDLE = MOD_ID+":mob.cat.ocelot.idle";
	public static final String SE_CAT_OCELOT_DEATH = MOD_ID+":mob.cat.ocelot.death";
	
	public static final String ENTITY_PARROT_AMBIENT = MOD_ID+":entity.parrot.ambient";
	public static final String ENTITY_PARROT_DEATH = MOD_ID+":entity.parrot.death";
	public static final String ENTITY_PARROT_EAT = MOD_ID+":entity.parrot.eat";
	public static final String ENTITY_PARROT_FLY = MOD_ID+":entity.parrot.fly";
	public static final String ENTITY_PARROT_HURT = MOD_ID+":entity.parrot.hurt";
	public static final String ENTITY_PARROT_STEP = MOD_ID+":entity.parrot.step";
	
	// --------------- //
	// -- Constants -- //
	// --------------- //
	
	public static final int VILLAGE_RADIUS_BUFFER=32; // The radius outside a village to check spawning
	public static final double SPAWN_BLOCK_OFFSET=0.5D; // If you obtained the spawn x,y,z as ints, add this offset to x and z to ensure it's in the center of the block.
	// The stray limit in a village is raised by 1 for this many blocks in a village area. Only used if "Use Village Radius To Limit Strays" is true
	public static final int VILLAGE_BLOCKS_PER_STRAY_LIMIT=500;
	
	public static final String MOB_OCELOT_NEP = "Ocelot_NEP";
	public static final String MOB_WOLF_NEP = "Wolf_NEP";
	public static final String MOB_PARROT_NEP = "Parrot_NEP"; 

	

}
