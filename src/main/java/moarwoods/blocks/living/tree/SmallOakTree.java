package moarwoods.blocks.living.tree;

import java.util.Collection;
import java.util.List;
import java.util.Random;

import javax.annotation.concurrent.Immutable;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoods;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallOakTree extends AbstractPlant
{
	
	@Override
	public BlockLivingLog getLogBlock()
	{
		return MoarWoods.LIVING_OAK_LOG;
	}
	
	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoods.LIVING_OAK_LEAF;
	}
	
	@Override
	public List<IPlantBranch> getBranches(World world, BlockPos pos, int height, long seed)
	{
		return Lists.newArrayList();
	}
	
	@Override
	public int getLeafSearchRadius(int height)
	{
		float f = (float)height / (float)this.getMinHeightLimit();
		int stage = f >= 1 ? 4 : f >= .75 ? 3 : f >= .5 ? 2 : 1;
		return stage > 2 ? 2 : 1;
	}
	
	@Override
	public int getLeafSearchExtraHeight(int height)
	{
		return 1;
	}
	
	@Override
	public int getMinHeightLimit()
	{
		return 4;
	}
	
	@Override
	public int getStaticHeightLimitFactor()
	{
		return 3;
	}
	
	@Override
	public int getDynamicHeightLimitFactor(World world, BlockPos pos)
	{
		return 0;
	}
	
	@Override
	public TObjectIntHashMap<BlockPos> getGrowthRadiuses(World world, BlockPos pos, int height, long seed)
	{
		TObjectIntHashMap<BlockPos> radiuses = new TObjectIntHashMap();
		if(height <= 0)
			return radiuses;
		radiuses.put(pos.up(height), height <= 2 ? 0 : 1);
		radiuses.put(pos.up(height - 1), 1);
		if(height >= 2)
			radiuses.put(pos.up(height - 2), height >= 3 ? 2 : 1);
		if(height >= 4)
			radiuses.put(pos.up(height - 3), 2);
		return radiuses;
	}
	
	public List<BlockPos> getForcedLeavesForStage(BlockPos pos, int stage)
	{
		List<BlockPos> leaves = Lists.newArrayList();
		if(0 >= stage)
			return leaves;
		switch(stage)
		{
			case 1:
			{
				for(int i = 0; i < 5; i++)
					leaves.add(pos.offset(EnumFacing.values()[i + 1]));
				break;
			}
			case 2:
			{
				leaves.addAll(Lists.newArrayList(Iterables.transform(this.getForcedLeavesForStage(pos, 1), (pos1) -> pos1.up())));
				for(int i = 0; i < 4; i++)
				{
					EnumFacing facing = EnumFacing.values()[i + 2];
					leaves.add(pos.offset(facing));
					if(2 > i)
						for(int j = 0; j < 2; j++)
							leaves.add(pos.offset(facing).offset(EnumFacing.values()[j + 4]));
				}
				break;
			}
			case 3:
			{
				leaves.addAll(Lists.newArrayList(Iterables.transform(this.getForcedLeavesForStage(pos, 2), (pos1) -> pos1.up())));
				for(int i = 0; i < 4; i++)
				{
					EnumFacing facing = EnumFacing.values()[i + 2];
					leaves.add(pos.up(3).offset(facing));
					leaves.add(pos.up().offset(facing, 2));
				}
				break;
			}
			case 4:
			{
				leaves.addAll(Lists.newArrayList(Iterables.transform(this.getForcedLeavesForStage(pos, 3), (pos1) -> pos1.up())));
				for(int i = -2; i <= 2; i++)
					for(int j = -2; j <= 2; j++)
						if(!(Math.abs(i) == 2 && Math.abs(j) == 2))
							leaves.add(pos.add(i, 1, j));
				for(int i = 0; i < 4; i++)
					for(int j = 0; j < 4; j++)
						if(i / 2 != j / 2)
							leaves.add(pos.up(2).offset(EnumFacing.values()[i + 2], 2).offset(EnumFacing.values()[j + 2]));
				break;
			}
			default:
			{
				leaves.addAll(Lists.newArrayList(Iterables.transform(this.getForcedLeavesForStage(pos, stage - 1), (pos1) -> pos1.up())));
				break;
			}
		}
		return leaves;
	}
	
	@Override
	public Collection<BlockPos> getForcedLeaves(BlockPos pos, int height)
	{
		int min_height_limit = this.getMinHeightLimit();
		float f = (float)height / (float)min_height_limit;
		int stage = f >= 1 ? (height) : f >= .75 ? 3 : f >= .5 ? 2 : 1;
		return this.getForcedLeavesForStage(pos, stage);
	}
	
	public List<BlockPos> getOptionalLeavesForStage(BlockPos pos, int stage, long seed)
	{
		boolean[] extraleaves = new boolean[12];
		{
			Random random = new Random(seed);
			for(int i = 0; i < extraleaves.length; i++)
				extraleaves[i] = random.nextBoolean();
		}
		List<BlockPos> leaves = Lists.newArrayList();
		switch(stage)
		{
			case 1: break;
			case 2:
			{
				int i = 0;
				for(int j = 0; j < 2; j++)
					for(int k = 0; k < 2; k++)
						if(extraleaves[i++])
							leaves.add(pos.up().offset(EnumFacing.values()[j + 2]).offset(EnumFacing.values()[k + 4]));
				break;
			}
			case 3:
			{
				leaves.addAll(Lists.newArrayList(Iterables.transform(this.getOptionalLeavesForStage(pos, 2, seed), (pos1) -> pos1.up())));
				break;
			}
			case 4:
			{
				leaves.addAll(Lists.newArrayList(Iterables.transform(this.getOptionalLeavesForStage(pos, 3, seed), (pos1) -> pos1.up())));
				int i = 4;
				for(int j = 0; j < 2; j++)
					for(int k = 0; k < 2; k++)
						if(extraleaves[i++])
							leaves.add(pos.up().offset(EnumFacing.values()[j + 2], 2).offset(EnumFacing.values()[k + 4], 2));
				for(int j = 0; j < 2; j++)
					for(int k = 0; k < 2; k++)
						if(extraleaves[i++])
							leaves.add(pos.up(2).offset(EnumFacing.values()[j + 2], 2).offset(EnumFacing.values()[k + 4], 2));
				break;
			}
			default:
			{
				leaves.addAll(Lists.newArrayList(Iterables.transform(this.getOptionalLeavesForStage(pos, stage - 1, seed), (pos1) -> pos1.up())));
				break;
			}
		}
		return leaves;
	}
	
	@Override
	public List<BlockPos> getOptionalLeaves(BlockPos pos, int height, long seed)
	{
		Random random = new Random(seed);
		List<BlockPos> leaves = Lists.newArrayList();
		int min_height_limit = this.getMinHeightLimit();
		float f = (float)height / (float)min_height_limit;
		int stage = f >= 1 ? (height) : f >= .75 ? 3 : f >= .5 ? 2 : 1;
		return this.getOptionalLeavesForStage(pos, stage, seed);
	}
	
	@Override
	public int getEmptySpace(int height)
	{
		return ((Math.min(5, height) - 1) / 2) + Math.max(0, height - 5);
	}
	
	public int getRequiredEnergyForGrowth(World world, BlockPos pos, int height)
	{
		return 8 * height;
	}
	
}
