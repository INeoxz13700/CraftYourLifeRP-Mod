package fr.craftyourliferp.items;

import java.util.Random;

import javax.vecmath.Vector3f;

import com.flansmod.common.driveables.EntityDriveable;
import com.flansmod.common.driveables.EntityWheel;

import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketExtinguisher;
import fr.craftyourliferp.utils.WorldUtils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.EnumAction;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.MathHelper;
import net.minecraft.util.MovingObjectPosition;
import net.minecraft.util.Vec3;
import net.minecraft.world.World;

public class Extincteur extends Item implements IItemPress{
	
    private int maxItemUseDuration;

	
	public Extincteur()
	{
		setMaxStackSize(1);
		setMaxDamage(1200);
		setCreativeTab(CraftYourLifeRPMod.CRPCreativeTabs);
	}

	@Override
	public void onItemRightClicked(EntityPlayer player, World worldObj, ItemStack heldItem) 
	{

	}

	@Override
	public void onItemUsing(EntityPlayer player, World worldObj, ItemStack is, int itemPressedTicksCount)
	{
		
		//PlayerCachedData data = PlayerCachedData.getData(player);

		if(player.getHeldItem().getItem() == this)
		{
			if(player.worldObj.isRemote)
			{
				if(Minecraft.getMinecraft().thePlayer == player)
				{
					Vec3 dir = player.getLookVec();
						
			    	player.worldObj.spawnParticle("cloud", player.posX-0.5f+dir.xCoord, player.posY-0.2+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY-0.2+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY-0.2+dir.yCoord, player.posZ+0.5+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY-0.4+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    		
			    	if(Minecraft.getMinecraft().thePlayer.ticksExisted % 10 == 0)
			    	{
				    	
				    	if(worldObj.isRemote)
				    	{
					    	MovingObjectPosition raytrace =  WorldUtils.getMouseOver(10.0D, 0.0F);

					    	if(raytrace == null)
							{
								return;
							}
					    	
					    	if(raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.ENTITY)
							{
								if(worldObj.isRemote)
								{
									Entity entity = raytrace.entityHit;
									if(entity.isBurning())
									{
										int rand = MathHelper.getRandomIntegerInRange(new Random(), 0, 100);
										if(rand >= 40 && rand <= 60)
										{
											PacketExtinguisher packet = new PacketExtinguisher(entity);
											CraftYourLifeRPMod.packetHandler.sendToServer(packet);
											
											if(entity instanceof EntityDriveable)
											{
												for(EntityWheel wheel : ((EntityDriveable) entity).wheels)
												{
													wheel.extinguish();
												}
											}
										}
									}
								}
							}
					    	else if(raytrace.typeOfHit == MovingObjectPosition.MovingObjectType.BLOCK)
					    	{
					    		 Block block = player.worldObj.getBlock(raytrace.blockX, raytrace.blockY, raytrace.blockZ);
					              
					              
					    		   int rand = MathHelper.getRandomIntegerInRange(new Random(), 0, 5);
						           if (block == Blocks.fire)
						           {
						              if (rand >= 1 && rand <= 4)
						              {
						                PacketExtinguisher packet = new PacketExtinguisher(raytrace.blockX, raytrace.blockY, raytrace.blockZ);
						                CraftYourLifeRPMod.packetHandler.sendToServer(packet);
						              }  
						           }
						              
						           block = player.worldObj.getBlock(raytrace.blockX, raytrace.blockY + 1, raytrace.blockZ);
						             
						           if (block == Blocks.fire)
						           {
							             if (rand >= 1 && rand <= 4)
							             {
							                PacketExtinguisher packet = new PacketExtinguisher(raytrace.blockX, raytrace.blockY + 1, raytrace.blockZ);
							                CraftYourLifeRPMod.packetHandler.sendToServer(packet);
							             }  
						           }
					              
					    	}
				    	}
						
			    	}
				}
				else
				{
					Vec3 dir = player.getLookVec();
						
			    	player.worldObj.spawnParticle("cloud", player.posX-0.5f+dir.xCoord, player.posY+player.eyeHeight-0.2+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY+player.eyeHeight+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY+player.eyeHeight-0.2+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY+player.eyeHeight-0.2+dir.yCoord, player.posZ+0.5+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
			    	player.worldObj.spawnParticle("cloud", player.posX+dir.xCoord, player.posY+player.eyeHeight-0.4+dir.yCoord, player.posZ+dir.zCoord, dir.xCoord*0.6f, dir.yCoord*1f, dir.zCoord*0.6f);
				}
			}
			else
			{
				if(player.ticksExisted % 5 == 0)
				{
					if(!is.hasTagCompound())
					{
						is.stackTagCompound = new NBTTagCompound();	
						is.stackTagCompound.setInteger("LifeTime", getMaxDamage());
					}
					
					is.stackTagCompound.setInteger("LifeTime", is.stackTagCompound.getInteger("LifeTime")-(int)2);
					if(is.stackTagCompound.getInteger("LifeTime") <= 0)
					{
						int index = player.inventory.currentItem;
						player.inventory.setInventorySlotContents(index, null);
					}
			    	player.worldObj.playSoundAtEntity(player, "craftyourliferp:extinguisher", 1.0F, 1.0F);
				}
			}
		}
	}

	@Override
	public void onItemStopUsing(EntityPlayer player, World worldObj, ItemStack heldItem, int itemPressedTicksCount)
	{
		
	}








}
