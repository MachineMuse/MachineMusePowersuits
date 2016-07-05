package net.machinemuse.powersuits.powermodule.weapon;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class SonicWeaponModule extends PowerModuleBase implements IRightClickModule {

    public static final String MODULE_SONIC_WEAPON = "Sonic Weapon";

    public SonicWeaponModule(List<IModularItem> validItems) {
        super(validItems);
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_WEAPON;
    }

    @Override
    public String getDataName() {
        return MODULE_SONIC_WEAPON;
    }

    @Override
    public String getUnlocalizedName() { return "sonicWeapon";
    }

    @Override
    public String getDescription() {
        return "A high-amplitude, complex-frequency soundwave generator can have shattering or disorienting effects on foes and blocks alike.";
    }


    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {

    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {

    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.sonicWeapon;
    }
}
