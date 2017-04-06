package moarwoods.blocks.living.tree;

import java.util.Random;

import moarwoods.MoarWoods;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class BirchTree extends SmallOakTree
{

	@Override
	public BlockLivingLog getLogBlock()
	{
		return MoarWoods.LIVING_BIRCH_LOG;
	}

	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoods.LIVING_BIRCH_LEAF;
	}
	
	@Override
	public int getHeightLimit(World world, BlockPos pos, long[] seeds)
	{
		Biome biome = world.getBiome(pos);
		Random random = new Random(seeds[0]);
		return 5 + random.nextInt(3) + random.nextInt(biome == Biomes.MUTATED_BIRCH_FOREST || biome == Biomes.MUTATED_BIRCH_FOREST_HILLS ? 7 : 1);
	}
	
}
