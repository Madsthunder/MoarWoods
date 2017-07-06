package moarwoods.blocks;

import net.minecraft.block.BlockSlab;
import net.minecraft.block.material.MapColor;
import net.minecraft.block.material.Material;
import net.minecraft.block.properties.IProperty;
import net.minecraft.block.properties.PropertyEnum;
import net.minecraft.block.state.BlockStateContainer;
import net.minecraft.block.state.IBlockState;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IStringSerializable;
import net.minecraft.util.NonNullList;

public class BlockStoneSlab extends BlockSlab
{
    public static final PropertyEnum<EnumSlabType> VARIANT = PropertyEnum.create("variant", EnumSlabType.class);
    private final boolean double_slab;
    
    public BlockStoneSlab(boolean double_slab)
    {
        super(Material.ROCK);
        this.double_slab = double_slab;
    }
    
    @Override
    public void getSubBlocks(CreativeTabs tab, NonNullList<ItemStack> items)
    {
        Item item = Item.getItemFromBlock(this);
        for(EnumSlabType type : EnumSlabType.values())
            items.add(new ItemStack(item, 1, type.ordinal()));
    }
    
    @Override
    public int damageDropped(IBlockState state)
    {
        return state.getValue(VARIANT).ordinal();
    }

    @Override
    public String getUnlocalizedName(int meta)
    {
        return String.format("tile.moarwoods:%s_slab", EnumSlabType.values()[meta].getUnlocalizedName());
    }

    @Override
    public boolean isDouble()
    {
        return this.double_slab;
    }

    @Override
    public IProperty<?> getVariantProperty()
    {
        return VARIANT;
    }

    @Override
    public Comparable<?> getTypeForItem(ItemStack stack)
    {
        return EnumSlabType.values()[stack.getMetadata()];
    }
    
    @Override
    public IBlockState getStateFromMeta(int meta)
    {
        int half = Math.min(1, meta / EnumSlabType.values().length);
        return this.getDefaultState().withProperty(HALF, BlockSlab.EnumBlockHalf.values()[half]).withProperty(VARIANT, EnumSlabType.values()[meta - (EnumSlabType.values().length * half)]);
    }
    
    @Override
    public int getMetaFromState(IBlockState state)
    {
        return (state.getValue(HALF).ordinal() * EnumSlabType.values().length) + state.getValue(VARIANT).ordinal();
    }
    
    @Override
    public BlockStateContainer createBlockState()
    {
        return new BlockStateContainer(this, HALF, VARIANT);
    }
    
    public static enum EnumSlabType implements IStringSerializable
    {
        GRANITE_BRICKS("normal_granite_bricks", "normal_granite_brick", MapColor.DIRT),
        DIORITE_BRICKS("normal_diorite_bricks", "normal_diorite_brick", MapColor.QUARTZ),
        ANDESITE_BRICKS("normal_andesite_bricks", "normal_andesite_brick", MapColor.STONE);
        
        public final MapColor color;
        private final String unlocalized_name;
        private final String name;
        
        private EnumSlabType(String name, String unlocalized_name, MapColor color)
        {
            this.color = color;
            this.unlocalized_name = unlocalized_name;
            this.name = name;
        }
        
        public String getUnlocalizedName()
        {
            return this.unlocalized_name;
        }
        
        @Override
        public String getName()
        {
            return this.name;
        }
    }
}
