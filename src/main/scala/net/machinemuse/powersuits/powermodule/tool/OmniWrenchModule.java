package net.machinemuse.powersuits.powermodule.tool;

import cofh.api.tileentity.IReconfigurableFacing;
import cofh.api.block.IDismantleable;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import crazypants.enderio.TileEntityEio;
import ic2.api.tile.IWrenchable;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.energy.tile.IEnergySink;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.MuseItemTag;
import net.machinemuse.api.EnderIOTool;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.api.moduletrigger.IPlayerTickModule;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.machinemuse.utils.MuseBlockUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.Entity;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.StatCollector;
import net.minecraft.world.World;
import net.minecraftforge.common.util.ForgeDirection;
import net.minecraftforge.event.entity.PlaySoundAtEntityEvent;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 4:39 PM 4/21/13
 */
public class OmniWrenchModule extends PowerModuleBase implements IRightClickModule, IPlayerTickModule {
public static final String MODULE_OMNI_WRENCH = "Prototype OmniWrench";
public static final int[] SIDE_OPPOSITE = {1, 0, 3, 2, 5, 4};

public OmniWrenchModule(List<IModularItem> validItems) {
        super(validItems);

        // TODO: It seems like OmniTools isn't available for 1.7.10? Perhaps this optional cost should be removed and/or reimplemented? - 2014-12-01 Korynkai
        if (ModCompatability.isOmniToolsLoaded() && GameRegistry.findItemStack("OmniTools", "OmniWrench", 1) != null) {
                addInstallCost(GameRegistry.findItemStack("OmniTools", "OmniWrench", 1));
        } else {
                addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.controlCircuit, 1));
                addInstallCost(MuseItemUtils.copyAndResize(ItemComponent.servoMotor, 2));
        }
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
        return MODULE_OMNI_WRENCH;
}

@Override
public String getLocalizedName() {
        return StatCollector.translateToLocal("module.omniwrench.name");
}

@Override
public String getDescription() {
        return StatCollector.translateToLocal("module.omniwrench.desc");
}

@Override
public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
}

@Override
public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
}

@Override
public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        Block b = world.getBlock(x, y, z);
        int bMeta = world.getBlockMetadata(x, y, z);

        if (MuseBlockUtils.canRotate(Block.getIdFromBlock(b))) {
                if (player.isSneaking()) {
                        world.setBlockMetadataWithNotify(x, y, z, MuseBlockUtils.rotateVanillaBlockAlt(world, Block.getIdFromBlock(b), bMeta, x, y, z), 3);
                        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                                world.playSoundAtEntity((Entity) player, b.stepSound.soundName, 1.0F, 0.6F);
                        }
                } else {
                        world.setBlockMetadataWithNotify(x, y, z, MuseBlockUtils.rotateVanillaBlock(world, Block.getIdFromBlock(b), bMeta, x, y, z), 3);
                        if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                                world.playSoundAtEntity((Entity) player, b.stepSound.soundName, 1.0F, 0.8F);
                        }
                }
                return !world.isRemote;
        }

        TileEntity tile = world.getTileEntity(x, y, z);

        if (ModCompatability.isEnderIOLoaded()) {
                // Hmm... Seems this allows breakage of more EnderIO blocks than the YetaWrench...
                // Though it seems there's no better way to provide similar functionality with EnderIO machines... - 2014-12-01 Korynkai
                if (tile instanceof TileEntityEio) {
                        if (player.isSneaking()) {
                                b.removedByPlayer(world, player, x, y, z, true);
                        } else {
                                b.rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side));
                        }
                }
        }
        // IC2: UNTESTED - 2014-12-01 Korynkai
        if (ModCompatability.isIndustrialCraftLoaded()) {
                if (tile instanceof IWrenchable) {
                        IWrenchable wrenchTile = (IWrenchable) tile;

                        if (player.isSneaking()) {
                                side = OmniWrenchModule.SIDE_OPPOSITE[side];
                        }

                        // TODO: Fixme? Or implement Calclavia's mod? - 2014-12-01 Korynkai
                        //  if (((tile instanceof IMFFS_Wrench)) && (!((IMFFS_Wrench) tile).wrenchCanManipulate(player, side))) {
                        //    return false;
                        //  }

                        if ((side == wrenchTile.getFacing()) && (wrenchTile.wrenchCanRemove(player))) {
                                ItemStack dropBlock = wrenchTile.getWrenchDrop(player);

                                if (dropBlock != null) {
                                        world.setBlockToAir(x, y, z);
                                        if (!world.isRemote) {
                                                float f = 0.7F;
                                                double x2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                                                double y2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                                                double z2 = world.rand.nextFloat() * f + (1.0F - f) * 0.5D;
                                                EntityItem entity = new EntityItem(world, x + x2, y + y2, z + z2, dropBlock);
                                                entity.delayBeforeCanPickup = 10;
                                                world.spawnEntityInWorld(entity);
                                        }
                                }

                                return !world.isRemote;
                        }

                        if (!world.isRemote) {
                                if ((side == 0) || (side == 1)) {
                                        if ((wrenchTile instanceof IEnergySource) && (wrenchTile instanceof IEnergySink)) {
                                                wrenchTile.setFacing((short) side);
                                        }
                                } else {
                                        wrenchTile.setFacing((short) side);
                                }
                        }
                        return !world.isRemote;
                }
        }

        if (ModCompatability.isCoFHCoreLoaded()) {
                if (!world.isRemote) {
                        if (tile instanceof IReconfigurableFacing) {
                                if (!player.isSneaking()) {
                                        return ((IReconfigurableFacing) tile).rotateBlock();
                                }
                        }

                        if (b instanceof IDismantleable) {
                                IDismantleable machine = (IDismantleable) b;
                                if (machine.canDismantle(player, world, x, y, z)) {
                                        machine.dismantleBlock(player, world, x, y, z, false);
                                }
                        }
                }
        }

        return false;
}

