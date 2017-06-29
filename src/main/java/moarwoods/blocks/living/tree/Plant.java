package moarwoods.blocks.living.tree;

import java.util.Collection;
import java.util.Map;

import javax.annotation.Nullable;

import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class Plant<T extends BlockLivingLog, L extends BlockLivingLeaf, B extends BlockLivingBranch>
{
	
	public abstract T getLogBlock();
	
	public abstract L getLeafBlock();
	
	@Nullable
	public B getBranchBlock()
	{
		return null;
	}
	
	public abstract long[] getSeeds(World world, BlockPos pos);
	
	public abstract int getLeafSearchRadius(World world, BlockPos pos, int height, long[] seeds);
	
	public abstract int getLeafSearchExtraHeight(World world, BlockPos pos, int height, long[] seeds);
	
	public abstract Map<IBlockState, Collection<BlockPos>> getBlocks(World world, BlockPos pos, int height, boolean include_existing, long[] seeds);
	
	public boolean grow(World world, BlockPos pos, boolean update_logs)
	{
		return false;
	}
}
