package moarwoods.blocks.living.tree;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoods;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractPlant implements IPlant
{
	
	public abstract int getHeightLimit(World world, BlockPos pos, long[] seeds);
	
	public abstract TObjectIntHashMap<BlockPos> getGrowthRadiuses(World world, BlockPos pos, int height, long seed);
	
	public abstract int getEmptySpace(int height);
	
	public int getRequiredEnergyForGrowth(World world, BlockPos pos, int height)
	{
		return 7;
	}
	
	public int getLeafShiftRadius(World world, BlockPos pos, int height, long[] seeds)
	{
		return this.getLeafSearchRadius(world, pos, height, seeds);
	}
	
	public int getLeafShiftExtraHeight(World world, BlockPos pos, int height, long[] seeds)
	{
		return this.getLeafSearchExtraHeight(world, pos, height, seeds);
	}
	
	public Map<BlockPos, BlockPos> shiftLeaves(World world, BlockPos pos, int from_height, int to_height, long[] seeds)
	{
		Map<BlockPos, BlockPos> transformations = Maps.newHashMap();
		if(from_height == to_height)
			return transformations;
		int shift_radius = this.getLeafShiftRadius(world, pos, from_height, seeds);
		int shift_height = from_height + this.getLeafShiftExtraHeight(world, pos, from_height, seeds);
		Map<BlockPos, IBlockState> to_set = Maps.newHashMap();
		for(int x = -shift_radius; x <= shift_radius; x++)
			for(int y = 0; y < shift_height; y++)
				for(int z = -shift_radius; z <= shift_radius; z++)
				{
					BlockPos pos1 = pos.add(x, y, z);
					IBlockState state1 = world.getBlockState(pos1);
					if(state1.getBlock() == this.getLeafBlock())
					{
						world.setBlockToAir(pos1);
						BlockPos pos2 = pos1.up(to_height - from_height);
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
	public boolean grow(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Byte seed = MoarWoods.getBlockHistory(world, pos);
		if(!world.isRemote && seed != null)
		{
			{
				IBlockState down = world.getBlockState(pos.down());
				if(!down.getBlock().canSustainPlant(down, world, pos.down(), EnumFacing.UP, MoarWoods.SAPLING))
					return false;
			}
			// [0] is for height, [1] is for branches, [2] is for leaves.
			long[] seeds;
			{
				Random random = new Random(seed);
				seeds = new long[] { random.nextLong(), random.nextLong(), random.nextLong() };
			}
			int height_limit = this.getHeightLimit(world, pos, seeds);
			int current_height = getHeight(world, pos, this.getLogBlock());
			if(current_height > height_limit)
			{
				BlockPos pos1 = pos.up(current_height - 1);
				IBlockState state1 = world.getBlockState(pos1);
				if(state1.getValue(BlockLivingLog.DYING))
				{
					world.setBlockToAir(pos1);
					this.shiftLeaves(world, pos, current_height, current_height - 1, seeds);
				}
				else
				{
					world.setBlockState(pos1, state1.withProperty(BlockLivingLog.DYING, true));
				}
				return false;
			}
			if(current_height == height_limit)
				return false;
			int total_energy;
			TObjectIntHashMap<BlockPos.MutableBlockPos> energy_sources;
			{
				Pair<Integer, TObjectIntHashMap<BlockPos.MutableBlockPos>> pair = getTotalEnergy(world, pos, this.getLeafSearchRadius(world, pos, current_height, seeds), current_height + this.getLeafSearchExtraHeight(world, pos, current_height, seeds), this.getLeafBlock());
				total_energy = pair.getLeft();
				energy_sources = pair.getRight();
			}
			int used_energy = 0;
			{
				int required_energy = this.getRequiredEnergyForGrowth(world, pos, current_height);
				if(required_energy > total_energy)
					return false;
				{
					TObjectIntHashMap<BlockPos> radiuses = this.getGrowthRadiuses(world, pos, current_height, seeds[2]);
					for(BlockPos pos1 : radiuses.keySet())
						if(!MoarWoods.TerrainGenEventHandler.checkIfCanGrow(world, pos1, radiuses.get(pos1)))
							return false;
				}
				{
					{
						Map<BlockPos, BlockPos> transformations = this.shiftLeaves(world, pos, current_height, current_height + 1, seeds);
						TObjectIntHashMap<BlockPos.MutableBlockPos> new_energy_sources = new TObjectIntHashMap<BlockPos.MutableBlockPos>();
						for(BlockPos.MutableBlockPos pos1 : energy_sources.keySet())
						{
							BlockPos.MutableBlockPos newpos = new BlockPos.MutableBlockPos(transformations.getOrDefault(pos1, pos1));
							int energy = energy_sources.get(pos1);
							if(new_energy_sources.contains(newpos))
								new_energy_sources.adjustValue(newpos, energy);
							else
								new_energy_sources.put(newpos, energy);
						}
						energy_sources = new_energy_sources;
					}
					world.setBlockState(pos.up(current_height), this.getLogBlock().getDefaultState().withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.Y));
					used_energy += required_energy;
				}
			}
			useEnergy(world, used_energy, energy_sources, this.getLeafBlock());
			return true;
		}
		return false;
	}

	public static Pair<Integer, TObjectIntHashMap<BlockPos.MutableBlockPos>> getTotalEnergy(World world, BlockPos pos, int search_radius, int search_height, BlockLivingLeaf block)
	{
		TObjectIntHashMap<BlockPos.MutableBlockPos> energy_map = new TObjectIntHashMap<BlockPos.MutableBlockPos>();
		int totalenergy = 0;
		for(int y = 0; y < search_height; y++)
			for(int x = -search_radius; x <= search_radius; x++)
				for(int z = -search_radius; z <= search_radius; z++)
				{
					BlockPos pos1 = pos.add(x, y, z);
					IBlockState state = world.getBlockState(pos1);
					if(state.getBlock() == block)
					{
						TObjectIntHashMap m = new TObjectIntHashMap();
						int energy = state.getValue(BlockLivingLeaf.ENERGY);
						if(energy > 0)
						{
							totalenergy += energy;
							energy_map.put(new BlockPos.MutableBlockPos(pos1), energy);
						}
					}
				}
		return Pair.of(totalenergy, energy_map);
	}
	
	public static void useEnergy(World world, int used_energy, TObjectIntHashMap<? extends BlockPos> energy_sources, BlockLivingLeaf block)
	{
		TObjectIntHashMap<BlockPos> tochange = new TObjectIntHashMap<BlockPos>();
		List<BlockPos> energy_positions = Lists.newArrayList(energy_sources.keySet());
		for(int i = 0; i < used_energy; i++)
		{
			energy_positions.sort((pos, pos1) -> Integer.compare(energy_sources.get(pos1), energy_sources.get(pos)));
			BlockPos pos = energy_positions.get(0);
			int new_energy = (tochange.contains(pos) ? tochange.get(pos) : energy_sources.get(pos)) - 1;
			if(0 >= new_energy)
			{
				energy_sources.remove(pos);
				energy_positions.remove(pos);
			}
			tochange.put(pos, Math.max(0, new_energy));
		}
		for(BlockPos pos : tochange.keySet())
			world.setBlockState(pos, block.getDefaultState().withProperty(BlockLivingLeaf.ENERGY, Math.min(7, tochange.get(pos))));
	}
	
	public static boolean isBase(World world, BlockPos pos, BlockLivingLog block)
	{
		IBlockState down = world.getBlockState(pos.down());
		return down.getBlock() != block && down.getBlock().canSustainPlant(down, world, pos.down(), EnumFacing.UP, MoarWoods.SAPLING);
	}
	
	public static int getHeight(World world, BlockPos pos, BlockLivingLog block)
	{
		int current_height = 0;
		for(;;current_height++)
			if(world.getBlockState(pos.up(current_height)).getBlock() != block)
				break;
		return current_height;
	}
	
	public static boolean setLeaves(World world, BlockPos pos, BlockLivingLeaf leaves)
	{
		return setLeaves(world, pos, leaves.getDefaultState());
	}
	
	public static boolean setLeaves(World world, BlockPos pos, IBlockState state)
	{
		return AbstractPlant.setLeaves(world, pos, state, Predicates.alwaysFalse());
	}
	
	public static boolean setLeaves(World world, BlockPos pos, BlockLivingLeaf leaves, Predicate<Triple<IBlockState, World, BlockPos>> alternative)
	{
		return AbstractPlant.setLeaves(world, pos, leaves.getDefaultState(), alternative);
	}
	
	public static boolean setLeaves(World world, BlockPos pos, IBlockState state, Predicate<Triple<IBlockState, World, BlockPos>> alternative)
	{
		Preconditions.checkArgument(state.getBlock() instanceof BlockLivingLeaf, "\'%s\' must be an instance of moarwoods.blocks.BlockLiveLeaves", state.getBlock().getRegistryName());
		IBlockState state1 = world.getBlockState(pos);
		if(state1.getBlock() == state.getBlock())
		{
			world.setBlockState(pos, state.withProperty(BlockLivingLeaf.ENERGY, Math.max(state.getValue(BlockLivingLeaf.ENERGY), state.getValue(BlockLivingLeaf.ENERGY))));
			return true;
		}
		if(state1.getBlock().isAir(state1, world, pos) || alternative.apply(Triple.of(state1, world, pos)))
		{
			world.setBlockState(pos, state);
			MoarWoods.setBlockHistory(world, pos, Integer.valueOf(world.rand.nextInt(256) - 128).byteValue());
			return true;
		}
		return false;
	}
}
