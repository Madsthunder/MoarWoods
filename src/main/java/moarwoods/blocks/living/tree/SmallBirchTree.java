package moarwoods.blocks.living.tree;

import java.util.Random;

import moarwoods.MoarWoodsObjects;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.init.Biomes;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

public class SmallBirchTree extends SmallOakTree
{

	@Override
	public BlockLivingLog getLogBlock()
	{
		return MoarWoodsObjects.BIRCH_TREE_SMALL_TRUNK;
	}

	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoodsObjects.BIRCH_TREE_SMALL_LEAVES;
	}
	
	@Override
	public int getHeightLimit(World world, BlockPos pos, long[] seeds)
	{
		Biome biome = world.getBiome(pos);
		Random random = new Random(seeds[0]);
		return 5 + random.nextInt(3) + random.nextInt(biome == Biomes.MUTATED_BIRCH_FOREST || biome == Biomes.MUTATED_BIRCH_FOREST_HILLS ? 7 : 1);
	}
	
}
