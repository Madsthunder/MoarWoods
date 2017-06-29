package moarwoods.blocks.living.tree;

import java.util.Collection;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoodsObjects;
import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.Axis;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class SmallOakTree extends AbstractSmallTree<BlockLivingLog, BlockLivingLeaf, BlockLivingBranch>
{
	
	@Override
	public BlockLivingLog getLogBlock()
	{
		return MoarWoodsObjects.OAK_TREE_SMALL_TRUNK;
	}
	
	@Override
	public BlockLivingLeaf getLeafBlock()
	{
		return MoarWoodsObjects.OAK_TREE_SMALL_LEAVES;
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
		TObjectIntHashMap<BlockPos> radiuses = new TObjectIntHashMap<BlockPos>();
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
	
	public Set<BlockPos> getLeavesForStage(World world, BlockPos pos, int height, boolean include_existing, int stage, long[] seeds)
	{
		Set<BlockPos> leaves = Sets.newHashSet();
		if(0 >= stage)
			return leaves;
		boolean[] extraleaves = new boolean[12];
		{
			Random random = new Random(seeds[2]);
			for(int i = 0; i < extraleaves.length; i++)
				extraleaves[i] = random.nextBoolean();
		}
		Map<BlockPos, Block> already_discovered = Maps.newHashMap();
		BlockLivingLeaf leaf = this.getLeafBlock();
		int x = pos.getX();
		int y = pos.getY();
		int z = pos.getZ();
		for(int i = 0; i < 5; i++)
			tryAdd(world, pos.up(height - 1).offset(EnumFacing.values()[i + 1]), x, y, z, height, include_existing, leaves, leaf, already_discovered);
		if(stage >= 2)
		{
			for(EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				leaves.add(pos.up(height - 2).offset(facing));
				if(facing.getAxis() == Axis.Z)
					for(int j = 0; j < 2; j++)
						tryAdd(world, pos.up(height - 2).offset(facing).offset(EnumFacing.values()[j + 4]), x, y, z, height, include_existing, leaves, leaf, already_discovered);
			}
			int i = 0;
			for(int j = 0; j < 2; j++)
				for(int k = 0; k < 2; k++)
					if(extraleaves[i++])
						tryAdd(world, pos.up(height - 1).offset(EnumFacing.values()[j + 2]).offset(EnumFacing.values()[k + 4]), x, y, z, height, include_existing, leaves, leaf, already_discovered);
		}
		if(stage >= 3)
		{
			for(EnumFacing facing : EnumFacing.HORIZONTALS)
			{
				tryAdd(world, pos.up(height).offset(facing), x, y, z, height, include_existing, leaves, leaf, already_discovered);
				tryAdd(world, pos.up(height - 2).offset(facing, 2), x, y, z, height, include_existing, leaves, leaf, already_discovered);
			}
		}
		if(stage >= 4)
		{
			for(int i = -2; i <= 2; i++)
				for(int j = -2; j <= 2; j++)
					if(!(Math.abs(i) == 2 && Math.abs(j) == 2) && !(i == 0 && j == 0))
						tryAdd(world,pos.add(i, height - 3, j) , x, y, z, height, include_existing, leaves, leaf, already_discovered);
			for(int i = 0; i < 4; i++)
				for(int j = 0; j < 4; j++)
					if(i / 2 != j / 2)
						tryAdd(world, pos.up(height - 2).offset(EnumFacing.values()[i + 2], 2).offset(EnumFacing.values()[j + 2]), x, y, z, height, include_existing, leaves, leaf, already_discovered);
			int i = 0;
			for(int j = 0; j < 2; j++)
				for(int k = 0; k < 2; k++)
				{
					if(extraleaves[4 + i])
						tryAdd(world, pos.up(height - 3).offset(EnumFacing.values()[j + 2], 2).offset(EnumFacing.values()[k + 4], 2), x, y, z, height, include_existing, leaves, leaf, already_discovered);
					if(extraleaves[8 + i++])
						tryAdd(world, pos.up(height - 2).offset(EnumFacing.values()[j + 2], 2).offset(EnumFacing.values()[k + 4], 2), x, y, z, height, include_existing, leaves, leaf, already_discovered);
				}
		}
		return leaves;
	}
	
	@Override
	public Map<IBlockState, Collection<BlockPos>> getBlocks(World world, BlockPos pos, int height, boolean include_existing, long[] seeds)
	{
		int height_limit = this.getHeightLimit(world, pos, seeds);
		float f = (float)height / (float)height_limit;
		int stage = f >= 1 ? 4 : f >= .75 ? 3 : f >= .5 ? 2 : 1;
		Map<IBlockState, Collection<BlockPos>> blocks = Maps.newHashMap();
		blocks.put(this.getLeafBlock().getDefaultState(), this.getLeavesForStage(world, pos, height, include_existing, stage, seeds));
		return blocks;
	}
	
	@Override
	public int getRequiredEnergyForSustainability(World world, BlockPos pos, int height, long[] seeds)
	{
		return 8 * (height - 1) + 1;
	}
	
	@Override
	public int getRequiredEnergyForGrowth(World world, BlockPos pos, int height, long[] seeds)
	{
		return 8 * height;
	}
	
	public static void tryAdd(IBlockAccess access, BlockPos pos, int x, int y, int z, int height, boolean include_existing, Collection<BlockPos> leaves, BlockLivingLeaf leaf, Map<BlockPos, Block> already_discovered)
	{
		if(already_discovered.computeIfAbsent(pos, (pos1) -> access.getBlockState(pos1).getBlock()) == leaf)
		{
			if(include_existing)
				leaves.add(pos);
			return;
		}
		for(EnumFacing facing : EnumFacing.values())
		{
			BlockPos pos2 = pos.offset(facing);
			int y1 = pos2.getY();
			if((pos2.getX() == x && pos2.getZ() == z && y1 >= y && y1 - y < height) || already_discovered.computeIfAbsent(pos2, (pos21) -> access.getBlockState(pos21).getBlock()) == leaf)
			{
				leaves.add(pos);
				return;
			}
		}
	}
}
