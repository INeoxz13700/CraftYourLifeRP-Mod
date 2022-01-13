package fr.craftyourliferp.data;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.swing.text.html.HTML.Tag;

import com.flansmod.common.driveables.DriveableManager;
import com.google.gson.Gson;
import com.google.gson.JsonSerializer;

import fr.craftyourliferp.capture.CaptureProcess;
import fr.craftyourliferp.items.MarketItem;
import fr.craftyourliferp.main.BlocksBackup;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ItemStackProbability;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.network.PacketSendSms;
import fr.craftyourliferp.utils.HTTPUtils;
import fr.craftyourliferp.utils.ServerUtils;
import fr.craftyourliferp.utils.StringUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.Vec3;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.WorldSavedData;
import net.minecraft.world.storage.MapStorage;

public class WorldData extends WorldSavedData {
		
	private static final String IDENTIFIER = "CYLWorldData";
	
	private List<SmsData> smsToSends = new ArrayList();
	
	private ItemStackProbability itemsProbability = new ItemStackProbability();
	
	private List<WorldSelector> explosionRegion = new ArrayList();
	private List<WorldSelector> fireRegion = new ArrayList();
	public HashMap<WorldSelector, CaptureProcess> capturesProcess = new HashMap<WorldSelector, CaptureProcess>();

	private BlackMarketData marketData = new BlackMarketData();
	
	private float bitcoinInEuro = 1.0f;
	
	private boolean marketUpdated = false;
	
	private World worldObj;
	
	public WorldSelector thieftAreaSelector;
	
	private BlocksBackup worldBlocksBackup = new BlocksBackup();
	
	public DriveableManager driveablesManager = new DriveableManager();
	
	public long lastFireInteractionTime;
	public long lastFireThunderTime;
	public long lastFireNaturalTime;
	public long lastFireMolotovTime;
	


	
	public WorldData(String identifier) {
		super(identifier);

	}
	public WorldData(){
		super(IDENTIFIER);
	}
	
	@Override
	public void readFromNBT(NBTTagCompound nbt) 
	{
		NBTTagList nbtList = (NBTTagList) nbt.getTag("SmsData");
		for(int i = 0; i < nbtList.tagCount(); i++)
		{
			NBTTagCompound compound = nbtList.getCompoundTagAt(i);
			SmsData data = new SmsData(0, compound.getString("SenderNumber"), compound.getString("ReceiverNumber"), compound.getString("Message"), false);
			smsToSends.add(data);
		}
		
		NBTTagCompound tag = (NBTTagCompound) nbt.getTag("GeneralData");
		bitcoinInEuro = tag.getFloat("BitcoinInEuro");
		marketUpdated = tag.getBoolean("MarketUpdated");
		
		lastFireInteractionTime = tag.getLong("LastFireInteractionTime");
		lastFireThunderTime = tag.getLong("LastFireThunderTime");
		lastFireNaturalTime = tag.getLong("LastFireNaturalTime");
		lastFireMolotovTime = tag.getLong("lastFireMolotovTime");


		
		thieftAreaSelector = new WorldSelector(worldObj); 
		thieftAreaSelector.readFromNbt("VehicleThiefArea", nbt);
		
		if(marketData != null) marketData.readFromNBT(nbt);
		
		if(itemsProbability != null) itemsProbability.readFromNBT("BlackMarketProbability", nbt);
		
		NBTTagList tagsList = (NBTTagList) nbt.getTag("ExplosionRegions");
		if(tagsList != null)
		{
			for(int i = 0; i < tagsList.tagCount(); i++)
			{
				NBTTagCompound compound = tagsList.getCompoundTagAt(i);
				WorldSelector selector = new WorldSelector(worldObj);
				selector.readFromNbt(compound);
				explosionRegion.add(selector);
			}
		}
		
		tagsList = (NBTTagList) nbt.getTag("FireRegions");
		if(tagsList != null)
		{
			for(int i = 0; i < tagsList.tagCount(); i++)
			{
				NBTTagCompound compound = tagsList.getCompoundTagAt(i);
				WorldSelector selector = new WorldSelector(worldObj);
				selector.readFromNbt(compound);
				fireRegion.add(selector);
			}
		}
		
		tagsList = (NBTTagList) nbt.getTag("Captures");
		if(tagsList != null)
		{
			for(int i = 0; i < tagsList.tagCount(); i++)
			{
				NBTTagCompound compound = tagsList.getCompoundTagAt(i);
				WorldSelector selector = new WorldSelector(worldObj);
				selector.readFromNbt(compound);
				System.out.println("ici");
				CaptureProcess process = CraftYourLifeRPMod.captureHander.getCaptureProcessFromId(compound.getInteger("CaptureType"));
				process.readFromNbt(compound);
				capturesProcess.put(selector, process);
			}
		}

		worldBlocksBackup.readFromNbt(nbt);
	}

