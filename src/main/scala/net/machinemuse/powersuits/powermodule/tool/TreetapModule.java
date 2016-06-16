package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.world.World;
import net.minecraftforge.fml.common.registry.GameRegistry;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 7:45 PM 4/23/13
 *
 * Updated by leon on 6/14/16.
 */

public class TreetapModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_TREETAP = "Treetap";
    public static final String TREETAP_ENERGY_CONSUMPTION = "Energy Consumption";
    private static ItemStack rubber = ModCompatability.getIC2Item("rubberWood");
    private static ItemStack resin = ModCompatability.getIC2Item("resin");

    Item item = GameRegistry.findItem("ic2", "misc_resource");


//    if(item != null) {
//        ItemStack stickyResin = new ItemStack(item, 1, 4);
//    }




    public TreetapModule(List<IModularItem> validItems) {
        super(validItems);
        addBaseProperty(TREETAP_ENERGY_CONSUMPTION, 100);
        addInstallCost(ModCompatability.getIC2Item("electricTreetap"));
    }

    @Override
    public String getTextureFile() {
        return "treetap";
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_TREETAP;
    }

    @Override
    public String getLocalizedName() {
        return Localization.translate("module.treetap.name");
    }

    @Override
    public String getDescription() {
        return "An IC2 treetap integrated in your power tool.";
    }

    @Override
    public void onRightClick(EntityPlayer player, World world, ItemStack item) {

    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        int blockID = world.getBlockId(x, y, z);

        if (blockID == rubber.itemID) {
            tryExtract(player, world, x, y, z, side, null);
            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStack, TREETAP_ENERGY_CONSUMPTION));
        }
    }

    public static boolean tryExtract(EntityPlayer player, World world, int x, int y, int z, int side, List stacks) {
        int metadata = world.getBlockMetadata(x, y, z);
        if ((metadata < 2) || (metadata % 6 != side)) {
            return false;
        }
        if (metadata < 6) {
            if (AddonUtils.isServerSide()) {
                world.setBlockMetadataWithNotify(x, y, z, metadata + 6, 7);
                if (stacks != null) {
                    ItemStack tempResin = resin.copy();
                    tempResin.stackSize = world.rand.nextInt(3);
                    stacks.add(tempResin);
                } else {
                    eject(world, x, y, z, side, world.rand.nextInt(3) + 1);
                }
                world.scheduleBlockUpdate(x, y, z, rubber.itemID, Block.blocksList[rubber.itemID].tickRate(world));
            }
        }
        if ((world.rand.nextInt(5) == 0) && (AddonUtils.isServerSide())) {
            world.setBlockMetadataWithNotify(x, y, z, 1, 7);
        }
        if (world.rand.nextInt(5) == 0) {
            if (AddonUtils.isServerSide()) {
                eject(world, x, y, z, side, 1);
                if (stacks != null) {
                    ItemStack tempResin = resin.copy();
                    tempResin.stackSize = world.rand.nextInt(3);
                    stacks.add(tempResin);
                } else {
                    eject(world, x, y, z, side, 1);
                }
            }
            return true;
        }
        return false;
    }

    public static void eject(World world, int x, int y, int z, int side, int quantity) {
        double ejectX = x + 0.5D;
        double ejectY = y + 0.5D;
        double ejectZ = z + 0.5D;

        if (side == 2)
            ejectZ -= 0.3D;
        else if (side == 5)
            ejectX += 0.3D;
        else if (side == 3)
            ejectZ += 0.3D;
        else if (side == 4) {
            ejectX -= 0.3D;
        }

        for (int i = 0; i < quantity; i++) {
            EntityItem item = new EntityItem(world, ejectX, ejectY, ejectZ, resin.copy());
            item.setDefaultPickupDelay();
            world.spawnEntityInWorld(item);
        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {

    }
}