package com.cstav.genshinstrument.item;

import java.util.function.Consumer;

import com.cstav.genshinstrument.capability.instrumentOpen.InstrumentOpenProvider;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractInstrumentScreen;
import com.cstav.genshinstrument.item.clientExtensions.ClientInstrumentItem;
import com.cstav.genshinstrument.networking.ModPacketHandler;
import com.cstav.genshinstrument.networking.packets.instrument.NotifyInstrumentOpenPacket;
import com.cstav.genshinstrument.networking.packets.instrument.OpenInstrumentPacket;
import com.cstav.genshinstrument.sound.NoteSound;

import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.UseAnim;
import net.minecraft.world.level.Level;
import net.minecraftforge.client.extensions.common.IClientItemExtensions;

/**
 * An item responsible for opening an {@link AbstractInstrumentScreen}.
 */
public class InstrumentItem extends Item {

    protected final ServerPlayerRunnable onOpenRequest;
    /**
     * @param onOpenRequest A server-side event fired when the player has requested to interact
     * with the instrument.
     * It should should send a packet to the given player for opening this instrument's screen.
     * @param noteSounds An array consisting of note sounds to update their instrument value to be this instruemnt
     */
    public InstrumentItem(final ServerPlayerRunnable onOpenRequest, final NoteSound[]... noteSounds) {
        this(onOpenRequest);

        for (int i = 0; i < noteSounds.length; i++)
            for (int j = 0; j < noteSounds[0].length; j++)
                noteSounds[i][j].instrument = this;
    }
    /**
     * @param onOpenRequest A server-side event fired when the player has requested to interact
     * with the instrument.
     * It should should send a packet to the given player for opening this instrument's screen.
     */
    public InstrumentItem(final ServerPlayerRunnable onOpenRequest) {
        super(new Properties()
            .stacksTo(1)
        );

        this.onOpenRequest = onOpenRequest;
    }

    static void sendOpenRequest(final ServerPlayer player, final String instrumentType) {
        ModPacketHandler.sendToClient(new OpenInstrumentPacket(instrumentType), player);
    }
    

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide) {
            onOpenRequest.run((ServerPlayer)pPlayer);

            // Update the the capabilty on server
            InstrumentOpenProvider.setOpen(pPlayer, true);
            // And clients
            pLevel.players().forEach((player) ->
                ModPacketHandler.sendToClient(
                    new NotifyInstrumentOpenPacket(pPlayer.getUUID(), true),
                    (ServerPlayer)player
                )
            );
        }
        
        return InteractionResultHolder.success(pPlayer.getItemInHand(pUsedHand));
    }

    @Override
    public UseAnim getUseAnimation(ItemStack pStack) {
        return UseAnim.CUSTOM;
    }
    @Override
    public void initializeClient(Consumer<IClientItemExtensions> consumer) {
        consumer.accept(new ClientInstrumentItem());
    }
    
    

    @FunctionalInterface
    public static interface ServerPlayerRunnable {
        void run(final ServerPlayer player);
    }
}
