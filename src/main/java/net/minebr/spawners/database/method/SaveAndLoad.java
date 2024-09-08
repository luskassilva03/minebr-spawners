package net.minebr.spawners.database.method;

import lombok.val;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.database.datamanager.DataManager;
import net.minebr.spawners.database.datasource.HikariDataSource;
import net.minebr.spawners.database.datasource.MySQLDataSource;
import net.minebr.spawners.database.datasource.SQLiteDataSource;
import net.minebr.spawners.database.exception.DatabaseException;
import net.minebr.spawners.database.util.Utils;
import org.bukkit.Bukkit;

public class SaveAndLoad {

    public static void saveAll(SpawnersMain main) {
        try {
            if (main.getAbstractDataSource() == null) return;
            main.getMainDataManager().saveCached(false);
            main.getAbstractDataSource().close();
        } catch (DatabaseException e) {
            e.printStackTrace();
        }
    }

    public static boolean prepareDatabase(SpawnersMain main) {
        try {
            val databaseType = main.getConfig().getString("Database.type");
            if (databaseType.equalsIgnoreCase("MYSQL_FAST")) {
                main.setAbstractDataSource(new HikariDataSource(main.getConfig().getString("Database.host"), main.getConfig().getString("Database.database"), main.getConfig().getString("Database.user"), main.getConfig().getString("Database.pass")));
            } else if (databaseType.equalsIgnoreCase("MYSQL")) {
                main.setAbstractDataSource(new MySQLDataSource(main.getConfig().getString("Database.host"), main.getConfig().getString("Database.database"), main.getConfig().getString("Database.user"), main.getConfig().getString("Database.pass")));
            } else {
                main.setAbstractDataSource(new SQLiteDataSource());
            }
            main.setMainDataManager(new DataManager(main.getAbstractDataSource()));

            return true;
        } catch (DatabaseException e) {
            Utils.debug(Utils.LogType.INFO, "Erro ao inicializar conexão com banco de dados.");
            e.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(main);
        }
        return false;
    }

}
