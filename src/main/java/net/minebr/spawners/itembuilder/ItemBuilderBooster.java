package net.minebr.spawners.itembuilder;

import de.tr7zw.changeme.nbtapi.NBTItem;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.itembuilder.builder.ItemBuilder;
import net.minebr.spawners.itembuilder.builder.SkullBuilder;
import net.minebr.spawners.object.config.BoosterConfig;
import net.minebr.spawners.utils.NumberFormat;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.stream.Collectors;

public class ItemBuilderBooster {

    public static void createBoosterItem(Player player, String boosterKey) {
        BoosterConfig boosterObject = SpawnersMain.getPlugin().getBoostersType().get(boosterKey);

        if (boosterObject == null) {
            player.sendMessage("§cBooster não encontrado: " + boosterKey);
            return;
        }

        ItemBuilder itemBuilder = new ItemBuilder();
        SkullBuilder skullBuilder = new SkullBuilder();

        if (boosterObject.isUseSkull() && !boosterObject.getSkullUrl().isEmpty()) {
            skullBuilder.setTexture(boosterObject.getSkullUrl());
        } else {
            itemBuilder.setType(boosterObject.getMaterial());
        }

        ItemStack item = itemBuilder
                .setName(boosterObject.getName().replace("&", "§"))
                .setLore(boosterObject.getLore().stream()
                        .map(line -> line.replace("&", "§")
                                .replace("{timer}", NumberFormat.numberFormat(boosterObject.getTime()))
                                .replace("{bonus}", NumberFormat.decimalFormat(boosterObject.getMultiply())))
                        .collect(Collectors.toList()))
                .addGlow()
                .build();

        NBTItem nbtItem = new NBTItem(item);
        nbtItem.setString("BOOSTERSPAWNER", boosterKey);
        nbtItem.setInteger("time", boosterObject.getTime());
        nbtItem.setDouble("multiply", boosterObject.getMultiply());

        player.getInventory().addItem(nbtItem.getItem());
    }
}
