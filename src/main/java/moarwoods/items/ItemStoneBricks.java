package moarwoods.items;

import moarwoods.blocks.BlockStoneBricks.EnumBrickType;
import net.minecraft.block.Block;
import net.minecraft.item.ItemBlock;
import net.minecraft.item.ItemStack;

public class ItemStoneBricks extends ItemBlock
{
    private final String name;
    
    public ItemStoneBricks(Block block, String name)
    {
        super(block);
        this.setHasSubtypes(true);
        this.name = name;
    }
    
    @Override
    public String getUnlocalizedName(ItemStack stack)
    {
        return String.format("tile.moarwoods:%s_%s", EnumBrickType.values()[stack.getMetadata()].getName(), this.name);
    }
    
    @Override
    public int getMetadata(int meta)
    {
        return Math.min(Math.max(meta, 0), 3);
    }

}
