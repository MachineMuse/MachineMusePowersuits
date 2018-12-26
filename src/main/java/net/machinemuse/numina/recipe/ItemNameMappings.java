//package net.machinemuse.numina.recipe;
//
//import net.minecraft.block.Block;
//import net.minecraft.item.Item;
//import net.minecraft.item.ItemStack;
//
//import javax.annotation.Nonnull;
//import java.util.HashMap;
//import java.util.Map;
//
///**
// * Author: MachineMuse (Claire Semple)
// * Created: 5:06 PM, 11/4/13
// */
//public class ItemNameMappings {
//    private static Map<String, ItemStack> itemMap;
//
//    private static Map<String, ItemStack> getItemMap() {
//        if (itemMap == null) {
//            itemMap = new HashMap<>();
//            for (Object obj : Block.REGISTRY) {
//                Block block = (Block)obj;
//                if (block != null && block.getUnlocalizedName() != null) {
//                    itemMap.put(block.getUnlocalizedName(), new ItemStack(block));
//                }
//            }
//            for (Object obj : Item.REGISTRY) {
//                Item item = (Item)obj;
//                if (item != null && item.getUnlocalizedName() != null) {
//                    itemMap.put(item.getUnlocalizedName(), new ItemStack(item));
//                }
//            }
//        }
//        return itemMap;
//    }
//
//    @Nonnull
//    public static ItemStack getItem(String id) {
//        if (getItemMap().containsKey(id))
//            return getItemMap().get(id);
//        else
//            return ItemStack.EMPTY;
//    }
//}