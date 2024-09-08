package net.minebr.spawners.command;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.command.impl.giveMiniature;
import net.minebr.spawners.command.impl.giveBooster;
import net.minebr.spawners.registery.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class MiniatureAdminCommand extends CommandRegistry {

    private giveMiniature giveMiniature;
    private giveBooster giveBooster;

    public MiniatureAdminCommand(SpawnersMain main) {
        super(main);

        this.giveMiniature = new giveMiniature();
        this.giveBooster = new giveBooster();
        main.getCommand("miniatureadmin").setExecutor(this);
    }

    @Override
    public boolean onCommand(CommandSender s, Command command, String label, String[] args) {
        if (args.length == 0) {
            if (s instanceof Player || s instanceof ConsoleCommandSender) {
                if (!s.hasPermission("minegeradores.admin")) {
                    s.sendMessage("§cVocê precisa ser Gerente ou superior para poder usar este comando!");
                    return false;
                }
                s.sendMessage("");
                s.sendMessage("§a/miniatureadmin giveminiature (player) (miniatura) (quantidade) §7- §fpara enviar um gerador.");
                s.sendMessage("§a/miniatureadmin givebooster (player) (booster) §7- §fpara enviar um booster.");
                return false;
            }
        }
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "giveminiature":
                case "enviarminiature":
                    giveMiniature.execute(s, args);
                    break;
                case "givebooster":
                case "darbooster":
                    giveBooster.execute(s, args);
                    break;
                default:
                case "help":
                case "ajuda":
                    s.sendMessage("");
                    s.sendMessage("§a/miniatureadmin giveminiature (player) (miniatura) (quantidade) §7- §fpara enviar um gerador.");
                    s.sendMessage("§a/miniatureadmin givebooster (player) (booster) §7- §fpara enviar um booster.");
                    break;
            }
        }
        return false;
    }
}

