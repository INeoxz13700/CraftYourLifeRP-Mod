package astrotibs.notenoughpets.events;

import java.text.SimpleDateFormat;
import java.time.Period;
import java.util.Arrays;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import javax.annotation.Nullable;

import astrotibs.notenoughpets.ModNotEnoughPets;
import astrotibs.notenoughpets.config.GeneralConfig;
import astrotibs.notenoughpets.entity.EntityOcelotNEP;
import astrotibs.notenoughpets.entity.EntityParrotNEP;
import astrotibs.notenoughpets.entity.EntityWolfNEP;
import astrotibs.notenoughpets.entity.IPetData;
import astrotibs.notenoughpets.entity.PetStateEnum;
import astrotibs.notenoughpets.name.BlackCatNames;
import astrotibs.notenoughpets.util.FunctionsNEP;
import astrotibs.notenoughpets.util.LogHelper;
import astrotibs.notenoughpets.util.Reference;
import astrotibs.notenoughpets.util.SkinVariations;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import cpw.mods.fml.common.gameevent.TickEvent.Phase;
import cpw.mods.fml.common.gameevent.TickEvent.WorldTickEvent;
import cpw.mods.fml.common.network.NetworkRegistry;
import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.main.ExtendedPlayer;
import fr.craftyourliferp.network.PacketPet;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityList;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.passive.EntityMooshroom;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.nbt.NBTBase;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.AxisAlignedBB;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.util.Vec3;
import net.minecraft.village.Village;
import net.minecraft.village.VillageCollection;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;
import net.minecraft.world.gen.structure.MapGenStructureData;
import net.minecraftforge.event.entity.EntityEvent.EntityConstructing;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.living.LivingEvent.LivingUpdateEvent;
import net.minecraftforge.event.entity.living.LivingHurtEvent;
import net.minecraftforge.event.entity.player.AttackEntityEvent;
import net.minecraftforge.event.entity.player.EntityInteractEvent;
import net.minecraftforge.event.terraingen.PopulateChunkEvent;

public class EventHandlerNEP {
	

