package com.tac.guns.client.handler.command;

import com.google.gson.GsonBuilder;
import com.tac.guns.Config;
import com.tac.guns.Reference;
import com.tac.guns.client.Keys;
import com.tac.guns.client.handler.command.data.ScopeData;
import com.tac.guns.common.Gun;
import com.tac.guns.common.tooling.CommandsHandler;
import com.tac.guns.item.attachment.impl.Scope;
import com.tac.guns.item.transition.TimelessGunItem;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.InputEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import org.apache.logging.log4j.Level;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;

import static com.tac.guns.GunMod.LOGGER;

@Mod.EventBusSubscriber(modid = Reference.MOD_ID, value = Dist.CLIENT)
public class ScopeEditor {
    private static ScopeEditor instance;
    public HashMap<String, ScopeData> map = new HashMap<>();
    private ScopeData scopeData;

    private ScopeEditor() {
    }

    public static ScopeEditor get() {
        if (instance == null) {
            instance = new ScopeEditor();
        }
        return instance;
    }

    public ScopeData getScopeData() {
        return scopeData;
    }

    @SubscribeEvent
    public void onKeyPressed(InputEvent.Key event) {
        if (!Config.COMMON.development.enableTDev.get())
            return;
        Minecraft mc = Minecraft.getInstance();
        if (mc.player == null)
            return;
        CommandsHandler ch = CommandsHandler.get();
        if (ch == null || ch.getCatCurrentIndex() != 2)
            return;
        if ((mc.player.getMainHandItem() == null || mc.player.getMainHandItem() == ItemStack.EMPTY || !(mc.player.getMainHandItem().getItem() instanceof TimelessGunItem)))
            return;
        if (((TimelessGunItem) mc.player.getMainHandItem().getItem()).isIntegratedOptic()) {
            if (!this.map.containsKey(mc.player.getMainHandItem().getItem().getDescriptionId()))
                this.map.put(mc.player.getMainHandItem().getItem().getDescriptionId(), new ScopeData(mc.player.getMainHandItem().getItem().getDescriptionId()));
            this.handleScopeMod(event, this.map.get(mc.player.getMainHandItem().getItem().getDescriptionId()));
            this.scopeData = this.map.get(mc.player.getMainHandItem().getItem().getDescriptionId());
        } else {
            Scope scopeItem = Gun.getScope(mc.player.getMainHandItem());
            if (scopeItem == null)
                return;
            if (!this.map.containsKey(scopeItem.getTagName()))
                this.map.put(scopeItem.getTagName(), new ScopeData(scopeItem.getTagName()));
            this.handleScopeMod(event, this.map.get(scopeItem.getTagName()));
            this.scopeData = this.map.get(scopeItem.getTagName());
        }

    }

