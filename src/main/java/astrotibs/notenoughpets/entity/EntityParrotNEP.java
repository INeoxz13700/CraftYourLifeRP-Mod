package astrotibs.notenoughpets.entity;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import astrotibs.notenoughpets.ai.minecraft.EntityAIFollowOwnerFlying;
import astrotibs.notenoughpets.ai.minecraft.EntityAIFollowParrot;
import astrotibs.notenoughpets.ai.minecraft.EntityAISwimmingNEP;
import astrotibs.notenoughpets.ai.minecraft.EntityAIWanderAvoidWaterFlying;
import astrotibs.notenoughpets.ai.minecraft.EntityFlyHelper;
import astrotibs.notenoughpets.config.GeneralConfig;
import astrotibs.notenoughpets.pathfinding.minecraft.PathNavigateFlying;
import astrotibs.notenoughpets.util.FunctionsNEP;
import astrotibs.notenoughpets.util.Reference;
import astrotibs.notenoughpets.util.SkinVariations;
import astrotibs.notenoughpets.util.minecraft.BlockPos;
import cpw.mods.fml.relauncher.ReflectionHelper;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketPet;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.Block;
import net.minecraft.block.BlockJukebox;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.material.Material;
import net.minecraft.command.IEntitySelector;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAIMate;
import net.minecraft.entity.ai.EntityAIPanic;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAIWatchClosest;
import net.minecraft.entity.ai.EntityMoveHelper;
import net.minecraft.entity.ai.attributes.RangedAttribute;
import net.minecraft.entity.passive.EntityAnimal;
import net.minecraft.entity.passive.EntityTameable;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Blocks;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.pathfinding.PathNavigate;
import net.minecraft.potion.Potion;
import net.minecraft.potion.PotionEffect;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.util.DamageSource;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

/**
 * This is called EntityAIFollow in 1.12.2 but is only used for parrots
 */
public class EntityParrotNEP extends EntityTameable implements EntityFlying, IPetData
{
	
	private long petBirthday;
	
	private String petName;
	
	private float food;
	private float hygiene;
	
	private float energy;
	
	private PetStateEnum state = PetStateEnum.REST;

	
	// --------------------- //
    // --- ESTABLISHMENT --- //
    // --------------------- //
    
	public static double rJB = Math.sqrt(12D); // Radius up to which a parrot responds to an active jukebox
    
	/** Used to select entities the parrot can mimic the sound of */
    private static final java.util.Map<String, Object[]> MIMIC_SOUNDS = Maps.newHashMapWithExpectedSize(32);
    private static final IEntitySelector CAN_MIMIC = new IEntitySelector()
    {
        // Return whether the specified entity is applicable to this filter.
        public boolean isEntityApplicable(Entity entity)
        {
        	return entity instanceof EntityLivingBase && EntityParrotNEP.MIMIC_SOUNDS.containsKey(entity.getClass().toString().substring(6));
        }
    };
    
    // Pasted in from 1.12 EntityLivingBase
    public static final net.minecraft.entity.ai.attributes.IAttribute SWIM_SPEED = new RangedAttribute("forge.swimSpeed", 1.0D, 0.0D, 1024.0D).setShouldWatch(true);
    private static final Item DEADLY_ITEM = Items.cookie;
    
    private static Set<Item> TAME_ITEMS = Sets.newHashSet(Items.wheat_seeds, Items.melon_seeds, Items.pumpkin_seeds);
    {
    	// Add mod items
    	Item beetrootseed = new Item();
    	
    	// Gany's Surface
    	beetrootseed = FunctionsNEP.getItemFromName("armamania:ItemBeets");
    	if (beetrootseed != null) {TAME_ITEMS.add(beetrootseed);}
    }
    
    public float flap;
    public float flapSpeed;
    public float oFlapSpeed;
    public float oFlap;
    public float flapping = 1.0F;
    private boolean partyParrot;
    private BlockPos jukeboxPosition;
    private float nextFlap;
    public float moveVertical;
    public double flyingSpeed;
    public boolean NO_GRAVITY;
    
    public EntityParrotNEP(World worldIn)
    {
    	super(worldIn);
        
		
    	food = GeneralConfig.MAX_FOOD;
		hygiene = GeneralConfig.MAX_HYGIENE;
		energy = GeneralConfig.MAX_ENERGY;
		
        //Added this because 1.8's EntityLiving initializes the navigator
    	//It's the navigator that's responsible for the bird flying upward when you punch it
        ReflectionHelper.setPrivateValue(EntityLiving.class, this, getNewNavigator(worldIn), new String[]{"navigator", "field_70699_by"});
    	
        this.setSize(0.5F, 0.9F);
        
        ReflectionHelper.setPrivateValue(EntityLiving.class, this, new EntityFlyHelper(this), new String[]{"moveHelper", "field_70765_h"});
        this.nextFlap = 1.0F;
        
        
        this.tasks.addTask(0, new EntityAIPanic(this, 1.25D)); // Same as 1.8
        this.tasks.addTask(0, new EntityAISwimmingNEP(this));
        this.tasks.addTask(1, new EntityAIWatchClosest(this, EntityPlayer.class, 8.0F));
        this.aiSit = new EntityAISit(this); this.tasks.addTask(2, this.aiSit);
        this.tasks.addTask(2, new EntityAIFollowOwnerFlying(this, 1.0D, 5.0F, 1.0F));
        this.tasks.addTask(2, new EntityAIWanderAvoidWaterFlying(this, 1.0D));
        this.tasks.addTask(3, new EntityAIFollowParrot(this, 1.0D, 3.0F, 7.0F));
        this.tasks.addTask(4, new EntityAIMate(this, 0.8D));
        
    }

    /**
     * Returns new PathNavigateFlying instance
     */
    protected PathNavigateFlying getNewNavigator(World worldIn)
    {
    	PathNavigateFlying pathnavigateflying = new PathNavigateFlying(this, worldIn);
        pathnavigateflying.setCanBreakDoors(false);
        pathnavigateflying.setCanFloat(true);
        pathnavigateflying.setCanEnterDoors(true);
        return pathnavigateflying;
    }

