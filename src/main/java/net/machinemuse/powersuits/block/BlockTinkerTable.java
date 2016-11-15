package net.machinemuse.powersuits.block;

import cpw.mods.fml.common.registry.GameRegistry;
import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.machinemuse.powersuits.common.ModularPowersuits;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IIconRegister;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.IIcon;
import net.minecraft.world.IBlockAccess;
import net.minecraft.world.World;

/**
 * This is the tinkertable block. It doesn't do much except look pretty
 * (eventually) and provide a way for the player to access the TinkerTable GUI.
 *
 * @author MachineMuse
 *
 *
 * Ported to Java by lehjr on 10/21/16.
 */
public class BlockTinkerTable extends Block {
    public static IIcon energyIcon;
    protected static int renderType = 0;

    public BlockTinkerTable() {
        super(Material.iron);
        setCreativeTab(Config.getCreativeTab());
        setHardness(1.5F);
        setResistance(1000.0F);
        setStepSound(Block.soundTypeMetal);
        setLightOpacity(0);
        setLightLevel(0.4f);
        setTickRandomly(false);
        GameRegistry.registerTileEntity(TileEntityTinkerTable.class, "tinkerTable");
        setBlockName("tinkerTable");
    }

    public static void setRenderType(int id) {
        renderType = id;
    }

    @Override
    public void registerBlockIcons(IIconRegister iconRegister) {
        this.blockIcon = iconRegister.registerIcon(MuseIcon.ICON_PREFIX + "heatresistantplating");
        energyIcon = blockIcon;
    }

    /**
     * Called upon block activation (right click on the block.)
     */
    @Override
    public boolean onBlockActivated(World world, int x, int y, int z, EntityPlayer player, int par6, float par7, float par8, float par9) {
        if (player.isSneaking()) {
            return false;
        }
        player.openGui(ModularPowersuits.getInstance(), 0, world, x, y, z);
        return true;
    }

    /**
     * returns some value from 0 to 30 or so for different models. Since we're
     * using a custom renderer, we pass in a completely different ID: the
     * assigned block ID. It won't conflict with other mods, since Forge looks
     * it up in a table anyway, but it's still best to have different internal
     * IDs.
     */
    @Override
    public int getRenderType() {
        return renderType;
    }

    /**
     * This method is called on a block after all other blocks gets already
     * created. You can use it to reference and configure something on the block
     * that needs the others ones.
     */
    protected void initializeBlock() {
    }

    /**
     * If this block doesn't render as an ordinary block it will return False
     * (examples: signs, buttons, stairs, etc)
     */
    @Override
    public boolean renderAsNormalBlock() {
        return false;
    }

    /**
     * Returns Returns true if the given side of this block type should be
     * rendered (if it's solid or not), if the adjacent block is at the given
     * coordinates. Args: blockAccess, x, y, z, side
     */
    @Override
    public boolean isBlockSolid(IBlockAccess p_149747_1_, int p_149747_2_, int p_149747_3_, int p_149747_4_, int p_149747_5_) {
        return true;
    }

    /**
     * Is this block (a) opaque and (b) a full 1m cube? This determines whether
     * or not to render the shared face of two adjacent blocks and also
     * whether the player can attach torches, redstone wire, etc to this block.
     */
    @Override
    public boolean isOpaqueCube() {
        return false;
    }

    /**
     * Called throughout the code as a replacement for block instanceof
     * BlockContainer Moving this to the Block base class allows for mods that
     * wish
     * to extend vinella blocks, and also want to have a tile entity on that
     * block, may.
     *
     * Return true from this function to specify this block has a tile entity.
     *
     * @param metadata
     * Metadata of the current block
     * @return True if block has a tile entity, false otherwise
     */
    @Override
    public boolean hasTileEntity(int metadata) {
        return true;
    }

    /**
     * Called throughout the code as a replacement for
     * BlockContainer.getBlockEntity Return the same thing you would from that
     * function. This will
     * fall back to BlockContainer.getBlockEntity if this block is a
     * BlockContainer.
     *
     * @param metadata
     * The Metadata of the current block
     * @return A instance of a class extending TileEntity
     */
    @Override
    public TileEntity createTileEntity(World world, int metadata) {
        return new TileEntityTinkerTable();
    }
}
