package continuum.moarwoods.items;

import java.util.List;

import continuum.essentials.mod.CTMod;
import continuum.moarwoods.mod.MoarWoods_EH;
import continuum.moarwoods.mod.MoarWoods_OH;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.IForgeRegistryEntry;

public class ItemStick extends Item implements IForgeRegistryEntry<Item>
{
	public ItemStick()
	{
		this.setUnlocalizedName("stick");
		this.setRegistryName("stick");
		this.setHasSubtypes(true);
		this.setCreativeTab(CreativeTabs.MATERIALS);
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
		list.add(new ItemStack(item, 1, 3));
		list.add(new ItemStack(item, 1, 4));
		list.add(new ItemStack(item, 1, 5));
		list.add(new ItemStack(item, 1, 6));
	}
	
	@Override
	public String getUnlocalizedName(ItemStack stack)
	{
		String name = "default";
		switch(stack.getMetadata())
		{
			case 1 : name = "oak"; break;
			case 2 : name = "spruce"; break;
			case 3 : name = "birch"; break;
			case 4 : name = "jungle"; break;
			case 5 : name = "acacia"; break;
			case 6 : name = "dark_oak"; break;
		}
		return "item." + name + "_stick";
	}
	
	@Override
	public int getMetadata(int meta)
	{
		return meta;
	}
}
