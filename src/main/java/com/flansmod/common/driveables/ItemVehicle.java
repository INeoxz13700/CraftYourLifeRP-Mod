package com.flansmod.common.driveables;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Collections;
import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.block.BlockLiquid;
import net.minecraft.block.BlockSponge;
import net.minecraft.block.material.Material;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItemFrame;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemMapBase;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.*;
import net.minecraft.util.MovingObjectPosition.MovingObjectType;
import net.minecraft.world.World;

import com.flansmod.common.FlansMod;

import com.flansmod.common.parts.PartType;
import com.flansmod.common.types.EnumType;
import com.flansmod.common.types.IFlanItem;
import com.flansmod.common.types.InfoType;

import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.data.PlayerCachedData;
import fr.craftyourliferp.data.WorldData;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.utils.ServerUtils;

public class ItemVehicle extends ItemMapBase implements IFlanItem
{
	public VehicleType type;
	
	public IIcon[] icons;

    public ItemVehicle(VehicleType type1)
    {
        maxStackSize = 1;
		type = type1;
		type.item = this;
		setCreativeTab(FlansMod.tabFlanDriveables);
		GameRegistry.registerItem(this, type.shortName, FlansMod.MODID);
    }

	@Override
	/** Make sure client and server side NBTtags update */
	public boolean getShareTag()
	{
		return true;
	}

	private NBTTagCompound getTagCompound(ItemStack stack, World world)
	{
		if(stack.stackTagCompound == null)
		{

			if(!world.isRemote)
				stack.stackTagCompound = getOldTagCompound(stack, world);
			
			
			if(stack.stackTagCompound == null)
			{

				stack.stackTagCompound = new NBTTagCompound();
				stack.stackTagCompound.setString("Type", type.shortName);
				stack.stackTagCompound.setString("Engine", PartType.defaultEngines.get(EnumType.vehicle).shortName);
			}
			
		}
		return stack.stackTagCompound;
	}

	private NBTTagCompound getOldTagCompound(ItemStack stack, World world)
    {
		try
		{
			File file1 = world.getSaveHandler().getMapFileFromName("vehicle_" + stack.getItemDamage());
	        FileInputStream fileinputstream = new FileInputStream(file1);
	        NBTTagCompound tags = CompressedStreamTools.readCompressed(fileinputstream).getCompoundTag("data");
	    	for(EnumDriveablePart part : EnumDriveablePart.values())
	    	{
	    		tags.setInteger(part.getShortName() + "_Health", type.health.get(part) == null ? 0 : type.health.get(part).health);
	    		tags.setBoolean(part.getShortName() + "_Fire", false);
	    	}
	        fileinputstream.close();
	        return tags;
		}
		catch(IOException e)
		{
			FlansMod.log("Failed to read old vehicle file");
			e.printStackTrace();
			return null;
		}
    }

	@Override
    public void addInformation(ItemStack stack, EntityPlayer player, List lines, boolean advancedTooltips)
	{
		if(!type.packName.isEmpty())
		{
			lines.add("\u00a7o" + type.packName);
		}
		if(type.description != null)
		{
            Collections.addAll(lines, type.description.split("_"));
		}

		lines.add("");
		NBTTagCompound tags = getTagCompound(stack, player.worldObj);
		PartType engine = PartType.getPart(tags.getString("Engine"));
		if(engine != null)
			lines.add("\u00a79Engine" + "\u00a77: " + engine.name);
	}

    @Override
	public ItemStack onItemRightClick(ItemStack itemstack, World world, EntityPlayer entityplayer)
    {    	
    	if(!world.isRemote)
    	{
	    		//Raytracing
		        float cosYaw = MathHelper.cos(-entityplayer.rotationYaw * 0.01745329F - 3.141593F);
		        float sinYaw = MathHelper.sin(-entityplayer.rotationYaw * 0.01745329F - 3.141593F);
		        float cosPitch = -MathHelper.cos(-entityplayer.rotationPitch * 0.01745329F);
		        float sinPitch = MathHelper.sin(-entityplayer.rotationPitch * 0.01745329F);
		        double length = 5D;
		        Vec3 posVec = Vec3.createVectorHelper(entityplayer.posX, entityplayer.posY + 1.62D - entityplayer.yOffset, entityplayer.posZ);
		        Vec3 lookVec = posVec.addVector(sinYaw * cosPitch * length, sinPitch * length, cosYaw * cosPitch * length);
		        MovingObjectPosition movingobjectposition = world.rayTraceBlocks(posVec, lookVec, type.placeableOnWater);
		        EntityVehicle vehicle = null;
		        
		        //Result check
		        if(movingobjectposition == null)
		        {
		        	entityplayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + " le v\u00E9hicule n a pas pu etre plac\u00E9 vous devez viser le block sur lequel vous voulez le plac\u00E9"));
		            return itemstack;
		        }
		        
