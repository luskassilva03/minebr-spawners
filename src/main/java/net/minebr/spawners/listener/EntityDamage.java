package net.minebr.spawners.listener;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.functions.SellDrops;
import net.minebr.spawners.gerador.MiniatureManager;
import net.minebr.spawners.gerador.SpawnMiniature;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.object.PlayerManager;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.registery.ListenerRegistry;
import net.minebr.spawners.utils.ActionBar;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.metadata.MetadataValue;

public class EntityDamage extends ListenerRegistry {

    private final MiniatureManager miniatureManager;
    private final SpawnMiniature spawnMiniature;
    private final SellDrops sellDrops;

    public EntityDamage(SpawnersMain main) {
        super(main);
        this.miniatureManager = new MiniatureManager(main);
        this.spawnMiniature = new SpawnMiniature(main, this.miniatureManager);
        this.sellDrops = new SellDrops(); // Instância compartilhada de SellDrops
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof ArmorStand) {
            ArmorStand armorStand = (ArmorStand) event.getEntity();

            if (event.getDamager() instanceof Player) {
                Player player = (Player) event.getDamager();

                if (spawnMiniature.isSpawnedArmorStand(armorStand)) {
                    event.setCancelled(true);

                    // Verifique se o jogador pode matar outro mob
                    if (!main.getDelayKill().canKillMob(player)) {
                        return;
                    }

                    Location blockBelowLocation = armorStand.getLocation().clone().add(0, -1, 0);

                    MiniatureObject miniatureObject = miniatureManager.getSpawnerByLocation(blockBelowLocation);

                    if (miniatureObject != null) {
                        double numberOfArmorStands = getNumberOfArmorStandsInMetadata(armorStand);

                        if (numberOfArmorStands <= 0) return;

                        PlayerManager playerManager = main.getMainDataManager().KILLSTACK.getCached(player.getName());
                        double killstack = playerManager.getQuantidade();

                        if (player.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)) {

                            ValuesArmorStand valuesArmorStand = main.getMiniaturesType().get(miniatureObject.getMiniature());

                            player.playSound(player.getLocation(), Sound.GLASS, 1.0F, 2.0F);
                            player.playEffect(armorStand.getLocation(), Effect.MOBSPAWNER_FLAMES, 1);

                            double amountToRemove = Math.min(numberOfArmorStands, killstack);
                            spawnMiniature.updateArmorStandName(armorStand.getLocation(), valuesArmorStand.getNameOverhead(), -amountToRemove);

                            sellDrops.sellDrops(player, miniatureObject, amountToRemove);

                            return;
                        }
                        player.sendMessage("§cWarn! Por favor, use uma LOOTING para matar essa miniatura.");
                    }
                }
            }
        }
    }

    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.ARMOR_STAND) {
            ArmorStand armorStand = (ArmorStand) event.getRightClicked();

            if (spawnMiniature.isSpawnedArmorStand(armorStand)) {
                event.setCancelled(true);
            }
        }
    }

    private double getNumberOfArmorStandsInMetadata(ArmorStand armorStand) {
        MetadataValue metadataValue = getMetadataValue(armorStand, "MINIATURE");
        return (metadataValue != null && metadataValue.value() instanceof Double) ? metadataValue.asDouble() : 0;
    }

    private MetadataValue getMetadataValue(ArmorStand armorStand, String key) {
        return spawnMiniature.getMetadata(armorStand, key);
    }

    @EventHandler
    public void onPlayerArmorStandManipulate(PlayerArmorStandManipulateEvent event) {
        ArmorStand armorStand = event.getRightClicked();
        if (spawnMiniature.isSpawnedArmorStand(armorStand)) {
            event.setCancelled(true);
        }
    }
}
