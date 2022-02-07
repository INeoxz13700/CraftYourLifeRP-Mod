package fr.craftyourliferp.game.events;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import javax.swing.JOptionPane;
import javax.xml.transform.Source;

import org.apache.commons.lang3.ArrayUtils;
import org.apache.logging.log4j.core.net.Priority;
import org.lwjgl.input.Keyboard;
import org.lwjgl.opengl.GL11;

import com.flansmod.common.driveables.DriveablePart;
import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityPlane;
import com.flansmod.common.driveables.EntitySeat;
import com.flansmod.common.driveables.EntityVehicle;
import com.flansmod.common.driveables.EnumDriveablePart;
import com.google.gson.JsonSyntaxException;
import com.mojang.authlib.minecraft.MinecraftProfileTexture.Type;

import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.Loader;
import cpw.mods.fml.common.ObfuscationReflectionHelper;
import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.InputEvent;
import cpw.mods.fml.common.gameevent.InputEvent.KeyInputEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedInEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerLoggedOutEvent;
import cpw.mods.fml.common.gameevent.PlayerEvent.PlayerRespawnEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ClientTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.ByteBufUtils;
import cpw.mods.fml.common.network.FMLNetworkEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ClientDisconnectionFromServerEvent;
import cpw.mods.fml.common.network.FMLNetworkEvent.ServerCustomPacketEvent;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import eu.nicoszpako.armamania.common.ItemCigarette;
import fr.craftyourliferp.blocks.BlockNuclear;
import fr.craftyourliferp.blocks.BlockNuclearOre;
import fr.craftyourliferp.blocks.BlockPainting;
import fr.craftyourliferp.blocks.tileentity.IStealingTileEntity;
import fr.craftyourliferp.blocks.tileentity.TileEntityPainting;
import fr.craftyourliferp.company.CompanyManager;
import fr.craftyourliferp.company.CompanyUser;
import fr.craftyourliferp.data.AvatarUpdater;
import fr.craftyourliferp.data.BlockData;
import fr.craftyourliferp.data.NumberData;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.entities.EntityFootballBall;
import fr.craftyourliferp.entities.EntityLootableBody;
import fr.craftyourliferp.entities.EntityStopStick;
import fr.craftyourliferp.ingame.gui.CylrpMessageHUD;
import fr.craftyourliferp.ingame.gui.GuiAnimation;
import fr.craftyourliferp.ingame.gui.GuiBase;
import fr.craftyourliferp.ingame.gui.GuiCosmetics;
import fr.craftyourliferp.ingame.gui.GuiIdentityCard;
import fr.craftyourliferp.ingame.gui.GuiPedagogical;
import fr.craftyourliferp.ingame.gui.GuiPhone;
import fr.craftyourliferp.items.CardIdentity;
import fr.craftyourliferp.items.IItemPress;
import fr.craftyourliferp.items.ItemBulletproofShield;
import fr.craftyourliferp.items.ItemVestBullet;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.items.PaintingItem;
import fr.craftyourliferp.main.BlocksBackup;
import fr.craftyourliferp.main.ClientProxy;
import fr.craftyourliferp.main.CommonProxy;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.network.PacketAnimation;
import fr.craftyourliferp.network.PacketAtm;
import fr.craftyourliferp.network.PacketAuthentification;
import fr.craftyourliferp.network.PacketBase;
import fr.craftyourliferp.network.PacketCosmetic;
import fr.craftyourliferp.network.PacketCustomInterract;
import fr.craftyourliferp.network.PacketExtinguisher;
import fr.craftyourliferp.network.PacketOpenCardIdentity;
import fr.craftyourliferp.network.PacketOpenCharacterGui;
import fr.craftyourliferp.network.PacketOpenTelephone;
import fr.craftyourliferp.network.PacketProning;
import fr.craftyourliferp.network.PacketSleeping;
import fr.craftyourliferp.network.PacketStealing;
import fr.craftyourliferp.network.PacketThirst;
import fr.craftyourliferp.phone.CallHandler;
import fr.craftyourliferp.phone.NetworkCallTransmitter;
import fr.craftyourliferp.shield.ShieldStats;
import fr.craftyourliferp.thirst.ThirstStats;
import fr.craftyourliferp.utils.GuiUtils;
import fr.craftyourliferp.utils.MathsUtils;
import fr.craftyourliferp.utils.MinecraftUtils;
import fr.craftyourliferp.utils.ServerUtils;
import ibxm.Player;
import net.minecraft.block.Block;
import net.minecraft.block.BlockAnvil;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockDoor;
import net.minecraft.block.BlockEnchantmentTable;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.AbstractClientPlayer;
import net.minecraft.client.gui.GuiSelectWorld;
import net.minecraft.client.gui.inventory.GuiContainerCreative;
import net.minecraft.client.renderer.EntityRenderer;
import net.minecraft.client.resources.SkinManager;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.client.shader.ShaderGroup;
import net.minecraft.command.ServerCommand;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.monster.EntityMob;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.entity.player.EntityPlayer.EnumChatVisibility;
import net.minecraft.entity.player.EntityPlayer.EnumStatus;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerPlayer;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemPotion;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.network.PacketBuffer;
import net.minecraft.network.play.server.S23PacketBlockChange;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource; 
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.Explosion;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraftforge.client.ForgeHooksClient;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiScreenEvent.InitGuiEvent;
import net.minecraftforge.client.event.MouseEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.event.CommandEvent;
import net.minecraftforge.event.ServerChatEvent;
import net.minecraftforge.event.entity.EntityEvent;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.event.entity.living.LivingDropsEvent;
import net.minecraftforge.event.entity.living.LivingEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingJumpEvent;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.event.entity.living.LivingSpawnEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerOpenContainerEvent;
import net.minecraftforge.event.entity.player.PlayerSleepInBedEvent;
import net.minecraftforge.event.entity.player.PlayerUseItemEvent;
import net.minecraftforge.event.entity.player.PlayerWakeUpEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;
import net.minecraftforge.event.world.BlockEvent.BreakEvent;
import net.minecraftforge.event.world.ChunkEvent;
import net.minecraftforge.event.world.ExplosionEvent;

public class EventsListener {
	
	public static World worldObj = null;
	
	public boolean firstUpdate = true;
	
	private boolean displayedJOption = false;
	
	private long lastSmsRedistrubitionTime;
	
