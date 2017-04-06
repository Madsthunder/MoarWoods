package moarwoods.blocks.living.tree;

import java.util.Collection;
import java.util.List;

import com.google.common.collect.Lists;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoods;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class SmallSpruceTree extends AbstractPlant
{

	@Override
	public BlockLivingLog getLogBlock()
	{
		return MoarWoods.LIVING_SPRUCE_LOG;
	}

	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoods.LIVING_SPRUCE_LEAF;
	}

	@Override
	public List<BlockPos> getLeaves(BlockPos pos, int height, long[] seeds)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public List<IPlantBranch> getBranches(World world, BlockPos pos, int height, long[] seeds)
	{
		return Lists.newArrayList();
	}

	@Override
	public int getLeafSearchRadius(int height, long[] seeds)
	{
		return 0;
	}

	@Override
	public int getLeafSearchExtraHeight(int height, long[] seeds)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public int getMinHeightLimit()
	{
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public TObjectIntHashMap<BlockPos> getGrowthRadiuses(World world, BlockPos pos, int height, long seed)
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public int getEmptySpace(int height)
	{
		// TODO Auto-generated method stub
		return 0;
	}

	
}
