package net.minebr.spawners.loader;

import lombok.Getter;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.object.config.objects.EconomiesConfig;
import org.bukkit.Bukkit;
import org.bukkit.configuration.ConfigurationSection;

public class Economies {

    @Getter
    private final SpawnersMain main = SpawnersMain.getPlugin();

    public void load() {
        try {
            ConfigurationSection section = main.drops.getConfigurationSection("miniatures");

            if (section != null) {
                Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) Carregando economias...");

                for (String miniature : section.getKeys(false)) {
                    ConfigurationSection priceSection = section.getConfigurationSection(miniature);

                    if (priceSection != null) {
                        for (String economieType : priceSection.getKeys(false)) {
                            String provider = priceSection.getString(economieType + ".provider");
                            double dropPrice = priceSection.getDouble(economieType + ".price");
                            String dropName = priceSection.getString(economieType + ".name").replace("&", "§");

                            EconomiesConfig economy = new EconomiesConfig(miniature, economieType, provider, dropPrice, dropName);
                            main.getEconomiesType().put(miniature, economy);

                            // Mensagem de depuração para cada economia carregada
                            Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) Carregada economia: " + miniature + " com tipo " + provider);
                        }
                    }
                }

                Bukkit.getConsoleSender().sendMessage("§a(minebr-spawners) §fforam carregadas §a" + main.getEconomiesType().size() + " §feconomia(s) em miniature.yml");
            } else {
                Bukkit.getConsoleSender().sendMessage("§c(minebr-spawners) Não foi possível encontrar a seção 'miniatures' no arquivo de configuração.");
            }
        } catch (Exception e) {
            Bukkit.getConsoleSender().sendMessage("§cWarn! há um erro nas economias de sua miniature.yml");
            e.printStackTrace();
        }
    }
}
