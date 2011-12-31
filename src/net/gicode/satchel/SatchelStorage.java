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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.inventory.ItemStack;

/**
 * Provides access to save and load satchel data for a specific player.
 */
public class SatchelStorage {
  private static final int DEFAULT_HEALTH = 20;
  private static final int DEFAULT_FOOD_LEVEL = 20;
  private static final float DEFAULT_EXHAUSTION = 0.0f;
  private static final float DEFAULT_SATURATION = 5.0f;

  private Plugin plugin;
  private String player;
  private String satchel;

  private File file;
  private FileConfiguration storage;

  public SatchelStorage(Plugin plugin, String player, String satchel) {
    this.plugin = plugin;
    this.player = player;
    this.satchel = satchel;

    String name = "storage/" + player + "/" + satchel + ".yml";
    file = new File(plugin.getDataFolder(), name);

    load();
  }

  public void load() {
    ConfigurationSerialization.registerClass(ItemStack.class);
    ConfigurationSerialization.registerClass(Location.class);

    storage = YamlConfiguration.loadConfiguration(file);
  }

  public void save() {
    try {
      storage.save(file);
    }
    catch (IOException e) {
      plugin.error("Failed to save satchel '" + satchel + "' for player '"
          + player + "'.");
      e.printStackTrace();
    }
  }

  public List<ItemStack> getArmor() {
    List<Object> raw = storage.getList("armor", null);
    if (raw == null) {
      return null;
    }

    List<ItemStack> stacks = new ArrayList<ItemStack>();
    for (Object stack : raw) {
      stacks.add((ItemStack) stack);
    }
    return stacks;
  }

  public Location getBedLocation() {
    return (Location) storage.get("bed-location");
  }

  public Experience getExperience() {
    int level = storage.getInt("experience.level");
    double partial = storage.getDouble("experience.partial");
    return new Experience(level, (float) partial);
  }

  public int getGameMode() {
    return storage.getInt("gamemode");
  }

  public int getHealth() {
    return storage.getInt("health", DEFAULT_HEALTH);
  }

  public Hunger getHunger() {
    int foodLevel = storage.getInt("hunger.food-level", DEFAULT_FOOD_LEVEL);
    double exhaustion = storage.getDouble("hunger.exhaustion",
        DEFAULT_EXHAUSTION);
    double saturation = storage.getDouble("hunger.saturation",
        DEFAULT_SATURATION);
    return new Hunger(foodLevel, (float) exhaustion, (float) saturation);
  }

  public List<ItemStack> getInventory() {
    List<Object> raw = storage.getList("inventory", null);
    if (raw == null) {
      return null;
    }

    List<ItemStack> stacks = new ArrayList<ItemStack>();
    for (Object stack : raw) {
      stacks.add((ItemStack) stack);
    }
    return stacks;
  }

  public Location getLocation() {
    return (Location) storage.get("location");
  }

  public void setArmor(List<ItemStack> stacks) {
    storage.set("armor", stacks);
  }

  public void setBedLocation(Location location) {
    storage.set("bed-location", location);
  }

  public void setExperience(Experience experience) {
    storage.set("experience.level", experience.getLevel());
    storage.set("experience.partial", experience.getPartial());
  }

  public void setGameMode(int mode) {
    storage.set("gamemode", mode);
  }

  public void setHealth(int health) {
    storage.set("health", health);
  }

  public void setHunger(Hunger hunger) {
    storage.set("hunger.food-level", hunger.getFoodLevel());
    storage.set("hunger.exhaustion", hunger.getExhaustion());
    storage.set("hunger.saturation", hunger.getSaturation());
  }

  public void setInventory(List<ItemStack> stacks) {
    storage.set("inventory", stacks);
  }

  public void setLocation(Location location) {
    storage.set("location", location);
  }

  public static final class Experience {
    private int level;
    private float partial;

    public Experience(int level, float partial) {
      this.level = level;
      this.partial = partial;
    }

    public int getLevel() {
      return level;
    }

    public float getPartial() {
      return partial;
    }
  }

  public static final class Hunger {
    private int foodLevel;
    private float exhaustion;
    private float saturation;

    public Hunger(int foodLevel, float exhaustion, float saturation) {
      this.foodLevel = foodLevel;
      this.exhaustion = exhaustion;
      this.saturation = saturation;
    }

    public int getFoodLevel() {
      return foodLevel;
    }

    public float getExhaustion() {
      return exhaustion;
    }

    public float getSaturation() {
      return saturation;
    }
  }
}
