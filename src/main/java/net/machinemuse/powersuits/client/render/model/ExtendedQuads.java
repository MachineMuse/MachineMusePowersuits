package net.machinemuse.powersuits.client.render.model;

import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.renderer.vertex.VertexFormat;
import net.minecraft.util.EnumFacing;

/**
 * Modular Powersuits by MachineMuse
 * Created by lehjr on 2/14/17.
 */
public class ExtendedQuads extends BakedQuad {
    protected final boolean glow;
    protected final boolean visible;
    protected final String partName;

    /*
     * Extended version of baked quads that we can use to help make parts of models glow
     */
    public ExtendedQuads(BakedQuad quad,
                         boolean glowIn,
                         boolean visibleIn,
                         String partNameIn) {
        super(quad.getVertexData(),
                quad.getTintIndex(),
                quad.getFace(),
                quad.getSprite(),
                quad.shouldApplyDiffuseLighting(),
                quad.getFormat());
        this.glow = glowIn;
        this.visible = visibleIn;
        this.partName = partNameIn;
    }

    public boolean getGlow() {
        return glow;
    }

    public boolean getVisible () {
        return this.visible;
    }

    public String getPartName() {
        return partName;
    }




}