    /**
     * Returns true if the newer Entity AI code should be run
     * I think this needs to be added in 1.7
     */
    public boolean isAIEnabled() {return true;}
    
    public void setMoveVertical(float amount) {this.moveVertical = amount;}
    public float getMoveVertical() {return this.moveVertical;}

    /**
     * Called only once on an entity when first time spawned, via egg, mob spawner, natural spawning etc, but not called
     * when entity is reloaded from nbt. Mainly used for initializing attributes and inventory
     */
    @Nullable
    @Override
    public IEntityLivingData onSpawnWithEgg(@Nullable IEntityLivingData livingdata)
    {
        this.setVariant(this.rand.nextInt(SkinVariations.parrotSkinArray.length));
        return super.onSpawnWithEgg(livingdata);
    }

    @Override
    protected void applyEntityAttributes()
    {
        super.applyEntityAttributes();
        
        this.getEntityAttribute(SharedMonsterAttributes.maxHealth).setBaseValue(6.0D);
        
        this.flyingSpeed = 0.4000000059604645D;
        this.getEntityAttribute(SharedMonsterAttributes.movementSpeed).setBaseValue(0.20000000298023224D);
        this.getAttributeMap().registerAttribute(SWIM_SPEED);
    }

    /**
     * Checks if the entity's current position is a valid location to spawn this entity.
     */
    @Override
    public boolean getCanSpawnHere()
    {
        int i = MathHelper.floor_double(this.posX);
        int j = MathHelper.floor_double(this.boundingBox.minY);
        int k = MathHelper.floor_double(this.posZ);
        Block block = this.worldObj.getBlock(i, j-1, k);
        return block instanceof BlockLeaves || block == Blocks.grass || block instanceof BlockLog || block == Blocks.air && this.worldObj.getFullBlockLightValue(i, j, k) > 8 && super.getCanSpawnHere();
    }

    public int getVariant()
    {
    	return (int)(this.dataWatcher.getWatchableObjectByte(18) % SkinVariations.parrotSkinArray.length);
    }

    public void setVariant(int variant)
    {
    	this.dataWatcher.updateObject(18, Byte.valueOf((byte)variant));
    }

    @Override
    protected void entityInit()
    {
        super.entityInit();
        
        // Using 18 for the variant skin
        this.dataWatcher.addObject(18, Byte.valueOf((byte)0));
        
        
        // Using 19 For baby scaling
        this.dataWatcher.addObject(19, new Integer(0));
        
        // No dataManager needed to booleans
        this.NO_GRAVITY = false;
    }

    // --- End Establishment --- //    

    
    
    
    // --------------- //
    // --- ONGOING --- //
    // --------------- //

    /**
     * Method written to explicitly check whether there's a jukebox playing music nearby.
     */
    public BlockPos isJukeboxPlayingNearby(double radius)
    {
    	double rJBsqrd = radius*radius;
    	
    	// Systematically check all blocks within a squared distance less than 12 (distance less than 3.46)
        for (int xScan = MathHelper.ceiling_double_int(this.posX-radius); xScan <= MathHelper.floor_double(this.posX+radius); xScan++) {
        	
        	double yzCircleDist = xScan-this.posX; // current search plane's distance from the parrot in the x direction
        	
        	for (int yScan = MathHelper.ceiling_double_int(this.posY-Math.sqrt(rJBsqrd-yzCircleDist*yzCircleDist)); yScan <= MathHelper.floor_double(this.posY+Math.sqrt(rJBsqrd-yzCircleDist*yzCircleDist)); yScan++) {
        		
        		double zSliceDist = yScan-this.posY; // Current search row's distance from the parrot in the y direction
        		
        		for (int zScan = MathHelper.ceiling_double_int(this.posZ-Math.sqrt(rJBsqrd-yzCircleDist*yzCircleDist-zSliceDist*zSliceDist)); zScan <= MathHelper.floor_double(this.posZ+Math.sqrt(rJBsqrd-yzCircleDist*yzCircleDist-zSliceDist*zSliceDist)); zScan++)
        		{
        			Block block = this.worldObj.getBlock(xScan, yScan, zScan);
        			
        			if (
        					block instanceof BlockJukebox
        					&& this.worldObj.getBlockMetadata(xScan, yScan, zScan)!=0
        					)
        			{
        				return new BlockPos(xScan, yScan, zScan);
        			}
        		}
        	}
        }
        return null;
    }

    @Override
    public float getEyeHeight()
    {
        return this.height * 0.6F;
    }

    
    /**
     * Called frequently so the entity can update its state every tick as required. For example, zombies and skeletons
     * use this to react to sunlight and start to burn.
     */
    @Override
    public void onLivingUpdate()
    {	
    	if (this.worldObj.rand.nextInt(100) < GeneralConfig.soundOccurrenceParrotAmbient) {playMimicSound(this.worldObj, this, this.getGrowingAge());}
        
        this.jukeboxPosition = isJukeboxPlayingNearby(rJB);
        
        if (this.jukeboxPosition == null
        		|| this.jukeboxPosition.distanceSq(this.posX, this.posY, this.posZ) > 12.0D
        		|| this.worldObj.getBlock(this.jukeboxPosition.getX(), this.jukeboxPosition.getY(), this.jukeboxPosition.getZ()) != Blocks.jukebox)
        {
            this.partyParrot = false;
            this.jukeboxPosition = null;
        }
        else
        {
        	this.partyParrot = true;
        }
        
        super.onLivingUpdate();
        
        //this.travel(this.moveStrafing, this.getMoveVertical(), this.moveForward);
        
        this.calculateFlapping();
    }

