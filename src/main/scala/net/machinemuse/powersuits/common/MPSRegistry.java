package net.machinemuse.powersuits.common;

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
        return new ResourceLocation(ModularPowersuits.MODID().toLowerCase(), name);
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
        item.setUnlocalizedName(ModularPowersuits.MODID().toLowerCase() + "." + name);
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
        }
        return block;
    }

//---------------------
/*TConstruct */









    /*







    protected static <T extends Block> T registerBlock(T block, String name) {
        ItemBlock itemBlock = new ItemBlockMeta(block);
        registerBlock(block, itemBlock, name);
        return block;
    }

    protected static <T extends EnumBlock<?>> T registerEnumBlock(T block, String name) {
        registerBlock(block, new ItemBlockMeta(block), name);
        ItemBlockMeta.setMappingProperty(block, block.prop);
        return block;
    }

    protected static <T extends EnumBlockSlab<?>> T registerEnumBlockSlab(T block, String name) {
        registerBlock(block, new ItemBlockSlab(block), name);
        ItemBlockMeta.setMappingProperty(block, block.prop);
        return block;
    }

    protected static <E extends Enum<E> & EnumBlock.IEnumMeta & IStringSerializable> BlockStairsBase registerBlockStairsFrom(EnumBlock<E> block, E value, String name) {
        return registerBlock(new BlockStairsBase(block.getDefaultState().withProperty(block.prop, value)), name);
    }

    protected static <T extends Block> T registerBlock(ItemBlock itemBlock, String name) {
        Block block = itemBlock.getBlock();
        return (T) registerBlock(block, itemBlock, name);
    }

    protected static <T extends Block> T registerBlock(T block, String name, IProperty<?> property) {
        ItemBlockMeta itemBlock = new ItemBlockMeta(block);
        registerBlock(block, itemBlock, name);
        ItemBlockMeta.setMappingProperty(block, property);
        return block;
    }

    protected static <T extends Block> T registerBlock(T block, ItemBlock itemBlock, String name) {
        if(!name.equals(name.toLowerCase(Locale.US))) {
            throw new IllegalArgumentException(String.format("Unlocalized names need to be all lowercase! Block: %s", name));
        }

        String prefixedName = Util.prefix(name);
        block.setUnlocalizedName(prefixedName);
        itemBlock.setUnlocalizedName(prefixedName);

        register(block, name);
        register(itemBlock, name);
        return block;
    }

    protected static <T extends Block> T registerBlockNoItem(T block, String name) {
        if(!name.equals(name.toLowerCase(Locale.US))) {
            throw new IllegalArgumentException(String.format("Unlocalized names need to be all lowercase! Block: %s", name));
        }

        String prefixedName = Util.prefix(name);
        block.setUnlocalizedName(prefixedName);

        register(block, name);
        return block;
    }

    protected static <T extends IForgeRegistryEntry<?>> T register(T thing, String name) {
        thing.setRegistryName(Util.getResource(name));
        GameRegistry.register(thing);
        return thing;
    }

    protected static void registerTE(Class<? extends TileEntity> teClazz, String name) {
        if(!name.equals(name.toLowerCase(Locale.US))) {
            throw new IllegalArgumentException(String.format("Unlocalized names need to be all lowercase! TE: %s", name));
        }

        GameRegistry.registerTileEntity(teClazz, Util.prefix(name));
    }

    // sets the stack size to make Tinkers Commons easier, as it uses base itemstacks there
    protected static void addSlabRecipe(ItemStack slab, ItemStack input) {
        GameRegistry.addShapedRecipe(new ItemStack(slab.getItem(), 6, slab.getItemDamage()), "BBB", 'B', input);
    }

    protected static void addStairRecipe(Block stairs, ItemStack input) {
        GameRegistry.addShapedRecipe(new ItemStack(stairs, 4, 0), "B  ", "BB ", "BBB", 'B', input);
    }

    protected static void addBrickRecipe(Block block, EnumBlock.IEnumMeta out, EnumBlock.IEnumMeta in) {
        ItemStack brickBlockIn = new ItemStack(block, 1, in.getMeta());
        ItemStack brickBlockOut = new ItemStack(block, 1, out.getMeta());

        // todo: convert to chisel recipes if chisel is present
        //GameRegistry.addShapedRecipe(searedBrickBlockOut, "BB", "BB", 'B', searedBrickBlockIn);
        GameRegistry.addShapelessRecipe(brickBlockOut, brickBlockIn);
    }




    */
}
