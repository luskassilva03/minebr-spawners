package net.minebr.spawners.listener;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.database.datamanager.DataManager;
import net.minebr.spawners.database.util.Utils;
import net.minebr.spawners.object.PlayerManager;
import net.minebr.spawners.registery.ListenerRegistry;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class TrafficPlayer extends ListenerRegistry {


    public TrafficPlayer(SpawnersMain main) {
        super(main);
    }

    @EventHandler
    private void onJoin(PlayerJoinEvent e) {
        Player p = e.getPlayer();
        PlayerManager playerManage = SpawnersMain.getPlugin().getMainDataManager().KILLSTACK.getCached(p.getName());
        if (playerManage == null) {
            PlayerManager killstack = new PlayerManager(p.getName(), 10);
            Utils.async(() -> {
                main.getMainDataManager().KILLSTACK.cache(killstack);
            });
        }
    }
}
