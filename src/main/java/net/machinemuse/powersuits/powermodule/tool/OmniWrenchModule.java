package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 4:39 PM 4/21/13
 * updated by MachineMuse, adapted from OpenComputers srench
 *
 * Ported to Java by lehjr on 10/11/16.
 */
public class OmniWrenchModule extends PowerModuleBase implements IRightClickModule {
    public static final String  MODULE_OMNI_WRENCH = "Prototype OmniWrench";

    public OmniWrenchModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
    }

    @Override
    public String getTextureFile() {
        return "omniwrench";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return OmniWrenchModule.MODULE_OMNI_WRENCH;
    }

    @Override
    public String getUnlocalizedName() {
        return "omniwrench";
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {

    }

    @Override
    public boolean onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public boolean onItemUseFirst(ItemStack stack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        if (!StolenWrenchCode.onItemUseFirst(stack, player, world, x, y, z, side, hitX, hitY, hitZ)) {
            Block block = world.getBlock(x, y, z);
            if (world.blockExists(x, y, z) && world.canMineBlock(player, x, y, z) && (block != null)) {
                if (block.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
                    player.swingItem();
                    return !world.isRemote;
                }
                return false;
            }
        }
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }
}