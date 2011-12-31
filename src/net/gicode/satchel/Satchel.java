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
import java.util.HashSet;
import java.util.Set;

/**
 * Manages the name and attributes of a generic satchel.
 */
public class Satchel {
  public static final Set<String> ALL_ATTRIBUTES;

  static {
    // TODO: add "bed-location"
    ALL_ATTRIBUTES = new HashSet<String>(
        Arrays.asList(new String[] { "armor", "experience", "gamemode",
            "health", "hunger", "inventory", "location" }));
  }

  private String name;
  private Set<String> attributes;

  public Satchel(Plugin plugin, String name, Iterable<String> attributes) {
    this.name = name;
    this.attributes = new HashSet<String>();

    for (String attribute : attributes) {
      attribute = attribute.toLowerCase();
      if (ALL_ATTRIBUTES.contains(attribute)) {
        this.attributes.add(attribute);
      }
      else {
        plugin.warn("Ignoring invalid attribute: " + attribute);
      }
    }
  }

  public String getName() {
    return name;
  }

  public Set<String> getAttributes() {
    return attributes;
  }
}
