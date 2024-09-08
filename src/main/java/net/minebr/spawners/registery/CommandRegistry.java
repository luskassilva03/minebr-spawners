package net.minebr.spawners.registery;

import net.minebr.spawners.SpawnersMain;
import org.bukkit.command.CommandExecutor;

public abstract class CommandRegistry implements CommandExecutor {

    protected SpawnersMain main;

    public CommandRegistry(SpawnersMain main) {
        this.main = main;
    }
}
