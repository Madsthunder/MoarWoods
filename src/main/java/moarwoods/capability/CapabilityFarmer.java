package moarwoods.capability;

import moarwoods.villagers.VillagerFarmer;
import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.capabilities.Capability;
import net.minecraftforge.common.capabilities.CapabilityInject;
import net.minecraftforge.common.capabilities.ICapabilityProvider;

public final class CapabilityFarmer implements ICapabilityProvider
{
	public static final ResourceLocation NAME = new ResourceLocation("moarwoods:farmer");
	@CapabilityInject(VillagerFarmer.class)
	public static Capability<VillagerFarmer> FARMER;
	private final VillagerFarmer farmer;
	
	public CapabilityFarmer(EntityVillager villager)
	{
		this.farmer = new VillagerFarmer(villager);
	}
	
	@Override
	public boolean hasCapability(Capability<?> capability, EnumFacing facing)
	{
		return capability == FARMER;
	}
	
	@Override
	public <T> T getCapability(Capability<T> capability, EnumFacing facing)
	{
		return capability == FARMER ? (T)this.farmer : null;
	}
}
