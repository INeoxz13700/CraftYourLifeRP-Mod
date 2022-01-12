package fr.craftyourliferp.items;

import com.flansmod.common.VoitureKey;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import fr.craftyourliferp.blocks.BlockAtm;
import fr.craftyourliferp.blocks.BlockCashRegister;
import fr.craftyourliferp.blocks.BlockComputer;
import fr.craftyourliferp.blocks.BlockCorpseFreezer;
import fr.craftyourliferp.blocks.BlockGoldIngot;
import fr.craftyourliferp.blocks.BlockInvisible;
import fr.craftyourliferp.blocks.BlockNuclear;
import fr.craftyourliferp.blocks.BlockNuclearOre;
import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.blocks.BlockRadar;
import fr.craftyourliferp.blocks.BlockRoad;
import fr.craftyourliferp.blocks.BlockRoadBalise;
import fr.craftyourliferp.blocks.BlockTrashCan;
import fr.craftyourliferp.blocks.tileentity.TileEntityAtm;
import fr.craftyourliferp.blocks.tileentity.TileEntityCashRegister;
import fr.craftyourliferp.blocks.tileentity.TileEntityComputer;
import fr.craftyourliferp.blocks.tileentity.TileEntityCorpseFreezer;
import fr.craftyourliferp.blocks.tileentity.TileEntityGoldIngot;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import fr.craftyourliferp.blocks.tileentity.TileEntityRadar;
import fr.craftyourliferp.blocks.tileentity.TileEntityRoadBalise;
import fr.craftyourliferp.blocks.tileentity.TileEntityTrashCan;
import fr.craftyourliferp.cosmetics.CosmeticsItem;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.item.Item;
import net.minecraft.item.Item.ToolMaterial;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemPickaxe;
import net.minecraftforge.common.util.EnumHelper;

public class ModdedItems
{

	//Item RP
	public static Item KSamsung;
	public static Item Journal;
	public static Item CarteBancaire;
	public static Item identityCard;
	public static Item extincteur;
    public static Item BulletGilet;
    public static Item watterBottle1L;
    public static Item watterBottle2L;
    public static Item itemCrowbar; 
    public static Item voiturekey;
    public static Item itemBillet5; 
    public static Item itemBillet10; 
    public static Item itemBillet20; 
    public static Item itemBillet50; 
    public static Item itemBillet100; 
    public static Item itemBillet200;
    public static Item itemBillet500; 
    public static Item itemCoin1Euro; 
    public static Item itemCoin2Euro;
    public static Item itemBulletproofShield;
    public static Item itemStopStickSize2;
    public static Item itemStopStickSize4;
    public static Item itemStopStickSize6;
    public static Item nuclearPickaxe;
    public static Item nuclearIngot;
    

    
    public static ItemBlock paintingItem;

	//Blocks
	public static Block InvisibleBlock;
	public static Block InvisiBleBlock_luminy;
	public static Block AtmBlock;
	public static Block CashRegisterBlock;
	public static Block RoadBaliseBlock;
	public static Block TrashCanBlock;
	public static Block CorpseFreezerBlock;
	public static Block RoadBlock1;
	public static Block PaintingBlock;
	public static Block RadarBlock;
	public static Block GoldIngotBlock;
	public static Block ComputerBlock;
	public static Block nuclearBlock;
	public static Block nuclearOre;

	//Cosmetics
	public static CosmeticsItem birdCosmetic;
	public static CosmeticsItem backpackCosmetic;
	public static CosmeticsItem voxelCosmetic;
	public static CosmeticsItem minccinoCosmetic;
	public static CosmeticsItem smithyHatCosmetic;
	public static CosmeticsItem cylrpGlassesCosmetic;
	public static CosmeticsItem witchHatCosmetic;
	public static CosmeticsItem snowmanCosmetic;
	public static CosmeticsItem noelBonnetCosmetic;
	public static CosmeticsItem rabbitEarsCosmetic;
	public static CosmeticsItem heliceHat;
	public static CosmeticsItem threeDGlasses;
	public static CosmeticsItem aureole;
	public static CosmeticsItem jesterHat;
	public static CosmeticsItem marioHat;
	public static CosmeticsItem swordOnHead;
	public static CosmeticsItem alienMask;
	public static CosmeticsItem clownHead;
	public static CosmeticsItem witchHat2;
	public static CosmeticsItem devilHorn;
	
