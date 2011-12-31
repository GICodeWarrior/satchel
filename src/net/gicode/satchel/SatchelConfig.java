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
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

/**
 * Manages the set of satchels needed for each world, based on the configuration
 * file.
 */
public class SatchelConfig {
  private Plugin plugin;
  private File configFile;
  private Map<String, Set<Satchel>> satchels;

  public SatchelConfig(Plugin plugin, File configFile) {
    this.plugin = plugin;
    this.configFile = configFile;
  }

  public boolean loadSatchels() {
    plugin.debug("Loading: " + configFile.getAbsolutePath());
    FileConfiguration config = YamlConfiguration.loadConfiguration(configFile);

    Set<String> satchelNames = config.getKeys(false);
    satchels = new HashMap<String, Set<Satchel>>();

    for (String satchelName : satchelNames) {
      plugin.debug("Reading config for satchel '" + satchelName + "'");

      List<String> attributes = config.getStringList(satchelName
          + ".attributes");
      Satchel satchel = new Satchel(plugin, satchelName, attributes);

      List<String> worlds = config.getStringList(satchelName + ".worlds");
      for (String world : worlds) {
        Set<Satchel> currentSatchels = satchels.get(world);
        if (currentSatchels == null) {
          currentSatchels = new HashSet<Satchel>();
          satchels.put(world, currentSatchels);
        }

        currentSatchels.add(satchel);
      }
    }

    boolean intersected = false;
    for (Map.Entry<String, Set<Satchel>> entry : satchels.entrySet()) {
      Set<String> attributes = new HashSet<String>();

      for (Satchel satchel : entry.getValue()) {
        if (attributes.removeAll(satchel.getAttributes())) {
          intersected = true;
          plugin.error("Satchel " + satchel.getName()
              + " intersects with another satchel. (world: " + entry.getKey()
              + ")");
        }
        attributes.addAll(satchel.getAttributes());
      }

      Set<String> defaults = new HashSet<String>(Satchel.ALL_ATTRIBUTES);
      defaults.removeAll(attributes);
      if (defaults.size() > 0) {
        entry.getValue().add(createDefaultSatchel(entry.getKey(), defaults));
      }
    }

    return !intersected;
  }

  public Set<Satchel> getSatchelsFor(World world) {
    Set<Satchel> match = satchels.get(world.getUID().toString());
    if (match == null) {
      match = satchels.remove(world.getName());
      satchels.put(world.getUID().toString(), match);
    }

    if (match == null) {
      match = new HashSet<Satchel>();
      match.add(createDefaultSatchel(world.getName(), Satchel.ALL_ATTRIBUTES));
      satchels.put(world.getUID().toString(), match);
    }

    return match;
  }

  private Satchel createDefaultSatchel(String world, Iterable<String> attributes) {
    return new Satchel(plugin, "default-" + world, attributes);
  }
}
