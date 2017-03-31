package moarwoods.entity.ai;

import net.minecraft.entity.Entity;
import net.minecraft.entity.ai.EntityAIRunAroundLikeCrazy;
import net.minecraft.entity.passive.AbstractHorse;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.entity.player.EntityPlayer;

public class EntityAIRunAroundLikeCrazyWrapper extends EntityAIRunAroundLikeCrazy
{
	private final AbstractHorse horse;
	
	public EntityAIRunAroundLikeCrazyWrapper(AbstractHorse horse, double speed)
	{
		super(horse, speed);
		this.horse = horse;
	}
	
	@Override
	public void updateTask()
	{
		if(this.horse.getRNG().nextInt(50) == 0)
		{
			Entity entity = this.horse.getPassengers().get(0);
			if(entity == null)
				return;
			if(entity instanceof EntityPlayer)
			{
				int i = this.horse.getTemper();
				int j = this.horse.getMaxTemper();
				
				if(j > 0 && this.horse.getRNG().nextInt(j) < i)
				{
					this.horse.setTamedBy((EntityPlayer)entity);
					return;
				}
				
				this.horse.increaseTemper(5);
			}
			if(entity instanceof EntityVillager)
			{
				int i = this.horse.getTemper();
				int j = this.horse.getMaxTemper();
				if(j > 0 && this.horse.getRNG().nextInt(j) < i)
				{
					this.horse.setOwnerUniqueId(entity.getUniqueID());
					this.horse.setHorseTamed(true);
					this.horse.getEntityWorld().setEntityState(this.horse, (byte)7);
					return;
				}
				this.horse.increaseTemper(5);
			}
			this.horse.removePassengers();
			this.horse.makeMad();
			this.horse.world.setEntityState(this.horse, (byte)6);
		}
	}
}
