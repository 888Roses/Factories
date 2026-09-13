package dev.rosenoire.factories.client.index;

import dev.rosenoire.factories.shared.index.AllItems;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.creativetab.v1.CreativeModeTabEvents;
import net.minecraft.world.item.CreativeModeTabs;
import net.minecraft.world.item.Items;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.NonExtendable
@Environment(EnvType.CLIENT)
public interface AllCreativeTabs {
    static void initialize() {
        CreativeModeTabEvents
                .modifyOutputEvent(CreativeModeTabs.REDSTONE_BLOCKS)
                .register(output -> {
                    output.insertAfter(Items.COMPARATOR, AllItems.REDSTONE_RADIO);
                });
    }
}
