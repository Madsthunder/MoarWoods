package continuum.moarwoods.containers;

import continuum.moarwoods.blocks.BlockCraftingGrid;
import continuum.moarwoods.inventories.InventoryCraftingGrid;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.block.state.IBlockState;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.InventoryCraftResult;
import net.minecraft.inventory.Slot;
import net.minecraft.inventory.SlotCrafting;
import net.minecraft.item.ItemStack;
import net.minecraft.item.crafting.CraftingManager;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class ContainerCraftingGrid extends Container
{
	private final EntityPlayer player;
	private final TileEntityCraftingGrid grid;
	private final InventoryCraftingGrid matrix;
	private final InventoryCraftResult result;
	
	public ContainerCraftingGrid(EntityPlayer player, TileEntityCraftingGrid grid)
	{
		this.player = player;
		this.grid = grid;
		this.matrix = new InventoryCraftingGrid(this);
		this.result = new InventoryCraftResult();
		this.addSlotToContainer(new SlotCrafting(player, this.matrix, this.result, 0, 124, 35));
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 3; ++j)
				this.addSlotToContainer(new Slot(this.matrix, j + i * 3, 30 + j * 18, 17 + i * 18));
		for (int i = 0; i < 3; ++i)
			for (int j = 0; j < 9; ++j)
				this.addSlotToContainer(new Slot(player.inventory, j + i * 9 + 9, 8 + j * 18, 84 + i * 18));
		for (int i = 0; i < 9; ++i)
			this.addSlotToContainer(new Slot(player.inventory, i, 8 + i * 18, 142));
		this.onCraftMatrixChanged(this.matrix);
	}
	
	public World getWorld()
	{
		return this.getGrid().getWorld();
	}
	
	public BlockPos getPos()
	{
		return this.getGrid().getPos();
	}
	
	public IBlockState getState()
	{
		return this.getWorld().getBlockState(this.getPos());
	}
	
	public TileEntityCraftingGrid getGrid()
	{
		return this.grid;
	}
	
	public EntityPlayer getPlayer()
	{
		return this.player;
	}
	
	public InventoryCraftingGrid getMatrix()
	{
		return this.matrix;
	}
	
	public InventoryCraftResult getCraftResult()
	{
		return this.result;
	}
	
	public void onCraftMatrixChanged(IInventory inventory)
	{
		this.getCraftResult().setInventorySlotContents(0, CraftingManager.getInstance().findMatchingRecipe(this.getMatrix(), this.getWorld()));
	}
	
	@Override
	public boolean canInteractWith(EntityPlayer player)
	{
		BlockPos pos = this.getPos();
		return this.getState().getBlock() instanceof BlockCraftingGrid ? player.getDistanceSq(pos.getX() + .5, pos.getY() + .5, pos.getZ() + .5) <= 64 : false;
	}
	
	public ItemStack transferStackInSlot(EntityPlayer player, int index)
	{
		ItemStack stack = null;
		Slot slot = this.getSlot(index);
		if(slot != null && slot.getHasStack())
		{
			ItemStack stack1;
			stack = (stack1 = slot.getStack()).copy();
			if(index == 0)
			{
				if(!this.mergeItemStack(stack1, 10, 46, true))
					return null;
				slot.onSlotChange(stack1, stack);
			}
			else if(index >= 1 && index < 10)
			{
				if(!this.mergeItemStack(stack1, 10, 46, false))
					return null;
			}
			else if(index >= 10 && index < 37)
			{
				if(!this.mergeItemStack(stack1, 37, 46, false))
					return null;
				else if(index >= 37 && index < 46)
					if(!this.mergeItemStack(stack1, 10, 37, false))
						return null;
					else if(!this.mergeItemStack(stack1, 10, 46, false))
						return null;
			}
			if(stack1.stackSize == 0)
				slot.putStack((ItemStack)null);
			else
				slot.onSlotChanged();
			if(stack1.stackSize == stack.stackSize)
				return null;
			slot.onPickupFromSlot(player, stack1);
		}
		
		return stack;
	}
	
	public boolean canMergeSlot(ItemStack stack, Slot slot)
	{
		return slot.inventory != this.getCraftResult() && super.canMergeSlot(stack, slot);
	}
}
