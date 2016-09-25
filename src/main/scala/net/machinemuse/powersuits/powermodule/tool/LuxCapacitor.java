package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.api.IModularItem;
import net.machinemuse.api.ModuleManager;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.numina.geometry.Colour;
import net.machinemuse.powersuits.entity.EntityLuxCapacitor;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.ElectricItemUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Items;
import net.minecraft.item.EnumAction;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ActionResult;
import net.minecraft.util.EnumActionResult;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.EnumHand;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.List;

public class LuxCapacitor extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_LUX_CAPACITOR = "Lux Capacitor";
    public static final String ENERGY = "Lux Capacitor Energy Consumption";
    public static final String RED =  "Lux Capacitor Red Hue";
    public static final String GREEN = "Lux Capacitor Green Hue";
    public static final String BLUE = "Lux Capacitor Blue Hue";

    public LuxCapacitor(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(new ItemStack(Items.GLOWSTONE_DUST, 1));
        addInstallCost(new ItemStack(Items.IRON_INGOT, 2));
        addBaseProperty(ENERGY, 100, "J");
        addTradeoffProperty("Red", RED, 1, "%");
        addTradeoffProperty("Green", GREEN, 1, "%");
        addTradeoffProperty("Blue", BLUE, 1, "%");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MODULE_LUX_CAPACITOR;
    }

    @Override
    public String getUnlocalizedName() { return "luxCapacitor";
    }

    @Override
    public String getDescription() {
        return "Launch a virtually infinite number of attractive light sources at the wall.";
    }

    @Override
    public ActionResult onRightClick(EntityPlayer player, World world, ItemStack itemStack, EnumHand hand) {
//        player.setItemInUse(itemStack, 10);
        if (!world.isRemote) {
            double energyConsumption = ModuleManager.computeModularProperty(itemStack, ENERGY);
            // MuseHeatUtils.heatPlayer(player, energyConsumption / 500);
            if (ElectricItemUtils.getPlayerEnergy(player) > energyConsumption) {
                ElectricItemUtils.drainPlayerEnergy(player, energyConsumption);

                Colour colour = new Colour(0);
                colour.r = ModuleManager.computeModularProperty(itemStack, RED);
                colour.g = ModuleManager.computeModularProperty(itemStack, GREEN);
                colour.b = ModuleManager.computeModularProperty(itemStack, BLUE);
                colour.a = 1;

                EntityLuxCapacitor luxCapacitor = new EntityLuxCapacitor(world, player, colour.getInt());
                world.spawnEntityInWorld(luxCapacitor);
            }
            return ActionResult.newResult(EnumActionResult.SUCCESS, itemStack);
        }
        return ActionResult.newResult(EnumActionResult.FAIL, itemStack);
    }

    @Override
    public EnumActionResult onItemUse(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumHand hand, EnumFacing facing, float hitX, float hitY, float hitZ) {
        return null;
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, BlockPos pos, EnumFacing side, float hitX, float hitY, float hitZ) {
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
        // TODO Auto-generated method stub

    }

    @Override
    public EnumAction getItemUseAction(ItemStack stack) {
        return EnumAction.BOW;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.luxCapacitor;
    }
}
