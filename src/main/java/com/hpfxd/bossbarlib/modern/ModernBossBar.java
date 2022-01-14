package com.hpfxd.bossbarlib.modern;

import com.hpfxd.bossbarlib.AbstractModernBossBar;
import com.hpfxd.bossbarlib.BossBarColor;
import com.hpfxd.bossbarlib.BossBarStyle;
import org.bukkit.Bukkit;
import org.bukkit.boss.BarColor;
import org.bukkit.boss.BarStyle;
import org.bukkit.boss.BossBar;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

import java.util.EnumMap;
import java.util.Map;

public class ModernBossBar extends AbstractModernBossBar {
    private static final @NotNull Map<@NotNull BossBarColor, @NotNull BarColor> COLOR_MAP = new EnumMap<>(BossBarColor.class);
    private static final @NotNull Map<@NotNull BossBarStyle, @NotNull BarStyle> STYLE_MAP = new EnumMap<>(BossBarStyle.class);

    static {
        // Populate color map
        COLOR_MAP.put(BossBarColor.PINK, BarColor.PINK);
        COLOR_MAP.put(BossBarColor.BLUE, BarColor.BLUE);
        COLOR_MAP.put(BossBarColor.RED, BarColor.RED);
        COLOR_MAP.put(BossBarColor.GREEN, BarColor.GREEN);
        COLOR_MAP.put(BossBarColor.YELLOW, BarColor.YELLOW);
        COLOR_MAP.put(BossBarColor.PURPLE, BarColor.PURPLE);
        COLOR_MAP.put(BossBarColor.WHITE, BarColor.WHITE);

        // Populate style map
        STYLE_MAP.put(BossBarStyle.SOLID, BarStyle.SOLID);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_6, BarStyle.SEGMENTED_6);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_10, BarStyle.SEGMENTED_10);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_12, BarStyle.SEGMENTED_12);
        STYLE_MAP.put(BossBarStyle.SEGMENTED_20, BarStyle.SEGMENTED_20);
    }

    private final @NotNull BossBar bossBar;

    public ModernBossBar(@NotNull Plugin plugin, @NotNull Player player, @NotNull String text, float progress, @NotNull BossBarColor color, @NotNull BossBarStyle style) {
        super(plugin, player, text, progress, color, style);
        this.bossBar = Bukkit.createBossBar(text, COLOR_MAP.get(color), STYLE_MAP.get(style));
        this.bossBar.setProgress(progress);
        this.bossBar.addPlayer(player);
    }

    @Override
    protected void updateText(@NotNull String text) {
        this.bossBar.setTitle(text);
    }

    @Override
    protected void updateProgress(float progress) {
        this.bossBar.setProgress(progress);
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
        this.bossBar.removeAll();
    }
}
