package fr.craftyourliferp.main;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import com.flansmod.common.FlansMod;
import com.flansmod.common.driveables.DriveableDestroyed;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityPlane;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.ItemPlane;
import com.flansmod.common.driveables.ItemVehicle;
import com.flansmod.common.guns.ItemGrenade;
import com.flansmod.common.guns.ItemGun;
import com.flansmod.common.guns.ItemShootable;
import com.flansmod.common.network.PacketSync;

import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.animations.PlayerAnimator;
import fr.craftyourliferp.blocks.tileentity.IStealingTileEntity;
import fr.craftyourliferp.capture.CaptureProcess;
import fr.craftyourliferp.cosmetics.CosmeticCachedData;
import fr.craftyourliferp.cosmetics.CosmeticManager;
import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.data.ContactData;
import fr.craftyourliferp.data.IdentityData;
import fr.craftyourliferp.data.NumberData;
import fr.craftyourliferp.data.PaypalData;
import fr.craftyourliferp.data.PhoneData;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.PlayerData;
import fr.craftyourliferp.data.ShopData;
import fr.craftyourliferp.data.SmsData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.entities.EntityFootballBall;
import fr.craftyourliferp.entities.EntityItemCollider;
import fr.craftyourliferp.game.events.ReanimationHandler;
import fr.craftyourliferp.network.PacketAlcol;
import fr.craftyourliferp.network.PacketBase;
import fr.craftyourliferp.network.PacketBitcoinPage;
import fr.craftyourliferp.network.PacketCosmetic;
import fr.craftyourliferp.network.PacketCustomInterract;
import fr.craftyourliferp.network.PacketProning;
import fr.craftyourliferp.network.PacketSleeping;
import fr.craftyourliferp.network.PacketStealing;
import fr.craftyourliferp.network.PacketSyncPhone;
import fr.craftyourliferp.network.PacketSyncPlayerData;
import fr.craftyourliferp.network.PacketSyncShield;
import fr.craftyourliferp.network.PacketThirst;
import fr.craftyourliferp.penalty.Penalty;
import fr.craftyourliferp.penalty.PenaltyManager;
import fr.craftyourliferp.penalty.VehiclePenalty;
import fr.craftyourliferp.phone.NetworkCallTransmitter;
import fr.craftyourliferp.phone.web.page.BMGPage;
import fr.craftyourliferp.phone.web.page.BitcoinConverterPage;
import fr.craftyourliferp.phone.web.page.WebPageData;
import fr.craftyourliferp.shield.ShieldStats;
import fr.craftyourliferp.thirst.ThirstStats;
import fr.craftyourliferp.utils.ICallback;
import fr.craftyourliferp.utils.MathsUtils;
import fr.craftyourliferp.utils.MinecraftUtils;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentTranslation;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.common.IExtendedEntityProperties;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.EntityInteractEvent;

public class ExtendedPlayer implements IExtendedEntityProperties {

    public final static String EXT_PROP_NAME = "CYLRPMAINDATA";
    
    private EntityPlayer player;
    
    public final static int belierUseTime = 2*60;// in seconds 

    public final static int authentificationAttemptEveryMinutes = 5;
    
    public static final int LimitVehicle = 11;
    public static final int startOwnedSlot = 1;
    public static final int slotPriceFactor = 12;
 
    public int authentificationAttempt = 0;
    
    public long authentificationAttemptResetTime;
    
    
    public ArrayList<ItemStack> ownedVehicle = new ArrayList<ItemStack>();
    public ArrayList<ItemStack> ownedPlane = new ArrayList<ItemStack>();
    public ArrayList<ItemStack> ownedBoat = new ArrayList<ItemStack>();
    
    public PenaltyManager PenaltyManager = new PenaltyManager(this);
    
    public List<Integer> stolenVehiclesId = new ArrayList<Integer>();
    public HashMap<Integer, DriveableDestroyed> explodedVehicles = new HashMap<Integer, DriveableDestroyed>();

    public int vCoins = 0;
    
    public int ownedSlot = startOwnedSlot;

    public boolean hasLicence;
            
    public ShieldStats shield;
    public ThirstStats thirst;

    public PhoneData phoneData;
    
    public IdentityData identityData;
    
    public boolean firstJoin;
    
    public boolean penaltyReset = false;
        
    public boolean isUsingVestBullet = false;
    
    public long lastBelierUseTime;
    
    public float bitcoin;

    public PaypalData paypalData = new PaypalData();
    
    public List<CosmeticObject> cosmeticsData = new ArrayList();
    
    public List<ItemStack> itemStockage = new ArrayList();
    
    public boolean inAviation;
    
    private float gAlcolInBlood;
    
    public int tickAlcol;
    
    public HashMap<UUID, ChunkCoordinates> petsOwned = new HashMap<UUID, ChunkCoordinates>();
        
    public int sleepingTime;
    
    private boolean isSleeping;
    
    public double sleepX, sleepY, sleepZ;
            
	public PlayerAnimator currentPlayingAnimation;
	
	public int lastProningTick;
	private boolean proning = false;
	
	//0 : no animation
	public byte currentAnimation;
	
	public PlayerData serverData = new PlayerData();
	
	public ShopData shopData = new ShopData();

	public boolean passwordReceived;
		
	public Vec3 connectionPos;

	public WebPageData currentPageData;
	
	public ItemStack usedItem;
	public int itemPressTicks;
	
	public IStealingTileEntity stealingTile;
	
	public WorldSelector regionBuilder;

	/*
	 * 0: fire
	 * 1: explosion
	 * 2: capture
	 */
	public byte builderType;
	
	public byte captureType;
	
	public EntityFootballBall selectedBall;

	public int ticksChangeSlot = -1;
	public int slotEquippedShield = -1;
	public int previousSelectedSlot;
	
	public EntityItemCollider entityItemCollider;
	
	private List<WorldSelector> capturingRegions = new ArrayList();
		
	public List<String> currentRegions = new ArrayList();
	
	private Object callbackCheckIfDoctor;
	
	private boolean shouldBeReanimate = false;
	
	public long startTimeTimestamp = 0;
	public long lastIdentityResetTime = 0;
	public int reanimationTick = 0;
	
	public String reanimatingPlayername;
		
	public static final int identityResetTimeInSeconds = 60*60*24*7;


