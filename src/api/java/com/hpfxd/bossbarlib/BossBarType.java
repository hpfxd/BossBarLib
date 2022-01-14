package com.hpfxd.bossbarlib;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.function.Predicate;

public class BossBarType {
    private final @NotNull String id;
    private final @NotNull Predicate<@NotNull Player> predicate;
    private final @NotNull BossBarCreator creator;

    public BossBarType(@NotNull String id, @NotNull Predicate<@NotNull Player> predicate, @NotNull BossBarCreator creator) {
        this.id = id;
        this.predicate = predicate;
        this.creator = creator;
    }

    public @NotNull String getId() {
        return this.id;
    }

    public boolean isSupported(@NotNull Player player) {
        return this.predicate.test(player);
    }

    public @NotNull BossBar create(
            @NotNull Plugin plugin,
            @NotNull Player player,
            @NotNull String text,
            float progress,
            @NotNull BossBarColor color,
            @NotNull BossBarStyle style
    ) {
        return this.creator.create(plugin, player, text, progress, color, style);
    }

    @FunctionalInterface
    public interface BossBarCreator {
        @NotNull BossBar create(
                @NotNull Plugin plugin,
                @NotNull Player player,
                @NotNull String text,
                float progress,
                @NotNull BossBarColor color,
                @NotNull BossBarStyle style
        );
    }
}
