package moarwoods.blocks.living.tree;

import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;

import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Predicate;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoods.ObjectReferences;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;

public class SmallSpruceTree extends AbstractPlant
{

	@Override
	public BlockLivingLog getLogBlock()
	{
		return ObjectReferences.LIVING_SPRUCE_LOG;
	}

	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return ObjectReferences.LIVING_SPRUCE_LEAF;
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
	public List<BlockPos> getLeaves(World world, BlockPos pos, int height, long[] seeds)
	{
		List<BlockPos> leaves = Lists.newArrayList();
		float growth = Math.min((float)height / (float)this.getHeightLimit(world, pos, seeds), 1);
		int empty_space;
		{
			Random random = new Random(seeds[0]);
			random.nextInt();
			empty_space = MathHelper.floor((1 + random.nextInt(2)) * growth);
		}
		Random random = new Random(seeds[2]);
		int extra_leaf_height = MathHelper.floor((1 + random.nextInt(2)) * growth);
		int radius_limit = 2 + random.nextInt(2);
        //How wide the leaves should grow, by default 1 or 0, 1 being the top leaves as a cross and 0 as a simple dot
        int current_radius = random.nextInt(2);
        //What's the maximum width before the leaves reset to 0 after the first one to two go-rounds of the loop below (determined by i3) and 1 after everything else
        int max_radius = 1;
        //What should the width reset to
        int min_radius = 0;
        //Incrememnt all the way down to where the leaves should stop
        for(int l3 = -extra_leaf_height; l3 <= height - empty_space; l3++)
        {
            for(int x = pos.getX() - current_radius; x <= pos.getX() + current_radius; x++)
                for(int z = pos.getZ() - current_radius; z <= pos.getZ() + current_radius; z++)
                    if(Math.abs(x - pos.getX()) != current_radius || Math.abs(z - pos.getZ()) != current_radius || current_radius <= 0)
                        leaves.add(new BlockPos(x, pos.getY() + height - l3, z));
            //Should i3 reset
            if (current_radius >= max_radius)
            {
            	//Reset i3 to whatever k3 is
                current_radius = min_radius;
                //Reset k3 to 1 after the first one to two go-rounds of the above loop (determined by i3)
                min_radius = 1;
                //Increment j3
                max_radius++;
                //Is j3 greater than the maximum radius of the leaves
                if(max_radius > radius_limit)
                    max_radius = radius_limit;
            }
            else
            	//If i3 shouldn't reset, increment it!
                current_radius++;
        }
		return leaves;
	}

	@Override
	public List<IPlantBranch> getBranches(World world, BlockPos pos, int height, long[] seeds)
	{
		return Lists.newArrayList();
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
			AbstractPlant.setLeaves(world, entry.getKey(), entry.getValue(), predicate);
		return transformations;
	
	}
	@Override
	public TObjectIntHashMap<BlockPos> getGrowthRadiuses(World world, BlockPos pos, int height, long seed)
	{
		// TODO Auto-generated method stub
		return new TObjectIntHashMap<BlockPos>();
	}

	@Override
	public int getEmptySpace(int height)
	{
		// TODO Auto-generated method stub
		return 0;
	}
	
}
