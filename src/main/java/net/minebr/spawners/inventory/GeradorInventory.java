package net.minebr.spawners.inventory;

import com.henryfabio.minecraft.inventoryapi.editor.InventoryEditor;
import com.henryfabio.minecraft.inventoryapi.inventory.impl.simple.SimpleInventory;
import com.henryfabio.minecraft.inventoryapi.item.InventoryItem;
import com.henryfabio.minecraft.inventoryapi.viewer.Viewer;
import net.minebr.spawners.SpawnersMain;
import net.minebr.spawners.itembuilder.builder.ItemBuilder;
import net.minebr.spawners.itembuilder.builder.SkullBuilder;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.object.PlayerManager;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class GeradorInventory extends SimpleInventory {

    private final SpawnersMain main;
    private final MiniatureObject miniatureObject;
    private final InventoryUtils inventoryUtils;

    public GeradorInventory(SpawnersMain main, MiniatureObject miniatureObject) {
        super("minebr.spawners.inv", "&8Spawner(s)", 9 * 5); // 45 slots
        this.main = main;
        this.miniatureObject = miniatureObject;
        this.inventoryUtils = new InventoryUtils(main, main.getSpawnMiniature()); // Inicializa InventoryUtils
        configuration(configuration -> configuration.secondUpdate(1));
    }

    @Override
    protected void update(Viewer viewer, InventoryEditor editor) {
        Player player = viewer.getPlayer();
        PlayerManager playerManager = main.getMainDataManager().KILLSTACK.getCached(player.getName());
        setGlassPanels(editor);

        // Adiciona o item de informações da miniatura
        InventoryItem spawnerInfo = InventoryItem.of((new SkullBuilder())
                        .setTexture("http://textures.minecraft.net/texture/b6e522d918252149e6ede2edf3fe0f2c2c58fee6ac11cb88c617207218ae4595")
                        .setName("§eVocê tem §e§l" + miniatureObject.getAmount() + " §eminiatura(s).")
                        .setLore(
                                new String[]{
                                        "",
                                        "  §e§l❘ §fPertence á §7" + miniatureObject.getOwner(),
                                        "  §e§l❘ §fMiniatura de §7" + miniatureObject.getMiniature(),
                                        "",
                                        "§eVocê pode ter BÔNUS tornando-se §e§lVIP",
                                        "§floja.minebr.net"
                                })
                        .build())
                .defaultCallback(event -> {
                    player.closeInventory();
                    // Lógica para lidar com a interação, se necessário
                });

        boolean isStarted = miniatureObject.isStarted(); // Ajuste conforme a lógica do seu plugin
        InventoryItem status = InventoryItem.of((new ItemBuilder())
                        .setType(Material.POWERED_MINECART)
                        .setName(isStarted ? "§aGerador ligado!" : "§cGerador desligado!")
                        .setLore(
                                new String[]{
                                        "§7Para que o seu gerador funcione, é preciso",
                                        "§7que ele esteja ligado!",
                                        "",
                                        isStarted ? "§cClique para desligar." : "§aClique para ligar."
                                })
                        .build())
                .defaultCallback(event -> {
                    //player.closeInventory();
                    inventoryUtils.handleInventoryClick(player, miniatureObject); // Chama o método para lidar com o clique
                });

        InventoryItem booster = InventoryItem.of((new ItemBuilder())
                        .setType(Material.EXP_BOTTLE)
                        .setName("§aBooster(s).")
                        .setLore(
                                new String[]{
                                        "§7Os boosters servem para multiplicar os seus ganhos,",
                                        "§7Quanto maior a porcentagem, mais ganhos você terá.",
                                        "",
                                        playerManager.getActiveBooster() != null ?
                                                "  §a§l❘ §fBooster: §7" + playerManager.getActiveBoosterType() :
                                                "   §cNenhum booster ativo",
                                        playerManager.getActiveBooster() != null ?
                                                "  §a§l❘ §fTempo Restante: §7" + playerManager.getActiveBooster().getRemainingTime() + " minutos" :
                                                "",
                                        playerManager.getActiveBooster() != null ?
                                                "  §a§l❘ §fPorcentagem: §7" + playerManager.getActiveBooster().getMultiply() + "%" :
                                                "",
                                        "",
                                        "§aAdquira BOOSTERs em nosso site.",
                                        "§floja.minebr.net"
                                })
                        .build())
                .defaultCallback(event -> {
                    player.closeInventory();
                    // Lógica para lidar com a interação, se necessário
                });


        InventoryItem friends = InventoryItem.of((new ItemBuilder())
                        .setType(Material.BOOK_AND_QUILL)
                        .setName("§aSistema de Amigo(s).")
                        .setLore(
                                new String[]{
                                        "§7Liberece acesso a um amigo para poder farmar com você,",
                                        "§7basta apenas adiciona-lo em seu terreno.",
                                        "§7O seu amigo(s) só poderá matar ou adicionar",
                                        "§7mais spawners aos seus, ele não tem acesso",
                                        "§7para remover os spawners.",
                                        "",
                                        "§aPara adicionar use /plot add (player)."
                                })
                        .build())
                .defaultCallback(event -> {
                    player.closeInventory();
                    // Lógica para lidar com a interação, se necessário
                });

        editor.setItem(4, spawnerInfo);
        editor.setItem(22, booster);
        editor.setItem(24, friends);
        editor.setItem(20, status); // Define o item de status
    }

    private void setGlassPanels(InventoryEditor editor) {
        int[] borderSlots = {
                0, 1, 2, 3, 4, 5, 6, 7, 8,
                9, 18, 27, 36,
                17, 26, 35, 44,
                37, 38, 39, 40, 41, 42, 43
        };

        ItemStack glassPanel = createGlassPanel("§ewww.minebr.net");

        for (int slot : borderSlots) {
            if (slot < editor.getInventory().getSize() && (editor.getInventory().getItem(slot) == null || editor.getInventory().getItem(slot).getType() == Material.AIR)) {
                editor.setItem(slot, InventoryItem.of(glassPanel));
            }
        }
    }

    private ItemStack createGlassPanel(String name) {
        return new ItemBuilder()
                .setType(Material.STAINED_GLASS_PANE)
                .setData((byte) 7) // A cor pode ser ajustada conforme necessário
                .setName(name)
                .build();
    }
}
