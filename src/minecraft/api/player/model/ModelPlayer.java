package api.player.model;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelBiped;

@SideOnly(Side.CLIENT)
public class ModelPlayer extends ModelBiped {
    public ModelPlayer(float var1) {
        super(var1);
    }
}