    public ExtendedPlayer(EntityPlayer player) 
    {    	
        this.player = player;
        
        shield = new ShieldStats(ShieldStats.initialShield, player);
        thirst = new ThirstStats(ThirstStats.initialThirst, player);
        phoneData = new PhoneData(player.worldObj);
        identityData = new IdentityData();
        
        firstJoin = true;
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
    	
        NBTTagCompound properties = new NBTTagCompound();
        	
        if(this.phoneData != null && this.phoneData.getNumber() != null && !this.phoneData.getNumber().isEmpty())
        	properties.setString("Number", this.phoneData.getNumber());

        if(this.shield != null)
        	properties.setFloat("Shield", this.shield.getShield());
        
        if(this.thirst != null)
        	properties.setFloat("Thirst", this.thirst.getThirst());
        	
        if(this.identityData != null)
        {
        	if(this.identityData.lastname != null && !this.identityData.lastname.isEmpty())
        		properties.setString("Lastname", this.identityData.lastname);
        	if(this.identityData.name != null && !this.identityData.name.isEmpty())
        		properties.setString("Name", this.identityData.name);
        	if(this.identityData.birthday != null && !this.identityData.birthday.isEmpty())
        		properties.setString("Birthday", this.identityData.birthday);
        	if(this.identityData.gender != null && !this.identityData.gender.isEmpty())
        		properties.setString("Gender", this.identityData.gender);
        }
        
        properties.setBoolean("PenaltyReset", penaltyReset);
        properties.setBoolean("FirstJoin", firstJoin);
        properties.setInteger("AuthentificationAttempt", authentificationAttempt);
        properties.setLong("AuthentificationAttemptResetTime", authentificationAttemptResetTime);
        properties.setFloat("Bitcoin", bitcoin);
    	properties.setInteger("Vcoins", vCoins);
    	properties.setInteger("OwnedSlots", ownedSlot);
    	properties.setBoolean("HasLicence", hasLicence);
    	properties.setLong("LastBelierUseTime", lastBelierUseTime);
    	properties.setBoolean("InAviation", inAviation);
    	properties.setFloat("gAlcolInBlood", gAlcolInBlood);
    	properties.setBoolean("isSleeping", isSleeping);
    	properties.setInteger("SleepingTime", sleepingTime);
    	
    	properties.setDouble("SleepingX", sleepX);
    	properties.setDouble("SleepingY", sleepY);
    	properties.setDouble("SleepingZ", sleepZ);
    	
    	properties.setBoolean("ShouldBeReanimate", shouldBeReanimate);
    	properties.setLong("StartTimeTimestamp", startTimeTimestamp);
    	properties.setLong("LastIdentityResetTime", lastIdentityResetTime);


    	NBTTagList petsList = new NBTTagList();
    	for(Map.Entry<UUID, ChunkCoordinates> uuid : petsOwned.entrySet())
    	{
    		NBTTagCompound petCompound = new NBTTagCompound();
    		petCompound.setString("PetUUID", uuid.getKey().toString());
    		petCompound.setInteger("x", uuid.getValue().posX);
    		petCompound.setInteger("y", uuid.getValue().posY);
    		petCompound.setInteger("z", uuid.getValue().posZ);
    		petsList.appendTag(petCompound);
    	}
    	properties.setTag("OwnedPets", petsList);
    	
    	NBTTagList stockageList = new NBTTagList();
    	for(ItemStack is : itemStockage)
    	{
    		NBTTagCompound itemstackCompound = new NBTTagCompound();
    		is.writeToNBT(itemstackCompound);
    		stockageList.appendTag(itemstackCompound);
    	}
    	properties.setTag("Stockage", stockageList);
        	
        compound.setTag(EXT_PROP_NAME + "-GeneralData", properties);
        	
        NBTTagList tagList = new NBTTagList();

        for(CosmeticObject obj : cosmeticsData)
        {  	
        	NBTTagCompound cosmeticTag = new NBTTagCompound();
        	obj.writeToNBT(cosmeticTag);
        	tagList.appendTag(cosmeticTag);
        }
        compound.setTag(EXT_PROP_NAME + "-Cosmetics", tagList);
        
        NBTTagCompound vehiclesTag = new NBTTagCompound();
        
        NBTTagList ownedVehiclesTag = new NBTTagList();
    	for(int i = 0; i < ownedVehicle.size(); i++) {
    		ItemStack item = ownedVehicle.get(i);
    		
    		if(item != null) {
    			NBTTagCompound tag = new NBTTagCompound();
    			item.writeToNBT(tag);
    			ownedVehiclesTag.appendTag(tag);
    		}
    	}    
    	for(int i = 0; i < ownedPlane.size(); i++) 
    	{
    		ItemStack item = ownedPlane.get(i);
    		
    		if(item != null) {
    			NBTTagCompound tag = new NBTTagCompound();
    			item.writeToNBT(tag);
    			ownedVehiclesTag.appendTag(tag);
    		}
    	}  
       	for(int i = 0; i < ownedBoat.size(); i++) 
       	{
    		ItemStack item = ownedBoat.get(i);
    		
    		if(item != null) {
    			NBTTagCompound tag = new NBTTagCompound();
    			item.writeToNBT(tag);
    			ownedVehiclesTag.appendTag(tag);
    		}
    	}  
       	
       NBTTagList stolenVehiclesList = new NBTTagList();
 	   for(int i = 0; i < stolenVehiclesId.size(); i++)
 	   {
 			int id = stolenVehiclesId.get(i);
 			NBTTagCompound vehicleTag = new NBTTagCompound();
 			vehicleTag.setInteger("VehicleId", id);
 			stolenVehiclesList.appendTag(vehicleTag);
 	   }
 	   
       NBTTagList explodedVehiclesList = new NBTTagList();
       for(Map.Entry<Integer, DriveableDestroyed> entry : explodedVehicles.entrySet())
       {
			NBTTagCompound vehicleTag = new NBTTagCompound();
    	    entry.getValue().writeToNbt(vehicleTag);
 			explodedVehiclesList.appendTag(vehicleTag);
       }
 	   
  	  vehiclesTag.setTag("ExplodedVehicles", explodedVehiclesList);
 	  vehiclesTag.setTag("StolenVehicles", stolenVehiclesList);
      vehiclesTag.setTag("OwnedVehicles", ownedVehiclesTag);
       	
      compound.setTag(EXT_PROP_NAME + "-Vehicles", vehiclesTag);
       
      NBTTagCompound penaltyTag = new NBTTagCompound();
      PenaltyManager.writeToNbt(penaltyTag);
      
      compound.setTag(EXT_PROP_NAME + "-Penalties", penaltyTag);

      
      NBTTagCompound paypalTag = new NBTTagCompound();
      paypalData.writeToNbt(paypalTag);
      
      compound.setTag(EXT_PROP_NAME + "-Paypal", paypalTag);

      

    }
    	
    
    @Override
    public void loadNBTData(NBTTagCompound compound) {
    	
        NBTTagCompound properties = compound.getCompoundTag(EXT_PROP_NAME + "-GeneralData");
    	
        
        if(properties.hasKey("Number")) phoneData.setNumber(properties.getString("Number"));
        
        if(properties.hasKey("Shield")) shield.setShield(properties.getFloat("Shield"));

        if(properties.hasKey("Thirst")) thirst.setThirst(properties.getFloat("Thirst"));

        if(properties.hasKey("Lastname"))
        {
	        identityData.lastname = properties.getString("Lastname");
	        identityData.name = properties.getString("Name");
	        identityData.birthday = properties.getString("Birthday");
	        identityData.gender = properties.getString("Gender");
        }

        firstJoin = properties.getBoolean("FirstJoin");
        authentificationAttempt = properties.getInteger("AuthentificationAttempt");
        authentificationAttemptResetTime = properties.getLong("AuthentificationAttemptResetTime");
        bitcoin = properties.getFloat("Bitcoin");
        vCoins = properties.getInteger("Vcoins");
        ownedSlot = properties.getInteger("OwnedSlots");
        hasLicence = properties.getBoolean("HasLicence");
        penaltyReset = properties.getBoolean("PenaltyReset");
        lastBelierUseTime = properties.getLong("LastBelierUseTime");
        inAviation = properties.getBoolean("InAviation");
        gAlcolInBlood = properties.getFloat("gAlcolInBlood");
    	isSleeping = properties.getBoolean("isSleeping");
    	sleepingTime = properties.getInteger("SleepingTime");

    	sleepX = properties.getDouble("SleepingX");
    	sleepY = properties.getDouble("SleepingY");
    	sleepZ = properties.getDouble("SleepingZ");
        
    	setShouldBeReanimate(properties.getBoolean("ShouldBeReanimate"));
    	startTimeTimestamp = properties.getLong("StartTimeTimestamp");
    	lastIdentityResetTime = properties.getLong("LastIdentityResetTime");

    	
    	NBTTagList petsList = (NBTTagList) properties.getTag("OwnedPets");

    	if(petsList != null)
    	{
	    	for(int i = 0; i < petsList.tagCount(); i++)
	    	{    		
	    		NBTTagCompound itemstackCompound = petsList.getCompoundTagAt(i);
	    		UUID uuid = UUID.fromString(itemstackCompound.getString("PetUUID"));
	    		if(!itemstackCompound.hasKey("x"))
	    		{
	    			continue;
	    		}
	    		ChunkCoordinates chunkCoordinates = new ChunkCoordinates(itemstackCompound.getInteger("x"), itemstackCompound.getInteger("y"), itemstackCompound.getInteger("z"));

	    		petsOwned.put(uuid,chunkCoordinates);
	    	}  	
    	}
        
    	NBTTagList stockageList =  (NBTTagList) properties.getTag("Stockage");
    	if(stockageList != null)
    	{
	    	for(int i = 0; i < stockageList.tagCount(); i++)
	    	{
	    		NBTTagCompound itemstackCompound = stockageList.getCompoundTagAt(i);
	    		ItemStack is = ItemStack.loadItemStackFromNBT(itemstackCompound);
	    		itemStockage.add(is);
	    	}  	
    	}
   
        NBTTagList tagList = (NBTTagList)compound.getTag(EXT_PROP_NAME + "-Cosmetics");
        
        if(tagList != null)
        {
	        int i = 0;
	        for(CosmeticObject obj : cosmeticsData)
	        {
	        	NBTTagCompound cosmeticTag = tagList.getCompoundTagAt(i);

	    		obj.loadNBTData(cosmeticTag);    	
	        	i++;
	        }
        }
        
        
        NBTTagCompound vehicleTag = (NBTTagCompound) compound.getTag(EXT_PROP_NAME + "-Vehicles");

        if(vehicleTag != null)
        {
	        NBTTagList ownedVehiclesTags = (NBTTagList) vehicleTag.getTag("OwnedVehicles");
	        
	        if(ownedVehiclesTags != null)
	        {
		    	for(int i = 0; i < ownedVehiclesTags.tagCount(); i++)
		    	{
		    		ItemStack vehicleIs = ItemStack.loadItemStackFromNBT(ownedVehiclesTags.getCompoundTagAt(i));
		    		
		    		if(vehicleIs.getItem() instanceof ItemVehicle)
		    		{
		    			ItemVehicle itemVehicle = (ItemVehicle) vehicleIs.getItem();
		    			if(itemVehicle.type.IsBoat())
		    			{
		    				ownedBoat.add(vehicleIs);
		    			}
		    			else
		    			{
		    				ownedVehicle.add(vehicleIs);
		    			}
		    		}
		    		else if(vehicleIs.getItem() instanceof ItemPlane)
		    		{
		        		ownedPlane.add(vehicleIs);
		    		}
		    	}    
	        }
        	
    	
	    	 NBTTagList stolenVehiclesList = (NBTTagList) vehicleTag.getTag("StolenVehicles");
	    	 if(stolenVehiclesList != null)
	    	 {
			   	 for(int i = 0; i < stolenVehiclesList.tagCount(); i++)
			   	 {
			   		 int id = stolenVehiclesList.getCompoundTagAt(i).getInteger("VehicleId");
			   		 stolenVehiclesId.add(id);
			   	 }
	    	 }
	    	 
	    	 NBTTagList explodedVehiclesList = (NBTTagList) vehicleTag.getTag("ExplodedVehicles");
	    	 if(explodedVehiclesList != null)
	    	 {
			   	 for(int i = 0; i < explodedVehiclesList.tagCount(); i++)
			   	 {
			   		 DriveableDestroyed destroyedDriveable = new DriveableDestroyed();
			   		 destroyedDriveable.readFromNbt(explodedVehiclesList.getCompoundTagAt(i));
			   		 this.explodedVehicles.put(destroyedDriveable.getVehicleId(),destroyedDriveable);
			   	 }
	    	 }
        }
	   	 
	     NBTTagCompound penaltyTag = compound.getCompoundTag(EXT_PROP_NAME + "-Penalties");  
	    
	     if(penaltyTag != null) PenaltyManager.readFromNbt(penaltyTag);
	     
	     if(!penaltyReset)
	     {
	    	 penaltyReset = true;
	    	 PenaltyManager.resetPenalty();
	     }
	     
	     NBTTagCompound paypalTag = compound.getCompoundTag(EXT_PROP_NAME + "-Paypal");  
	     paypalData.readFromNbt(paypalTag);
	    	     
    }

