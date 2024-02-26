package com.tac.guns.client.resource.animation;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.tac.guns.client.resource.animation.gltf.RawAnimationStructure;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class AnimationAssetLoader {
    public static RawAnimationStructure loadRawAnimationStructure(ResourceLocation resourceLocation) throws IOException {
        InputStream inputStream = Minecraft.getInstance().getResourceManager().open(resourceLocation);
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        JsonObject json = JsonParser.parseReader(bufferedReader).getAsJsonObject();
        Gson gson = new Gson();
        return gson.fromJson(json, RawAnimationStructure.class);
    }
}
