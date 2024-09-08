package net.minebr.spawners.gerador;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.itembuilder.builder.SkullBuilder;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.*;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.metadata.MetadataValue;

import java.util.List;
import java.util.stream.Collectors;

public class SpawnMiniature {

    private final SpawnersMain plugin;
    private final MiniatureManager miniatureManager;

    public SpawnMiniature(SpawnersMain plugin, MiniatureManager miniatureManager) {
        this.plugin = plugin;
        this.miniatureManager = miniatureManager;
    }

    public void spawnSmallArmorStand(Location location, ValuesArmorStand values) {

        Location finalLocation = new Location(
                location.getWorld(),
                location.getBlockX() + 0.5, // Centraliza o X
                location.getBlockY() + 0.8, // Ajusta o Y para 0.8 acima do bloco
                location.getBlockZ() + 0.5, // Centraliza o Z
                location.getYaw() + 180, // Ajusta a rotação Yaw
                location.getPitch() // Mantém a rotação Pitch
        );


        ArmorStand armorStand = (ArmorStand) finalLocation.getWorld().spawnEntity(finalLocation, EntityType.ARMOR_STAND);


        // Configurações do ArmorStand
        armorStand.setSmall(values.getSize() == 1); // Define como pequeno se size for 1
        armorStand.setCustomName(values.getNameOverhead()); // Define o nome visível sobre o ArmorStand
        armorStand.setCustomNameVisible(true); // Torna o nome visível
        armorStand.setArms(true);
        armorStand.setGravity(false);
        armorStand.setVisible(false);
        armorStand.setMetadata("MINIATURE",
                new FixedMetadataValue(SpawnersMain.getPlugin(), 0));
        armorStand.setMetadata("MINIATURE_NAME", new FixedMetadataValue(plugin, values.getName()));

        // Configuração da cor da armadura de couro
        String armorColor = values.getArmorColor();
        if (armorColor != null && !armorColor.isEmpty()) {
            // Cria um novo ItemStack para cada peça de armadura de couro
            ItemStack helmet = new ItemStack((new SkullBuilder()).setTexture(values.getHead()).build());
            ItemStack chestplate = new ItemStack(Material.LEATHER_CHESTPLATE);
            ItemStack leggings = new ItemStack(Material.LEATHER_LEGGINGS);
            ItemStack boots = new ItemStack(Material.LEATHER_BOOTS);

            // Obtém a LeatherArmorMeta de cada item
            LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) chestplate.getItemMeta();
            LeatherArmorMeta leggingsMeta = (LeatherArmorMeta) leggings.getItemMeta();
            LeatherArmorMeta bootsMeta = (LeatherArmorMeta) boots.getItemMeta();

            // Converte a cor da armadura de string para Color
            Color color = getColorFromString(armorColor);
            if (color != null) {
                chestplateMeta.setColor(color);
                leggingsMeta.setColor(color);
                bootsMeta.setColor(color);
            } else {
                plugin.getLogger().warning("Cor da armadura inválida: " + armorColor);
            }
            // Aplica a LeatherArmorMeta atualizada de volta aos ItemStacks
            chestplate.setItemMeta(chestplateMeta);
            leggings.setItemMeta(leggingsMeta);
            boots.setItemMeta(bootsMeta);

            // Define as peças de armadura no ArmorStand
            armorStand.setHelmet(helmet);
            armorStand.setChestplate(chestplate);
            armorStand.setLeggings(leggings);
            armorStand.setBoots(boots);
        }
        //plugin.getLogger().info("ArmorStand spawnado com sucesso: " + values.toString());
    }

    private Color getColorFromString(String colorString) {
        switch (colorString.toLowerCase()) {
            case "white":
                return Color.fromRGB(255, 255, 255); // Branco puro
            case "black":
                return Color.fromRGB(25, 25, 25); // Preto escuro
            case "red":
                return Color.fromRGB(255, 85, 85); // Vermelho claro
            case "green":
                return Color.fromRGB(85, 255, 85); // Verde claro
            case "blue":
                return Color.fromRGB(85, 85, 255); // Azul claro
            case "yellow":
                return Color.fromRGB(255, 255, 85); // Amarelo claro
            case "orange":
                return Color.fromRGB(255, 170, 0); // Laranja
            case "purple":
                return Color.fromRGB(170, 0, 255); // Roxo claro
            case "cyan":
                return Color.fromRGB(85, 255, 255); // Ciano claro
            case "pink":
                return Color.fromRGB(255, 85, 255); // Rosa claro
            case "brown":
                return Color.fromRGB(139, 69, 19); // Marrom
            case "gray":
                return Color.fromRGB(128, 128, 128); // Cinza
            default:
                return null;
        }
    }



    public void updateArmorStandName(Location location, String newName, double amountToAdd) {
        // Encontra o ArmorStand na localização especificada
        ArmorStand armorStand = findArmorStandAtLocation(location);
        if (armorStand != null) {
            // Verifica se a metadata "MINIATURE" existe e obtém o valor atual
            if (armorStand.hasMetadata("MINIATURE")) {
                double currentAmount = 0;
                List<MetadataValue> values = armorStand.getMetadata("MINIATURE");
                if (!values.isEmpty() && values.get(0).value() instanceof Double) {
                    currentAmount = values.get(0).asDouble();
                }

                // Soma ao valor atual o amountToAdd fornecido
                double newAmount = currentAmount + amountToAdd;

                // Atualiza a metadata "MINIATURE" com o novo valor somado
                armorStand.setMetadata("MINIATURE", new FixedMetadataValue(plugin, newAmount));

                // Atualiza o nome visível do ArmorStand
                armorStand.setCustomName(newName.replace("{stack}", NumberFormat.numberFormat(newAmount)).replace("&", "§"));
            }
        }
    }


    public ArmorStand findArmorStandAtLocation(Location location) {
        World world = location.getWorld();
        if (world == null) {
            return null; // Retorna null se o mundo não estiver carregado
        }

        // Encontra todas as entidades ArmorStand na localização
        List<ArmorStand> armorStands = world.getNearbyEntities(location, 0.5, 2, 0.5)
                .stream()
                .filter(entity -> entity instanceof ArmorStand)
                .map(entity -> (ArmorStand) entity)
                .collect(Collectors.toList());

        // Procura o ArmorStand exato na localização exata
        for (ArmorStand armorStand : armorStands) {
            Location standLocation = armorStand.getLocation();
            if (standLocation.getBlockX() == location.getBlockX()
                    && standLocation.getBlockY() == location.getBlockY()
                    && standLocation.getBlockZ() == location.getBlockZ()) {
                return armorStand;
            }
        }
        return null; // Retorna null se nenhum ArmorStand for encontrado na localização
    }

    public MetadataValue getMetadata(ArmorStand armorStand, String key) {
        // Obtém um MetadataValue de um ArmorStand com a chave especificada
        if (armorStand.hasMetadata(key)) {
            for (MetadataValue value : armorStand.getMetadata(key)) {
                if (value.getOwningPlugin().equals(plugin)) {
                    return value;
                }
            }
        }
        return null;
    }

    public boolean isSpawnedArmorStand(ArmorStand armorStand) {
        // Verifica se o ArmorStand foi criado pela classe SpawnMiniature
        // Neste exemplo, vamos verificar se o ArmorStand possui um metadata "MINIATURE"
        return armorStand.hasMetadata("MINIATURE");
    }

}
