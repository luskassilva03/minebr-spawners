package net.minebr.spawners.object;

import lombok.Getter;
import lombok.val;
import net.minebr.spawners.database.Keyable;
import net.minebr.spawners.database.datamanager.CachedDataManager;
import net.minebr.spawners.database.util.Utils;

import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PlayerManager implements Keyable<String> {

    private String name;
    private double quantidade;
    @Getter
    private final Map<String, BoosterInfo> boosters; // Map to store booster types and their information

    public PlayerManager(String name, double quantidade) {
        this.name = name;
        this.quantidade = quantidade;
        this.boosters = new HashMap<>();
    }

    public String getName() {
        return name;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public void addBooster(String type, double multiply, int time) {
        BoosterInfo boosterInfo = new BoosterInfo(multiply, time);
        if (boosterInfo.getActivationTime() == null) {
            System.out.println("Erro ao criar BoosterInfo: activationTime é null");
        }
        boosters.put(type, boosterInfo);
    }
    public void removeBooster(String type) {
        boosters.remove(type);
    }

    public BoosterInfo getActiveBooster() {
        return boosters.values().stream().findFirst().orElse(null);
    }

    public String getActiveBoosterType() {
        return boosters.keySet().stream().findFirst().orElse(null);
    }

    @Override
    public String getKey() {
        return name;
    }

    public static void loadAll(CachedDataManager<String, PlayerManager> dao) {
        Utils.measureTime(() -> {
            int i = 0;
            for (PlayerManager playerKillStack : dao.getAll()) {
                if (dao.isCached(playerKillStack.getName())) continue;
                load(playerKillStack.getName(), dao);
                i++;
            }
            return "Carregado " + i + " objetos em {time}";
        });
    }

    public static void load(String name, CachedDataManager<String, PlayerManager> dao) {
        if (dao.exists(name)) {
            val account = dao.find(name);
            dao.cache(account);
        }
    }
    @Getter
    public static class BoosterInfo {
        private final double multiply;
        private final int time; // Tempo em segundos
        private final Instant activationTime;

        public BoosterInfo(double multiply, int time) {
            this.multiply = multiply;
            this.time = time;
            this.activationTime = Instant.now(); // Define o tempo de ativação
        }

        public double getMultiply() {
            return multiply;
        }

        public int getTime() {
            return time;
        }

        public Instant getActivationTime() {
            return activationTime;
        }

        public int getRemainingTime() {
            if (activationTime == null) {
                throw new IllegalStateException("Activation time is null");
            }
            long elapsed = Instant.now().getEpochSecond() - activationTime.getEpochSecond();
            int remaining = time - (int) elapsed;
            return Math.max(remaining, 0);
        }
    }
}
