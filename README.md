# BossBarLib
Bukkit library for displaying boss bars to players.

This library was mainly created as a way to create boss bars while also keeping compatibility with 1.8 servers. If your
plugin does not need to support 1.8, you should probably just
use [Bukkit's BossBar API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/boss/BossBar.html).

## How it works

- Uses [Bukkit's BossBar API](https://hub.spigotmc.org/javadocs/spigot/org/bukkit/boss/BossBar.html) when running on
  newer versions.
- If [ViaVersion](https://viaversion.com) is installed, it uses their API to display colored & styled boss bars to
  players running 1.9+.
- If neither of the above options are available (for example if both the server and client are using 1.8), it falls back
  to using NMS to send a fake wither entity to the player. (This wither will be automatically teleported every 10 ticks)

## Setup

**Maven**

```xml

<repositories>
    <repository>
        <id>hpfxd-repo</id>
        <url>https://repo.hpfxd.com</url>
    </repository>
</repositories>

<dependencies>
    <dependency>
        <groupId>com.hpfxd</groupId>
        <artifactId>bossbarlib</artifactId>
        <version>1.0.0</version>
        <scope>compile</scope>
    </dependency>
</dependencies>
```

**Gradle** (kts)

```kotlin
repositories {
    maven(url = "https://repo.hpfxd.com")
}

dependencies {
    implementation("com.hpfxd:bossbarlib:1.0.0")
}
```

Note: Please relocate your dependencies when shading. :)

## Usage

Using the library is pretty straightforward, here's an example for showing a boss bar when a player joins:

```java
@EventHandler
public void onJoin(PlayerJoinEvent event) {
    BossBar bar = BossBars.create(
        yourPluginInstance,
        ChatColor.YELLOW + "Welcome to my server!",
        1f, // This is the "progress" of the boss bar, it can be from 0.0-1.0.
        BossBarColor.YELLOW,
        BossBarStyle.SOLID
    );
    
    // Then, if you want to update the text or progress some time in the future:
    bar.setText(ChatColor.GREEN + "Hello!");
    bar.setProgress(0.5f); // Make the bar half full
        
    // And if you want to destroy the boss bar and hide it from the player:
    bar.destroy();
    
    // There's also bar.destroyLater(ticks) which you can use to destroy the boss bar after a certain amount of time.
}
```

<small>Note that you may want to delay creating the BossBar by a few ticks after a player joins!
This is due to ViaVersion sometimes not returning the player's correct protocol version right away.</small>

Boss bars will be automatically destroyed when the player leaves, as when it is created it registers a handler for the
`PlayerQuitEvent` using the plugin instance it is given. When the bar is destroyed, this listener is removed.
