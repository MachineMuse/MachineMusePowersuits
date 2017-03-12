package net.machinemuse.powersuits.common;

import net.machinemuse.numina.general.MuseLogger;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.item.ItemBlock;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.fml.common.registry.GameRegistry;

import javax.annotation.Nullable;
import java.util.function.Function;

/**
 * Created by leon on 7/6/16.
 */
public class MPSRegistry {
    private static ResourceLocation nameToRegName(String name) {
        return new ResourceLocation(ModularPowersuits.MODID.toLowerCase(), name);
    }

    /**
     * Sets the registry and unlocalized name and registers the item.
     */
    protected static <T extends Item> T registerItem(T item, String regName, String unlocalizedName) {
        item.setUnlocalizedName(unlocalizedName);
        item.setRegistryName(nameToRegName(regName));
        GameRegistry.register(item);
        return item;
    }

    protected static <T extends Item> T registerItem(T item, String name) {
        item.setUnlocalizedName(ModularPowersuits.MODID.toLowerCase() + "." + name);
        item.setRegistryName(nameToRegName(name));
        GameRegistry.register(item);
        return item;
    }

    //----------------
    protected static <BLOCK extends Block> Block registerBlock(BLOCK block) {
        return registerBlock(block, ItemBlock::new);
    }

    protected static <BLOCK extends Block> Block registerBlock(BLOCK block, @Nullable Function<BLOCK, ItemBlock> itemFactory) {
        GameRegistry.register(block);
        if (itemFactory != null) {
            final ItemBlock itemBlock = itemFactory.apply(block);
            GameRegistry.register(itemBlock.setRegistryName(block.getRegistryName()));
            itemBlock.setUnlocalizedName(block.getUnlocalizedName());
        }
        return block;
    }
}