@Override
public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
}

@Override
public void onPlayerTickActive(EntityPlayer player, ItemStack item) {
        if (ModCompatability.isEnderIOLoaded()) {
                if (item != null && item.getItem() instanceof IModularItem) {
                        if (!MuseItemTag.getMuseItemTag(item).getBoolean("eioFacadeTransparency")) {
                                MuseItemTag.getMuseItemTag(item).setString("eioNoCompete", MODULE_OMNI_WRENCH);
                                MuseItemTag.getMuseItemTag(item).setBoolean("eioFacadeTransparency", true);
                                MuseItemTag.getMuseItemTag(item).setBoolean("eioManipulateConduit", true);
                        }
                }
        }
}

@Override
public void onPlayerTickInactive(EntityPlayer player, ItemStack item) {
        if (ModCompatability.isEnderIOLoaded()) {
                if (item != null && item.getItem() instanceof IModularItem) {
                        if ((MuseItemTag.getMuseItemTag(item).getString("eioNoCompete") != null) 
                        		&& (!MuseItemTag.getMuseItemTag(item).getString("eioNoCompete").isEmpty())) {
                                if (MuseItemTag.getMuseItemTag(item).getString("eioNoCompete").equals(MODULE_OMNI_WRENCH)) {
                                        MuseItemTag.getMuseItemTag(item).setString("eioNoCompete", "");
                                        if (MuseItemTag.getMuseItemTag(item).getBoolean("eioFacadeTransparency")) {
                                                MuseItemTag.getMuseItemTag(item).setBoolean("eioFacadeTransparency", false);
                                        }
                                        if (MuseItemTag.getMuseItemTag(item).getBoolean("eioManipulateConduit")) {
																								MuseItemTag.getMuseItemTag(item).setBoolean("eioManipulateConduit", false);
                                        }
                                }
                        } else {
                                if (MuseItemTag.getMuseItemTag(item).getBoolean("eioFacadeTransparency")) {
                                        MuseItemTag.getMuseItemTag(item).setBoolean("eioFacadeTransparency", false);
                                }
                                if (MuseItemTag.getMuseItemTag(item).getBoolean("eioManipulateConduit")) {
																								MuseItemTag.getMuseItemTag(item).setBoolean("eioManipulateConduit", false);
                                }
                        }
                }
        }
}
}
