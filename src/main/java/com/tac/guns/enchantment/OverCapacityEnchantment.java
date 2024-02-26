package com.tac.guns.enchantment;

import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.item.ItemStack;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class OverCapacityEnchantment extends GunEnchantment {
    public OverCapacityEnchantment() {
        super(Rarity.RARE, EnchantmentTypes.GUN, new EquipmentSlot[]{EquipmentSlot.MAINHAND}, Type.WEAPON);
    }

    @Override
    public int getMaxLevel() {
        return 3;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack) {
        return false;
    }

    @Override
    public int getMinCost(int level) {
        return 5 + (level - 1) * 10;
    }

    @Override
    public int getMaxCost(int level) {
        return super.getMinCost(level) + 50;
    }
}
