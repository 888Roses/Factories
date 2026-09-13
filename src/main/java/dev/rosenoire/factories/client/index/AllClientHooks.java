package dev.rosenoire.factories.client.index;

import dev.rosenoire.factories.client.content.hooks.debugging.ItemTagsDebugEntryHook;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;

public interface AllClientHooks {
    static void initialize() {
        ItemTooltipCallback.EVENT.register(ItemTagsDebugEntryHook.INSTANCE);
    }
}
