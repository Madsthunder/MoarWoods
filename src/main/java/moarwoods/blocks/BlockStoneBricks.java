package moarwoods.blocks;

import net.minecraft.block.Block;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.util.IStringSerializable;

public class BlockStoneBricks extends Block
{
    public static final PropertyEnum<EnumBrickType> TYPE = PropertyEnum.create("type", EnumBrickType.class);
    
    public BlockStoneBricks(MapColor color)
    {
        super(Material.ROCK, color);
    }
    
    public int getMetaFromState(IBlockState state)
    {
        switch(state.getValue(TYPE))
        {
        case CRACKED: return 1;
        case MOSSY: return 2;
        case CHISELED: return 3;
        default: return 0;
        }
    }
    
    public IBlockState getStateFromMeta(int meta)
    {
        switch(meta)
        {
        case 1: return this.getDefaultState().withProperty(TYPE, EnumBrickType.CRACKED);
        case 2: return this.getDefaultState().withProperty(TYPE, EnumBrickType.MOSSY);
        case 3: return this.getDefaultState().withProperty(TYPE, EnumBrickType.CHISELED);
        default: return this.getDefaultState().withProperty(TYPE, EnumBrickType.NORMAL);
        }
    }
    
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, TYPE);
    }
    
    public static enum EnumBrickType implements IStringSerializable
    {
        NORMAL("normal"),
        CRACKED("cracked"),
        MOSSY("mossy"),
        CHISELED("chiseled");
        
        private final String name;
        
        private EnumBrickType(String name)
        {
            this.name = name;
        }
        @Override
        public String getName()
        {
            return this.name;
        }
        
    }
}