	@SubscribeEvent
	public void onPlayerInterractWithPet(EntityInteractEvent event)
	{
		if(!event.entity.worldObj.isRemote)
		{
			if(event.entity instanceof EntityPlayer)
			{
				if(event.target instanceof IPetData)
				{
					IPetData petData = (IPetData) event.target;
					if(petData.getPetName() == null)
					{
						CraftYourLifeRPMod.packetHandler.sendTo(PacketPet.openGui(0, event.target.getEntityId()), (EntityPlayerMP) event.entityPlayer);
					}
					else
					{
						if(event.entity.isSneaking()) return;
						
						SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yyyy");
						String date = simpleDateFormat.format(new Date(petData.getPetBirthday()));
						int age = petData.getAge();
						
						ServerUtils.sendChatMessage(event.entityPlayer, "");

						ServerUtils.sendChatMessage(event.entityPlayer, "§6 ----------------------------------------------------");
						
						ServerUtils.sendChatMessage(event.entityPlayer, "§6Prénom : §e" + petData.getPetName());
						
						
						ServerUtils.sendChatMessage(event.entityPlayer, "§6Date de naissance : §e" + date + " §6Age : §e" + age);
						
						int foodBar = (int) (GeneralConfig.MAX_FOOD - (GeneralConfig.MAX_FOOD - petData.getFood()));
						String color = "";
						float normalizedFood = petData.getFood() / GeneralConfig.MAX_FOOD;
						if(normalizedFood < 0.25F)
						{
							color = "§c";
						}
						else if(normalizedFood >= 0.25F && normalizedFood <= 0.5)
						{
							color = "§6";
						}
						else
						{
							color = "§a";
						}
						
						String foodText = "§6Faim : ";
						foodText += color;
						for(int i = 0; i < foodBar; i++)
						{
							foodText += "#";
						}
						foodText += "§7";
						for(int i = foodBar; i < GeneralConfig.MAX_FOOD; i++)
						{
							foodText += "#";
						}
						
						ServerUtils.sendChatMessage(event.entityPlayer, foodText);
						
						int energyBar = (int) (GeneralConfig.MAX_ENERGY - (GeneralConfig.MAX_ENERGY - petData.getEnergy()));
						color = "";
						float normalizedEnergy = petData.getEnergy() / GeneralConfig.MAX_ENERGY;
						if(normalizedEnergy < 0.25F)
						{
							color = "§c";
						}
						else if(normalizedEnergy >= 0.25F && normalizedEnergy <= 0.5)
						{
							color = "§6";
						}
						else
						{
							color = "§a";
						}
						
						String energyText = "§6Energie : ";
						energyText += color;
						for(int i = 0; i < energyBar; i++)
						{
							energyText += "#";
						}
						energyText += "§7";
						for(int i = energyBar; i < GeneralConfig.MAX_ENERGY; i++)
						{
							energyText += "#";
						}
						
						ServerUtils.sendChatMessage(event.entityPlayer, energyText);
						
						int hygieneBar = (int) (GeneralConfig.MAX_HYGIENE - (GeneralConfig.MAX_HYGIENE - petData.getHygiene()));
						color = "";
						float normalizedHygiene = petData.getHygiene() / GeneralConfig.MAX_HYGIENE;
						if(normalizedHygiene < 0.25F)
						{
							color = "§c";
						}
						else if(normalizedHygiene >= 0.25F && normalizedHygiene <= 0.5)
						{
							color = "§6";
						}
						else
						{
							color = "§a";
						}
						
						String hygieneText = "§6Hygiene : ";
						hygieneText += color;
						for(int i = 0; i < hygieneBar; i++)
						{
							hygieneText += "#";
						}
						hygieneText += "§7";
						for(int i = hygieneBar; i < GeneralConfig.MAX_HYGIENE; i++)
						{
							hygieneText += "#";
						}
						ServerUtils.sendChatMessage(event.entityPlayer, hygieneText);
						
						if(petData.getMood() < 10)
						{
							ServerUtils.sendChatMessage(event.entityPlayer, "§6Humeur : §c:(");
						}
						else if(petData.getMood() >= 10 && petData.getMood() <= 13)
						{
							ServerUtils.sendChatMessage(event.entityPlayer, "§6Humeur : §e:/");
						}
						else
						{
							ServerUtils.sendChatMessage(event.entityPlayer, "§6Humeur : §a:)");
						}
					}
				}
			}
		}
	}

	
	/**
	 * This is here to play particular sounds when a cat is injured by a living entity
	 */
	// Added in v 1.1
	@SubscribeEvent
	public void onLivingHurtEvent(LivingHurtEvent event)
	{
		// summon Ocelot_NEC ~ ~ ~ {Owner:AstroTibs, CatType:0}
		// summon Ocelot_NEC ~ ~ ~ {Owner:AstroTibs, CatType:0, Age:-24000}
		// summon Ocelot_NEC ~ ~ ~ {CatType:16}
		if (!event.entityLiving.worldObj.isRemote)
		{
			
			if (event.entity instanceof EntityOcelotNEP)
			{
				EntityOcelotNEP ocelot = (EntityOcelotNEP)event.entity;
				
				if (ocelot.getHealth() <= event.ammount)
				{ // Play a death sound depending on whether or not it's an ocelot.
					if (ocelot.getTameSkin()==0)
					     {event.entity.playSound(Reference.SE_CAT_OCELOT_DEATH, ocelot.getOcelotVolume(), FunctionsNEP.getScaledSoundPitch(ocelot));}
					else {event.entity.playSound("mob.cat.hitt", ocelot.getOcelotVolume(), FunctionsNEP.getScaledSoundPitch(ocelot));}
				}
				else if (
						(event.source.getSourceOfDamage() instanceof EntityPlayer
								||event.source.getSourceOfDamage() instanceof EntityLiving)
							&& event.entity.worldObj.rand.nextInt(4)==0)
				     {event.entity.playSound("mob.cat.hiss", ocelot.getOcelotVolume(), FunctionsNEP.getScaledSoundPitch(ocelot));}
				else {event.entity.playSound("mob.cat.hitt", ocelot.getOcelotVolume(), FunctionsNEP.getScaledSoundPitch(ocelot));}
			}

		}
	}
	
	@SubscribeEvent
	public void onAttackPet(AttackEntityEvent event)
	{
		if(event.entity.worldObj.isRemote) return;

		if(event.target instanceof IPetData)
		{
			IPetData petData = (IPetData)event.target;
			if(petData.getOwner() == null)
			{
				event.setCanceled(true);
			}
		}
	}
	
