package com.cstav.genshinstrument.networking.packet.instrument;

import com.cstav.genshinstrument.capability.instrumentOpen.InstrumentOpenProvider;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.IModPacket;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent.Context;

public class CloseInstrumentPacket implements IModPacket {
    public static final NetworkDirection NETWORK_DIRECTION = NetworkDirection.PLAY_TO_SERVER;

    public CloseInstrumentPacket() {}
    public CloseInstrumentPacket(FriendlyByteBuf buf) {}


    @Override
    public void handle(final Context context) {
        final ServerPlayer player = context.getSender();
        InstrumentOpenProvider.setClosed(player);

        for (final Player oPlayer : player.level().players())
            GIPacketHandler.sendToClient(new NotifyInstrumentOpenPacket(player.getUUID()), (ServerPlayer)oPlayer);
    }
    
}