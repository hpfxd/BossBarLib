package com.hpfxd.bossbarlib;

import com.hpfxd.bossbarlib.legacy.LegacyBossBar;
import com.hpfxd.bossbarlib.modern.ModernBossBar;
import com.hpfxd.bossbarlib.viaver.ViaVersionBossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public class BossBars {
    private static final @NotNull BossBarType MODERN = new BossBarType("modern", (player) -> {
        try {
            Class.forName("org.bukkit.boss.BossBar");
            return true;
        } catch (ClassNotFoundException ignored) {
        }

        return false;
    }, ModernBossBar::new);

    public static final @NotNull BossBarType VIAVERSION = new BossBarType("viaversion", (player) -> {
        try {
            Class.forName("com.viaversion.viaversion.api.legacy.bossbar.BossBar");

            return ViaVersionBossBar.isSupported(player);
        } catch (ClassNotFoundException ignored) {
        }

        return false;
    }, ViaVersionBossBar::new);

    public static final @NotNull BossBarType LEGACY = new BossBarType("legacy", (player) -> {
        try {
            Class.forName("net.minecraft.server.v1_8_R3.EntityWither");
            return true;
        } catch (ClassNotFoundException ignored) {
        }

        return false;
    }, (plugin, player, text, progress, color, style) -> new LegacyBossBar(plugin, player, text, progress));

    private BossBars() {
    }

    // ordered from highest to lowest priority
    private static final @NotNull BossBarType @NotNull [] TYPES = new BossBarType[]{
            MODERN,
            VIAVERSION,
            LEGACY,
    };

    /**
     * Create a new boss bar for a player.
     * <p>
     * This method will automatically return the best {@link BossBar} instance available for the player.
     *
     * @param plugin The plugin creating this boss bar.
     * @param player The player viewing this boss bar.
     * @param text The text that will be shown on this boss bar.
     * @param progress The progress of this boss bar. This is how full the boss bar is,
     *                 from 0.0 to 1.0.
     * @param color The color of this boss bar. If the player is on a legacy version, this will be ignored.
     * @param style The style of this boss bar. If the player is on a legacy version, this will be ignored.
     * @return The new {@link BossBar}.
     * @throws IllegalStateException If no suitable {@link BossBar} instances are able to be used.
     */
    public static @NotNull BossBar create(
            @NotNull Plugin plugin,
            @NotNull Player player,
            @NotNull String text,
            float progress,
            @NotNull BossBarColor color,
            @NotNull BossBarStyle style
    ) {
        for (BossBarType type : TYPES) {
            if (type.isSupported(player)) {
                return type.create(plugin, player, text, progress, color, style);
            }
        }

        throw new IllegalStateException("There are no available BossBar implementations!");
    }
}
