package com.cstav.genshinstrument.util;

import static java.util.Map.entry;

import java.util.Map;

import com.cstav.genshinstrument.client.config.ModClientConfigs;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.AbstractInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.notegrid.AbstractGridInstrumentScreen;
import com.cstav.genshinstrument.client.gui.screens.instrument.partial.notegrid.NoteGridButton;
import com.mojang.logging.LogUtils;

public abstract class LabelUtil {
    
    public static final String[]
        DO_RE_MI = {
            "do", "re", "mi", "fa", "so", "la", "ti"
        }
    ;
    public static final char[]
        ABC = {
            'C', 'D', 'E', 'F', 'G', 'A', 'B'
        }
    ;


    // Pitch system implementation
    // Literally could not have been done w/o Specy's instrument app, oh my gosh their method is genius
    // Either that or I'm just too much of a novice on music theory

    /**
     * @implNote Scales map taken from Specy's
     * <a href=https://github.com/Specy/genshin-music/blob/19dfe0e2fb8081508bd61dd47289dcb2d89ad5e3/src/Config.ts#L89>
     * Genshin Music app configs
     * </a>
     */
    public static final Map<String, String[]> NOTE_SCALES = Map.ofEntries(
        entry("Cb", strArr("Cb", "Dbb", "Db", "Ebb", "Eb", "Fb", "Gbb", "Gb", "Abb", "Ab", "Bbb", "Bb")),
        entry("C", strArr("C", "Db", "D", "Eb", "E", "F", "Gb", "G", "Ab", "A", "Bb", "B")),
        entry("C#", strArr("C#", "D", "D#", "E", "E#", "F#", "G", "G#", "A", "A#", "B", "B#")),
        entry("Db", strArr("Db", "Ebb", "Eb", "Fb", "F", "Gb", "Abb", "Ab", "Bbb", "Bb", "Cb", "C")),
        entry("D", strArr("D", "Eb", "E", "F", "F#", "G", "Ab", "A", "Bb", "B", "C", "C#")),
        entry("D#", strArr("D#", "E", "E#", "F#", "F##", "G#", "A", "A#", "B", "B#", "C#", "C##")),
        entry("Eb", strArr("Eb", "Fb", "F", "Gb", "G", "Ab", "Bbb", "Bb", "Cb", "C", "Db", "D")),
        entry("E", strArr("E", "F", "F#", "G", "G#", "A", "Bb", "B", "C", "C#", "D", "D#")),
        entry("E#", strArr("E#", "F#", "F##", "G#", "G##", "A#", "B", "B#", "C#", "C##", "D#", "D##")),
        entry("Fb", strArr("Fb", "Gbb", "Gb", "Abb", "Ab", "Bbb", "Cbb", "Cb", "Dbb", "Db", "Ebb", "Eb")),
        entry("F", strArr("F", "Gb", "G", "Ab", "A", "Bb", "Cb", "C", "Db", "D", "Eb", "E")),
        entry("F#", strArr("F#", "G", "G#", "A", "A#", "B", "C", "C#", "D", "D#", "E", "E#")),
        entry("Gb", strArr("Gb", "Abb", "Ab", "Bbb", "Bb", "Cb", "Dbb", "Db", "Ebb", "Eb", "Fb", "F")),
        entry("G", strArr("G", "Ab", "A", "Bb", "B", "C", "Db", "D", "Eb", "E", "F", "F#")),
        entry("G#", strArr("G#", "A", "A#", "B", "B#", "C#", "D", "D#", "E", "E#", "F#", "F##")),
        entry("Ab", strArr("Ab", "Bbb", "Bb", "Cb", "C", "Db", "Ebb", "Eb", "Fb", "F", "Gb", "G")),
        entry("A", strArr("A", "Bb", "B", "C", "C#", "D", "Eb", "E", "F", "F#", "G", "G#")),
        entry("A#", strArr("A#", "B", "B#", "C#", "C##", "D#", "E", "E#", "F#", "F##", "G#", "G##")),
        entry("Bb", strArr("Bb", "Cb", "C", "Db", "D", "Eb", "Fb", "F", "Gb", "G", "Ab", "A")),
        entry("B", strArr("B", "C", "C#", "D", "D#", "E", "F", "F#", "G", "G#", "A", "A#")),
        entry("B#", strArr("B#", "C#", "C##", "D#", "D##", "E#", "F#", "F##", "G#", "G##", "A#", "A##"))
    );
    public static final int NOTES_PER_SCALE = NOTE_SCALES.get("C").length;

    private static String[] strArr(final String... arr) {
        return arr;
    }
    

    public static String getNoteName(final int pitch, final String[] noteLayout, final int offset) {
        final String baseNote = noteLayout[CommonUtil.wrapAround(offset, noteLayout.length)];

        final String[] scale = NOTE_SCALES.get(baseNote);
        return scale[(CommonUtil.doublyPyWrap(pitch, scale.length))];
    }


    public static String getNoteName(final NoteGridButton gridButton) {
        final AbstractGridInstrumentScreen screen = (AbstractGridInstrumentScreen) gridButton.instrumentScreen;
        return getNoteName(screen.getPitch(), screen.noteLayout(), getNoteOffset(gridButton));
    }
    public static int getNoteOffset(final NoteGridButton gridButton) {
        return gridButton.row + gridButton.column * ((AbstractGridInstrumentScreen)gridButton.instrumentScreen).rows();
    }

    public static String getCutNoteName(final NoteGridButton gridButton) {
        return getCutNoteName(LabelUtil.getNoteName(gridButton));
    }
    public static String getCutNoteName(String noteName) {
        if (ModClientConfigs.ACCURATE_NOTES.get())
            noteName = String.valueOf(noteName.charAt(0));

        return noteName;
    }


    public static int getABCOffset(final char note, final AbstractInstrumentScreen screen) {
        for (int i = 0; i < ABC.length; i++) {
            if (note == ABC[i])
                return i;
        }

        LogUtils.getLogger().warn("Could not get note letter "+note+" for "+screen.getInstrumentId()+"!");
        return 0;
    }
    public static int getABCOffset(final NoteGridButton gridButton) {
        return getABCOffset(getNoteName(gridButton).charAt(0), gridButton.instrumentScreen);
    }

}