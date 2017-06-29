package moarwoods.blocks.living.tree;

import java.util.Collection;
import java.util.Map;
import java.util.Random;

import com.google.common.collect.Maps;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoodsObjects;
import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingQuarterLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class LargeDarkOakTree extends AbstractLargeTree<BlockLivingQuarterLog, BlockLivingLeaf, BlockLivingBranch>
{
	
	@Override
	public int getHeightLimit(World world, BlockPos pos, long[] seeds)
	{
		return 6 + new Random(seeds[0]).nextInt(4);
	}
	
	@Override
	public TObjectIntHashMap<BlockPos> getGrowthRadiuses(World world, BlockPos pos, int height, long seed)
	{
		TObjectIntHashMap<BlockPos> growth_radiuses = new TObjectIntHashMap<BlockPos>();
		return growth_radiuses;
	}
	
	@Override
	public BlockLivingQuarterLog getLogBlock()
	{
		return MoarWoodsObjects.DARKOAK_TREE_LARGE_TRUNK;
	}
	
	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoodsObjects.DARKOAK_TREE_LARGE_LEAVES;
	}
	
	@Override
	public int getLeafSearchRadius(World world, BlockPos pos, int height, long[] seeds)
	{
		return 3;
	}
	
	@Override
	public int getLeafSearchExtraHeight(World world, BlockPos pos, int height, long[] seeds)
	{
		return 1;
	}
	
	@Override
	public Map<IBlockState, Collection<BlockPos>> getBlocks(World world, BlockPos pos, int height, boolean include_existing, long[] seeds)
	{
		// TODO Auto-generated method stub
		return Maps.newHashMap();
	}
	
}
