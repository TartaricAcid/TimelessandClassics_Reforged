package com.tac.guns.tileentity;

import com.tac.guns.common.container.WorkbenchContainer;
import com.tac.guns.init.ModTileEntities;
import com.tac.guns.tileentity.inventory.IStorageBlock;
import net.minecraft.core.BlockPos;
import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;

import javax.annotation.Nullable;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class WorkbenchTileEntity extends SyncedTileEntity implements IStorageBlock {
    private NonNullList<ItemStack> inventory = NonNullList.withSize(1, ItemStack.EMPTY);

    public WorkbenchTileEntity(BlockPos pos, BlockState state) {
        super(ModTileEntities.WORKBENCH.get(), pos, state);
    }

    @Override
    public NonNullList<ItemStack> getInventory() {
        return this.inventory;
    }

    @Override
    public void saveAdditional(CompoundTag compound) {
        ContainerHelper.saveAllItems(compound, this.inventory);
    }

    @Override
    public void load(CompoundTag compound) {
        super.load(compound);
        ContainerHelper.loadAllItems(compound, this.inventory);
    }

    @Override
    public boolean canPlaceItem(int index, ItemStack stack) {
        return index != 0 || (stack.getItem() instanceof DyeItem && this.inventory.get(index).getCount() < 1);
    }

    @Override
    public boolean stillValid(Player player) {
        return this.level.getBlockEntity(this.worldPosition) == this && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64.0;
    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("container.tac.workbench");
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int windowId, Inventory playerInventory, Player playerEntity) {
        return new WorkbenchContainer(windowId, playerInventory, this);
    }
}
