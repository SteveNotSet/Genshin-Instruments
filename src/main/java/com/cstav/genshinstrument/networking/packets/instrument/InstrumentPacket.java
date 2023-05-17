package com.cstav.genshinstrument.networking.packets.instrument;

import java.util.function.Supplier;

import com.cstav.genshinstrument.capability.instrumentOpen.InstrumentOpen;
import com.cstav.genshinstrument.networking.ModPacket;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.util.ServerUtil;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;

public class InstrumentPacket implements ModPacket {
    public static final NetworkDirection NETWORK_DIRECTION = NetworkDirection.PLAY_TO_SERVER;


    private final NoteSound sound;
    private final InteractionHand hand;
    private final float pitch;

    public InstrumentPacket(final NoteSound sound, final float pitch, final InteractionHand hand) {
        this.sound = sound;
        this.hand = hand;
        this.pitch = pitch;
    }
    public InstrumentPacket(FriendlyByteBuf buf) {
        sound = NoteSound.readFromNetwork(buf);
        hand = buf.readEnum(InteractionHand.class);
        pitch = buf.readFloat();
    }

    @Override
    public void toBytes(final FriendlyByteBuf buf) {
        sound.writeToNetwork(buf);
        buf.writeEnum(hand);
        buf.writeFloat(pitch);
    }


    @Override
    public boolean handle(final Supplier<Context> supplier) {
        final Context context = supplier.get();
        
        context.enqueueWork(() -> {
            final ServerPlayer player = context.getSender();

            if (!InstrumentOpen.isOpen(player))
                return;

            ServerUtil.sendPlayNotePackets(player, hand, sound, pitch);
        });

        return true;
    }
    
}