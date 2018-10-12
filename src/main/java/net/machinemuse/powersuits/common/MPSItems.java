package net.machinemuse.powersuits.common;

import net.machinemuse.powersuits.block.BlockLuxCapacitor;
import net.machinemuse.powersuits.block.BlockTinkerTable;
import net.machinemuse.powersuits.block.itemblock.ItemBlockLuxCapacitor;
import net.machinemuse.powersuits.block.itemblock.ItemBlockTinkerTable;
import net.machinemuse.powersuits.fluid.BlockFluidLiquidNitrogen;
import net.machinemuse.powersuits.fluid.LiquidNitrogen;
import net.machinemuse.powersuits.item.ItemComponent;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorBoots;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorChestplate;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorHelmet;
import net.machinemuse.powersuits.item.armor.ItemPowerArmorLeggings;
import net.machinemuse.powersuits.item.tool.ItemPowerFist;
import net.minecraft.block.Block;
import net.minecraft.item.Item;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fluids.FluidRegistry;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

import static net.machinemuse.powersuits.common.ModularPowersuits.MODID;

/**
 * Created by Claire Semple on 9/9/2014.
 *
 * Ported to Java by lehjr on 10/22/16.
 */
@Mod.EventBusSubscriber(modid = MODID)
public enum MPSItems {
    INSTANCE;

    // Armor --------------------------------------------------------------------------------------
    public static Item powerArmorHead = initItem(new ItemPowerArmorHelmet(), "powerarmor_head", "powerArmorHelmet");
    public static Item powerArmorTorso = initItem(new ItemPowerArmorChestplate(), "powerarmor_torso", "powerArmorChestplate");
    public static Item powerArmorLegs = initItem(new ItemPowerArmorLeggings(), "powerarmor_legs", "powerArmorLeggings");
    public static Item powerArmorFeet = initItem(new ItemPowerArmorBoots(), "powerarmor_feet", "powerArmorBoots");
    // HandHeld -----------------------------------------------------------------------------------
    public static Item powerFist = initItem(new ItemPowerFist(), "power_fist", "powerFist");

    // Components ---------------------------------------------------------------------------------
    public static Item components = ItemComponent.getInstance();

    // Blocks -------------------------------------------------------------------------------------
    public static BlockTinkerTable tinkerTable = new BlockTinkerTable();
    public static ItemBlockTinkerTable itemTinkerTable = new ItemBlockTinkerTable();

    public static BlockLuxCapacitor luxCapacitor = new BlockLuxCapacitor();
    public static ItemBlockLuxCapacitor itemLuxCapacitor = new ItemBlockLuxCapacitor();

    // Fluid --------------------------------------------------------------------------------------
    public static LiquidNitrogen liquidNitrogen = new LiquidNitrogen();
    public static BlockFluidLiquidNitrogen blockLiquidNitrogen = new BlockFluidLiquidNitrogen();



    @SubscribeEvent
    public static void regigisterItems(RegistryEvent.Register<Item> event) {
        event.getRegistry().registerAll(
                powerArmorHead ,
                powerArmorTorso,
                powerArmorLegs,
                powerArmorFeet,
                powerFist,
                components,


                itemTinkerTable,
                itemLuxCapacitor
                );


        ((ItemComponent)components).populate();
        ((ItemComponent)components).registerOres();
    }

    private static Item initItem(Item item, String regName, String unlocalizedName) {
        // including the ModID in the unlocalized name helps keep the names unique so they can be used as keys for maps
        item.setUnlocalizedName(new StringBuilder(MODID).append(".").append(unlocalizedName).toString());
        item.setRegistryName(new ResourceLocation(MODID, regName));
        return item;
    }

    @SubscribeEvent
    public static void initBlocks(RegistryEvent.Register<Block> event) {
        event.getRegistry().register(tinkerTable);
        event.getRegistry().register(luxCapacitor);
        event.getRegistry().register(blockLiquidNitrogen);
    }


    public static void initFluids() {
//        FluidRegistry.enableUniversalBucket();
        FluidRegistry.registerFluid(liquidNitrogen);
        FluidRegistry.addBucketForFluid(liquidNitrogen);
    }
 }
