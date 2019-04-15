package net.machinemuse.powersuits.powermodule.tool;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.module.EnumModuleCategory;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IBlockBreakingModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.init.Blocks;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent.BreakSpeed;

import javax.annotation.Nonnull;


// FIXME!!!! this module does nothing. Maybe rewrite it to use it as an actual chisel
public class ChiselModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    // TODO Fixme put actual item.
    private static final ItemStack emulatedTool = new ItemStack(
            Item.REGISTRY.getObject(new ResourceLocation("chisel", "chisel_iron")), 1);

    public ChiselModule(EnumModuleTarget moduleTarget) {
        super(moduleTarget);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Item.getItemFromBlock(Blocks.OBSIDIAN), 2));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        addBasePropertyDouble(MPSModuleConstants.CHISEL_ENERGY_CONSUMPTION, 500, "RF");
        addBasePropertyDouble(MPSModuleConstants.CHISEL_HARVEST_SPEED, 8, "x");
        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.CHISEL_ENERGY_CONSUMPTION, 9500);
        addTradeoffPropertyDouble(MPSModuleConstants.OVERCLOCK, MPSModuleConstants.CHISEL_HARVEST_SPEED, 22);
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_CHISEL__DATANAME;
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) Math.round(ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.CHISEL_ENERGY_CONSUMPTION));
    }

    @Override
    public boolean onBlockDestroyed(ItemStack itemStack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving, int playerEnergy) {
        if (this.canHarvestBlock(itemStack, state, (EntityPlayer) entityLiving, pos, playerEnergy)) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer) entityLiving, getEnergyUsage(itemStack));
            return true;
        }
        return false;
    }

    @Override
    public void handleBreakSpeed(BreakSpeed event) {
        event.setNewSpeed((float) (event.getNewSpeed() *
                ModuleManager.INSTANCE.getOrSetModularPropertyDouble(event.getEntityPlayer().inventory.getCurrentItem(), MPSModuleConstants.CHISEL_HARVEST_SPEED)));
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool; // FIXME TOO!!
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return Minecraft.getMinecraft().getRenderItem().getItemModelMesher().getItemModel(emulatedTool).getParticleTexture();
    }
}