	@SubscribeEvent
	public void onLivingDeath(LivingDeathEvent event)
	{
		if(event.entity.worldObj.isRemote) return;
		
		if(event.entityLiving instanceof IPetData)
		{
			IPetData petData = (IPetData)event.entityLiving;
			if(petData.getOwner() == null)
			{
				event.setCanceled(true);
				event.entityLiving.heal(20f);
			}
			else
			{
				EntityPlayer player = (EntityPlayer)petData.getOwner();
				ExtendedPlayer exp = ExtendedPlayer.get(player);
				
				exp.petsOwned.remove(event.entityLiving.getUniqueID());
				if(petData.getPetName() != null)
					ServerUtils.sendChatMessage(player, "§b" + petData.getPetName() + " §cvous a quitté à §b" + petData.getAge() + " §can(s) paix à son âme!");
			}
		}
	}


	@SubscribeEvent
	public void onLivingUpdate(LivingUpdateEvent event)
	{
		if (event.entityLiving.worldObj.isRemote) {return;} // Do server-side functions only
	
		
    	if (event.entity.getClass().toString().substring(6).equals("net.minecraft.entity.passive.EntityWolf"))
    	{
    		EntityWolf wolfVanilla = (EntityWolf) event.entity;
    		convertVanillaWolfToNEP(wolfVanilla);
    	}
    	
    	
    	if(event.entityLiving instanceof IPetData)
    	{
    		EntityLiving entityLiving = (EntityLiving)event.entityLiving;
    		IPetData petData = (IPetData) event.entityLiving;
    		    		
    		if(petData.getOwner() == null) return;
    		
    		float lossFeed = 0.98f;
    		float lossHygiene = 0.98f;
    		float lossEnergy = 0.98f;

    		if(event.entityLiving.getRNG().nextInt(1000) == 10)
    		{
        		if(entityLiving.getAttackTarget() != null)
        		{
        			lossEnergy *= 0.98;
        		}
        		
        		if(petData.isSitting())
        		{
        			lossEnergy *= 1.021500f;
        		}
        		
        		if(event.entityLiving.worldObj.isRaining())
        		{
        			lossHygiene *= 1.021500f;
        		}
    			
    			petData.setFood(petData.getFood()*lossFeed);
    			petData.setHygiene(petData.getHygiene()*lossHygiene);
    			if(petData.getEnergy() < 1)
    			{
        			petData.setEnergy(0.1F + petData.getEnergy() * lossEnergy);
    			}
    			else
    			{
        			petData.setEnergy(petData.getEnergy() * lossEnergy);
    			}
    		}
    		
    		if(petData.getMood() <= 0F)
    		{
    			entityLiving.attackEntityFrom(DamageSource.magic, 5.0F);
    		}
    		
    		if(petData.getEnergy() < 1F)
    		{
    			petData.setPetState(PetStateEnum.REST);
    			ChunkCoordinates chunkCoords = petData.getHomePosition();
    			System.out.println(chunkCoords);
    			int randX = MathHelper.getRandomIntegerInRange(event.entityLiving.getRNG(), chunkCoords.posX - (int) petData.func_110174_bM(), chunkCoords.posX + (int) petData.func_110174_bM());
    			int randZ = MathHelper.getRandomIntegerInRange(event.entityLiving.getRNG(), chunkCoords.posZ - (int) petData.func_110174_bM(), chunkCoords.posZ + (int) petData.func_110174_bM());
    			if(!petData.isWithinHomeDistanceCurrentPosition())
    			{
    				EntityPlayer player = (EntityPlayer)petData.getOwner();
    				ServerUtils.sendChatMessage(player, "§cVotre animal de compagnie est épuisé");
    				entityLiving.setPosition(randX, chunkCoords.posY+1, randZ);
    			}
    		}
    	} 
    	
	}
	

