package net.minebr.spawners.database.method;

import lombok.val;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.database.datamanager.DataManager;
import net.minebr.spawners.database.util.Utils;
import org.bukkit.scheduler.BukkitRunnable;

public class AutoSave extends BukkitRunnable {

    private final DataManager dataManager;

    public AutoSave(SpawnersMain main, DataManager dataManager) {
        this.dataManager = dataManager;

        runTaskTimerAsynchronously(main, 20L * 60 * 30, 20L * 60 * 30);
    }

    @Override
    public void run() {
        Utils.debug(Utils.LogType.DEBUG, "Iniciando auto save");
        val before = System.currentTimeMillis();
        val i = dataManager.saveCached(false);
        val now = System.currentTimeMillis();
        val total = now - before;
        Utils.debug(Utils.LogType.INFO, "Auto completo, salvo " + i + " objetos em " + total + "ms.");
    }

}
