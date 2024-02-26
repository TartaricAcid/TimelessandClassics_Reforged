package com.tac.guns.item;

import com.tac.guns.item.attachment.ISideRail;
import com.tac.guns.item.attachment.impl.SideRail;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * A basic under barrel attachment item implementation with color support
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class SideRailItem extends Item implements ISideRail, IColored, IEasyColor {
    private final SideRail sideRail;
    private final boolean colored;

    public SideRailItem(SideRail underBarrel, Properties properties) {
        super(properties);
        this.sideRail = underBarrel;
        this.colored = true;
    }

    public SideRailItem(SideRail underBarrel, Properties properties, boolean colored) {
        super(properties);
        this.sideRail = underBarrel;
        this.colored = colored;
    }

    @Override
    public SideRail getProperties() {
        return this.sideRail;
    }

    @Override
    public boolean canColor(ItemStack stack) {
        return this.colored;
    }

    @Override
    public boolean canApplyAtEnchantingTable(ItemStack stack, Enchantment enchantment) {
        return enchantment == Enchantments.BINDING_CURSE || super.canApplyAtEnchantingTable(stack, enchantment);
    }
}