    public static void saveProxyData(EntityPlayer player) {
        ExtendedPlayer playerData = ExtendedPlayer.get(player);
        NBTTagCompound savedData = new NBTTagCompound();

        playerData.saveNBTData(savedData);
        CommonProxy.storeEntityData(getSaveKey(player), savedData);
    }

    public static void loadProxyData(EntityPlayer player) {
        ExtendedPlayer playerData = ExtendedPlayer.get(player);
        NBTTagCompound savedData = CommonProxy.getEntityData(getSaveKey(player));
        if (savedData != null) {
            playerData.loadNBTData(savedData);
        }     
    }
    

    private static String getSaveKey(EntityPlayer player) {
        return player.getDisplayName() + ":" + EXT_PROP_NAME;
    }

    public static final void register(EntityPlayer player) {
        player.registerExtendedProperties(ExtendedPlayer.EXT_PROP_NAME, new ExtendedPlayer(player).initNewData());
    }

    public static final ExtendedPlayer get(EntityPlayer player) {
    	ExtendedPlayer extendedPlayer = (ExtendedPlayer) player.getExtendedProperties(EXT_PROP_NAME);
    	if(extendedPlayer != null)
    	{
    		extendedPlayer.player = player;
    		return extendedPlayer;
    	}
        return null;
    }