	@Override
	public void writeToNBT(NBTTagCompound nbt) {
		NBTTagList nbtList = new NBTTagList();
		
		for(SmsData data : smsToSends)
		{
			NBTTagCompound compound = new NBTTagCompound();
			compound.setString("SenderNumber", data.senderNumber);
			compound.setString("ReceiverNumber", data.receiverNumber);
			compound.setString("Message", data.message);
			nbtList.appendTag(compound);
		}
		
		nbt.setTag("SmsData", nbtList);
		
		NBTTagCompound generalData = new NBTTagCompound();
		
		generalData.setFloat("BitcoinInEuro", bitcoinInEuro);
		generalData.setBoolean("MarketUpdated", marketUpdated);
		generalData.setLong("LastFireInteractionTime", lastFireInteractionTime);
		generalData.setLong("LastFireThunderTime",lastFireThunderTime);
		generalData.setLong("LastFireNaturalTime",lastFireNaturalTime);
		generalData.setLong("lastFireMolotovTime",lastFireMolotovTime);

		
		nbt.setTag("GeneralData", generalData);
		
		if(marketData != null) marketData.writeToNBT(nbt);
		
		if(itemsProbability != null) itemsProbability.writeToNBT("BlackMarketProbability", nbt);
		
		if(thieftAreaSelector.selectionDefinied()) thieftAreaSelector.writeToNbt("VehicleThiefArea", nbt);

		NBTTagList tagsList = new NBTTagList();
		for(WorldSelector region : explosionRegion)
		{
			if(region.selectionDefinied())
			{
				NBTTagCompound regionTag = new NBTTagCompound();
				region.writeToNbt(regionTag);
				tagsList.appendTag(regionTag);
			}
		}
		nbt.setTag("ExplosionRegions", tagsList);
		
		tagsList = new NBTTagList();
		for(WorldSelector region : fireRegion)
		{
			if(region.selectionDefinied())
			{
				NBTTagCompound regionTag = new NBTTagCompound();
				region.writeToNbt(regionTag);
				tagsList.appendTag(regionTag);
			}
		}
		nbt.setTag("FireRegions", tagsList);
		
		tagsList = new NBTTagList();
		for (Map.Entry<WorldSelector, CaptureProcess> entry : capturesProcess.entrySet()) {
			WorldSelector key = entry.getKey();
			CaptureProcess value = entry.getValue();
			
			NBTTagCompound compound = new NBTTagCompound();
			key.writeToNbt(compound);
			value.writeToNbt(compound);
			
			tagsList.appendTag(compound);
		}	
		nbt.setTag("Captures", tagsList);
		
		
		worldBlocksBackup.writeToNbt(nbt);
	}
	
	public boolean addItemToBlackMarket(String id, float priceInEuro, int probabilityToSpawn)
	{
		return addItemToBlackMarket(id,priceInEuro, probabilityToSpawn, null);
	}
	
