package net.minebr.spawners.inventory;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.gerador.SpawnMiniature;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.utils.LocationConverter;
import net.minebr.spawners.utils.SendTitle;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.Player;

import java.util.Map;

public class InventoryUtils {

    private final SpawnersMain plugin;
    private final SpawnMiniature spawnMiniature;
    private final Map<String, ValuesArmorStand> miniaturesCache;

    public InventoryUtils(SpawnersMain plugin, SpawnMiniature spawnMiniature) {
        this.plugin = plugin;
        this.spawnMiniature = spawnMiniature;
        this.miniaturesCache = plugin.getMiniaturesType(); // Cache miniaturas
    }

    public void handleInventoryClick(Player player, MiniatureObject miniatureObject) {
        if (miniatureObject == null) return;

        Location location = LocationConverter.convertStringToLocation(miniatureObject.getLocation());
        if (miniatureObject.isStarted()) {
            disableMiniature(player, miniatureObject, location);
        } else {
            enableMiniature(player, miniatureObject, location);
        }
    }

    private void disableMiniature(Player player, MiniatureObject miniatureObject, Location location) {
        miniatureObject.setStarted(false);
        player.closeInventory();
        SendTitle.enviarTitulo(player, "§6§lGERADORES", "§fVocê §cdesabilitou §fo nascimento de miniaturas.", 0, 0, 0);

        World world = location.getWorld();
        if (world != null) {
            world.getNearbyEntities(location, 2, 2, 2).stream()
                    .filter(entity -> entity instanceof ArmorStand)
                    .map(entity -> (ArmorStand) entity)
                    .filter(armorStand -> isNearLocation(armorStand.getLocation(), location))
                    .findFirst() // Encontra a primeira miniatura correspondente
                    .ifPresent(ArmorStand::remove);
        }
    }

    private void enableMiniature(Player player, MiniatureObject miniatureObject, Location location) {
        ValuesArmorStand valuesArmorStand = miniaturesCache.get(miniatureObject.getMiniature());
        if (valuesArmorStand != null) {
            ValuesArmorStand values = new ValuesArmorStand(
                    valuesArmorStand.getName(),
                    valuesArmorStand.getSize(),
                    valuesArmorStand.getHead(),
                    valuesArmorStand.getNameOverhead().replace("{stack}", NumberFormat.numberFormat(0)).replace("&", "§"),
                    valuesArmorStand.getArmorColor()
            );
            spawnMiniature.spawnSmallArmorStand(location, values);

            SendTitle.enviarTitulo(player, "§6§lGERADORES", "§fVocê §ahabilitou §fo nascimento de miniaturas.", 0, 0, 0);

            miniatureObject.setStarted(true);
            player.closeInventory();
        }
    }

    private boolean isNearLocation(Location loc1, Location loc2) {
        return loc1.getWorld().equals(loc2.getWorld())
                && loc1.getBlockX() == loc2.getBlockX()
                && loc1.getBlockY() == loc2.getBlockY()
                && loc1.getBlockZ() == loc2.getBlockZ();
    }
}
