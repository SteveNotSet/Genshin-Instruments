package com.cstav.genshinstrument.event;

import com.cstav.genshinstrument.networking.packet.instrument.NoteSoundMetadata;
import com.cstav.genshinstrument.sound.NoteSound;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraftforge.eventbus.api.Cancelable;

/**
 * An event fired when a {@link NoteSound} has been produced.
 * This event is fired on the Forge event bus
 */
@Cancelable
public class NoteSoundPlayedEvent extends InstrumentPlayedEvent<NoteSound> {
    public NoteSoundPlayedEvent(Level level, NoteSound sound, NoteSoundMetadata soundMeta) {
        super(level, sound, soundMeta);
    }

    @Cancelable
    public static class ByPlayer extends InstrumentPlayedEvent.ByPlayer<NoteSound> {
        public ByPlayer(Player player, NoteSound sound, NoteSoundMetadata soundMeta) {
            super(player, sound, soundMeta);
        }
    }
}