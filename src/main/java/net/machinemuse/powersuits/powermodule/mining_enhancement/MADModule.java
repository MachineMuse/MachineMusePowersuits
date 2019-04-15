package net.machinemuse.powersuits.powermodule.mining_enhancement;

import net.machinemuse.numina.energy.ElectricItemUtils;
import net.machinemuse.numina.item.MuseItemUtils;
import net.machinemuse.numina.misc.ModCompatibility;
import net.machinemuse.numina.module.EnumModuleTarget;
import net.machinemuse.numina.module.IMiningEnhancementModule;
import net.machinemuse.numina.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModuleManager;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;

import javax.annotation.Nonnull;

//


/**
 * Mekanism Atomic Disassembler module
 */
public class MADModule extends PowerModuleBase implements IToggleableModule, IMiningEnhancementModule {
    ItemStack emulatedTool = ItemStack.EMPTY;

    //FIXME: need to create a proper storage location for all these emulated tools.

    public MADModule(EnumModuleTarget moduleTargetIn) {
        super(moduleTargetIn);
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.solenoid, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
        ModuleManager.INSTANCE.addInstallCost(getDataName(), new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("mekanism", "atomicdisassembler")), 1));
        if (ModCompatibility.isMekanismLoaded()) {
            emulatedTool = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("mekanism", "atomicdisassembler")), 1);
        }

        addBasePropertyDouble(MPSModuleConstants.ENERGY_CONSUMPTION, 100, "RF");
    }

    /**
     * Called before a block is broken.  Return true to prevent default block harvesting.
     *
     * Note: In SMP, this is called on both client and server sides!
     *
     * @param itemstack The current ItemStack
     * @param pos Block's position in world
     * @param player The Player that is wielding the item
     * @return True to prevent harvesting, false to continue as normal
     */
    @Override
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {
        // set mode for the device
        NBTTagCompound nbt = emulatedTool.getTagCompound();
        if (nbt == null) {
            nbt = new NBTTagCompound();
            NBTTagCompound nbt2 = new NBTTagCompound();
            nbt2.setInteger("mode", 3);
            nbt.setTag("mekData", nbt2);
            emulatedTool.setTagCompound(nbt);
        }

        ElectricItemUtils.chargeItem(emulatedTool, 100000);
        // TODO: set tag manually?          //        System.out.println("emulated tool: " + emulatedTool.serializeNBT().toString());

//        {id:"mekanism:atomicdisassembler",Count:1b,tag:{mekData:{mode:3,energyStored:1000000.0d}},Damage:0s}

//        NBTTagCompound nbt2 = new NBTTagCompound();


// Fixme: todo in 1.13 when emulated tools are actually stored
        // charge the device for usage
//        ElectricItemUtils.chargeEmulatedToolFromPlayerEnergy(player, emulatedTool);
        return emulatedTool.getItem().onBlockStartBreak(emulatedTool, pos, player);
    }

    @Override
    public int getEnergyUsage(@Nonnull ItemStack itemStack) {
        return (int) ModuleManager.INSTANCE.getOrSetModularPropertyDouble(itemStack, MPSModuleConstants.ENERGY_CONSUMPTION);
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.madModule;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_MAD__DATANAME;
    }
}
