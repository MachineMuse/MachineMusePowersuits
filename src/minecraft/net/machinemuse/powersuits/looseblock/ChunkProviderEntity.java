package net.machinemuse.powersuits.looseblock;

import net.minecraft.entity.EnumCreatureType;
import net.minecraft.util.IProgressUpdate;
import net.minecraft.world.ChunkPosition;
import net.minecraft.world.World;
import net.minecraft.world.chunk.Chunk;
import net.minecraft.world.chunk.IChunkProvider;

import java.util.List;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:13 PM, 07/05/13
 */
public class ChunkProviderEntity implements IChunkProvider {
    @Override
    public boolean chunkExists(int i, int j) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Chunk provideChunk(int i, int j) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public Chunk loadChunk(int i, int j) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void populate(IChunkProvider ichunkprovider, int i, int j) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean saveChunks(boolean flag, IProgressUpdate iprogressupdate) {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean unloadQueuedChunks() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public boolean canSave() {
        return false;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public String makeString() {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public List getPossibleCreatures(EnumCreatureType enumcreaturetype, int i, int j, int k) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public ChunkPosition findClosestStructure(World world, String s, int i, int j, int k) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public int getLoadedChunkCount() {
        return 0;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void recreateStructures(int i, int j) {
        //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public void func_104112_b() {
        //To change body of implemented methods use File | Settings | File Templates.
    }
}
