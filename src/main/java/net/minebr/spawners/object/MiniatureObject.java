package net.minebr.spawners.object;

import lombok.*;
import net.minebr.spawners.database.Keyable;
import net.minebr.spawners.database.datamanager.CachedDataManager;
import net.minebr.spawners.database.util.Utils;
import net.minebr.spawners.utils.LocationConverter;
import org.bukkit.Bukkit;
import org.bukkit.Location;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class MiniatureObject implements Keyable<String> {

    private String location;
    private String owner;
    private String miniature;
    private double amount;
    private boolean started = false;

    public String getLocation() {
        return location;
    }

    public String getOwner() {
        return owner;
    }

    public String getMiniature() {
        return miniature;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public void setStarted(boolean started) {
        this.started = started;
    }

    public boolean isStarted() {
        return started;
    }

    @Override
    public String getKey() {
        return location;
    }

    public static void loadAll(CachedDataManager<String, MiniatureObject> dao) {
        Utils.measureTime(() -> {
            int i = 0;
            for (MiniatureObject miniatureObject : dao.getAll()) {
                if (dao.isCached(miniatureObject.getLocation())) continue;
                load(LocationConverter.convertStringToLocation(miniatureObject.getLocation()), dao);
                i++;
            }
            return "Carregado " + i + " objetos em {time}";
        });
    }

    public static void load(Location location, CachedDataManager<String, MiniatureObject> dao) {
        if (dao.exists(LocationConverter.convertLocationToString(location, false))) {
            val account = dao.find(LocationConverter.convertLocationToString(location, false));
            dao.cache(account);
            if (account.started) {
                account.started = false;
            }
        }
    }
}
