package moarwoods.blocks;

import java.util.List;
import java.util.Random;

import org.apache.commons.lang3.tuple.Pair;

import gnu.trove.map.hash.TObjectIntHashMap;
import moarwoods.MoarWoods;
import moarwoods.blocks.living.tree.AbstractPlant;
import moarwoods.blocks.living.tree.IPlant;
import net.minecraft.block.BlockLog;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.SoundEvents;
import net.minecraft.util.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
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
		if(this.plant == null)
			return;
		if(random.nextInt(14) == 0)
			if(this.plant.grow(world, pos))
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
		Byte seed = MoarWoods.getBlockHistory(world, pos);
		if(!world.isRemote && AbstractPlant.isBase(world, pos, this) && seed != null)
		{
			int height = AbstractPlant.getHeight(world, pos, this);
			long[] seeds = new long[3];
			{
				Random r = new Random(seed);
				for(int i = 0; i < seeds.length; i++)
					seeds[i] = r.nextLong();
			}
			int total_energy;
			TObjectIntHashMap<BlockPos.MutableBlockPos> energy_sources;
			{
				Pair<Integer, TObjectIntHashMap<BlockPos.MutableBlockPos>> pair = AbstractPlant.getTotalEnergy(world, pos, this.plant.getLeafSearchRadius(world, pos, height, seeds), height + this.plant.getLeafSearchExtraHeight(world, pos, height, seeds), this.plant.getLeafBlock());
				total_energy = pair.getLeft();
				energy_sources = pair.getRight();
			}
			if(total_energy >= 1)
			{
				List<BlockPos> leaves = this.plant.getLeaves(world, pos, height, seeds);
				if(!leaves.isEmpty())
				{
					BlockPos pos1 = leaves.get(world.rand.nextInt(leaves.size()));
					if(world.getBlockState(pos1).getBlock() != this.plant.getLeafBlock() && AbstractPlant.setLeaves(world, pos1, this.plant.getLeafBlock()))
						AbstractPlant.useEnergy(world, 1, energy_sources, this.plant.getLeafBlock());
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
