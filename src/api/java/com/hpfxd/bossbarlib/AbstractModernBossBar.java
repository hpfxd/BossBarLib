package com.hpfxd.bossbarlib;

import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.jetbrains.annotations.NotNull;

public abstract class AbstractModernBossBar extends AbstractBossBar {
    protected @NotNull BossBarColor color;
    protected @NotNull BossBarStyle style;

    public AbstractModernBossBar(@NotNull Plugin plugin, @NotNull Player player, @NotNull String text, float progress, @NotNull BossBarColor color, @NotNull BossBarStyle style) {
        super(plugin, player, text, progress);
        this.color = color;
        this.style = style;
    }

    @Override
    public @NotNull BossBarColor getColor() {
        return this.color;
    }

    @Override
    public void setColor(@NotNull BossBarColor color) {
        if (this.color == color) return;
        this.updateColor(color);
        this.color = color;
    }

    @Override
    public @NotNull BossBarStyle getStyle() {
        return this.style;
    }

    @Override
    public void setStyle(@NotNull BossBarStyle style) {
        if (this.style == style) return;
        this.updateStyle(style);
        this.style = style;
    }

    protected abstract void updateColor(@NotNull BossBarColor color);
    protected abstract void updateStyle(@NotNull BossBarStyle style);
}
