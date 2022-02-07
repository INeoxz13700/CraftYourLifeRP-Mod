package fr.craftyourliferp.game.events;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import cpw.mods.fml.common.eventhandler.EventPriority;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent;
import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
import net.minecraftforge.event.world.BlockEvent;

public class ReanimationHandler {
	
	public static List<Integer[]> reanimationBeds = new ArrayList<Integer[]>();
	public final static List<Integer> itemsToNotRemove = Arrays.asList(6616,6615,4255,4256,345,4259,4260,4258);
	public final static List<Integer> itemsToForceRemoveOnReanimation = Arrays.asList(1188,1183,5462,5461,5463,5475,5469,1274,1275);
	public final static int deathAfterNotReanimatedTime = 60*5;
	
	public static final String syringeDisplayName = "§aSeringue";
	
	public static final int reanimationDurationTicks = 20*60*1;
	
	public boolean canWakeupFromBed(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
		if(extendedPlayer.getShouldBeReanimate() || (extendedPlayer.shouldBeInEthylicComa() && extendedPlayer.reanimatingPlayername != null))
		{
			return false;
		}
		return true;
	}
	
	public boolean damageForceWakeupFromBed(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
		if(extendedPlayer.getShouldBeReanimate() || extendedPlayer.shouldBeInEthylicComa())
		{
			return false;
		}
		return true;
	}
	
		
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDoctorInteractBed(PlayerInteractEvent event)
	{
		if(!event.world.isRemote)
		{
			if(event.action == Action.RIGHT_CLICK_BLOCK)
			{
				EntityPlayer doctorPlayer = event.entityPlayer;
				int x = event.x;
				int z = event.z;
				int i1 = event.world.getBlockMetadata(event.x, event.y, event.z);

				 
				if (!BlockBed.isBlockHeadOfBed(i1))
		        {
		             int j1 = BlockBed.getDirection(i1);
		             x += BlockBed.field_149981_a[j1][0];
		             z += BlockBed.field_149981_a[j1][1];

		             if (!(event.world.getBlock(x, event.y, z) instanceof BlockBed))
		             {
		                 return;
		             }
		        }
				
				EntityPlayer interactPlayer = null;
		        Iterator iterator = event.world.playerEntities.iterator();

				while (iterator.hasNext())
		        {
		           EntityPlayer entityplayer2 = (EntityPlayer)iterator.next();
		           ExtendedPlayer extendedPlayer2 = ExtendedPlayer.get(entityplayer2);

		           if (extendedPlayer2.isSleeping())
		           {
		        	   
		            	 ChunkCoordinates chunkcoordinates = entityplayer2.playerLocation;
		            	 
		            	 if(chunkcoordinates == null) continue;
		            	 
		                 if (chunkcoordinates.posX == x && chunkcoordinates.posY == event.y && chunkcoordinates.posZ == z)
		                 {
		                	 interactPlayer = entityplayer2;
		                 }
		            }
		        }
				
				if(interactPlayer != null)
				{		
					ExtendedPlayer doctorPlayerExtended = ExtendedPlayer.get(doctorPlayer);
					if(doctorPlayerExtended.getCallbackCheckIfDoctor() == null)
					{	
						EntityInteractEvent theEvent = new EntityInteractEvent(doctorPlayer, interactPlayer);
						doctorPlayerExtended.setCallbackCheckIfDoctor(theEvent);
						doctorPlayerExtended.updatePlayerData();
					}
					event.setCanceled(true);
				}
				
			}
		}
	}
	
	@SubscribeEvent(priority = EventPriority.HIGHEST)
	public void onDoctorInteractBed(EntityInteractEvent event)
	{
		World world = event.entityPlayer.worldObj;
		if(!world.isRemote)
		{
			if(event.target instanceof EntityPlayer)
			{
				
				EntityPlayer doctorPlayer = event.entityPlayer;
				
				EntityPlayer interactPlayer = (EntityPlayer) event.target;
				ExtendedPlayer interactPlayerExtended = ExtendedPlayer.get(interactPlayer);
				if(interactPlayerExtended.getShouldBeReanimate() || interactPlayerExtended.reanimatingPlayername != null)
				{
					doctorPlayer.closeScreen();
				}

				ExtendedPlayer doctorPlayerExtended = ExtendedPlayer.get(doctorPlayer);
				if(doctorPlayerExtended.getCallbackCheckIfDoctor() == null)
				{	
					doctorPlayerExtended.setCallbackCheckIfDoctor(event);
					doctorPlayerExtended.updatePlayerData();				
				}
			}
		}
	}
	
