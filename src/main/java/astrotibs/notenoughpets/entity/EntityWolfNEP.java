package astrotibs.notenoughpets.entity;

import java.util.Iterator;

import astrotibs.notenoughpets.ai.EntityAIFollowOwnerNEP;
import astrotibs.notenoughpets.config.GeneralConfig;
import astrotibs.notenoughpets.util.FunctionsNEP;
import astrotibs.notenoughpets.util.SkinVariations;
import cpw.mods.fml.relauncher.ReflectionHelper;
import fr.craftyourliferp.main.CraftYourLifeRPMod;
import fr.craftyourliferp.network.PacketPet;
import fr.craftyourliferp.utils.ServerUtils;
import net.minecraft.block.BlockColored;
import net.minecraft.entity.EntityAgeable;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.ai.EntityAIFollowOwner;
import net.minecraft.entity.ai.EntityAIOwnerHurtByTarget;
import net.minecraft.entity.ai.EntityAIOwnerHurtTarget;
import net.minecraft.entity.ai.EntityAISit;
import net.minecraft.entity.ai.EntityAITasks;
import net.minecraft.entity.passive.EntitySheep;
import net.minecraft.entity.passive.EntityWolf;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.entity.player.EntityPlayerMP;
import net.minecraft.init.Items;
import net.minecraft.item.ItemFood;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.world.World;

public class EntityWolfNEP extends EntityWolf implements IPetData {

	private long petBirthday;
	
	private String petName;
	
	private float hygiene;
		
	private float energy;
	
	private float food;
	
	private PetStateEnum state = PetStateEnum.REST;
	
	public EntityWolfNEP(World world) {
		super(world);
		
		
		hygiene = GeneralConfig.MAX_HYGIENE;
		energy = GeneralConfig.MAX_ENERGY;
		food = GeneralConfig.MAX_FOOD;

		EntityAIFollowOwnerNEP.removeVanillaFollow(this);
		Iterator iterator = tasks.taskEntries.iterator();
		
		this.tasks.addTask(5, new EntityAIFollowOwnerNEP(this, 1.0D, 10.0F, 2.0F));
	}

	// v2.0.0 stray non-wolves don't attack sheep
	@Override
	public void setAttackTarget(EntityLivingBase entitylivingbaseIn)
    {
		super.setAttackTarget(entitylivingbaseIn);
		
		if (this.getTameSkin() != 0 && !this.isTamed() && super.getAttackTarget() instanceof EntitySheep)
		{
			this.setAngry(false);
			super.setAttackTarget(null);
			return;
		}
    }
	
	@Override
	protected String getLivingSound()
	{
		// Suppression only applies to tamed - v2.0.0
		if (!this.isTamed() || rand.nextInt(100) < GeneralConfig.soundOccurrenceDog)
		{
			// Pugs sometimes growl instead of panting
			if (this.getTameSkin()==7)
			{
				return this.isAngry() ?
						"mob.wolf.growl" :
							(this.rand.nextInt(3) == 0 ?
									(this.isTamed() && this.dataWatcher.getWatchableObjectFloat(18) < 10.0F ?
											"mob.wolf.whine" :
												this.rand.nextBoolean() ? "mob.wolf.panting" : "mob.wolf.growl") :
													"mob.wolf.bark");
			}
			else
			{
				return super.getLivingSound();
			}
		}
		else
		{
			return null;
		}
	}
	
	
	// Added in v1.2
	/**
     * Gets the pitch of living sounds in living entities.
     */
	@Override
    protected float getSoundPitch()
    {
		return FunctionsNEP.getScaledSoundPitch(this) + 0.7F*(1F/dogBreedScale(this)-1F); //v2.0.0 - pitch up sounds for smaller breeds
    }

	// v2.0.0 - How to scale the dog's size based on its breed
    public static float dogBreedScale(EntityWolfNEP wolf)
    {
    	switch (wolf.getTameSkin())
    	{
    		case 7: // Pug
    			return 0.5F;
    		case 9: // Boston Terrier
    			return 0.6F;
    		case 4: // White Terrier
    			return 0.7F;
    		case 6: // Beagle
    			return 0.8F;
    		default:
    			return 1.0F;
    		case 11: // Golden Retriever
    			return 1.1F;
    		case 10: // St. Bernard -- v2.1.0
    			return 1.2F;
    	}
    }
    
