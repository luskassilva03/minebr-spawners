package net.minebr.spawners.functions;

import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.api.VaultAPI;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.object.PlayerManager;
import net.minebr.spawners.object.config.BonusesConfig;
import net.minebr.spawners.object.config.objects.EconomiesConfig;
import net.minebr.spawners.utils.ActionBar;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;

public class SellDrops {

    private final SpawnersMain main = SpawnersMain.getPlugin();

    public void sellDrops(Player p, MiniatureObject miniatureObject, double amount) {
        // Recupera a configuração da economia para a miniatura
        EconomiesConfig economies = main.getEconomiesType().get(miniatureObject.getMiniature());
        if (economies == null) {
            p.sendMessage("§cErro: Economia não encontrada para a miniatura " + miniatureObject.getMiniature());
            return;
        }

        // Recupera o PlayerManager para o jogador
        PlayerManager playerManager = main.getMainDataManager().KILLSTACK.getCached(p.getName());
        if (playerManager == null) {
            p.sendMessage("§cErro: Não foi possível recuperar as informações do jogador.");
            return;
        }

        // Calcula os bônus
        double bonusMultiplier = getMaxBonusMultiplier(p);
        double boosterBonusPercentage = getActiveBoosterBonusPercentage(playerManager);
        double lootingMultiplier = getLootingMultiplier(p);

        // Calcula o preço base e a quantidade efetiva de drops
        double basePrice = economies.getDropPrice() * amount;
        double effectiveAmount = amount * lootingMultiplier;
        double totalPrice = basePrice * lootingMultiplier * (1 + bonusMultiplier / 100) * (1 + boosterBonusPercentage / 100);

        // Mensagem para o jogador
        /*p.sendMessage("§aVocê vendeu " + NumberFormat.numberFormat(effectiveAmount) + " drops do " + economies.getMiniature());
        p.sendMessage("§aPelo preço de " + NumberFormat.numberFormat(totalPrice));
        p.sendMessage("§aSeu bônus: " + bonusMultiplier + "%");
        p.sendMessage("§aSeu booster: " + boosterBonusPercentage + "%");
        p.sendMessage("§aSeu looting: " + lootingMultiplier + "x"); */

        // Monta a mensagem do ActionBar com ou sem o booster
        String actionBarMessage = "§aVocê vendeu " + NumberFormat.numberFormat(effectiveAmount) + " §adrop(s) por: §2$§f" + NumberFormat.numberFormat(totalPrice);
        if (boosterBonusPercentage > 0) {
            actionBarMessage += " §e[" + bonusMultiplier + "%] §f| §b[Booster: " + boosterBonusPercentage + "%]";
        } else {
            actionBarMessage += " §e[" + bonusMultiplier + "%]";
        }
        ActionBar.sendActionText(p, actionBarMessage);
        VaultAPI.econ.depositPlayer(p, totalPrice);
    }

    private double getMaxBonusMultiplier(Player p) {
        double maxBonus = 0.0;
        for (BonusesConfig bonusConfig : main.getBonusesType().values()) {
            if (p.hasPermission(bonusConfig.getPermission())) {
                maxBonus = Math.max(maxBonus, bonusConfig.getBonus());
            }
        }
        return maxBonus;
    }

    private double getActiveBoosterBonusPercentage(PlayerManager playerManager) {
        double totalBonusPercentage = 0.0;
        for (PlayerManager.BoosterInfo boosterInfo : playerManager.getBoosters().values()) {
            if (boosterInfo.getRemainingTime() > 0) {
                totalBonusPercentage += boosterInfo.getMultiply();
            }
        }
        return totalBonusPercentage;
    }

    private double getLootingMultiplier(Player p) {
        // Calcula o bônus do encantamento LOOTING como multiplicador
        int lootingLevel = p.getItemInHand().containsEnchantment(Enchantment.LOOT_BONUS_MOBS)
                ? p.getItemInHand().getEnchantmentLevel(Enchantment.LOOT_BONUS_MOBS)
                : 0;
        return 1 + lootingLevel; // Cada nível de looting aumenta os drops em 1x
    }
}