	public static ToolMaterial nuclearMaterial = EnumHelper.addToolMaterial("Nuclear", 4, 1561, 12.0F, 3.0F, 0);

	
	public static void registerItems()
	{
    	ModdedItems.BulletGilet = new ItemVestBullet().setMaxDamage(100).setUnlocalizedName("GiletBullet").setTextureName("craftyourliferp" + ":bulletgilet");
    	ModdedItems.Journal= new Journal().setTextureName("craftyourliferp" + ":Journal").setUnlocalizedName("Journal");
    	ModdedItems.CarteBancaire= new CarteBancaire().setTextureName("craftyourliferp" + ":CarteBancaire").setUnlocalizedName("CarteBancaire");
    	ModdedItems.identityCard = new CardIdentity().setTextureName("craftyourliferp" + ":item_cardidentity");
    	ModdedItems.KSamsung = new KSamsung().setTextureName("craftyourliferp" + ":ksamsung").setUnlocalizedName("Kamsung");
    	ModdedItems.extincteur = new Extincteur().setUnlocalizedName("Extincteur");
		
    	ModdedItems.watterBottle2L = new ItemWaterBottle(2,100).setUnlocalizedName("Bouteille d'eau").setTextureName("craftyourliferp:waterbottle");
    	ModdedItems.watterBottle1L = new ItemWaterBottle(1,100).setUnlocalizedName("Bouteille d'eau").setTextureName("craftyourliferp:waterbottle");

    	ModdedItems.itemStopStickSize2 = new ItemStopStick(2);
    	ModdedItems.itemStopStickSize4 = new ItemStopStick(4);
    	ModdedItems.itemStopStickSize6 = new ItemStopStick(6);

    	ModdedItems.nuclearIngot = new Item().setMaxDamage(-1).setUnlocalizedName("nuclear_ingot").setTextureName("craftyourliferp:nuclear_ingot").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);;
    	ModdedItems.nuclearPickaxe = new ItemNuclearPickaxe().setUnlocalizedName("nuclear_pickaxe").setTextureName("craftyourliferp:nuclear_pickaxe");
    	
