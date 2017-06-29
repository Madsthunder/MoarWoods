package moarwoods.blocks.living.tree;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.base.Function;
import com.google.common.base.Functions;
import com.google.common.collect.Lists;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoodsBlockSeeds;
import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import moarwoods.blocks.BlockLivingQuarterLog;
import moarwoods.blocks.BlockQuarterLog;
import moarwoods.blocks.BlockQuarterLog.EnumLogQuarter;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractLargeTree<T extends BlockLivingQuarterLog, L extends BlockLivingLeaf, B extends BlockLivingBranch> extends AbstractTree<T, L, B>
{
	@Override
	public long[] getSeeds(World world, BlockPos pos)
	{
		Random random = new Random(MoarWoodsBlockSeeds.getBlockSeed(world, pos));
		long[] seeds = new long[4];
		for(int i = 0; i < seeds.length; i++)
			seeds[i] = random.nextLong();
		return seeds;
	}
	
	@Override
	public void placeLogsAt(World world, BlockPos pos, int from_height, int to_height, long[] seeds)
	{
		boolean removing = to_height < from_height;
		{
			int new_from_height = Math.min(from_height, to_height);
			int new_to_height = Math.max(from_height, to_height);
			from_height = new_from_height;
			to_height = new_to_height;
		}
		EnumLogQuarter quarter = world.getBlockState(pos).getValue(BlockQuarterLog.QUARTER);
		IBlockState state = this.getLogBlock().getDefaultState();
		Function<? super EnumLogQuarter, IBlockState> function = removing ? Functions.constant(Blocks.AIR.getDefaultState()) : (quarter1) -> state.withProperty(BlockQuarterLog.QUARTER, quarter1);
		List<EnumFacing> lean_positions = this.getLeanPositions(world, pos, seeds);
		int size = lean_positions.size();
		int x_offset = 0;
		int z_offset = 0;
		for(int i = 0; i < size && i < from_height; i++)
		{
			EnumFacing facing = lean_positions.get(i);
			if(facing != null)
			{
				x_offset += facing.getFrontOffsetX();
				z_offset += facing.getFrontOffsetZ();
			}
		}
		for(int y_offset = from_height; to_height > y_offset; y_offset++)
		{
			if(y_offset < lean_positions.size())
			{
				EnumFacing facing = lean_positions.get(y_offset);
				if(facing != null)
				{
					x_offset += facing.getFrontOffsetX();
					z_offset += facing.getFrontOffsetZ();
				}
			}
			BlockPos pos1 = pos.add(x_offset, y_offset, z_offset);
			for(EnumLogQuarter quarter1 : EnumLogQuarter.values())
				function.apply(quarter1);
		}
	}
	
	@Override
	public Pair<Integer, TObjectIntHashMap<BlockPos>> updateHeight(World world, BlockPos pos, int current_height, boolean update_logs, long[] seeds)
	{
		int height_limit = this.getHeightLimit(world, pos, seeds);
		EnumLogQuarter quarter = world.getBlockState(pos).getValue(BlockQuarterLog.QUARTER);
		if(current_height > height_limit)
		{
			if(update_logs)
			{
				BlockPos pos1 = pos.up(current_height - 1);
				BlockLivingLog block = this.getLogBlock();
				List<Pair<BlockPos, IBlockState>> states = Lists.newArrayList();
				for(EnumLogQuarter quarter1 : EnumLogQuarter.values())
				{
					BlockPos pos2 = quarter.offset(pos1, quarter1, Axis.Y);
					IBlockState state1 = world.getBlockState(pos2);
					if(state1.getBlock() == block && state1.getValue(BlockQuarterLog.QUARTER) == quarter)
						states.add(Pair.of(pos2, state1));
				}
				if(states.isEmpty())
					return null;
				Pair<BlockPos, IBlockState> pair = states.get(world.rand.nextInt(states.size()));
				IBlockState state1 = pair.getRight();
				int stage = state1.getValue(BlockLivingLog.DEATH_STAGE);
				if(stage >= block.getMaxDeathStage(state1))
				{
					world.setBlockState(pair.getLeft(), this.getLeafBlock().getDefaultState());
					if(states.size() == 1)
						this.shiftTree(world, pos, current_height, current_height - 1, seeds);
				}
				else
					world.setBlockState(pair.getLeft(), block.incrementDeathStage(state1, 1));
			}
			return null;
		}
		Pair<Integer, TObjectIntHashMap<BlockPos>> pair = getTotalEnergy(world, pos, this.getLeafSearchRadius(world, pos, current_height, seeds), current_height + this.getLeafSearchExtraHeight(world, pos, current_height, seeds), this.getLeafBlock());
		if(update_logs)
		{
			BlockPos pos1 = pos.up(current_height - 1);
			BlockLivingLog block = this.getLogBlock();
			List<Pair<BlockPos, IBlockState>> states = Lists.newArrayList();
			for(EnumLogQuarter quarter1 : EnumLogQuarter.values())
			{
				BlockPos pos2 = quarter.offset(pos1, quarter1, Axis.Y);
				IBlockState state1 = world.getBlockState(pos2);
				if(state1.getBlock() == block && state1.getValue(BlockQuarterLog.QUARTER) == quarter)
					states.add(Pair.of(pos2, state1));
			}
			if(states.isEmpty())
				return null;
			Pair<BlockPos, IBlockState> pair1 = states.get(world.rand.nextInt(states.size()));
			IBlockState state1 = pair1.getRight();
			int stage = state1.getValue(BlockLivingLog.DEATH_STAGE);
			if(this.getRequiredEnergyForSustainability(world, pos, current_height, seeds) > pair.getLeft())
			{
				if(stage >= block.getMaxDeathStage(state1))
				{
					world.setBlockState(pair1.getLeft(), this.getLeafBlock().getDefaultState());
					if(states.size() == 1)
						this.shiftLeaves(world, pos, current_height, current_height - 1, seeds);
				}
				else
					world.setBlockState(pair1.getLeft(), block.incrementDeathStage(state1, 1));
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
