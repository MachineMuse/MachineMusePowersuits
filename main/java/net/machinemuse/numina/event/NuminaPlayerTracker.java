package net.machinemuse.numina.event;

//import net.machinemuse.numina.network.MusePacketRecipeUpdate;
//import net.machinemuse.numina.network.PacketSender;
//import net.machinemuse.numina.recipe.JSONRecipe;
//import net.machinemuse.numina.recipe.JSONRecipeList;
//import net.minecraft.entity.player.EntityPlayerMP;
//import net.minecraftforge.fml.common.FMLCommonHandler;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.PlayerEvent;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 11:57 AM, 9/3/13
 *
 * Ported to Java by lehjr on 10/26/16.
 */
public final class NuminaPlayerTracker {
    @SubscribeEvent
    public void onPlayerLogin(PlayerEvent.PlayerLoggedInEvent event) {
//        boolean isUsingBuiltInServer = FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer();
//
//        // dedidated server or multiplayer game
//        if (!isUsingBuiltInServer ||
//                (isUsingBuiltInServer && FMLCommonHandler.instance().getMinecraftServerInstance().getCurrentPlayerCount() > 1)) {
//
//            for (JSONRecipe recipe : JSONRecipeList.getJSONRecipesList()) {
//                JSONRecipe[] recipeArray = new JSONRecipe[]{recipe};
//                String recipeAsString= JSONRecipeList.gson.toJson(recipeArray);
//                PacketSender.sendTo(new MusePacketRecipeUpdate(event.player, recipeAsString), (EntityPlayerMP)event.player);
//            }
//        }
    }
}