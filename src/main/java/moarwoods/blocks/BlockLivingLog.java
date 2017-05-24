package moarwoods.blocks;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoods;
import moarwoods.blocks.living.tree.AbstractPlant;
import moarwoods.blocks.living.tree.IPlant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class BlockLivingLog extends BlockLog
{
	public static final PropertyInteger DEATH_STAGE = PropertyInteger.create("death_stage", 0, 3);
	private final IPlant plant;
	
	public BlockLivingLog(IPlant plant)
	{
		this.setTickRandomly(true);
		this.setDefaultState(this.getDefaultState().withProperty(DEATH_STAGE, 0));
		this.plant = plant;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(DEATH_STAGE) * 4) + state.getValue(BlockLog.LOG_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DEATH_STAGE, Math.min(3, Math.max(0, meta / 4))).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.values()[meta & 3]);
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		random = new Random(random.nextLong());
		if(this.plant == null)
			return;
		if(random.nextInt(14) == 0 && this.plant.grow(world, pos, true))
			return;
		else if(!AbstractPlant.hasBase(world, pos, this.plant.getLogBlock()) && world.getBlockState(pos.up()).getBlock() != this.plant.getLogBlock() && random.nextInt(6) == 0)
		{
			int death_stage = state.getValue(DEATH_STAGE);
			if(death_stage >= 3)
			{
				world.setBlockToAir(pos);
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.BLOCKS, 1F, 2F, false);
			}
			else
				world.setBlockState(pos, state.withProperty(DEATH_STAGE, death_stage + 1));
		}
		long[] seeds;
		if(AbstractPlant.isBase(world, pos, this) && (seeds = this.plant.getSeeds(world, pos)) != null)
		{
			int height = AbstractPlant.getHeight(world, pos, this);
			BlockLivingLeaf leaf = this.plant.getLeafBlock();
			int total_energy;
			TObjectIntHashMap<BlockPos> energy_sources;
			{
				Pair<Integer, TObjectIntHashMap<BlockPos>> pair = AbstractPlant.getTotalEnergy(world, pos, this.plant.getLeafSearchRadius(world, pos, height, seeds), height + this.plant.getLeafSearchExtraHeight(world, pos, height, seeds), leaf);
				total_energy = pair.getLeft();
				energy_sources = pair.getRight();
			}
			if(total_energy >= 1)
			{
				List<BlockPos> leaves = this.plant.getLeaves(world, pos, height, seeds);
				if(!leaves.isEmpty())
				{
					if(random.nextBoolean())
					{
						BlockPos pos1 = leaves.get(world.rand.nextInt(leaves.size()));
						if(world.getBlockState(pos1).getBlock() != leaf && AbstractPlant.setLeaves(world, pos1, leaf))
							AbstractPlant.useEnergy(world, 1, energy_sources, leaf);
					}
					else
					{
						List<BlockPos> outliers = Lists.newArrayList(energy_sources.keySet());
						outliers.removeAll(leaves);
						int removeamount = Math.min(random.nextInt(5) + 1, Math.min(outliers.size(), total_energy));
						Set<BlockPos> toremove = Sets.newHashSet();
						Map<BlockPos, List<BlockPos>> bases = Maps.newHashMap();
						for(int i = 0; i < removeamount; i++)
						{
							BlockPos pos1 = outliers.get(random.nextInt(outliers.size()));
							boolean remove = true;
							for(int x = -leaf.searchArea; remove && x <= leaf.searchArea; x++)
								for(int y = -leaf.searchArea; remove && y <= leaf.searchArea; y++)
									for(int z = -leaf.searchArea; remove && z <= leaf.searchArea; z++)
									{
										BlockPos pos2 = pos1.add(x, y, z);
										Block block = world.getBlockState(pos2).getBlock();
										IPlant plant = block instanceof BlockLivingLog ? ((BlockLivingLog)block).getPlant() : null;
										if(plant != null && plant.getLeafBlock() == leaf)
										{
											BlockPos base1 = AbstractPlant.getBase(world, pos2, plant.getLogBlock());
											if(bases.getOrDefault(base1, Collections.EMPTY_LIST).contains(pos1))
											{
												remove = false;
												continue;
											}
											long[] seeds1 = plant.getSeeds(world, pos2);
											if(seeds1 != null)
											{
												List<BlockPos> leaves1 = plant.getLeaves(world, base1, height, seeds1);
												if(leaves1.contains(pos1))
													remove = false;
												bases.put(base1, leaves1);
											}
											else if(base1 != null)
												bases.put(base1, Lists.newArrayList());
										}
									}
							if(remove)
								toremove.add(pos1);
							outliers.remove(pos1);
						}
						AbstractPlant.useEnergy(world, removeamount, energy_sources, leaf);
						for(BlockPos pos1 : toremove)
							world.setBlockToAir(pos1);
					}
				}
			}
		}
	}
	
	public final IPlant getPlant()
	{
		return this.plant;
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { BlockLog.LOG_AXIS, DEATH_STAGE});
	}
}
