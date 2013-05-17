package net.machinemuse.powersuits.powermodule.tool;

import cofh.api.tileentity.IReconfigurableFacing;
import cpw.mods.fml.client.FMLClientHandler;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.relauncher.Side;
import ic2.api.energy.tile.IEnergySink;
import ic2.api.energy.tile.IEnergySource;
import ic2.api.tile.IWrenchable;
import mods.mffs.api.IMFFS_Wrench;
import net.machinemuse.api.IModularItem;
import net.machinemuse.api.moduletrigger.IRightClickModule;
import net.machinemuse.powersuits.common.ModCompatability;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.powermodule.PowerModuleBase;
import net.machinemuse.utils.MuseBlockUtils;
import net.machinemuse.utils.MuseCommonStrings;
import net.machinemuse.utils.MuseItemUtils;
import net.minecraft.block.Block;
import net.minecraft.entity.item.EntityItem;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.item.ItemStack;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.ForgeDirection;

import java.util.List;

/**
 * Created by User: Andrew2448
 * 4:39 PM 4/21/13
 */
public class OmniWrenchModule extends PowerModuleBase implements IRightClickModule {
    public static final String MODULE_OMNI_WRENCH = "Prototype OmniWrench";
    public static final int[] SIDE_OPPOSITE = {1, 0, 3, 2, 5, 4};

    public OmniWrenchModule(List<IModularItem> validItems) {
        super(validItems);
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
    public String getName() {
        return MODULE_OMNI_WRENCH;
    }

    @Override
    public String getDescription() {
        return "A wrench which can interact with almost every mod.";
    }

    @Override
    public void onRightClick(EntityPlayer playerClicking, World world, ItemStack item) {
    }

    @Override
    public void onItemUse(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
    }

    @Override
    public boolean onItemUseFirst(ItemStack itemStack, EntityPlayer player, World world, int x, int y, int z, int side, float hitX, float hitY, float hitZ) {
        int bId = world.getBlockId(x, y, z);
        int bMeta = world.getBlockMetadata(x, y, z);

        if (MuseBlockUtils.canRotate(bId)) {
            if (player.isSneaking()) {
                world.setBlockMetadataWithNotify(x, y, z, MuseBlockUtils.rotateVanillaBlockAlt(world, bId, bMeta, x, y, z), 3);
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                    String soundName = Block.blocksList[bId].stepSound.getPlaceSound();
                    FMLClientHandler.instance().getClient().sndManager.playSoundFX(soundName, 1.0F, 0.6F);
                }
            } else {
                world.setBlockMetadataWithNotify(x, y, z, MuseBlockUtils.rotateVanillaBlock(world, bId, bMeta, x, y, z), 3);
                if (FMLCommonHandler.instance().getEffectiveSide() == Side.CLIENT) {
                    String soundName = Block.blocksList[bId].stepSound.getPlaceSound();
                    FMLClientHandler.instance().getClient().sndManager.playSoundFX(soundName, 1.0F, 0.8F);
                }
            }
            return !world.isRemote;
        }
        if (Block.blocksList[bId].rotateBlock(world, x, y, z, ForgeDirection.getOrientation(side))) {
            return !world.isRemote;
        }

        TileEntity tile = world.getBlockTileEntity(x, y, z);
        if ((tile instanceof IWrenchable)) {
            IWrenchable wrenchTile = (IWrenchable) tile;

            if (player.isSneaking()) {
                side = OmniWrenchModule.SIDE_OPPOSITE[side];
            }

            if (((tile instanceof IMFFS_Wrench)) && (!((IMFFS_Wrench) tile).wrenchCanManipulate(player, side))) {
                return false;
            }

            if ((side == wrenchTile.getFacing()) && (wrenchTile.wrenchCanRemove(player))) {
                ItemStack dropBlock = wrenchTile.getWrenchDrop(player);

                if (dropBlock != null) {
                    world.setBlock(x, y, z, 0);
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
                    if (((wrenchTile instanceof IEnergySource)) && ((wrenchTile instanceof IEnergySink))) {
                        wrenchTile.setFacing((short) side);
                    }
                } else {
                    wrenchTile.setFacing((short) side);
                }
            }
            return !world.isRemote;
        }
        if ((tile instanceof IReconfigurableFacing)) {
            if ((!world.isRemote) && (!player.isSneaking())) {
                return ((IReconfigurableFacing) tile).rotateBlock();
            }
            return false;
        }
        return false;
    }

    @Override
    public void onPlayerStoppedUsing(ItemStack itemStack, World world, EntityPlayer player, int par4) {
    }
}
