package net.minebr.spawners.listener;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.database.datamanager.DataManager;
import net.minebr.spawners.database.util.Utils;
import net.minebr.spawners.gerador.MiniatureManager;
import net.minebr.spawners.gerador.SpawnMiniature;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.registery.ListenerRegistry;
import net.minebr.spawners.utils.LocationConverter;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.inventory.ItemStack;

public class PlaceMiniature extends ListenerRegistry {

    private final SpawnMiniature spawnMiniature;
    private final MiniatureManager miniatureManager;

    public PlaceMiniature(SpawnersMain main) {
        super(main);
        this.miniatureManager = new MiniatureManager(main);
        this.spawnMiniature = new SpawnMiniature(main, this.miniatureManager);
    }

    @EventHandler
    public void placeGerador(BlockPlaceEvent e) {
        ItemStack itemInHand = e.getItemInHand();
        if (itemInHand == null) return;

        NBTItem nbtItem = new NBTItem(itemInHand);
        if (!nbtItem.hasTag("MINIATURE")) return;

        Player player = e.getPlayer();
        String miniatureType = nbtItem.getString("MINIATURE");
        double amountInHand = nbtItem.getDouble("AMOUNT");

        // Verifique se o jogador pode colocar o bloco
        if (!main.getDelayManager().canInteract(player)) {
            player.sendMessage("§cAguarde um momento antes de colocar outro bloco.");
            e.setCancelled(true);
            return;
        }

        Location blockLocation = e.getBlock().getLocation();
        MiniatureObject nearbySpawner = miniatureManager.getSpawnerByLocation(blockLocation);

        if (nearbySpawner != null) {
            handleExistingSpawner(e, player, miniatureType, amountInHand, nearbySpawner);
        } else {
            spawnNewSpawner(blockLocation, player, miniatureType, amountInHand);
        }

        player.playSound(player.getLocation(), Sound.ENDERMAN_TELEPORT, 1.0F, 1.0F);
    }

    private void handleExistingSpawner(BlockPlaceEvent e, Player player, String miniatureType, double amountInHand, MiniatureObject nearbySpawner) {
        e.setCancelled(true);

        if (!nearbySpawner.getOwner().equals(player.getName())) {
            player.sendMessage("§cWarn! você não pode juntar um gerador que não pertence a você!");
            return;
        }

        if (nearbySpawner.getMiniature().equals(miniatureType)) {
            double updatedAmount = nearbySpawner.getAmount() + amountInHand;
            nearbySpawner.setAmount(updatedAmount);
            player.sendMessage("§aSucesso! você agrupou um gerador! " + NumberFormat.numberFormat(updatedAmount));
            Utils.async(() -> main.getMainDataManager().MINIATURES.cache(nearbySpawner));
        }
    }

    private void spawnNewSpawner(Location blockLocation, Player player, String miniatureType, double amountInHand) {
        ValuesArmorStand valuesArmorStand = SpawnersMain.getPlugin().getMiniaturesType().get(miniatureType);

        ValuesArmorStand values = new ValuesArmorStand(
                valuesArmorStand.getName(),
                valuesArmorStand.getSize(),
                valuesArmorStand.getHead(),
                valuesArmorStand.getNameOverhead().replace("{stack}", NumberFormat.numberFormat(0)).replace("&", "§"),
                valuesArmorStand.getArmorColor()
        );

        spawnMiniature.spawnSmallArmorStand(blockLocation, values);

        MiniatureObject newMiniature = new MiniatureObject(
                LocationConverter.convertLocationToString(blockLocation, false),
                player.getDisplayName(),
                miniatureType,
                amountInHand,
                true
        );

        Utils.async(() -> main.getMainDataManager().MINIATURES.cache(newMiniature));
    }
}
