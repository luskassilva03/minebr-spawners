package net.minebr.spawners.utils;

import org.bukkit.Bukkit;
import org.bukkit.Location;

public class LocationConverter {


    // Método para converter Location em String
    public static String convertLocationToString(Location loc, boolean asPlayer) {
        if (loc == null || loc.getWorld() == null) { // Verifique se loc e loc.getWorld() não são nulos
            return "";
        }
        // Centralizar coordenadas X e Z no meio do bloco
        String x = String.format("%.1f", loc.getBlockX() + 0.5);
        String y = String.format("%.1f", loc.getY());
        String z = String.format("%.1f", loc.getBlockZ() + 0.5);

        if (asPlayer) {
            return loc.getWorld().getName() + "," + x + "," + y + "," + z + "," + loc.getPitch() + "," + loc.getYaw();
        } else {
            return loc.getWorld().getName() + "," + x + "," + y + "," + z;
        }
    }



    // Método para converter String em Location
    public static Location convertStringToLocation(String locString) {
        if (locString == null || locString.isEmpty()) {
            return null;
        }
        String[] parts = locString.split(",");
        if (parts.length == 4) { // Sem yaw e pitch
            String world = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            return new Location(Bukkit.getServer().getWorld(world), x, y, z);
        } else if (parts.length == 6) { // Com yaw e pitch
            String world = parts[0];
            double x = Double.parseDouble(parts[1]);
            double y = Double.parseDouble(parts[2]);
            double z = Double.parseDouble(parts[3]);
            float pitch = Float.parseFloat(parts[4]);
            float yaw = Float.parseFloat(parts[5]);
            return new Location(Bukkit.getServer().getWorld(world), x, y, z, yaw, pitch);
        }
        return null;
    }
}
