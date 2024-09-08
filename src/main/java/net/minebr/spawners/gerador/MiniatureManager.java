package net.minebr.spawners.gerador;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.utils.LocationConverter;
import org.bukkit.Location;

public class MiniatureManager {

    private final SpawnersMain main;

    public MiniatureManager(SpawnersMain main) {
        this.main = main;
    }

    public MiniatureObject getSpawnerByLocation(Location location) {
        return main.getMainDataManager().MINIATURES.getCached().stream()
                .map(miniatureObject -> {
                    Location objLocation = LocationConverter.convertStringToLocation(miniatureObject.getLocation());
                    if (objLocation != null && objLocation.getWorld().equals(location.getWorld())) {
                        // Arredondando as coordenadas para o centro do bloco
                        Location centeredObjLocation = centerLocation(objLocation);
                        Location centeredLocation = centerLocation(location);
                        return isWithinRadius(centeredObjLocation, centeredLocation, 2) ? miniatureObject : null;
                    }
                    return null;
                })
                .filter(obj -> obj != null)
                .findFirst()
                .orElse(null);
    }

    private Location centerLocation(Location location) {
        return new Location(location.getWorld(), location.getBlockX() + 0.5, location.getBlockY(), location.getBlockZ() + 0.5);
    }

    private boolean isWithinRadius(Location location1, Location location2, int radius) {
        return location1.distanceSquared(location2) <= radius * radius;
    }
}
