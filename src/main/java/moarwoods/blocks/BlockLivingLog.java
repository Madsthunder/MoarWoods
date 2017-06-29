package moarwoods.blocks;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;

import org.apache.commons.lang3.tuple.Pair;

import com.google.common.collect.Iterables;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoodsObjects;
import moarwoods.blocks.living.tree.AbstractPlant;
import moarwoods.blocks.living.tree.Plant;
import net.minecraft.block.Block;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumFacing.AxisDirection;
import net.minecraft.util.EnumHand;
import net.minecraft.util.Rotation;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLivingLog extends BlockLog
{
	public static final PropertyInteger DEATH_STAGE = PropertyInteger.create("death_stage", 0, 3);
	public final Plant<? , ?, ?> plant;
	
	public BlockLivingLog(Plant<? , ?, ?> plant)
	{
		this.setTickRandomly(true);
		this.setDefaultState(this.getDefaultState().withProperty(DEATH_STAGE, 0).withProperty(LOG_AXIS, BlockLog.EnumAxis.Y));
		this.plant = plant;
	}
	
	@Override
	public boolean onBlockActivated(World world, BlockPos pos, IBlockState state, EntityPlayer player, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ)
	{
		if(player.getHeldItemMainhand().getItem() == Items.DYE)
			return false;
		return true;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return state.getValue(DEATH_STAGE);
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.withDeathStage(this.getDefaultState(), meta);
	}
	
	@Override
	public IBlockState withRotation(IBlockState state, Rotation rotation)
	{
		return state;
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		random = new Random(random.nextLong());
		if(this.plant == null || this.plant.grow(world, pos, true))
			return;
		else if(AbstractPlant.getBases(world, pos).isEmpty() && world.getBlockState(pos.up()).getBlock() != this.plant.getLogBlock() && random.nextInt(6) == 0)
		{
			int death_stage = state.getValue(DEATH_STAGE);
			if(death_stage >= 3)
			{
				world.setBlockToAir(pos);
				world.playSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.ENTITY_ZOMBIE_BREAK_DOOR_WOOD, SoundCategory.BLOCKS, 10F, 2F, false);
			}
			else
				world.setBlockState(pos, state.withProperty(DEATH_STAGE, death_stage + 1));
		}
		long[] seeds;
		if(world.getBlockState(pos).getBlock() == this && AbstractPlant.isBase(world, pos) && (seeds = this.plant.getSeeds(world, pos)) != null)
		{
			int height = AbstractPlant.getHeight(world, pos);
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
				if(random.nextBoolean())
				{
					Map<IBlockState, List<BlockPos>> blocks = Maps.transformValues(plant.getBlocks(world, pos, height, false, seeds), (positions) -> Lists.newArrayList(positions));
					if(Iterables.any(blocks.values(), (positions) -> !positions.isEmpty()))
					{
						int total_size = 0;
						for(List<BlockPos> positions : blocks.values())
							total_size += positions.size();
						int block_index = world.rand.nextInt(total_size);
						Pair<IBlockState, BlockPos> pair = null;
						int current_index = 0;
						for(Entry<IBlockState, List<BlockPos>> entry : blocks.entrySet())
						{
							List<BlockPos> positions = entry.getValue();
							int size = positions.size();
							if(block_index < current_index + size)
							{
								pair = Pair.of(entry.getKey(), positions.get(current_index + block_index));
								break;
							}
							else
								current_index += size;
						}
						if(pair != null)
						{
							if(AbstractPlant.setBlock(world, pair.getRight(), pair.getLeft(), (triple) -> triple.getLeft().getBlock() instanceof BlockLeaves))
								AbstractPlant.useEnergy(world, 1, energy_sources, plant.getLeafBlock());
						}
					}
				}
				else
				{
					Collection<BlockPos> leaves = Iterables.find(this.plant.getBlocks(world, pos, height, true, seeds).entrySet(), leaf.filter, Pair.of(null, Collections.emptyList())).getValue();
					if(!leaves.isEmpty())
					{
						Set<BlockPos> outliers = Sets.newHashSet(energy_sources.keySet());
						outliers.removeAll(leaves);
						int removeamount = Math.min(random.nextInt(5) + 1, outliers.size());
						Set<BlockPos> toremove = Sets.newHashSet();
						Set<Collection<BlockPos>> leaves1 = Sets.newHashSet();
						for(int i = 0; i < removeamount; i++)
						{
							BlockPos pos1 = Iterables.get(outliers, random.nextInt(outliers.size()));
							boolean remove = true;
							for(int x = -leaf.searchArea; x <= leaf.searchArea; x++)
								for(int y = -leaf.searchArea; y <= leaf.searchArea; y++)
									for(int z = -leaf.searchArea; z <= leaf.searchArea; z++)
									{
										BlockPos pos2 = pos1.add(x, y, z);
										Block block = world.getBlockState(pos2).getBlock();
										Plant<? , ?, ?> plant = block instanceof BlockLivingLog ? ((BlockLivingLog)block).plant : null;
										if(plant != null && plant.getLeafBlock() == leaf)
											for(BlockPos base : AbstractPlant.getBases(world, pos2))
												leaves1.add(Iterables.find(plant.getBlocks(world, base, AbstractPlant.getHeight(world, base), true, plant.getSeeds(world, base)).entrySet(), leaf.filter, Pair.of(null, Collections.emptyList())).getValue());
									}
							if(!Iterables.any(leaves1, (leaves2) -> leaves2.contains(pos1)))
								toremove.add(pos1);
							outliers.remove(pos1);
						}
						for(BlockPos pos1 : toremove)
							world.setBlockToAir(pos1);
					}
				}
			}
		}
	}
	
	public boolean isBase(IBlockAccess access, BlockPos pos)
	{
		IBlockState state = access.getBlockState(pos.down());
		return state.getBlock().canSustainPlant(state, access, pos.down(), EnumFacing.UP, MoarWoodsObjects.SAPLING);
	}
	
	public Set<BlockPos> nextPositions(IBlockState state, IBlockAccess access, BlockPos pos, AxisDirection direction)
	{
		BlockPos next = pos.up(direction.getOffset());
		return access.getBlockState(next).getBlock() == this ? Sets.newHashSet(next) : Collections.emptySet();
	}
	
	public IBlockState withDeathStage(IBlockState state, int stage)
	{
		return state.withProperty(DEATH_STAGE, Math.max(0, Math.min(this.getMaxDeathStage(state), stage)));
	}
	
	public int getMaxDeathStage(IBlockState state)
	{
		return 3;
	}
	
	public IBlockState incrementDeathStage(IBlockState state, int i)
	{
		return this.withDeathStage(state, state.getValue(DEATH_STAGE) + i);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { DEATH_STAGE, LOG_AXIS });
	}
}
