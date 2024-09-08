package net.minebr.spawners.tasks;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.object.PlayerManager;
import org.bukkit.scheduler.BukkitRunnable;

public class BoosterUpdater extends BukkitRunnable {
    private final SpawnersMain plugin;

    public BoosterUpdater(SpawnersMain plugin) {
        this.plugin = plugin;
    }

    @Override
    public void run() {
        for (PlayerManager user : plugin.getMainDataManager().KILLSTACK.getAll()) {
            PlayerManager.BoosterInfo booster = user.getActiveBooster();

            if (booster == null) {
                continue; // Continue com o próximo jogador
            }

            if (booster.getActivationTime() == null) {
                continue; // Pular o processamento para este booster
            }

            // Verifica o tempo restante do booster
            int remainingTime = booster.getRemainingTime();

            if (remainingTime <= 0) {
                // Remove o booster se o tempo expirou
                String boosterType = user.getActiveBoosterType();
                if (boosterType != null) {
                    user.removeBooster(boosterType);
                }
            }
        }
    }

    public void start() {
        this.runTaskTimer(plugin, 20L, 20L); // Atualiza a cada segundo (20 ticks)
    }
}