	public void onDoctorInteractPlayerPost(EntityInteractEvent event)
	{
		EntityPlayer doctorPlayer = event.entityPlayer;
		ExtendedPlayer doctorPlayerExtended = ExtendedPlayer.get(doctorPlayer);			

		
		
		if(doctorPlayerExtended.serverData.job.equalsIgnoreCase("Medecin"))
		{
			ItemStack heldItem = event.entityPlayer.getHeldItem();
		    if(heldItem != null && heldItem.hasDisplayName() && heldItem.getDisplayName().equals(syringeDisplayName)) //seringue pierre ici
		    {
				ExtendedPlayer targetEP = ExtendedPlayer.get((EntityPlayer)event.target);    	

		    	if(targetEP.getShouldBeReanimate() || targetEP.shouldBeInEthylicComa())
		    	{
			    	if(doctorPlayerExtended.reanimatingPlayername == null)
			    	{
			    		if(targetEP.reanimatingPlayername == null)
			    		{
			    			doctorPlayerExtended.reanimatingPlayername = event.target.getCommandSenderName();
					    	targetEP.reanimatingPlayername = doctorPlayer.getCommandSenderName();
				    		ServerUtils.sendChatMessage(doctorPlayer, "§aDébut de la réanimation de §c" + doctorPlayerExtended.reanimatingPlayername);
			    		}
			    		else
			    		{
			    			ServerUtils.sendChatMessage(doctorPlayer, "§cCe patient se fait déjà réanimer par quelqu'un.");
			    		} 
			    	}
			    	else
			    	{
				    	ServerUtils.sendChatMessage(doctorPlayer, "§cVous êtes déjà entrain de réanimer un patient.");
			    	}
		    	}
		    	else
		    	{
			    	ServerUtils.sendChatMessage(doctorPlayer, "§cCe patient est en pleine forme!");
		    	}
		    }
		    else
		    {
		    	ServerUtils.sendChatMessage(doctorPlayer, "§cVous devez avoir une seringue en main pour réanimer votre");
			    ServerUtils.sendChatMessage(doctorPlayer, "§cpatient.");
		    }
		}
	}
	
