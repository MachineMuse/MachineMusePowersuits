package net.machinemuse.powersuits.common.items.modules.tool;

import net.machinemuse.numina.api.item.IModularItem;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.items.ItemComponent;
import net.machinemuse.powersuits.common.items.modules.PowerModuleBase;
import net.machinemuse.numina.utils.string.MuseCommonStrings;
import net.machinemuse.numina.utils.item.MuseItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

import java.util.List;

/**
 * Created by Eximius88 on 1/29/14.
 */
public class AOEPickUpgradeModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    public static final String MODULE_AOE_PICK_UPGRADE = "Diamond Drill Upgrade";
    //public static final ItemStack ironPickaxe = new ItemStack(Item.pickaxeIron);
    public static final String ENERGY_CONSUMPTION = "Energy Consumption";
    public AOEPickUpgradeModule(List<IModularItem> validItems) {
        super(validItems);
        addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        //addInstallCost(new ItemStack(Item.diamond, 3));
        addBaseProperty(ENERGY_CONSUMPTION, 5, "J");
    }

    @Override
    public String getCategory() {
        return MuseCommonStrings.CATEGORY_SPECIAL;
    }

    @Override
    public String getDataName() {
        return MODULE_AOE_PICK_UPGRADE;
    }

    @Override
    public String getUnlocalizedName() {
        return "aoePickUpgrade";
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {
        return false;
    }

    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {
        return false;
    }

    @Override
    public void handleBreakSpeed(PlayerEvent.BreakSpeed breakSpeed) {
    }

    @Override
    public ItemStack getEmulatedTool() {
        return null; // FIXME
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.aoePickUpgrade;
    }
}