    public EntityPlayer getPlayer()
    {
        return this.player;
    }

	@Override
	public void init(Entity entity, World world) {}
	
	public ExtendedPlayer initNewData()
	{
    	CosmeticManager manager = CraftYourLifeRPMod.getCosmeticManager();
    	for(CosmeticObject obj : manager.getCosmetics())
    	{
    		CosmeticObject copy = manager.getCopy(obj);
    		cosmeticsData.add(copy);    		
    	}
		return this;
	}
	
    public void syncShield() {
    	if(!player.worldObj.isRemote)
    	{
    		if(this.shield != null)
    			CraftYourLifeRPMod.packetHandler.sendTo(new PacketSyncShield(this.shield.getShield()), (EntityPlayerMP) player);
    	}
    	else
    	{
    		CraftYourLifeRPMod.packetHandler.sendToServer(new PacketSyncShield(0));
    	}
    }
    
    public void syncThirst() {
    	if(!player.worldObj.isRemote)
    	{
    		if(this.thirst != null)
    			CraftYourLifeRPMod.packetHandler.sendTo(PacketThirst.syncThirst(this.thirst.getThirst()), (EntityPlayerMP) player);
    	}
    	else
    	{
    		CraftYourLifeRPMod.packetHandler.sendToServer(PacketThirst.syncThirst());
    	}
    }
    
    
    public void syncPhone() {
    	if(!player.worldObj.isRemote)
    	{
    		if(this.phoneData != null)
    		{
    			CraftYourLifeRPMod.packetHandler.sendTo(new PacketSyncPhone(this.phoneData.getNumber()), (EntityPlayerMP)player);
    		}
    	}
    	else
    	{
			CraftYourLifeRPMod.packetHandler.sendToServer(new PacketSyncPhone(""));
    	}
    }
    
    public void syncMoney()
    {
    	if(!player.worldObj.isRemote)
    	{
    		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
    		if(this.phoneData != null)
    		{
    			CraftYourLifeRPMod.packetHandler.sendTo(PacketBitcoinPage.syncPlayerMoney(bitcoin,(float)extendedPlayer.serverData.money), (EntityPlayerMP)player);
    		}
    	}
    	else
    	{
			CraftYourLifeRPMod.packetHandler.sendToServer(PacketBitcoinPage.syncPlayerMoney(bitcoin,0));
    	}
    }
    
    public void sync() {
    	this.syncPhone();
    	this.syncShield();
    	this.syncMoney();
    	this.syncThirst();
    	this.syncVehiclesGui();
    	

    	
    	if(this.identityData != null)
    		this.identityData.syncData(player);
    }
    
    public void syncAlcol()
    {
    	if(!player.worldObj.isRemote)
    	{
    		CraftYourLifeRPMod.packetHandler.sendTo(PacketAlcol.syncAlcol(gAlcolInBlood),(EntityPlayerMP) player);
    	}
    }
    
    public void syncShouldBeReanimate()
    {
    	if(!player.worldObj.isRemote)
    	{
    		CraftYourLifeRPMod.packetHandler.sendTo(PacketSleeping.syncShouldBeReanimate(shouldBeReanimate),(EntityPlayerMP) player);
    	}
    }
    
    public void syncVehiclesGui()
    {
        if (!player.worldObj.isRemote) {
			System.out.println("/getplayerdata " + player.getCommandSenderName());
            EntityPlayerMP player1 = (EntityPlayerMP) player;
            FlansMod.getPacketHandler().sendTo(new PacketSync(this.vCoins, this.ownedSlot), player1);
        } else {
            FlansMod.getPacketHandler().sendToServer(new PacketSync(this.vCoins, this.ownedSlot));
        }
    }
    
    public CosmeticObject getCosmeticById(int id)
    {
    	for(CosmeticObject obj : cosmeticsData)
    	{
    		if(obj.getId() == id)
    		{
    			return obj;
    		}
    	}
    	return null;
    }
    
    public List<CosmeticObject> getEquippedCosmetics()
    {
    	return cosmeticsData.stream().filter(x -> x.getIsEquipped()).collect(Collectors.toList());
    }

	public void syncCosmetics() {
		PacketCosmetic packet = new PacketCosmetic((byte) 4);
		packet.cosmeticsToSynchronise = getEquippedCosmetics();
		packet.entityId = getPlayer().getEntityId();
		
		CraftYourLifeRPMod.packetHandler.sendTo(packet, (EntityPlayerMP)getPlayer());
	}
	
