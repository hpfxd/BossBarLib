package com.hpfxd.bossbarlib.legacy;

import com.hpfxd.bossbarlib.AbstractBossBar;
import com.hpfxd.bossbarlib.BossBarColor;
import com.hpfxd.bossbarlib.BossBarStyle;
import net.minecraft.server.v1_8_R3.EntityWither;
import net.minecraft.server.v1_8_R3.Packet;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityDestroy;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityMetadata;
import net.minecraft.server.v1_8_R3.PacketPlayOutEntityTeleport;
import net.minecraft.server.v1_8_R3.PacketPlayOutSpawnEntityLiving;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitTask;
import org.jetbrains.annotations.NotNull;

public class LegacyBossBar extends AbstractBossBar {
    private final @NotNull CraftPlayer player;
    private final @NotNull EntityWither entity;
    private final @NotNull BukkitTask positionTask;
    private @NotNull World lastUpdateWorld;
    private boolean spawned;

    public LegacyBossBar(@NotNull Plugin plugin, @NotNull Player player, @NotNull String text, float progress) {
        super(plugin, player, text, progress);
        this.player = (CraftPlayer) player;
        this.lastUpdateWorld = this.player.getWorld();
        this.entity = new EntityWither(this.player.getHandle().world);
        this.entity.setInvisible(true);
        this.entity.setCustomName(text);
        this.updateProgress(progress);
        this.entity.r(1000); // r = setInvulnerableTime

        this.spawn();

        this.positionTask = Bukkit.getScheduler().runTaskTimer(this.plugin, this::updatePosition, 0, 10);
    }

    @Override
    protected void updateText(@NotNull String text) {
        this.entity.setCustomName(text);
        this.sendMetadata();
    }

    @Override
    protected void updateProgress(float progress) {
        float actualHealth = progress * this.entity.getMaxHealth();
        this.entity.setHealth(Math.max(0.00001f, actualHealth)); // if health = 0, the wither dies
        this.sendMetadata();
    }

    @Override
    public @NotNull BossBarColor getColor() {
        return BossBarColor.PURPLE;
    }

    @Override
    public void setColor(@NotNull BossBarColor color) {
    }

    @Override
    public @NotNull BossBarStyle getStyle() {
        return BossBarStyle.SOLID;
    }

    @Override
    public void setStyle(@NotNull BossBarStyle style) {
    }

    @Override
    protected void doDestroy() {
        this.sendPacket(new PacketPlayOutEntityDestroy(this.entity.getId()));
        this.spawned = false;
        this.positionTask.cancel();
    }

    private void spawn() {
        this.spawned = true;
        this.sendPacket(new PacketPlayOutSpawnEntityLiving(this.entity));
    }

    private void sendMetadata() {
        this.sendPacket(new PacketPlayOutEntityMetadata(this.entity.getId(), this.entity.getDataWatcher(), true));
    }

    private void updatePosition() {
        World world = this.player.getWorld();
        Location loc = this.player.getLocation();
        LegacyBossBar.getWitherLocation(loc);

        this.entity.setLocation(loc.getX(), loc.getY(), loc.getZ(), 0, 0);
        this.entity.r(1000); // r = setInvulnerableTime

        // Player changed worlds, so respawn the entity.
        if (!this.lastUpdateWorld.equals(world)) {
            this.lastUpdateWorld = world;
            this.spawned = false;
            this.spawn();

            // spawn() will update the position, no need to send teleport packet
            return;
        }

        this.sendPacket(new PacketPlayOutEntityTeleport(this.entity));
    }

    private void sendPacket(@NotNull Packet<?> packet) {
        if (!this.spawned) return; // don't send packets if entity is not spawned
        this.player.getHandle().playerConnection.sendPacket(packet);
    }

    private static void getWitherLocation(@NotNull Location loc) {
        loc.add(loc.getDirection().multiply(40)); // put wither 40 blocks in front of player

        if (loc.getY() > 255) {
            loc.setY(255); // don't let it go above 255
        } else if (loc.getY() < 1) {
            loc.setY(1); // don't let it go below 1
        }
    }
}
