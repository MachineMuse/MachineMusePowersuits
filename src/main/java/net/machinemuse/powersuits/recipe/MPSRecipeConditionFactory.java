package net.machinemuse.powersuits.recipe;

import com.google.gson.JsonObject;
import net.machinemuse.numina.recipe.NuminaShapedRecipe;
import net.machinemuse.powersuits.common.ModCompatibility;
import net.minecraft.util.JsonUtils;
import net.minecraftforge.common.crafting.IConditionFactory;
import net.minecraftforge.common.crafting.JsonContext;

import java.util.function.BooleanSupplier;

@SuppressWarnings("unused")
public class MPSRecipeConditionFactory implements IConditionFactory {
    @Override
    public BooleanSupplier parse(JsonContext context, JsonObject json) {
        if(JsonUtils.hasField(json, "type")) {
            String key = JsonUtils.getString(json, "type");
            switch (key) {
                // Thermal Expansion
                case "powersuits:thermal_expansion_recipes_enabled":
                    return () -> ModCompatibility.isThermalExpansionLoaded();

                // EnderIO
                case "powersuits:enderio_recipes_enabled":
                    return () -> ModCompatibility.isEnderIOLoaded();

                // Original recipe loading code set priority for TechReborn recipes instead of Gregtech or Industrialcraft
                // Tech Reborn
                case "powersuits:tech_reborn_recipes_enabled":
                    return () -> ModCompatibility.isTechRebornLoaded();

                    // FIXME: Gregtech recipe loading is broken due to ores being registered during recipe loading.
//                // GregTech
//                case "powersuits:gregtech_recipes_enabled":
//                    return () -> (ModCompatibility.isGregTechLoaded() && !ModCompatibility.isTechRebornLoaded());

                // IC2
                case "powersuits:ic2_recipes_enabled":
                    return () -> (ModCompatibility.isIndustrialCraftExpLoaded() &&
                            (/*!ModCompatibility.isGregTechLoaded() &&*/ !ModCompatibility.isTechRebornLoaded()));
                // IC2 Classic
                case "powersuits:ic2_classic_recipes_enabled":
                    return () -> (ModCompatibility.isIndustrialCraftClassicLoaded() &&
                            (/*!ModCompatibility.isGregTechLoaded() && */!ModCompatibility.isTechRebornLoaded()));

                // Vanilla reciples only as fallback
                case "powersuits:vanilla_recipes_enabled":
                    return () -> (!(ModCompatibility.isEnderIOLoaded() ||
                            /*ModCompatibility.isGregTechLoaded() || */
                            ModCompatibility.isIndustrialCraftLoaded() ||
                            ModCompatibility.isTechRebornLoaded() ||
                            ModCompatibility.isThermalExpansionLoaded()));
            }
        }
        return () -> false;
    }
}