	public void displayCosmeticsGui()
	{
	    PacketCosmetic packet = new PacketCosmetic((byte)3);
	    packet.putList(this.cosmeticsData);
	    CraftYourLifeRPMod.packetHandler.sendTo(packet, (EntityPlayerMP)getPlayer());
	}
	
	public void forceSaveDriveable(EntityDriveable driveable)
	{
		if(driveable instanceof EntityVehicle)
		{
			EntityVehicle vehicle = (EntityVehicle) driveable;
			
			if(vehicle.getVehicleType().IsBoat())
			{
				for(ItemStack itemstack : ownedBoat)
				{
					if(vehicle.getVehicleType().getItem() == itemstack.getItem())
					{
						if(!itemstack.hasTagCompound())
						{
							itemstack.stackTagCompound = new NBTTagCompound();
						}
						driveable.writeToNBT(itemstack.getTagCompound());
						return;
					}
				}
			}
			else
			{
				for(ItemStack itemstack : ownedVehicle)
				{
					if(vehicle.getVehicleType().getItem() == itemstack.getItem())
					{
						if(!itemstack.hasTagCompound())
						{
							itemstack.stackTagCompound = new NBTTagCompound();
						}
						driveable.writeToNBT(itemstack.getTagCompound());
						return;
					}
				}
			}
		}
		else
		{
			EntityPlane plane = (EntityPlane) driveable;

			for(ItemStack itemstack : ownedPlane)
			{
				if(plane.getPlaneType().getItem() == itemstack.getItem())
				{
					if(!itemstack.hasTagCompound())
					{
						itemstack.stackTagCompound = new NBTTagCompound();
					}
					driveable.writeToNBT(itemstack.getTagCompound());
					return;
				}
			}
		}
	}
	
	public static MovingObjectPosition rayTraceServer(EntityPlayer player, double range, float partialTickTime)
    {
        Vec3 vec3 = getPositionServer(player, partialTickTime);
        Vec3 vec31 = getLookServer(player, partialTickTime);
        Vec3 vec32 = vec3.addVector(vec31.xCoord * range, vec31.yCoord * range, vec31.zCoord * range);
        return player.worldObj.func_147447_a(vec3, vec32, false, false, true);
    }
 
    public static Vec3 getLookServer(EntityPlayer player, float p_70676_1_)
    {
        float f1;
        float f2;
        float f3;
        float f4;
 
        if(p_70676_1_ == 1.0F)
        {
            f1 = MathHelper.cos(-player.rotationYaw * 0.017453292F - (float)Math.PI);
            f2 = MathHelper.sin(-player.rotationYaw * 0.017453292F - (float)Math.PI);
            f3 = -MathHelper.cos(-player.rotationPitch * 0.017453292F);
            f4 = MathHelper.sin(-player.rotationPitch * 0.017453292F);
            return Vec3.createVectorHelper(f2 * f3, f4, f1 * f3);
        }
        else
        {
            f1 = player.prevRotationPitch + (player.rotationPitch - player.prevRotationPitch) * p_70676_1_;
            f2 = player.prevRotationYaw + (player.rotationYaw - player.prevRotationYaw) * p_70676_1_;
            f3 = MathHelper.cos(-f2 * 0.017453292F - (float)Math.PI);
            f4 = MathHelper.sin(-f2 * 0.017453292F - (float)Math.PI);
            float f5 = -MathHelper.cos(-f1 * 0.017453292F);
            float f6 = MathHelper.sin(-f1 * 0.017453292F);
            return Vec3.createVectorHelper(f4 * f5, f6, f3 * f5);
        }
    }
 
    public static Vec3 getPositionServer(EntityPlayer player, float p_70666_1_)
    {
        if(p_70666_1_ == 1.0F)
        {
            return Vec3.createVectorHelper(player.posX, player.posY+ player.eyeHeight * 0.8, player.posZ);
        }
        else
        {
            double d0 = player.prevPosX + (player.posX - player.prevPosX) * p_70666_1_;
            double d1 = player.prevPosY + (player.posY - player.prevPosY) * p_70666_1_;
            double d2 = player.prevPosZ + (player.posZ - player.prevPosZ) * p_70666_1_;
            return Vec3.createVectorHelper(d0, d1, d2);
        }
    }
	

	public void updateRendererDatas() //permet de mettre à jour les données concernant seulement le rendu du joueur pour tout le monde
	{	
		updateRendererDatas(CraftYourLifeRPMod.entityTrackerHandler.getSyncDataOf(player));
	}
	
	public void updateRendererDatas(List<PacketBase> packets) //permet de mettre à jour les données concernant seulement le rendu du joueur pour tout le monde
	{	
		if(!player.worldObj.isRemote)
		{			
			CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(player, true, packets);
		}
	}
	
	public void onReanimationEnter()
	{
		reanimatingPlayername = null;
		setShouldBeReanimate(true);
		startTimeTimestamp = System.currentTimeMillis();
		
		System.out.println("/wanted remove " + player.getCommandSenderName());

		NetworkCallTransmitter activeCall = NetworkCallTransmitter.getByUsername(player.getCommandSenderName());
		if(activeCall != null)
		{
			activeCall.finishCall();
		}
		
		clearInventoryInReanimation();
	}
	
	public void onReanimated() //Used by reanimation system
	{		
		setShouldBeReanimate(false);
		currentAnimation = 0;
		setgAlcolInBlood(0F);
		player.clearActivePotions();
		player.extinguish();

		player.setHealth(10);
		player.getFoodStats().addStats(10, 5F);
		setProning(false);

		ExtendedPlayer.forceWakeupPlayer(player);
		
		shield.setShield(ShieldStats.initialShield);
		thirst.setThirst(ThirstStats.initialThirst);
		
		
		player.setPositionAndUpdate(1428, 71, -807);
	}
	
	public void onRespawn()
	{
		setShouldBeReanimate(false);
		currentAnimation = 0;
		setgAlcolInBlood(0F);
		player.extinguish();
		player.clearActivePotions();
		heal();

		setProning(false);
		ExtendedPlayer.forceWakeupPlayer(player);
		clearInventory();
	}
	
	public void heal()
	{
		player.setHealth(20);
		player.getFoodStats().addStats(20, 5F);
		shield.setShield(ShieldStats.initialShield);
		thirst.setThirst(ThirstStats.initialThirst);
	}
	
