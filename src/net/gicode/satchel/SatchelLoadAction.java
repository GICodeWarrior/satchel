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

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Loads the satchel data for a specific player.
 */
public class SatchelLoadAction {
  private Plugin plugin;
  private Player player;
  private Set<Satchel> previousSatchels;

  public SatchelLoadAction(Plugin plugin, Player player, World previousWorld) {
    this.plugin = plugin;
    this.player = player;

    if (previousWorld != null) {
      previousSatchels = plugin.getSatchelsFor(previousWorld);
    }
    else {
      previousSatchels = new HashSet<Satchel>();
    }
  }

  public void execute() {
    for (Satchel satchel : plugin.getSatchelsFor(player.getWorld())) {
      if (previousSatchels.contains(satchel)) {
        plugin.debug("Skipping satchel '" + satchel.getName()
            + "' for player '" + player.getName()
            + "'. (shared with previous world)");
        continue;
      }

      plugin.debug("Loading satchel '" + satchel.getName() + "' for player '"
          + player.getName() + "'.");
      SatchelStorage storage = new SatchelStorage(plugin, player.getName(),
          satchel.getName());
      PlayerInventory inventory = player.getInventory();

      for (String attribute : satchel.getAttributes()) {
        if (attribute.equals("armor")) {
          List<ItemStack> stacks = storage.getArmor();
          if (stacks == null) {
            inventory.setArmorContents(null);
          }
          else {
            inventory.setArmorContents(stacks.toArray(new ItemStack[0]));
          }
        }
        else if (attribute.equals("bed-location")) {
          // TODO: player.setBedSpawnLocation(storage.getBedLocation());
        }
        else if (attribute.equals("experience")) {
          SatchelStorage.Experience experience = storage.getExperience();
          player.setLevel(experience.getLevel());
          player.setExp(experience.getPartial());
        }
        else if (attribute.equals("gamemode")) {
          player.setGameMode(GameMode.getByValue(storage.getGameMode()));
        }
        else if (attribute.equals("health")) {
          player.setHealth(storage.getHealth());
        }
        else if (attribute.equals("hunger")) {
          SatchelStorage.Hunger hunger = storage.getHunger();
          player.setFoodLevel(hunger.getFoodLevel());
          player.setExhaustion(hunger.getExhaustion());
          player.setSaturation(hunger.getSaturation());
        }
        else if (attribute.equals("inventory")) {
          List<ItemStack> stacks = storage.getInventory();
          if (stacks == null) {
            inventory.clear();
          }
          else {
            inventory.setContents(stacks.toArray(new ItemStack[0]));
          }
        }
        else if (attribute.equals("location")) {
          Location location = storage.getLocation();
          if (location == null) {
            plugin.debug("Skipping location in satchel '" + satchel.getName()
                + "' for player '" + player.getName() + "'. (first visit)");
          }
          else {
            player.teleport(location);
          }
        }
        else {
          plugin.warn("Don't know how to load satchel attribute '" + attribute
              + "'");
        }
      }
    }
  }
}
