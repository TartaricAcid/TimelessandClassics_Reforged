package com.tac.guns.util;

import com.tac.guns.common.Rig;
import com.tac.guns.duck.PlayerWithSynData;
import com.tac.guns.entity.ProjectileEntity;
import com.tac.guns.item.transition.wearables.ArmorRigItem;
import com.tac.guns.network.PacketHandler;
import com.tac.guns.network.message.MessageGunSound;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.network.PacketDistributor;

import javax.annotation.Nonnull;

/**
 * Author: Forked from MrCrayfish, continued by Timeless devs
 */
public class WearableHelper {
    // Helpers, to maintain speed and efficency, we need to check if the tag is populated BEFORE running the helper methods

    @Nonnull
    public static ItemStack PlayerWornRig(Player player) {
        return ((PlayerWithSynData) player).getRig();
    }

    public static void FillDefaults(ItemStack item, Rig rig) {
        item.getTag().putFloat("RigDurability", RigEnchantmentHelper.getModifiedDurability(item, rig));
    }

    /**
     * @param rig The Itemstack for armor, I don't want helpers to view through static capability's
     * @return true if the armor is at full durability
     */
    public static boolean isFullDurability(ItemStack rig) {
        Rig modifiedRig = ((ArmorRigItem) rig.getItem()).getModifiedRig(rig);
        float max = RigEnchantmentHelper.getModifiedDurability(rig, modifiedRig);

        if (rig.getTag().getFloat("RigDurability") >= max) {
            return true;
        }
        return false;
    }

    public static boolean tickFromCurrentDurability(Player player, ProjectileEntity proj) {
        ItemStack rig = PlayerWornRig(player);
        float og = rig.getTag().getFloat("RigDurability");
        rig.getTag().remove("RigDurability");

        if (og == 0)
            return true;
        if (og - proj.getDamage() > 0)
            rig.getTag().putFloat("RigDurability", og - proj.getDamage());
        else if (og - proj.getDamage() < 0) {
            ResourceLocation brokenSound = ((ArmorRigItem) rig.getItem()).getRig().getSounds().getBroken();
            if (brokenSound != null) {
                MessageGunSound messageSound = new MessageGunSound(brokenSound, SoundSource.PLAYERS, (float) player.getX(), (float) (player.getY() + 1.0), (float) player.getZ(), 1.5F, 1F, player.getId(), false, false);
                PacketHandler.getPlayChannel().send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) player), messageSound);
            }
            rig.getTag().putFloat("RigDurability", 0);
            return false;
        }

        return false;
    }

    public static float currentDurabilityPercentage(ItemStack rig) {
        return rig.getTag().getFloat("RigDurability") / ((ArmorRigItem) rig.getItem()).getRig().getRepair().getDurability();
    }

    /**
     * @param rig The Itemstack for armor, I don't want helpers to view through static capability's
     * @return true if the armor is fully repaired, false if armor only got ticked and not at max
     */
    public static boolean tickRepairCurrentDurability(ItemStack rig) {
        Rig modifiedRig = ((ArmorRigItem) rig.getItem()).getModifiedRig(rig);
        float og = rig.getTag().getFloat("RigDurability"),
                max = RigEnchantmentHelper.getModifiedDurability(rig, modifiedRig),
                ofDurability = modifiedRig.getRepair().getQuickRepairability();

        rig.getTag().remove("RigDurability");
        float totalAfterRepair = og + (max * ofDurability);
        if (totalAfterRepair >= max) {
            rig.getTag().putFloat("RigDurability", max);
        } else {
            rig.getTag().putFloat("RigDurability", totalAfterRepair);
            return true;
        }
        return false;
    }

    /**
     * @param rig    The Itemstack for armor, I don't want helpers to view through static capability's
     * @param repair The percentage to repair off the armor, can be used for custom methods, healing stations, ETC.
     * @return true if the armor is fully repaired, false if armor only got ticked and not at max
     */
    public static boolean tickRepairCurrentDurability(ItemStack rig, float repair) {
        Rig modifiedRig = ((ArmorRigItem) rig.getItem()).getModifiedRig(rig);
        float og = rig.getTag().getFloat("RigDurability"),
                max = RigEnchantmentHelper.getModifiedDurability(rig, modifiedRig),
                ofDurability = repair;

        rig.getTag().remove("RigDurability");
        float totalAfterRepair = og + (max * ofDurability);
        if (og >= max) {
            rig.getTag().putFloat("RigDurability", max);
        } else {
            rig.getTag().putFloat("RigDurability", totalAfterRepair);
            return true;
        }
        return false;
    }

    public static float GetCurrentDurability(ItemStack item) {
        return item.getTag().getFloat("RigDurability");
    }
}
