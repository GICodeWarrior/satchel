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

import java.io.File;
import java.util.Set;
import java.util.logging.Logger;

import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * This plug-in provides multi-world inventory, health, gamemode, and location
 * management.
 */
public class Plugin extends JavaPlugin {
  private boolean debug = true;

  private Logger log;
  private PlayerListener listener;
  private SatchelConfig satchels;

  @Override
  public void onDisable() {
    info("Disabled.");
  }

  @Override
  public void onEnable() {
    debug = getConfig().getBoolean("debug");
    getConfig().options().copyDefaults(true);
    saveConfig();
    
    log = Logger.getLogger("Minecraft");
    listener = new PlayerListener(this);
    satchels = new SatchelConfig(this, new File(this.getDataFolder(),
        "satchels.yml"));

    if (!satchels.loadSatchels()) {
      error("Configuration load failed! Plugin disabled!");
      this.setEnabled(false);
      return;
    }

    listener.registerEvents(this.getServer().getPluginManager());
    info("Enabled.");
  }

  public Set<Satchel> getSatchelsFor(World world) {
    return satchels.getSatchelsFor(world);
  }

  protected void info(String message) {
    log.info("[Satchel] " + message);
  }

  protected void warn(String message) {
    info("==WARNING== " + message);
  }

  protected void debug(String message) {
    if (debug) {
      info("==DEBUG== " + message);
    }
  }

  protected void error(String message) {
    log.severe("[Satchel] ==ERROR==" + message);
  }
}
