//package com.tac.guns.client.settings;
//
//import com.tac.guns.Config;
//import net.minecraft.client.OptionInstance;
//import net.minecraft.client.ProgressOption;
//import net.minecraft.util.Mth;
//
//import java.text.DecimalFormat;
//
///**
// * Author: Forked from MrCrayfish, continued by Timeless devs
// */
//public class GunOptions {
//
//    public static final OptionInstance<Boolean> DOUBLE_RENDER_EXIST = OptionInstance.createBoolean("tac.options.doubleRender",
//            Config.CLIENT.display.scopeDoubleRender.get(), (value) -> {
//                Config.CLIENT.display.scopeDoubleRender.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> HOLD_TO_AIM = OptionInstance.createBoolean("tac.options.holdToAim",
//            Config.CLIENT.controls.holdToAim.get(), (value) -> {
//                Config.CLIENT.controls.holdToAim.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> ALLOW_LEVER = OptionInstance.createBoolean("tac.options.allowLever",
//            Config.CLIENT.rightClickUse.allowLever.get(), (value) -> {
//                Config.CLIENT.rightClickUse.allowLever.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> ALLOW_BUTTON = OptionInstance.createBoolean("tac.options.allowButton",
//            Config.CLIENT.rightClickUse.allowButton.get(), (value) -> {
//                Config.CLIENT.rightClickUse.allowButton.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> ALLOW_DOORS = OptionInstance.createBoolean("tac.options.allowDoors",
//            Config.CLIENT.rightClickUse.allowDoors.get(), (value) -> {
//                Config.CLIENT.rightClickUse.allowDoors.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> ALLOW_TRAP_DOORS = OptionInstance.createBoolean("tac.options.allowTrapDoors",
//            Config.CLIENT.rightClickUse.allowTrapDoors.get(), (value) -> {
//                Config.CLIENT.rightClickUse.allowTrapDoors.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> ALLOW_CRAFTING_TABLE = OptionInstance.createBoolean("tac.options.allowCraftingTable",
//            Config.CLIENT.rightClickUse.allowCraftingTable.get(), (value) -> {
//                Config.CLIENT.rightClickUse.allowCraftingTable.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> ALLOW_CHESTS = OptionInstance.createBoolean("tac.options.allowChests",
//            Config.CLIENT.rightClickUse.allowChests.get(), (value) -> {
//                Config.CLIENT.rightClickUse.allowChests.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> ALLOW_FENCE_GATES = OptionInstance.createBoolean("tac.options.allowFenceGates",
//            Config.CLIENT.rightClickUse.allowFenceGates.get(), (value) -> {
//                Config.CLIENT.rightClickUse.allowFenceGates.set(value);
//                Config.saveClientConfig();
//            });
//    public static final OptionInstance<Boolean> FIREMODE_EXIST = OptionInstance.createBoolean("tac.options.firemodeExist",
//            Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.get(), (value) -> {
//                Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.set(value);
//                Config.saveClientConfig();
//            });
//    //AmmoCounter positioning
//    public static final OptionInstance<Boolean> AMMOCOUNTER_EXIST = OptionInstance.createBoolean("tac.options.ammoCounterExist",
//            Config.CLIENT.weaponGUI.weaponAmmoCounter.showWeaponAmmoCounter.get(), (value) -> {
//                Config.CLIENT.weaponGUI.weaponAmmoCounter.showWeaponAmmoCounter.set(value);
//                Config.saveClientConfig();
//            });
//
//    /*private static Object CrosshairHandler;
//    public static final Option CROSSHAIR = new GunListOption<>("tac.options.crosshair", () -> {
//        return CrosshairHandler.get().getRegisteredCrosshairs();
//    }, () -> {
//        return ResourceLocation.tryParse(Config.CLIENT.display.crosshair.get());
//    }, (value) -> {
//        Config.CLIENT.display.crosshair.set(value toString());
//        Config.saveClientConfig();
//        CrosshairHandler.get().setCrosshair(value);
//    }, (value) -> {
//        ResourceLocation id = value.getLocation();
//        return Component.translatable(id.getNamespace() + ".crosshair." + id.getPath());
//    }).setRenderer((button, matrixStack, partialTicks) -> {
//        matrixStack.pushPose();
//        matrixStack.translate(button.x, button.y, 0);
//        matrixStack.translate(button.getWidth() + 2, 2, 0);
//        Crosshair crosshair = CrosshairHandler.get().getCurrentCrosshair();
//        if(crosshair != null)
//        {
//            if(crosshair.isDefault())
//            {
//                Minecraft mc = Minecraft.getInstance();
//                mc.getTextureManager().bind(GuiComponent.GUI_ICONS_LOCATION);
//                GuiComponent.blit(matrixStack, (16 - 15) / 2, (16 - 15) / 2, 0, 0, 0, 15, 15, 256, 256);
//            }
//            else
//            {
//                crosshair.render(Minecraft.getInstance(), matrixStack, 16, 16, partialTicks);
//            }
//        }
//        matrixStack.popPose();
//    });
//
//    public static final OptionInstance<Boolean> SHOW_FPS_TRAILS_EXIST = OptionInstance.createBoolean("tac.options.showFirstPersonBulletTrails",
//        return Config.CLIENT.display.showFirstPersonBulletTrails.get();
//    }, (settings, function, value) -> {
//        Config.CLIENT.display.showFirstPersonBulletTrails.set(value);
//        Config.saveClientConfig();
//    });*/
//    //WeaponIcon positioning
//    public static final OptionInstance<Boolean> WeaponIcon_EXIST = OptionInstance.createBoolean("tac.options.iconExist",
//            Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.get(), (value) -> {
//                Config.CLIENT.weaponGUI.weaponTypeIcon.showWeaponIcon.set(value);
//                Config.saveClientConfig();
//            });
//    //WeaponIcon positioning
//    public static final OptionInstance<Boolean> ReloadBar_EXIST = OptionInstance.createBoolean("tac.options.reloadBarExist",
//            Config.CLIENT.weaponGUI.weaponReloadTimer.showWeaponReloadTimer.get(), (value) -> {
//                Config.CLIENT.weaponGUI.weaponReloadTimer.showWeaponReloadTimer.set(value);
//                Config.saveClientConfig();
//            });
//    private static final DecimalFormat FORMAT = new DecimalFormat("0.0#");
//    public static final OptionInstance<Double> ADS_SENSITIVITY = new OptionInstance<>("tac.options.adsSensitivity", OptionInstance.noTooltip(), 0.0, 1.0, 0.01F, gameSettings -> {
//        return Config.CLIENT.controls.aimDownSightSensitivity.get();
//    }, (gameSettings, value) -> {
//        Config.CLIENT.controls.aimDownSightSensitivity.set(Mth.clamp(value, 0.0, 2.0));
//        Config.saveClientConfig();
//    }, (gameSettings, option) -> {
//        double adsSensitivity = Config.CLIENT.controls.aimDownSightSensitivity.get();
//        return Component.translatable("tac.options.adsSensitivity.format", FORMAT.format(adsSensitivity));
//    });
//    //Firemode positioning
//    public static final ProgressOption X_FIREMODE_POS = new ProgressOption("tac.options.xFiremodePos", -500, 500, 0.001F,
//            gameSettings -> {
//                return Config.CLIENT.weaponGUI.weaponFireMode.x.get();
//            },
//            (gameSettings, value) -> {
//                Config.CLIENT.controls.aimDownSightSensitivity.set(Mth.clamp(value, 0.0, 2.0));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> {
//                double xPos = Config.CLIENT.weaponGUI.weaponFireMode.x.get();
//                return Component.translatable("tac.options.xFiremodePos.format", FORMAT.format(xPos));
//            });
//    public static final ProgressOption Y_FIREMODE_POS = new ProgressOption("tac.options.yFiremodePos", -500, 500, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponFireMode.y.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponFireMode.y.set(Mth.clamp(value, -500, 500));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.yFiremodePos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponFireMode.y.get())));
//    public static final ProgressOption SIZE_FIREMODE_POS = new ProgressOption("tac.options.sizeFiremodePos", 0.1, 4, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponFireMode.weaponFireModeSize.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponFireMode.weaponFireModeSize.set(Mth.clamp(value, 0.1, 4));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.sizeFiremodePos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponFireMode.weaponFireModeSize.get())));
//    public static final ProgressOption X_AMMOCOUNTER_POS = new ProgressOption("tac.options.xAmmoCounterPos", -500, 500, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponAmmoCounter.x.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponAmmoCounter.x.set(Mth.clamp(value, -500, 500));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.xAmmoCounterPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponAmmoCounter.x.get())));
//    public static final ProgressOption Y_AMMOCOUNTER_POS = new ProgressOption("tac.options.yAmmoCounterPos", -500, 500, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponAmmoCounter.y.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponAmmoCounter.y.set(Mth.clamp(value, -500, 500));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.yAmmoCounterPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponAmmoCounter.y.get())));
//    public static final ProgressOption SIZE_AMMOCOUNTER_POS = new ProgressOption("tac.options.sizeAmmoCounterPos", 0.1, 4, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponAmmoCounter.weaponAmmoCounterSize.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponAmmoCounter.weaponAmmoCounterSize.set(Mth.clamp(value, 0.1, 4));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.sizeAmmoCounterPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponAmmoCounter.weaponAmmoCounterSize.get())));
//    public static final ProgressOption X_Icon_POS = new ProgressOption("tac.options.xIconPos", -500, 500, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponTypeIcon.x.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponTypeIcon.x.set(Mth.clamp(value, -500, 500));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.xIconPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponTypeIcon.x.get())));
//    public static final ProgressOption Y_Icon_POS = new ProgressOption("tac.options.yIconPos", -500, 500, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponTypeIcon.y.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponTypeIcon.y.set(Mth.clamp(value, -500, 500));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.yIconPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponTypeIcon.y.get())));
//    public static final ProgressOption SIZE_Icon_POS = new ProgressOption("tac.options.sizeIconPos", 0.1, 4, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponTypeIcon.weaponIconSize.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponTypeIcon.weaponIconSize.set(Mth.clamp(value, 0.1, 4));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.sizeIconPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponTypeIcon.weaponIconSize.get())));
//    public static final ProgressOption X_ReloadBar_POS = new ProgressOption("tac.options.xReloadBarPos", -500, 500, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponReloadTimer.x.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponReloadTimer.x.set(Mth.clamp(value, -500, 500));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.xReloadBarPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponReloadTimer.x.get())));
//
//
//    public static final ProgressOption Fire_Volume = new ProgressOption("tac.options.weaponsVolume", 0f, 1f, 0.01F,
//            gameSettings -> Config.CLIENT.sounds.weaponsVolume.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.sounds.weaponsVolume.set(Mth.clamp(value, 0f, 1f));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.weaponsVolume.format", FORMAT.format(Config.CLIENT.sounds.weaponsVolume.get())));
//
//    public static final ProgressOption Y_ReloadBar_POS = new ProgressOption("tac.options.yReloadBarPos", -500, 500, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponReloadTimer.y.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponReloadTimer.y.set(Mth.clamp(value, -500, 500));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.yReloadBarPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponReloadTimer.y.get())));
//
//    public static final ProgressOption SIZE_ReloadBar_POS = new ProgressOption("tac.options.sizeReloadBarPos", 0.1, 4, 0.001F,
//            gameSettings -> Config.CLIENT.weaponGUI.weaponReloadTimer.weaponReloadTimerSize.get(),
//            (gamesettings, value) -> {
//                Config.CLIENT.weaponGUI.weaponReloadTimer.weaponReloadTimerSize.set(Mth.clamp(value, 0.1, 4));
//                Config.saveClientConfig();
//            },
//            (gameSettings, option) -> Component.translatable("tac.options.sizeReloadBarPos.format", FORMAT.format(Config.CLIENT.weaponGUI.weaponReloadTimer.weaponReloadTimerSize.get())));
//
//}