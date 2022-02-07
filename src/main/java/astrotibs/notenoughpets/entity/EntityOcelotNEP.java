package astrotibs.notenoughpets.entity;

import java.lang.reflect.Method;

import astrotibs.notenoughpets.ai.EntityAIFollowOwnerNEP;
import astrotibs.notenoughpets.config.GeneralConfig;
import astrotibs.notenoughpets.util.FunctionsNEP;
import astrotibs.notenoughpets.util.Reference;
import astrotibs.notenoughpets.util.SkinVariations;
import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketPet;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.IEntityLivingData;
import net.minecraft.entity.SharedMonsterAttributes;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAITempt;
import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.passive.EntityOcelot;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.Item;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ChunkCoordinates;
import net.minecraft.world.World;

public class EntityOcelotNEP extends EntityOcelot implements IPetData {

	private long petBirthday;
	
	private String petName;
	
	private float hygiene;
	
	private float food;
	
	private float energy;
	
	private PetStateEnum state = PetStateEnum.REST;

	
	public EntityOcelotNEP(World world) {
		
		super(world);
		EntityAIFollowOwnerNEP.removeVanillaFollow(this);
		
		hygiene = GeneralConfig.MAX_HYGIENE;
		energy = GeneralConfig.MAX_ENERGY;
		food = GeneralConfig.MAX_FOOD;

		this.tasks.addTask(5, new EntityAIFollowOwnerNEP(this, 1.0D, 10.0F, 5.0F));
	}
	
	@Override
	protected void entityInit()
	{
		super.entityInit();
		this.dataWatcher.addObject(20, new Byte((byte)BlockColored.func_150032_b(1)));
	}
	
	
	/**
     * (abstract) Protected helper method to write subclass entity data to NBT.
     */
	@Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        
        tagCompound.setInteger("HomePositionX", getHomePosition().posX);
        tagCompound.setInteger("HomePositionY", getHomePosition().posY);
        tagCompound.setInteger("HomePositionZ", getHomePosition().posZ);

        
        tagCompound.setByte("CollarColor", (byte)this.getCollarColor());
        
        if(petName != null)
        {
        	tagCompound.setString("PetName", petName);
        }
        tagCompound.setLong("PetBirthday", petBirthday);
        
