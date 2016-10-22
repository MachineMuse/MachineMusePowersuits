package net.machinemuse.utils;

import net.minecraft.world.World;

/**
 * Created by User: Andrew2448
 * 5:50 PM 4/21/13
 */
public class MuseBlockUtils {
    public static final byte[] rotateType = new byte[4096];
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
//                    if (world.getBlockId(coords[0], coords[1], coords[2]) == block) {
//                        world.setBlockMetadataWithNotify(coords[0], coords[1], coords[2], SIDE_OPPOSITE[meta], 1);
//                        return SIDE_OPPOSITE[meta];
//                    }
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
                    return shift;
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
//                    if (world.getBlockId(coords[0], coords[1], coords[2]) == block) {
//                        world.setBlockMetadataWithNotify(coords[0], coords[1], coords[2], SIDE_OPPOSITE[meta], 1);
//                        return SIDE_OPPOSITE[meta];
//                    }
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
                    return shift;
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
//        rotateType[Block.wood.blockID] = 7;
//        rotateType[Block.dispenser.blockID] = 2;
//        rotateType[Block.railPowered.blockID] = 3;
//        rotateType[Block.railDetector.blockID] = 3;
//        rotateType[Block.pistonStickyBase.blockID] = 2;
//        rotateType[Block.pistonBase.blockID] = 2;
//        rotateType[Block.stoneSingleSlab.blockID] = 8;
//        rotateType[Block.stairsWoodOak.blockID] = 5;
//        rotateType[Block.chest.blockID] = 9;
//        rotateType[Block.furnaceIdle.blockID] = 1;
//        rotateType[Block.furnaceBurning.blockID] = 1;
//        rotateType[Block.signPost.blockID] = 11;
//        rotateType[Block.rail.blockID] = 3;
//        rotateType[Block.stairsCobblestone.blockID] = 5;
//        rotateType[Block.lever.blockID] = 10;
//        rotateType[Block.pumpkin.blockID] = 4;
//        rotateType[Block.pumpkinLantern.blockID] = 4;
//        rotateType[Block.redstoneRepeaterIdle.blockID] = 6;
//        rotateType[Block.redstoneRepeaterActive.blockID] = 6;
//        rotateType[Block.stairsBrick.blockID] = 5;
//        rotateType[Block.stairsStoneBrick.blockID] = 5;
//        rotateType[Block.stairsNetherBrick.blockID] = 5;
//        rotateType[Block.woodSingleSlab.blockID] = 8;
//        rotateType[Block.stairsSandStone.blockID] = 5;
//        rotateType[Block.enderChest.blockID] = 1;
//        rotateType[Block.stairsWoodSpruce.blockID] = 5;
//        rotateType[Block.stairsWoodBirch.blockID] = 5;
//        rotateType[Block.stairsWoodJungle.blockID] = 5;
//        rotateType[Block.chestTrapped.blockID] = 9;
//        rotateType[Block.stairsNetherQuartz.blockID] = 5;
//        rotateType[Block.hopperBlock.blockID] = 2;
//        rotateType[Block.railActivator.blockID] = 3;
//        rotateType[Block.dropper.blockID] = 2;
    }
}
