package net.minebr.spawners.utils;

import org.bukkit.Location;
import org.bukkit.World;

import java.util.Random;

public class RandomLocation {

    public static Location getRandomLocationAroundGerador(Location playerLocation, double radius) {
        // Obter o mundo do jogador
        World world = playerLocation.getWorld();

        // Obter as coordenadas do jogador
        int playerX = playerLocation.getBlockX();
        int playerY = playerLocation.getBlockY();
        int playerZ = playerLocation.getBlockZ();

        // Gerar deslocamentos aleatórios dentro do raio especificado
        Random random = new Random();
        int offsetX = random.nextInt((int) radius * 2) - (int) radius;
        int offsetY = random.nextInt((int) radius * 2) - (int) radius;
        int offsetZ = random.nextInt((int) radius * 2) - (int) radius;

        // Calcular a localização aleatória ao redor do jogador
        int randomX = playerX + offsetX;
        int randomY = playerY + offsetY;
        int randomZ = playerZ + offsetZ;

        // Criar e retornar a localização aleatória
        return new Location(world, randomX + 0.5, randomY - 1, randomZ + 0.5); // Adiciona 0.5 para estar no centro do bloco
    }

}