		        if(movingobjectposition.typeOfHit == MovingObjectType.BLOCK)
		        {
		            int i = movingobjectposition.blockX;
		            int j = movingobjectposition.blockY;
		            int k = movingobjectposition.blockZ;
		            Block block = world.getBlock(i, j, k);      
		              
		            if(type.placeableOnWater)
		            {
			            if(block == Blocks.water || block == Blocks.flowing_water)
			            {
				            if(!world.isRemote) 
				            {
					            vehicle = new EntityVehicle(world, (double) i + 0.5F, (double) j + 2.5F, (double) k + 0.5F,entityplayer, type, getData(itemstack, world),itemstack);
					            vehicle.attribuatedItemStack = itemstack;
	
					            if(itemstack.hasTagCompound()) vehicle.driveableData.writeToNBT(itemstack.stackTagCompound);
					            
					            WorldData data = WorldData.get(world);
					            

					            data.driveablesManager.removeDriveablesFromSameType(vehicle, entityplayer);
					           
					            
					            if(!ExtendedPlayer.get(entityplayer).vehicleExploded(Item.getIdFromItem(itemstack.getItem())))
					            {
						            data.driveablesManager.addDriveable(entityplayer, vehicle);
					            }
					            else
					            {
					            	ServerUtils.sendChatMessage(entityplayer, "§cPayez les frais d'indemnisation de votre véhicule");
					            }
					            
					            					            
					              
					        	if(!entityplayer.capabilities.isCreativeMode)
								{
									itemstack.stackSize--;
								}
					            
					            saveVehicle(vehicle,entityplayer,itemstack);
					            
					            vehicle.driveableData.fuel = null;
					            if(vehicle.driveableData.cargo != null)
					            {
						            for(int d = 0; d < vehicle.driveableData.cargo.length; d++)
						            {
						            	vehicle.driveableData.cargo[d] = null;
						            }
					            }
				            }
			            }
			            else
			            {
		            		entityplayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Les bateaux peuvent seulement être placé sur l'eau"));
			            }
			            
			            return itemstack;
		            }
		            
		            if(type.placeableOnLand)
		            {
		            	if(block == Blocks.bedrock || block == Block.getBlockById(993) || block == Block.getBlockById(998) || block == Block.getBlockById(1000))
		            	{
				            if(!world.isRemote)
				            {
				            	if(this.getBlockInHeight(world, Vec3.createVectorHelper(i, j, k)) <= 0 && movingobjectposition.sideHit == 1)
				            	{
						            vehicle = new EntityVehicle(world, (double) i + 0.5F, (double) j + 2.5F, (double) k + 0.5F, entityplayer, type, getData(itemstack, world),itemstack);			            
						            vehicle.attribuatedItemStack = itemstack;
		
						            if(itemstack.hasTagCompound())
						            {
						            	vehicle.driveableData.writeToNBT(itemstack.stackTagCompound);
						            }
						            
						            WorldData data = WorldData.get(world);
						            data.driveablesManager.removeDriveablesFromSameType(vehicle, entityplayer);
						           
						            if(!ExtendedPlayer.get(entityplayer).vehicleExploded(Item.getIdFromItem(itemstack.getItem())))
						            {
							            data.driveablesManager.addDriveable(entityplayer, vehicle);
						            }
						            else
						            {
						            	ServerUtils.sendChatMessage(entityplayer, "§cPayez les frais d'indemnisation de votre véhicule");
						            }
						            
						            if(!entityplayer.capabilities.isCreativeMode)
									{
										 itemstack.stackSize--;
									}
						            
						            saveVehicle(vehicle,entityplayer,itemstack);
						            
						            vehicle.driveableData.fuel = null;
						            if(vehicle.driveableData.cargo != null)
						            {
							            for(int d = 0; d < vehicle.driveableData.cargo.length; d++)
							            {
							            	vehicle.driveableData.cargo[d] = null;
							            }
						            }
						            
						            
				            	}
				            	else
				            	{
				            		entityplayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Tentative d'usebug action bloqué."));
				            	}
				            	
				             }
							
		
		            	}
			            else
			            {
		            		entityplayer.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Les voitures peuvent seulement être placé sur la route"));
			            }
		            	
		            	return itemstack;
		            }
		        }
    	}
        return itemstack;
    }
    
    private void saveVehicle(EntityVehicle vehicle, EntityPlayer player, ItemStack is)
    {
    	if(vehicle.getDriveableData() != null && vehicle.driveableType != null && (vehicle.getDriveableData().ownerName.equalsIgnoreCase("") || vehicle.getDriveableData().ownerName == null)) 
		{
			DriveableType type = vehicle.getDriveableType();
			VehicleType vType = (VehicleType) type;
			DriveableData data = vehicle.getDriveableData();
				
			ExtendedPlayer clickedPData = ExtendedPlayer.get(player);
				
			if(vType.IsBoat())
			{
				if(clickedPData.ownedBoat.size() + 1 > clickedPData.getOwnedSlots())
				{
					player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Vous ne pouvez pas sauvegarder ce bâteau votre limite " + EnumChatFormatting.RED + "de slot est de : " + EnumChatFormatting.BLUE + clickedPData.getOwnedSlots()));	
					is.stackSize++;
		            WorldData.get(player.worldObj).driveablesManager.removeDriveablesFromSameType(vehicle, player);	     
					return;
				}
			}
			else
			{
				if(clickedPData.ownedVehicle.size() + 1 > clickedPData.getOwnedSlots())
				{
					player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Vous ne pouvez pas sauvegarder ce vehicule votre limite " + EnumChatFormatting.RED + "de slot est de : " + EnumChatFormatting.BLUE + clickedPData.getOwnedSlots()));	
					is.stackSize++;
		            WorldData.get(player.worldObj).driveablesManager.removeDriveablesFromSameType(vehicle, player);	     
					return;
				}
			}

				
			ItemStack vehicleStack = new ItemStack(type.item, 1, data.paintjobID);
			
			boolean contains = false;

			if(vType.IsBoat())
			{
				for(ItemStack itemstack : clickedPData.ownedBoat) 
				{
					if(itemstack.getItem() == vehicleStack.getItem()) 
					{
						contains = true;
						break;
					}
				}
			}
			else
			{
				for(ItemStack itemstack : clickedPData.ownedVehicle) 
				{
					if(itemstack.getItem() == vehicleStack.getItem()) 
					{
						contains = true;
						break;
					}
				}
			}

			
			if(contains == true)
			{
				player.addChatMessage(new ChatComponentText(EnumChatFormatting.RED + "Vous avez d\u00E9ja ce type de vehicule sauvegard\u00E9"));	
				is.stackSize++;
	            WorldData.get(player.worldObj).driveablesManager.removeDriveablesFromSameType(vehicle, player);	     
				return;
			}	
			
		    if(!vehicleStack.hasTagCompound()) vehicleStack.stackTagCompound = new NBTTagCompound(); 

			vehicle.setproprio(player.getCommandSenderName());
			data.writeToNBT(vehicleStack.stackTagCompound);
			
			clickedPData.saveOwnedVehicle(vehicleStack);

			player.addChatMessage(new ChatComponentText(EnumChatFormatting.GREEN + "V\u00E9hicule sauvegard\u00E9"));	
		}
    }

    public Entity spawnVehicle(World world, double x, double y, double z, ItemStack stack)
    {
    	Entity entity = new EntityVehicle(world, x, y, z, type, getData(stack, world));
    	if(!world.isRemote)
        {
			world.spawnEntityInWorld(entity);
        }
    	return entity;
    }

	public DriveableData getData(ItemStack itemstack, World world)
    {
		return new DriveableData(getTagCompound(itemstack, world), itemstack.getItemDamage());
    }

    @Override
	@SideOnly(Side.CLIENT)
    public int getColorFromItemStack(ItemStack par1ItemStack, int par2)
    {
    	return type.colour;
    }

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IIconRegister icon)
	{
		itemIcon = icon.registerIcon("FlansMod:" + type.iconPath);
	}


    /** Make sure that creatively spawned planes have nbt data */
    @Override
    public void getSubItems(Item item, CreativeTabs tabs, List list)
    {
    	ItemStack planeStack = new ItemStack(item, 1, 0);
    	NBTTagCompound tags = new NBTTagCompound();
    	tags.setString("Type", type.shortName);
    	if(PartType.defaultEngines.containsKey(EnumType.vehicle))
    		tags.setString("Engine", PartType.defaultEngines.get(EnumType.vehicle).shortName);
    	for(EnumDriveablePart part : EnumDriveablePart.values())
    	{
    		tags.setInteger(part.getShortName() + "_Health", type.health.get(part) == null ? 0 : type.health.get(part).health);
    		tags.setBoolean(part.getShortName() + "_Fire", false);
    	}
    	planeStack.stackTagCompound = tags;
        list.add(planeStack);
    }

    
    public static boolean canSpawnVehicle(ItemVehicle item, EntityPlayer player)
    {
    	ExtendedPlayer ep = ExtendedPlayer.get(player);
    	
    	int vehicleId = Item.getIdFromItem(item);
    	
    	if(ep.getSavedVehicle(vehicleId) != null)
    	{
    		return false;
    	}
    	
    	return true;
    }

	
	public int getBlockInHeight(World world, Vec3 clickPos) {
		int blockSize = 0;
		while(true)
		{
			Block block = world.getBlock((int)clickPos.xCoord, (int)++clickPos.yCoord, (int)clickPos.zCoord);
			if(block.getMaterial() == Material.air || block.getMaterial() == Material.water)
			{
				return blockSize;
			}
			blockSize++;
		}
	}

	@Override
	public InfoType getInfoType() {
		return type;
	}

}
