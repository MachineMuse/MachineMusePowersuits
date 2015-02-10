package net.machinemuse.utils;

import net.minecraft.block.Block;
import net.minecraft.world.World;

/**
 * Created by User: Andrew2448
 * 5:50 PM 4/21/13
 */
public class MuseBlockUtils {
    public static byte[] rotateType = new byte[4096];
    public static final int[][] SIDE_COORD_MOD = {{0, -1, 0}, {0, 1, 0}, {0, 0, -1}, {0, 0, 1}, {-1, 0, 0}, {1, 0, 0}};
    public static final int[] SIDE_LEFT = {4, 5, 5, 4, 2, 3};
    public static final int[] SIDE_RIGHT = {5, 4, 4, 5, 3, 2};
    public static final int[] SIDE_OPPOSITE = {1, 0, 3, 2, 5, 4};
    public static final int[] SIDE_UP = {2, 3, 1, 1, 1, 1};
    public static final int[] SIDE_DOWN = {3, 2, 0, 0, 0, 0};

    public static boolean canRotate(int blockId) {
        return rotateType[blockId] != 0;
    }

    public static int rotateVanillaBlock(World world, int block, int meta, int x, int y, int z) {
        switch (rotateType[block]) {
            case 1:
                return SIDE_LEFT[meta];
            case 2:
                if (meta < 6) {
                    meta++;
                    return meta % 6;
                }
                return meta;
            case 3:
                if (meta < 2) {
                    meta++;
                    return meta % 2;
                }
                return meta;
            case 4:
                meta++;
                return meta % 4;
            case 5:
                meta++;
                return meta % 8;
            case 6:
                int upper = meta & 0xC;
                int lower = meta & 0x3;
                return upper + ++lower % 4;
            case 7:
                return (meta + 4) % 12;
            case 8:
                return (meta + 8) % 16;
            case 9:
                for (int i = 2; i < 6; i++) {
                    int[] coords;
                    coords = getAdjacentCoordinatesForSide(x, y, z, i);
                   if (Block.blockRegistry.getIDForObject(world.getBlock(coords[0], coords[1], coords[2])) == block) {
                       world.setBlockMetadataWithNotify(coords[0], coords[1], coords[2], SIDE_OPPOSITE[meta], 1);
                       return SIDE_OPPOSITE[meta];
                   }
                }
                return SIDE_LEFT[meta];
            case 10:
                int shift = 0;
                if (meta > 7) {
                    meta -= 8;
                    shift = 8;
                }
                if (meta == 5)
                    return 6 + shift;
                if (meta == 6)
                    return 5 + shift;
                if (meta == 7)
                    return 0 + shift;
                if (meta == 0) {
                    return 7 + shift;
                }
                return meta + shift;
            case 11:
                meta++; return meta % 16;
        }
        return meta;
    }

    public static int rotateVanillaBlockAlt(World world, int block, int meta, int x, int y, int z) {
        switch (rotateType[block]) {
            case 1:
                return SIDE_RIGHT[meta];
            case 2:
                if (meta < 6) {
                    return (meta + 5) % 6;
                }
                return meta;
            case 3:
                if (meta < 2) {
                    meta++;
                    return meta % 2;
                }
                return meta;
            case 4:
                return (meta + 3) % 4;
            case 5:
                return (meta + 7) % 8;
            case 6:
                int upper = meta & 0xC;
                int lower = meta & 0x3;
                return upper + (lower + 3) % 4;
            case 7:
                return (meta + 8) % 12;
            case 8:
                return (meta + 8) % 16;
            case 9:
                for (int i = 2; i < 6; i++) {
                    int[] coords;
                    coords = getAdjacentCoordinatesForSide(x, y, z, i);
                   if (Block.blockRegistry.getIDForObject(world.getBlock(coords[0], coords[1], coords[2])) == block) {
                       world.setBlockMetadataWithNotify(coords[0], coords[1], coords[2], SIDE_OPPOSITE[meta], 1);
                       return SIDE_OPPOSITE[meta];
                   }
                }
                return SIDE_RIGHT[meta];
            case 10:
                int shift = 0;
                if (meta > 7) {
                    meta -= 8;
                    shift = 8;
                }
                if (meta == 5)
                    return 6 + shift;
                if (meta == 6)
                    return 5 + shift;
                if (meta == 7)
                    return 0 + shift;
                if (meta == 0)
                    return 7 + shift;
                break;
            case 11:
                meta++; return meta % 16;
        }
        return meta;
    }

    public static int[] getAdjacentCoordinatesForSide(int x, int y, int z, int side) {
        return new int[]{x + SIDE_COORD_MOD[side][0], y + SIDE_COORD_MOD[side][1], z + SIDE_COORD_MOD[side][2]};
    }

    static {
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("wood"))] = 7;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("dispenser"))] = 2;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("railPowered"))] = 3;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("railDetector"))] = 3;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("pistonStickyBase"))] = 2;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("pistonBase"))] = 2;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stoneSingleSlab"))] = 8;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsWoodOak"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("chest"))] = 9;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("furnaceIdle"))] = 1;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("furnaceBurning"))] = 1;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("signPost"))] = 11;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("rail"))] = 3;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsCobblestone"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("lever"))] = 10;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("pumpkin"))] = 4;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("pumpkinLantern"))] = 4;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("redstoneRepeaterIdle"))] = 6;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("redstoneRepeaterActive"))] = 6;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsBrick"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsStoneBrick"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsNetherBrick"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("woodSingleSlab"))] = 8;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsSandStone"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("enderChest"))] = 1;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsWoodSpruce"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsWoodBirch"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsWoodJungle"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("chestTrapped"))] = 9;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("stairsNetherQuartz"))] = 5;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("hopperBlock"))] = 2;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("railActivator"))] = 3;
       rotateType[Block.blockRegistry.getIDForObject(Block.blockRegistry.getObject("dropper"))] = 2;
    }
}
