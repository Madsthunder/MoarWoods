package continuum.moarwoods.mod;

import continuum.moarwoods.client.gui.GuiCraftingGrid;
import continuum.moarwoods.containers.ContainerCraftingGrid;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.network.IGuiHandler;

public class MoarWoods_EH implements IGuiHandler
{
	
	@Override
	public Object getServerGuiElement(int index, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		switch(index)
		{
			case 0 :
				if(entity instanceof TileEntityCraftingGrid)
					return new ContainerCraftingGrid(player, (TileEntityCraftingGrid)entity);
		}
		return null;
	}
	
	@Override
	public Object getClientGuiElement(int index, EntityPlayer player, World world, int x, int y, int z)
	{
		TileEntity entity = world.getTileEntity(new BlockPos(x, y, z));
		switch(index)
		{
			case 0 :
				if(entity instanceof TileEntityCraftingGrid)
					return new GuiCraftingGrid(player, (TileEntityCraftingGrid)entity);
		}
		return null;
	}
	
}
