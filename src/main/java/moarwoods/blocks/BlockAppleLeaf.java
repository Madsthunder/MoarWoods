package moarwoods.blocks;

import javax.annotation.Nullable;

import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockAppleLeaf extends BlockLivingLeaf
{
	public static final PropertyInteger GROWTH_STAGE = PropertyInteger.create("growth_stage", 0, 3);
	public static final PropertyInteger ENERGY = PropertyInteger.create("energy", 0, 1);
	
	public BlockAppleLeaf(IBlockState baseState)
	{
		super(baseState);
		this.setDefaultState(this.getDefaultState().withProperty(GROWTH_STAGE, 0));
	}
	
	@Override
	public int getEnergy(IBlockState state, @Nullable IBlockAccess access, @Nullable BlockPos pos)
	{
		return state.getValue(ENERGY);
	}
	
	@Override
	public int getMaxEnergy(@Nullable IBlockAccess access, @Nullable BlockPos pos)
	{
		return 1;
	}
	
	@Override
	public IBlockState withEnergy(int energy, @Nullable IBlockAccess access, @Nullable BlockPos pos)
	{
		return this.getDefaultState().withProperty(ENERGY, Math.max(0, Math.min(this.getMaxEnergy(access, pos), energy)));
	}
	
	@Override
	public BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, GROWTH_STAGE, ENERGY);
	}
}
