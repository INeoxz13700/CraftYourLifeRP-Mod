package eu.nicoszpako.armamania.common;

import cpw.mods.fml.common.Mod;
import cpw.mods.fml.common.Mod.EventHandler;
import cpw.mods.fml.common.SidedProxy;
import cpw.mods.fml.common.event.FMLInitializationEvent;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.registry.GameRegistry;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemArmor;
import net.minecraftforge.common.util.EnumHelper;

@Mod(modid = "armamania", name = "Arma Mania Mod", version = "1.0.0")
public class ArmaMania {

  public static Item b;
  
  public static Item c;
  
  public static Item d;
  
  public static Item e;
  
  public static Item f;
  
  public static Item g;
  
  public static Item h;
  
  public static Item i;
 
  
  public static Item z;
  
  public static Item A;
  
  public static Item C;
  
  public static Item D;
  
  public static Item E;
  
  public static Item F;
 
  
  public static Item M;
  
  public static Item N;
  
  public static Item O;
  
  public static Item P;
  
  public static Item Q;
  
  public static Item R;
    
  public static Item T;
  
  public static Item U;
  
  public static Item V;
  
  public static Item X;
  
  public static Item Y;
  
  public static Item Z;
  
  public static Item aa;
  
  public static Item ab;
  
  public static Item ac;
  
  public static Item ad;
  
  public static Item ae;
  
  public static Item af;
  
  public static Item ag;
    
  public static Item aj;
  
  public static Item ak;
  
  public static Item al;
    
  public static Item an;
  
  public static Item ao;
  
  public static Item ap;
  
  public static Item aq;
  
  public static Item ar;
  
  public static Item as;
  
  public static Item at;
  
  public static Item au;
  
  public static Item av;
  
  public static Item aw;
  
  public static Item ax;
  
  public static Item ay;
  
  public static Item az;
  
  public static Item aA;
    
  
  public static Item aO;
  
  public static Item aP;
  
  public static Item aQ;
  
  public static Item aR;
  
  public static Item aS;
  
  public static Item aT;
  
  public static Item aU;
  
  public static Item bb;
  
  public static Item bc;
  
      
  
  public static Item bj;
    
  public static Item bl;
  
  public static Item bm;
  
  public static Item bn;
  
  public static Item bo;
    
  public static Block bq;
  
  public static Block br;
  
  public static Block bs;
  
  public static Block bt;
  
  public static Block bu;
  
  public static Block bv;
  
  public static Block bw;
  
  public static Block bx;
  
  public static Block by;
    
  public static Block bA;
  
  
  public static CreativeTabs bI = (new ArmaManiaTabs("arma_mania_tab")).setBackgroundImageName("Text_New.png").setNoTitle();
  
  public static CreativeTabs bJ = (new ArmaManiaTabsIcon("arma_mania_tab_icon")).setBackgroundImageName("Text_New.png").setNoTitle();
  
  public static ItemArmor.ArmorMaterial bK = EnumHelper.addArmorMaterial("armorHopital", 18, new int[] { 2, 4, 4, 2 }, 1);
  
  public static ItemArmor.ArmorMaterial bL = EnumHelper.addArmorMaterial("armorPolice", 36, new int[] { 2, 4, 4, 2 }, 1);
  
