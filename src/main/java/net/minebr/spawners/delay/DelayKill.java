package net.minebr.spawners.delay;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;

public class DelayKill {
    private final Map<Player, Long> playerKillDelays = new HashMap<>();
    private final long delayTime;

    public DelayKill(long delayTime) {
        this.delayTime = delayTime;
    }

    public boolean canKillMob(Player player) {
        long currentTime = System.currentTimeMillis();

        // Verifica se o jogador já pode matar outro mob
        if (!playerKillDelays.containsKey(player) || (currentTime - playerKillDelays.get(player) >= delayTime)) {
            playerKillDelays.put(player, currentTime);
            return true;
        }
        return false;
    }

    public void resetDelay(Player player) {
        playerKillDelays.remove(player);
    }
}
