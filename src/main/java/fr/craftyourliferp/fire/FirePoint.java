package fr.craftyourliferp.fire;

import fr.craftyourliferp.main.CraftYourLifeRPMod;
import net.minecraft.init.Blocks;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;

public class FirePoint {

	public int x;
	public int y;
	public int z;
	
	public FirePoint linkedTo;


	
	private float inflammability; //between 0 and 1 more inflammability is high more fire progression is fast
	
	private float inflammation = 0.1f;
	
	private Fire fire;
	
	public boolean wasFire = false;
	
	
	
	public FirePoint(Fire fire,int x, int y, int z, FirePoint linkedTo)
	{		
		this.fire = fire;
		
		this.x = x;
		this.y = y;
		this.z = z;
		
		this.linkedTo = linkedTo;
	}
	
	public void setInflammation(float value)
	{
		this.inflammation = value;
	}
	
	public void tick(World world)
	{

		inflammability = Blocks.fire.getFlammability(world.getBlock(x, y-1, z)) / 100f;
		
		if(world.getBlock(x, y, z) != Blocks.fire)
		{
			if(world.getTotalWorldTime() % 20 == 0)
			{
				inflammation += MathHelper.getRandomDoubleInRange(world.rand, 0, (1 * inflammability * 0.1f) + 0.050f);
			}
			
			if(linkedTo != null)
			{
				if(world.getBlock(linkedTo.x, linkedTo.y, linkedTo.z) != Blocks.fire)
				{
					fire.removeFirePoint(world, x, y, z);
				}
				
				if(inflammation >= 1f)
				{
					if(wasFire)
					{
						fire.removeFirePoint(world, x, y, z);
					}
					else
					{
						world.setBlock(x, y, z, Blocks.fire);
						wasFire = true;
						fire.addPointFrom(world, this);
					}
				}
				
				
			}
			else //Main point
			{
				
				if(fire.points.size() == 1 && fire.points.get(0) == this && world.getBlock(x, y, z) == Blocks.air)
				{
					fire.removeFirePoint(world, x, y, z);
				}
			}
			
			
		}
		else
		{
			
			if(world.getTotalWorldTime() % 20 == 0) inflammation -= MathHelper.getRandomDoubleInRange(world.rand, 0, 0.0000002510D);

			
			if(inflammation < 0)
			{

				if(world.getBlock(x, y, z) == Blocks.fire)
				{
					world.setBlock(x, y, z, Blocks.air);
				}
				fire.removeFirePoint(world, x, y, z);
			}
		}
		
		if(world.isRaining() && world.getBlock(x, y+1, z) == Blocks.air)
		{
			inflammation -= MathHelper.getRandomDoubleInRange(world.rand, 0, 0.000002);
		}
				
		
	}
	

}
