package moarwoods.entity.ai.requests;

import java.util.Map;

import javax.annotation.Nullable;

import com.google.common.collect.Maps;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;

public abstract class RequestInstanceFactory<I extends Request>
{
	private static final Map<ResourceLocation, RequestInstanceFactory<? extends Request>> FACTORIES = Maps.newHashMap();
	private static final Map<Class<? extends Request>, RequestInstanceFactory<? extends Request>> CLASSES = Maps.newHashMap();
	
	public static final RequestInstanceFactory<Request> NULL = new RequestInstanceFactory<Request>(new ResourceLocation("moarwoods:null"), Request.class)
	{
		@Override
		public Request newInstance(EntityVillager villager, NBTTagCompound compound)
		{
			return null;
		}
	};
	private final ResourceLocation name;
	private final Class<I> clasz;
	
	public RequestInstanceFactory(ResourceLocation name, Class<I> clasz)
	{
		this.name = name;
		this.clasz = clasz;
	}
	
	@Nullable
	public abstract Request newInstance(EntityVillager villager, NBTTagCompound compound);
	
	public final ResourceLocation getName()
	{
		return this.name;
	}
	
	public final Class<I> getRequestClass()
	{
		return this.clasz;
	}
	
	public static void register(RequestInstanceFactory factory)
	{
		if(FACTORIES.containsKey(factory.getName()))
			throw new IllegalArgumentException("Cannot have more than one type of instance factory.");
		if(CLASSES.containsKey(factory.getRequestClass()))
			throw new IllegalArgumentException("Cannot have multiple instance factories for one class.");
		FACTORIES.put(factory.getName(), factory);
		CLASSES.put(factory.getRequestClass(), factory);
	}
	
	public static RequestInstanceFactory getByName(String name)
	{
		return getByName(new ResourceLocation(name));
	}
	
	public static RequestInstanceFactory getByName(ResourceLocation name)
	{
		return FACTORIES.getOrDefault(name, NULL);
	}
	
	public static RequestInstanceFactory getByClass(Class<? extends Request> clasz)
	{
		return CLASSES.getOrDefault(clasz, NULL);
	}
}
