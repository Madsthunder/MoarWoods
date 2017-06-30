package moarwoods.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.BlockStone;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
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
    public MapColor getMapColor(IBlockState state, IBlockAccess access, BlockPos pos)
    {
        return state.getValue(VARIANT).getMapColor();
    }
}
