package machinemuse.powersuits.common;

import net.minecraft.entity.EntityLiving;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.EnumArmorMaterial;
import net.minecraft.item.ItemArmor;
import net.minecraft.item.ItemStack;
import net.minecraft.util.DamageSource;
import net.minecraftforge.common.ISpecialArmor;
import cpw.mods.fml.common.registry.LanguageRegistry;

public class ItemPowerArmor extends ItemArmor implements ISpecialArmor {
	protected ItemPowerArmor(Config.Items item) {
		super(Config.getAssignedItemID(item), // itemID
				EnumArmorMaterial.IRON, // Material
				item.iconIndex, // Texture index
				item.ordinal()); // armor type.
		setMaxStackSize(1);
		setCreativeTab(Config.getCreativeTab());
		setIconIndex(item.iconIndex);
		setItemName(item.idName);
		LanguageRegistry.addName(this, item.englishName);
	}

	public String getTextureFile() {
		return CommonProxy.ITEMS_PNG;
	}

	@Override
	public ArmorProperties getProperties(EntityLiving player, ItemStack armor,
			DamageSource source, double damage, int slot) {
		// Order in which this armor is assessed for damage.
		int priority = 1;

		// How much of incoming damage is absorbed by this armor piece.
		// 1.0 = absorbs all damage
		// 0.5 = 50% damage to item, 50% damage carried over
		double absorbRatio = 0.1;

		// Maximum damage absorbed by this piece
		int absorbMax = 2;

		ArmorProperties props = new ArmorProperties(priority, absorbRatio,
				absorbMax);
		return props;
	}

	@Override
	public int getArmorDisplay(EntityPlayer player, ItemStack armor, int slot) {
		// How many half-shields of armor display on the player's gui from this
		// piece
		return 4;
	}

	@Override
	public void damageArmor(EntityLiving entity, ItemStack stack,
			DamageSource source, int damage, int slot) {
		// Damage the armor's durability
	}
}
