package com.hpfxd.bossbarlib;

import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public interface BossBar {
    /**
     * @return The player this boss bar is for.
     */
    @NotNull Player getPlayer();

    /**
     * @return The text displaying on this boss bar.
     */
    @NotNull String getText();

    /**
     * Set the text to display on this boss bar. Color codes are supported.
     *
     * @param text The new text.
     */
    void setText(@NotNull String text);

    /**
     * @return The progress of this boss bar (how full it is, from 0.0 to 1.0).
     */
    float getProgress();

    /**
     * Set the progress of this boss bar (how full it is, from 0.0 to 1.0).
     * <p>
     * For example, 0.0 would be empty, 0.5 would be half full, and 1.0 would be full.
     *
     * @param progress The new progress.
     * @throws IllegalArgumentException If {@code progress} is outside the acceptable range.
     */
    void setProgress(float progress);

    /**
     * Get the color of this boss bar.
     * <p>
     * On legacy versions, this will always return {@link BossBarColor#PURPLE}.
     *
     * @return The color of this boss bar.
     */
    @NotNull BossBarColor getColor();

    /**
     * Set the color of this boss bar.
     * <p>
     * On legacy versions, this does nothing.
     *
     * @param color The new color.
     */
    void setColor(@NotNull BossBarColor color);

    /**
     * Get the style of this boss bar.
     * <p>
     * On legacy versions, this will always return {@link BossBarStyle#SOLID}.
     *
     * @return The new style.
     */
    @NotNull BossBarStyle getStyle();

    /**
     * Set the style of this boss bar.
     * <p>
     * On legacy versions, this does nothing.
     *
     * @param style The new style.
     */
    void setStyle(@NotNull BossBarStyle style);

    /**
     * Checks whether this boss bar is currently active (not {@link #destroy() destroyed}).
     *
     * @return {@code true} if this boss bar is active.
     * @see #destroy()
     */
    boolean isActive();

    /**
     * Destroys this boss bar, and hides it from the player.
     * <p>
     * Once a boss bar is destroyed, it cannot be re-shown to the player. A new boss bar must be created.
     *
     * @see #isActive()
     */
    void destroy();

    /**
     * Schedule this boss bar for removal at a later time.
     * <p>
     * If this boss bar is already scheduled for removal, the existing task will be cancelled.
     *
     * @param ticks The amount of ticks until the boss bar should be removed.
     */
    void destroyLater(int ticks);
}
