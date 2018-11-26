package net.machinemuse.powersuits.common.config;

import net.machinemuse.numina.utils.MuseLogger;
import net.machinemuse.powersuits.utils.nbt.MPSNBTUtils;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.CompressedStreamTools;
import net.minecraft.nbt.NBTTagCompound;

import javax.annotation.Nonnull;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;
import java.util.zip.GZIPOutputStream;


/**
 * Saves and loads cosmetic presets to/from NBT files.
 */
public class CosmeticPresetSaveLoad {
    static final String EXTENSION = "dat";

    public static Map<String, NBTTagCompound> loadPresetsForItem(@Nonnull ItemStack itemStack) {
        return loadPresetsForItem(itemStack.getItem());
    }

    public static Map<String, NBTTagCompound> loadPresetsForItem(Item item) {
        Map<String, NBTTagCompound> retmap = new HashMap<>();
        if (item == null) {
            return retmap;
        }

        // sub folder based on the item name
        String subfolder = item.getRegistryName().getPath();

        // path with subfolder
        Path directory = Paths.get(MPSConfig.INSTANCE.getConfigFolder().getAbsolutePath(), "cosmeticpresets", subfolder);


        try {
            Files.walkFileTree(directory, EnumSet.noneOf(FileVisitOption.class), 1, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path selectedPath, BasicFileAttributes attrs) throws IOException {
                    if (selectedPath.getFileName().toString().endsWith("." + EXTENSION)) {
                        String name = selectedPath.getFileName().toString().replaceFirst("[.][^.]+$", "");
                        NBTTagCompound nbt = CompressedStreamTools.readCompressed(Files.newInputStream(selectedPath));

                        if (nbt != null && name != null && !name.isEmpty()) {
                            retmap.put(name, nbt);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
        return retmap;
    }

    /**
     * "adapted" from 1.7.10
     */
    public static byte[] compressGZip(NBTTagCompound nbt) {
        ByteArrayOutputStream bytearrayoutputstream = new ByteArrayOutputStream();
        try {
            DataOutputStream dataoutputstream = new DataOutputStream(new GZIPOutputStream(bytearrayoutputstream));
            CompressedStreamTools.write(nbt, dataoutputstream);

            // bytearrayoutputstream only updates if dataoutputstream closes
            dataoutputstream.close();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {

        }
        return bytearrayoutputstream.toByteArray();
    }

    /**
     * Save the model settings as a Json in the config folder using the itemstack name as the folder name
     */
    public static boolean savePreset(String presetName, @Nonnull ItemStack itemStack) {
        if (itemStack.isEmpty())
            return false;

        // get the render tag for the item
        NBTTagCompound nbt = MPSNBTUtils.getMuseRenderTag(itemStack).copy();

        // byte array
        byte [] byteArray = compressGZip(nbt);

        try {
            // sub folder based on the item name
            String subfolder = itemStack.getItem().getRegistryName().getPath();

            // path with subfolder
            Path directory = Paths.get(MPSConfig.INSTANCE.getConfigFolder().getAbsolutePath(), "cosmeticpresets", subfolder);

            try {
                Files.createDirectories(directory);
            } catch(Exception e) {
                // FIXME
                MuseLogger.logException("Exception here: ", e); // debugging during development
            }

            // final complete path
            Path fullPath = Paths.get(directory.toString(), presetName + "." + EXTENSION);
            Files.write(fullPath, byteArray);
        } catch(Exception e) {
            MuseLogger.logException("Failed to saveButton preset: ", e);
            return false;
        }
        return true;
    }
}