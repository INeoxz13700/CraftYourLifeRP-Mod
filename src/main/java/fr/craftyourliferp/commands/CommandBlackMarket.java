package fr.craftyourliferp.commands;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.items.MarketItem;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.command.ICommand;
import net.minecraft.command.ICommandSender;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.ChatComponentText;
import net.minecraft.world.World;

public class CommandBlackMarket implements ICommand {

    	private final List aliases;
    	    	
    	public CommandBlackMarket() {
    		aliases = new ArrayList();
    		aliases.add("bm");
    	}
    	
    	@Override
    	public int compareTo(Object arg0) {
    		return 0;
    	}

    	@Override
    	public String getCommandName() {
    		return "blackmarket";
    	}

    	@Override
    	public String getCommandUsage(ICommandSender var1) {
    		// TODO Auto-generated method stub
    		return "§c/blackmarket";
    	}

    	@Override
    	public List getCommandAliases() {
    		return this.aliases;
    	}
    	
    	private void displayCommands(ICommandSender sender)
    	{
    		sender.addChatMessage(new ChatComponentText("§c/blackmarket add <ItemId> <UnityPriceInEuro> <Probability> <DisplayName> "));
    		sender.addChatMessage(new ChatComponentText("§c/blackmarket remove <ItemId> <Probability>"));
    		sender.addChatMessage(new ChatComponentText("§c/blackmarket list"));
    		sender.addChatMessage(new ChatComponentText("§c/blackmarket update"));
    		sender.addChatMessage(new ChatComponentText("§c/blackmarket give item <Username>"));


    	}

    	@Override
    	public void processCommand(ICommandSender sender, String[] args) {
    		World world = sender.getEntityWorld();
    		WorldData data = WorldData.get(world);
    		if(!world.isRemote) {
    			
				if(!canCommandSenderUseCommand(sender))
				{
					sender.addChatMessage(new ChatComponentText("§cPermission Error"));
					return;
				}
    			
    			if(args.length >= 4)
    			{
    				if(args[0].equalsIgnoreCase("add"))
    				{
    					
    					
    					String id = args[1];
    					float priceInEuro;
    					
    					try
    					{
    						priceInEuro = Float.parseFloat(args[2].replace(",", "."));
    					}
    					catch(Exception e)
    					{
    						sender.addChatMessage(new ChatComponentText("§cEntrez un nombre (peut contenir une virgule)"));
    						return;
    					}
    					
    					int probability;
    					
    					try
    					{
    						probability = Integer.parseInt(args[3]);
    					}
    					catch(Exception e)
    					{
    						sender.addChatMessage(new ChatComponentText("§cEntrez un nombre"));
    						return;
    					}
    					
    					if(args.length == 4)
    					{
    	  					if(data.addItemToBlackMarket(id,priceInEuro, probability)) sender.addChatMessage(new ChatComponentText("§aItem successfully added !"));
        					else sender.addChatMessage(new ChatComponentText("§aItem already exist !"));		
    					}
    					else
    					{
    						String displayName = "";
    						for(int i = 4; i < args.length; i++)
        					{
        						displayName += args[i] + " ";
        					}
        					displayName = displayName.trim();
        					
    	  					if(data.addItemToBlackMarket(id,priceInEuro, probability,displayName)) sender.addChatMessage(new ChatComponentText("§aItem successfully added !"));
        					else sender.addChatMessage(new ChatComponentText("§aItem already exist !"));
    					}
    					
  
    				}
    			}
    			else if(args.length == 3)
    			{
    				if(args[0].equalsIgnoreCase("remove"))
    				{

    					
    					
    					String id = args[1];
    					
    					
    					int probability;
    					
    					try
    					{
    						probability = Integer.parseInt(args[2]);
    					}
    					catch(Exception e)
    					{
    						sender.addChatMessage(new ChatComponentText("§cEntrez un nombre"));
    						return;
    					}
    					
    					
    					if(data.removeItemFromBlackMarket(id, probability)) sender.addChatMessage(new ChatComponentText("§aItem successfully removed !"));
    					else sender.addChatMessage(new ChatComponentText("§aItem not found !"));
    				}
    				else if(args[0].equalsIgnoreCase("give"))
    				{
    					if(args[1].equalsIgnoreCase("item"))
    					{
    						if(args[2].isEmpty())
    						{
    	    					sender.addChatMessage(new ChatComponentText("§aEnter username !"));
    						}
    						else 
    						{
    							EntityPlayer player = sender.getEntityWorld().getPlayerEntityByName(args[2]);
    							
    							if(player == null)
    							{
        	    					sender.addChatMessage(new ChatComponentText("§aPlayer not found !"));
        	    					return;
    							}
    							
    							ExtendedPlayer ep = ExtendedPlayer.get(player);
    							List<ItemStack> itemsToRemove = new ArrayList();
    							
    							if(ep.itemStockage.isEmpty())
    							{
									player.addChatMessage(new ChatComponentText("§cStockage vide"));
    								return;
    							}
    							
    							for(ItemStack is : ep.itemStockage)
    							{
    								if(!player.inventory.addItemStackToInventory(is))
    								{
    									if(player.inventory.getFirstEmptyStack() >= 0)
    									{
    										itemsToRemove.add(is);
    									}
    									else
    									{
    										player.addChatMessage(new ChatComponentText("§cVotre inventaire est plein"));
        									
        									return;
    									}
    								}
    								else 
    								{
    									itemsToRemove.add(is);
    								}
    							}
    							ep.itemStockage.removeAll(itemsToRemove);
    							
								player.addChatMessage(new ChatComponentText("§aStockage récupéré en entier"));
    						}
    					}
    				}

    			}
    			else if(args.length == 1)
    			{
    				if(args[0].equalsIgnoreCase("list"))
    				{
    					
    					sender.addChatMessage(new ChatComponentText("§aRegistered Items :"));
    					sender.addChatMessage(new ChatComponentText(""));

    					
    					HashMap<MarketItem, Integer> items = data.getItemStackProbability().getRegisteredItems();
    				    Iterator it = items.entrySet().iterator();
    				    while (it.hasNext()) {
    				        Map.Entry pair = (Map.Entry)it.next();
    				        
    				        MarketItem marketItem = (MarketItem) pair.getKey();
    				        int probability = (int)pair.getValue();
        					sender.addChatMessage(new ChatComponentText("- " + marketItem.getItem().getDisplayName() + " " + marketItem.getPriceInEuro() + " euro " + probability + "%"));
    				    }
    				}
    				else if(args[0].equalsIgnoreCase("update"))
    				{
    					data.updateMarket();
    					
      					sender.addChatMessage(new ChatComponentText("Market updated"));

    				}
    			}
    			else if(args.length == 0)
    			{
    				displayCommands(sender);	
    			}
    			else
    			{
					sender.addChatMessage(new ChatComponentText(getCommandUsage(sender)));
    			}
    		}
    	}
    	

    	@Override
    	public boolean canCommandSenderUseCommand(ICommandSender p_71519_1_) {
    		if(p_71519_1_ instanceof EntityPlayer)
    		{
	    		EntityPlayer player = (EntityPlayer)p_71519_1_;
	    		if(ServerUtils.isOp(player)) 
	    		{
	    			return true;
	    		}
	    		else
	    		{
	    			return false;
	    		}
    		}
    		return true;
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

