package com.cstav.genshinstrument.client.gui.screens.options.instrument;

import java.util.function.Supplier;

import com.cstav.genshinstrument.sounds.NoteSound;
import com.cstav.genshinstrument.sounds.ModSounds;

public enum ZitherSoundType {
    OLD(() -> ModSounds.ZITHER_OLD_NOTE_SOUNDS),
    NEW(() -> ModSounds.ZITHER_NEW_NOTE_SOUNDS);

    private Supplier<NoteSound[]> soundArr;
    private ZitherSoundType(final Supplier<NoteSound[]> soundType) {
        this.soundArr = soundType;
    }

    public Supplier<NoteSound[]> soundArr() {
        return soundArr;
    }
}