    private void playShoulderEntityAmbientSound(EntityPlayer player, @Nullable NBTTagCompound shoulderEntityTag)
    {
    	if (
        		shoulderEntityTag != null
        		&& (
        				!shoulderEntityTag.hasKey("Silent")
        				|| (shoulderEntityTag.hasKey("Silent") && !shoulderEntityTag.getBoolean("Silent"))
        				)
        		)
        {
            String s = shoulderEntityTag.getString("id");
            
            if (s.equals(Reference.MOB_PARROT_NEP))
            {
            	int entityAge = 0;
            	
            	if (shoulderEntityTag.hasKey("Age")) {entityAge = shoulderEntityTag.getInteger("Age");}
            	
            	EntityParrotNEP.playAmbientSound(player.worldObj, player, entityAge);
            }
        }
    }
    
   
    private void spawnShoulderEntity(@Nullable EntityPlayer player, NBTTagCompound tagcompound)
    {
        if (!player.worldObj.isRemote)
        {
        	if(!(tagcompound==null) && !tagcompound.hasNoTags())
        	{
        		// Add failsafe
        		try
        		{
        			tagcompound.setBoolean("NoGravity", true); // Added manually to force flying from shoulders
                    Entity entity = EntityList.createEntityFromNBT(tagcompound, player.worldObj);
                    
                    if (entity instanceof EntityTameable)
                    {
                        ((EntityTameable)entity).func_152115_b(player.getUniqueID().toString()); // setOwnerId in 1.8
                    }

                    entity.setPosition(player.posX, player.posY + 0.699999988079071D, player.posZ);
                    player.worldObj.spawnEntityInWorld(entity);
        		}
        		catch (Exception e) {}
        	}
        }
    }
    

	
	/**
	 * This method intercepts every spawned cat/dog and replaces it with my version.
	 * Alternately, you can reverse this.
	 * 
	 * In addition, there is a limiter to prevent ocelot/wolf spawns
	 */
    @SubscribeEvent
    public void onEntityJoinWorld(EntityJoinWorldEvent event)
    {
    	if (!event.world.isRemote)
    	{
    		if(event.entity.getClass().toString().substring(6).equals("net.minecraft.entity.passive.EntityOcelot") || event.entity.getClass().toString().substring(6).equals("net.minecraft.entity.passive.EntityWolf"))
    		{
    			event.entity.setDead();
    		}
    	}

    }
    
	
	/**
	 * Searches within a given swamp hut to see if a cat hasn't yet spawned and can spawn
	 */
	private static boolean searchNBTAndAddCat(NBTTagCompound nbttagcompound2, int[] boundingBox, World world, int xChunkCenter, int zChunkCenter, int buffer, MapGenStructureData structureData)
	{
		// Swamp hut seed 7892649471625196211
		
		if (
				   xChunkCenter >= boundingBox[0]-buffer
				//&& event.y >= boundingBox[1]
				&& zChunkCenter >= boundingBox[2]-buffer
				&& xChunkCenter <= boundingBox[3]+buffer
				//&& event.y <= boundingBox[4]
				&& zChunkCenter <= boundingBox[5]+buffer
				)
		{
			Block blocktoscan;
			
			for (int k = 50; k <= 80; k++) {
				for (int i=-8; i < 8; i++) {
					for (int j=-8; j < 8; j++) {
						
						blocktoscan = world.getBlock(xChunkCenter+i, k, zChunkCenter+j);
						
						// Put cat on or near the crafting table
						if (blocktoscan == Blocks.crafting_table)
						{
							for (int k1 = 0; k1 < 26; ++k1)
					        {
								int l = world.rand.nextInt(3)-1;
								int n = world.rand.nextInt(3)-1;
								int m = world.rand.nextInt(3)-1;
								
								if (
										world.getBlock( xChunkCenter+i+l, k+n, zChunkCenter+j+m ).isAir( world, xChunkCenter+i+l, k+n, zChunkCenter+j+m )
										&& world.doesBlockHaveSolidTopSurface(world, xChunkCenter+i+l, k+n-1, zChunkCenter+j+m )
										)
								{
									EntityOcelotNEP entityBlackCat = new EntityOcelotNEP(world);
						    		
						    		((EntityOcelotNEP)entityBlackCat).setTameSkin(10); // Black cat
									
									entityBlackCat.setLocationAndAngles((double)(xChunkCenter+i+l)+Reference.SPAWN_BLOCK_OFFSET, (double)(k+n), (double)(zChunkCenter+j+m)+Reference.SPAWN_BLOCK_OFFSET, world.rand.nextFloat()*360, 0.0F);
					            	entityBlackCat.setTamed(false);
					            	// Persistence enabled on this cat
					            	entityBlackCat.func_110163_bv(); // enablePersistence() in 1.8 
					            	
					            	if (GeneralConfig.swampHutCatName) {entityBlackCat.setCustomNameTag(BlackCatNames.newRandomName(world.rand));}
					            	
					            	world.spawnEntityInWorld(entityBlackCat);
					            	
					            	if (GeneralConfig.debugMessages) {LogHelper.info("Adding black cat to swamp hut at " + (xChunkCenter+i+l) + " " + (k+n) + " " + (zChunkCenter+j+m));}
					            	
									return true;
								}
					        }
							if (GeneralConfig.debugMessages) {LogHelper.warn("Failed to add black cat to swamp hut at " + (xChunkCenter+i) + " " + (k) + " " + (zChunkCenter+j));}
						}
					}
				}
			}
		}
		return false;
	}
    
    
    /**
     * Checks box of regions around test point in order to locate a valid spawn
     */
    private static boolean isValidSpawningLocation(int spawnposX, int spawnposY, int spawnposZ, int xRange, int yRange, int zRange, World world)
    {
        if (!World.doesBlockHaveSolidTopSurface(world, spawnposX, spawnposY - 1, spawnposZ))
        {
            return false;
        }
        else
        {
            int k1 = spawnposX - xRange / 2;
            int l1 = spawnposZ - zRange / 2;

            for (int checkX = k1; checkX < k1 + xRange; ++checkX)
            {
                for (int checkY = spawnposY; checkY < spawnposY + yRange; ++checkY)
                {
                    for (int checkZ = l1; checkZ < l1 + zRange; ++checkZ)
                    {
                        if (world.getBlock(checkX, checkY, checkZ).isNormalCube())
                        {
                            return false;
                        }
                    }
                }
            }

            return true;
        }
    }
    
    
    /**
     * Returns true, if the given coordinates are within the bounding box of the village.
     */
    public static boolean isInRange(int x, int y, int z, Village village)
    {
        return village.getCenter().getDistanceSquared(x, y, z) < (float)((village.getVillageRadius()+Reference.VILLAGE_RADIUS_BUFFER) * (village.getVillageRadius()+Reference.VILLAGE_RADIUS_BUFFER));
    }
    
