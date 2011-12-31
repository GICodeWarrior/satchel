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

import java.util.Arrays;

import org.bukkit.entity.Player;
import org.bukkit.inventory.PlayerInventory;

/**
 * TODO: Describe type
 */
public class PlayerSaveAction {
  private Plugin plugin;
  private Player player;

  public PlayerSaveAction(Plugin plugin, Player player) {
    this.plugin = plugin;
    this.player = player;
  }

  public void execute() {
    for (Satchel satchel : plugin.getSatchelsFor(player.getWorld())) {
      plugin.debug("Saving satchel '" + satchel.getName() + "' for player '"
          + player.getName() + "'.");
      PlayerStorage storage = new PlayerStorage(plugin, player.getName(),
          satchel.getName());

      PlayerInventory inventory = player.getInventory();
      storage.setArmor(Arrays.asList(inventory.getArmorContents()));

      storage.setBedLocation(player.getBedSpawnLocation());

      storage.setExperience(new PlayerStorage.Experience(player.getLevel(),
          player.getExp()));

      storage.setGameMode(player.getGameMode().getValue());

      storage.setHealth(player.getHealth());

      storage.setHunger(new PlayerStorage.Hunger(player.getFoodLevel(), player
          .getExhaustion(), player.getSaturation()));

      storage.setInventory(Arrays.asList(inventory.getContents()));

      storage.setLocation(player.getLocation());

      storage.save();
    }
  }
}
