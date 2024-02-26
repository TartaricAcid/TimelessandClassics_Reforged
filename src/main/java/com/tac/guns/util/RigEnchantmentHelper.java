package com.tac.guns.util;

import com.tac.guns.common.Rig;
import com.tac.guns.init.ModEnchantments;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.EnchantmentHelper;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class RigEnchantmentHelper {

    /*public static float getModifiedDurability(ItemStack rig, Rig modifiedRig)
    {
        float maxDurability = modifiedRig.getRepair().getDurability();
        int level = EnchantmentHelper.getEnchantmentLevel(ModEnchantments.TRIGGER_FINGER.get(), rig);
        if(level > 0)
        {
            float newRate = maxDurability * (0.15F * level);
            maxDurability += MathHelper.clamp(newRate, 0, maxDurability);
        }
        return Math.max(maxDurability, 1);
    }*/
    public static float getModifiedDurability(ItemStack rig, Rig modifiedRig) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.RIFLING.get(), rig);
        if (level > 0) {
            return modifiedRig.getRepair().getDurability() * (1.0f + (0.0334f * level));
        }
        return modifiedRig.getRepair().getDurability();
    }


    public static double getAimDownSightSpeed(ItemStack weapon) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTWEIGHT.get(), weapon);
        if (level > 0) {
            return 1.0 + (0.075 * level);
        }
        return 1;
    }

    public static float getSpreadModifier(ItemStack weapon) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.RIFLING.get(), weapon);
        if (level > 0) {
            return 1.0f - (0.0333f * level);
        }
        return 1f;
    }

    public static float getWeightModifier(ItemStack weapon) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.LIGHTWEIGHT.get(), weapon);
        if (level > 0) {
            return 0.4f * level;
        }
        return 0f;
    }

    public static double getProjectileSpeedModifier(ItemStack weapon) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if (level > 0) {
            return 1.0 + 0.075 * level;
        }
        return 1.0;
    }

    public static float getAcceleratorDamage(ItemStack weapon, float damage) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.ACCELERATOR.get(), weapon);
        if (level > 0) {
            return damage + damage * (0.0875F * level);
        }
        return damage;
    }

    public static float getBufferedRecoil(ItemStack weapon) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.BUFFERED.get(), weapon);
        if (level > 0) {
            return (1 - (0.03F * level));
        }
        return 1;
    }

    public static float getPuncturingChance(ItemStack weapon) {
        int level = EnchantmentHelper.getItemEnchantmentLevel(ModEnchantments.PUNCTURING.get(), weapon);
        return level * 0.05F;
    }
}
