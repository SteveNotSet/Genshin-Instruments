package com.cstav.genshinstrument.sound.held;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Player;

public record HeldNoteSoundKey(Player player, ResourceLocation baseSoundLocation, int index) {
    @Override
    public boolean equals(Object o) {
        return (o instanceof HeldNoteSoundKey other)
            && other.player.equals(player)
            && other.baseSoundLocation.equals(baseSoundLocation)
            && (other.index == index);
    }

    @Override
    public int hashCode() {
        return player.hashCode() + baseSoundLocation.hashCode() + index;
    }
}