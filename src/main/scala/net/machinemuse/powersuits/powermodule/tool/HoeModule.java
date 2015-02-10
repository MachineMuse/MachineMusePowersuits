package net.machinemuse.powersuits.powermodule.tool;

import cpw.mods.fml.common.eventhandler.Event;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.IPowerModule;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.IIcon;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.player.UseHoeEvent;

import java.util.List;

public class HoeModule extends PowerModuleBase implements IPowerModule, IRightClickModule {
    public static final String MODULE_HOE = "Rototiller";
    public static final String HOE_ENERGY_CONSUMPTION = "Hoe Energy Consumption";
    public static final String HOE_SEARCH_RADIUS = "Hoe Search Radius";

    public HoeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));

        addBaseProperty(HOE_ENERGY_CONSUMPTION, 50);
        addTradeoffProperty("Search Radius", HOE_ENERGY_CONSUMPTION, 950);
        addTradeoffProperty("Search Radius", HOE_SEARCH_RADIUS, 8, "m");
        addPropertyLocalString(HOE_ENERGY_CONSUMPTION, StatCollector.translateToLocal("module.hoe.energy"));
        addPropertyLocalString(HOE_SEARCH_RADIUS, StatCollector.translateToLocal("module.hoe.radius"));
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        double energyConsumed = ModuleManager.computeModularProperty(itemStack, HOE_ENERGY_CONSUMPTION);
        if (player.canPlayerEdit(x, y, z, side, itemStack) && ElectricItemUtils.getPlayerEnergy(player) > energyConsumed) {
            UseHoeEvent event = new UseHoeEvent(player, itemStack, world, x, y, z);
            if (MinecraftForge.EVENT_BUS.post(event)) {
                return;
            }

            if (event.getResult() == Event.Result.ALLOW) {
                ElectricItemUtils.drainPlayerEnergy(player, energyConsumed);
                return;
            }

            if (world.isRemote) {
                return;
            }
            double radius = (int) ModuleManager.computeModularProperty(itemStack, HOE_SEARCH_RADIUS);
            for (int i = (int) Math.floor(-radius); i < radius; i++) {
                for (int j = (int) Math.floor(-radius); j < radius; j++) {
                    if (i * i + j * j < radius * radius) {
                        Block block = world.getBlock(x + i, y, z + j);
                        if (block == Blocks.grass || block == Blocks.dirt) {
                            world.setBlock(x + i, y, z + j, Blocks.farmland);
                            ElectricItemUtils.drainPlayerEnergy(player, ModuleManager.computeModularProperty(itemStack, HOE_ENERGY_CONSUMPTION));
                        }
                    }
                }
            }
            // TODO: Musique? Huh.. world sound effect eh?
            world.playSoundEffect((double) ((float) x + 0.5F), (double) ((float) y + 0.5F), (double) ((float) z + 0.5F),
                    Blocks.farmland.stepSound.soundName, (Blocks.farmland.stepSound.getVolume() + 1.0F) / 2.0F,
                    Blocks.farmland.stepSound.getPitch() * 0.8F);

        }
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY,
                                  float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_HOE;
    }

    @Override
    public String getLocalizedName() {
        return StatCollector.translateToLocal("module.hoe.name");
    }

    @Override
    public String getDescription() {
        return StatCollector.translateToLocal("module.hoe.desc");
    }

    @Override
    public String getTextureFile() {
        return null;
    }

    @Override
    public IIcon getIcon(ItemStack item) {
        return ((Item) Item.itemRegistry.getObject("golden_hoe")).getIconFromDamage(0);
    }
}
