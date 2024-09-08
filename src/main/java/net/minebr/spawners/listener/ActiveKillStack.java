package net.minebr.spawners.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.database.datamanager.DataManager;
import net.minebr.spawners.object.PlayerManager;
import net.minebr.spawners.registery.ListenerRegistry;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class ActiveKillStack extends ListenerRegistry {


    public ActiveKillStack(SpawnersMain main) {
        super(main);
    }

    @EventHandler
    public void activeKillStack(PlayerInteractEvent e) {
        Player p = e.getPlayer();
        if (p.getItemInHand().getItemMeta() == null)
            return;
        if (p.getItemInHand().getItemMeta().getDisplayName() == null)
            return;
        NBTItem nbtItem = new NBTItem(p.getItemInHand());
        if (e.getAction() == Action.RIGHT_CLICK_AIR || e.getAction() == Action.RIGHT_CLICK_BLOCK) {
            if (nbtItem.getString("KILLSTACK").equals("KILLSTACK")){
                PlayerManager playerManage = main.getMainDataManager().KILLSTACK.getCached(p.getName());
                if (playerManage == null) {
                    p.sendMessage("§cHouve um problema ao carregar os seus dados, por favor relogue!");
                    p.sendMessage("§cCaso o erro persista, por favor contactar algum diretor.");
                    return;
                }
                double amountItem = nbtItem.getDouble("AMOUNT");
                playerManage.setQuantidade(amountItem + playerManage.getQuantidade());
                p.getInventory().removeItem(p.getItemInHand());
                p.sendMessage("§cKill Stack §7▸ §fVocê ativou um cheque com sucesso!");
                p.sendMessage("             §7↳ §fNovo saldo é de §c§l" + NumberFormat.numberFormat(playerManage.getQuantidade()) + "§cx KillStack§f.");

            }
        }
    }
}
