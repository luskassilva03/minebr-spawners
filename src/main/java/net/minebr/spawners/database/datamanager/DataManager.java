package net.minebr.spawners.database.datamanager;

import net.minebr.spawners.database.datasource.AbstractDataSource;
import net.minebr.spawners.object.PlayerManager;
import net.minebr.spawners.object.MiniatureObject;

import java.util.ArrayList;
import java.util.List;

public class DataManager {

    public final CachedDataManager<String, MiniatureObject> MINIATURES;
    public final CachedDataManager<String, PlayerManager> KILLSTACK;

    @SuppressWarnings("rawtypes")
    private List<CachedDataManager> daos;

    public DataManager(AbstractDataSource abstractDataSource) {
        this.daos = new ArrayList<>();
        daos.add(MINIATURES = new CachedDataManager<>(abstractDataSource, "miniatures", MiniatureObject.class));
        daos.add(KILLSTACK = new CachedDataManager<>(abstractDataSource, "player_killstack", PlayerManager.class));
    }

    public int saveCached(boolean async) {
        daos.forEach(dao -> dao.saveCached(async));
        return daos.stream().mapToInt(dao -> dao.getCached().size()).sum();
    }
}