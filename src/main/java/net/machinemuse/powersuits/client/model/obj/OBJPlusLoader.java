/*
 * Minecraft Forge
 * Copyright (c) 2016.
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation version 2.1
 * of the License.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 51 Franklin Street, Fifth Floor, Boston, MA  02110-1301  USA
 */

package net.machinemuse.powersuits.client.model.obj;

import net.machinemuse.numina.utils.MuseLogger;
import net.minecraft.client.resources.IResource;
import net.minecraft.client.resources.IResourceManager;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.model.ICustomModelLoader;
import net.minecraftforge.client.model.IModel;
import net.minecraftforge.client.model.ModelLoaderRegistry;
import net.minecraftforge.fml.common.FMLLog;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/*
 * Loader for OBJ models.
 * To enable your mod call instance.addDomain(modid).
 * If you need more control over accepted resources - extend the class, and register a new instance with ModelLoaderRegistry.
 *
 * Slightly modified version of Forge's loader because You cannot extend an Enum ^
 */
public enum OBJPlusLoader implements ICustomModelLoader {
    INSTANCE;

    private final Set<String> enabledDomains = new HashSet<>();
    private final Map<ResourceLocation, OBJModelPlus> cache = new HashMap<>();
    private final Map<ResourceLocation, Exception> errors = new HashMap<>();
    private IResourceManager manager;

    public void addDomain(String domain) {
        enabledDomains.add(domain.toLowerCase());
        FMLLog.log.info("OBJPlusLoader: Domain {} has been added.", domain.toLowerCase());
    }

    @Override
    public void onResourceManagerReload(IResourceManager resourceManager) {
        this.manager = resourceManager;
        cache.clear();
        errors.clear();
    }

    @Override
    public boolean accepts(ResourceLocation modelLocation) {
        return enabledDomains.contains(modelLocation.getResourceDomain()) && modelLocation.getResourcePath().endsWith(".obj");
    }

    @Override
    public IModel loadModel(ResourceLocation modelLocation) throws Exception {
        ResourceLocation file = new ResourceLocation(modelLocation.getResourceDomain(), modelLocation.getResourcePath());
        if (!cache.containsKey(file)) {
            IResource resource;
            try {
                resource = manager.getResource(file);
            } catch (FileNotFoundException e) {
                MuseLogger.logException("failed to load model: ", e);
                if (modelLocation.getResourcePath().startsWith("models/block/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/item/" + file.getResourcePath().substring("models/block/".length())));
                else if (modelLocation.getResourcePath().startsWith("models/item/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath().substring("models/item/".length())));
                else if (modelLocation.getResourcePath().startsWith("models/models/item/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/item/" + file.getResourcePath().substring("models/models/item/".length())));
                else if (modelLocation.getResourcePath().startsWith("models/models/block/"))
                    resource = manager.getResource(new ResourceLocation(file.getResourceDomain(), "models/block/" + file.getResourcePath().substring("models/models/block/".length())));
                else throw e;
            }
            OBJModelPlus.Parser parser = new OBJModelPlus.Parser(resource, manager);
            OBJModelPlus model = null;
            try {
                model = parser.parse();
            } catch (Exception e) {
                MuseLogger.logException("failed to parse model: ", e);

                errors.put(modelLocation, e);
            } finally {
                cache.put(modelLocation, model);
            }
        }
        OBJModelPlus model = cache.get(file);
        if (model == null)
            throw new ModelLoaderRegistry.LoaderException("Error loading model previously: " + file, errors.get(modelLocation));
        return model;
    }
}