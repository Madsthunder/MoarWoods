package moarwoods.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.IBlockAccess;

public class BlockCobbledStone extends Block
{
    public static final PropertyEnum<BlockStone.EnumType> VARIANT = PropertyEnum.create("variant", BlockStone.EnumType.class, BlockStone.EnumType.GRANITE, BlockStone.EnumType.DIORITE, BlockStone.EnumType.ANDESITE);
    
    public BlockCobbledStone()
    {
        super(Material.ROCK);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        switch(state.getValue(VARIANT))
        {
        case DIORITE: return 1;
        case ANDESITE: return 2;
        default: return 0;
        }
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        switch(meta)
        {
        case 1: return this.getDefaultState().withProperty(VARIANT, BlockStone.EnumType.DIORITE);
        case 2: return this.getDefaultState().withProperty(VARIANT, BlockStone.EnumType.ANDESITE);
        default: return this.getDefaultState().withProperty(VARIANT, BlockStone.EnumType.GRANITE);
        }
    }
    
    @Override
    public MapColor getMapColor(IBlockState state, IBlockAccess access, BlockPos pos)
    {
        return state.getValue(VARIANT).getMapColor();
    }
    
    @Override
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, VARIANT);
    }
}
