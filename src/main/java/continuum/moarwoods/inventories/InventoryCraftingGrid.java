package continuum.moarwoods.inventories;

import continuum.moarwoods.containers.ContainerCraftingGrid;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.InventoryCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.util.text.ITextComponent;

public class InventoryCraftingGrid extends InventoryCrafting
{
	private final ContainerCraftingGrid container;
	private final TileEntityCraftingGrid grid;
	
	public InventoryCraftingGrid(ContainerCraftingGrid container)
	{
		super(container, container.getGrid().getWidth(), container.getGrid().getHeight());
		this.container = container;
		this.grid = container.getGrid();
	}
	
	public InventoryCraftingGrid(TileEntityCraftingGrid grid)
	{
		super(null, grid.getWidth(), grid.getHeight());
		this.container = null;
		this.grid = grid;
	}
	
	public ContainerCraftingGrid getContainer()
	{
		return this.container;
	}
	
	public TileEntityCraftingGrid getGrid()
	{
		return this.grid;
	}
	
	public Boolean isDummy()
	{
		return this.container == null;
	}
	
	@Override
	public String getName()
	{
		return this.getGrid().getName();
	}
	
	@Override
	public ItemStack getStackInSlot(int index)
	{
		return this.getGrid().getStackInSlot(index);
	}
	
	@Override
	public boolean hasCustomName()
	{
		return this.getGrid().hasCustomName();
	}
	
	@Override
	public ITextComponent getDisplayName()
	{
		return this.getGrid().getDisplayName();
	}
	
	@Override
	public ItemStack removeStackFromSlot(int index)
	{
		return this.getGrid().removeStackFromSlot(index);
	}
	
	@Override
	public ItemStack decrStackSize(int index, int count)
	{
		ItemStack stack = this.getGrid().decrStackSize(index, count);
		if(stack != null && !this.isDummy())
			this.getContainer().onCraftMatrixChanged(this);
		return stack;
	}
	
	@Override
	public void setInventorySlotContents(int index, ItemStack stack)
	{
		this.getGrid().setInventorySlotContents(index, stack);
		if(!this.isDummy())
			this.getContainer().onCraftMatrixChanged(this);
	}
	
	@Override
	public int getInventoryStackLimit()
	{
		return this.getGrid().getInventoryStackLimit();
	}
	
	@Override
	public void markDirty()
	{
		System.out.println("marked");
		this.getGrid().markDirty();
	}
	
	@Override
	public boolean isUseableByPlayer(EntityPlayer player)
	{
		return this.getGrid().isUseableByPlayer(player);
	}
	
	@Override
	public void openInventory(EntityPlayer player)
	{
		this.getGrid().openInventory(player);
	}
	
	@Override
	public void closeInventory(EntityPlayer player)
	{
		this.getGrid().openInventory(player);
	}
	
	@Override
	public boolean isItemValidForSlot(int index, ItemStack stack)
	{
		return this.getGrid().isItemValidForSlot(index, stack);
	}
	
	@Override
	public int getField(int id)
	{
		return this.getGrid().getField(id);
	}
	
	@Override
	public void setField(int id, int value)
	{
		this.getGrid().setField(id, value);
	}
	
	@Override
	public int getFieldCount()
	{
		return this.getGrid().getFieldCount();
	}
	
	@Override
	public void clear()
	{
		this.getGrid().clear();
	}
}
