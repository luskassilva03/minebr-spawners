package net.minebr.spawners.command;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.command.impl.giveKillStack;
import net.minebr.spawners.command.impl.setKillStack;
import net.minebr.spawners.command.impl.addKillStack;
import net.minebr.spawners.registery.CommandRegistry;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

public class KillStackCommand extends CommandRegistry {

    private net.minebr.spawners.command.impl.giveKillStack giveKillStack;
    private addKillStack addKillStack;
    private net.minebr.spawners.command.impl.setKillStack setKillStack;

    public KillStackCommand(SpawnersMain main) {
        super(main);

        this.giveKillStack = new giveKillStack();
        this.addKillStack = new addKillStack();
        this.setKillStack = new setKillStack();
        main.getCommand("killstack").setExecutor(this);
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
                s.sendMessage("§a/killstack give (player) (amount)");
                s.sendMessage("       §7↳ §fentrega em forma de item.");
                s.sendMessage("§a/killstack add (player) (amount)");
                s.sendMessage("       §7↳ §fadiciona um valor na conta.");
                s.sendMessage("§a/killstack set (player) (amount)");
                s.sendMessage("       §7↳ §fseta um valor na conta do player.");
                s.sendMessage("");
                return false;
            }
        }
        if (args.length >= 1) {
            switch (args[0].toLowerCase()) {
                case "give":
                case "enviar":
                    giveKillStack.execute(s, args);
                    break;
                case "add":
                case "adicionar":
                    addKillStack.execute(s, args);
                    break;
                case "set":
                case "setar":
                    setKillStack.execute(s, args);
                    break;
                default:
                case "help":
                case "ajuda":
                    s.sendMessage("");
                    s.sendMessage("§a/killstack give (player) (amount)");
                    s.sendMessage("       §7↳ §fentrega em forma de item.");
                    s.sendMessage("§a/killstack add (player) (amount)");
                    s.sendMessage("       §7↳ §fadiciona um valor na conta.");
                    s.sendMessage("§a/killstack set (player) (amount)");
                    s.sendMessage("       §7↳ §fseta um valor na conta do player.");
                    s.sendMessage("");
                    break;
            }
        }
        return false;
    }
}