    /*
     * Added this in to FORCE updates of the move helper
     */
    @Override
    protected void updateEntityActionState()
    {
    	super.updateEntityActionState(); //This needs to call super so that updateAITasks doesn't get gutted
    	
    	//this.navigator.onUpdateNavigation();
    	PathNavigate navigator_reflected = ReflectionHelper.getPrivateValue(EntityLiving.class, this, new String[]{"navigator", "field_70699_by"});
    	Method onUpdateNavigation_reflected = ReflectionHelper.findMethod(
    			PathNavigate.class,
    			navigator_reflected,
    			new String[]{"onUpdateNavigation", "func_75501_e"});
    	try {onUpdateNavigation_reflected.invoke(navigator_reflected);}
    	catch (Exception e) {}
    	
    	//this.moveHelper.onUpdateMoveHelper();
    	EntityMoveHelper moveHelper_reflected = ReflectionHelper.getPrivateValue(EntityLiving.class, this, new String[]{"moveHelper", "field_70765_h"});
    	Method onUpdateMoveHelper_reflected = ReflectionHelper.findMethod(
    			EntityMoveHelper.class,
    			moveHelper_reflected,
    			new String[]{"onUpdateMoveHelper", "func_75641_c"});
    	try {onUpdateMoveHelper_reflected.invoke(moveHelper_reflected);}
    	catch (Exception e) {}
    }
    
    @SideOnly(Side.CLIENT)
    public void setPartying(BlockPos pos, boolean ispartying)
    {
        this.jukeboxPosition = pos;
        this.partyParrot = ispartying;
    }
    
    @SideOnly(Side.CLIENT)
    public boolean isPartying()
    {
        return this.partyParrot;
    }
    
    private void calculateFlapping()
    {
    	this.oFlap = this.flap;
        this.oFlapSpeed = this.flapSpeed;
        this.flapSpeed = (float)((double)this.flapSpeed + (double)(this.onGround ? -1 : 4) * 0.3D);
        this.flapSpeed = MathHelper.clamp_float(this.flapSpeed, 0.0F, 1.0F);

        if (!this.onGround && this.flapping < 1.0F)
        {
            this.flapping = 1.0F;
        }

        this.flapping = (float)((double)this.flapping * 0.9D);
        
        if (!this.onGround && this.motionY < 0.0D)
        {
            this.motionY *= 0.6D;
        }
        
        this.flap += this.flapping * 2.0F;
    }

	@Override
    public int getGrowingAge()
    {
		return this.dataWatcher.getWatchableObjectInt(19);
    }

	// Added in v1.2.1
	@Override
    public void setGrowingAge(int age)
    {
		super.setGrowingAge(age);
		this.dataWatcher.updateObject(19, Integer.valueOf(age));
    }

    
    // --- End Ongoing --- //
    

    
    
    // ------------------- //
    // --- INTERACTION --- //
    // ------------------- //

