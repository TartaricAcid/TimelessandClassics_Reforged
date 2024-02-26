package com.tac.guns.client.resource.internal;

import com.tac.guns.Reference;
import com.tac.guns.client.resource.model.VanillaBakedModel;
import net.minecraft.client.Minecraft;
import net.minecraft.client.resources.model.BakedModel;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.packs.resources.ReloadableResourceManager;
import net.minecraft.server.packs.resources.ResourceManager;
import net.minecraft.server.packs.resources.ResourceManagerReloadListener;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.event.ModelEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.MOD)
public enum MyBakedModels {
    FLAME("flame"),
    //Everything from this point on is all scope additions

    MINI_DOT_BASE("optics/mini_dot_base"),
    MICRO_HOLO_BASE("optics/micro_holo_base"),
    LPVO_1_6_FRONT("optics/lpvo_1_6_front"),
    LPVO_1_6("optics/lpvo_1_6"),
    Sx8_FRONT("optics/8x_scope_front"),
    Sx8_BODY("optics/8x_scope"),
    ACOG_SCOPE_MIRROR("scope_mirror/acog_4x_scope_mirror"),
    BULLET_SHELL_HIGH_CAL("shell_huge"),
    BULLET_SHELL_RIFLE("shell_large"),
    BULLET_SHELL_SHOTGUN("shell_shotgun"),
    BULLET_SHELL_PISTOL("shell_small"),
    BULLET_SHELL_PISTOL_SILVER("shell_silver"),
    BULLET_SHELL_RIFLE_SURPLUS("shell_steel");

    private final ResourceLocation modelLocation;

    private final VanillaBakedModel cacheableModel;

    /**
     * Sets the model's location
     *
     * @param modelName name of the model file
     */
    MyBakedModels(String modelName) {
        this(new ResourceLocation(Reference.MOD_ID, "special/" + modelName));
    }

    /**
     * Sets the model's location
     *
     * @param resource name of the model file
     */
    MyBakedModels(ResourceLocation resource) {
        this.modelLocation = resource;
        cacheableModel = new VanillaBakedModel(resource);
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void init(ModelEvent.RegisterAdditional event) {
        for (MyBakedModels model : values()) {
            event.register(model.modelLocation);
        }

        ResourceManager manager = Minecraft.getInstance().getResourceManager();
        ((ReloadableResourceManager) manager).registerReloadListener((ResourceManagerReloadListener) resourceManager -> MyBakedModels.cleanCache());
    }

    public static void cleanCache() {
        for (MyBakedModels model : values()) {
            model.cacheableModel.cleanCache();
        }
    }

    /**
     * Gets the model
     *
     * @return isolated model
     */
    @OnlyIn(Dist.CLIENT)
    public BakedModel getModel() {
        return cacheableModel.getModel();
    }
}
