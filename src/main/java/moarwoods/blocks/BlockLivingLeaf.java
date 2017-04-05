package moarwoods.blocks;

import java.util.List;
import java.util.Random;

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
import net.minecraft.world.EnumSkyBlock;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockLivingLeaf extends BlockLeaves
{
	public static final PropertyInteger ENERGY = PropertyInteger.create("energy", 0, 7);
	public final IBlockState baseState;
	
	public BlockLivingLeaf(IBlockState baseState)
	{
		this.setDefaultState(this.getDefaultState().withProperty(DECAYABLE, true).withProperty(ENERGY, 0));
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
		return (state.getValue(CHECK_DECAY) ? 8 : 0) + (state.getValue(ENERGY));
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		return this.getDefaultState().withProperty(CHECK_DECAY, meta >= 8).withProperty(ENERGY, meta & 7);
	}
	
	@Override
	public void randomTick(World world, BlockPos pos, IBlockState state, Random random)
	{
		super.randomTick(world, pos, state, random);
		if(!world.isRemote && world.getBlockState(pos) == state)
		{
			int skylight = world.getLightFor(EnumSkyBlock.SKY, pos);
			int blocklight = world.getLightFor(EnumSkyBlock.BLOCK, pos);
			world.setBlockState(pos, state.withProperty(ENERGY, Math.min(state.getValue(ENERGY) + (!world.isRaining() && 20 - skylight > 0 ? random.nextInt(20 - skylight) == 0 ? 1 : 0 : 1) + (30 - blocklight > 0 ? random.nextInt(30 - blocklight) == 0 ? 1 : 0 : 1), 7)));
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
		return new BlockStateContainer(this, new IProperty[] { ENERGY, CHECK_DECAY, DECAYABLE });
	}
}
