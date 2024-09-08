package net.minebr.spawners.itembuilder;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.loader.Miniatures;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.object.config.ValuesItem;
import net.minebr.spawners.itembuilder.builder.ItemBuilder;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;
import java.util.stream.Collectors;

public class ItemGerador {

    static SpawnersMain main = SpawnersMain.getPlugin();

    public static void giveGerador(Player p, String miniature, double amount) {

        ValuesArmorStand miniatureObject = main.getMiniaturesType().get(miniature);
        ValuesItem geradorObject = main.getItemType().get("item");

        double total = amount;

        for (ItemStack stack : p.getInventory().getContents()) {
            if (stack != null) {
                if (stack.getType() == Material.AIR)
                    return;
                NBTItem nbtItem = new NBTItem(stack);
                if (nbtItem.getString("MINIATURE").contains(miniatureObject.getName())) {
                    double miniatureAmount = nbtItem.getInteger("AMOUNT") * stack.getAmount();
                    total += miniatureAmount;
                    p.getInventory().remove(stack);
                }
            }
        }

        List<String> lore = geradorObject.getLore();
        double finalTotal = total;
        List<String> replacedLore = lore.stream().map(s -> s.replace("{amount}", NumberFormat.numberFormat(finalTotal))
                .replace("{miniature}", miniatureObject.getName())).collect(Collectors.toList());
        ItemStack item = new ItemStack((new ItemBuilder())
                .setType(geradorObject.getMaterial())
                .setData(geradorObject.getData())
                .setName(geradorObject.getName().replace("{amount}", NumberFormat.numberFormat(finalTotal)))
                .setLore(replacedLore).build());

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("MINIATURE", miniatureObject.getName());
        nbtItem.setDouble("AMOUNT", finalTotal);
        p.getInventory().addItem(new ItemStack(nbtItem.getItem()));
    }

}