	public boolean addItemToBlackMarket(String id, float priceInEuro, int probabilityToSpawn, String displayName)
	{
		if(itemsProbability != null)
		{
			boolean success = itemsProbability.addItem(id, 1, priceInEuro, probabilityToSpawn,displayName);
			if(success)
			{
				markDirty();
				return success;
			}
			return false;
		}
		return false;
	}
	
	public boolean removeItemFromBlackMarket(String id, int probabilityToSpawn)
	{
		if(itemsProbability != null)
		{
			boolean success = itemsProbability.remvoveItem(id, probabilityToSpawn);
			if(success)
			{
				markDirty();
				return success;
			}
			return false;
		}
		return false;
	}
	
	public void addSmsToSend(SmsData data)
	{
		smsToSends.add(data);
		markDirty();
	}

	
	public void updateBitcoinPrice() 
	{
		String data = HTTPUtils.doPostHttps("https://blockchain.info/ticker", "");

		int index = data.indexOf("EUR");
		
		if(index == -1) return;
		
		String[] datas = StringUtils.extractString(data, '{', '}', index).replace("{", "").replace("}", "").split(", ");
		
		bitcoinInEuro = Float.parseFloat(datas[1].split(":")[1].replace(" ", ""));
		markDirty();
	}
	

	public ItemStackProbability getItemStackProbability()
	{
		return itemsProbability;
	}
	
	public void setMarketUpdated(boolean state)
	{
		marketUpdated = state;
		markDirty();
	}
	
	/**
	 * 1 natural
	 * 2 thunder
	 * 3 molotov
	 **/
	public void setLastFireTime(long time,int type)
	{
		if(type == 1)
		{
			lastFireNaturalTime = time;
		}
		else if(type == 2)
		{
			lastFireThunderTime = time;
		}
		else if(type == 3)
		{
			lastFireMolotovTime = time;
		}
		markDirty();
	}
	
	public boolean getMarketUpdated()
	{
		return marketUpdated;
	}
	
	public BlackMarketData getMarketData()
	{
		return marketData;
	}
	
	public void updateMarket()
	{
		marketData.clear();
		
		int addedItem = 0;
		
		int attemptCount = 1000;
		
		while(addedItem <= 4)
		{
			if(addedItem == itemsProbability.getRegisteredItemCount()) break;

			MarketItem item = itemsProbability.getRandomDropItem();	
			
			if(item == null) continue;
			
			if(marketData.addItem(item)) addedItem++;
				

			if(attemptCount-- <= 0) break;
		}
		
		markDirty();
	}
	
	public float getBitcoinPrice() 
	{
		return bitcoinInEuro;
	}
	
