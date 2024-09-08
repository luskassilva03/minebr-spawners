package net.minebr.spawners.delay;

import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class DelayManager {

    private final Map<Player, Long> playerDelays = new HashMap<>();
    private final long delayTime;

    public DelayManager(long delayTime) {
        this.delayTime = delayTime;
    }

    public boolean canInteract(Player player) {
        long currentTime = System.currentTimeMillis();

        // Verifica se o jogador tem um delay e se já passou tempo suficiente
        if (!playerDelays.containsKey(player) || (currentTime - playerDelays.get(player) >= delayTime)) {
            playerDelays.put(player, currentTime);
            return true;
        }
        return false;
    }

    public void resetDelay(Player player) {
        playerDelays.remove(player);
    }
}
