package net.machinemuse.api;

import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.IIcon;

import java.util.List;
import java.util.Map;

public interface IPowerModule {
	List<ItemStack> getInstallCost();

	IIcon getIcon(ItemStack item);

	String getStitchedTexture(ItemStack item);

	void registerIcon(IIconRegister registry);

	String getCategory();

	boolean isValidForItem(ItemStack stack);

	String getDataName();

    // Use StatCollector.translateToLocal("module." + m.getUnlocalizedName + ".name") instead

	double applyPropertyModifiers(NBTTagCompound itemTag, String propertyName, double propertyValue);

	NBTTagCompound getNewTag();

    // Use StatCollector.translateToLocal("module." + m.getUnlocalizedName + ".desc") instead
    @Deprecated
	String getDescription();

	Map<String, List<IPropertyModifier>> getPropertyModifiers();

	boolean isAllowed();
}