	// Added manually to check against
	public static boolean isHealingFood(Item item) {return TAME_ITEMS.contains(item);}
	
	
    @Override
    public boolean interact(EntityPlayer player)
    {
    	if(worldObj.isRemote) return true;
    	
    	if(getOwner() != player)
    	{
    		return true;
    	}
    	
    	
    	ItemStack itemstack = player.inventory.getCurrentItem();
    	
    	
    	if(player.isSneaking())
		{
			int petState = getPetState().ordinal()+1;
			
			if(petState > PetStateEnum.values().length-1)
			{
				petState = 0;
			}
			if(petState <= 1 && getEnergy() < 1)
			{
				ServerUtils.sendChatMessage(player, "§cVotre animal est épuisé laissez le se reposer.");

				return true;
			}
			
			if(petState == 0)
			{
				ServerUtils.sendChatMessage(player, "§bVotre animal vous suit");
			}
			else if(petState == 1)
			{
				ServerUtils.sendChatMessage(player, "§bVotre animal ne vous suit plus");
			}
			else if(petState == 2)
			{
				ServerUtils.sendChatMessage(player, "§bVotre animal est en repos");
			}
			else if(petState == 3)
			{
				ServerUtils.sendChatMessage(player, "§bDéfinissez la maison de votre animal (clique droit sur un bloc)");
			}
			
			setPetState(PetStateEnum.values()[petState]);
			return true;
		}

        if (itemstack!=null)
        {
        	
        	
    		if (itemstack!=null && itemstack.getItem() == DEADLY_ITEM)
            {
                if (!player.capabilities.isCreativeMode) {--itemstack.stackSize;}

                this.addPotionEffect(new PotionEffect(Potion.poison.id, 900));
                
                boolean invulnerable_reflected = ReflectionHelper.getPrivateValue(Entity.class, this, new String[]{"invulnerable", "field_83001_bt"}); // The MCP mapping for this field name
                
                if ((player.capabilities.isCreativeMode || !invulnerable_reflected) && GeneralConfig.cookiesKillParrots) // v2.0.0
                {
                    this.attackEntityFrom(DamageSource.causePlayerDamage(player), Float.MAX_VALUE);
                }

                return true;
            }
    		else
    		{
    			if(itemstack.getItem() == Items.water_bucket)
				{
					if(getHygiene() < GeneralConfig.MAX_HYGIENE)
					{
						if (!player.capabilities.isCreativeMode) 
        				{
        					--itemstack.stackSize;
            				if (itemstack.stackSize <= 0) {player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);}
        				}
						
						setHygiene(getHygiene() + 10F);
        				CraftYourLifeRPMod.packetHandler.sendTo(PacketPet.playEffect(getEntityId()), (EntityPlayerMP) player);
					}
					else
					{
						ServerUtils.sendChatMessage(player, "§cVotre animal est propre");
					}
				}
    			else
    			{
    				if(!(itemstack.getItem() instanceof ItemFood) && !isHealingFood(itemstack.getItem()) && itemstack.getItem() != Items.milk_bucket) return true;
    				
    				if(getFood() >= GeneralConfig.MAX_FOOD && getHealth() >= getMaxHealth())
    				{
        				ServerUtils.sendChatMessage(player, "§cVotre animal refuse de manger");
        				return true;
    				}
        			
        			if((System.currentTimeMillis() - getPetBirthday()) < 86400*7*8*1000)
        			{
            			if(itemstack.getItem() != Items.milk_bucket)
            			{
            				ServerUtils.sendChatMessage(player, "§cLes animaux de cette âge préfèrent du lait.");
            			}
            			else
            			{
            				if (!player.capabilities.isCreativeMode) 
            				{
            					--itemstack.stackSize;
                				if (itemstack.stackSize <= 0) {player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);}
            				}            				
            				
            				if(getFood() < GeneralConfig.MAX_FOOD)
            				{
                				this.setFood(getFood() + 6F);
            				}     
            				else
            				{
            					heal(2F);
            				}
            				
                    		this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ,Reference.ENTITY_PARROT_EAT, 1.0F, (1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F)*ageScalePitch(this.getGrowingAge()));

            				CraftYourLifeRPMod.packetHandler.sendTo(PacketPet.playEffect(getEntityId()), (EntityPlayerMP) player);
            			}
            			return true;
        			}
        			else
        			{
        				ItemFood itemFood = (ItemFood)itemstack.getItem();

        				if (isHealingFood(itemstack.getItem()) && (this.getHealth() < this.getMaxHealth()))
            			{
            				if (!player.capabilities.isCreativeMode) 
            				{
            					--itemstack.stackSize;
                				if (itemstack.stackSize <= 0) {player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);}
            				}
            				
            				if(getFood() < GeneralConfig.MAX_FOOD)
            				{
                				this.setFood(getFood() + (float)itemFood.func_150905_g(itemstack));
            				}     
            				else
            				{
                				this.heal((float)itemFood.func_150905_g(itemstack)); // becomes getHealAmount() in 1.8
            				}
            				
                    		this.worldObj.playSoundEffect(this.posX, this.posY, this.posZ,Reference.ENTITY_PARROT_EAT, 1.0F, (1.0F + (this.rand.nextFloat() - this.rand.nextFloat()) * 0.2F)*ageScalePitch(this.getGrowingAge()));
            				CraftYourLifeRPMod.packetHandler.sendTo(PacketPet.playEffect(getEntityId()), (EntityPlayerMP) player);
            			}
            			
            			    
            			
            			return true;
        			}
    			}
    			
    			
    		}

    	}

        return true;
    }

	@Override
	public boolean isSitting()
	{
		return super.isSitting();
	}
    
    public void setTamedBy(EntityPlayer player)
    {
        this.setTamed(true);
        this.func_152115_b(player.getUniqueID().toString()); // setOwnerId in 1.8
    }
    
    /**
     * Checks if the parameter is an item which this animal can be fed to breed it (wheat, carrots or seeds depending on
     * the animal type)
     */
    @Override
    public boolean isBreedingItem(ItemStack stack)
    {
    	return isHealingFood(stack.getItem()); // Allows parrot to breed!
    }

    /**
     * Returns true if the mob is currently able to mate with the specified mob.
     */
    @Override
    public boolean canMateWith(EntityAnimal otherAnimal)
    {
        if (otherAnimal == this)
        {
            return false;
        }
        else if (!this.isTamed())
        {
            return false;
        }
        else if (!(otherAnimal instanceof EntityParrotNEP))
        {
            return false;
        }
        else
        {
        	EntityParrotNEP otherParrot = (EntityParrotNEP)otherAnimal;

            if (!otherParrot.isTamed())
            {
                return false;
            }
            else
            {
                return this.isInLove() && otherParrot.isInLove();
            }
        }
    }

    @Override
    public boolean attackEntityAsMob(Entity entityIn)
    {
        return entityIn.attackEntityFrom(DamageSource.causeMobDamage(this), 3.0F);
    }

    @Nullable
    @Override
    public EntityAgeable createChild(EntityAgeable otherParent)
    {
		EntityParrotNEP babyParrot = new EntityParrotNEP(this.worldObj);
		
        if (this.isTamed())
        {
        	babyParrot.func_152115_b(this.func_152113_b()); // setOwnerId() and getOwnerId() in 1.8
            babyParrot.setTamed(true);
            // Set the offspring to a random variant based on the parents
            try
            {
            	babyParrot.setVariant( this.worldObj.rand.nextBoolean() ? this.getVariant() : ((EntityParrotNEP)otherParent).getVariant() );
            }
            catch (Exception e) {babyParrot.setVariant(this.getVariant());} // If something goes wrong, set the variant to this parrot
            // v2.0.0 randomly select a skin that has nothing to do with the parents - MUTATION!
            if (this.worldObj.rand.nextInt(1000)==0) {babyParrot.setVariant(SkinVariations.parrotSkinArray.length);}
        }
        return babyParrot;
    }

    /**
     * Called when the entity is attacked.
     */
    @Override
    public boolean attackEntityFrom(DamageSource source, float amount)
    {
    	if (this.isEntityInvulnerable())//(this.isEntityInvulnerable(source)) // Source-based invulnerability is introduced in 1.8
        {
            return false;
        }
        else
        {
            if (this.aiSit != null)
            {
            	this.aiSit.setSitting(false);
            }

            return super.attackEntityFrom(source, amount);
        }
    }
    
    /**
     * Drop 0-2 items of this living's type
     */
    protected void dropFewItems(boolean wasRecentlyHit, int lootingLevel)
    {
        int i = this.rand.nextInt(2) + 1 + this.rand.nextInt(1 + lootingLevel);

        for (int j = 0; j < i; ++j)
        {
        	this.dropItem(Items.feather, 1);
        }
    }

    // --- End Interaction --- //
    
    
    
    // -------------- //
    // --- SOUNDS --- //
    // -------------- //

    // Register all the mimic sounds in the config
    static
    {
    	registerMimicsFromConfig(GeneralConfig.parrotMimicSounds);
    }
    
	/**
	 * Loads the (classPath|soundPath|pitch) string lists and assigns them to this instance's variables.
	 */
	public static void registerMimicsFromConfig(String[] inputList)
	{
		for (String entry : inputList)
		{
			// Split by pipe
			String[] splitEntry = entry.split("\\|");
			try
			{
				registerMimicSound(splitEntry[0].trim(), splitEntry[1].trim(), Float.parseFloat(splitEntry[2].trim()));
			}
			catch (Exception e) {} // Failed to interpret config entry
		}
		
		// Load the defaults if there is a problem
		if (MIMIC_SOUNDS.size() == 0)
		{
	    	registerMimicSound("net.minecraft.entity.monster.EntityBlaze",           "minecraft:mob.blaze.breathe", 1.8F);
	        registerMimicSound("net.minecraft.entity.monster.EntityCaveSpider",      "minecraft:mob.spider.say", 1.8F);
	        registerMimicSound("net.minecraft.entity.monster.EntityCreeper",         "minecraft:creeper.primed", 1.8F);
	        //registerMimicSound("net.minecraft.entity.monster.EntityElderGuardian",   "minecraft:mob.guardian.land.idle", 1.8F); // Added in 1.11
	        registerMimicSound("net.minecraft.entity.boss.EntityDragon",             "minecraft:mob.enderdragon.growl", 1.8F);
	        registerMimicSound("net.minecraft.entity.monster.EntityEnderman",        "minecraft:mob.endermen.idle", 1.7F);
	        //registerMimicSound("net.minecraft.entity.monster.EntityEndermite",       "minecraft:mob.silverfish.say", 1.8F); // Added in 1.8
	        //registerMimicSound("net.minecraft.entity.monster.EntityEvoker",          "minecraft:entity.evocation_illager.ambient", 1.8F); // Added in 1.11
	        registerMimicSound("net.minecraft.entity.monster.EntityGhast",           "minecraft:mob.ghast.moan", 1.8F);
	        //registerMimicSound("net.minecraft.entity.monster.EntityHusk",            "minecraft:entity.husk.ambient", 1.8F); // Added in 1.11
	        //registerMimicSound("net.minecraft.entity.monster.EntityIllusionIllager", "minecraft:entity.illusion_illager.ambient", 1.8F); // Added in 1.12
	        registerMimicSound("net.minecraft.entity.monster.EntityMagmaCube",       "minecraft:mob.magmacube.big", 1.8F);
	        registerMimicSound("net.minecraft.entity.monster.EntityPigZombie",       "minecraft:mob.zombiepig.zpig", 1.8F);
	        //registerMimicSound("net.minecraft.entity.monster.EntityPolarBear",       "minecraft:entity.polar_bear.ambient", 0.7F); // Added in 1.10
	        //registerMimicSound("net.minecraft.entity.monster.EntityShulker",         "minecraft:entity.shulker.ambient", 1.7F); // added in 1.9
	        registerMimicSound("net.minecraft.entity.monster.EntitySilverfish",      "minecraft:mob.silverfish.say", 1.8F);
	        registerMimicSound("net.minecraft.entity.monster.EntitySkeleton",        "minecraft:mob.skeleton.say", 1.7F);
	        registerMimicSound("net.minecraft.entity.monster.EntitySlime",           "minecraft:mob.slime.big", 1.8F);
	        registerMimicSound("net.minecraft.entity.monster.EntitySpider",          "minecraft:mob.spider.say", 1.8F);
	        //registerMimicSound("net.minecraft.entity.monster.EntityStray",           "minecraft:entity.stray.ambient", 1.6F); // Added in 1.11
	        //registerMimicSound("net.minecraft.entity.monster.EntityVex",             "minecraft:entity.vex.ambient", 1.6F); // Added in 1.11
	        //registerMimicSound("net.minecraft.entity.monster.EntityVindicator",      "minecraft:entity.vindication_illager.ambient", 1.7F); // Added in 1.11
	        //registerMimicSound("net.minecraft.entity.monster.EntityWitch",           "minecraft:entity.witch.ambient", 1.8F); // Sounds not implemented until 1.9
	        registerMimicSound("net.minecraft.entity.boss.EntityWither",          "minecraft:mob.wither.idle", 1.8F);
	        //registerMimicSound("net.minecraft.entity.monster.EntityWitherSkeleton",  "minecraft:entity.wither_skeleton.ambient", 1.8F); // Added in 1.11
	        //registerMimicSound("net.minecraft.entity.passive.EntityWolf",            "minecraft:mob.wolf.bark", 1.8F);
	        registerMimicSound("net.minecraft.entity.monster.EntityZombie",          "minecraft:mob.zombie.say", 1.8F);
	        //registerMimicSound("net.minecraft.entity.monster.EntityZombieVillager",  "minecraft:entity.zombie_villager.ambient", 1.8F); // Added in 1.11
		}
	}
    
    public static void registerMimicSound(String classpath, String sound, float pitch)
    {
    	pitch = MathHelper.clamp_float(pitch, 0.4F, 2.0F);
        MIMIC_SOUNDS.put(classpath, new Object[]{sound, pitch});
    }

    /*
     * This is intended to be played when the parrot is on your shoulder
     */
    public static void playAmbientSound(World worldIn, Entity entity, int entityAge)
    {
    	
    	//if (!entity.isSilent()) // Silent flag introduced in 1.8
		if (worldIn.rand.nextInt(100) < GeneralConfig.soundOccurrenceParrotAmbient)
    	{
			if (!playMimicSound(worldIn, entity, entityAge) && worldIn.rand.nextInt(200) == 0)
	        {
				worldIn.playSoundEffect(
    					entity.posX, entity.posY, entity.posZ,
    					getAmbientSound(worldIn.rand), 1.0F, getPitch(worldIn.rand) * ageScalePitch(entityAge)
    					);
	        }
    	}
    }

    private static boolean playMimicSound(World worldIn, Entity mimic, int entityAge)
    {
        if (//!mimic.isSilent() && // Silent flag introduced in 1.8
        		GeneralConfig.enableParrotMimicry &&
        		worldIn.rand.nextInt(100 * 50) < GeneralConfig.soundOccurrenceParrotImitate)
        {
            List<EntityLiving> list = worldIn.selectEntitiesWithinAABB(EntityLiving.class, mimic.boundingBox.expand(20.0D, 20.0D, 20.0D), CAN_MIMIC);

            if (!list.isEmpty())
            {
                EntityLiving entityliving = list.get(worldIn.rand.nextInt(list.size()));
                
            	Object[] soundMimicObject = MIMIC_SOUNDS.get(entityliving.getClass().toString().substring(6));
            	
            	String soundName = (String)soundMimicObject[0];
            	float pitchMimic = (Float) soundMimicObject[1];
                
            	worldIn.playSoundEffect(
            			mimic.posX, mimic.posY, mimic.posZ,
            			soundName, 0.7F, getPitch(worldIn.rand)*pitchMimic*ageScalePitch(entityAge));
                return true;
            }
            return false;
        }
        else
        {
            return false;
        }
    }
    

    /**
     * Gets the pitch of living sounds in living entities.
     */
    @Override
    protected float getSoundPitch()
    {
    	return FunctionsNEP.getScaledSoundPitch(this);
    }
    
    
    private static float getPitch(Random random)
    {
        return (random.nextFloat() - random.nextFloat()) * 0.2F + 1.0F;
    }
    
    /**
     * Scales the parrot's pitch
     */
    public static float ageScalePitch(int age)
    {
    	return MathHelper.clamp_float(1.0F + 0.5F*(((float)age)/-24000), 1.0F, 1.5F);
    }
    
    @Override
    public String getLivingSound()
    {
		if (!this.isTamed() || rand.nextInt(100) < GeneralConfig.soundOccurrenceParrotAmbient)
		{
			return getAmbientSound(this.rand);
		}
		else
		{
			return "";
		}
    }
    
    
    private static String getAmbientSound(Random random)
    {
        if (random.nextInt(100 * 1000) < GeneralConfig.soundOccurrenceParrotImitate)
        {
        	// Play a random sound from the mimic list
            List<Object[]> list = new ArrayList<Object[]>(MIMIC_SOUNDS.values());
            String ret = (String)list.get(random.nextInt(list.size()))[0];
            return ret == null ? Reference.ENTITY_PARROT_AMBIENT : ret;
        }
        else
        {
        	return Reference.ENTITY_PARROT_AMBIENT;
        }
    }
    
    @Override
    protected String getHurtSound()
    {
        return Reference.ENTITY_PARROT_HURT;
    }
    
    @Override
    protected String getDeathSound()
    {
        return Reference.ENTITY_PARROT_DEATH;
    }
    
    @Override
    protected void func_145780_a(int posX, int posY, int posZ, Block blockIn) // renamed to playStepSound in 1.8
    {
        this.playSound(Reference.ENTITY_PARROT_STEP, 0.15F, 1.0F);
    }

    protected float playFlySound(float distanceWalkedOnStepModified)
    {
        this.playSound(Reference.ENTITY_PARROT_FLY, 0.15F, 1.0F);
        return distanceWalkedOnStepModified + this.flapSpeed / 2.0F;
    }

    protected boolean makeFlySound() {return true;}

    // --- End Sounds --- //
    
    
    
    
    // -------------- //
    // --- MOTION --- //
    // -------------- //

    // This is server only
    @Override
    public void moveEntity(double x, double y, double z)
    {
    	super.moveEntity(x, y, z);
    	
    	// The below was adapted from the move() method in Entity.class from 1.12.2
    	if (!this.noClip)
    	{
			int nextStepDistance_reflected = ReflectionHelper.getPrivateValue(Entity.class, this, new String[]{"nextStepDistance", "field_70150_b"}); // The MCP mapping for this field name
			
			Block block = this.worldObj.getBlock(MathHelper.floor_double(this.posX), MathHelper.floor_double(this.posY - 0.20000000298023224D), MathHelper.floor_double(this.posZ));
			
    		if (
    				(this.canTriggerWalking() && !this.isRiding())
    				&& !(this.distanceWalkedOnStepModified > nextStepDistance_reflected && block.getMaterial() != Material.air)
    				&& (this.distanceWalkedOnStepModified > this.nextFlap && this.makeFlySound() && block.getMaterial() == Material.air)
    				)
            {
                this.nextFlap = this.playFlySound(this.distanceWalkedOnStepModified);
            }
    	}
    }


    /**
     * Suffers no falling damage
     */
    @Override
    public void fall(float distance) //, float damageMultiplier)
    {
    }
    
    @Override
    protected void updateFallState(double y, boolean onGroundIn)//, Block blockIn, BlockPos pos)
    {
    }
    
    /**
     * Returns true if this entity should push and be pushed by other entities when colliding.
     */
    @Override
    public boolean canBePushed() {return true;}
    
    
    @Override
    protected void collideWithEntity(Entity entityIn)
    {
        if (!(entityIn instanceof EntityPlayer)) {super.collideWithEntity(entityIn);}
    }

    // Pasted in from 1.10's Entity class
    public boolean hasNoGravity() {return this.NO_GRAVITY;}
    public void setNoGravity(boolean noGravity) {this.NO_GRAVITY = noGravity;}
    
    public boolean isFlying() {return !this.onGround;}
    
    // Pasted in from EntityLivingBase in 1.10
    protected float getWaterSlowDown() {return 0.8F;}
    

    /**
     * Copied out of 1.12 because moveEntityWithHeading() was changed to travel() and now includes a vertical component
     * It's going to attempt to use this entity's vertical motion value to substitute for the input parameter
     */
    // This is responsible for the entity not understanding the ground
    @Override
    public void moveEntityWithHeading(float strafe, float forward)
    {
        //if (!this.worldObj.isRemote || this.canBeSteered()) // Removed server check to ensure that birds don't flap when on the ground
        {
            if (!this.isInWater())// || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)
            {
                if (!this.handleLavaMovement())// || this instanceof EntityPlayer && ((EntityPlayer)this).capabilities.isFlying)
                {
                	// Movement in air
                	
                    float f6 = 0.91F;
                    BlockPos.PooledMutableBlockPos blockpos$pooledmutableblockpos = BlockPos.PooledMutableBlockPos.retain(this.posX, this.boundingBox.minY - 1.0D, this.posZ);
                    
                    if (this.onGround)
                    {
                        blockpos$pooledmutableblockpos.setPos(this.posX, this.boundingBox.minY - 1.0D, this.posZ);
                        f6 = this.worldObj.getBlock(blockpos$pooledmutableblockpos.getX(), blockpos$pooledmutableblockpos.getY(), blockpos$pooledmutableblockpos.getZ()).slipperiness * 0.91F;
                    }

                    float f7 = 0.16277136F / (f6 * f6 * f6);
                    float f8;

                    if (this.onGround)
                    {
                        f8 = this.getAIMoveSpeed() * f7;
                    }
                    else
                    {
                        f8 = this.jumpMovementFactor;
                    }

                    this.moveRelative(strafe, this.getMoveVertical(), forward, f8);
                    f6 = 0.91F;

                    if (this.onGround)
                    {
                        blockpos$pooledmutableblockpos.setPos(this.posX, this.boundingBox.minY - 1.0D, this.posZ);
                    	f6 = this.worldObj.getBlock(blockpos$pooledmutableblockpos.getX(), blockpos$pooledmutableblockpos.getY(), blockpos$pooledmutableblockpos.getZ()).slipperiness * 0.91F;
                    }

                    if (this.isOnLadder())
                    {
                        float f9 = 0.15F;
                        this.motionX = MathHelper.clamp_double(this.motionX, -0.15000000596046448D, 0.15000000596046448D);
                        this.motionZ = MathHelper.clamp_double(this.motionZ, -0.15000000596046448D, 0.15000000596046448D);
                        this.fallDistance = 0.0F;

                        if (this.motionY < -0.15D)
                        {
                            this.motionY = -0.15D;
                        }
                    }

                    this.moveEntity(this.motionX, this.motionY, this.motionZ);

                    if (this.isCollidedHorizontally && this.isOnLadder())
                    {
                        this.motionY = 0.2D;
                    }
                    
                    /*
                    if (this.isPotionActive(MobEffects.LEVITATION))
                    {
                        this.motionY += (0.05D * (double)(this.getActivePotionEffect(MobEffects.LEVITATION).getAmplifier() + 1) - this.motionY) * 0.2D;
                    }
                    else
                    */
                    {
                        blockpos$pooledmutableblockpos.setPos(this.posX, 0.0D, this.posZ);

                        if (
                        		!this.worldObj.isRemote ||
                        		(
                        			(!(blockpos$pooledmutableblockpos.getX() >= -30000000 && blockpos$pooledmutableblockpos.getZ() >= -30000000
                        			&& blockpos$pooledmutableblockpos.getX() < 30000000 && blockpos$pooledmutableblockpos.getZ() < 30000000
                        			&& blockpos$pooledmutableblockpos.getY() >= 0 && blockpos$pooledmutableblockpos.getY() < 256) ? false :
                        				(this.worldObj.getChunkProvider().chunkExists(blockpos$pooledmutableblockpos.getX() >> 4, blockpos$pooledmutableblockpos.getZ() >> 4)
                        						))
                        				
                        			&& this.worldObj.getChunkFromBlockCoords(blockpos$pooledmutableblockpos.getX(), blockpos$pooledmutableblockpos.getZ()).isChunkLoaded
                        				)
                        		)
                        {
                            if (!this.hasNoGravity())
                            {
                                this.motionY -= 0.08D;
                            }
                        }
                        else if (this.posY > 0.0D)
                        {
                            this.motionY = -0.1D;
                        }
                        else
                        {
                            this.motionY = 0.0D;
                        }
                    }

                    this.motionY *= 0.9800000190734863D;
                    this.motionX *= (double)f6;
                    this.motionZ *= (double)f6;
                    blockpos$pooledmutableblockpos.release();
                }
                else
                {
                	// Movement in lava
                	
                    double d4 = this.posY;
                    this.moveRelative(strafe, this.getMoveVertical(), forward, 0.02F);
                    this.moveEntity(this.motionX, this.motionY, this.motionZ);
                    this.motionX *= 0.5D;
                    this.motionY *= 0.5D;
                    this.motionZ *= 0.5D;

                    if (!this.hasNoGravity())
                    {
                        this.motionY -= 0.02D;
                    }

                    if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d4, this.motionZ))
                    {
                        this.motionY = 0.30000001192092896D;
                    }
                }
            }
            else
            {
            	// Movement in water
            	
                double d0 = this.posY;
                float f1 = this.getWaterSlowDown();
                float f2 = 0.02F;
                
                // Depth Strider introduced in 1.8
                /*
                float f3 = (float)EnchantmentHelper.getDepthStriderModifier(this);

                if (f3 > 3.0F)
                {
                    f3 = 3.0F;
                }

                if (!this.onGround)
                {
                    f3 *= 0.5F;
                }

                if (f3 > 0.0F)
                {
                    f1 += (0.54600006F - f1) * f3 / 3.0F;
                    f2 += (this.getAIMoveSpeed() - f2) * f3 / 3.0F;
                }
                 */
                
                this.moveRelative(strafe, this.getMoveVertical(), forward, f2);
                this.moveEntity(this.motionX, this.motionY, this.motionZ);
                this.motionX *= (double)f1;
                this.motionY *= 0.800000011920929D;
                this.motionZ *= (double)f1;

                if (!this.hasNoGravity())
                {
                    this.motionY -= 0.02D;
                }

                if (this.isCollidedHorizontally && this.isOffsetPositionInLiquid(this.motionX, this.motionY + 0.6000000238418579D - this.posY + d0, this.motionZ))
                {
                    this.motionY = 0.30000001192092896D;
                }
            }
        }
        
        this.prevLimbSwingAmount = this.limbSwingAmount;
        double d5 = this.posX - this.prevPosX;
        double d7 = this.posZ - this.prevPosZ;
        double d9 = this instanceof EntityFlying ? this.posY - this.prevPosY : 0.0D;
        float f10 = MathHelper.sqrt_double(d5 * d5 + d9 * d9 + d7 * d7) * 4.0F;

        if (f10 > 1.0F)
        {
            f10 = 1.0F;
        }

        this.limbSwingAmount += (f10 - this.limbSwingAmount) * 0.4F;
        this.limbSwing += this.limbSwingAmount;
    }
    
    public void moveRelative(float strafe, float up, float forward, float friction)
    {
    	float f = strafe * strafe + up * up + forward * forward;
        
        if (f >= 1.0E-4F)
        {
            f = MathHelper.sqrt_float(f);
            
            if (f < 1.0F) {f = 1.0F;}
            
            f = friction / f;
            strafe = strafe * f;
            up = up * f;
            forward = forward * f;
            
            if(this.isInWater() || this.handleLavaMovement())
            {
                strafe = strafe * (float)this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
                up = up * (float)this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
                forward = forward * (float)this.getEntityAttribute(SWIM_SPEED).getAttributeValue();
            }
            float f1 = MathHelper.sin(this.rotationYaw * 0.017453292F);
            float f2 = MathHelper.cos(this.rotationYaw * 0.017453292F);
            this.motionX += (double)(strafe * f2 - forward * f1);
            this.motionY += (double)up;
            this.motionZ += (double)(forward * f2 + strafe * f1);
        }
    }
    
    // --- End Motion --- //
    
    
    
    // -------------------------- //
    // --- SAVING AND LOADING --- //
    // -------------------------- //

    /**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
    @Override
    public void writeEntityToNBT(NBTTagCompound compound)
    {
        super.writeEntityToNBT(compound);
        
        compound.setInteger("HomePositionX", getHomePosition().posX);
        compound.setInteger("HomePositionY", getHomePosition().posY);
        compound.setInteger("HomePositionZ", getHomePosition().posZ);
        
        compound.setInteger("Variant", this.getVariant());
        
        if(petName != null)
        {
        	compound.setString("PetName", petName);
        }
        compound.setLong("PetBirthday", petBirthday);
        
        compound.setFloat("Food", food);
        compound.setFloat("Hygiene", hygiene);
        compound.setFloat("Energy", energy);
    }

    /**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
    @Override
    public void readEntityFromNBT(NBTTagCompound compound)
    {
        super.readEntityFromNBT(compound);
        
        setHomeArea(compound.getInteger("HomePositionX"), compound.getInteger("HomePositionY"), compound.getInteger("HomePositionZ"), 16);

        
        this.setVariant(compound.getInteger("Variant"));
        
        if(compound.hasKey("PetName"))
        {
        	petName = compound.getString("PetName");
        }
        petBirthday = compound.getLong("PetBirthday");
        
        food = compound.getFloat("Food");
        hygiene = compound.getFloat("Hygiene");
        energy = compound.getFloat("Energy");
    }
    
	@Override
	public boolean canDespawn()
	{
		return false;
	}

	@Override
	public long getPetBirthday() {
		return petBirthday;
	}

	@Override
	public void setPetBirthday(long birthday) {
		petBirthday = birthday;
	}

	@Override
	public String getPetName() {
		return petName;
	}

	@Override
	public void setPetName(String name) {
		petName = name;
	}
	
	@Override
	public void setEnergy(float energy) {
		if(energy > GeneralConfig.MAX_ENERGY)
		{
			energy = GeneralConfig.MAX_ENERGY;
		}
		
		if(energy < 0)
		{
			energy = 0;
		}
		
		this.energy = energy;
	}

	@Override
	public void setFood(float food) {
		if(food > GeneralConfig.MAX_FOOD)
		{
			food = GeneralConfig.MAX_FOOD;
		}
		
		if(food < 0)
		{
			food = 0;
		}
		
		this.food = food;
	}
	
	@Override
	public void setHygiene(float hygiene) {
		
		if(hygiene > GeneralConfig.MAX_HYGIENE)
		{
			hygiene = GeneralConfig.MAX_HYGIENE;
		}
		
		if(hygiene < 0)
		{
			hygiene = 0;
		}
		
		this.hygiene = hygiene;
	}

	@Override
	public float getEnergy() {
		return energy;
	}

	@Override
	public float getFood() {
		return food;
	}

	@Override
	public float getHygiene() {
		return hygiene;
	}
	
	@Override
    public void playTameEffect(boolean bool)
    {
        String s = "heart";

        if (!bool)
        {
            s = "smoke";
        }

        for (int i = 0; i < 7; ++i)
        {
            double d0 = this.rand.nextGaussian() * 0.02D;
            double d1 = this.rand.nextGaussian() * 0.02D;
            double d2 = this.rand.nextGaussian() * 0.02D;
            this.worldObj.spawnParticle(s, this.posX + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, this.posY + 0.5D + (double)(this.rand.nextFloat() * this.height), this.posZ + (double)(this.rand.nextFloat() * this.width * 2.0F) - (double)this.width, d0, d1, d2);
        }
    }
	
	@Override
	public float getMood() {
		return (getFood() + getHygiene() + getEnergy()) / 3F;
	}
    
	@Override
	public PetStateEnum getPetState() 
	{
		return state;
	}

	@Override
	public void setPetState(PetStateEnum state) 
	{
		if(state == PetStateEnum.REST)
		{
			this.aiSit.setSitting(true);
		}
		else
		{
			this.aiSit.setSitting(false);
		}
		
		
		this.state = state;
	}
	
	@Override
	public int getAge()
	{
		int age = (int) (((System.currentTimeMillis() - getPetBirthday()) / 1000) / 31104000);
		return age;
	}
	
	@Override
	public void setTamedImplements(boolean value) {
		this.setTamed(value);	
	}
	

	@Override
	public void setGrowingAgeImplements(int growthTick) {
		this.setGrowingAge(growthTick);
	}

	@Override
	public void setHomeAreaImplements(int x, int y, int z, int range) {
		this.setHomeArea(x, y, z, range);
	}
	
	@Override
	public EntityLivingBase getOwnerImplements() {
		return getOwner();
	}
	

	@Override
	public ChunkCoordinates getHomePositionImplements() {
		return getHomePosition();
	}
	

	@Override
	public int getGrowingAgeImplements() {
		return getGrowingAge();
	}
	
	@Override
	public boolean isSittingImplements() {
		return isSitting();
	}
	
	@Override
	public int getAgeImplements() {
		return getAge();
	}
	
	@Override
	public float func_110174_bM_Implements() {
		return func_110174_bM();
	}

	@Override
	public boolean isWithinHomeDistanceCurrentPositionImplements() {
		return isWithinHomeDistanceCurrentPosition();
	}
	
	@Override
	public void playTameEffectImplements(boolean bool) {
		playTameEffect(bool);
	}
	

	@Override
	public void func_152115_bImplements(String UUID) {
		this.func_152115_b(UUID);
	}

	@Override
	public EntityAISit func_70907_rImplements() {
		return this.func_70907_r();
	}


}