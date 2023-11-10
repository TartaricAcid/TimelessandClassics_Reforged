package com.tac.guns.client;

import com.mojang.blaze3d.platform.InputConstants;
import com.mojang.blaze3d.platform.InputConstants.Key;
import com.tac.guns.Config;
import com.tac.guns.client.TacKeyMapping.TacKeyBuilder;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageArmorEquip;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.client.Options;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.client.settings.KeyConflictContext;
import net.minecraftforge.client.settings.KeyModifier;

@OnlyIn(Dist.CLIENT)
public final class Keys
{
    
    private static class MouseKeyBinding extends KeyMapping
    {
        private MouseKeyBinding( String description, Key mouse_button )
        {
            super(
                description,
                GunConflictContext.IN_GAME_HOLDING_WEAPON,
                KeyModifier.NONE,
                mouse_button,
                "key.categories.tac"
            );
        }
        
        @Override
        public boolean same( KeyMapping mapping )
        {
            // No conflict with vanilla attack and item use key binding.
            final Options settings = Minecraft.getInstance().options;
            return(
                mapping != settings.keyAttack
                && mapping != settings.keyUse
                && super.same( mapping )
            );
        }
    }
    
    public static final KeyMapping
        PULL_TRIGGER = Minecraft.getInstance().options.keyAttack,
        AIM_HOLD = Minecraft.getInstance().options.keyUse,
        AIM_TOGGLE = AIM_HOLD;
    
    public static final TacKeyMapping
        RELOAD = new TacKeyBuilder( "key.tac.reload" ).withKeyboardKey( InputConstants.KEY_R ).buildAndRegis(),
        UNLOAD = new TacKeyBuilder( "key.tac.unload" ).withKeyboardKey( InputConstants.KEY_R ).withKeyModifier( KeyModifier.ALT ).buildAndRegis(),
        ATTACHMENTS = new TacKeyBuilder( "key.tac.attachments" ).withKeyboardKey( InputConstants.KEY_Z ).buildAndRegis(),
        FIRE_SELECT = new TacKeyBuilder( "key.tac.fireSelect" ).withKeyboardKey( InputConstants.KEY_G ).buildAndRegis(),
        INSPECT = new TacKeyBuilder( "key.tac.inspect" ).withKeyboardKey( InputConstants.KEY_H ).buildAndRegis(),
        SIGHT_SWITCH = new TacKeyBuilder( "key.tac.sight_switch" ).withKeyboardKey( InputConstants.KEY_V ).buildAndRegis(),
        ACTIVATE_SIDE_RAIL = new TacKeyBuilder( "key.tac.activeSideRail" ).withKeyboardKey( InputConstants.KEY_B ).buildAndRegis(),
        EQUIP_ARMOR = new TacKeyBuilder("key.tac.equipArmor").withKeyboardKey( InputConstants.KEY_O).buildAndRegis(),
        
        ARMOR_REPAIRING = new TacKeyBuilder( "key.tac.armor_repairing" ).withKeyboardKey( InputConstants.KEY_K ).buildAndRegis();

        static {
            EQUIP_ARMOR.addPressCallback(()->{
                PacketHandler.getPlayChannel().sendToServer(new MessageArmorEquip());
            });
        }
    public static final TacKeyMapping[] KEYS_VALUE = {RELOAD, UNLOAD, ATTACHMENTS, FIRE_SELECT, INSPECT, SIGHT_SWITCH, ACTIVATE_SIDE_RAIL, ARMOR_REPAIRING};

    public static boolean noConflict(TacKeyMapping key) {
        for (TacKeyMapping k : KEYS_VALUE) {
            if (k == key) {
                if (!k.getKeyModifier().isActive(null))
                    return false;
            } else {
                if (k.isDown() && k.getKeyModifier().isActive(null))
                    return false;
            }
        }
        return true;
    }
  
    public static final TacKeyMapping MORE_INFO_HOLD = new TacKeyBuilder( "key.tac.more_info_hold" )
            .withKeyboardKey( InputConstants.KEY_LSHIFT )
            .withConflictContext( KeyConflictContext.GUI )
            .buildAndRegis();
    
    public static KeyMapping
        SHIFTY = null,
        CONTROLLY = null,
        ALTY = null,
        SHIFTYR = null,
        CONTROLLYR = null,
        ALTYR = null,
        SIZE_OPT = null,
        P = null,
        L = null,
        O = null,
        K = null,
        M = null,
        I = null,
        J = null,
        N = null,
        UP = null,
        RIGHT = null,
        LEFT = null,
        DOWN = null;
    
    static
    {
        if ( Config.COMMON.development.enableTDev.get() )
        {
            SHIFTY = new TacKeyBuilder( "key.tac.ss" ).withKeyboardKey( InputConstants.KEY_LSHIFT ).buildAndRegis();
            CONTROLLY = new TacKeyBuilder( "key.tac.cc" ).withKeyboardKey( InputConstants.KEY_LCONTROL ).buildAndRegis();
            ALTY = new TacKeyBuilder( "key.tac.aa" ).withKeyboardKey( InputConstants.KEY_LALT ).buildAndRegis();
            SHIFTYR = new TacKeyBuilder( "key.tac.ssr" ).withKeyboardKey( InputConstants.KEY_RSHIFT ).buildAndRegis();
            CONTROLLYR = new TacKeyBuilder( "key.tac.ccr" ).withKeyboardKey( InputConstants.KEY_RCONTROL ).buildAndRegis();
            ALTYR = new TacKeyBuilder("key.tac.aar" ).withKeyboardKey( InputConstants.KEY_RALT ).buildAndRegis();
            SIZE_OPT = new TacKeyBuilder( "key.tac.sizer" ).withKeyboardKey( InputConstants.KEY_PERIOD ).buildAndRegis();
            P = new TacKeyBuilder( "key.tac.p" ).withKeyboardKey( InputConstants.KEY_P ).buildAndRegis();
            L = new TacKeyBuilder( "key.tac.l" ).withKeyboardKey( InputConstants.KEY_L ).buildAndRegis();
            O = new TacKeyBuilder( "key.tac.o" ).withKeyboardKey( InputConstants.KEY_O ).buildAndRegis();
            K = new TacKeyBuilder( "key.tac.k" ).withKeyboardKey( InputConstants.KEY_K ).buildAndRegis();
            M = new TacKeyBuilder( "key.tac.m" ).withKeyboardKey( InputConstants.KEY_M ).buildAndRegis();
            I = new TacKeyBuilder( "key.tac.i" ).withKeyboardKey( InputConstants.KEY_I ).buildAndRegis();
            J = new TacKeyBuilder( "key.tac.j" ).withKeyboardKey( InputConstants.KEY_J ).buildAndRegis();
            N = new TacKeyBuilder( "key.tac.n" ).withKeyboardKey( InputConstants.KEY_N ).buildAndRegis();
            UP = new TacKeyBuilder( "key.tac.bbb" ).withKeyboardKey( InputConstants.KEY_UP ).buildAndRegis();
            RIGHT = new TacKeyBuilder( "key.tac.vvv" ).withKeyboardKey( InputConstants.KEY_RIGHT ).buildAndRegis();
            LEFT = new TacKeyBuilder( "key.tac.ccc" ).withKeyboardKey( InputConstants.KEY_LEFT ).buildAndRegis();
            DOWN = new TacKeyBuilder( "key.tac.zzz" ).withKeyboardKey( InputConstants.KEY_DOWN ).buildAndRegis();
        }
    }
    
    private Keys() { }
}