        tagCompound.setFloat("Food", food);
        tagCompound.setFloat("Hygiene", hygiene);
        tagCompound.setFloat("Energy", energy);
    }
	
	/**
     * (abstract) Protected helper method to read subclass entity data from NBT.
     */
	@Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        
        setHomeArea(tagCompound.getInteger("HomePositionX"), tagCompound.getInteger("HomePositionY"), tagCompound.getInteger("HomePositionZ"), 16);

        if (tagCompound.hasKey("CollarColor", 99))
        {
            this.setCollarColor(tagCompound.getByte("CollarColor"));
        }
        
        if(tagCompound.hasKey("PetName"))
        {
        	petName = tagCompound.getString("PetName");
        }
        petBirthday = tagCompound.getLong("PetBirthday");
        
        food = tagCompound.getFloat("Food");
        hygiene = tagCompound.getFloat("Hygiene");
        energy = tagCompound.getFloat("Energy");
    }
    
	
	@Override
	public void onLivingUpdate()
	{
	
	    if(!worldObj.isRemote)
	    {
	    	if(rand.nextInt(250) == 0)
	        {
	    		if(getMood() <= 10F)
	    		{
	    			this.playSound(Reference.SE_CAT_BEG, this.getSoundVolume(), FunctionsNEP.getScaledSoundPitch(this)); // No isSilent() flag until 1.8
	    		}
	        }
	    }
	    
		
		super.onLivingUpdate();
		
		// If this is not an Ocelot, make it avoid water
		if (this.getTameSkin()!=0 && !this.getNavigator().getAvoidsWater()) {
			this.getNavigator().setAvoidsWater(true);
		}
		else if (this.getTameSkin()==0 && this.getNavigator().getAvoidsWater()) {
			this.getNavigator().setAvoidsWater(false);
		}
		//this.tasks.removeTask( this.followTaskOld );
	}
	
	@Override
	protected Item getDropItem()
    {
		if (this.getTameSkin()==0 && this.getGrowingAge()>=0 )
		{
			return Items.leather;
		}
		else return null;
    }
    
    
    /**
     * If this is not an ocelot or kitten, drop string
     */
	@Override
    protected void dropFewItems(boolean recentlyHit, int looting)
    {
		if (this.getTameSkin()!=0 && this.getGrowingAge()>=0)
		{
	        int i = this.rand.nextInt(3) + this.rand.nextInt(looting + 1);
	        
	        if (i > 0)
	        {
	            this.entityDropItem(new ItemStack(Items.string, i, 0), 1.0F);
	        }
		}
		
    }
	


    /**
     * Called when a player interacts with a mob. Specifically, giving a fish to the kitty.
     */
	@Override
	// Re-written in v2.0.0
    public boolean interact(EntityPlayer player)
    {
    	if(worldObj.isRemote) return true;

    	if(getOwner() != player)
    	{
    		return true;
    	}
    	
		ItemStack itemstack = player.inventory.getCurrentItem();
		
		EntityAITempt aiTempt_f = ReflectionHelper.getPrivateValue( EntityOcelot.class, this, new String[]{"aiTempt", "field_70914_e"} );
        
        if (this.isTamed())
        {
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

        		// Dye the collar
        		if (itemstack.getItem() == Items.dye)
        		{
        			int dyeColorInt = BlockColored.func_150032_b(itemstack.getItemDamage());
        			
                    if (dyeColorInt != this.getCollarColor())
                    {
                        this.setCollarColor(dyeColorInt);

                        if (!player.capabilities.isCreativeMode) 
                        {
                        	--itemstack.stackSize;
            				if (itemstack.stackSize <= 0) {player.inventory.setInventorySlotContents(player.inventory.currentItem, (ItemStack)null);}
                        }
        				this.playSound("dig.sand", this.getOcelotVolume(), FunctionsNEP.getScaledSoundPitch(this)); // No isSilent() flag until 1.8

        				return true;
                    }
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
        					if(!(itemstack.getItem() instanceof ItemFood) && itemstack.getItem() != Items.milk_bucket) return true;

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
	                				CraftYourLifeRPMod.packetHandler.sendTo(PacketPet.playEffect(getEntityId()), (EntityPlayerMP) player);
	                				
	                				this.playSound(Reference.SE_CAT_EAT, this.getOcelotVolume(), FunctionsNEP.getScaledSoundPitch(this)); // No isSilent() flag until 1.8
	
	                			}
	                			return true;
	            			}
	            			else
	            			{
	            				ItemFood itemFood = (ItemFood)itemstack.getItem();
	                			// Heal the dog
	                			if (this.isBreedingItem(itemstack) && (this.getHealth() < this.getMaxHealth()))
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
	                				
	                				CraftYourLifeRPMod.packetHandler.sendTo(PacketPet.playEffect(getEntityId()), (EntityPlayerMP) player);
	                				
	                				this.playSound(Reference.SE_CAT_EAT, this.getOcelotVolume(), FunctionsNEP.getScaledSoundPitch(this)); // No isSilent() flag until 1.8
	
	                			}
	                			return true;
	            			}
	            		}
        			}
       				
        		
            }
        	
        }
    	return true;
        
    }

	
	/**
     * Forces the baby to be the NEC type - v1.3.0
     */
	@Override
	public EntityOcelotNEP createChild(EntityAgeable otherParent)
    {
		EntityOcelotNEP babyOcelot = new EntityOcelotNEP(this.worldObj);

        if (this.isTamed())
        {
            babyOcelot.func_152115_b(this.func_152113_b());
            babyOcelot.setTamed(true);
            // Set the offspring to a random variant based on the parents - v2.0.0
            try
            {
            	babyOcelot.setTameSkin( this.worldObj.rand.nextBoolean() ? this.getTameSkin() : ((EntityOcelotNEP)otherParent).getTameSkin() );
            }
            catch (Exception e) {babyOcelot.setTameSkin(this.getTameSkin());}
            // v2.0.0 randomly select a skin that has nothing to do with the parents - MUTATION!
            if (this.worldObj.rand.nextInt(1000)==0) {babyOcelot.setTameSkin(SkinVariations.catSkinArray.length);}
        }
        return babyOcelot;
    }
	
    
	// Added in v1.2
	/**
     * Gets the pitch of living sounds in living entities.
     */
	@Override
    protected float getSoundPitch()
    {
		return FunctionsNEP.getScaledSoundPitch(this);
    }

	// Turned off babies because I don't want them with spawn eggs. They spawn just fine vanilla-style - v1.4.0
	@Override
    public IEntityLivingData onSpawnWithEgg(IEntityLivingData ientitylivingdata)
    {
        //ientitylivingdata = super.onSpawnWithEgg(ientitylivingdata);
        this.getEntityAttribute(SharedMonsterAttributes.followRange).applyModifier(new AttributeModifier("Random spawn bonus", this.rand.nextGaussian() * 0.05D, 1));
        
        /*
        // Make kittens!
        if (this.worldObj.rand.nextInt(7) == 0)
        {
        	// Make them each be 50/50 between having this parent's skin and another arbitrary parent's skin
        	
        	int otherParentType=0;
        	// This is not a jungle ocelot
        	if (!(this.getTameSkin()==0)) {otherParentType = this.worldObj.rand.nextInt(SkinVariations.catSkinArray.length-1)+1;}
        	
            for (int i = 0; i < 2; ++i)
            {
            	EntityOcelotNEC entityocelot = new EntityOcelotNEC(this.worldObj); // Changed in v1.3.0
                entityocelot.setLocationAndAngles(this.posX, this.posY, this.posZ, this.rotationYaw, 0.0F);
                entityocelot.setGrowingAge(-24000);
                entityocelot.setTameSkin(this.worldObj.rand.nextBoolean() ? this.getTameSkin() : otherParentType);
                this.worldObj.spawnEntityInWorld(entityocelot);
            }
        }
        */
        return ientitylivingdata;
    }
	
	// All sound entries re-written in v 1.1
	@Override
	protected String getLivingSound()
	{
		if (this.isTamed())
		{
			// Suppression only applies to tamed - v2.0.0
			if (rand.nextInt(100) < GeneralConfig.soundOccurrenceCat)
			{
				if (this.isInLove())
				{
					return "mob.cat.purr";
				}
				else
				{
					if (this.getTameSkin()==0)
					{
						return Reference.SE_CAT_OCELOT_IDLE; // Ocelot idle sound
					}
					else
					{
						return (this.rand.nextInt(4) == 0 ? "mob.cat.purreow" : "mob.cat.meow"); // Non-ocelot idle sound
					}
				}
			}
		}
		else
		{
			EntityAITempt aiTempt_f = ReflectionHelper.getPrivateValue(
					EntityOcelot.class, this, new String[]{"aiTempt", "field_70914_e"});
	        
			if (
					aiTempt_f != null &&
					aiTempt_f.isRunning() // "Running" means "executing"
					)
	        {
				return Reference.SE_CAT_BEG; // Cat is begging for fishies
	        }
			else
			{
				if (!(this.getTameSkin()==0))
				{
					return Reference.SE_CAT_STRAY_IDLE; // Sound made when not tamed and not an ocelot
				}
			}
		}
		return null;
	}
	// Cancel hurt and death sounds: they are covered in the event handler.
	@Override
    protected String getHurtSound()
    {
		return null;
    }
	@Override
    protected String getDeathSound()
    {
		return null;
    }
	
    /**
     * Return this ocelot's collar color.
     */
    public int getCollarColor()
    {
        return this.dataWatcher.getWatchableObjectByte(20) & 15;
    }

    /**
     * Set this ocelot's collar color.
     */
    public void setCollarColor(int p_82185_1_)
    {
        this.dataWatcher.updateObject(20, Byte.valueOf((byte)(p_82185_1_ & 15)));
    }
    
    // Added in v1.2
	/**
	 * This accesses the EntityOcelot.getSoundVolume() value, which is 0.4F.
	 * This method exists just in case some other mod wants to adjust that value.
	 */
	public float getOcelotVolume() {
		
		try {
			Method getSoundVolume_m = ReflectionHelper.findMethod(EntityOcelot.class, this, new String[]{"getSoundVolume", "func_70599_aP"});
			return (Float)getSoundVolume_m.invoke(this);
			}
		catch (Exception e) {}
		
		return 0.4F;
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
	public boolean isSitting()
	{
		return super.isSitting();
	}
	
	@Override
	public int getAge()
	{
		int age = (int) (((System.currentTimeMillis() - getPetBirthday()) / 1000) / 31104000);
		return age;
	}
	
	@Override
	public void setTamedImplements(boolean value)
	{
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
