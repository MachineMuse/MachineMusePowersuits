package net.machinemuse.powersuits.powermodule.tool;

import mekanism.common.config.MekanismConfig.general;
import mekanism.common.item.ItemAtomicDisassembler;
import net.machinemuse.numina.api.module.EnumModuleCategory;
import net.machinemuse.numina.api.module.EnumModuleTarget;
import net.machinemuse.numina.api.module.IBlockBreakingModule;
import net.machinemuse.numina.api.module.IToggleableModule;
import net.machinemuse.powersuits.api.constants.MPSModuleConstants;
import net.machinemuse.powersuits.client.event.MuseIcon;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.powersuits.utils.ElectricItemUtils;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.entity.EntityLivingBase;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.event.entity.player.PlayerEvent;

//


/**
 * Mekanism Atomic Disassembler module
 */
public class MADModule extends PowerModuleBase implements IBlockBreakingModule, IToggleableModule {
    ItemStack emulatedTool = ItemStack.EMPTY;

    //FIXME: need to create a proper storage location for all these emulated tools.



    public MADModule(EnumModuleTarget moduleTargetIn) {
        super(moduleTargetIn);

        if (ModCompatibility.isMekanismLoaded()) {
            emulatedTool = new ItemStack(Item.REGISTRY.getObject(new ResourceLocation("mekanism", "atomicdisassembler")), 1);
            NBTTagCompound nbt = emulatedTool.getTagCompound();
            if (nbt == null)
                nbt = new NBTTagCompound();

            NBTTagCompound nbt2 = nbt.getCompoundTag("mekData");
            if (nbt2 == null)
                nbt2 = new NBTTagCompound();

            nbt2.setInteger("mode", 3);
            nbt2.setDouble("energyStored", 1000000.0);
            nbt.setTag("mekData", nbt2);
            emulatedTool.setTagCompound(nbt);
        }
    }

    @Override
    public boolean canHarvestBlock(ItemStack stack, IBlockState state, EntityPlayer player) {

        // TODO: check more than this, like power?
//        return state.getBlock() != Blocks.BEDROCK;
        return false;
    }

    // MPS Version
    @Override
    public boolean onBlockDestroyed(ItemStack stack, World worldIn, IBlockState state, BlockPos pos, EntityLivingBase entityLiving) {

        // FIXME: should this be handled here or in other modules?

        if (state.getBlockHardness(worldIn, pos) != 0.0D) {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer)entityLiving, (int) (general.DISASSEMBLER_USAGE *
                            ((ItemAtomicDisassembler) emulatedTool.getItem()).getEfficiency(emulatedTool)));
        } else {
            ElectricItemUtils.drainPlayerEnergy((EntityPlayer)entityLiving, (int) (general.DISASSEMBLER_USAGE *
                    (((ItemAtomicDisassembler) emulatedTool.getItem()).getEfficiency(emulatedTool)) / 2));
        }

        if (!worldIn.isRemote) {
            NBTTagCompound nbt = emulatedTool.getTagCompound();
            if (nbt == null)
                nbt = new NBTTagCompound();

            NBTTagCompound nbt2 = nbt.getCompoundTag("mekData");
            if (nbt2 == null)
                nbt2 = new NBTTagCompound();

            int playerEnergy = ElectricItemUtils.getPlayerEnergy((EntityPlayer)entityLiving)/10;
            nbt2.setInteger("mode", 3);
            nbt2.setDouble("energyStored",  playerEnergy > 1000000.0 ? 1000000.0 : playerEnergy);
            nbt.setTag("mekData", nbt2);
            emulatedTool.setTagCompound(nbt);
        }
        return true;
    }

    // MPS Version
    public boolean onBlockStartBreak(ItemStack itemstack, BlockPos pos, EntityPlayer player) {

        // TODO: set emulatedTool power value based on player energy

        NBTTagCompound nbt = emulatedTool.getTagCompound();
        if (!player.world.isRemote) {
            if (nbt == null)
                nbt = new NBTTagCompound();

            NBTTagCompound nbt2 = nbt.getCompoundTag("mekData");
            if (nbt2 == null)
                nbt2 = new NBTTagCompound();

            int playerEnergy = ElectricItemUtils.getPlayerEnergy(player)/10;

            nbt2.setInteger("mode", 3);
            nbt2.setDouble("energyStored", playerEnergy > 1000000.0 ? 1000000.0 : playerEnergy);
            nbt.setTag("mekData", nbt2);
            emulatedTool.setTagCompound(nbt);
        }
        return emulatedTool.getItem().onBlockStartBreak(emulatedTool, pos, player);
    }

    @Override
    public void handleBreakSpeed(PlayerEvent.BreakSpeed event) {
    }

    @Override
    public ItemStack getEmulatedTool() {
        return emulatedTool;
    }

    @Override
    public TextureAtlasSprite getIcon(ItemStack item) {
        return MuseIcon.madModule;
    }

    @Override
    public EnumModuleCategory getCategory() {
        return EnumModuleCategory.CATEGORY_TOOL;
    }

    @Override
    public String getDataName() {
        return MPSModuleConstants.MODULE_MAD_MODULE__DATANAME;
    }
}