    private void handleScopeMod(InputEvent.Key event, ScopeData data) {
        double stepModifier = 1;
        boolean isLeft = Keys.LEFT.isDown();
        boolean isRight = Keys.RIGHT.isDown();
        boolean isUp = Keys.UP.isDown();
        boolean isDown = Keys.DOWN.isDown();
        boolean isControlDown = Keys.CONTROLLY.isDown() || Keys.CONTROLLYR.isDown(); // Increase Module Size
        boolean isShiftDown = Keys.SHIFTY.isDown() || Keys.SHIFTYR.isDown(); // Increase Step Size
        boolean isAltDown = Keys.ALTY.isDown() || Keys.ALTYR.isDown(); // Swap X -> Z modify

        if (isShiftDown)
            stepModifier *= 10;
        if (isControlDown)
            stepModifier /= 10;

        boolean isPeriodDown = Keys.SIZE_OPT.isDown();

        Player player = Minecraft.getInstance().player;
        if (Keys.P.isDown()) // P will be for adjusting double render
        {
            if (isShiftDown)
                stepModifier *= 10;
            if (isControlDown)
                stepModifier /= 10;

            player.displayClientMessage(Component.translatable("DR X: " + data.getDrXZoomMod() + " | DR Y: " + data.getDrYZoomMod() + " | DR Z: " + data.getDrZZoomMod()), true);

            if (isAltDown && isUp) {
                data.setDrZZoomMod(data.getDrZZoomMod() + 0.025 * stepModifier);
                player.displayClientMessage(Component.translatable("DR Z: " + data.getDrZZoomMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isAltDown && isDown) {
                data.setDrZZoomMod(data.getDrZZoomMod() - 0.025 * stepModifier);
                player.displayClientMessage(Component.translatable("DR Z: " + data.getDrZZoomMod()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isPeriodDown && isUp) {
                stepModifier *= 10;
                data.setDrZoomSizeMod((float) (data.getDrZoomSizeMod() + 0.0075f * stepModifier));
                player.displayClientMessage(Component.translatable("DR Size: " + data.getDrZoomSizeMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isPeriodDown && isDown) {
                stepModifier *= 10;
                data.setDrZoomSizeMod((float) (data.getDrZoomSizeMod() - 0.0075f * stepModifier));
                player.displayClientMessage(Component.translatable("DR Size: " + data.getDrZoomSizeMod()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isUp) {
                data.setDrYZoomMod(data.getDrYZoomMod() + 0.025 * stepModifier);
                player.displayClientMessage(Component.translatable("DR Y: " + data.getDrYZoomMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                data.setDrYZoomMod(data.getDrYZoomMod() - 0.025 * stepModifier);
                player.displayClientMessage(Component.translatable("DR Y: " + data.getDrYZoomMod()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isLeft) {
                data.setDrXZoomMod(data.getDrXZoomMod() + 0.025 * stepModifier);
                player.displayClientMessage(Component.translatable("DR X: " + data.getDrXZoomMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isRight) {
                data.setDrXZoomMod(data.getDrXZoomMod() - 0.025 * stepModifier);
                player.displayClientMessage(Component.translatable("DR X: " + data.getDrXZoomMod()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.L.isDown()) // L will be for adjusting reticle pos
        {
            if (isShiftDown)
                stepModifier *= 5;
            if (isControlDown)
                stepModifier /= 20;

            player.displayClientMessage(Component.translatable("Reticle X: " + data.getDrXZoomMod() + " | Reticle Y: " + data.getDrYZoomMod() + " | Reticle Z: " + data.getDrZZoomMod() + " | Reticle Size: " + data.getReticleSizeMod()), true);

            if (isAltDown && isUp) {
                data.setReticleZMod(data.getReticleZMod() + 0.00025 * stepModifier);
                player.displayClientMessage(Component.translatable("Reticle Z: " + data.getReticleZMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isAltDown && isDown) {
                data.setReticleZMod(data.getReticleZMod() - 0.00025 * stepModifier);
                player.displayClientMessage(Component.translatable("Reticle Z: " + data.getReticleZMod()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isPeriodDown && isUp) {
                data.setReticleSizeMod((float) (data.getReticleSizeMod() + 0.0075f * stepModifier));
                player.displayClientMessage(Component.translatable("Reticle Size: " + data.getReticleSizeMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isPeriodDown && isDown) {
                data.setReticleSizeMod((float) (data.getReticleSizeMod() - 0.0075f * stepModifier));
                player.displayClientMessage(Component.translatable("Reticle Size: " + data.getReticleSizeMod()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isUp) {
                data.setReticleYMod(data.getReticleYMod() + 0.00025 * stepModifier);
                player.displayClientMessage(Component.translatable("Reticle Y: " + data.getReticleYMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                data.setReticleYMod(data.getReticleYMod() - 0.00025 * stepModifier);
                player.displayClientMessage(Component.translatable("Reticle Y: " + data.getReticleYMod()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isLeft) {
                data.setReticleXMod(data.getReticleXMod() - 0.00025 * stepModifier);
                player.displayClientMessage(Component.translatable("Reticle X: " + data.getReticleXMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isRight) {
                data.setReticleXMod(data.getReticleXMod() + 0.00025 * stepModifier);
                player.displayClientMessage(Component.translatable("Reticle X: " + data.getReticleXMod()).withStyle(ChatFormatting.DARK_RED), true);
            }
        } else if (Keys.M.isDown()) // L will be for adjusting reticle pos
        {
            if (isShiftDown)
                stepModifier *= 10;
            if (isControlDown)
                stepModifier /= 10;

            player.displayClientMessage(Component.translatable("Crop: " + data.getDrZoomCropMod() + " | FOV zoom: " + data.getAdditionalZoomMod()), true);


            if (isUp) {
                data.setDrZoomCropMod((float) (data.getDrZoomCropMod() + 0.025 * stepModifier));
                player.displayClientMessage(Component.translatable("Crop: " + data.getDrZoomCropMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isDown) {
                data.setDrZoomCropMod((float) (data.getDrZoomCropMod() - 0.025 * stepModifier));
                player.displayClientMessage(Component.translatable("Crop: " + data.getDrZoomCropMod()).withStyle(ChatFormatting.DARK_RED), true);
            } else if (isLeft) {
                data.setAdditionalZoomMod((float) (data.getAdditionalZoomMod() - 0.025 * stepModifier));
                player.displayClientMessage(Component.translatable("FOV zoom: " + data.getAdditionalZoomMod()).withStyle(ChatFormatting.GREEN), true);
            } else if (isRight) {
                data.setAdditionalZoomMod((float) (data.getAdditionalZoomMod() + 0.025 * stepModifier));
                player.displayClientMessage(Component.translatable("FOV zoom: " + data.getAdditionalZoomMod()).withStyle(ChatFormatting.DARK_RED), true);
            }
        }
        //this.map.put(scope.getTagName(), data);
    }

    public void resetData() {
    }

    public void exportData() {
        this.map.forEach((name, scope) ->
        {
            if (this.map.get(name) == null) {
                LOGGER.log(Level.ERROR, "SCOPE EDITOR FAILED TO EXPORT THIS BROKEN DATA. CONTACT CLUMSYALIEN.");
                return;
            }
            GsonBuilder gsonB = new GsonBuilder().setLenient().addSerializationExclusionStrategy(Gun.strategy).setPrettyPrinting();
            String jsonString = gsonB.create().toJson(scope);
            this.writeExport(jsonString, name);
        });
    }

    private void writeExport(String jsonString, String name) {
        try {
            File dir = new File(Config.COMMON.development.TDevPath.get() + "\\tac_export\\scope_export");
            dir.mkdir();
            FileWriter dataWriter = new FileWriter(dir.getAbsolutePath() + "\\" + name + "_export.json");
            dataWriter.write(jsonString);
            dataWriter.close();
            LOGGER.log(Level.INFO, "SCOPE EDITOR EXPORTED FILE ( " + name + "export.txt ). BE PROUD!");
        } catch (IOException e) {
            LOGGER.log(Level.ERROR, "SCOPE EDITOR FAILED TO EXPORT, NO FILE CREATED!!! NO ACCESS IN PATH?. CONTACT CLUMSYALIEN.");
        }
    }
}
