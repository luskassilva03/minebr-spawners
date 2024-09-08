package net.minebr.spawners.registery;

import net.minebr.spawners.SpawnersMain;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;

public class ListenerRegistry implements Listener {

    protected SpawnersMain main;

    public ListenerRegistry(SpawnersMain main) {
        this.main = main;

        Bukkit.getPluginManager().registerEvents(this, main);
    }

}
