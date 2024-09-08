package net.minebr.spawners.object.config;

import org.bukkit.Material;

import java.util.List;

public class ValuesItem {
    private Material material;
    private byte data;
    private String name;
    private List<String> lore;

    public ValuesItem(Material material, byte data, String name, List<String> lore) {
        this.material = material;
        this.data = data;
        this.name = name;
        this.lore = lore;
    }

    public Material getMaterial() {
        return material;
    }

    public byte getData() {
        return data;
    }

    public String getName() {
        return name;
    }

    public List<String> getLore() {
        return lore;
    }

}
