package com.tac.guns.item.transition;


import com.tac.guns.GunMod;
import com.tac.guns.client.Keys;
import com.tac.guns.common.Gun;
import com.tac.guns.interfaces.IGunModifier;
import com.tac.guns.util.Process;
import net.minecraft.ChatFormatting;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;

import javax.annotation.Nullable;
import java.util.List;

public class TimelessPistolGunItem extends TimelessGunItem {
    public TimelessPistolGunItem(Process<Properties> properties) {
        super(properties1 -> properties.process(new Properties().stacksTo(1).tab(GunMod.GROUP)));
    }

    public TimelessPistolGunItem(Process<Item.Properties> properties, IGunModifier... modifiers) {
        super(properties1 -> properties.process(new Item.Properties().stacksTo(1).tab(GunMod.GROUP)), modifiers);
    }

    public TimelessPistolGunItem() {
        this(properties -> properties);
    }

    @Override
    public void appendHoverText(ItemStack stack, @Nullable Level worldIn, List<Component> tooltip, TooltipFlag flag) {
        Gun modifiedGun = this.getModifiedGun(stack);
        CompoundTag tagCompound = stack.getTag();
        super.appendHoverText(stack, worldIn, tooltip, flag);
        boolean isShift = Keys.MORE_INFO_HOLD.isDown();
        if (isShift) {
            if (tagCompound != null) {
                //tooltip.add((Component.translatable("info.tac.oldRifle", Component.translatable(IAttachment.Type.OLD_SCOPE.getTranslationKey())).withStyle(ChatFormatting.GREEN)));
                //tooltip.add((Component.translatable("info.tac.pistolScope", Component.translatable("MiniScope").withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.LIGHT_PURPLE)));
                tooltip.add((Component.translatable("info.tac.pistolBarrel", Component.translatable("PistolBarrel").withStyle(ChatFormatting.BOLD)).withStyle(ChatFormatting.LIGHT_PURPLE)));
            }
        }
    }
}