	@SubscribeEvent
	public void blockBreakEvent(BlockEvent.BreakEvent event)
	{
		World world = event.world;
		Block block = event.block;
		if(!world.isRemote)
		{
		     int x = event.x;
		     int y = event.y;
		     int z = event.z;
		     
	         int i1 = world.getBlockMetadata(x, y, z);

			 if (!BlockBed.isBlockHeadOfBed(i1))
	         {
	             int j1 = BlockBed.getDirection(i1);
	             x += BlockBed.field_149981_a[j1][0];
	             z += BlockBed.field_149981_a[j1][1];

	             if (!(world.getBlock(x, y, z) instanceof BlockBed))
	             {
	                 return;
	             }
	         }
			 
		     Iterator iterator = event.world.playerEntities.iterator();

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
		                	if(extendedPlayer2.getShouldBeReanimate() || extendedPlayer2.reanimatingPlayername != null)
		                	{
		                		ServerUtils.sendChatMessage(event.getPlayer(), "§cVous ne pouvez pas casser ce lit pour le moment.");
		                		event.setCanceled(true);
		                		return;
		                	}
		              }
		          }
		     }
		}
	}

	
	@SubscribeEvent
	public void onPlayerReanimateProcess(PlayerTickEvent event)
	{
		World world = event.player.worldObj;
		EntityPlayer thePlayer = event.player;
		
		if(world.isRemote) return;
		
		if(event.phase == TickEvent.Phase.END) return;
		
		ExtendedPlayer thePlayerExtended = ExtendedPlayer.get(thePlayer);
		
		if(thePlayerExtended.getShouldBeReanimate() || thePlayerExtended.shouldBeInEthylicComa()) //if player is in reanimation
		{		
			if(thePlayerExtended.getShouldBeReanimate() && thePlayerExtended.reanimatingPlayername == null)
			{
				int timeElapsedInSeconds = (int) ((System.currentTimeMillis() - thePlayerExtended.startTimeTimestamp) / 1000f);
				if(!thePlayerExtended.isSleeping())
				{
					subitDeath(thePlayer);
				}
				else if(thePlayer.ticksExisted % (20*5) == 0)
				{
					int leftTime = deathAfterNotReanimatedTime - timeElapsedInSeconds;
					ServerUtils.sendMessage("§cIl vous reste §e" + getLeftTimeDisplay(leftTime) + " §cavant de mourir!", thePlayer, 1000, 1);
				}
				
				if(timeElapsedInSeconds >= deathAfterNotReanimatedTime)
				{
					subitDeath(thePlayer);
					return;
				}
			}
			
			if(thePlayerExtended.reanimatingPlayername != null)
			{
				
				EntityPlayer doctorPlayer = world.getPlayerEntityByName(thePlayerExtended.reanimatingPlayername);
				if(doctorPlayer == null)
				{
					thePlayerExtended.reanimatingPlayername = null;
					thePlayerExtended.reanimationTick = 0;
					ServerUtils.sendMessage("§cRéanimation annulé votre docteur n'est plus la!", thePlayer, 1000, 1);
					return;
				}
				
				ExtendedPlayer doctorExtendedPlayer = ExtendedPlayer.get(doctorPlayer);
				
				if(doctorPlayer.getHeldItem() == null || (doctorPlayer.getHeldItem() != null && (!doctorPlayer.getHeldItem().hasDisplayName() || !doctorPlayer.getHeldItem().getDisplayName().equals(syringeDisplayName))))
				{
					thePlayerExtended.reanimatingPlayername = null;
					thePlayerExtended.reanimationTick = 0;
					doctorExtendedPlayer.reanimatingPlayername = null;
					ServerUtils.sendMessage("§cRéanimation annulé vous n'avez plus votre seringue en main!", doctorPlayer, 1000, 1);
					return;
				}
				else if(doctorPlayer.getDistanceSqToEntity(thePlayer) > 5*5)
				{
					thePlayerExtended.reanimatingPlayername = null;
					thePlayerExtended.reanimationTick = 0;
					doctorExtendedPlayer.reanimatingPlayername = null;
					ServerUtils.sendMessage("§cRéanimation annulé ne vous eloignez pas de votre patient!", doctorPlayer, 1000, 1);
					return;
				}
				
				int reanimationPercentage = (int) ((thePlayerExtended.reanimationTick++ / (float)reanimationDurationTicks) * 100f);
	
				ServerUtils.sendMessage("§cRéanimation en cours : §a" + reanimationPercentage + "%" , doctorPlayer, 1000, 1);
				ServerUtils.sendMessage("§cRéanimation en cours : §a" + reanimationPercentage + "%" , thePlayer, 1000, 1);
	
				if(reanimationPercentage >= 100)
				{
					System.out.println("/eco take " + thePlayer.getCommandSenderName() + " 100");
					System.out.println("/eco give " + thePlayerExtended.reanimatingPlayername + " 100");
					
					ServerUtils.sendChatMessage(thePlayer, "§eLes frais d'hospitalisation vous en coûté §6100 euro");
					ServerUtils.sendChatMessage(doctorPlayer, "§eLa réanimation vous a rapporté §6100 euro");
	
					thePlayerExtended.reanimatingPlayername = null;
					doctorExtendedPlayer.reanimatingPlayername = null;
					
					thePlayerExtended.onReanimated();
					
					doctorPlayer.inventory.mainInventory[doctorPlayer.inventory.currentItem] = null;
					
					ServerUtils.sendMessage("§aVous avez survécu ! Vous pouvez vous lever.", thePlayer,1000,1);				
				}
			}
		}
		else if(thePlayerExtended.reanimatingPlayername != null) //if player is Doctor
		{
			EntityPlayer victimPlayer = world.getPlayerEntityByName(thePlayerExtended.reanimatingPlayername);
			if(victimPlayer == null)
			{
				ServerUtils.sendMessage("§cRéanimation annulé votre patient s'est déconnecté.", thePlayer, 1000, 1);
				thePlayerExtended.reanimatingPlayername = null;
			}
		}
		
	
	}
	

	public void placePlayerInReanimation(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
		World world = player.worldObj;
		WorldData worldData = WorldData.get(world);
		
		extendedPlayer.onReanimationEnter();
		
		if(worldData.hospitalBeds.size() == 0)
		{
			subitDeath(player);
			return;
		}
		
		ChunkCoordinates randomBed = (ChunkCoordinates)worldData.hospitalBeds.keySet().toArray()[(MathHelper.getRandomIntegerInRange(world.rand, 0, worldData.hospitalBeds.size()-1))];
		
		while(world.getBlock(randomBed.posX, randomBed.posY, randomBed.posZ) != Blocks.bed)
		{
			worldData.removeHospitalBed(randomBed);
			randomBed = (ChunkCoordinates)worldData.hospitalBeds.values().toArray()[(MathHelper.getRandomIntegerInRange(world.rand, 0, worldData.hospitalBeds.size()-1))];
		}
				
		ExtendedPlayer.forcePlayerSleep(player, randomBed.posX, randomBed.posY, randomBed.posZ, true);
		
		ServerUtils.broadcastMessageJob(world, "§5[§dRéanimation§5] Un patient doit être soigner d'urgence dans §d" + worldData.hospitalBeds.get(randomBed), "Medecin");
		//Envoyez message aux medecins depuis spigot.
	}
	
	public void subitDeath(EntityPlayer player)
	{
		ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
		
		extendedPlayer.onRespawn();
	
		player.setPositionAndUpdate(-1150, 65, -1146);
		
		ServerUtils.sendMessage("§cMort subite!", player, 1000, 1);
	}
	
	public String getLeftTimeDisplay(int leftTime)
	{
		
		if(leftTime < 0)
		{
			leftTime = 0;
		}
		
		int hours = (int) (leftTime  / 60 / 60); 
		int minutes = (int) (leftTime / 60) % 60;
		int seconds = (int)(leftTime % 60);


		String hoursStr = hours + "";
		if(hours <= 9)
		{
			hoursStr = "0" + hours;
		}
		
		String minutesStr = minutes + "";

		if(minutes <= 9)
		{
			minutesStr = "0" + minutes;
		}

		String secondsStr = seconds + "";

		if(seconds <= 9)
		{
			secondsStr = "0" + seconds;
		}
		
		return hoursStr + ":" + minutesStr + ":" + secondsStr;

	}
	
}
