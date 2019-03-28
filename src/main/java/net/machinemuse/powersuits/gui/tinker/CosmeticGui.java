package net.machinemuse.powersuits.gui.tinker;

import net.machinemuse.numina.math.Colour;
import net.machinemuse.numina.math.geometry.MusePoint2D;
import net.machinemuse.numina.math.geometry.MuseRect;
import net.machinemuse.powersuits.common.config.MPSConfig;
import net.machinemuse.powersuits.gui.MuseGui;
import net.machinemuse.powersuits.gui.tinker.frame.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.ScaledResolution;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.server.management.UserListOpsEntry;
import net.minecraftforge.fml.common.FMLCommonHandler;

import java.io.IOException;

/**
 * Author: MachineMuse (Claire Semple)
 * Created: 6:32 PM, 29/04/13
 * <p>
 * Ported to Java by lehjr on 10/19/16.
 */
public class CosmeticGui extends MuseGui {
    EntityPlayer player;
    int worldx;
    int worldy;
    int worldz;
    ItemSelectionFrame itemSelect;
    LoadSaveResetSubFrame loadSaveResetSubFrame;

    protected final boolean allowCosmeticPresetCreation;
    protected final boolean usingCosmeticPresets;

    public CosmeticGui(EntityPlayer player, int worldx, int worldy, int worldz) {
        this.player = player;
        this.worldx = worldx;
        this.worldy = worldy;
        this.worldz = worldz;
        ScaledResolution screen = new ScaledResolution(Minecraft.getMinecraft());
        this.xSize = Math.min(screen.getScaledWidth() - 50, 500);
        this.ySize = Math.min(screen.getScaledHeight() - 50, 300);

        usingCosmeticPresets = !MPSConfig.INSTANCE.useLegacyCosmeticSystem();
        if (usingCosmeticPresets) {
            // check if player is the server owner
            if (FMLCommonHandler.instance().getMinecraftServerInstance().isSinglePlayer()) {
                allowCosmeticPresetCreation = player.getName().equals(FMLCommonHandler.instance().getMinecraftServerInstance().getServerOwner());
            } else {
                // check if player is top level op
                UserListOpsEntry opEntry =  FMLCommonHandler.instance().getMinecraftServerInstance().getPlayerList().getOppedPlayers().getEntry(player.getGameProfile());
                int opLevel = opEntry != null ? opEntry.getPermissionLevel() : 0;
                allowCosmeticPresetCreation = opLevel == 4;
            }
        } else allowCosmeticPresetCreation = false;
    }

    /**
     * Add the buttons (and other controls) to the screen.
     */
    @Override
    public void initGui() {
        super.initGui();
        itemSelect = new ItemSelectionFrame(
                new MusePoint2D(absX(-0.95F), absY(-0.95F)),
                new MusePoint2D(absX(-0.78F), absY(-0.025F)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F), player);
        frames.add(itemSelect);

        PlayerModelViewFrame renderframe = new PlayerModelViewFrame(
                itemSelect,
                new MusePoint2D(absX(-0.75F), absY(-0.95f)),
                new MusePoint2D(absX(0.15F), absY(-0.025f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F));
        frames.add(renderframe);

        ColourPickerFrame colourpicker = new ColourPickerFrame(
                new MusePoint2D(absX(0.18f),
                        absY(-0.95f)),

                new MusePoint2D(absX(0.95f),
                        absY(-0.27f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F),
                itemSelect);
        frames.add(colourpicker);

        PartManipContainer partframe = new PartManipContainer(
                itemSelect, colourpicker,
                new MusePoint2D(absX(-0.95F), absY(0.025f)),
                new MusePoint2D(absX(+0.95F), absY(0.95f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F));
        frames.add(partframe);

        CosmeticPresetContainer cosmeticFrame = new CosmeticPresetContainer(
                itemSelect, colourpicker,
                new MusePoint2D(absX(-0.95F), absY(0.025f)),
                new MusePoint2D(absX(+0.95F), absY(0.95f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F));
        frames.add(cosmeticFrame);

        // if not using presets then only the reset button is displayed
        loadSaveResetSubFrame = new LoadSaveResetSubFrame(
                colourpicker,
                player,
                new MuseRect(
                        absX(0.18f),
                        absY(-0.23f),
                        absX(0.95f),
                        absY(-0.025f)),
                Colour.LIGHTBLUE.withAlpha(0.8F),
                Colour.DARKBLUE.withAlpha(0.8F),
                itemSelect,
                usingCosmeticPresets,
                allowCosmeticPresetCreation,
                partframe,
                cosmeticFrame);
        frames.add(loadSaveResetSubFrame);

        TabSelectFrame tabFrame = new TabSelectFrame(
                player,
                new MusePoint2D(absX(-0.95F), absY(-1.05f)),
                new MusePoint2D(absX(0.95F), absY(-0.95f)),
                worldx, worldy, worldz);
        frames.add(tabFrame);
    }

    @Override
    public void onGuiClosed() {
        super.onGuiClosed();
        loadSaveResetSubFrame.onGuiClosed();
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        super.keyTyped(typedChar, keyCode);
        if (loadSaveResetSubFrame != null)
            loadSaveResetSubFrame.keyTyped(typedChar, keyCode);
    }

    @Override
    public void update() {
        super.update();
    }

    @Override
    public void drawScreen(int x, int y, float z) {
        super.drawScreen(x, y, z);
    }
}