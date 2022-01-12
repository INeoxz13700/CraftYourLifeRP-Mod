package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.flansmod.common.FlansMod;
import com.flansmod.common.network.PacketOpenGui;
import com.google.gson.Gson;

import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.entities.EntityLootableBody;
import fr.craftyourliferp.guicomponents.UIColor;
import fr.craftyourliferp.guicomponents.UIText;
import fr.craftyourliferp.items.MarketItem;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.network.PacketHandler;
import fr.craftyourliferp.network.PacketMessageDisplay;
import fr.craftyourliferp.network.PacketSleeping;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockBed;
import net.minecraft.block.BlockChest;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.inventory.IInventory;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.Vec3;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeChunkManager;
import net.minecraftforge.common.ForgeChunkManager.Ticket;
import net.minecraftforge.common.ForgeChunkManager.Type;
import fr.craftyourliferp.data.PlayerData;

public class CommandApi implements ICommand {

    	    	
    	public CommandApi()
    	{
    		 
    	}
    	
    	@Override
    	public int compareTo(Object arg0) {
    		return 0;
    	}

    	@Override
    	public String getCommandName() {
    		return "api";
    	}

    	@Override
    	public String getCommandUsage(ICommandSender var1) {
    		// TODO Auto-generated method stub
    		return "§c/api";
    	}

   
    	@Override
    	public void processCommand(ICommandSender sender, String[] args) {
    		World world = sender.getEntityWorld();
    		WorldData data = WorldData.get(world);
    		
    		if(!world.isRemote) 
    		{
    			if(args[0].equalsIgnoreCase("getplayerdata"))
    			{
    				EntityPlayer player = world.getPlayerEntityByName(args[1]);
    				if(player != null)
    				{
    					//PlayerCachedData playerTempData = PlayerCachedData.getData(player);
    					ExtendedPlayer playerTempData = ExtendedPlayer.get(player);

    					Gson gson = new Gson();
    					String json = "";
    					for(int i = 2; i < args.length; i++)
    					{
    						json += args[i] + " ";
    					}
    					json = json.trim();
    					
    					if(playerTempData == null)
    					{
    						return;
    					}
	    					
    					playerTempData.serverData = gson.fromJson(json, PlayerData.class);
	    					
	    					
	    				if(playerTempData.openPage() != null)
	    				{
	    					playerTempData.openPage().updatePageData();
	    				}
    					
    				}
    			}
    			else if(args[0].equalsIgnoreCase("cadavre"))
    			{
    				if(args[1].equalsIgnoreCase("spawn"))
    				{
    					String playerName = args[2];
    					String[] coordinatesStr = args[3].split(",");
    					Vec3 coordinates = Vec3.createVectorHelper(Double.parseDouble(coordinatesStr[0]), Double.parseDouble(coordinatesStr[1]), Double.parseDouble(coordinatesStr[2]));
						
    					EntityPlayer player = world.getPlayerEntityByName(playerName);
    					
    					if(player == null) return;
    					
    					float rotation = player.getRotationYawHead();
						
						EntityLootableBody corpse = new EntityLootableBody(player.worldObj);
									
						corpse.setOwner(player);
						corpse.setPositionAndRotation(coordinates.xCoord, coordinates.yCoord, coordinates.zCoord, rotation, 0);
							
						player.worldObj.spawnEntityInWorld(corpse);
						corpse.setRotation(rotation);
    				}
    			}
    			else if(args[0].equalsIgnoreCase("respawn"))
    			{
    				String playerName = args[1];
    				
					EntityPlayer player = world.getPlayerEntityByName(playerName);

					if(player == null) return;
					
					ExtendedPlayer.get(player).onRespawnPlayerSpigot();
    			}
    			else if(args[0].equalsIgnoreCase("explosion"))
    			{
    				
    				if(!(sender instanceof EntityPlayer)) return;
    				
    				if(args[1].equalsIgnoreCase("create"))
    				{
    					if(args[2].isEmpty())
    					{
    						return;
    					}
    					
						sender.addChatMessage(new ChatComponentText("§4Region created!"));
						sender.addChatMessage(new ChatComponentText("§4Please do selection!"));

						//PlayerCachedData cached = PlayerCachedData.getData((EntityPlayer)sender);
    					ExtendedPlayer cached = ExtendedPlayer.get((EntityPlayer)sender);

						cached.newRegionBuilder(args[2]);
						cached.builderType = 1;

    				}
    				else if(args[1].equalsIgnoreCase("delete"))
    				{
    					if(args[2].isEmpty())
    					{
    						return;
    					}
    					
    					
    					if(!data.removeExplosionsRegion(args[2]))
    					{
    						sender.addChatMessage(new ChatComponentText("§4La région n'existe pas!"));
    					}
    					else
    					{
    						sender.addChatMessage(new ChatComponentText("§4La région a été supprimé"));
    					}
    					
    				}
    				else if(args[1].equalsIgnoreCase("list"))
    				{
    					sender.addChatMessage(new ChatComponentText(""));
    					sender.addChatMessage(new ChatComponentText("§2Liste des regions : "));
    					
    					List<WorldSelector> regions = data.getExplosions();
    					
    					for(int i = 0; i < regions.size(); i++)
    					{
    						WorldSelector region = regions.get(i);
        					sender.addChatMessage(new ChatComponentText("§b- " + region.getName()));
    					}


    				}
    			}
    			else if(args[0].equalsIgnoreCase("fire"))
    			{
    				
    				if(!(sender instanceof EntityPlayer)) return;
    				
    				if(args[1].equalsIgnoreCase("create"))
    				{
    					if(args[2].isEmpty())
    					{
    						return;
    					}
    					
						sender.addChatMessage(new ChatComponentText("§4Region created!"));
						sender.addChatMessage(new ChatComponentText("§4Please do selection!"));

						//PlayerCachedData cachedData = PlayerCachedData.getData((EntityPlayer)sender);
						ExtendedPlayer cachedData = ExtendedPlayer.get((EntityPlayer)sender);
						cachedData.newRegionBuilder(args[2]);
						cachedData.builderType = 0;
    				}
    				else if(args[1].equalsIgnoreCase("delete"))
    				{
    					if(args[2].isEmpty())
    					{
    						return;
    					}
    					
    					
    					if(!data.removeFireRegion(args[2]))
    					{
    						sender.addChatMessage(new ChatComponentText("§4La région n'existe pas!"));
    					}
    					else
    					{
    						sender.addChatMessage(new ChatComponentText("§4La région a été supprimé"));
    					}
    					
    				}
    				else if(args[1].equalsIgnoreCase("list"))
    				{
    					sender.addChatMessage(new ChatComponentText(""));
    					sender.addChatMessage(new ChatComponentText("§2Liste des regions : "));
    					
    					List<WorldSelector> regions = data.getFires();
    					
    					for(int i = 0; i < regions.size(); i++)
    					{
    						WorldSelector region = regions.get(i);
        					sender.addChatMessage(new ChatComponentText("§b- " + region.getName()));
    					}


    				}
    			}
    			else if(args[0].equalsIgnoreCase("capture"))
    			{
    				if(!(sender instanceof EntityPlayer)) return;
    				
    				if(args[1].equalsIgnoreCase("create"))
    				{
    					if(args[2].isEmpty() || args[3].isEmpty())
    					{
    						return;
    					}
    					
    					int captureType = 0;
    					try
    					{
    						captureType = Integer.parseInt(args[3]);
    					}
    					catch(Exception e)
    					{
    						ServerUtils.sendChatMessage(sender, "§cEntrez un type de capture (un entier)");
    						return;
    					}
    					
						sender.addChatMessage(new ChatComponentText("§4Region created!"));
						sender.addChatMessage(new ChatComponentText("§4Please do selection!"));

						ExtendedPlayer cachedData = ExtendedPlayer.get((EntityPlayer)sender);
						cachedData.newRegionBuilder(args[2]);
						cachedData.builderType = 2;
						cachedData.captureType = (byte) captureType;
    				}
    				else if(args[1].equalsIgnoreCase("delete"))
    				{
    					if(args[2].isEmpty())
    					{
    						return;
    					}
    					
    					
    					if(!data.removeCaptureRegion(args[2]))
    					{
    						sender.addChatMessage(new ChatComponentText("§4La région n'existe pas!"));
    					}
    					else
    					{
    						sender.addChatMessage(new ChatComponentText("§4La région a été supprimé"));
    					}
    					
    				}
    				else if(args[1].equalsIgnoreCase("list"))
    				{
    					sender.addChatMessage(new ChatComponentText(""));
    					sender.addChatMessage(new ChatComponentText("§2Liste des regions : "));
    					
    					Iterator<WorldSelector> iterator = data.getCaptures().keySet().iterator();
    					
    					while(iterator.hasNext())
    					{
    						WorldSelector region = iterator.next();
        					sender.addChatMessage(new ChatComponentText("§b- " + region.getName()));
    					}
    				}
    			}
    			else if(args[0].equalsIgnoreCase("menotte"))
    			{
    				String playerName = args[1];
    				
    				boolean menotte = Boolean.parseBoolean(args[2]);
    				
					EntityPlayer player = world.getPlayerEntityByName(playerName);

					if(player == null) return;
					
					//PlayerCachedData cachedData = PlayerCachedData.getData(player);
					ExtendedPlayer cachedData = ExtendedPlayer.get(player);

					if(menotte)
					{
						cachedData.currentAnimation = 2;
					}
					else
					{
						cachedData.currentAnimation = 0;
					}
					ExtendedPlayer.get(player).updateRendererDatas();
    			}
    			else if(args[0].equalsIgnoreCase("setshield"))
    			{
    				EntityPlayer player = world.getPlayerEntityByName(args[1]);
    				if(player == null) return;
    				
    				float shieldAmount = Float.parseFloat(args[2]);
    				
    				ExtendedPlayer.get(player).shield.setShield(shieldAmount);
    			}
    			else if(args[0].equalsIgnoreCase("getUsername"))
    			{
    				if(args.length < 2)
    				{
    					ServerUtils.sendChatMessage(sender, "§cMauvaise commande : /api getUsername <id>");
    					return;
    				}
    				String base64String = args[1];
    				ServerUtils.sendChatMessage(sender, "§6Pseudo du joueur : §e" + new String(Base64.getDecoder().decode(base64String)));
    			}
    			else if(args[0].equalsIgnoreCase("bitcoin"))
    			{
    				if(args.length >= 3)
    				{
    					EntityPlayer player = world.getPlayerEntityByName(args[2]);
    					
    					if(player == null)
    					{
    						ServerUtils.sendChatMessage(sender, "§cCe joueur n'est pas connecté");
    						return;
    					}
    					
    					ExtendedPlayer extendedPlayer = ExtendedPlayer.get(player);
    							
        				if(args[1].equalsIgnoreCase("set"))
        				{
        					float bitcoin;
        					try
        					{
        						bitcoin = Float.parseFloat(args[3]);
        					}
        					catch(Exception e)
        					{
        						ServerUtils.sendChatMessage(sender, "§cEntrez une valeur décimal");
        						return;
        					}
        					extendedPlayer.bitcoin = bitcoin;
        					ServerUtils.sendChatMessage(sender, "§aVous avez mis le solde en bitcoin du joueur à : ");
        					ServerUtils.sendChatMessage(sender, "§a" + ServerUtils.getMoneyDisplay("%f",bitcoin) + " BTC");
        				}
        				else if(args[1].equalsIgnoreCase("count"))
        				{
        					ServerUtils.sendChatMessage(sender, "§cBitcoin du joueur : " +  ServerUtils.getMoneyDisplay("%f",extendedPlayer.bitcoin));
        					ServerUtils.sendChatMessage(sender, "§cEn euro : " + ServerUtils.getMoneyDisplay("%.2f",(extendedPlayer.bitcoin*data.getBitcoinPrice())));

        				}
    				}
    			}
    			else if(args[0].equalsIgnoreCase("ethylic"))
    			{
    				if(args.length >= 6)
    				{
    					EntityPlayer doctorPlayer = world.getPlayerEntityByName(args[1]);
    					EntityPlayer victimPlayer = world.getPlayerEntityByName(args[2]);
    					int x = Integer.parseInt(args[3]);
    					int y = Integer.parseInt(args[4]);
    					int z = Integer.parseInt(args[5]);
    					
    					if(doctorPlayer == null || victimPlayer == null) 
    					{
    						ServerUtils.sendChatMessage(doctorPlayer, "§cJoueur introuvable");
    						return;
    					}

    					ExtendedPlayer expVictim = ExtendedPlayer.get(victimPlayer);
    					if(expVictim.shouldBeInEthylicComa())
    					{
    						Block block = doctorPlayer.worldObj.getBlock(x, y, z);
    						if(block instanceof BlockBed)
    						{	   
    							System.out.println(expVictim.reanimatorPlayerName);
    							if(expVictim.reanimatorPlayerName == null || expVictim.reanimatorPlayerName.isEmpty())
    							{
           							if(ExtendedPlayer.forcePlayerSleep(victimPlayer, x, y, z))
        							{
            							expVictim.reanimatorPlayerName = doctorPlayer.getCommandSenderName();
            							ServerUtils.sendChatMessage(victimPlayer, "§eLes frais d'hospitalisation vous ont coûté §6100 euro");
        								ServerUtils.sendChatMessage(doctorPlayer, "§eVous avez reçu §6100 euro!");
        								ServerUtils.addMoney(doctorPlayer, 100F);
        								ServerUtils.takeMoney(victimPlayer, 100F);
        							}
        							else
        							{
        								ServerUtils.sendChatMessage(doctorPlayer, "§cCe lit est occupé");
        							}
    							}
    							else
    							{
    								ServerUtils.sendChatMessage(doctorPlayer, "§cCe joueur est déjà en réanimation");
    							}
    						}
    					}
    					else
    					{
    						ServerUtils.sendChatMessage(doctorPlayer, "§cCe joueur n'a pas d'intoxication d'alcool");
    					}
    					
    					
    				}
    			}
    			/*else if(args[0].equalsIgnoreCase("effect"))
    			{
    				String playerName = args[1];
    				
    				EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(playerName);
    				
    				if(player == null)
    				{
    					ServerUtils.sendChatMessage((EntityPlayer)sender, "§cJoueur introuvable");
    					return;
    				}
    				
    				int id = 0;
    				int timeInSeconds = 0;
    				try
    				{
    					id = Integer.parseInt(args[2]);
    					timeInSeconds = Integer.parseInt(args[3]);
    				}
    				catch(Exception e)
    				{
    					e.printStackTrace();
    				}
    				
    				Effect effect = Effect.getEffect(id);
    				
    				if(effect != null)
    				{
    					if(effect.getEffectSong() != null)
    					{
    						sender.getEntityWorld().playSoundAtEntity(player, effect.getEffectSong(), 1.0F, 1.0F);
    					}
    					CraftYourLifeRPMod.packetHandler.sendTo(PacketEffect.displayEffect(id, timeInSeconds), (EntityPlayerMP)player);
    				}
    			}
    			else if(args[0].equalsIgnoreCase("displaygui"))
    			{
    				String playerName = args[1];
    				
    				EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(playerName);
    				
    				if(player == null)
    				{
    					ServerUtils.sendChatMessage((EntityPlayer)sender, "§cJoueur introuvable");
    					return;
    				}
    				
    				FlansMod.packetHandler.sendTo(new PacketOpenGui((byte)1), (EntityPlayerMP)player);
    			}*/

    		}
    	}

    	@Override
    	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
    		return true;
    	}

		@Override
		public List getCommandAliases() {
			return null;
		}

		@Override
		public List addTabCompletionOptions(ICommandSender p_71516_1_, String[] p_71516_2_) {
			return null;
		}

		@Override
		public boolean isUsernameIndex(String[] p_82358_1_, int p_82358_2_) {
			return false;
		}


}
