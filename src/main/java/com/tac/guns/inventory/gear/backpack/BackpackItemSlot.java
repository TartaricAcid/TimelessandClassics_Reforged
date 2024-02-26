package com.tac.guns.inventory.gear.backpack;

import com.tac.guns.item.IAmmo;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.items.IItemHandler;
import net.minecraftforge.items.SlotItemHandler;

public class BackpackItemSlot extends SlotItemHandler {

    public BackpackItemSlot(IItemHandler itemHandler, int index, int xPosition, int yPosition) {
        super(itemHandler, index, xPosition, yPosition);
    }

    @Override
    public boolean mayPlace(ItemStack stack) {
        return stack.getItem() instanceof IAmmo;
    }
}
