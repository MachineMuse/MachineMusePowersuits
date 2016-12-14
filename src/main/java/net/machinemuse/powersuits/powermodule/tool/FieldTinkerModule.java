package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 3:14 PM, 4/30/13
 *
 * Ported to Java by lehjr on 10/11/16.
 */
public class FieldTinkerModule extends PowerModuleBase implements IRightClickModule {
    public FieldTinkerModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return "Field Tinker Module";
    }

    @Override
    public String getUnlocalizedName() {
        return "fieldTinkerer";
    }

    @Override
    public String getTextureFile() {
        return "transparentarmor";
    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {
        player.openGui(ModularPowersuits.getInstance(), 2, world, (int)player.posX, (int)player.posY, (int)player.posZ);
    }
}