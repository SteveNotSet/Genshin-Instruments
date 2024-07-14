package com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.held;

import com.cstav.genshinstrument.GInstrumentMod;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.InstrumentScreen;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.NoteButton;
import com.cstav.genshinstrument.client.gui.screen.instrument.partial.note.label.NoteLabelSupplier;
import com.cstav.genshinstrument.networking.GIPacketHandler;
import com.cstav.genshinstrument.networking.packet.instrument.c2s.C2SHeldNoteSoundPacketAttack;
import com.cstav.genshinstrument.sound.NoteSound;
import com.cstav.genshinstrument.sound.held.HeldNoteSound;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;

@OnlyIn(Dist.CLIENT)
public abstract class HeldNoteButton extends NoteButton implements IHoldableNoteButton {
    private boolean isHeld = false;
    private HeldNoteSound heldNoteSound;

    public HeldNoteButton(NoteSound sound, NoteLabelSupplier labelSupplier, InstrumentScreen instrumentScreen, HeldNoteSound heldNoteSound) {
        super(sound, labelSupplier, instrumentScreen);
        this.heldNoteSound = heldNoteSound;
    }
    public HeldNoteButton(NoteSound sound, NoteLabelSupplier labelSupplier, InstrumentScreen instrumentScreen, int pitch, HeldNoteSound heldNoteSound) {
        super(sound, labelSupplier, instrumentScreen, pitch);
        this.heldNoteSound = heldNoteSound;
    }

    @Override
    public void setSound(NoteSound sound) {
        GInstrumentMod.LOGGER.warn("Attempted to set the sound of a held note button; ignoring");
    }


    @Override
    public boolean isPlaying() {
        return isHeld();
    }

    @Override
    public boolean isHeld() {
        return isHeld;
    }
    @Override
    public void setHeld(boolean held) {
        isHeld = held;
    }

    @Override
    public HeldNoteSound getHeldNoteSound() {
        return heldNoteSound;
    }

    @Override
    public void setHeldNoteSound(HeldNoteSound heldNoteSound) {
        this.heldNoteSound = heldNoteSound;
    }


    @Override
    protected void playLocalSound(final NoteSound sound, final int pitch) {
        isHeld = true;
        playLocalHeldSound(sound, pitch);
    }
    @Override
    protected void sendNotePlayPacket(NoteSound sound, int pitch) {
        GIPacketHandler.sendToServer(new C2SHeldNoteSoundPacketAttack(this, toHeldSound(sound), pitch));
    }

    @Override
    public void release() {
        releaseHeld(false);
    }
    @Override
    public void releaseHeld(int notePitch, boolean targetPitch, HeldNoteSound heldSound, boolean playAnimation) {
        super.release();
        IHoldableNoteButton.super.releaseHeld(notePitch, targetPitch, heldSound, playAnimation);
    }

    @Override
    protected abstract HeldNoteButtonRenderer initNoteRenderer();
}