	public void clearInventory()
	{
		for(int i = 0; i < player.inventory.mainInventory.length; i++)
		{
			ItemStack is = player.inventory.mainInventory[i];
			
			if(is == null) continue;
			
			if(ReanimationHandler.itemsToNotRemove.contains(Item.getIdFromItem(is.getItem())))
			{
				continue;
			}
			
			player.inventory.mainInventory[i] = null;
		}
		
		for(int i = 0; i < player.inventory.armorInventory.length; i++)
		{
			ItemStack is = player.inventory.armorInventory[i];
			
			if(is == null) continue;
			
			player.inventory.armorInventory[i] = null;
		}
	}
	
	public void clearInventoryInReanimation()
	{
		for(int i = 0; i < player.inventory.mainInventory.length; i++)
		{
			ItemStack is = player.inventory.mainInventory[i];
			
			if(is == null) continue;
			
			if(ReanimationHandler.itemsToForceRemoveOnReanimation.contains(Item.getIdFromItem(is.getItem())))
			{
				player.inventory.mainInventory[i] = null;
			}
		}
	}
	
	public void setVcoins(int value) {
	    this.vCoins = value;
	    this.sync();
	}

	public int getVcoins() 
	{
	    this.sync();
	    return this.vCoins;
	}
	    
	public void setOwnedSlots(int value)
	{
	   this.ownedSlot = value;
	   this.sync();
	}
	    
	public int getOwnedSlots() 
	{
		return this.ownedSlot;
	}
	    
	public boolean vehicleStolen(int vehicleId)
	{
		for(int i = 0; i < stolenVehiclesId.size(); i++)
		{
			int id = stolenVehiclesId.get(i);
	    	if(vehicleId == id)
	    	{
	    		return true;
	    	}
	    }
	    return false;
	}
	
	public boolean vehicleExploded(int vehicleId)
	{
		return explodedVehicles.containsKey(vehicleId);
	}
	    
	public void saveOwnedVehicle(ItemStack is)
	{
		if(!player.worldObj.isRemote)
		{
			if(is.getItem() instanceof ItemPlane)
	    	{
				this.ownedPlane.add(is);
	    	}
	    	else
	    	{
	    		ItemVehicle item = (ItemVehicle)is.getItem();
	    		if(item.type.IsBoat())
	    		{
	        		this.ownedBoat.add(is);
	    		}
	    		else
	    		{
	    			this.ownedVehicle.add(is);
	    		}
	    	}
	    }
	 }
	    
	 public ItemStack getSavedVehicle(int itemId)
	 {
	    for(ItemStack it : this.ownedVehicle)
	    {
	    	if(it.getItem().getIdFromItem(it.getItem()) == itemId)
	    	{
	    		return it;
	    	}
	    }
	    for(ItemStack it : this.ownedPlane)
	    {
	    	if(it.getItem().getIdFromItem(it.getItem()) == itemId)
	    	{
	    		return it;
	    	}
	    }
	    for(ItemStack it : this.ownedBoat)
	    {
	    	if(it.getItem().getIdFromItem(it.getItem()) == itemId)
	    	{
	    		return it;
	    	}
	    }
	    return null;
	 }
	 
	    
	 public boolean vehicleIsImpound(int vehicleId)
	 {
		 for(Penalty penalty : PenaltyManager.getImpoundVehicles())
	     {
	    	VehiclePenalty vehiclePenalty = (VehiclePenalty)penalty;
	    	if(vehiclePenalty.getVehicleId() == vehicleId)
	    	{
	    		return true;
	    	}
	    }
	    return false;
	 }
	 
	 public void setgAlcolInBlood(float gAlcolInBlood)
	 {
	    	if(!player.worldObj.isRemote)
	    	{
	    		this.gAlcolInBlood = MathsUtils.Clamp(gAlcolInBlood, 0.0F, 4.0F);

	    		syncAlcol();
	    		
	    	}
	    	else
	    	{
	    		this.gAlcolInBlood = gAlcolInBlood;
	    	}
	 }
	 
	public void setShouldBeReanimate(boolean shouldBeReanimate)
	{
		this.shouldBeReanimate = shouldBeReanimate;
		if(!player.worldObj.isRemote)
		{
			syncShouldBeReanimate();
		}
	}
	
	public boolean getShouldBeReanimate()
	{
		return shouldBeReanimate;
	}
	 
	public float getgAlcolInBlood()
	{
		return this.gAlcolInBlood;
	}
	 
	public boolean shouldBeInEthylicComa()
	{
		return this.gAlcolInBlood >= 4f;
	}
	 
	 public static boolean forcePlayerSleep(EntityPlayer player, int x, int y, int z, boolean serverForce)
	 {
		 World world = player.worldObj;
	     if (world.isRemote)
	     {
	    	 return false;
	     }
		 
         int i1 = world.getBlockMetadata(x, y, z);

		 if (!BlockBed.isBlockHeadOfBed(i1))
         {
             int j1 = BlockBed.getDirection(i1);
             x += BlockBed.field_149981_a[j1][0];
             z += BlockBed.field_149981_a[j1][1];

             if (!(world.getBlock(x, y, z) instanceof BlockBed))
             {
                 return false;
             }

             i1 = world.getBlockMetadata(x, y, z);             
         }
		 
		 if(serverForce) player.setPositionAndUpdate(x, y, z);
         EntityPlayer.EnumStatus enumstatus = sleepPlayerAt(player,x, y, z, serverForce);

         if (enumstatus == EntityPlayer.EnumStatus.OK)
         {
        	 CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(player, true, PacketSleeping.syncSleeping(player.getEntityId(), x, y, z, BlockBed.getDirection(i1)));
             return true;
         }
         else if(enumstatus == EntityPlayer.EnumStatus.TOO_FAR_AWAY)
         {
        	 ServerUtils.sendChatMessage(player, "§cApprochez-vous du lit pour interagir avec.");
        	 return false;
         }
         return false;
	 }
	 
