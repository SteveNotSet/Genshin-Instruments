package com.cstav.genshinstrument;

import com.cstav.genshinstrument.item.ModItems;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber;
import net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

@EventBusSubscriber(modid = GInstrumentMod.MODID, bus = Bus.MOD)
public class ModCreativeModeTabs {

    private static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, GInstrumentMod.MODID);

    public static void regsiter(final IEventBus bus) {
        TABS.register(bus);
    }

    public static final RegistryObject<CreativeModeTab>
        instrumentsTab = TABS.register("instruments_tab",
            () -> CreativeModeTab.builder()

                .title(Component.translatable("genshinstrument.itemGroup.instruments"))
                .icon(() -> new ItemStack(ModItems.FLORAL_ZITHER.get()))
                .noScrollBar()
                
                .displayItems((displayParams, out) ->
                    ModItems.ITEMS.getEntries().forEach((item) ->
                        out.accept(item.get())
                    )
                )

            .build()
        )
    ;

}
