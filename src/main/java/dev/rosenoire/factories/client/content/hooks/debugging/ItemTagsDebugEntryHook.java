package dev.rosenoire.factories.client.content.hooks.debugging;

import dev.rosenoire.factories.client.index.AllDebugEntries;
import dev.rosenoire.factories.shared.index.AllLang;
import net.fabricmc.fabric.api.client.item.v1.ItemTooltipCallback;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import org.jspecify.annotations.NonNull;

import java.util.Comparator;
import java.util.List;

public final class ItemTagsDebugEntryHook implements ItemTooltipCallback {
    public static final ItemTagsDebugEntryHook INSTANCE = new ItemTagsDebugEntryHook();

    @Override
    public void getTooltip(@NonNull ItemStack stack,
                           Item.@NonNull TooltipContext tooltipContext,
                           @NonNull TooltipFlag tooltipFlag,
                           @NonNull List<Component> lines) {
        if (!AllDebugEntries.isVisible(AllDebugEntries.ITEM_TAGS)) {
            return;
        }

        var tags = stack
                .tags()
                .sorted(Comparator.comparing(tagKey -> tagKey.location().toString()))
                .toList();

        if (tags.isEmpty()) {
            return;
        }

        lines.add(Component.empty());
        lines.add(Component.translatable(AllLang.DEBUG_TAGS_TITLE));

        for (var tag : tags) {
            var location = tag.location().toString();
            var formattedLocation = Component
                    .literal(location)
                    .withStyle(
                            location.startsWith("c:")
                                    ? ChatFormatting.BLUE
                                    : location.startsWith("minecraft:")
                                      ? ChatFormatting.DARK_GREEN
                                      : ChatFormatting.WHITE
                    );
            lines.add(Component.translatable(AllLang.DEBUG_TAGS_ENTRY, formattedLocation));
        }
    }
}