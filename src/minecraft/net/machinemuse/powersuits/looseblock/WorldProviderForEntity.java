package net.machinemuse.powersuits.looseblock;

import net.minecraft.world.WorldProvider;
import net.minecraft.world.chunk.IChunkProvider;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 10:09 PM, 07/05/13
 */
public class WorldProviderForEntity extends WorldProvider {
    @Override
    public IChunkProvider createChunkGenerator() {
        return new ChunkProviderEntity();
    }

    @Override
    public String getDimensionName() {
        return "entityDimension";
    }
}
