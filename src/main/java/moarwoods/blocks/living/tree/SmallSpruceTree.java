package moarwoods.blocks.living.tree;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Predicate;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoodsObjects;
import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SmallSpruceTree extends AbstractSmallTree<BlockLivingLog, BlockLivingLeaf, BlockLivingBranch>
{

	@Override
	public BlockLivingLog getLogBlock()
	{
		return MoarWoodsObjects.SPRUCE_TREE_SMALL_TRUNK;
	}

	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoodsObjects.SPRUCE_TREE_SMALL_LEAVES;
	}

	@Override
	public int getLeafSearchRadius(World world, BlockPos pos, int height, long[] seeds)
	{
		return 4;
	}

	@Override
	public int getLeafSearchExtraHeight(World world, BlockPos pos, int height, long[] seeds)
	{
		return Math.max(1, (1 + new Random(seeds[2]).nextInt(2)) * Math.min(height / this.getHeightLimit(world, pos, seeds), 1));
	}

	@Override
	public Map<IBlockState, Collection<BlockPos>> getBlocks(World world, BlockPos pos, int height, boolean include_existing, long[] seeds)
	{
		Set<BlockPos> leaves = Sets.newHashSet();
		float growth = Math.min((float)height / (float)this.getHeightLimit(world, pos, seeds), 1);
		int empty_space;
		{
			Random random = new Random(seeds[0]);
			random.nextInt();
			empty_space = Math.max(height == 1 ? 0 : 1, MathHelper.floor((1 + random.nextInt(2)) * growth));
		}
		Random random = new Random(seeds[2]);
		int extra_leaf_height = MathHelper.floor((1 + random.nextInt(2)) * growth);
		int radius_limit = 2 + random.nextInt(2);
        int current_radius = random.nextInt(2);
        int max_radius = 1;
        int min_radius = 0;
        for(int l3 = -extra_leaf_height; l3 <= height - empty_space; l3++)
        {
            for(int x = pos.getX() - current_radius; x <= pos.getX() + current_radius; x++)
                for(int z = pos.getZ() - current_radius; z <= pos.getZ() + current_radius; z++)
                    if(Math.abs(x - pos.getX()) != current_radius || Math.abs(z - pos.getZ()) != current_radius || current_radius <= 0)
                        leaves.add(new BlockPos(x, pos.getY() + height - l3, z));
            if (current_radius >= max_radius)
            {
                current_radius = min_radius;
                min_radius = 1;
                max_radius++;
                if(max_radius > radius_limit)
                    max_radius = radius_limit;
            }
            else
                current_radius++;
        }
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		Block leaf = this.getLeafBlock();
		Map<IBlockState, Collection<BlockPos>> blocks = Maps.newHashMap();
		Map<BlockPos, Block> already_discovered = Maps.newHashMap();
		blocks.put(this.getLeafBlock().getDefaultState(), Sets.filter(leaves, (pos1) -> 
		{
			if(already_discovered.computeIfAbsent(pos1, (pos11) -> world.getBlockState(pos11).getBlock()) == leaf)
				return include_existing;
			for(EnumFacing facing : EnumFacing.values())
			{
				BlockPos pos2 = pos1.offset(facing);
				int y1 = pos2.getY();
				if((pos2.getX() == x && pos2.getZ() == z && y1 >= y && y1 - y < height) || (leaves.contains(pos2) && already_discovered.computeIfAbsent(pos2, (pos21) -> world.getBlockState(pos21).getBlock()) == leaf))
					return true;
			}
			return false;
		}));
		return blocks;
	}

	@Override
	public int getHeightLimit(World world, BlockPos pos, long[] seeds)
	{
		return 5 + new Random(seeds[0]).nextInt(4) - new Random(seeds[2]).nextInt(2);
	}

	@Override
	public Map<BlockPos, BlockPos> shiftLeaves(World world, BlockPos pos, int from_height, int to_height, long[] seeds)
	{
		int difference = to_height - from_height;
		{
			float height_limit = this.getHeightLimit(world, pos, seeds);
			int max_extra_leaf_height = 1 + new Random(seeds[2]).nextInt(2);
			difference += (MathHelper.floor(max_extra_leaf_height * Math.min((float)to_height / height_limit, 1)) - MathHelper.floor(max_extra_leaf_height * Math.min((float)from_height / height_limit, 1)));
		}
		Map<BlockPos, BlockPos> transformations = Maps.newHashMap();
		int shift_radius = this.getLeafShiftRadius(world, pos, from_height, seeds);
		int shift_height = from_height + this.getLeafSearchExtraHeight(world, pos, from_height, seeds);
		Map<BlockPos, IBlockState> to_set = Maps.newHashMap();
		for(int i = 1; i < difference; i++)
			to_set.put(pos.up(from_height + i), this.getLeafBlock().getDefaultState());
		for(int x = -shift_radius; x <= shift_radius; x++)
			for(int y = 0; y < shift_height; y++)
				for(int z = -shift_radius; z <= shift_radius; z++)
				{
					BlockPos pos1 = pos.add(x, y, z);
					IBlockState state1 = world.getBlockState(pos1);
					if(state1.getBlock() == this.getLeafBlock())
					{
						world.setBlockToAir(pos1);
						BlockPos pos2 = pos1.up(difference);
						transformations.put(pos1, pos2);
						to_set.put(pos2, state1);
					}
				}
		Predicate<Triple<IBlockState, World, BlockPos>> predicate = (triple) -> triple.getLeft().getBlock() instanceof BlockLeaves;
		for(Entry<BlockPos, IBlockState> entry : to_set.entrySet())
			AbstractPlant.setBlock(world, entry.getKey(), entry.getValue(), predicate);
		return transformations;
	
	}
	@Override
	public TObjectIntHashMap<BlockPos> getGrowthRadiuses(World world, BlockPos pos, int height, long seed)
	{
		// TODO Auto-generated method stub
		return new TObjectIntHashMap<BlockPos>();
	}
	
}
