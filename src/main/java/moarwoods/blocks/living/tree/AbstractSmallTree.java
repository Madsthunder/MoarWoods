package moarwoods.blocks.living.tree;

import org.apache.commons.lang3.tuple.Pair;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractSmallTree<T extends BlockLivingLog, L extends BlockLivingLeaf, B extends BlockLivingBranch> extends AbstractTree<T, L, B>
{
	@Override
	public Pair<Integer, TObjectIntHashMap<BlockPos>> updateHeight(World world, BlockPos pos, int current_height, boolean update_logs, long[] seeds)
	{
		int height_limit = this.getHeightLimit(world, pos, seeds);
		if(current_height > height_limit)
		{
			if(update_logs)
			{
				BlockPos pos1 = pos.up(current_height - 1);
				IBlockState state1 = world.getBlockState(pos1);
				BlockLivingLog block = (BlockLivingLog)state1.getBlock();
				int stage = state1.getValue(BlockLivingLog.DEATH_STAGE);
				if(stage >= block.getMaxDeathStage(state1))
					this.shiftTree(world, pos, current_height, current_height - 1, seeds);
				else
					world.setBlockState(pos1, block.incrementDeathStage(state1, 1));
			}
			return null;
		}
		Pair<Integer, TObjectIntHashMap<BlockPos>> pair = getTotalEnergy(world, pos, this.getLeafSearchRadius(world, pos, current_height, seeds), current_height + this.getLeafSearchExtraHeight(world, pos, current_height, seeds), this.getLeafBlock());
		if(update_logs)
		{
			BlockPos pos1 = pos.up(current_height - 1);
			IBlockState state1 = world.getBlockState(pos1);
			BlockLivingLog block = (BlockLivingLog)state1.getBlock();
			int stage = state1.getValue(BlockLivingLog.DEATH_STAGE);
			if(this.getRequiredEnergyForSustainability(world, pos, current_height, seeds) > pair.getLeft())
			{
				if(stage >= block.getMaxDeathStage(state1))
					this.shiftTree(world, pos, current_height, current_height - 1, seeds);
				else
					world.setBlockState(pos1, block.incrementDeathStage(state1, 1));
				return null;
			}
			else
			{
				IBlockState state2 = block.incrementDeathStage(state1, -1);
				world.setBlockState(pos1, state2);
				if(state2.getValue(BlockLivingLog.DEATH_STAGE) != stage)
					return null;
			}
		}
		if(current_height == height_limit || pair.getLeft() <= 0)
			return null;
		return pair;
	}	
}
