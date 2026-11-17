package org.omega.createforever.util;

import org.omega.createforever.advancements.ModCriteriaTriggers;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.Item;

public final class SewingAdvancements {
    private SewingAdvancements() {
    }

    public static void onHatSewn(ServerPlayer player, Item hat) {
        ModCriteriaTriggers.SEW_HAT.get().trigger(player, hat);
    }

    public static String criterionName(Item item) {
        return "sew_" + net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(item).getPath();
    }

}
