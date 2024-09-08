package net.minebr.spawners.command.impl;

import lombok.val;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.command.utils.ISubCommand;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.itembuilder.ItemGerador;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Map;

public class giveMiniature implements ISubCommand {
    @Override
    public void execute(CommandSender s, String[] a) {
        if (s.hasPermission("minegeradores.admin")) {
            if (a.length < 4) {
                s.sendMessage("§cVocê está usando menos argumentos que o permitido!");
                s.sendMessage("§cUse: /miniatureadmin give (player) (miniatura) (quantidade)");
                return;
            }
            val nick = a[1];
            val miniatureName = a[2];
            double quantidade = Integer.parseInt(a[3]);
            Map<String, ValuesArmorStand> miniatures = SpawnersMain.getPlugin().getMiniaturesType();
            if (!miniatures.containsKey(miniatureName)) {
                s.sendMessage("§cErro: Miniatura não encontrada.");
                s.sendMessage("§cDisponíveis: " + miniatures.keySet());
                return;
            }
            Player targetPlayer = null;
            targetPlayer = Bukkit.getPlayer(nick);
            if (targetPlayer == null) {
                s.sendMessage("§cUsuário não encontrado!");
                return;
            }
            ItemGerador.giveGerador(targetPlayer, miniatureName, quantidade);

            s.sendMessage("§a" + quantidade + " " + miniatureName + "(s) dado(s) para " + targetPlayer.getName() + ".");
        }
    }
}
