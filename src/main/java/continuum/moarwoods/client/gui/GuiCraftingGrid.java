package continuum.moarwoods.client.gui;

import continuum.moarwoods.containers.ContainerCraftingGrid;
import continuum.moarwoods.tileentity.TileEntityCraftingGrid;
import net.minecraft.client.gui.inventory.GuiContainer;
import net.minecraft.client.renderer.GlStateManager;
import net.minecraft.client.resources.I18n;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.relauncher.Side;
import net.minecraftforge.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class GuiCraftingGrid extends GuiContainer
{
	private static final ResourceLocation gui = new ResourceLocation("minecraft", "textures/gui/container/crafting_table.png");
	private final ContainerCraftingGrid container;
	
	public GuiCraftingGrid(EntityPlayer player, TileEntityCraftingGrid grid)
	{
		super(new ContainerCraftingGrid(player, grid));
		this.container = (ContainerCraftingGrid)this.inventorySlots;
	}
	
	public void drawGuiContainerForegroundLayer(int mouseX, int mouseY)
	{
		this.fontRendererObj.drawString(I18n.format(this.container.getMatrix().getName()), 28, 6, 4210752);
		this.fontRendererObj.drawString(I18n.format(this.container.getPlayer().inventory.getName()), 8, this.ySize - 96 + 2, 4210752);
	}
	
	public void drawGuiContainerBackgroundLayer(float partialTicks, int mouseX, int mouseY)
	{
		GlStateManager.color(1.0F, 1.0F, 1.0F, 1.0F);
		this.mc.getTextureManager().bindTexture(gui);
		this.drawTexturedModalRect((this.width - this.xSize) / 2, (this.height - this.ySize) / 2, 0, 0, this.xSize, this.ySize);
	}
}