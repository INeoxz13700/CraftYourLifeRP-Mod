package astrotibs.notenoughpets.util;

import net.minecraft.util.ResourceLocation;

/**
 * Created because a server won't instantiate the Ocelot/Wolf renderer classes,
 * and so no methods can count their total number of skin variations.
 * v1.3.0
 */
public class SkinVariations {
	
	// ------------ //
	// --- CATS --- //
	// ------------ //
	
	public static final ResourceLocation[] catSkinArray = new ResourceLocation[]{
			
			// Vanilla skins
			new ResourceLocation("textures/entity/cat/ocelot.png"), // Type 0
			new ResourceLocation("textures/entity/cat/black.png"), // Type 1
			new ResourceLocation("textures/entity/cat/red.png"), // Type 2
			new ResourceLocation("textures/entity/cat/siamese.png"), // Type 3
			
			// Official skins from other versions/ports
			new ResourceLocation("textures/entity/cat/british_shorthair.png"), // Type 4
			new ResourceLocation("textures/entity/cat/calico.png"), // Type 5
			new ResourceLocation("textures/entity/cat/persian.png"), // Type 6
			new ResourceLocation("textures/entity/cat/ragdoll.png"), // Type 7
			new ResourceLocation("textures/entity/cat/white.png"), // Type 8
			new ResourceLocation("textures/entity/cat/jellie.png"), // Type 9
			new ResourceLocation("textures/entity/cat/all_black.png"), // Type 10
			new ResourceLocation("textures/entity/cat/tabby.png"), // Type 11
			new ResourceLocation("textures/entity/cat/gray_tabby.png"), // Type 12
			
			// Custom skins by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/juno.png"), // Type 13
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/willie.png"), // Type 14
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/lily.png"), // Type 15
			
			// Additional skins
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/boo.png"), // Type 16 by Asher Applegate
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/winslow.png"), // Type 17 - Winslow from Minecraft: Story Mode Season Two by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/blitzkrieg.png"), // Type 18 by Shahayhay
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/princess.png"), // Type 19 by Chara Violet
			new ResourceLocation(Reference.MOD_ID, "textures/entity/cat/cocoa.png"), // Type 20 by _MashedTaters_
	};
	
	
	// ------------ //
	// --- DOGS --- //
	// ------------ //
	
	public static final ResourceLocation[][] dogSkinArray = new ResourceLocation[][] {
		
		// Vanilla skins
		{ // Type 0
			new ResourceLocation("textures/entity/wolf/wolf.png"),
			new ResourceLocation("textures/entity/wolf/wolf_angry.png"),
			new ResourceLocation("textures/entity/wolf/wolf_tame.png")
			},

		// Official skins from other versions/ports
		{ // Type 1
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/blocco.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/blocco_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/blocco_tame.png")
			},
		
		// User-submitted skins
		{ // Type 2 by inkdear
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/brown_wolf.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/brown_wolf_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/brown_wolf_tame.png")
			},
		{ // Type 3 by inkdear
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/cream_wolf.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/cream_wolf_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/cream_wolf_tame.png")
			},
		// v2.0.0
		{ // Type 4 by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/white_terrier.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/white_terrier_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/white_terrier_tame.png")
			},
		{ // Type 5 by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/red_shiba_inu.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/red_shiba_inu_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/red_shiba_inu_tame.png")
			},
		{ // Type 6 by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/beagle.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/beagle_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/beagle_tame.png")
			},
		{ // Type 7 by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/pug.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/pug_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/pug.png")
			},
		{ // Type 8 by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/border_collie.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/border_collie_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/border_collie_tame.png")
			},
		{ // Type 9 by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/boston_terrier.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/boston_terrier_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/boston_terrier_tame.png")
			},
		{ // Type 10 by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/st_bernard.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/st_bernard_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/st_bernard.png")
			},
		{ // Type 11 by Tild09
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/golden_retriever.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/golden_retriever_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/golden_retriever_tame.png")
			},
		{ // Type 12 by Tild09
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/siberian_husky.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/siberian_husky_angry.png"),
			new ResourceLocation(Reference.MOD_ID, "textures/entity/dog/siberian_husky.png")
			},
	};
	
	
	// ------------------ //
	// --- MOOSHROOMS --- //
	// ------------------ //
	
	public static final ResourceLocation[] mooshroomSkinArray = new ResourceLocation[] {
			
			// Vanilla
			new ResourceLocation("textures/entity/cow/red_mooshroom.png"), // Type 0
			
			// Official skins from other versions/ports
			new ResourceLocation("textures/entity/cow/brown_mooshroom.png"), // Type 1
	};

	
	// --------------- //
	// --- PARROTS --- //
	// --------------- //
	// v2.0.0
	public static final ResourceLocation[] parrotSkinArray = new ResourceLocation[] {
			
			// Vanilla
			new ResourceLocation("textures/entity/parrot/parrot_red_blue.png"), // Type 0
			new ResourceLocation("textures/entity/parrot/parrot_blue.png"), // Type 1
			new ResourceLocation("textures/entity/parrot/parrot_green.png"), // Type 2
			new ResourceLocation("textures/entity/parrot/parrot_yellow_blue.png"), // Type 3
			new ResourceLocation("textures/entity/parrot/parrot_grey.png"), // Type 4
			
			// Custom skins by AstroTibs
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_golden.png"), // Type 5
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_white.png"), // Type 6
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_black.png"), // Type 7
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_rosy_faced.png"), // Type 8
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_dusky.png"), // Type 9
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_african_grey.png"), // Type 10
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_galah.png"), // Type 11
			new ResourceLocation(Reference.MOD_ID, "textures/entity/parrot/parrot_rainbow.png"), // Type 12
	};
	
}