  @EventHandler
  public void preinit(FMLPreInitializationEvent paramFMLPreInitializationEvent) {
    b = (new itemCannabis()).setUnlocalizedName("itemCannabis").setTextureName("armamania:cannabis1").setCreativeTab(bI).setMaxStackSize(3);
    c = (new itemCannabis2()).setUnlocalizedName("itemCannabis2").setTextureName("armamania:cannabis2").setCreativeTab(bI).setMaxStackSize(1);
    d = (new itemCannabis3()).setUnlocalizedName("itemCannabis3").setTextureName("armamania:cannabis3").setCreativeTab(bI).setMaxStackSize(1);
    e = (new itemPeyote1()).setUnlocalizedName("itemPeyote1").setTextureName("armamania:peyote1").setCreativeTab(bI).setMaxStackSize(4);
    f = (new itemPeyote2()).setUnlocalizedName("itemPeyote2").setTextureName("armamania:peyote2").setCreativeTab(bI).setMaxStackSize(1);
    g = (new itemCok1()).setUnlocalizedName("itemCok1").setTextureName("armamania:cok1").setCreativeTab(bI).setMaxStackSize(3);
    h = (new itemCok2()).setUnlocalizedName("itemCok2").setTextureName("armamania:cok2").setCreativeTab(bI).setMaxStackSize(2);
    i = (new itemCok3()).setUnlocalizedName("itemCok3").setTextureName("armamania:cok3").setCreativeTab(bI).setMaxStackSize(1);
    z = (new itemCheque()).setUnlocalizedName("cheque").setTextureName("armamania:cheque").setCreativeTab(bI);
    A = (new itemFouille()).setUnlocalizedName("BatonFouille").setTextureName("armamania:batonFouille").setCreativeTab(bI).setMaxStackSize(1);
    C = (new itemNote1()).setUnlocalizedName("1 $").setTextureName("armamania:note1").setCreativeTab(bI);
    D = (new itemNote10()).setUnlocalizedName("10 $").setTextureName("armamania:note10").setCreativeTab(bI);
    E = (new itemNote100()).setUnlocalizedName("100 $").setTextureName("armamania:note100").setCreativeTab(bI);
    F = (new itemNote1000()).setUnlocalizedName("1000 $").setTextureName("armamania:note1000").setCreativeTab(bI);
    M = (new itemLockpick()).setUnlocalizedName("lockpick").setTextureName("armamania:lockpick").setCreativeTab(bI).setMaxStackSize(1);
    O = (new itemArtefact()).setUnlocalizedName("Artefact").setTextureName("armamania:Artefact").setCreativeTab(bI).setMaxStackSize(5);
    P = (new itemLoto()).setUnlocalizedName("TicketLoto").setTextureName("armamania:loto").setCreativeTab(bI).setMaxStackSize(1);
    Q = (new itemBaton()).setUnlocalizedName("Baton").setTextureName("armamania:baton").setCreativeTab(bI).setMaxStackSize(1);
    N = (new itemContrat()).setUnlocalizedName("Contrat").setTextureName("armamania:contrat").setCreativeTab(bI).setMaxStackSize(1);
    R = (new itemDallas(bK, 0)).setUnlocalizedName("Dallas").setTextureName("armamania:Dallas").setCreativeTab(bI);
    V = (new itemDallas2(bK, 0)).setUnlocalizedName("Hoxton").setTextureName("armamania:Hoxton").setCreativeTab(bI);
    T = (new itemTazer()).setUnlocalizedName("Tazer").setTextureName("armamania:tazer").setCreativeTab(bI).setMaxStackSize(1);
    U = (new itemBillets()).setUnlocalizedName("Billets").setTextureName("armamania:billets").setCreativeTab(bI);
    X = (new itemPizza(11, 0.6F, false)).setUnlocalizedName("Pizza").setTextureName("armamania:pizza").setCreativeTab(bI).setMaxStackSize(1);
    Y = (new itemPizza(10, 0.6F, false)).setUnlocalizedName("Hotdog").setTextureName("armamania:hotdog").setCreativeTab(bI).setMaxStackSize(1);
    Z = (new itemPizza(11, 0.6F, false)).setUnlocalizedName("Hamburger").setTextureName("armamania:burger").setCreativeTab(bI).setMaxStackSize(1);
    ad = (new itemPizza(8, 0.6F, false)).setUnlocalizedName("Sandwich").setTextureName("armamania:sandwich").setCreativeTab(bI).setMaxStackSize(1);
    ab = (new itemPizza(4, 0.6F, false)).setUnlocalizedName("Yogurt1").setTextureName("armamania:yogurt").setCreativeTab(bI).setMaxStackSize(2);
    ac = (new itemPizza(4, 0.6F, false)).setUnlocalizedName("Yogurt2").setTextureName("armamania:yogurt2").setCreativeTab(bI).setMaxStackSize(2);
    aa = (new itemPizza(6, 0.6F, false)).setUnlocalizedName("Frites").setTextureName("armamania:frites").setCreativeTab(bI).setMaxStackSize(3);
    ae = (new itemPizza(2, 0.6F, false)).setUnlocalizedName("ArmaFizz").setTextureName("armamania:aramfizz").setCreativeTab(bI).setMaxStackSize(10);
    af = (new itemPizza(7, 0.6F, false)).setUnlocalizedName("Donuts").setTextureName("armamania:donuts").setCreativeTab(bI).setMaxStackSize(4);
    ag = (new itemTabac()).setUnlocalizedName("Tabac").setTextureName("armamania:Tabac").setCreativeTab(bI).setMaxStackSize(4);
    aj = (new itemPeche()).setUnlocalizedName("Peche").setTextureName("armamania:peche").setCreativeTab(bI).setMaxStackSize(5);
    ak = (new itemCarte1()).setUnlocalizedName("CarteCitoyen").setTextureName("armamania:CarteCitoyen").setCreativeTab(bI).setMaxStackSize(1);
    al = (new itemCarte2()).setUnlocalizedName("CarteGouv").setTextureName("armamania:CartePolice").setCreativeTab(bI).setMaxStackSize(1);
    an = (new itemDriedCok()).setUnlocalizedName("Cok4").setTextureName("armamania:driedCocaLeaves").setCreativeTab(bI).setMaxStackSize(2);
    ao = (new itemRaisin()).setUnlocalizedName("Raisin").setTextureName("armamania:itemRaisin").setCreativeTab(bI).setMaxStackSize(4);
    ap = (new itemVin(0.2f)).setUnlocalizedName("Vin").setTextureName("armamania:vin").setCreativeTab(bI).setMaxStackSize(1);
    aq = (new itemOrge()).setUnlocalizedName("Orge").setTextureName("armamania:orge").setCreativeTab(bI).setMaxStackSize(4);
    ar = (new itemMalt()).setUnlocalizedName("Malt").setTextureName("armamania:malt").setCreativeTab(bI).setMaxStackSize(2);
    au = (new itemBierre(0.2f)).setUnlocalizedName("Bierre").setTextureName("armamania:biere").setCreativeTab(bI).setMaxStackSize(1);
    as = (new itemBarril1()).setUnlocalizedName("BarrilVin").setTextureName("armamania:BarrilVin").setCreativeTab(bI).setMaxStackSize(1);
    at = (new itemBarril2()).setUnlocalizedName("BarrilBierre").setTextureName("armamania:BarrilBierre").setCreativeTab(bI).setMaxStackSize(1);
    av = (new itemTabac2()).setUnlocalizedName("Tabac2").setTextureName("armamania:tabac2").setCreativeTab(bI).setMaxStackSize(2);
    aw = (new ItemCigarette()).setUnlocalizedName("Cigarette").setTextureName("armamania:cigarette").setCreativeTab(bI).setMaxStackSize(1);
    ax = (new itemCafe()).setUnlocalizedName("Cafe").setTextureName("armamania:gcafe").setCreativeTab(bI).setMaxStackSize(4);
    ay = (new itemBarril3()).setUnlocalizedName("BarrilCafe").setTextureName("armamania:BarrilCafe").setCreativeTab(bI).setMaxStackSize(1);
    aA = (new itemBeets()).setUnlocalizedName("Betteraves").setTextureName("armamania:betteraves").setCreativeTab(bI).setMaxStackSize(4);
 
    aO = (new itemPermis()).setUnlocalizedName("Permis_Conduire").setTextureName("armamania:permisC").setCreativeTab(bI);
    aP = (new itemPermis()).setUnlocalizedName("Permis_Armes").setTextureName("armamania:permisA").setCreativeTab(bI);
    aQ = (new itemPermis()).setUnlocalizedName("Brevet_Pilote").setTextureName("armamania:permisP").setCreativeTab(bI);
    aR = (new itemPermis()).setUnlocalizedName("Permis_Construire").setTextureName("armamania:permisCo").setCreativeTab(bI);
    aS = (new itemPermis()).setUnlocalizedName("Permis_Agricole").setTextureName("armamania:permisAg").setCreativeTab(bI);
    aT = (new itemPermis()).setUnlocalizedName("Permis_Mine").setTextureName("armamania:permisM").setCreativeTab(bI);
    aU = (new itemPermis()).setUnlocalizedName("Permis_Archéologie").setTextureName("armamania:permisAr").setCreativeTab(bI);
    bb = (new itemPermis()).setUnlocalizedName("Montre1").setTextureName("armamania:montrebasique").setCreativeTab(bI);
    bc = (new itemPermis()).setUnlocalizedName("MontreLuxe").setTextureName("armamania:montreluxe").setCreativeTab(bI);
    bj = (new itemPermis()).setUnlocalizedName("radio").setTextureName("armamania:radio").setCreativeTab(bI);
    bl = (new itemPermis()).setUnlocalizedName("Gang_Icone").setTextureName("armamania:gangster").setCreativeTab(bJ);
    bm = (new itemPermis()).setUnlocalizedName("Police_Icone").setTextureName("armamania:police").setCreativeTab(bJ);
    bn = (new itemPermis()).setUnlocalizedName("Maire_Icone").setTextureName("armamania:maire").setCreativeTab(bJ);
    bo = (new itemPermis()).setUnlocalizedName("Citoyen_Icone").setTextureName("armamania:civil").setCreativeTab(bJ);
    GameRegistry.registerItem(X, "itemPizza");
    GameRegistry.registerItem(b, "itemCannabis");
    GameRegistry.registerItem(c, "itemCannabis2");
    GameRegistry.registerItem(d, "itemCannabis3");
    GameRegistry.registerItem(e, "itemPeyote1");
    GameRegistry.registerItem(f, "itemPeyote2");
    GameRegistry.registerItem(g, "itemCok1");
    GameRegistry.registerItem(h, "itemCok2");
    GameRegistry.registerItem(i, "itemCok3");
    GameRegistry.registerItem(z, "ItemCheque");
    GameRegistry.registerItem(A, "ItemFouille");
    GameRegistry.registerItem(C, "itemNote1");
    GameRegistry.registerItem(D, "itemNote10");
    GameRegistry.registerItem(E, "itemNote100");
    GameRegistry.registerItem(F, "itemNote1000");
    GameRegistry.registerItem(M, "itemLockpick");
    GameRegistry.registerItem(R, "ItemDallas");
    GameRegistry.registerItem(V, "ItemDallas2");
    GameRegistry.registerItem(U, "ItemBillets");
    GameRegistry.registerItem(T, "ItemTazer");
    GameRegistry.registerItem(Y, "ItemHotdog");
    GameRegistry.registerItem(Z, "ItemHamburger");
    GameRegistry.registerItem(ad, "ItemSandwich");
    GameRegistry.registerItem(ab, "ItemYogurt1");
    GameRegistry.registerItem(ac, "ItemYogurt2");
    GameRegistry.registerItem(aa, "ItemFrites");
    GameRegistry.registerItem(ae, "ItemArmaFizz");
    GameRegistry.registerItem(af, "ItemDonuts");
    GameRegistry.registerItem(ag, "ItemTabac");
    GameRegistry.registerItem(aj, "ItemPeche");
    GameRegistry.registerItem(ak, "ItemCarte1");
    GameRegistry.registerItem(al, "ItemCarte2");
    GameRegistry.registerItem(an, "ItemDriedCok");
    GameRegistry.registerItem(O, "ItemArtefact");
    GameRegistry.registerItem(Q, "ItemBaton");
    GameRegistry.registerItem(P, "ItemLoto");
    GameRegistry.registerItem(N, "ItemContrat");
    GameRegistry.registerItem(ao, "ItemRaisin");
    GameRegistry.registerItem(ap, "ItemVin");
    GameRegistry.registerItem(aq, "ItemOrge");
    GameRegistry.registerItem(ar, "ItemMalt");
    GameRegistry.registerItem(au, "ItemBierre");
    GameRegistry.registerItem(as, "ItemBarril1");
    GameRegistry.registerItem(at, "ItemBarril2");
    GameRegistry.registerItem(av, "ItemTabac2");
    GameRegistry.registerItem(aw, "ItemCigarette");
    GameRegistry.registerItem(ax, "ItemCafe");
    GameRegistry.registerItem(ay, "ItemBarril3");
    GameRegistry.registerItem(aA, "ItemBeets");
    GameRegistry.registerItem(aO, "itemPermis1");
    GameRegistry.registerItem(aP, "itemPermis2");
    GameRegistry.registerItem(aQ, "itemPermis3");
    GameRegistry.registerItem(aR, "itemPermis4");
    GameRegistry.registerItem(aS, "itemPermis5");
    GameRegistry.registerItem(aT, "itemPermis6");
    GameRegistry.registerItem(aU, "itemPermis7");
    GameRegistry.registerItem(bb, "itemMontre1");
    GameRegistry.registerItem(bc, "itemMontre2");
    GameRegistry.registerItem(bj, "itemRadio");
    GameRegistry.registerItem(bl, "itemGang");
    GameRegistry.registerItem(bm, "itemPolice");
    GameRegistry.registerItem(bn, "itemMaire");
    GameRegistry.registerItem(bo, "itemCitoyen");
    
    bq = (new BlockTest2(Material.grass)).setBlockName("CannabisPlant").setBlockTextureName("armamania:cannabisBlock").setCreativeTab(bI);
    br = (new BlockTest3(Material.grass)).setBlockName("CocainePlant").setBlockTextureName("armamania:cocaineBlock").setCreativeTab(bI);
    bs = (new BlockTest4(Material.grass)).setBlockName("TabacPlant").setBlockTextureName("armamania:tabacBlock").setCreativeTab(bI);
    bt = (new BlockPeche(Material.grass)).setBlockName("PecheBlock").setBlockTextureName("armamania:Fpeche").setCreativeTab(bI);
    bu = (new BlockRaisin(Material.grass)).setBlockName("RaisinBlock").setBlockTextureName("armamania:Fraison").setCreativeTab(bI);
    bv = (new BlockPeyote(Material.grass)).setBlockName("PeyoteBlock").setCreativeTab(bI);
    bw = (new BlockBiere(Material.grass)).setBlockName("OrgeBlock").setBlockTextureName("armamania:tabacPlante").setCreativeTab(bI);
    bx = (new BlockCafe(Material.grass)).setBlockName("CafeBlock").setBlockTextureName("armamania:cafePlante").setCreativeTab(bI);
    by = (new BlockDistrib(Material.grass)).setBlockName("Distributeur").setCreativeTab(bI);
    bA = (new BlockBetter(Material.grass)).setBlockName("BetteravesBlock").setBlockTextureName("armamania:betteraves").setCreativeTab(bI);
    GameRegistry.registerBlock(bq, "blockTest2");
    GameRegistry.registerBlock(br, "blockTest3");
    GameRegistry.registerBlock(bs, "blockTest4");
    GameRegistry.registerBlock(bt, "blockPeche");
    GameRegistry.registerBlock(bu, "blockRaisin");
    GameRegistry.registerBlock(bv, "blockPeyote");
    GameRegistry.registerBlock(bw, "blockBiere");
    GameRegistry.registerBlock(bx, "blockCafe");
    GameRegistry.registerBlock(by, "blockDistrib");
    GameRegistry.registerBlock(bA, "blockBetter");
  }
  
  

}