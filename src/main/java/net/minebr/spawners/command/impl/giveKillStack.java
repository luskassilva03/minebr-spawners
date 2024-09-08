package net.minebr.spawners.command.impl;

import lombok.val;
import net.minebr.spawners.command.utils.ISubCommand;
import net.minebr.spawners.itembuilder.ItemKillStack;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class giveKillStack implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (s.hasPermission("minegeradores.admin")) {
            if (a.length < 3) {
                s.sendMessage("§cVocê está usando menos argumentos que o permitido!");
                s.sendMessage("§cUse: /killstack ajuda");
                return;
            }
            val nick = a[1];
            Player targetPlayer = null;
            targetPlayer = Bukkit.getPlayer(nick);
            if (targetPlayer == null) {
                s.sendMessage("§cUsuário não encontrado!");
                return;
            }
            val amount = a[2];
            ItemKillStack.killstackItem(targetPlayer.getPlayer(), Double.parseDouble(amount));

            s.sendMessage("§cKill Stack §7▸ §fVocê enviou §c§l" + NumberFormat.numberFormat(Double.parseDouble(amount)) + "§cx Kill Stack §fpara este jogador§f.");
            targetPlayer.sendMessage("§cKill Stack §7▸ §fVocê recebeu §c§l" + NumberFormat.numberFormat(Double.parseDouble(amount)) + "§cx Kill Stack§f.");


        }
    }
}
