package net.minebr.spawners.loader;

import lombok.Getter;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.object.config.ValuesItem;
import org.bukkit.Bukkit;
import org.bukkit.Material;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MiniaturesItem {


    @Getter
    private final SpawnersMain main = SpawnersMain.getPlugin();

    public void load() {
        try {
            Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) Carregando itens de miniaturas...");

            String[] materialData = main.miniatures.getString("item.material").split(":");
            Material material = Material.getMaterial(Integer.parseInt(materialData[0]));
            byte dataValue = Byte.parseByte(materialData[1]);
            String display = main.miniatures.getString("item.name").replace("&", "§");

            List<String> lore = new ArrayList<>();
            for (String line : main.miniatures.getStringList("item.lore"))
                lore.add(line.replace("&", "§"));

            ValuesItem miniatureItem = new ValuesItem(material, dataValue, display, lore);
            main.getItemType().put("item", miniatureItem);

            // Mensagem de depuração para item de miniatura carregado
            Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) Item de miniatura carregado.");
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro nos itens de sua miniature.yml");
            e.printStackTrace();
        }
    }

}
