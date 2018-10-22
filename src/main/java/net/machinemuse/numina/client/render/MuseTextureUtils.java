package net.machinemuse.numina.client.render;

import net.minecraft.client.Minecraft;
import net.minecraft.util.ResourceLocation;

import java.util.Stack;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 2:38 PM, 9/6/13
 * <p>
 * Ported to Java by lehjr on 10/25/16.
 */
public final class MuseTextureUtils {
    public static final String TEXTURE_QUILT = "textures/atlas/blocks.png";
    private static final Stack<String> texturestack = new Stack<>();
    private static String TEXTURE_MAP = "textures/atlas/blocks.png";

    static {
        new MuseTextureUtils();
    }

    private MuseTextureUtils() {
    }

    public static void pushTexture(final String filename) {
        texturestack.push(TEXTURE_MAP);
        TEXTURE_MAP = filename;
        bindTexture(TEXTURE_MAP);
    }

    public static void popTexture() {
        TEXTURE_MAP = texturestack.pop();
        bindTexture(TEXTURE_MAP);
    }

    public static void bindTexture(final String tex) {
        Minecraft.getMinecraft().renderEngine.bindTexture(new ResourceLocation(tex));
    }
}