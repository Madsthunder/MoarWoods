package continuum.moarwoods.items;

import continuum.moarwoods.blocks.BlockCraftingGrid;
import net.minecraft.item.ItemBlock;

public class ItemBlockCraftingGrid extends ItemBlock
{
	
	public ItemBlockCraftingGrid(BlockCraftingGrid grid)
	{
		super(grid);
		this.setRegistryName(grid.getRegistryName());
		this.setUnlocalizedName(grid.getUnlocalizedName());
	}
	
}
