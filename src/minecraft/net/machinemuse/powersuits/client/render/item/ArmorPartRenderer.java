package net.machinemuse.powersuits.client.render.item;

import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import net.minecraft.client.model.ModelRenderer;
import net.minecraftforge.client.model.obj.WavefrontObject;

public class ArmorPartRenderer extends ModelRenderer {
    protected String[] normalparts;
    protected String[] glowyparts;
    protected ArmorModel modelBase;

    public ArmorPartRenderer(ArmorModel modelBase, WavefrontObject model, String normalparts, String glowyparts) {
        super(modelBase);
        this.modelBase = modelBase;
        this.normalparts = normalparts.split(";");
        this.glowyparts = glowyparts.split(";");
    }

    public ArmorPartRenderer setInitialOffsets(float x, float y, float z) {
        this.field_82906_o = x;
        this.field_82907_q = y;
        this.field_82908_p = z;
        return this;
    }

    @SideOnly(Side.CLIENT)
    public void render(float scale, WavefrontObject model) {
    }

}
