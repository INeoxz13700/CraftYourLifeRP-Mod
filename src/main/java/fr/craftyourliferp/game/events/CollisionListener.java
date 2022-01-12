package fr.craftyourliferp.game.events;

import java.util.List;
import java.util.stream.Collectors;

import org.lwjgl.opengl.GL11;

import com.flansmod.common.PlayerHandler;
import com.flansmod.common.guns.raytracing.PlayerSnapshot;

import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.PlayerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.ServerTickEvent;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.entities.EntityItemCollider;
import fr.craftyourliferp.items.IItemCollider;
import fr.craftyourliferp.items.ModdedItems;
import fr.craftyourliferp.main.CraftYourLifeRPClient;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.main.WorldSelector;
import fr.craftyourliferp.utils.MathsUtils;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.OpenGlHelper;
import net.minecraft.client.renderer.RenderGlobal;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.Entity;
import net.minecraft.entity.IProjectile;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.InventoryPlayer;
import net.minecraft.entity.projectile.EntityArrow;
import net.minecraft.server.MinecraftServer;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;
import net.minecraftforge.client.event.RenderPlayerEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;

public class CollisionListener
{

	
	
	@SubscribeEvent
	@SideOnly(Side.CLIENT)
	public void playerTickEventClient(PlayerTickEvent event)
	{
		EntityPlayer player = event.player;
		if(player.worldObj.isRemote) 
		{
			if(Minecraft.getMinecraft().thePlayer == player)
			{
				
				
				if(player.getHeldItem() != null)
				{

					if(player.getHeldItem().getItem() == ModdedItems.itemBulletproofShield)
					{
						player.motionX *= 0.65;
						player.motionY *= 0.999985;
						player.motionZ *= 0.65;
						if(CraftYourLifeRPClient.cachedData.slotEquippedShield == -1)
						{
							if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1 || player.ticksExisted - CraftYourLifeRPClient.cachedData.ticksChangeSlot < 20*2)
							{
								if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1) CraftYourLifeRPClient.cachedData.ticksChangeSlot = player.ticksExisted;
								
								if(player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.previousSelectedSlot) != null &&  player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.previousSelectedSlot).getItem() == ModdedItems.itemBulletproofShield)
								{
									CraftYourLifeRPClient.cachedData.previousSelectedSlot++;
								}
								player.inventory.currentItem = CraftYourLifeRPClient.cachedData.previousSelectedSlot;
								event.player.addChatMessage(new ChatComponentText("§cVous devez attendre 2 secondes pour équipper le bouclier"));
							}
							else
							{
								CraftYourLifeRPClient.cachedData.slotEquippedShield = player.inventory.currentItem;
								CraftYourLifeRPClient.cachedData.ticksChangeSlot = -1;
							}
						}
					}
				}
				
				if(CraftYourLifeRPClient.cachedData != null && CraftYourLifeRPClient.cachedData.slotEquippedShield != -1)
				{
					if(player.getHeldItem() == null || player.getHeldItem().getItem() != ModdedItems.itemBulletproofShield)
					{
						if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1 || player.ticksExisted - CraftYourLifeRPClient.cachedData.ticksChangeSlot < 20*2)
						{
							if(CraftYourLifeRPClient.cachedData.ticksChangeSlot == -1) CraftYourLifeRPClient.cachedData.ticksChangeSlot = player.ticksExisted;
							player.inventory.currentItem = CraftYourLifeRPClient.cachedData.slotEquippedShield;
							event.player.addChatMessage(new ChatComponentText("§cVous devez attendre 2 secondes pour changer d'item"));
							
							if(player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.slotEquippedShield) == null || player.inventory.getStackInSlot(CraftYourLifeRPClient.cachedData.slotEquippedShield).getItem() != ModdedItems.itemBulletproofShield)
							{
								CraftYourLifeRPClient.cachedData.slotEquippedShield = -1;
								CraftYourLifeRPClient.cachedData.ticksChangeSlot = -1;
							}
						}
						else
						{
							CraftYourLifeRPClient.cachedData.slotEquippedShield = -1;
							CraftYourLifeRPClient.cachedData.ticksChangeSlot = -1;
						}
					}
				}
				
				if(CraftYourLifeRPClient.cachedData != null) CraftYourLifeRPClient.cachedData.previousSelectedSlot = player.inventory.currentItem;

			}
		}

	}
	
	@SubscribeEvent
	public void playerTickEventServer(PlayerTickEvent event)
	{
			EntityPlayer player = (EntityPlayer) event.player;
	
			if(player.worldObj.isRemote)
			{
				return;
			}
	
			//PlayerCachedData cachedData = PlayerCachedData.getData(player);
			ExtendedPlayer cachedData = ExtendedPlayer.get(player);

			
			if(player.getHeldItem() != null && player.getHeldItem().getItem() instanceof IItemCollider)
			{
				if(cachedData.isUsingItem() && cachedData.entityItemCollider == null)
				{
					cachedData.entityItemCollider = new EntityItemCollider(player.worldObj,player,(IItemCollider)player.getHeldItem().getItem());
					player.worldObj.spawnEntityInWorld(cachedData.entityItemCollider);
				}
			}
			
			if(!cachedData.isUsingItem() && cachedData.entityItemCollider != null)
			{
				cachedData.entityItemCollider.setDead();
				cachedData.entityItemCollider = null;
			}
				
	}

}

	