	 @SideOnly(Side.CLIENT)
	 public static EnumStatus sleepPlayerAt(EntityPlayer player, int x, int y, int z, int bedDirection)
	 {		   
		 ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
	     World world = player.worldObj;
		    		 
		 if (player.isRiding())
		 {
			 player.mountEntity((Entity)null);
		 }
		 else if(extendedPlayer.isProning())
		 {
			 extendedPlayer.setProning(false);
		 }
		        
		 MinecraftUtils.setPlayerSize(player, 0.2F, 0.2F);

		 if (player.worldObj.blockExists(x, y, z))
		 {

			 int l = bedDirection;
		     float f1 = 0.5F;
		     float f = 0.5F;

		     switch (l)
		     {
		     	case 0:
		     		f = 0.9F;
		            break;
		        case 1:
		        	f1 = 0.1F;
		            break;
		        case 2:
		        	f = 0.1F;
		            break;
		        case 3: 
		        	f1 = 0.9F;
		      }

			  ExtendedPlayer.setBedTranslation(player, l);
			  
			  extendedPlayer.sleepX = (double)((float)x + f1);
			  extendedPlayer.sleepY = (double)((float)y + 0.6F);
			  extendedPlayer.sleepZ = (double)((float)z + f);

		  }
		  else
		  {
			  extendedPlayer.sleepX = (double)((float)x + 0.5F);
			  extendedPlayer.sleepY = (double)((float)y + 0.6);
			  extendedPlayer.sleepZ = (double)((float)z + 0.5F);
		  }

		  extendedPlayer.sleepingTime = 0;
		  extendedPlayer.setSleeping(true);
		  
		  player.motionX = player.motionZ = player.motionY = 0.0D;
		  player.playerLocation = new ChunkCoordinates(x, y, z);
		  ExtendedPlayer.setBedOccupied(world, x, y, z, true);
		  
		  player.rotationYaw = MinecraftUtils.getPlayerYawFromBedDirection(player);
		  player.rotationYawHead = MinecraftUtils.getPlayerYawFromBedDirection(player);
		  player.rotationPitch = 0F;
		  player.cameraYaw = 0F;

		  Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
		  

		  return EntityPlayer.EnumStatus.OK;
		}
	 
	 public static EnumStatus sleepPlayerAt(EntityPlayer player, int x, int y, int z , boolean serverForce)
	 {		   
		 ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
	     World world = player.worldObj;
		 if (!world.isRemote)
		 {
			 if (world.provider.canRespawnHere() && world.getBiomeGenForCoords(x, z) != BiomeGenBase.hell)
		     {
				 if (ExtendedPlayer.isBedOccupied(world, x, y, z))
			     {
					 EntityPlayer entityplayer1 = null;
			         Iterator iterator = world.playerEntities.iterator();
	
			         while (iterator.hasNext())
			         {
			        	 EntityPlayer entityplayer2 = (EntityPlayer)iterator.next();
			             ExtendedPlayer extendedPlayer2 = ExtendedPlayer.get(entityplayer2);
	
			             if (extendedPlayer2.isSleeping())
			             {
			            	 ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;
			            	 
			            	 if(chunkcoordinates == null) continue;
			            	 
			                 if (chunkcoordinates.posX == x && chunkcoordinates.posY == y && chunkcoordinates.posZ == z)
			                 {
			                	 entityplayer1 = entityplayer2;
			                 }
			              }
			          }
	
			          if (entityplayer1 != null)
			          {
			        	  return EntityPlayer.EnumStatus.NOT_POSSIBLE_NOW;
			          }
	
			          ExtendedPlayer.setBedOccupied(world, x, y, z, false);
			      }
			}
		    	    
		    	   
		    if (extendedPlayer.isSleeping() || !player.isEntityAlive()) 
		    {
		    	return EntityPlayer.EnumStatus.OTHER_PROBLEM;
		    }

		    if (!player.worldObj.provider.isSurfaceWorld()) 
		    {
		    	return EntityPlayer.EnumStatus.NOT_POSSIBLE_HERE;
		    }


		    if(!serverForce)
		    {
			    if (Math.abs(player.posX - (double)x) > 5.0D || Math.abs(player.posY - (double)y) > 3.0D || Math.abs(player.posZ - (double)z) > 5.0D) //distance
			    {
			    	return EntityPlayer.EnumStatus.TOO_FAR_AWAY;
			    }
		    }
		 }
		      		 
		 if (player.isRiding())
		 {
			 player.mountEntity((Entity)null);
		 }
		 else if(extendedPlayer.isProning())
		 {
			 extendedPlayer.setProning(false);
		 }
		        
		 MinecraftUtils.setPlayerSize(player, 0.2F, 0.2F);

		 if (player.worldObj.blockExists(x, y, z))
		 {

			 int l = player.worldObj.getBlock(x, y, z).getBedDirection(player.worldObj, x, y, z);
		     float f1 = 0.5F;
		     float f = 0.5F;

		     switch (l)
		     {
		     	case 0:
		     		f = 0.9F;
		            break;
		        case 1:
		        	f1 = 0.1F;
		            break;
		        case 2:
		        	f = 0.1F;
		            break;
		        case 3: 
		        	f1 = 0.9F;
		      }

			  ExtendedPlayer.setBedTranslation(player, l);
			  
			  extendedPlayer.sleepX = (double)((float)x + f1);
			  extendedPlayer.sleepY = (double)((float)y + 0.6F);
			  extendedPlayer.sleepZ = (double)((float)z + f);

		  }
		  else
		  {
			  extendedPlayer.sleepX = (double)((float)x + 0.5F);
			  extendedPlayer.sleepY = (double)((float)y + 0.6);
			  extendedPlayer.sleepZ = (double)((float)z + 0.5F);
		  }

		  extendedPlayer.sleepingTime = 0;
		  extendedPlayer.setSleeping(true);
		  
		  player.motionX = player.motionZ = player.motionY = 0.0D;
		  player.playerLocation = new ChunkCoordinates(x, y, z);
		  ExtendedPlayer.setBedOccupied(world, x, y, z, true);
		  
		  player.rotationYaw = MinecraftUtils.getPlayerYawFromBedDirection(player);
		  player.rotationYawHead = MinecraftUtils.getPlayerYawFromBedDirection(player);
		  player.rotationPitch = 0F;
		  player.cameraYaw = 0F;
		  
		  if(player.worldObj.isRemote)
		  {
			  Minecraft.getMinecraft().gameSettings.thirdPersonView = 0;
		  }

		  return EntityPlayer.EnumStatus.OK;
		}
	 
	 	public static boolean isBedOccupied(World world, int x, int y, int z)
	 	{
	 		int meta = world.getBlockMetadata(x, y, z);
	 		
	 	    return (meta & 4) != 0;
	 	}
	 	
	    public static void setBedOccupied(World world, int x, int y, int z, boolean state)
	    {
	        int l = world.getBlockMetadata(x, y, z);

	        if (state)
	        {
	            l |= 4;
	        }
	        else
	        {
	            l &= -5;
	        }

	        world.setBlockMetadataWithNotify(x, y, z, l, 4);
	    }
	 
