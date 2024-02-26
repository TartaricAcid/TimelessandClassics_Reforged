package com.tac.guns.item;

import com.tac.guns.item.attachment.IStock;
import com.tac.guns.item.attachment.impl.Stock;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;

/**
 * A basic stock attachment item implementation with color support
 * <p>
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class StockItem extends Item implements IStock, IColored {
    private final Stock stock;
    private final boolean colored;

    public StockItem(Stock stock, Properties properties) {
        super(properties);
        this.stock = stock;
        this.colored = true;
    }

    public StockItem(Stock stock, Properties properties, boolean colored) {
        super(properties);
        this.stock = stock;
        this.colored = colored;
    }

    @Override
    public Stock getProperties() {
        return this.stock;
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
