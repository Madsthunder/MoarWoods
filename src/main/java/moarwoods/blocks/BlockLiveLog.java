package moarwoods.blocks;

import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;

public class BlockLiveLog extends BlockLog
{
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(BlockLog.LOG_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.values()[meta]);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { BlockLog.LOG_AXIS });
	}
}
