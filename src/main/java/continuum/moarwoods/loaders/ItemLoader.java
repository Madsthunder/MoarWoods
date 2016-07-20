package continuum.moarwoods.loaders;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;

import continuum.essentials.mod.CTMod;
import continuum.essentials.mod.ObjectLoader;
import continuum.moarwoods.items.ItemBlockCraftingGrid;
import continuum.moarwoods.items.ItemStick;
import continuum.moarwoods.mod.MoarWoods_EH;
import continuum.moarwoods.mod.MoarWoods_OH;
import net.minecraft.init.Items;
import net.minecraftforge.fml.common.registry.ForgeRegistries;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.fml.common.registry.GameRegistry.Type;
import net.minecraftforge.fml.relauncher.Side;

public class ItemLoader implements ObjectLoader<MoarWoods_OH, MoarWoods_EH>
{
	@Override
	public void construction(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		
	}
	
	@Override
	public void pre(CTMod<MoarWoods_OH, MoarWoods_EH> mod)
	{
		MoarWoods_OH holder = mod.getObjectHolder();
		ForgeRegistries.ITEMS.register(new ItemBlockCraftingGrid(holder.crafingGridOak));
		ForgeRegistries.ITEMS.register(new ItemBlockCraftingGrid(holder.crafingGridSpruce));
		ForgeRegistries.ITEMS.register(new ItemBlockCraftingGrid(holder.crafingGridBirch));
		ForgeRegistries.ITEMS.register(new ItemBlockCraftingGrid(holder.crafingGridJungle));
		ForgeRegistries.ITEMS.register(new ItemBlockCraftingGrid(holder.crafingGridAcacia));
		ForgeRegistries.ITEMS.register(new ItemBlockCraftingGrid(holder.crafingGridDarkOak));
		ForgeRegistries.ITEMS.register(holder.stick = new ItemStick());
		/**try
		{
			mod.holder.stick = new ItemStick(mod);
			Field field = Items.class.getField("STICK");
			field.setAccessible(true);
			Field modifiersField = Field.class.getDeclaredField("modifiers");
			modifiersField.setAccessible(true);
			modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
			field.set(null, mod.holder.stick);
			GameRegistry.addSubstitutionAlias("minecraft:stick", Type.ITEM, mod.holder.stick);
		}
		catch(Exception e)
		{
			e.printStackTrace();
		}*/
	}
	
	@Override
	public String getName()
	{
		return "Items";
	}
}
