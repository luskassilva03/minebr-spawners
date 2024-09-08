package net.minebr.spawners.loader;

import lombok.Getter;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.object.config.ValuesItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;

import java.util.*;

public class Miniatures {

    @Getter

    private final SpawnersMain main = SpawnersMain.getPlugin();

    public void load() {
        try {
            ConfigurationSection section = main.miniatures.getConfigurationSection("miniatures");

            if (section != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) Carregando miniaturas...");

                for (String name : section.getKeys(false)) {
                    String displayName = section.getString(name + ".name").replace("&", "§");
                    int size = section.getInt(name + ".size");
                    String head = section.getString(name + ".head");
                    String armorColor = section.getString(name + ".colors");

                    ValuesArmorStand miniature = new ValuesArmorStand(name, size, head, displayName, armorColor);
                    main.getMiniaturesType().put(name, miniature);

                    // Mensagem de depuração para cada miniatura carregada
                    Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) Carregada miniatura: " + name);
                }

                Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) §fforam carregadas §a" + main.getMiniaturesType().size() + " §fminiatura(s) em miniature.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-spawners) Não foi possível encontrar a seção 'miniatures' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro nas miniaturas de sua miniature.yml");
            e.printStackTrace();
        }
    }
}
