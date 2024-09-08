package net.minebr.spawners.gerador;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.utils.LocationConverter;
import org.bukkit.Location;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;

public class RunnableSpawn extends BukkitRunnable {

    private final SpawnersMain plugin;
    private final MiniatureManager miniatureManager;
    private final SpawnMiniature spawnMiniature;

    public RunnableSpawn(SpawnersMain plugin, MiniatureManager miniatureManager) {
        this.plugin = plugin;
        this.miniatureManager = miniatureManager;
        this.spawnMiniature = new SpawnMiniature(plugin, miniatureManager);
    }

    @Override
    public void run() {
        Collection<MiniatureObject> miniatures = plugin.getMainDataManager().MINIATURES.getCached();

        for (MiniatureObject miniature : miniatures) {
            if (miniature.isStarted()) {
                Location location = LocationConverter.convertStringToLocation(miniature.getLocation());
                double amount = miniature.getAmount();
                String type = miniature.getMiniature();
                updateArmorStand(location, amount, type);
            }
        }
    }

    private void updateArmorStand(Location location, double amount, String type) {
        ValuesArmorStand valuesArmorStand = plugin.getMiniaturesType().get(type);
        spawnMiniature.updateArmorStandName(location, valuesArmorStand.getNameOverhead(), amount);
    }
}
