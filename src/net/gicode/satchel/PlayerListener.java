/* 
 * Copyright (c) 2011 Rusty Burchfield
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to
 * deal in the Software without restriction, including without limitation the
 * rights to use, copy, modify, merge, publish, distribute, sublicense, and/or
 * sell copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions: The above copyright
 * notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS
 * IN THE SOFTWARE.
 */
package net.gicode.satchel;

import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerPortalEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.plugin.PluginManager;

/**
 * Registers for events to know when players join and leave worlds. Uses these
 * events to load and save the players satchels.
 */
public class PlayerListener extends org.bukkit.event.player.PlayerListener {
  private Plugin plugin;

  public PlayerListener(Plugin plugin) {
    this.plugin = plugin;
  }

  public void registerEvents(PluginManager manager) {
    Event.Priority priority = Event.Priority.Highest;
    manager.registerEvent(Event.Type.PLAYER_CHANGED_WORLD, this, priority,
        plugin);
    manager.registerEvent(Event.Type.PLAYER_JOIN, this, priority, plugin);
    manager.registerEvent(Event.Type.PLAYER_PORTAL, this, priority, plugin);
    manager.registerEvent(Event.Type.PLAYER_QUIT, this, priority, plugin);
    manager.registerEvent(Event.Type.PLAYER_TELEPORT, this, priority, plugin);
  }

  @Override
  public void onPlayerChangedWorld(PlayerChangedWorldEvent event) {
    debug("changed world", event.getPlayer(), event.getFrom(), null);

    SatchelLoadAction action = new SatchelLoadAction(plugin, event.getPlayer(),
        event.getFrom());
    action.execute();
  }

  @Override
  public void onPlayerJoin(PlayerJoinEvent event) {
    debug("join", event.getPlayer(), null, null);

    SatchelLoadAction action = new SatchelLoadAction(plugin, event.getPlayer(),
        null);
    action.execute();
  }

  @Override
  public void onPlayerPortal(PlayerPortalEvent event) {
    debug("portal", event.getPlayer(), event.getFrom(), event.getTo());

    SatchelSaveAction action = new SatchelSaveAction(plugin, event.getPlayer());
    action.execute();
  }

  @Override
  public void onPlayerQuit(PlayerQuitEvent event) {
    debug("quit", event.getPlayer(), null, null);

    SatchelSaveAction action = new SatchelSaveAction(plugin, event.getPlayer());
    action.execute();
  }

  @Override
  public void onPlayerTeleport(PlayerTeleportEvent event) {
    debug("teleport", event.getPlayer(), event.getFrom(), event.getTo());

    SatchelSaveAction action = new SatchelSaveAction(plugin, event.getPlayer());
    action.execute();
  }

  private void debug(String type, Player player, Object from, Object to) {
    plugin.debug("Got player " + type + " event! (in:" + player.getWorld()
        + ";from:" + from + ";to:" + to + ")");
  }
}