    private long lastBitcoinUpdateTime;
    
    private long lastMarketUpdate;
    		

	
	@SubscribeEvent
	public void onJoin(PlayerLoggedInEvent event) {
		EntityPlayer player = event.player;
				
		if(MinecraftServer.getServer().isDedicatedServer())
		{		

	    	if(!(event.player.posX > Double.NEGATIVE_INFINITY && event.player.posX < Double.POSITIVE_INFINITY) || !(event.player.posY > Double.NEGATIVE_INFINITY && event.player.posY < Double.POSITIVE_INFINITY) || !(event.player.posZ > Double.NEGATIVE_INFINITY && event.player.posZ < Double.POSITIVE_INFINITY))
	    	{
	    		event.player.setPosition(-1150f, 65f, -1146);
	    	}
	    	
			ExtendedPlayer data = ExtendedPlayer.get(player);
	    	
	    	player.setInvisible(true);
			data.connectionPos = Vec3.createVectorHelper(player.posX, player.posY, player.posZ);
		}
		else
		{
			ExtendedPlayer data = ExtendedPlayer.get(player);

			data.passwordReceived = true;
			onPlayerAuthentificated(player);
		}
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketAuthentification("",""), (EntityPlayerMP)event.player);
	}
	
	@SubscribeEvent
	public void onLeft(PlayerLoggedOutEvent event) {
		WorldData.get(event.player.worldObj).driveablesManager.onPlayerDisconnect(event.player);
	}
	
	
	@SubscribeEvent
	public void onEntityJoinWorld(EntityJoinWorldEvent event)
	{
		if(event.world.isRemote)
		{
			if(event.entity instanceof AbstractClientPlayer)
			{
				new AvatarUpdater((EntityPlayer)event.entity).updateAvatar();
			}
		}
	}
	
	
	public static void onPlayerAuthentificated(EntityPlayer player)
	{
		ExtendedPlayer pData = ExtendedPlayer.get(player);

		if(pData.shield.getShield() > 0)
		{
	    	ServerUtils.syncShieldToSpigot(player.getCommandSenderName(),pData.shield.getShield());
		}
		
		if(pData.firstJoin)
		{
			if(pData.phoneData.getNumber().isEmpty())
			{
				String number = NumberData.generateNumber(player.getCommandSenderName());
				try {
					NumberData.saveNumber(number, player);
				} catch (IOException e) {
					e.printStackTrace();
				}
				
	    		pData.phoneData.setNumber(number);
			}
			
    		if(MinecraftServer.getServer().isDedicatedServer())
    		{
	    		pData.identityData.waitingDataFromClient = true;
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketOpenCharacterGui(), (EntityPlayerMP) player);
    		}
		}
		else if(pData.identityData.name == null || pData.identityData.name.isEmpty())
		{
			if(MinecraftServer.getServer().isDedicatedServer())
			{
				pData.identityData.waitingDataFromClient = true;
				CraftYourLifeRPMod.packetHandler.sendTo(new PacketOpenCharacterGui(), (EntityPlayerMP) player);
			}
		}
		
		pData.sync();
		pData.updateRendererDatas();
		pData.PenaltyManager.updatePenalties();
		CraftYourLifeRPMod.packetHandler.sendTo(new PacketOpenTelephone(true), (EntityPlayerMP) player); 
		
		
		System.out.println("/passwordsync " + player.getCommandSenderName());
	}
		
	 @SideOnly(Side.CLIENT)
	 @SubscribeEvent
	 public void onClientReceiveData(ClientChatReceivedEvent event) 
	 {
		 String data = event.message.getUnformattedText();
		 EntityPlayer p = Minecraft.getMinecraft().thePlayer;
		 if(data.startsWith("WANTED") || data.startsWith("COMPANY"))
		 {
		 
			 String[] splitData = data.split("#SEPARATOR#");
			 
			 if(splitData[0].equals("WANTED")) 
			 {
				 event.setCanceled(true);
				 if(Integer.parseInt(splitData[1]) == 0) 
				 {
					 for(int i = 0; i < 5; i++) 
					 {
						 CraftYourLifeRPMod.getClientData().wantedLevel[i] = false;
						 CylrpMessageHUD.sendMessage("&aVous n'etes plus recherche", 500, (byte)0);
					 }
					 return;
				 }
				 for(int i = 0; i < Integer.parseInt(splitData[1]); i++) 
				 {
					 CraftYourLifeRPMod.getClientData().wantedLevel[i] = true;
					 CylrpMessageHUD.sendMessage("&c[Alerte] &dUn avis de recherche vient d'etre lancé contre vous", 500,(byte)0);
				 }
			 }
			 else if(splitData[0].equalsIgnoreCase("COMPANY"))
			 {
				 event.setCanceled(true);
				 
				 String[] repartitionsStr = splitData[5].split(",");
				 int[] repartitions = new int[repartitionsStr.length];
				 for(int i = 0; i < repartitionsStr.length; i++)
				 {
					 repartitions[i] = Integer.parseInt(repartitionsStr[i]);
				 }
				 
				 List<String> achievements = new ArrayList();
				 if(!splitData[8].equalsIgnoreCase("null"))
				 {
					 String[] achievementsSplitted = splitData[8].split(",");
					 for(String achievement : achievementsSplitted)
					 {
						 achievements.add(achievement);
					 }
				 }
				 
				 List<CompanyUser> users = new ArrayList();
	
				 CompanyUser user = new CompanyUser();
				 user.rank = "Gerant";
				 user.username = splitData[9];
				 users.add(user);
				 
				 if(!splitData[10].equalsIgnoreCase("null"))
				 {
					 String[] coGerants = splitData[10].split(",");
					 for(String coGerant : coGerants)
					 {
						 user = new CompanyUser();
						 user.rank = "CoGerant";
						 user.username = coGerant;
						 users.add(user);
					 }
				 }
				 
				 if(!splitData[11].equalsIgnoreCase("null"))
				 {
					 String[] managers = splitData[11].split(",");
					 for(String manager : managers)
					 {
						 user = new CompanyUser();
						 user.rank = "Managers";
						 user.username = manager;
						 users.add(user);
					 }
				 }
			
				 if(!splitData[12].equalsIgnoreCase("null"))
				 {
					 String[] secretaires = splitData[12].split(",");
					 for(String secretaire : secretaires)
					 {
						 user = new CompanyUser();
						 user.rank = "Secretaires";
						 user.username = secretaire;
						 users.add(user);
					 }
				 }
				 
				 if(!splitData[13].equalsIgnoreCase("null"))
				 {
					 String[] stagiaires = splitData[13].split(",");
					 for(String stagiaire : stagiaires)
					 {
						 user = new CompanyUser();
						 user.rank = "Stagiaires";
						 user.username = stagiaire;
						 users.add(user);
					 }
				 }
				 
				 if(!splitData[14].equalsIgnoreCase("null"))
				 {
					 String[] salaries = splitData[14].split(",");
					 for(String salarie : salaries)
					 {
						 user = new CompanyUser();
						 user.rank = "Salaries";
						 user.username = salarie;
						 users.add(user);
					 }
				 }
				 
				 CompanyManager.displayCompany(splitData[1], Integer.parseInt(splitData[2]), Double.parseDouble(splitData[3]), Double.parseDouble(splitData[4]), repartitions, Double.parseDouble(splitData[6]), Integer.parseInt(splitData[7]), achievements, users);
			 }
		 }
	 }
	 
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void onRenderLiving(RenderLivingEvent.Specials.Pre event) {
		
		if (event.entity instanceof EntityPlayer) {
			if (event.isCancelable()) {
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onPlayerTick(PlayerTickEvent event)
	{
			EntityPlayer player = event.player;
			//PlayerCachedData data = PlayerCachedData.getData(player);
			ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
			if(event.phase == event.phase.END)
			{			
				if(extendedPlayer.isSleeping())
				{
					
					++extendedPlayer.sleepingTime;
				
					
					if(extendedPlayer.sleepingTime > 100)
					{
						if(extendedPlayer.getgAlcolInBlood() < 4.0F)
						{
							extendedPlayer.setgAlcolInBlood(extendedPlayer.getgAlcolInBlood()-0.00040F);
						}
					}
					double yDelta = 0.0D;
					if(event.player.worldObj.isRemote && event.player == Minecraft.getMinecraft().thePlayer)
					{ 
						yDelta += 1.6D;
					}
					player.setPosition(extendedPlayer.sleepX, extendedPlayer.sleepY + yDelta, extendedPlayer.sleepZ);


					player.rotationYaw = MinecraftUtils.getPlayerYawFromBedDirection(player);
					player.rotationYawHead = MinecraftUtils.getPlayerYawFromBedDirection(player);

					player.rotationPitch = 0F;
			        player.cameraYaw = 0F;
				}
				
				if(!event.player.worldObj.isRemote)
				{				
					if(extendedPlayer.isSleeping())
					{
						if(!ExtendedPlayer.isInBed(player))
						{
							ExtendedPlayer.forceWakeupPlayer(player);
						}
					}
				
					float playergAlcolInBlood = extendedPlayer.getgAlcolInBlood();

					if(extendedPlayer.getgAlcolInBlood() > 0)
					{
						if(!extendedPlayer.shouldBeInEthylicComa() && extendedPlayer.tickAlcol++ >= 20*60*10)
						{
							extendedPlayer.tickAlcol = 0;
							playergAlcolInBlood -= 0.25F;
						}
						
						int time = (int) ((extendedPlayer.getgAlcolInBlood()*4)*50/4);
						int effectPower = (int) (4-(4-extendedPlayer.getgAlcolInBlood()));
						

						if(effectPower > 0) player.addPotionEffect(new PotionEffect(Potion.moveSlowdown.id,time,effectPower-1));
						
						extendedPlayer.setgAlcolInBlood(playergAlcolInBlood);
					}
					
					if(extendedPlayer.getgAlcolInBlood() >= 4)
					{
						if(MathHelper.getRandomIntegerInRange(player.getRNG(), 0, 20*15) == 0)
						{
							player.attackEntityFrom(new DamageSource("alcol"), 0.5F);
						}
					}
					
					if(extendedPlayer.inAviation)
					{
						player.fallDistance = 0;
						if(!(player.ridingEntity != null && player.ridingEntity instanceof EntityPlane) && player.onGround)
						{
							extendedPlayer.inAviation = false;
						}
					}
					
					if(player.isUsingItem() && player.getHeldItem().getItem() instanceof ItemCigarette)
					{
						extendedPlayer.itemPressTicks++;
						if(extendedPlayer.itemPressTicks >= 40)
						{
							extendedPlayer.itemPressTicks = 0;
						    player.worldObj.playSoundAtEntity(player, "craftyourliferp:smoking_0", 0.5F,  player.worldObj.rand.nextFloat() * 0.1F + 0.9F);
						}
					}
															
					if(player.ticksExisted % 20 == 0)
					{	
						
						if(extendedPlayer.connectionPos != null)
						{
							if(player.posX != extendedPlayer.connectionPos.xCoord || player.posY != extendedPlayer.connectionPos.yCoord || player.posZ != extendedPlayer.connectionPos.zCoord)
							{		
								player.setPositionAndUpdate(extendedPlayer.connectionPos.xCoord, extendedPlayer.connectionPos.yCoord, extendedPlayer.connectionPos.zCoord);
							}
					    }
					}
					
					if(extendedPlayer.stealingTile == null) return;
					
					if(player.ticksExisted % 5 == 0)
					{
						if(extendedPlayer.isStealing())
						{
							
							if(extendedPlayer.serverData.job == null || !ServerUtils.isIlegalJob(extendedPlayer.serverData.job))
							{
								
								player.addChatComponentMessage(new ChatComponentText("§cVous ne pouvez pas braquer"));
								player.addChatComponentMessage(new ChatComponentText("§cVous êtes hors service ou n'avez pas le métier nécessaire"));

								extendedPlayer.setNotStealing();
								return;
							}
							
							MovingObjectPosition mop = ExtendedPlayer.rayTraceServer(player, 4.0D, 1.0F);
							
							TileEntity tileEntity = (TileEntity) player.worldObj.getTileEntity(mop.blockX, mop.blockY, mop.blockZ);
							
							if(tileEntity == null)
							{
								tileEntity = (TileEntity) player.worldObj.getTileEntity(mop.blockX, mop.blockY+1, mop.blockZ);
							}
						
							IStealingTileEntity stealingTileEntity = (IStealingTileEntity) tileEntity;
							TileEntity dataTile = (TileEntity) extendedPlayer.stealingTile;
							
							if(player.getHeldItem() != null)
							{
								extendedPlayer.setNotStealing();
								player.addChatMessage(new ChatComponentText("§cVol annulé vous ne devez rien avoir en main"));
							}
							
							if(tileEntity == null || !(tileEntity instanceof IStealingTileEntity) || dataTile != tileEntity)
							{
								extendedPlayer.setNotStealing();
								player.addChatMessage(new ChatComponentText("§cVol annulé rapprochez-vous ou visez bien le block"));
							}
						}


					}
					
					if(player.ticksExisted % 10 == 0)
					{
						if(extendedPlayer.isStealing())
						{
							
							if((System.currentTimeMillis() - extendedPlayer.stealingTile.getStealingStartedTime()) / 1000 >= extendedPlayer.stealingTile.getStealingTime())
							{
								extendedPlayer.stealingTile.finalizeStealing();	
							}
						}
					}
					
					
					
				}
				else
				{
					
		

					if(player.getHealth() < player.getMaxHealth() / 2f || player.getHeldItem() != null && player.getHeldItem().getItem() == ModdedItems.itemBulletproofShield)
					{
						player.motionX *= 0.65;
						player.motionY *= 0.999985;
						player.motionZ *= 0.65;
					}
					
					
					
					Minecraft mc = Minecraft.getMinecraft();
					if(extendedPlayer.isProning())
					{
						if(player.isSprinting())
						{
							player.setSprinting(false);
						}
						player.motionX *= 0.00000000001;
						player.motionZ *= 0.00000000001;
							
						if(event.player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0)
						{
							mc.renderViewEntity.posY -= 1.2F;
						}
					}
					else if(extendedPlayer.isSleeping())
					{
						if(event.player == mc.thePlayer && mc.gameSettings.thirdPersonView == 0)
						{
							mc.renderViewEntity.posY -= 1.2F;
						}
					}
					
				}
				
				
			}
			
				
	}
	
	@SubscribeEvent
	public void onPlayerInteractBed(PlayerInteractEvent event)
	{
		if(!event.world.isRemote)
		{
			if(event.action == Action.RIGHT_CLICK_BLOCK)
			{
				if(ServerUtils.isOp(event.entityPlayer) && event.entityPlayer.capabilities.isCreativeMode)
				{
					if(event.entityPlayer.getHeldItem() != null &&  event.entityPlayer.getHeldItem().hasDisplayName() && event.entityPlayer.getHeldItem().getDisplayName().equals(ReanimationHandler.syringeDisplayName))
					{
						WorldData worldData = WorldData.get(event.world);
						if(event.entityPlayer.isSneaking())
						{

							if(worldData.removeHospitalBed(new ChunkCoordinates(event.x, event.y, event.z)))
        					{
        						ServerUtils.sendChatMessage(event.entityPlayer, "§aLit d'hôpital supprimé avec succès!");
        					}
        					else
        					{
        						ServerUtils.sendChatMessage(event.entityPlayer, "§cCe lit d'hôpital n'existe pas");
        					}
						}
						else
						{
							if(worldData.addHospitalBed(new ChunkCoordinates(event.x, event.y, event.z), "Chambre " + (worldData.hospitalBeds.size()+1)))
        					{
        						ServerUtils.sendChatMessage(event.entityPlayer, "§aLit d'hôpital ajouté avec succès!");
        					}
        					else
        					{
        						ServerUtils.sendChatMessage(event.entityPlayer, "§cCe lit d'hôpital existe déjà ou il n'y a pas de tête de lit dans votre clique");
        					}
						}
						event.setCanceled(true);
					}
				}
				else
				{
					Block block = event.world.getBlock(event.x, event.y, event.z);
					if(block == Blocks.bed)
					{
						ExtendedPlayer.forcePlayerSleep(event.entityPlayer, event.x, event.y, event.z,false);
						event.setCanceled(true);
					}
				}
			}
		}
	}
	

	
	@SubscribeEvent(priority=EventPriority.HIGHEST,receiveCanceled=false)
	public void onPlayerInteract(PlayerInteractEvent event)
	{
		if(!event.world.isRemote)
		{
			if(event.action == Action.RIGHT_CLICK_BLOCK)
			{
				List<EntityStopStick> stopsticks = (List<EntityStopStick>) event.world.loadedEntityList.stream().filter(x -> x instanceof EntityStopStick).collect(Collectors.toList());
				
				boolean stickDestroyed = false;
				for(EntityStopStick stick : stopsticks)
				{
					if(stick.destroy(event.x, event.y+1, event.z, event.entityPlayer))
					{
						stickDestroyed = true;
					}
				}
				
				if(stickDestroyed) return;
				
				Block block = event.world.getBlock(event.x, event.y, event.z);
				
				if(!event.entityPlayer.capabilities.isCreativeMode && block instanceof BlockEnchantmentTable || block instanceof BlockAnvil)
				{
					ServerUtils.sendChatMessage(event.entityPlayer, "§cVous ne pouvez pas utiliser ce block");
					event.setCanceled(true);
					return;
				}
				
				Block Atm = block.getBlockById(CraftYourLifeRPMod.ATM);
				
				if(Block.isEqualTo(block, Atm))
				{
					CraftYourLifeRPMod.packetHandler.sendTo(PacketAtm.packetOpenAtm(), (EntityPlayerMP)event.entityPlayer);
				}
				
				if(event.entityPlayer.getHeldItem() != null && event.entityPlayer.getHeldItem().getItem() == ModdedItems.itemCrowbar)
				{
					if(ServerUtils.isOp(event.entityPlayer) && event.entityPlayer.capabilities.isCreativeMode)
					{
						WorldData world = WorldData.get(event.world);
						
						if(!world.thieftAreaSelector.selectionDefinied())
						{
							if(world.thieftAreaSelector.getSelectedPos1() == null)
							{
								world.setThiefAreaPos1(Vec3.createVectorHelper(event.x, event.y, event.z));
								event.entityPlayer.addChatComponentMessage(new ChatComponentText("§aThief area pos1 defined"));
							}
							else if(world.thieftAreaSelector.getSelectedPos2() == null)
							{
								world.setThiefAreaPos2(Vec3.createVectorHelper(event.x, event.y, event.z));
								event.entityPlayer.addChatComponentMessage(new ChatComponentText("§aThief area pos2 defined"));
							}
						}
						else
						{
							world.thieftAreaSelector.resetSelection();
							event.entityPlayer.addChatComponentMessage(new ChatComponentText("§aThief area selection reset"));
						}
						
					}
					return;
				}
			
				if(event.entityPlayer.getHeldItem() != null) return;

				ExtendedPlayer pData = ExtendedPlayer.get(event.entityPlayer);
					
				if(pData.regionBuilder != null && !pData.regionBuilder.selectionDefinied())
				{
					if(pData.regionBuilder.getSelectedPos1() == null)
					{
						pData.regionBuilder.setSelectedPos1(Vec3.createVectorHelper(event.x, event.y, event.z));
						event.entityPlayer.addChatMessage(new ChatComponentText("§4Pos1 selected"));
					}
					else if(pData.regionBuilder.getSelectedPos2() == null)
					{
						pData.regionBuilder.setSelectedPos2(Vec3.createVectorHelper(event.x, event.y, event.z));
						event.entityPlayer.addChatMessage(new ChatComponentText("§4Pos2 selected"));
						event.entityPlayer.addChatMessage(new ChatComponentText("§4Region configured!"));

						if(pData.builderType == 0)
						{
							WorldData.get(event.entity.worldObj).addFireRegion(pData.regionBuilder);
						}
						else if(pData.builderType == 1)
						{
							WorldData.get(event.entity.worldObj).addExplosionsRegion(pData.regionBuilder);
						}
						else if(pData.builderType == 2)
						{
							WorldData.get(event.entity.worldObj).addCaptureRegion(event.entityPlayer,pData.captureType, pData.regionBuilder);
						}
					}
						
					return;
				}
					
					if(pData.selectedBall != null)
					{
							
						if(pData.selectedBall.getPost1Selector().getSelectedPos1() == null)
						{
							pData.selectedBall.getPost1Selector().setSelectedPos1(Vec3.createVectorHelper(event.x, event.y, event.z));
							event.entityPlayer.addChatMessage(new ChatComponentText("Post1 Pos1 selected"));
							return;
						}
							
						if(pData.selectedBall.getPost1Selector().getSelectedPos2() == null)
						{
							pData.selectedBall.getPost1Selector().setSelectedPos2(Vec3.createVectorHelper(event.x, event.y, event.z));
							event.entityPlayer.addChatMessage(new ChatComponentText("Post1 Pos2 selected"));
							return;
						}
							
						if(pData.selectedBall.getPost2Selector().getSelectedPos1() == null)
						{
							pData.selectedBall.getPost2Selector().setSelectedPos1(Vec3.createVectorHelper(event.x, event.y, event.z));
							event.entityPlayer.addChatMessage(new ChatComponentText("Post2 Pos1 selected"));
							return;
						}
							
						if(pData.selectedBall.getPost2Selector().getSelectedPos2() == null)
						{
							pData.selectedBall.getPost2Selector().setSelectedPos2(Vec3.createVectorHelper(event.x, event.y, event.z));
							event.entityPlayer.addChatMessage(new ChatComponentText("Post2 Pos2 selected"));
							return;
						}
							
							
						if(pData.selectedBall.getWorldSelector().getSelectedPos1() == null)
						{
							pData.selectedBall.getWorldSelector().setSelectedPos1(Vec3.createVectorHelper(event.x, event.y, event.z));
							event.entityPlayer.addChatMessage(new ChatComponentText("Terrain Pos1 selected"));
							return;
						}
							
						if(pData.selectedBall.getWorldSelector().getSelectedPos2() == null)
						{
	   						pData.selectedBall.getWorldSelector().setSelectedPos2(Vec3.createVectorHelper(event.x, event.y, event.z));
							event.entityPlayer.addChatMessage(new ChatComponentText("Terrain Pos2 selected"));
							return;
						}
				}
			}
			
		}
			
		if(event.action == Action.RIGHT_CLICK_BLOCK || event.action == Action.RIGHT_CLICK_AIR)
		{
			if(!event.world.isRemote)
			{
				if(event.entityPlayer.getCurrentEquippedItem() != null)
				{
					if(event.entityPlayer.getCurrentEquippedItem().getItem() == ModdedItems.KSamsung)
					{
						CraftYourLifeRPMod.packetHandler.sendTo(new PacketOpenTelephone(),(EntityPlayerMP) event.entityPlayer);
					}
					else if(event.entityPlayer.capabilities.isCreativeMode && event.entityPlayer.getCurrentEquippedItem().getItem() instanceof PaintingItem)
					{
						ItemStack is = event.entityPlayer.getCurrentEquippedItem();
						
						if(!is.hasTagCompound())
						{
							is.stackTagCompound = new NBTTagCompound();
							is.stackTagCompound.setByte("PaintingType", (byte) 0);
						}
						else
						{
							byte currentType = is.stackTagCompound.getByte("PaintingType");
							if(currentType + 1 > 11)
							{
								is.stackTagCompound.setByte("PaintingType", (byte) (0));
							}
							else
							{
								is.stackTagCompound.setByte("PaintingType", (byte) (currentType+1));
							}
						}
					}
					else if(event.entityPlayer.getCurrentEquippedItem().getItem() instanceof ItemPotion && !event.entityPlayer.capabilities.isCreativeMode)
					{
						ServerUtils.sendChatMessage(event.entityPlayer, "§cVous ne pouvez pas utiliser cette item.");
						event.setCanceled(true);
					}
				}
			
			}
		}

	}
	
	
	
    @SubscribeEvent
    public void onEntityConstructing(EntityEvent.EntityConstructing event)
    {
        if (event.entity instanceof EntityPlayer && ExtendedPlayer.get((EntityPlayer) event.entity) == null) 
        {
            ExtendedPlayer.register((EntityPlayer) event.entity);
        }
    }
   
    
    @SubscribeEvent
    public void onClonePlayer(PlayerEvent.Clone e) 
    {
        if(e.wasDeath) {
            NBTTagCompound compound = new NBTTagCompound();
            
            ExtendedPlayer originalExp = ExtendedPlayer.get(e.original);
            originalExp.saveNBTData(compound);
            
            ExtendedPlayer exp = ExtendedPlayer.get(e.entityPlayer);
            
            originalExp.migrateCacheData(exp);
            
            exp.loadNBTData(compound);
            
        }
    }
    
    @SubscribeEvent
    public void onClientDisconnect(ClientDisconnectionFromServerEvent e)
    {
    	GuiPhone phone = GuiPhone.getPhone();
    	
    	if(phone != null)
    	{
			phone.currentApp = null;
	    	
	    	CallHandler ch = phone.getCallHandler();
	    	if(ch != null)
	    	{
	    		ch.recorder.setRunninng(false);
	    		phone.setCallHandler(null);
	    	}
	    	
			GuiPhone.setPhone(null);
    	}
		
		if(CraftYourLifeRPMod.radioHandler.getCurrentRadio() != null)
		{
			CraftYourLifeRPMod.radioHandler.stop();
		}
		
		PlayerCachedData.clearDatas();
    }
    
  
   
    
    
    @SubscribeEvent
	public void itemEvent(ItemTossEvent event)
	{
		if(!event.entity.worldObj.isRemote)
		{
			EntityPlayer player = (EntityPlayer) event.player;
	        //PlayerCachedData data = PlayerCachedData.getData(event.player);
	        ExtendedPlayer data = ExtendedPlayer.get(event.player);

	        if(data == null)
	        {
				player.inventory.addItemStackToInventory(event.entityItem.getEntityItem());
	        	event.setCanceled(true);
	        	return;
	        }
	        
	        
			if(!data.passwordReceived)
			{
				player.inventory.addItemStackToInventory(event.entityItem.getEntityItem());
				event.setCanceled(true);
			}
		}
	}
    
    @SubscribeEvent
	public void onContainerOpen(PlayerOpenContainerEvent event)
	{
		EntityPlayer player = event.entityPlayer;

		if(!event.entity.worldObj.isRemote)
		{
	        //PlayerCachedData data = PlayerCachedData.getData(event.entityPlayer);
	        ExtendedPlayer data = ExtendedPlayer.get(player);

	        if(data == null)
	        {
	        	player.closeScreen();
	        	return;
	        }
	        
	        
			if(!data.passwordReceived)
			{
				player.closeScreen();
			}
		}
	}
    
    @SubscribeEvent
    public void onSendChat(ServerChatEvent event)
    {
    	EntityPlayer player = event.player;
        //PlayerCachedData data = PlayerCachedData.getData(event.player);
        ExtendedPlayer data = ExtendedPlayer.get(player);

        if(data == null)
        {
        	event.setCanceled(true);
        	return;
        }
        
        
    	if(!data.passwordReceived)
    	{
    		event.setCanceled(true);
    	}
    }
    
    @SubscribeEvent
    public void onSendCommand(CommandEvent event) 
    {
    	if(!event.sender.getEntityWorld().isRemote)
    	{
	    	if(event.sender instanceof EntityPlayer)
	    	{
		        //PlayerCachedData data = PlayerCachedData.getData((EntityPlayer)event.sender);
		        ExtendedPlayer data = ExtendedPlayer.get((EntityPlayer)event.sender);

		        if(data == null)
		        {
		        	event.setCanceled(true);
		        	return;
		        }
		        
	        	if(!data.passwordReceived)
	        	{
	        		event.setCanceled(true);
	        	}	
	    	}
    	}
    }
    
    @SubscribeEvent
    public void onInteract(PlayerInteractEvent event)
    {
    	if(!event.entityPlayer.worldObj.isRemote)
    	{
	        //PlayerCachedData data = PlayerCachedData.getData(event.entityPlayer);
	        ExtendedPlayer data = ExtendedPlayer.get(event.entityPlayer);

	        if(data == null)
	        {
	        	event.setCanceled(true);
	        	return;
	        }
	        
	        if(!data.passwordReceived)
	        {
	        	event.setCanceled(true);
	        }
	        
	        
    	}
    } 
    
    
    @SubscribeEvent
    public void onExplosion(ExplosionEvent.Detonate event) 
    {
    	if(!event.world.isRemote)
    	{
	    	WorldData data = WorldData.get(event.world);
	    	
	    	WorldSelector region = data.getRegionForExplosion(event.explosion);
	    	
	    	List<EntityDriveable> driveables = event.world.getEntitiesWithinAABB(EntityDriveable.class, AxisAlignedBB.getBoundingBox(event.explosion.explosionX-event.explosion.explosionSize/2f, event.explosion.explosionY-event.explosion.explosionSize/2f, event.explosion.explosionZ-event.explosion.explosionSize/2f, event.explosion.explosionX+event.explosion.explosionSize/2f, event.explosion.explosionY+event.explosion.explosionSize/2f, event.explosion.explosionZ+event.explosion.explosionSize/2f));
	    	
	    	for(EntityDriveable driveable : driveables)
	    	{
	    		driveable.attackPart(EnumDriveablePart.core, DamageSource.generic, 500);
	    	}
	    			
	    	if(region != null)
	    	{
	    		List<ChunkPosition> list = event.getAffectedBlocks().stream().filter(x -> region.insideSelection(Vec3.createVectorHelper(x.chunkPosX, x.chunkPosY, x.chunkPosZ))).collect(Collectors.toList());
	    		for(ChunkPosition pos : list)
	    		{
					event.world.setBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ, Blocks.air);
	    		}
	    	}
	    	else
	    	{
	    		List<ChunkPosition> positions = new ArrayList();
	    		for(ChunkPosition pos : event.getAffectedBlocks())
	    		{
	    			
	    			Block block = event.world.getBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
	    			if(block instanceof BlockDoor)
	    			{
	    				if(event.world.getBlock(pos.chunkPosX, pos.chunkPosY-1, pos.chunkPosZ) instanceof BlockDoor)
						{
							continue;
						}
	    				else
	    				{
	    	    			byte metaData = (byte)event.world.getBlockMetadata(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ);
	
	    					WorldData worldData = WorldData.get(event.world);
	    					positions.add(pos);

	    					event.world.setBlock(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ, Blocks.air);
	    					worldData.getBlocksBackup().addBlock(new BlockData(Block.getIdFromBlock(block),metaData,Vec3.createVectorHelper(pos.chunkPosX, pos.chunkPosY, pos.chunkPosZ),600));
	    				}
	    			}
	    		}
	    		
	    	}
    	}
    	
    } 
    
    @SideOnly(Side.CLIENT)
    @SubscribeEvent(priority = EventPriority.HIGHEST)
    public void onKeyPress(KeyInputEvent event)
    {
        KeyBinding[] keyBindings = ClientProxy.keyBindings;
        
        Minecraft mc = Minecraft.getMinecraft();
        EntityPlayer p = mc.thePlayer;
        ExtendedPlayer extendedPlayer = ExtendedPlayer.get(p);
                
        if(ClientProxy.proningKeyBinding != null && ClientProxy.proningKeyBinding.isPressed())
        {
        	try {
				Method method = ClientProxy.proningKeyBinding.getClass().getDeclaredMethod("func_74505_d");
				method.setAccessible(true);
				method.invoke(ClientProxy.proningKeyBinding);
        	} catch ( SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
        	
        	AxisAlignedBB axis = null;
        	
        	if(extendedPlayer.isProning()) axis = AxisAlignedBB.getBoundingBox(p.boundingBox.minX, p.boundingBox.minY, p.boundingBox.minZ, p.boundingBox.minX + 0.8F, p.boundingBox.minY + 1.8F, p.boundingBox.minZ + 0.8F);
        	else axis = AxisAlignedBB.getBoundingBox(p.boundingBox.minX, p.boundingBox.minY, p.boundingBox.minZ, p.boundingBox.minX + 0.8F, p.boundingBox.minY + 0.6F, p.boundingBox.minZ + 0.8F);
        	
        	if(MinecraftUtils.boundingBoxCollide(p.worldObj, axis))
        	{
            	ServerUtils.sendChatMessage(mc.thePlayer, "§cVous ne pouvez pas vous levez/coucher ici.");
        		return;
        	}
        	
        	if(extendedPlayer.currentAnimation != 0)
        	{
        		ServerUtils.sendChatMessage(mc.thePlayer, "§cVous ne pouvez pas vous baisser actuellement");
        		return;
        	}
        	

        	if(mc.thePlayer.ticksExisted - extendedPlayer.lastProningTick <= 40)
        	{
        		ServerUtils.sendChatMessage(mc.thePlayer, "§cAttendez 2 secondes.");
        		return;
        	}
        	extendedPlayer.lastProningTick = mc.thePlayer.ticksExisted;
        	extendedPlayer.setProning(!extendedPlayer.isProning());
        	CraftYourLifeRPMod.packetHandler.sendToServer(PacketProning.setProning());
        }
       
        
        if (Keyboard.getEventKey() == 30 && Keyboard.isKeyDown(61))
        {
        	CraftYourLifeRPClient.displayBlackScreen(5);
        }
        
        if(keyBindings[0].isPressed())
        {        	
        	if(p.ridingEntity != null) return;
        	
        	if(ExtendedPlayer.get(p).thirst.playerLookWater(p.worldObj))
        	{
        		CraftYourLifeRPMod.packetHandler.sendToServer(PacketThirst.drinkWater());
        	}
        	else
        	{
        		
        		if(p.getCurrentEquippedItem() != null)
            	{
            		if(p.getCurrentEquippedItem().getItem() == ModdedItems.identityCard)
            		{
        				CraftYourLifeRPMod.packetHandler.sendToServer(new PacketOpenCardIdentity(p));		   
            		}
            	}
        		else if(mc.objectMouseOver.typeOfHit == MovingObjectType.BLOCK)
        		{
	                ItemStack itemstack = mc.thePlayer.inventory.getCurrentItem();
	        		int i = mc.objectMouseOver.blockX;
	                int j =  mc.objectMouseOver.blockY;
	                int k =  mc.objectMouseOver.blockZ; 
	        	    boolean result = !net.minecraftforge.event.ForgeEventFactory.onPlayerInteract(p, net.minecraftforge.event.entity.player.PlayerInteractEvent.Action.RIGHT_CLICK_BLOCK, i, j, k, mc.objectMouseOver.sideHit, mc.theWorld).isCanceled();
	                if (result && mc.playerController.onPlayerRightClick(mc.thePlayer, mc.theWorld,itemstack, i, j, k, mc.objectMouseOver.sideHit, mc.objectMouseOver.hitVec))
	                {
	                    mc.thePlayer.swingItem();
	                }
        		}
        		else if(mc.objectMouseOver.typeOfHit == MovingObjectType.ENTITY)
        		{
        			if(Minecraft.getMinecraft().objectMouseOver.entityHit instanceof EntityPlayer)
	        		{
	  		            EntityPlayer target = (EntityPlayer) Minecraft.getMinecraft().objectMouseOver.entityHit;
	  		            CraftYourLifeRPMod.packetHandler.sendToServer(new PacketOpenCardIdentity(target));		 
	        		}
        			
        		}
        	}
        }
        else if(keyBindings[1].isPressed())
        {
        	Minecraft.getMinecraft().displayGuiScreen(new GuiAnimation());
        }
        else if(keyBindings[2].isPressed())
        {   
        	CraftYourLifeRPClient.optimizeRendering = !CraftYourLifeRPClient.optimizeRendering;
        	
        	if(CraftYourLifeRPClient.optimizeRendering)
        	{
        		p.addChatMessage(new ChatComponentText("§6Optimisation : §aON"));
        	}
        	else
        	{
        		p.addChatMessage(new ChatComponentText("§6Optimisation : §cOFF"));
        	}    
        }
        else if(mc.gameSettings.keyBindSneak.isPressed() && (extendedPlayer.isProning() || extendedPlayer.isSleeping())) 
        {
        	try {
				Method method = mc.gameSettings.keyBindSneak.getClass().getDeclaredMethod("func_74505_d");
				method.setAccessible(true);
				method.invoke(mc.gameSettings.keyBindSneak);
        	} catch ( SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
        }
        else if(mc.gameSettings.keyBindJump.isPressed() && (extendedPlayer.isProning() || extendedPlayer.isSleeping())) 
        {
        	try {
				Method method = mc.gameSettings.keyBindJump.getClass().getDeclaredMethod("func_74505_d");
				method.setAccessible(true);
				method.invoke(mc.gameSettings.keyBindJump);
        	} catch ( SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException | NoSuchMethodException e) {
				e.printStackTrace();
			}
        }
        else if((mc.gameSettings.keyBindForward.isPressed() || mc.gameSettings.keyBindBack.isPressed() || mc.gameSettings.keyBindLeft.isPressed() || mc.gameSettings.keyBindRight.isPressed()) && extendedPlayer.isSleeping())
        {
        	mc.gameSettings.keyBindForward.unPressAllKeys();
        }
        

    }
    
    @SubscribeEvent
    public void onPlayerJump(LivingJumpEvent event)
    {
    	if(event.entityLiving instanceof EntityPlayer)
    	{
    		EntityPlayer player = (EntityPlayer) event.entityLiving;
    		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);

    	
    		if(extendedPlayer.isProning() || extendedPlayer.isSleeping())
    		{
    			player.motionY = 0F;
    		}
    	}
    }
    
    @SubscribeEvent
    public void onInterractWithFootballBallon(EntityInteractEvent event)
    {
    	if(!event.target.worldObj.isRemote)
    	{
		    if(event.target instanceof EntityFootballBall)
		    {
					if(FMLCommonHandler.instance().getMinecraftServerInstance().getConfigurationManager().func_152596_g(event.entityPlayer.getGameProfile()))
					{
			    		//PlayerCachedData pData = PlayerCachedData.getData(event.entityPlayer);
			    		ExtendedPlayer pData = ExtendedPlayer.get(event.entityPlayer);
						EntityFootballBall ball = (EntityFootballBall) event.target;
		
			    		if(pData.selectedBall == null)
			    		{
			    			pData.selectedBall = ball;
			    			event.entityPlayer.addChatMessage(new ChatComponentText("Ball selected"));
			    		}
			    		else 
			    		{
			    			pData.selectedBall = null;
			    			event.entityPlayer.addChatMessage(new ChatComponentText("Ball unselected"));
			    		}
					}
		    }
		    else if(event.target instanceof EntityPlayer)
		    {
		    	ItemStack heldItem = event.entityPlayer.getHeldItem();
		    	if(heldItem != null)
		    	{
		    		if(heldItem.hasDisplayName() && heldItem.getDisplayName().equalsIgnoreCase("§aEthylotest"))
		    		{
		    			ExtendedPlayer targetEP = ExtendedPlayer.get((EntityPlayer)event.target);
		    			
		    			ServerUtils.sendChatMessage(event.entityPlayer, "§6Possède §e" + String.format("%.2f", targetEP.getgAlcolInBlood()) + "g §6d'alcool dans le sang");
		    			event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] = null;
		    		}
		    	}
		    }
		    else if(event.target instanceof EntitySeat)
		    {
		    	EntitySeat seat = (EntitySeat) event.target;
		    	
		    	if(seat.riddenByEntity instanceof EntityPlayer)
		    	{
			    	EntityPlayer targetPlayer = (EntityPlayer)seat.riddenByEntity;
			    	
			    	ItemStack heldItem = event.entityPlayer.getHeldItem();
			    	if(heldItem != null)
			    	{
			    		if(heldItem.hasDisplayName() && heldItem.getDisplayName().equalsIgnoreCase("§aEthylotest"))
			    		{
			    			ExtendedPlayer targetEP = ExtendedPlayer.get(targetPlayer);
			    			
			    			ServerUtils.sendChatMessage(event.entityPlayer, "§6Possède §e" + String.format("%.2f", targetEP.getgAlcolInBlood()) + "g §6d'alcool dans le sang");
			    			event.entityPlayer.inventory.mainInventory[event.entityPlayer.inventory.currentItem] = null;
			    		}
			    	}
		    	}
		    	
		    	
		    }

    	}
    }
    
    @SubscribeEvent
    public void onWorldTick(WorldTickEvent event)
    {
    	if(event.phase == event.phase.END)
    	{
	    	if(!event.world.isRemote)
	    	{
	    		WorldData worldData = WorldData.get(event.world);
		    	if((System.currentTimeMillis() - lastSmsRedistrubitionTime) / 1000 >= 5)
		    	{
		    		lastSmsRedistrubitionTime = System.currentTimeMillis();	
		    		worldData.distribuateSms();
		    	}
		    	
		    	if((System.currentTimeMillis() - lastBitcoinUpdateTime) / 1000 >= 60*15)
				{
		    		lastBitcoinUpdateTime = System.currentTimeMillis();
		    		worldData.updateBitcoinPrice();
				}
		    	
		    	Timestamp currentData = new Timestamp(System.currentTimeMillis());
		    	if(currentData.getHours() > 0 && currentData.getMinutes() > 0)
		    	{
		    		if(!worldData.getMarketUpdated())
		    		{
		    			worldData.setMarketUpdated(true);
		    			worldData.updateMarket();
		    		}
		    	}
		    	else
		    	{
		    		worldData.setMarketUpdated(false);
		    	}
		    	
		    	if(event.world.getTotalWorldTime() % (20 * BlocksBackup.updateTime) == 0)
		    	{
		    		worldData.getBlocksBackup().update();
		    	}
		    	
		    	
	    	}
	    	
	    	
    	}
    }
    
    @SubscribeEvent
    public void onTickClient(TickEvent.ClientTickEvent event) 
    {
    	if(ClientProxy.forceExit)
    	{
    		for(int i = 0; i < 50; i++)System.out.print("#");
    		System.out.println("");
    		System.out.print("\n");
    		System.out.println("Les cheats sont interdit sur CYLRP! \n");
    		System.out.println("");
    		for(int i = 0; i < 50; i++)System.out.print("#");
    		ClientProxy.exit();
    	}
    }
    
    @SubscribeEvent
    public void onBlockBreak(BreakEvent event)
    {
    	Block block = event.block;
    	if(block instanceof BlockNuclearOre)
    	{
        	if(event.getPlayer().getHeldItem().getItem() == ModdedItems.nuclearPickaxe)
        	{
        		MinecraftUtils.dropBlockAsItem(event.world, event.x, event.y, event.z, new ItemStack(ModdedItems.nuclearIngot,event.world.rand.nextInt(3)));
        	}
    		ServerUtils.broadcastMessage("§9[Force de l'ordre] §cLe central nucléaire se fait braquer, des renforts sont demandés", (byte)0);
    		System.out.println("/wanted add " + event.getPlayer().getCommandSenderName() + " 4");
    	}
    	else if(block instanceof BlockNuclear)
    	{
    		ServerUtils.broadcastMessage("§9[Force de l'ordre] §cLe central nucléaire se fait braquer, des renforts sont demandés", (byte)0);
    		System.out.println("/wanted add " + event.getPlayer().getCommandSenderName() + " 4");
    	}
    }
    

    
    

}
	 
	
 
