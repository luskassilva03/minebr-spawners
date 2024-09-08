package net.minebr.spawners.utils;

import org.bukkit.Location;
import org.bukkit.block.Block;

import java.util.ArrayList;
import java.util.List;

public class Nearb {

    public static List<Block> getBlocksBetweenPoints(Location l1, Location l2) {
        List<Block> blocks = new ArrayList<>();
        int topBlockX = (l1.getBlockX() < l2.getBlockX()) ? l2.getBlockX() : l1.getBlockX();
        int bottomBlockX = (l1.getBlockX() > l2.getBlockX()) ? l2.getBlockX() : l1.getBlockX();
        int topBlockY = (l1.getBlockY() < l2.getBlockY()) ? l2.getBlockY() : l1.getBlockY();
        int bottomBlockY = (l1.getBlockY() > l2.getBlockY()) ? l2.getBlockY() : l1.getBlockY();
        int topBlockZ = (l1.getBlockZ() < l2.getBlockZ()) ? l2.getBlockZ() : l1.getBlockZ();
        int bottomBlockZ = (l1.getBlockZ() > l2.getBlockZ()) ? l2.getBlockZ() : l1.getBlockZ();
        for (int x = bottomBlockX; x <= topBlockX; x++) {
            for (int y = bottomBlockY; y <= topBlockY; y++) {
                for (int z = bottomBlockZ; z <= topBlockZ; z++) {
                    Block block = l1.getWorld().getBlockAt(x, y, z);
                    blocks.add(block);
                }
            }
        }
        return blocks;
    }
}
