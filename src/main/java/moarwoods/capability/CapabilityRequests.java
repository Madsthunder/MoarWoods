package moarwoods.capability;

import moarwoods.entity.ai.EntityAIVillagerRequests;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.nbt.NBTTagList;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilitySerializable;

public final class CapabilityRequests implements ICapabilitySerializable<NBTTagList>
{
	public static final ResourceLocation NAME = new ResourceLocation("moarwoods:requests");
	@CapabilityInject(EntityAIVillagerRequests.class)
	public static Capability<EntityAIVillagerRequests> REQUESTS;
	
	private final EntityAIVillagerRequests requests;
	
	public CapabilityRequests(EntityVillager villager)
	{
		this.requests = new EntityAIVillagerRequests(villager);
	}
	
	public EntityAIVillagerRequests getRequests()
	{
		return this.requests;
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == REQUESTS;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == REQUESTS ? (T)this.requests : null;
	}
	
	@Override
	public NBTTagList serializeNBT()
	{
		return this.requests.getNBT();
	}
	
	@Override
	public void deserializeNBT(NBTTagList list)
	{
		this.requests.loadFromNBT(list);
	}
}
