package continuum.moarwoods.blocks;

import java.util.List;

import com.google.common.collect.Lists;

import continuum.essentials.hooks.ClientHooks;
import continuum.essentials.hooks.ObjectHooks;
import net.minecraft.block.BlockLeaves;
import net.minecraft.block.BlockPlanks.EnumType;
import net.minecraft.block.properties.PropertyBool;
import net.minecraft.block.properties.PropertyInteger;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.BlockStateContainer.Builder;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.BlockRenderLayer;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class BlockAppleLeaves extends BlockLeaves
{
	public static final PropertyInteger GROWTH = PropertyInteger.create("growth", 0, 3);
	public static final PropertyBool FANCY = PropertyBool.create("fancy");
	
	@SideOnly(Side.CLIENT)
	@Override
	public IBlockState getActualState(IBlockState state, IBlockAccess access, BlockPos pos)
	{
		return state.withProperty(FANCY, ClientHooks.isFancyEnabled());
	}
	
	@Override
	public IBlockState getStateFromMeta(int meta)
	{
		Integer growth = Math.floorDiv(meta, 4);
		Integer check_decay = Math.floorDiv(meta -= (growth * 4), 2);
		return this.getDefaultState().withProperty(GROWTH, growth).withProperty(CHECK_DECAY, check_decay == 1).withProperty(DECAYABLE, meta - check_decay == 1);
	}
	
	@Override
	public int getMetaFromState(IBlockState state)
	{
		return (state.getValue(GROWTH) * 4) + (state.getValue(CHECK_DECAY) ? 2 : 0) + (state.getValue(DECAYABLE) ? 1 : 0);
	}
	
	@Override
	public IBlockState onBlockPlaced(World world, BlockPos pos, EnumFacing direction, float hitX, float hitY, float hitZ, int meta, EntityLivingBase placer)
	{
		return this.getDefaultState().withProperty(GROWTH, meta);
	}
	
	@Override
	public void getSubBlocks(Item item, CreativeTabs tab, List list)
	{
		list.add(new ItemStack(item));
		list.add(new ItemStack(item, 1, 1));
		list.add(new ItemStack(item, 1, 2));
		list.add(new ItemStack(item, 1, 3));
	}
	
	public BlockStateContainer createBlockState()
	{
		return new Builder(this).add(GROWTH, DECAYABLE, CHECK_DECAY, FANCY).build();
	}
	
	@Override
	public List<ItemStack> onSheared(ItemStack item, IBlockAccess access, BlockPos pos, int fortune)
	{
		return Lists.newArrayList(new ItemStack(this, 1, access.getBlockState(pos).getValue(GROWTH)));
	}

	@Override
	public EnumType getWoodType(int meta)
	{
		return null;
	}

	@SideOnly(Side.CLIENT)
	@Override
    public boolean isOpaqueCube(IBlockState state)
    {
        return !ClientHooks.isFancyEnabled();
    }

    @SideOnly(Side.CLIENT)
    @Override
    public BlockRenderLayer getBlockLayer()
    {
        return ClientHooks.isFancyEnabled() ? BlockRenderLayer.CUTOUT_MIPPED : BlockRenderLayer.SOLID;
    }
	
    @SideOnly(Side.CLIENT)
    @Override
    public boolean shouldSideBeRendered(IBlockState blockState, IBlockAccess blockAccess, BlockPos pos, EnumFacing side)
    {
        return !ClientHooks.isFancyEnabled() && blockAccess.getBlockState(pos.offset(side)).getBlock() == this ? false : super.shouldSideBeRendered(blockState, blockAccess, pos, side);
    }
}