		GameRegistry.registerItem(ModdedItems.Journal, "item_journal");
		GameRegistry.registerItem(ModdedItems.CarteBancaire, "item_cartebancaire");
		GameRegistry.registerItem(ModdedItems.BulletGilet, "bullet_gilet");
		GameRegistry.registerItem(ModdedItems.identityCard, "item_cardidentity");
		GameRegistry.registerItem(ModdedItems.KSamsung, "item_samsung");
		GameRegistry.registerItem(ModdedItems.extincteur, "item_extincteur");
		GameRegistry.registerItem(ModdedItems.watterBottle2L, "item_water_bottle_2L");
		GameRegistry.registerItem(ModdedItems.watterBottle1L, "item_water_bottle_1L");
		GameRegistry.registerItem(ModdedItems.itemStopStickSize2, "item_stop_stick_2");
		GameRegistry.registerItem(ModdedItems.itemStopStickSize4, "item_stop_stick_4");
		GameRegistry.registerItem(ModdedItems.itemStopStickSize6, "item_stop_stick_6");
		GameRegistry.registerItem(ModdedItems.nuclearIngot, "nuclear_ingot");
		GameRegistry.registerItem(ModdedItems.nuclearPickaxe, "nuclear_pickaxe");

		
		itemCoin1Euro = new Item().setUnlocalizedName("coin_1euro").setTextureName("craftyourliferp:coin_1euro").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemCoin1Euro, "coin_1euro");
        itemCoin2Euro = new Item().setUnlocalizedName("coin_2euro").setTextureName("craftyourliferp:coin_2euro").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemCoin2Euro, "coin_2euro");
        itemBillet5 = new Item().setUnlocalizedName("billet5").setTextureName("craftyourliferp:item_billet5").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemBillet5, "item_billet5");
        itemBillet10 = new Item().setUnlocalizedName("billet10").setTextureName("craftyourliferp:item_billet10").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemBillet10, "item_billet10");
        itemBillet20 = new Item().setUnlocalizedName("billet20").setTextureName("craftyourliferp:item_billet20").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemBillet20, "item_billet20");
        itemBillet50 = new Item().setUnlocalizedName("billet50").setTextureName("craftyourliferp:item_billet50").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemBillet50, "item_billet50");
        itemBillet100 = new Item().setUnlocalizedName("billet100").setTextureName("craftyourliferp:item_billet100").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemBillet100, "item_billet100");
        itemBillet200 = new Item().setUnlocalizedName("billet200").setTextureName("craftyourliferp:item_billet200").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemBillet200, "item_billet200");
        itemBillet500 = new Item().setUnlocalizedName("billet500").setTextureName("craftyourliferp:item_billet500").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemBillet500, "item_billet500");
        
        itemCrowbar = new itemCrowbar().setUnlocalizedName("itemCrowbar").setTextureName("craftyourliferp:item_crowbar").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
        GameRegistry.registerItem(itemCrowbar, "item_crowbar");
        
        itemBulletproofShield = new ItemBulletproofShield();
        GameRegistry.registerItem(itemBulletproofShield, "BulletproofShield");
        
		voiturekey = new VoitureKey().setUnlocalizedName("key").setTextureName("craftyourliferp:key").setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
		GameRegistry.registerItem(voiturekey, "voiturekey");
		
	}
	
	public static void registerBlocks() 
    {
    	
    	ModdedItems.InvisibleBlock = new BlockInvisible("Invisible_block",false);
    	ModdedItems.InvisiBleBlock_luminy = new BlockInvisible("Invisible_block_luminy",true);
    	
    	ModdedItems.AtmBlock = new BlockAtm(Material.rock);
    	GameRegistry.registerTileEntity(TileEntityAtm.class, "TileEntityATM");
    	 
    	ModdedItems.TrashCanBlock = new BlockTrashCan(Material.rock);
    	GameRegistry.registerTileEntity(TileEntityTrashCan.class, "TileEntityTrashCan");     
    	
    	ModdedItems.CorpseFreezerBlock = new BlockCorpseFreezer(Material.iron);
	    GameRegistry.registerBlock(ModdedItems.CorpseFreezerBlock, "CorpseFreezerBlock");
	    ModdedItems.CorpseFreezerBlock.setBlockName("CorpseFreezerBlock");
    	GameRegistry.registerTileEntity(TileEntityCorpseFreezer.class, "TileEntityCorpseFreezer");     
    	
    	ModdedItems.RoadBlock1 = new BlockRoad().setBlockName("road").setBlockTextureName(CraftYourLifeRPMod.name + ":road");
	    GameRegistry.registerBlock(ModdedItems.RoadBlock1, "road");


	    ModdedItems.PaintingBlock = new BlockPainting(Material.rock);
		GameRegistry.registerBlock(ModdedItems.PaintingBlock,PaintingItem.class, "Painting");
        GameRegistry.registerTileEntity(TileEntityPainting.class, "Painting");
        
	    ModdedItems.RoadBaliseBlock = new BlockRoadBalise(Material.rock);
        GameRegistry.registerTileEntity(TileEntityRoadBalise.class, "RoadBalise");
        
	    ModdedItems.CashRegisterBlock = new BlockCashRegister(Material.rock);
        GameRegistry.registerTileEntity(TileEntityCashRegister.class, "CashRegister");
        
        ModdedItems.RadarBlock = new BlockRadar(Material.iron);
        GameRegistry.registerBlock(ModdedItems.RadarBlock, "Radar");
        GameRegistry.registerTileEntity(TileEntityRadar.class, "flansmod:radar");   
        
    	ModdedItems.GoldIngotBlock = new BlockGoldIngot(Material.rock);
		GameRegistry.registerBlock(ModdedItems.GoldIngotBlock,ItemGoldIngot.class, "gold_ingot");
        GameRegistry.registerTileEntity(TileEntityGoldIngot.class, "GoldIngot");  
        
        
    	ModdedItems.ComputerBlock = new BlockComputer(Material.rock);
		GameRegistry.registerBlock(ModdedItems.ComputerBlock, "Computer");
        GameRegistry.registerTileEntity(TileEntityComputer.class, "Computer");
        
        ModdedItems.nuclearBlock = new BlockNuclear(Material.rock).setBlockName("nuclear_block").setBlockTextureName("craftyourliferp:nuclear_block"); 
		GameRegistry.registerBlock(ModdedItems.nuclearBlock, "nuclear_block");

        ModdedItems.nuclearOre = new BlockNuclearOre().setBlockName("nuclear_ore").setBlockTextureName("craftyourliferp:nuclear_ore"); 
		GameRegistry.registerBlock(ModdedItems.nuclearOre, "nuclear_ore");

    }
	

	
	
	
}
