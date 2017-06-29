package moarwoods.blocks.living.tree;

import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import javax.annotation.Nullable;

import org.apache.commons.lang3.tuple.Pair;
import org.apache.commons.lang3.tuple.Triple;

import com.google.common.base.Predicate;
import com.google.common.base.Predicates;
import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoodsBlockSeeds;
import moarwoods.MoarWoodsObjects;
import moarwoods.blocks.BlockLivingBranch;
import moarwoods.blocks.BlockLivingLeaf;
import moarwoods.blocks.BlockLivingLog;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class AbstractPlant<T extends BlockLivingLog, L extends BlockLivingLeaf, B extends BlockLivingBranch> extends Plant<T, L, B>
{
	@Override
	public long[] getSeeds(World world, BlockPos pos)
	{
		Random random = new Random(MoarWoodsBlockSeeds.getBlockSeed(world, pos));
		long[] seeds = new long[3];
		for(int i = 0; i < seeds.length; i++)
			seeds[i] = random.nextLong();
		return seeds;
	}
	
	public abstract int getHeightLimit(World world, BlockPos pos, long[] seeds);
	
	public abstract TObjectIntHashMap<BlockPos> getGrowthRadiuses(World world, BlockPos pos, int height, long seed);
	
	public int getRequiredEnergyForSustainability(World world, BlockPos pos, int height, long[] seeds)
	{
		return this.getRequiredEnergyForGrowth(world, pos, height, seeds);
	}
	
	public int getRequiredEnergyForGrowth(World world, BlockPos pos, int height, long[] seeds)
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
			AbstractPlant.setBlock(world, entry.getKey(), entry.getValue(), predicate);
		return transformations;
	}
	
	@Nullable
	public abstract Pair<Integer, TObjectIntHashMap<BlockPos>> updateHeight(World world, BlockPos pos, int current_height, boolean update_logs, long[] seeds);
	
	public boolean hasRoomToGrow(World world, BlockPos pos, int current_height, long[] seeds)
	{
		TObjectIntHashMap<BlockPos> radiuses = this.getGrowthRadiuses(world, pos, current_height, seeds[2]);
		return Iterables.all(radiuses.keySet(), (pos1) -> checkIfCanGrow(world, pos1, radiuses.get(pos1)));
		
	}
	
	public Map<BlockPos, BlockPos> shiftTree(World world, BlockPos pos, int from_height, int to_height, long[] seeds)
	{
		this.placeLogsAt(world, pos, from_height, to_height, seeds);
		return this.shiftLeaves(world, pos, from_height, to_height, seeds);
	}
	
	public void placeLogsAt(World world, BlockPos pos, int from_height, int to_height, long[] seeds)
	{
		IBlockState state = this.getLogBlock().getDefaultState();
		for(int i = from_height; to_height > i; i++)
			world.setBlockState(pos.up(i), state);
	}
	
	@Override
	public boolean grow(World world, BlockPos pos, boolean update_logs)
	{
		long[] seeds = this.getSeeds(world, pos);
		if(seeds != null)
		{
			if(!isBase(world, pos))
				return false;
			int current_height = getHeight(world, pos);
			Pair<Integer, TObjectIntHashMap<BlockPos>> pair = this.updateHeight(world, pos, current_height, update_logs, seeds);
			if(pair == null)
				return false;
			int total_energy = pair.getLeft();
			TObjectIntHashMap<BlockPos> energy_sources = pair.getRight();
			int used_energy = 0;
			int required_energy = this.getRequiredEnergyForGrowth(world, pos, current_height, seeds);
			if(required_energy > total_energy)
				return false;
			if(!this.hasRoomToGrow(world, pos, current_height, seeds))
				return false;
			energy_sources = transformEnergySources(this.shiftTree(world, pos, current_height, current_height + 1, seeds), energy_sources);
			used_energy += required_energy;
			useEnergy(world, used_energy, energy_sources, this.getLeafBlock());
			return true;
		}
		return false;
	}
	
	public static Pair<Integer, TObjectIntHashMap<BlockPos>> getTotalEnergy(World world, BlockPos pos, int search_radius, int search_height, BlockLivingLeaf block)
	{
		TObjectIntHashMap<BlockPos> energy_map = new TObjectIntHashMap<BlockPos>();
		int totalenergy = 0;
		for(int y = 0; y < search_height; y++)
			for(int x = -search_radius; x <= search_radius; x++)
				for(int z = -search_radius; z <= search_radius; z++)
				{
					BlockPos pos1 = pos.add(x, y, z);
					IBlockState state = world.getBlockState(pos1);
					if(state.getBlock() == block)
					{
						int energy = ((BlockLivingLeaf)state.getBlock()).getEnergy(state, world, pos1);
						totalenergy += energy;
						energy_map.put(pos1, energy);
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
			if(new_energy != -1)
				tochange.put(pos, Math.max(0, new_energy));
		}
		for(BlockPos pos : tochange.keySet())
			world.setBlockState(pos, block.withEnergy(tochange.get(pos), world, pos));
	}
	
	public static boolean isBase(World world, BlockPos pos)
	{
		IBlockState down = world.getBlockState(pos.down());
		return down.getBlock().canSustainPlant(down, world, pos.down(), EnumFacing.UP, MoarWoodsObjects.SAPLING);
	}
	
	public static int getHeight(World world, BlockPos pos)
	{
		int highest_y = pos.getY();
		Set<BlockPos> check_history = Sets.newHashSet();
		List<BlockPos> to_check = Lists.newArrayList(pos);
		while(!to_check.isEmpty())
		{
			List<BlockPos> next_to_check = Lists.newArrayList();
			for(BlockPos pos1 : to_check)
				if(check_history.add(pos1))
				{
					highest_y = pos1.getY() > highest_y ? pos1.getY() : highest_y;
					IBlockState state = world.getBlockState(pos1);
					next_to_check.addAll(((BlockLivingLog)state.getBlock()).nextPositions(state, world, pos1, AxisDirection.POSITIVE));
				}
			to_check = next_to_check;
		}
		return highest_y - pos.getY() + 1;
	}
	
	public static Set<BlockPos> getBases(World world, BlockPos pos)
	{
		Set<BlockPos> check_history = Sets.newHashSet();
		Set<BlockPos> bases = Sets.newHashSet();
		List<BlockPos> to_check = Lists.newArrayList(pos);
		while(!to_check.isEmpty())
		{
			List<BlockPos> next_to_check = Lists.newArrayList();
			for(BlockPos pos1 : to_check)
				if(check_history.add(pos1))
					if(isBase(world, pos1))
						bases.add(pos1);
					else
					{
						IBlockState state = world.getBlockState(pos1);
						next_to_check.addAll(((BlockLivingLog)state.getBlock()).nextPositions(state, world, pos1, AxisDirection.NEGATIVE));
					}
			to_check = next_to_check;
		}
		return bases;
	}
	
	public static TObjectIntHashMap<BlockPos> transformEnergySources(Map<BlockPos, BlockPos> transformations, TObjectIntHashMap<BlockPos> energy_sources)
	{
		TObjectIntHashMap<BlockPos> new_energy_sources = new TObjectIntHashMap<BlockPos>();
		for(BlockPos pos1 : energy_sources.keySet())
		{
			BlockPos newpos = new BlockPos(transformations.getOrDefault(pos1, pos1));
			int energy = energy_sources.get(pos1);
			if(new_energy_sources.contains(newpos))
				new_energy_sources.adjustValue(newpos, energy);
			else
				new_energy_sources.put(newpos, energy);
		}
		return new_energy_sources;
	}
	
	public static boolean checkIfCanGrow(World world, BlockPos pos, int radius)
	{
		boolean flag = true;
		BlockPos.MutableBlockPos blockpos$mutableblockpos = new BlockPos.MutableBlockPos();
		for(int l = pos.getX() - radius; l <= pos.getX() + radius && flag; l++)
			for(int i1 = pos.getZ() - radius; i1 <= pos.getZ() + radius && flag; i1++)
				if((pos.getY() < 0 && pos.getY() >= world.getHeight()) || !canGrowInto(world, blockpos$mutableblockpos.setPos(l, pos.getY(), i1).toImmutable()))
					flag = false;
		return flag;
	}
	
	public static boolean canGrowInto(World world, BlockPos pos)
	{
		IBlockState state = world.getBlockState(pos);
		Block block = state.getBlock();
		return state.getBlock().isAir(state, world, pos) || state.getBlock().isLeaves(state, world, pos) || state.getBlock().isWood(world, pos) || block == Blocks.GRASS || block == Blocks.DIRT || block == Blocks.SAPLING || block == Blocks.VINE;
	}
	
	public static boolean setLeaves(World world, BlockPos pos, BlockLivingLeaf leaves)
	{
		return setLeaves(world, pos, leaves.getDefaultState());
	}
	
	public static boolean setLeaves(World world, BlockPos pos, IBlockState state)
	{
		return AbstractPlant.setBlock(world, pos, state, Predicates.alwaysFalse());
	}
	
	public static boolean setLeaves(World world, BlockPos pos, BlockLivingLeaf leaves, Predicate<Triple<IBlockState, World, BlockPos>> alternative)
	{
		return AbstractPlant.setBlock(world, pos, leaves.getDefaultState(), alternative);
	}
	
	public static boolean setBlock(World world, BlockPos pos, IBlockState state, Predicate<Triple<IBlockState, World, BlockPos>> alternative)
	{
		Block block = state.getBlock();
		IBlockState state1 = world.getBlockState(pos);
		if(block instanceof BlockLivingLeaf && state.getBlock() == state1.getBlock())
		{
			BlockLivingLeaf leaf = (BlockLivingLeaf)block;
			world.setBlockState(pos, leaf.withEnergy(Math.max(leaf.getEnergy(state, null, null), ((BlockLivingLeaf)state1.getBlock()).getEnergy(state1, world, pos)), world, pos));
			return true;
		}
		if(world.isAirBlock(pos) || alternative.apply(Triple.of(state1, world, pos)))
		{
			world.setBlockState(pos, state);
			MoarWoodsBlockSeeds.setBlockSeed(world, pos, MoarWoodsBlockSeeds.randomBlockSeed(world.rand));
			return true;
		}
		return false;
	}
}
