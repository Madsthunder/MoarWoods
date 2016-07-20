package continuum.moarwoods.blocks;

import java.util.List;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockStateContainer.Builder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public abstract class BlockFruitLeaves extends BlockLeaves
{
	public static final PropertyInteger growth = PropertyInteger.create("growth", 0, 3);
	public static final PropertyInteger type = PropertyInteger.create("type", 0, 3);
	private final Item seed;
	
	public BlockFruitLeaves(Item seed)
	{
		this.seed = seed;
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		IBlockState state = this.getDefaultState();
		if(meta < 4)
			state = state.withProperty(type, 0).withProperty(growth, meta);
		else if(meta < 8)
			state = state.withProperty(type, 1).withProperty(growth, meta - 4);
		else if(meta < 12)
			state = state.withProperty(type, 2).withProperty(growth, meta - 8);
		else
			state = state.withProperty(type, 3).withProperty(growth, meta - 12);
		return state;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(type) * 4) + state.getValue(growth);
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return null;
	}
	
	public BlockStateContainer createBlockState()
	{
		return new Builder(this).add(growth, type).build();
	}
}
