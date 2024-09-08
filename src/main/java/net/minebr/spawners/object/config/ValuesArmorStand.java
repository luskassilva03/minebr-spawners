package net.minebr.spawners.object.config;
import lombok.Getter;

import java.util.HashMap;
import java.util.Map;

//Nome dado a esse tipo de class é Value Object
public class ValuesArmorStand {

    @Getter
    private String name;
    @Getter
    private int size;
    @Getter
    private String head;
    private final String display;
    @Getter
    private String armorColor; // Cor da armadura da miniatura

    // Construtor
    public ValuesArmorStand(String name, int size, String head, String display, String armorColor) {
        this.name = name;
        this.size = size;
        this.head = head;
        this.display = display;
        this.armorColor = armorColor;
    }

    // Getters e Setters

    public String getNameOverhead() {
        return display;
    }

    @Override
    public String toString() {
        return "Spawner{" +
                "name='" + name + '\'' +
                ", size=" + size +
                ", head='" + head + '\'' +
                ", nameOverhead='" + display + '\'' +
                ", armorColor='" + armorColor + '\'' +
                '}';
    }
}
