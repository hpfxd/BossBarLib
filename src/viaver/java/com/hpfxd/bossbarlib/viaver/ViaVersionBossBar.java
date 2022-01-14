package com.hpfxd.bossbarlib.viaver;

import com.hpfxd.bossbarlib.AbstractModernBossBar;
import com.hpfxd.bossbarlib.BossBarColor;
import com.hpfxd.bossbarlib.BossBarStyle;
import com.viaversion.viaversion.api.Via;
import com.viaversion.viaversion.api.legacy.bossbar.BossBar;
import com.viaversion.viaversion.api.legacy.bossbar.BossColor;
import com.viaversion.viaversion.api.legacy.bossbar.BossStyle;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class ViaVersionBossBar extends AbstractModernBossBar {
    private static final @NotNull Map<@NotNull BossBarColor, @NotNull BossColor> COLOR_MAP = new EnumMap<>(BossBarColor.class);
    private static final @NotNull Map<@NotNull BossBarStyle, @NotNull BossStyle> STYLE_MAP = new EnumMap<>(BossBarStyle.class);

    static {
        // Populate color map
        COLOR_MAP.put(BossBarColor.PINK, BossColor.PINK);
        COLOR_MAP.put(BossBarColor.BLUE, BossColor.BLUE);
        COLOR_MAP.put(BossBarColor.RED, BossColor.RED);
        COLOR_MAP.put(BossBarColor.GREEN, BossColor.GREEN);
        COLOR_MAP.put(BossBarColor.YELLOW, BossColor.YELLOW);
        COLOR_MAP.put(BossBarColor.PURPLE, BossColor.PURPLE);
        COLOR_MAP.put(BossBarColor.WHITE, BossColor.WHITE);

        // Populate style map
        STYLE_MAP.put(BossBarStyle.SOLID, BossStyle.SOLID);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_6, BossStyle.SEGMENTED_6);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_10, BossStyle.SEGMENTED_10);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_12, BossStyle.SEGMENTED_12);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_20, BossStyle.SEGMENTED_20);
    }

    private final @NotNull BossBar bossBar;

    public ViaVersionBossBar(@NotNull Plugin plugin, @NotNull Player player, @NotNull String text, float progress, @NotNull BossBarColor color, @NotNull BossBarStyle style) {
        super(plugin, player, text, progress, color, style);
        this.bossBar = Via.getAPI().legacyAPI().createLegacyBossBar(text, progress, COLOR_MAP.get(color), STYLE_MAP.get(style));
        this.bossBar.addPlayer(this.getPlayer().getUniqueId());
    }

    @Override
    protected void updateText(@NotNull String text) {
        this.bossBar.setTitle(text);
    }

    @Override
    protected void updateProgress(float progress) {
        this.bossBar.setHealth(progress);
    }

    @Override
    protected void updateColor(@NotNull BossBarColor color) {
        this.bossBar.setColor(COLOR_MAP.get(color));
    }

    @Override
    protected void updateStyle(@NotNull BossBarStyle style) {
        this.bossBar.setStyle(STYLE_MAP.get(style));
    }

    @Override
    protected void doDestroy() {
        this.bossBar.removePlayer(this.player.getUniqueId());
    }

    public static boolean isSupported(@NotNull Player player) {
        return Via.getAPI().getPlayerVersion(player.getUniqueId()) > 107; // Is the player on 1.9+?
    }
}
