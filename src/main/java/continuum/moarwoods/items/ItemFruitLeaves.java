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
}
