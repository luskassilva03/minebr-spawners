package net.minebr.spawners.itembuilder;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.itembuilder.builder.ItemBuilder;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class ItemKillStack {

    static SpawnersMain main = SpawnersMain.getPlugin();

    public static void killstackItem(Player p, double amount) {



        double total = amount;

        for (ItemStack stack : p.getInventory().getContents()) {
            if (stack != null) {
                if (stack.getType() == Material.AIR)
                    return;
                NBTItem nbtItem = new NBTItem(stack);
                if (nbtItem.getString("KILLSTACK").contains("KILLSTACK")) {
                    int amountOfTower = nbtItem.getInteger("AMOUNT") * stack.getAmount();
                    total += amountOfTower;
                    p.getInventory().remove(stack);
                }
            }
        }

        ItemStack item = new ItemStack((new ItemBuilder())
                .setType(Material.NAME_TAG)
                .setName("§c§l" + NumberFormat.numberFormat(total) + "§cx §c§lK§cill §c§lS§ctack.")
                .setLore(
                        "§7Use esse item para poder",
                        "§7matar mais mob(s) com",
                        "§7um único hit.",
                        "",
                        "§cBotão direito para ativar."
                ).build());

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("KILLSTACK", "KILLSTACK");
        nbtItem.setDouble("AMOUNT", total);
        p.getInventory().addItem(new ItemStack(nbtItem.getItem()));
    }

}
