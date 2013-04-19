package net.machinemuse.powersuits.block;

import net.machinemuse.general.gui.MuseIcon;
import net.machinemuse.powersuits.common.Config;
import net.minecraft.block.Block;
import net.minecraft.block.material.Material;
import net.minecraft.client.renderer.texture.IconRegister;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.world.World;
import net.minecraftforge.common.MinecraftForge;
import cpw.mods.fml.common.registry.GameRegistry;
import cpw.mods.fml.common.registry.LanguageRegistry;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

public class BlockLuxCapacitor extends Block {
	public static int assignedBlockID;
	protected int renderType;

	public BlockLuxCapacitor setRenderType(int id) {
		this.renderType = id;
		return this;
	}

	public BlockLuxCapacitor() {
		super(assignedBlockID, Material.circuits);

		// Block's creative tab
		setCreativeTab(Config.getCreativeTab());

		// Block's hardness (base time to harvest it with the correct tool).
		// Sand = 0.5, Stone = 1.5, Ore = 3.0 Obsidian = 20
		setHardness(0.5F);

		// Block's resistance to explosions. Stone = 10, obsidian = 2000
		setResistance(10.0F);

		// Sound to play when player steps on the block
		setStepSound(Block.soundMetalFootstep);

		// How much light is stopped by this block; 0 for air, 255 for fully
		// opaque.
		setLightOpacity(0);

		// Light level, 0-1. Gets multiplied by 15 and truncated to find the
		// actual light level for the block.
		setLightValue(1.0f);

		// Whether to receive random ticks e.g. plants
		setTickRandomly(false);

		// Harvest level for this block. par2 can be pickaxe, axe, or shovel, or
		// a different toolclass. par3 is the minimum level of item required to
		// break it:
		// 0=bare hands, 1=wood, 2=stone, 3=iron, 4=diamond
		MinecraftForge.setBlockHarvestLevel(this, "pickaxe", 0);

		// Finally, register the block so that it appears in the game. New
		// standard requires a name to be passed.
		GameRegistry.registerBlock(this, "luxCapacitor");

		GameRegistry.registerTileEntity(TileEntityLuxCapacitor.class, "luxCapacitor");

		setUnlocalizedName("mmmPowersuits.luxCapacitor");

		LanguageRegistry.addName(this, "Lux Capacitor");
	}

	@Override
	@SideOnly(Side.CLIENT)
	public void registerIcons(IconRegister iconRegister) {
		this.blockIcon = iconRegister.registerIcon(MuseIcon.ICON_PREFIX + "bluelight");
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

	@Override
	public boolean renderAsNormalBlock() {
		return false;
	}

	@Override
	public boolean isOpaqueCube() {
		return false;
	}

	@Override
	public boolean hasTileEntity(int metadata) {
		return true;
	}

	@Override
	public TileEntity createTileEntity(World world, int metadata) {
		return new TileEntityLuxCapacitor();

	}

}
