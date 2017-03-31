package moarwoods.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLiveLeaves extends BlockLeaves
{
	public static final PropertyInteger SATURATION = PropertyInteger.create("saturation", 0, 7);
	public final IBlockState baseState;
	
	public BlockLiveLeaves(IBlockState baseState)
	{
		this.setDefaultState(this.getDefaultState().withProperty(DECAYABLE, true));
		this.baseState = baseState;
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess world, BlockPos pos, int fortune)
	{
		return Lists.newArrayList();
	}
	
	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(CHECK_DECAY) ? 8 : 0) + (state.getValue(SATURATION));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(CHECK_DECAY, meta >= 8).withProperty(SATURATION, meta & 7);
	}
	
	@Override
	public void fillWithRain(World world, BlockPos pos)
	{
		IBlockState down = world.getBlockState(pos.down());
		if(down.getBlock() instanceof BlockLiveLeaves && down.getValue(SATURATION) < 4)
			down.getBlock().fillWithRain(world, pos.down());
		else
		{
			IBlockState state = world.getBlockState(pos);
			int saturation = state.getValue(SATURATION);
			if(saturation < 7)
				world.setBlockState(pos, state.withProperty(SATURATION, saturation++));
			else if(down.getBlock() instanceof BlockLiveLeaves)
				down.getBlock().fillWithRain(world, pos);
		}
	}
	
	@Override
	public boolean isOpaqueCube(IBlockState state)
	{
		return this.baseState == null ? false : this.baseState.isOpaqueCube();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public BlockRenderLayer getBlockLayer()
	{
		return this.baseState.getBlock().getBlockLayer();
	}
	
	@Override
	@SideOnly(Side.CLIENT)
	public boolean shouldSideBeRendered(IBlockState state, IBlockAccess access, BlockPos pos, EnumFacing side)
	{
		return this.baseState.shouldSideBeRendered(access, pos, side);
	}
	
	@Override
	protected BlockStateContainer createBlockState()
	{
		return new BlockStateContainer(this, new IProperty[] { SATURATION, CHECK_DECAY, DECAYABLE });
	}
}
