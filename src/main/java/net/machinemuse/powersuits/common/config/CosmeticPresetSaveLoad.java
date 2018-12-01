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
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.Collections;
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
        return loadPresetsForItem(itemStack.getItem(), 0);
    }

    public static Map<String, NBTTagCompound> loadPresetsForItem(Item item, int count) {
        Map<String, NBTTagCompound> retmap = new HashMap<>();
        if (item == null || count > 4) {
            return retmap;
        }

        // sub folder based on the item name
        String subfolder = item.getRegistryName().getPath();

        // path with subfolder
        Path directory = Paths.get(MPSConfig.getConfigFolder().getAbsolutePath(), "cosmeticpresets", subfolder);
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

        if (retmap.isEmpty()) {
            copyPresetsFromJar();
            return loadPresetsForItem(item, count + 1);
        }

        return retmap;
    }

    public static void copyPresetsFromJar() {
        Path sourcePath;
        FileSystem fileSystem = null;

        try {
            URI uri = CosmeticPresetSaveLoad.class.getResource("/assets/powersuits/cosmeticpresets/").toURI();
            if ("jar".equals(uri.getScheme())) {
                // We're running within a jar file
                if (fileSystem == null) {
                    try {
                        // Create a reference to the filesystem
                        fileSystem = FileSystems.newFileSystem(uri, Collections.emptyMap());
                    } catch (FileSystemAlreadyExistsException lEx) {
                        // Sometimes newFileSystem seems to raise FileSystemAlreadyExistsException - this code gets around this problem.
                        fileSystem = FileSystems.getFileSystem(uri);
                    }
                }
                // Get hold of the path to the top level directory of the JAR file
                sourcePath = fileSystem.getPath("/");
                sourcePath = sourcePath.resolve("assets/powersuits/cosmeticpresets");

            } else
                sourcePath = Paths.get(uri);


            Files.walkFileTree(sourcePath, EnumSet.noneOf(FileVisitOption.class), 10, new SimpleFileVisitor<Path>() {
                @Override
                public FileVisitResult visitFile(Path selectedPath, BasicFileAttributes attrs) throws IOException {
                    if (selectedPath.getFileName().toString().endsWith("." + EXTENSION)) {
                        Path subFolder = selectedPath.getParent().getFileName();

                        // path with subfolder
                        Path target = Paths.get(MPSConfig.getConfigFolder().getAbsolutePath(), "cosmeticpresets", subFolder.toString(), selectedPath.getFileName().toString());
                        try {
                            // create dir
                            if (!Files.exists(target.getParent()))
                                Files.createDirectories(target.getParent());

                            // create file without overwriting
                            if (!Files.exists(target))
                                Files.copy(selectedPath, target);//, StandardCopyOption.REPLACE_EXISTING);
                        } catch(Exception e) {
                            // FIXME
                            MuseLogger.logException("Exception here: ", e);
                        }
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (Exception e) {
            MuseLogger.logException("Something happened here: ", e);
        }
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
            Path directory = Paths.get(MPSConfig.getConfigFolder().getAbsolutePath(), "cosmeticpresets", subfolder);

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