package fr.craftyourliferp.halloween;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import cpw.mods.fml.relauncher.Side;
import fr.craftyourliferp.cosmetics.CosmeticObject;
import fr.craftyourliferp.guicomponents.UIButton;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;

public class HalloweenEvent {

	public static Item moneyItem;

	public long eventExpireTime;
	
	public static List<HalloweenItem> items = new ArrayList();
	
	public HalloweenEvent(Side side)
	{
		moneyItem = Item.getItemById(5174);

		try {
		    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		    eventExpireTime = sdf.parse("2020-11-01 00:00:00").getTime();
		} catch (ParseException e) {
			e.printStackTrace();
		}
		
		items.add(new HalloweenItem(new ItemStack(ModdedItems.witchHatCosmetic,1), 300, "ItemStack","§dCosmétique sorcière",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + "§6vient d'acheter x1 §dCosmétique sorcière"  , (byte)0);
					CosmeticObject.setCosmetiqueUnlocked(player, 4);
				}
			}
		}));
		
		items.add(new HalloweenItem(null, 350, "ItemStack","§4Cosmétique démon (à venir)",null));
		
		items.add(new HalloweenItem(null, 150, "ResourceLocation=craftyourliferp:gui/vehicles/vcoins.png","§b50 vcoins",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x50 §bVCoins"  , (byte)0);
					ExtendedPlayer.get(player).vCoins += 50;
				}
			}
		}));
		
		items.add(new HalloweenItem(null, 300, "ResourceLocation=craftyourliferp:gui/vehicles/vcoins.png","§b200 vcoins",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x200 §bVCoins"  , (byte)0);
					ExtendedPlayer.get(player).vCoins += 200;
				}
			}
		}));
		
		items.add(new HalloweenItem(new ItemStack(Item.getItemById(4958),1), 300, "ItemStack","§fCH-53K",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §fCH-53k"  , (byte)0);
					ServerUtils.addItemStacksToInventory(player, Arrays.asList(new ItemStack[] { new ItemStack(Item.getItemById(4958),1) }));
				}
			}
		}));
		
		items.add(new HalloweenItem(new ItemStack(Item.getItemById(4955),1), 280, "ItemStack","§fVH-46f",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §fVH-46f"  , (byte)0);
					ServerUtils.addItemStacksToInventory(player, Arrays.asList(new ItemStack[] { new ItemStack(Item.getItemById(4955),1) }));
				}
			}
		}));
		
		items.add(new HalloweenItem(new ItemStack(Item.getItemById(4953),1), 300, "ItemStack","§fF35b",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §fF35b"  , (byte)0);
					ServerUtils.addItemStacksToInventory(player, Arrays.asList(new ItemStack[] { new ItemStack(Item.getItemById(4953),1) }));
				}
			}
		}));
		
		items.add(new HalloweenItem(new ItemStack(Item.getItemById(332),1), 180, "ItemStack","§bColis stratégique",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §bColis stratégique"  , (byte)0);
					System.out.println("/colis give " + player.getCommandSenderName());
				}
			}
		}));
		
		items.add(new HalloweenItem(new ItemStack(Item.getItemById(332),1), 360, "ItemStack","§bColis stratégique",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x2 §bColis stratégique"  , (byte)0);
					System.out.println("/colis give " + player.getCommandSenderName());
					System.out.println("/colis give " + player.getCommandSenderName());
				}
			}
		}));
		
		items.add(new HalloweenItem(new ItemStack(Item.getItemById(4255),1), 500, "ItemStack","§cLicence Pilote",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §cLicence Pilote"  , (byte)0);
					ExtendedPlayer.get(player).hasLicence = true;
				}
			}
		}));
	
		items.add(new HalloweenItem(null, 150, "ResourceLocation=craftyourliferp:gui/halloween/vip.png","§f[§bVIP§f] 1 Semaine",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §f[§bVIP§f] 1 semaine"  , (byte)0);
					System.out.println("/rank add VIP " + player.getCommandSenderName() + " 604800");
				}
			}
		}));
		
		items.add(new HalloweenItem(null, 180, "ResourceLocation=craftyourliferp:gui/halloween/vip+.png","§f[§dVIP+§f] 1 Semaine",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §f[§dVIP+§f] 1 semaine"  , (byte)0);
					System.out.println("/rank add VIP+ " + player.getCommandSenderName() + " 604800");
				}
			}
		}));
		
		items.add(new HalloweenItem(new ItemStack(ModdedItems.birdCosmetic,1), 200, "ItemStack","§cCosmétique oiseau",new UIButton.CallBackObject()
		{
			@Override
			public void call(EntityPlayer player)
			{
				if(player != null)
				{
					ServerUtils.broadcastMessage("§6[§bHalloween-Event§6] " + player.getCommandSenderName() + " §6vient d'acheter x1 §cCosmétique oiseau"  , (byte)0);
					CosmeticObject.setCosmetiqueUnlocked(player, 0);
				}
			}
		}));
	}

	
	public class HalloweenItem
	{
		public Object itemType;
				
		public int price;
		
		public String texture;
		
		public String displayName;
		
		public UIButton.CallBackObject callback;
		
		public HalloweenItem(Object type, int price, String texture, String displayName, UIButton.CallBackObject callback)
		{
			this.itemType = type;
			this.price = price;
			this.texture = texture;
			this.displayName = displayName;
			this.callback = callback;
		}
		
		
	}
}