    public void onLivingUpdate()
    {
        super.onLivingUpdate();

        if(!worldObj.isRemote)
        {
            if(rand.nextInt(250) == 0)
            {
            	if(getMood() <= 10F)
    			{
    				this.playSound("mob.wolf.whine", this.getSoundVolume(), FunctionsNEP.getScaledSoundPitch(this)); // No isSilent() flag until 1.8
    			}
            }
        }
    }
    
	
	// Added in v1.2.1
	@Override
	protected void entityInit()
	{
		super.entityInit();
		
        // A datawatcher value to represent type: for new skins
        this.dataWatcher.addObject(22, Byte.valueOf((byte)0));
        
	}

	// Added in v1.2.1
    public int getTameSkin()
    {
        return this.dataWatcher.getWatchableObjectByte(22);
    }
	
	// Added in v1.2.1
    public void setTameSkin(int skinId)
    {
        this.dataWatcher.updateObject(22, Byte.valueOf((byte)skinId));
    }
	
    // Added in v1.2.1
    @Override
    public void writeEntityToNBT(NBTTagCompound tagCompound)
    {
        super.writeEntityToNBT(tagCompound);
        tagCompound.setInteger("DogType", this.getTameSkin());
        
        if(petName != null)
        {
        	tagCompound.setString("PetName", petName);
        }
    	tagCompound.setLong("PetBirthday", petBirthday);
    	
    	tagCompound.setFloat("Food", food);
    	tagCompound.setFloat("Hygiene", hygiene);
    	tagCompound.setFloat("Energy", energy);

    }
    
    // Added in v1.2.1
    @Override
    public void readEntityFromNBT(NBTTagCompound tagCompound)
    {
        super.readEntityFromNBT(tagCompound);
        this.setTameSkin(tagCompound.getInteger("DogType"));
        
        if(tagCompound.hasKey("PetName"))
        {
        	petName = tagCompound.getString("PetName");
        }
        petBirthday = tagCompound.getLong("PetBirthday");
        
        food = tagCompound.getFloat("Food");
        hygiene = tagCompound.getFloat("Hygiene");
        energy = tagCompound.getFloat("Energy");
    }
    
    // Added in v1.2.1
    @Override
    public EntityWolfNEP createChild(EntityAgeable otherParent)
    {
    	EntityWolfNEP babyWolf = new EntityWolfNEP(this.worldObj);
    	
        if (this.isTamed())
        {
        	babyWolf.func_152115_b(babyWolf.getUniqueID().toString());
            babyWolf.setTamed(true);
            // Set the offspring to a random variant based on the parents - v2.0.0
            try
            {
            	babyWolf.setTameSkin( this.worldObj.rand.nextBoolean() ? this.getTameSkin() : ((EntityWolfNEP)otherParent).getTameSkin() );
            }
            catch (Exception e) {babyWolf.setTameSkin(this.getTameSkin());}
            // v2.0.0 randomly select a skin that has nothing to do with the parents - MUTATION!
            if (this.worldObj.rand.nextInt(1000)==0) {babyWolf.setTameSkin(SkinVariations.dogSkinArray.length);}
        }

        return babyWolf;
    }

    // Added in v2.0.0
    @Override
    public boolean interact(EntityPlayer player)
    {
    	if(player.worldObj.isRemote) return true;
    	
    	
    	ItemStack itemstack = player.inventory.getCurrentItem();
    	
    	// Play a sound when you dye the wolf's collar
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
    			else
    			{
    				ServerUtils.sendChatMessage(player, "§bVotre animal est en repos");
    			}
    			
    			setPetState(PetStateEnum.values()[petState]);
    			return true;
    		}
    		
    		if (itemstack!=null)
    		{
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
        				this.playSound("dig.sand", this.getSoundVolume(), FunctionsNEP.getScaledSoundPitch(this)); // No isSilent() flag until 1.8
        				
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
            				ReflectionHelper.setPrivateValue(EntityWolf.class, this, true, 2);
						}
						else
						{
							ServerUtils.sendChatMessage(player, "§cVotre animal est propre");
						}
					}
        			else
        			{
        				if(getFood() >= GeneralConfig.MAX_FOOD && getHealth() >= getMaxHealth())
        				{
            				ServerUtils.sendChatMessage(player, "§cVotre animal refuse de manger");
            				return true;
        				}
            			
            			if((System.currentTimeMillis() - getPetBirthday()) < 86400*7*8*1000)
            			{
                			if(itemstack.getItem() != Items.milk_bucket)
                			{
                				ServerUtils.sendChatMessage(player, "§cVotre animal refuse de manger");
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
                			}
                			return true;
            			}
            			else
            			{
            				if(itemstack.getItem() instanceof ItemFood)
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

                    			}
                    			return true;
            				}
            				
            			}
        			}
    				
        		}
        		
    		}
        }
    	
    	return true;
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
}