	public void distribuateSms()
	{
		List<SmsData> toRemove = new ArrayList();
		
		for(SmsData data : smsToSends)
		{
			try {
				String username = NumberData.getUsernameByNumber(data.receiverNumber);
				if(username == null) continue;
				
				EntityPlayer receiverPlayer = worldObj.getPlayerEntityByName(username);
								
				if(receiverPlayer == null) continue;
				
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketSendSms(data.message,data.receiverNumber,data.senderNumber), (EntityPlayerMP)receiverPlayer);
				
				toRemove.add(data);
				
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
		
		smsToSends.removeAll(toRemove);
	}
	
	public void setThiefAreaPos1(Vec3 pos)
	{
		thieftAreaSelector.setSelectedPos1(pos);
		markDirty();
	}
	
	public void setThiefAreaPos2(Vec3 pos)
	{
		thieftAreaSelector.setSelectedPos2(pos);
		markDirty();
	}
	
	public void resetSelection()
	{
		thieftAreaSelector.resetSelection();
		markDirty();
	}
	
	public void addFireRegion(WorldSelector regionBuilder)
	{		
		fireRegion.add(regionBuilder);
				
		markDirty();		
	}
	
	public void addExplosionsRegion(WorldSelector regionBuilder)
	{		
		explosionRegion.add(regionBuilder);
				
		markDirty();		
	}
	
	public void addCaptureRegion(EntityPlayer player, int captureType, WorldSelector regionBuilder)
	{		
		CaptureProcess process = CraftYourLifeRPMod.captureHander.getCaptureProcessFromId(captureType);
		if(process == null)
		{
			ServerUtils.sendChatMessage(player, "§cType de capture inexistant!");
			return;
		}
		capturesProcess.put(regionBuilder, process);
		markDirty();		
	}
	
	public boolean removeExplosionsRegion(String rgname)
	{		
		
		WorldSelector region = getExplosionRegionByName(rgname);
		
		if(region == null) return false;
		
		explosionRegion.remove(region);
		
		markDirty();	
		
		return true;
	}
	
	public boolean removeFireRegion(String rgname)
	{		
		
		WorldSelector region = getFireRegionByName(rgname);
		
		if(region == null) return false;
		
		fireRegion.remove(region);
		
		markDirty();	
		
		return true;
	}
	
	public boolean removeCaptureRegion(String rgname)
	{		
		
		WorldSelector region = getCaptureRegionByName(rgname);
		
		if(region == null) return false;
		
		capturesProcess.remove(region);
		
		markDirty();	
		
		return true;
	}
	
	public WorldSelector getCaptureRegionByName(String rgname)
	{
		Iterator<WorldSelector> iterator = capturesProcess.keySet().iterator();
		
		while(iterator.hasNext())
		{
			WorldSelector region = iterator.next();
			
			if(region.getName().equalsIgnoreCase(rgname)) return region;
		}
		return null;
	}
		
	private WorldSelector getExplosionRegionByName(String rgname)
	{
		for(int i = 0; i < explosionRegion.size(); i++)
		{
			WorldSelector region = explosionRegion.get(i);
			
			if(region.getName().equalsIgnoreCase(rgname)) return region;
		}
		return null;
	}
	
	private WorldSelector getFireRegionByName(String rgname)
	{
		for(int i = 0; i < fireRegion.size(); i++)
		{
			WorldSelector region = fireRegion.get(i);
			
			if(region.getName().equalsIgnoreCase(rgname)) return region;
		}
		return null;
	}
	
	private boolean containsRegion(String rgname)
	{
		for(int i = 0; i < explosionRegion.size(); i++)
		{
			WorldSelector region = explosionRegion.get(i);
			
			if(region.getName().equalsIgnoreCase(rgname)) return true;
		}
		
		return false;
	}
	
	public boolean canExplosionDamage(Explosion explosion)
	{
		for(int i = 0; i < explosionRegion.size(); i++)
		{
			WorldSelector region = explosionRegion.get(i);
			if(region.insideSelection(Vec3.createVectorHelper(explosion.explosionX, explosion.explosionY, explosion.explosionZ))) return true;
		}
		return false;
	}
	
	public WorldSelector getRegionForExplosion(Explosion explosion)
	{
		for(int i = 0; i < explosionRegion.size(); i++)
		{
			WorldSelector region = explosionRegion.get(i);
			if(region.insideSelection(Vec3.createVectorHelper(explosion.explosionX, explosion.explosionY, explosion.explosionZ))) return region;
		}
		return null;
	}
	
	public List<WorldSelector> getExplosions()
	{
		return explosionRegion;
	}
	
	public List<WorldSelector> getFires()
	{
		return fireRegion;
	}
	
	public HashMap<WorldSelector, CaptureProcess> getCaptures()
	{
		return capturesProcess;
	}
	
	public BlocksBackup getBlocksBackup()
	{
		return worldBlocksBackup;
	}
	
	public static WorldData get(World world)
	{		
		WorldData data = (WorldData) world.loadItemData(WorldData.class, IDENTIFIER);
		
		if (data == null) {
			data = new WorldData();
			data.worldObj = world;
			data.marketData = new BlackMarketData();
			data.itemsProbability = new ItemStackProbability();
			data.thieftAreaSelector = new WorldSelector(world);
			world.setItemData(IDENTIFIER, data);
		}
		else
		{
			data.worldObj = world;
		}
		return data;
	}
	
	
}
