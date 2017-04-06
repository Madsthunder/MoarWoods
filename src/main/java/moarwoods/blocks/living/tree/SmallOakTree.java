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
	public List<IPlantBranch> getBranches(World world, BlockPos pos, int height, long[] seeds)
	{
		return Lists.newArrayList();
	}
	
	@Override
	public int getLeafSearchRadius(World world, BlockPos pos, int height, long[] seeds)
	{
		float f = (float)height / (float)this.getHeightLimit(world, pos, seeds);
		int stage = f >= 1 ? 4 : f >= .75 ? 3 : f >= .5 ? 2 : 1;
		return stage > 2 ? 2 : 1;
	}
	
	@Override
	public int getLeafSearchExtraHeight(World world, BlockPos pos, int height, long[] seeds)
	{
		return 1;
	}
	
	@Override
	public int getHeightLimit(World world, BlockPos pos, long[] seeds)
	{
		return 4 + new Random(seeds[0]).nextInt(3);
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
	
	public List<BlockPos> getLeavesForStage(BlockPos pos, int height, int stage, long seed)
	{
		List<BlockPos> leaves = Lists.newArrayList();
		if(0 >= stage)
			return leaves;
		boolean[] extraleaves = new boolean[12];
		{
			Random random = new Random(seed);
			for(int i = 0; i < extraleaves.length; i++)
				extraleaves[i] = random.nextBoolean();
		}
		for(int i = 0; i < 5; i++)
			leaves.add(pos.up(height - 1).offset(EnumFacing.values()[i + 1]));
		if(stage >= 2)
		{
			for(int i = 0; i < 4; i++)
			{
				EnumFacing facing = EnumFacing.values()[i + 2];
				leaves.add(pos.up(height - 2).offset(facing));
				if(2 > i)
					for(int j = 0; j < 2; j++)
						leaves.add(pos.up(height - 2).offset(facing).offset(EnumFacing.values()[j + 4]));
			}
			int i = 0;
			for(int j = 0; j < 2; j++)
				for(int k = 0; k < 2; k++)
					if(extraleaves[i++])
						leaves.add(pos.up(height - 1).offset(EnumFacing.values()[j + 2]).offset(EnumFacing.values()[k + 4]));
		}
		if(stage >= 3)
		{
			for(int i = 0; i < 4; i++)
			{
				EnumFacing facing = EnumFacing.values()[i + 2];
				leaves.add(pos.up(height).offset(facing));
				leaves.add(pos.up(height - 2).offset(facing, 2));
			}
		}
		if(stage >= 4)
		{
			for(int i = -2; i <= 2; i++)
				for(int j = -2; j <= 2; j++)
					if(!(Math.abs(i) == 2 && Math.abs(j) == 2))
						leaves.add(pos.add(i, height - 3, j));
			for(int i = 0; i < 4; i++)
				for(int j = 0; j < 4; j++)
					if(i / 2 != j / 2)
						leaves.add(pos.up(height - 2).offset(EnumFacing.values()[i + 2], 2).offset(EnumFacing.values()[j + 2]));
			int i = 0;
			for(int j = 0; j < 2; j++)
				for(int k = 0; k < 2; k++)
				{
					if(extraleaves[4 + i])
						leaves.add(pos.up(height - 3).offset(EnumFacing.values()[j + 2], 2).offset(EnumFacing.values()[k + 4], 2));
					if(extraleaves[8 + i++])
						leaves.add(pos.up(height - 2).offset(EnumFacing.values()[j + 2], 2).offset(EnumFacing.values()[k + 4], 2));
				}
		
		}
		return leaves;
	}
	
	@Override
	public List<BlockPos> getLeaves(World world, BlockPos pos, int height, long[] seeds)
	{
		int height_limit = this.getHeightLimit(world, pos, seeds);
		float f = (float)height / (float)height_limit;
		int stage = f >= 1 ? 4 : f >= .75 ? 3 : f >= .5 ? 2 : 1;
		return this.getLeavesForStage(pos, height, stage, seeds[2]);
	}
	
	@Override
	public int getEmptySpace(int height)
	{
		return ((Math.min(5, height) - 1) / 2) + Math.max(0, height - 5);
	}
	
	@Override
	public int getRequiredEnergyForGrowth(World world, BlockPos pos, int height)
	{
		return 8 * height;
	}
	
}
