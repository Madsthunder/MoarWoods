package moarwoods.entity.ai.requests;

import moarwoods.entity.ai.EntityAIVillagerRequests;
import net.minecraft.nbt.NBTTagCompound;

public interface Request
{
	default public boolean merge(EntityAIVillagerRequests requests, Request request)
	{
		return false;
	}
	
	public boolean canAcceptRequest(EntityAIVillagerRequests requests);
	
	public void pairRequests(EntityAIVillagerRequests requests);
	
	public boolean requestComplete();
	
	public void startRequest();
	
	public void continueRequest();
	
	public NBTTagCompound getNBT();
	
	public boolean canChange();
	
	default public boolean hasChanged()
	{
		return false;
	}
	
	default public void notifyRecievedChanges()
	{
		
	}
}