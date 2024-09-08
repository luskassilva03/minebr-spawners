package net.minebr.spawners.listener;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.inventory.GeradorInventory;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.registery.ListenerRegistry;
import net.minebr.spawners.utils.LocationConverter;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;

public class InteractGerador extends ListenerRegistry {

    public InteractGerador(SpawnersMain main) {
        super(main);
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK) return;

        Block clickedBlock = event.getClickedBlock();
        if (clickedBlock == null || clickedBlock.getType() != Material.ENDER_PORTAL_FRAME) return;

        Player player = event.getPlayer();
        String locationString = LocationConverter.convertLocationToString(clickedBlock.getLocation(), false);
        MiniatureObject miniatureObject = main.getMainDataManager().MINIATURES.getCached(locationString);

        if (miniatureObject != null) {
            GeradorInventory inventory = new GeradorInventory(main, miniatureObject);
            inventory.openInventory(player);
            player.playSound(player.getLocation(), Sound.CHICKEN_EGG_POP, 1.0F, 2.0F);
        }
    }
}
