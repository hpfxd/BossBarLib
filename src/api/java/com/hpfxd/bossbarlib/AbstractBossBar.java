package com.hpfxd.bossbarlib;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.HandlerList;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public abstract class AbstractBossBar implements BossBar, Listener {
    protected final @NotNull Plugin plugin;
    protected final @NotNull Player player;
    protected @NotNull String text;
    protected float progress;
    private boolean active;
    private @Nullable BukkitTask destroyTask;

    public AbstractBossBar(@NotNull Plugin plugin, @NotNull Player player, @NotNull String text, float progress) {
        this.plugin = plugin;
        this.player = player;
        this.text = text;
        this.progress = progress;
        this.active = true;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @Override
    public @NotNull Player getPlayer() {
        return this.player;
    }

    @Override
    public @NotNull String getText() {
        return this.text;
    }

    @Override
    public void setText(@NotNull String text) {
        if (this.text.equals(text)) return;
        this.updateText(text);
        this.text = text;
    }

    @Override
    public float getProgress() {
        return this.progress;
    }

    @Override
    public void setProgress(float progress) {
        if (this.progress == progress) return;
        if (progress < 0 || progress > 1) {
            throw new IllegalArgumentException("Progress must be in range 0.0-1.0");
        }

        this.updateProgress(progress);
        this.progress = progress;
    }

    @Override
    public boolean isActive() {
        return this.active;
    }

    @Override
    public void destroy() {
        if (!this.active) return;
        this.active = false;
        HandlerList.unregisterAll(this);

        if (this.destroyTask != null) {
            this.destroyTask.cancel();
            this.destroyTask = null;
        }

        this.doDestroy();
    }

    @Override
    public void destroyLater(int ticks) {
        if (this.destroyTask != null) {
            this.destroyTask.cancel();
        }

        this.destroyTask = Bukkit.getScheduler().runTaskLater(this.plugin, this::destroy, ticks);
    }

    protected abstract void updateText(@NotNull String text);

    protected abstract void updateProgress(float progress);

    protected abstract void doDestroy();

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        if (event.getPlayer().equals(this.player)) {
            this.doDestroy();
        }
    }
}
