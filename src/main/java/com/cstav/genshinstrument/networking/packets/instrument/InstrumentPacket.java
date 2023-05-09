package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.function.Supplier;

import com.cstav.genshinstrument.capability.instrumentOpen.InstrumentOpen;
import com.cstav.genshinstrument.event.InstrumentPlayedEvent;
import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.ServerUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.gameevent.GameEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;

public class InstrumentPacket implements ModPacket {
    public static final NetworkDirection NETWORK_DIRECTION = NetworkDirection.PLAY_TO_SERVER;


    private final NoteSound sound;
    private final InteractionHand hand;

    public InstrumentPacket(final NoteSound sound, final InteractionHand hand) {
        this.sound = sound;
        this.hand = hand;
    }
    public InstrumentPacket(FriendlyByteBuf buf) {
        sound = NoteSound.readFromNetwork(buf);
        this.hand = buf.readEnum(InteractionHand.class);
    }

    @Override
    public void toBytes(final FriendlyByteBuf buf) {
        sound.writeToNetwork(buf);
        buf.writeEnum(hand);
    }


    @Override
    public boolean handle(final Supplier<Context> supplier) {
        final Context context = supplier.get();
        
        context.enqueueWork(() -> {
            final ServerPlayer player = context.getSender();

            if (InstrumentOpen.isOpen(player))
                return;

            ServerUtil.sendPlayNotePackets(player, sound);
            
            // Fire the Forge instrument event
            MinecraftForge.EVENT_BUS.post(new InstrumentPlayedEvent(player, sound, hand));
            
            // Trigger an instrument game event
            // This is done so that sculk sensors can pick up the instrument's sound
            player.level.gameEvent(GameEvent.INSTRUMENT_PLAY, player.blockPosition(), GameEvent.Context.of(player));
        });

        return true;
    }
    
}