package continuum.moarwoods.items;

import java.util.List;

import net.minecraft.block.Block;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemFruitLeaves extends ItemBlock
{
	public ItemFruitLeaves(Block block)
	{
		super(block);
		this.setRegistryName(block.getRegistryName());
		this.setHasSubtypes(true);
	}
	
	@Override
	public int getMetadata(int metadata)
	{
		return metadata;
	}
	
	@Override
	public void getSubItems(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(item));
		list.add(new ItemStack(item, 1, 1));
	}
}