    // Helper function to convert vanilla wolves to NEP since that's a bit more complicated now
    public void convertVanillaWolfToNEP(EntityWolf wolfVanilla)
    {
		wolfVanilla.lastTickPosX = wolfVanilla.posX;
		wolfVanilla.lastTickPosY = wolfVanilla.posY;
		wolfVanilla.lastTickPosZ = wolfVanilla.posZ;
		
		EntityWolfNEP wolfNEC = new EntityWolfNEP(wolfVanilla.worldObj);
		wolfNEC.setLocationAndAngles(wolfVanilla.posX, wolfVanilla.posY, wolfVanilla.posZ, wolfVanilla.rotationYaw, 0.0F);
		// Set age, in the event that the ocelot is followed along by babies
		wolfNEC.setGrowingAge(wolfVanilla.getGrowingAge());

		// Aggression stuff
		wolfNEC.setAngry(wolfVanilla.isAngry());
		wolfNEC.setAttackTarget(wolfVanilla.getAttackTarget());
		wolfNEC.setRevengeTarget(wolfVanilla.getAITarget());
		
		// Owner stuff
		wolfNEC.setTamed(wolfVanilla.isTamed());
		if (wolfVanilla.func_152113_b() == null)
			{wolfNEC.func_152115_b("");}
        else
        	{wolfNEC.func_152115_b(wolfVanilla.func_152113_b());}
		wolfNEC.setCollarColor(wolfVanilla.getCollarColor());
		
		// Set other properties
		wolfNEC.setCustomNameTag(wolfVanilla.getCustomNameTag());
		wolfNEC.setSitting(wolfVanilla.isSitting());
		//wolfNEC.setTameSkin(wolfVanilla.getTameSkin());
		
		//ocelot.setHealth(0);
		wolfVanilla.setDead();
		wolfNEC.worldObj.spawnEntityInWorld(wolfNEC);
    }
    
 
}
