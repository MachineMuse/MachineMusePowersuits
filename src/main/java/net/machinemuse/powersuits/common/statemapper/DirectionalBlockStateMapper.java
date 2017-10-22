package net.machinemuse.powersuits.common.statemapper;

import com.google.common.collect.Maps;
import net.machinemuse.powersuits.client.models.ModelLuxCapacitor;
import net.machinemuse.powersuits.common.blocks.BlockLuxCapacitor;
import net.minecraft.block.Block;
import net.minecraft.block.state.IBlockState;
import net.minecraft.client.renderer.block.statemap.DefaultStateMapper;
import net.minecraft.util.EnumFacing;

import java.util.LinkedHashMap;
import java.util.Map;

public class DirectionalBlockStateMapper extends DefaultStateMapper {
    private static DirectionalBlockStateMapper INSTANCE;

    public static DirectionalBlockStateMapper getInstance() {
        if (INSTANCE == null) {
            synchronized (DirectionalBlockStateMapper.class) {
                if (INSTANCE == null) {
                    INSTANCE = new DirectionalBlockStateMapper();
                }
            }
        }
        return INSTANCE;
    }

    private DirectionalBlockStateMapper() {
    }

    public Map putStateModelLocations(Block block) {
        IBlockState state;
        LinkedHashMap modelLocations = Maps.newLinkedHashMap();

        if (block instanceof BlockLuxCapacitor) {
            for (EnumFacing facing : EnumFacing.HORIZONTALS) {
                modelLocations.put(BlockLuxCapacitor.getInstance().getStateFromMeta(facing.getIndex()), ModelLuxCapacitor.getModelResourceLocation(facing));
            }







//            for (int i = 0; i < EnumFacing.values().length; i++) {
////                state = BlockLuxCapacitor.getInstance().getStateFromMeta(i);
//                modelLocations.put(BlockLuxCapacitor.getInstance().getStateFromMeta(i), ModelLuxCapacitor.modelResourceLocation.);
//            }








//            Block.REGISTRY.getNameForObject(block);



//            protected ModelResourceLocation getModelResourceLocation(IBlockState state)
//            {
//                return new ModelResourceLocation(Block.REGISTRY.getNameForObject(state.getBlock()), this.getPropertyString(state.getProperties()));
//            }




        }



        return modelLocations;
    }
}
