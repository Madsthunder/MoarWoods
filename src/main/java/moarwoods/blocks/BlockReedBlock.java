package moarwoods.blocks;

import net.minecraft.block.BlockRotatedPillar;
import net.minecraft.block.material.Material;
import net.minecraft.creativetab.CreativeTabs;

public class BlockReedBlock extends BlockRotatedPillar
{
	public BlockReedBlock()
	{
		super(Material.PLANTS);
		this.setCreativeTab(CreativeTabs.BUILDING_BLOCKS);
	}
}