	 	public static void forceWakeupPlayer(EntityPlayer player)
	 	{
	 		wakeUpPlayer(player);
			CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(player, true, PacketSleeping.unSleep(player.getEntityId()));
	 	}
	 
	 	public static boolean wakeUpPlayer(EntityPlayer player)
	    {
	        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
	        if(!CraftYourLifeRPMod.reanimationHandler.canWakeupFromBed(player))
	        {
	        	if(!player.worldObj.isRemote) ServerUtils.sendMessage("§cVous êtes en réanimation!", player, 100, 0);
	        	return false;
	        }
	        
	 		MinecraftUtils.resetPlayerSize(player);
	        ChunkCoordinates chunkcoordinates = player.playerLocation;
	        ChunkCoordinates chunkcoordinates1 = player.playerLocation;
	        Block block = (chunkcoordinates == null ? null : player.worldObj.getBlock(chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ));

	        if (chunkcoordinates != null && block.isBed(player.worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, player))
	        {
	            ExtendedPlayer.setBedOccupied(player.worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, false);
	            chunkcoordinates1 = block.getBedSpawnPosition(player.worldObj, chunkcoordinates.posX, chunkcoordinates.posY, chunkcoordinates.posZ, player);

	            if (chunkcoordinates1 == null)
	            {
	                chunkcoordinates1 = new ChunkCoordinates(chunkcoordinates.posX, chunkcoordinates.posY + 1, chunkcoordinates.posZ);
	            }

	            player.setPosition((double)((float)chunkcoordinates1.posX + 0.5F), (double)((float)chunkcoordinates1.posY + player.yOffset + 0.1F), (double)((float)chunkcoordinates1.posZ + 0.5F));
	        }

	        extendedPlayer.sleepingTime = 0;
	        extendedPlayer.setSleeping(false);
	        return true;
	    }
	 	
	    public static void setBedTranslation(EntityPlayer player, int metadata)
	    {
	    	player.field_71079_bU = 0.0F;
	    	player.field_71089_bV = 0.0F;

	        switch (metadata)
	        {
	            case 0:
	            	player.field_71089_bV = -1.8F;
	                break;
	            case 1:
	            	player.field_71079_bU = 1.8F;
	                break;
	            case 2:
	            	player.field_71089_bV = 1.8F;
	                break;
	            case 3:
	            	player.field_71079_bU = -1.8F;
	        }
	    }
	 	
	    public static boolean isInBed(EntityPlayer player)
	    {
	    	ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
	    	int sleepX = (int) Math.floor(extendedPlayer.sleepX);
	    	int sleepY = (int) Math.floor(extendedPlayer.sleepY);
	    	int sleepZ = (int) Math.floor(extendedPlayer.sleepZ);

	        return player.worldObj.getBlock(sleepX, sleepY, sleepZ) == Blocks.bed;
	    }
	 
	 
		public void setSleeping(boolean sleep)
		{
			 this.isSleeping = sleep;
		}
		 
		public boolean isSleeping()
		{
			return this.isSleeping;
		}
		
		 public boolean isProning()
		 {
			 return proning;
		 }
		 
		 public void setProning(boolean proning)
		 {
			 this.proning = proning;
			 
			 if(proning)
			 {
				 MinecraftUtils.setPlayerSize(player, 0.8F, 0.6F);
			 }
			 else
			 { 
				 MinecraftUtils.resetPlayerSize(player);
			 }
			 
			 if(!player.worldObj.isRemote)
			 {
				 if(proning)
				 {
					 player.eyeHeight = 0.6F;
				 }
				 else
				 {
					 player.eyeHeight = player.getDefaultEyeHeight();
				 }
				 

				CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(player, false, PacketProning.syncPlayerState(player, isProning()));
			 }
		 }
		 
		public void syncData()
		{
			if(player.worldObj.isRemote)
			{
				CraftYourLifeRPMod.packetHandler.sendToServer(PacketSyncPlayerData.askDataToServer());
			}
			else
			{
				if(serverData != null)
				{
					CraftYourLifeRPMod.packetHandler.sendTo(PacketSyncPlayerData.sendDataToClient(this), (EntityPlayerMP) player);
				}
			}
		}
		
		public void updatePlayerData()
		{
			System.out.println("/getplayerdata " + player.getCommandSenderName());
		}
	 
		public WebPageData openPage(byte pageId)
		{
			switch(pageId)
			{
				case 1:
				{
					currentPageData = new BitcoinConverterPage(player);
					break;
				}
				case 2:
				{
					currentPageData = new BMGPage(player);
					break;
				}
			}
			
			currentPageData.initPage();
			
			return currentPageData;
		}
		
		public boolean isUsingItem()
		{
			return usedItem != null;
		}
		
		 public void setItemReleased()
		 {
			 usedItem = null;
			 itemPressTicks = 0;
			 
			 CraftYourLifeRPMod.entityTrackerHandler.syncPlayerToPlayers(player,false, PacketCustomInterract.syncUsingItem(player));
		 }
		 
		 public void setNotStealing()
		 {
				
				if(!getPlayer().worldObj.isRemote) 
				{
					TileEntity tile = (TileEntity) stealingTile;
	
					CraftYourLifeRPMod.packetHandler.sendTo(PacketStealing.notificateClient(tile.xCoord, tile.yCoord, tile.zCoord), (EntityPlayerMP)player);
				}

				
				stealingTile.resetStealing();
				stealingTile = null;
		 }
			
		 public boolean isStealing()
		 {
		 	 return stealingTile != null;
		 }
		 
		 public WebPageData openPage()
		 {
			 return currentPageData;
		 }
		 
		public void newRegionBuilder(String regionName)
		{
			regionBuilder = new WorldSelector(player.worldObj);
			regionBuilder.setName(regionName);
		}
		
		public void migrateCacheData(ExtendedPlayer to)
		{
			to.passwordReceived = passwordReceived;
			to.serverData = serverData;
			to.capturingRegions = capturingRegions;
		}
		
		public List<WorldSelector> getCapturingRegions()
		{
			return capturingRegions;
		}
		
		public void setCallbackCheckIfDoctor(Object object)
		{
			callbackCheckIfDoctor = object;
		}
		
		public Object getCallbackCheckIfDoctor()
		{
			return callbackCheckIfDoctor;
		} 
		


}
