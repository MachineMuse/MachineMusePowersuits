package net.machinemuse.powersuits.utils.modulehelpers;


import it.unimi.dsi.fastutil.longs.Long2ObjectMap;
import it.unimi.dsi.fastutil.longs.Long2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectIterator;
import net.machinemuse.powersuits.block.TileEntityPortal;
import net.minecraft.entity.Entity;
import net.minecraft.init.Blocks;
import net.minecraft.tileentity.TileEntity;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.Teleporter;
import net.minecraft.world.WorldServer;
import net.minecraft.world.chunk.Chunk;

public class DimensionalRiftHelper extends Teleporter {
    private final WorldServer worldServerInstance;

    private final Long2ObjectMap<PortalPosition> destinationCoordinateCache = new Long2ObjectOpenHashMap();

    public DimensionalRiftHelper(WorldServer par1WorldServer) {
        super(par1WorldServer);
        this.worldServerInstance = par1WorldServer;
    }

    public void placeInPortal(Entity entityIn, float rotationYaw) {
        if (!this.placeInExistingPortal(entityIn, rotationYaw)) {
            if (this.worldServerInstance.provider.getDimensionType().getId() != -1) {
                BlockPos otherpos = this.worldServerInstance.getTopSolidOrLiquidBlock(new BlockPos(entityIn).add(0, -1, 0));
                entityIn.setLocationAndAngles(otherpos.getX(), otherpos.getY(), otherpos.getZ(), rotationYaw, 0.0F);
            } else {
                makePortal(entityIn);
            }
        }
    }

    public boolean placeInExistingPortal(Entity entityIn, float rotationYaw) {

        // TODO: check this whole thing
        int x = MathHelper.floor(entityIn.posX);
        int y = MathHelper.floor(entityIn.posY) - 1;
        int z = MathHelper.floor(entityIn.posZ);

        TileEntity destPortal = null;
        for (int s = 0; (s <= 5) && (destPortal == null); s++) {
            for (int dx = -s; dx <= s; dx++) {
                for (int dz = -s; dz <= s; dz++) {
                    if (destPortal == null) {
                        destPortal = findPortalInChunk(new BlockPos(x + dx * 16, y, z + dz * 16));
                    }
                }
            }
        }
        if (destPortal != null) {
            entityIn.setLocationAndAngles(destPortal.getPos().getX() + 0.5D, destPortal.getPos().getY() + 1, destPortal.getPos().getZ() + 0.5D, entityIn.rotationYaw, entityIn.rotationPitch);
            entityIn.motionX = (entityIn.motionY = entityIn.motionZ = 0.0D);
            return true;
        }
        return false;
    }

    public TileEntity findPortalInChunk(BlockPos pos) {
        Chunk chunk = this.worldServerInstance.getChunk(pos);
        for (Object tile : chunk.getTileEntityMap().values()) {
            if ((tile instanceof TileEntityPortal)) {
                return (TileEntity) tile;
            }
        }
        return null;
    }

    public boolean makePortal(Entity entity) {
        int ex = MathHelper.floor(entity.posX);
        int ey = MathHelper.floor(entity.posY) - 1;
        int ez = MathHelper.floor(entity.posZ);

        ey /= 5;
        ey += 22;
        if (ey > 247) {
            ey = 247;
        }
        for (int x = -3; x <= 3; x++) {
            for (int z = -3; z <= 3; z++) {
                for (int y = -2; y <= 4; y++) {
                    if ((x == 0) && (y == -1) && (z == 0)) {
                    } else if ((y <= -2)) {
                        this.worldServerInstance.setBlockState(new BlockPos(ex + x, ey + y, ez + z), Blocks.STONE.getDefaultState());
                    } else if ((y == 0) && ((x == 2) || (x == -2) || (z == 2) || (z == -2))) {
                        //this.worldServerInstance.setBlock(ex + x, ey + y, ez + z, 0, 5, 3);
                    } else {
                        this.worldServerInstance.setBlockState(new BlockPos(ex + x, ey + y, ez + z), Blocks.AIR.getDefaultState());
                    }
                }
            }
        }
        entity.setLocationAndAngles(ex + 0.5D, ey, ez + 0.5D, entity.rotationYaw, 0.0F);
        entity.motionX = (entity.motionY = entity.motionZ = 0.0D);
        return true;
    }

    /**
     * called periodically to remove out-of-date portal locations from the cache list. Argument par1 is a
     * WorldServer.getTotalWorldTime() getValue.
     */
    public void removeStalePortalLocations(long worldTime) {
        if (worldTime % 100L == 0L) {
            long i = worldTime - 300L; // TODO: Check. This used to be 600L
            ObjectIterator<PortalPosition> objectiterator = this.destinationCoordinateCache.values().iterator();

            while (objectiterator.hasNext()) {
                PortalPosition portalposition = objectiterator.next();
                if (portalposition == null || portalposition.lastUpdateTime < i) {
                    objectiterator.remove();
                }
            }
        }
    }
}