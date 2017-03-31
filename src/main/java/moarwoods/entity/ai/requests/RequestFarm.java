package moarwoods.entity.ai.requests;

import moarwoods.capability.CapabilityFarmer;
import moarwoods.entity.ai.EntityAIVillagerRequests;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public class RequestFarm implements Request
{
	public static final RequestInstanceFactory<RequestFarm> REQUEST_FACTORY = new RequestInstanceFactory<RequestFarm>(new ResourceLocation("moarwoods:farming"), RequestFarm.class)
	{
		@Override
		public RequestFarm newInstance(EntityVillager villager, NBTTagCompound compound)
		{
			return new RequestFarm(villager, new ItemStack(compound));
		}
	};
	private final EntityVillager villager;
	private final ItemStack stack;
	private EntityAIVillagerRequests requests;
	
	public RequestFarm(EntityVillager villager, ItemStack stack)
	{
		this.villager = villager;
		this.stack = stack;
	}
	
	@Override
	public boolean merge(EntityAIVillagerRequests requests, Request request)
	{
		if(request instanceof RequestFarm)
		{
			ItemStack stack = ((RequestFarm)request).stack;
			if(this.stack.isItemEqual(stack))
			{
				this.stack.setCount(this.stack.getCount() + stack.getCount());
				return true;
			}
		}
		return false;
	}
	
	@Override
	public boolean canAcceptRequest(EntityAIVillagerRequests requests)
	{
		EntityVillager villager = requests.getVillager();
		if(!villager.isDead && villager.hasCapability(CapabilityFarmer.FARMER, null))
			return villager.getCapability(CapabilityFarmer.FARMER, null).canFarm(this.stack);
		return false;
	}
	
	@Override
	public void pairRequests(EntityAIVillagerRequests requests)
	{
		this.requests = requests;
	}
	
	@Override
	public boolean requestComplete()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
	@Override
	public void startRequest()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public void continueRequest()
	{
		// TODO Auto-generated method stub
		
	}
	
	@Override
	public NBTTagCompound getNBT()
	{
		// TODO Add what task a villager is currently on
		NBTTagCompound compound = new NBTTagCompound();
		compound.setTag("item", this.stack.serializeNBT());
		return null;
	}
	
	@Override
	public boolean canChange()
	{
		// TODO Auto-generated method stub
		return false;
	}
	
}
