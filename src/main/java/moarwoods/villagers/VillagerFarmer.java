package moarwoods.villagers;

import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import net.minecraft.entity.passive.EntityVillager;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerCareer;
import net.minecraftforge.fml.common.registry.VillagerRegistry.VillagerProfession;

public class VillagerFarmer
{
	private static final Map<Pair<VillagerProfession, VillagerCareer>, List<Predicate<ItemStack>>> FARMERS;
	private final EntityVillager villager;
	
	public VillagerFarmer(EntityVillager villager)
	{
		this.villager = villager;
	}
	
	public boolean canFarm(ItemStack stack)
	{
		VillagerProfession profession = this.villager.getProfessionForge();
		NBTTagCompound compound = new NBTTagCompound();
		this.villager.writeEntityToNBT(compound);
		if(!compound.hasKey("Career"))
			return false;
		return Iterables.any(FARMERS.getOrDefault(Pair.of(profession, profession.getCareer(compound.getInteger("Career"))), Lists.newArrayList(Predicates.alwaysFalse())), predicate -> predicate.apply(stack));
	}
	
	public static void addItems(VillagerProfession profession, VillagerCareer career, Item... items)
	{
		addItemPredicates(profession, career, Iterables.toArray(Iterables.transform(Lists.newArrayList(items), item -> (Predicate<ItemStack>)stack -> stack.getItem() == item), Predicate.class));
	}
	
	public static void addItemStacks(VillagerProfession profession, VillagerCareer career, ItemStack... stacks)
	{
		addItemPredicates(profession, career, Iterables.toArray(Iterables.transform(Lists.newArrayList(stacks), stack -> (Predicate<ItemStack>)stack1 -> ItemStack.areItemsEqual(stack, stack1)), Predicate.class));
	}
	
	public static void addItemPredicates(VillagerProfession profession, VillagerCareer career, Predicate<ItemStack>... predicates)
	{
		Pair<VillagerProfession, VillagerCareer> pair = Pair.of(profession, career);
		if(FARMERS.containsKey(pair))
			FARMERS.get(pair).addAll(Lists.newArrayList(predicates));
		else
			FARMERS.put(pair, Lists.newArrayList(predicates));
	}
	
	static
	{
		FARMERS = Maps.newHashMap();
	}
}
