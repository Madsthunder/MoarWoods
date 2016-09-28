package continuum.moarwoods.tileentity;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import com.google.common.collect.Lists;

import continuum.essentials.tileentity.TileEntityInventory;
import continuum.moarwoods.inventories.InventoryCraftingGrid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.inventory.ItemStackHelper;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

public class TileEntityCraftingGrid extends TileEntityInventory
{
	private final int width;
	private final int height;
	private List<Float> gridYaws;
	private final InventoryCraftingGrid matrix;
	
	public TileEntityCraftingGrid()
	{
		this(false);
	}
	
	public TileEntityCraftingGrid(boolean isRemote)
	{
		this(isRemote, 3, 3);
	}
	
	public TileEntityCraftingGrid(boolean isRemote, int width, int height)
	{
		super(width * height);
		this.width = width;
		this.height = height;
		this.matrix = new InventoryCraftingGrid(this);
		if(isRemote)
		{
			Random random = new Random();
			this.gridYaws = Lists.newArrayListWithCapacity(this.getSizeInventory() + 1);
			for(Integer i = 0; i < this.getSizeInventory() + 1; i++)
				this.gridYaws.add(random.nextFloat() * 360F);
		}
	}
	
	public Integer getWidth()
	{
		return this.width;
	}
	
	public Integer getHeight()
	{
		return this.height;
	}
	
	@SideOnly(Side.CLIENT)
	public void updateRenderYaw()
	{
		for (Integer index = 0; index < this.gridYaws.size(); index++)
		{
			Float f = this.gridYaws.get(index);
			if(f != null)
				this.gridYaws.set(index, (f += 1F) == 360F ? 0F : f);
		}
	}
	
	public Boolean hasResult()
	{
		return this.getResult() != null;
	}
	
	public ItemStack getResult()
	{
		if(this.hasWorldObj())
			return CraftingManager.getInstance().findMatchingRecipe(this.matrix, this.getWorld());
		return null;
	}
	
	public List<Float> getGridYaws()
	{
		return this.gridYaws;
	}
	
	public void setGridYaws(List<Float> gridYaws)
	{
		this.gridYaws = gridYaws;
	}
}
