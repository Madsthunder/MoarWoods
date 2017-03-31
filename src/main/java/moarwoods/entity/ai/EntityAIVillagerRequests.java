package moarwoods.entity.ai;

import java.util.ArrayList;
import java.util.Comparator;

import com.google.common.collect.Lists;

import moarwoods.entity.ai.requests.IDeadlinedRequest;
import moarwoods.entity.ai.requests.Request;
import moarwoods.entity.ai.requests.RequestInstanceFactory;
import net.minecraft.entity.ai.EntityAIBase;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.nbt.NBTTagList;

public class EntityAIVillagerRequests extends EntityAIBase
{
	private final Comparator<Request> comparator;
	private final EntityVillager villager;
	private final ArrayList<Request> requests = Lists.newArrayList();
	private Request currentRequest;
	
	public EntityAIVillagerRequests(EntityVillager villager)
	{
		this.villager = villager;
		this.comparator = new RequestComparator(this);
	}
	
	public EntityVillager getVillager()
	{
		return this.villager;
	}
	
	@Override
	public boolean shouldExecute()
	{
		return !this.requests.isEmpty();
	}
	
	@Override
	public void startExecuting()
	{
		if(this.requests.isEmpty())
			return;
		(this.currentRequest = this.requests.get(0)).startRequest();
	}
	
	@Override
	public boolean continueExecuting()
	{
		return this.currentRequest != null;
	}
	
	@Override
	public void updateTask()
	{
		for(Request request : this.requests)
			if(request.canChange() && request.hasChanged())
			{
				request.notifyRecievedChanges();
				this.requests.sort(this.comparator);
			}
		if(this.requests.contains(this.currentRequest))
		{
			if(this.currentRequest.requestComplete())
				this.requests.remove(this.currentRequest);
		}
		if(!this.requests.isEmpty())
			this.currentRequest = this.requests.get(0);
		return;
	}
	
	@Override
	public void resetTask()
	{
		this.currentRequest = null;
	}
	
	public boolean addRequest(Request request)
	{
		for(Request r : this.requests)
			if(r.merge(this, request))
				return true;
		this.requests.add(request);
		this.requests.sort(this.comparator);
		request.pairRequests(this);
		// TODO Add Conditions
		return true;
	}
	
	public NBTTagList getNBT()
	{
		NBTTagList list = new NBTTagList();
		for(Request request : this.requests)
		{
			RequestInstanceFactory factory = RequestInstanceFactory.getByClass(request.getClass());
			if(factory != RequestInstanceFactory.NULL)
			{
				NBTTagCompound compound = request.getNBT();
				compound.setString("name", factory.getName().toString());
				list.appendTag(compound);
			}
		}
		return list;
	}
	
	public void loadFromNBT(NBTTagList list)
	{
		for(int i = 0; i < list.tagCount(); i++)
		{
			NBTTagCompound compound = list.getCompoundTagAt(i);
			Request request = RequestInstanceFactory.getByName(compound.getString("name")).newInstance(this.villager, compound);
			if(request != null)
			{
				this.requests.add(request);
				request.pairRequests(this);
			}
		}
	}
	
	private static class RequestComparator implements Comparator<Request>
	{
		private final EntityAIVillagerRequests requests;
		
		public RequestComparator(EntityAIVillagerRequests requests)
		{
			this.requests = requests;
		}
		
		@Override
		public int compare(Request request1, Request request2)
		{
			boolean[] tests = new boolean[] { request1 instanceof IDeadlinedRequest, request2 instanceof IDeadlinedRequest };
			if(tests[0] && !tests[1])
				return -1;
			else if(!tests[0] && tests[1])
				return 1;
			else if(tests[0] && tests[1])
			{
				boolean f1 = ((IDeadlinedRequest)request1).forgivableRequest();
				boolean f2 = ((IDeadlinedRequest)request1).forgivableRequest();
				if(!f1 && f2)
					return -1;
				if(f1 && !f2)
					return 1;
				long d1 = ((IDeadlinedRequest)request1).getDeadline();
				long d2 = ((IDeadlinedRequest)request1).getDeadline();
				if(d1 < d2)
					return -1;
				if(d1 > d2)
					return 1;
			}
			return 0;
		}
		
	}
}
