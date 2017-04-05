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
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

public class BlockLivingLog extends BlockLog
{
	public static final PropertyBool DYING = PropertyBool.create("dying");
	private final IPlant plant;
	
	public BlockLivingLog(IPlant plant)
	{
		this.setTickRandomly(true);
		this.setDefaultState(this.getDefaultState().withProperty(DYING, false));
		this.plant = plant;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(DYING) ? 8 : 0) + state.getValue(BlockLog.LOG_AXIS).ordinal();
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(DYING, meta >= 8).withProperty(BlockLog.LOG_AXIS, BlockLog.EnumAxis.values()[meta & 3]);
	}
	
	@Override
	public boolean canSustainLeaves(IBlockState state, IBlockAccess world, BlockPos pos)
	{
		return !state.getValue(DYING);
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		
		if(random.nextInt(14) == 0)
			if(this.plant.grow(world, pos))
				return;
		Byte seed = MoarWoods.getBlockHistory(world, pos);
		if(!world.isRemote && AbstractPlant.isBase(world, pos, this) && seed != null)
		{
			int height = AbstractPlant.getHieght(world, pos, this);
			int total_energy;
			TObjectIntHashMap<BlockPos.MutableBlockPos> energy_sources;
			{
				Pair<Integer, TObjectIntHashMap<BlockPos.MutableBlockPos>> pair = AbstractPlant.getTotalEnergy(world, pos, this.plant.getLeafSearchRadius(height), height + this.plant.getLeafSearchExtraHeight(height), this.plant.getLeafBlock());
				total_energy = pair.getLeft();
				energy_sources = pair.getRight();
			}
			if(total_energy >= 1)
			{
				Random r = new Random(seed);
				for(int i = 0; i < 2; i++)
					r.nextLong();
				long leaves_seed = r.nextLong();
				List<BlockPos> leaves = this.plant.getLeaves(pos, height, leaves_seed);
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
		return new BlockStateContainer(this, new IProperty[] { BlockLog.LOG_AXIS, DYING});
	}
}
