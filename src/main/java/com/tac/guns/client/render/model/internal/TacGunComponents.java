package com.tac.guns.client.render.model.internal;

import com.tac.guns.client.render.model.GunComponent;

public class TacGunComponents {
    //crossbow
    public static final GunComponent BEND_L = new GunComponent("tac", "bend_l", "bend_l");
    public static final GunComponent BEND_R = new GunComponent("tac", "bend_r", "bend_r");
    public static final GunComponent BONE_L = new GunComponent("tac", "bone_l", "bone_l");
    public static final GunComponent BONE_R = new GunComponent("tac", "bone_r", "bone_r");
    public static final GunComponent STRING_L_MAIN = new GunComponent("tac", "string_l_main", "string_l_main");
    public static final GunComponent STRING_R_MAIN = new GunComponent("tac", "string_r_main", "string_r_main");
    public static final GunComponent STRING_L_MOVE = new GunComponent("tac", "string_l_move", "string_l_move");
    public static final GunComponent STRING_R_MOVE = new GunComponent("tac", "string_r_move", "string_r_move");
    public static final GunComponent WHEEL_L = new GunComponent("tac", "wheel_l", "wheel_l");
    public static final GunComponent WHEEL_R = new GunComponent("tac", "wheel_r", "wheel_r");
    //barrel
    public static final GunComponent BARREL = new GunComponent("tac", "barrel", "body");                               //out length barrel for adjust the position
    public static final GunComponent BARREL_EXTENDED = new GunComponent("tac", "barrel_extended", "body");
    public static final GunComponent BARREL_STANDARD = new GunComponent("tac", "barrel_standard", "body");
    public static final GunComponent CLUMSYYY = new GunComponent("tac", "clumsyyy", "body");
    public static final GunComponent NEKOOO = new GunComponent("tac", "nekooo", "body");
    public static final GunComponent LOADER = new GunComponent("tac", "loader", "loader");
    public static final GunComponent ROTATE = new GunComponent("tac", "rotate", "mag");
    public static final GunComponent SCOPE_DEFAULT = new GunComponent("tac", "scope_default", "body");
    public static final GunComponent LIGHT = new GunComponent("tac", "light", "body");
    public static final GunComponent SAFETY = new GunComponent("tac", "safety", "body");
    public static final GunComponent BIPOD = new GunComponent("tac", "bipod", "body");                                 //the tactical bipod
    public static final GunComponent BULLET_CHAIN_COVER = new GunComponent("tac", "bullet_chain_cover", "iron");       //the clip on bullet chain (like m249)
    public static final GunComponent HAMMER = new GunComponent("tac", "hammer", "body");                               //the hammer to fire
    public static final GunComponent HANDLE_EXTRA = new GunComponent("tac", "handle_extra", "handle_extra");                               //extra handle
    public static final GunComponent RELEASE = new GunComponent("tac", "release", "release");                             //release mag clip
    public static final GunComponent ROCKET = new GunComponent("tac", "rocket", "rocket");                               //rpg7 rocket
    public static final GunComponent STOCK_FOLDED = new GunComponent("tac", "stock_folded", "body");                   //default folded stock; render if there is no stock attachment
    public static final GunComponent SLIDE_EXTENDED = new GunComponent("tac", "slide_extended", "slide");               //long pistol slide
    public static final GunComponent SLIDE_EXTENDED_LIGHT = new GunComponent("tac", "slide_extended_light", "slide");   //the light part move with slide
    public static final GunComponent PULL = new GunComponent("tac", "pull", "pull");                                   //something in barrel connect to bolt handle
}