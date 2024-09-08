package net.minebr.spawners;

import com.henryfabio.minecraft.inventoryapi.manager.InventoryManager;
import lombok.Getter;
import lombok.Setter;
import net.minebr.spawners.api.ConfigAPI;
import net.minebr.spawners.api.VaultAPI;
import net.minebr.spawners.command.KillStackCommand;
import net.minebr.spawners.command.MiniatureAdminCommand;
import net.minebr.spawners.delay.DelayKill;
import net.minebr.spawners.delay.DelayManager;
import net.minebr.spawners.listener.*;
import net.minebr.spawners.loader.*;
import net.minebr.spawners.database.datamanager.DataManager;
import net.minebr.spawners.database.datasource.AbstractDataSource;
import net.minebr.spawners.database.method.AutoSave;
import net.minebr.spawners.database.method.SaveAndLoad;
import net.minebr.spawners.database.util.Utils;
import net.minebr.spawners.gerador.MiniatureManager;
import net.minebr.spawners.gerador.RunnableSpawn;
import net.minebr.spawners.gerador.SpawnMiniature;
import net.minebr.spawners.object.PlayerManager;
import net.minebr.spawners.object.MiniatureObject;
import net.minebr.spawners.object.config.BonusesConfig;
import net.minebr.spawners.object.config.BoosterConfig;
import net.minebr.spawners.object.config.ValuesArmorStand;
import net.minebr.spawners.object.config.ValuesItem;
import net.minebr.spawners.object.config.objects.EconomiesConfig;
import net.minebr.spawners.tasks.BoosterUpdater;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.entity.ArmorStand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class SpawnersMain extends JavaPlugin {
    @Getter private static SpawnersMain plugin;
    @Setter private AbstractDataSource abstractDataSource;
    @Setter private DataManager mainDataManager;
    private BoosterUpdater boosterUpdater;
    @Getter
    private DelayManager delayManager;
    private DelayKill delayKill;

    public ConfigAPI miniatures;
    public ConfigAPI configAPI;
    public ConfigAPI bonuses;
    public ConfigAPI boosters;
    public ConfigAPI drops;

    private SpawnMiniature spawnMiniature;
    private MiniatureManager miniatureManager;
    private RunnableSpawn runnableSpawn;

    private Map<String, ValuesItem> itemType = new HashMap<>();
    private Map<String, ValuesArmorStand> miniaturesType = new HashMap<>();
    private final Map<String, BonusesConfig> bonusesType = new HashMap<>();
    private final Map<String, BoosterConfig> boostersType = new HashMap<>();
    private final Map<String, EconomiesConfig> economiesType = new HashMap<>();

    @Override
    public void onEnable() {
        plugin = this;
        VaultAPI.hook();

        // Verifica se o mundo "Terrenos" está carregado
        World world = Bukkit.getWorld("Terrenos");
        if (world == null) {
            // Se o mundo não estiver carregado, agenda um atraso
            getLogger().info("O mundo 'Terrenos' não está carregado. Aguardando o carregamento do mundo...");
            new BukkitRunnable() {
                @Override
                public void run() {
                    // Verifica novamente se o mundo foi carregado
                    World delayedWorld = Bukkit.getWorld("Terrenos");
                    if (delayedWorld != null) {
                        getLogger().info("O mundo 'Terrenos' foi carregado. Continuando a inicialização do plugin.");
                        initializePlugin(); // Método para inicializar o plugin
                    } else {
                        getLogger().warning("O mundo 'Terrenos' ainda não está carregado. Tentando novamente em 5 segundos.");
                        this.runTaskLater(SpawnersMain.this, 100); // Reagenda a verificação em 5 segundos (100 ticks)
                    }
                }
            }.runTaskLater(this, 20); // Inicia a verificação após 1 segundo (20 ticks)
        } else {
            // Se o mundo já estiver carregado, inicializa o plugin normalmente
            initializePlugin();
        }
    }

    // Método separado para inicializar o plugin
    private void initializePlugin() {
        miniatures = new ConfigAPI(null, "miniature.yml", false);
        configAPI = new ConfigAPI(null, "config.yml", false);
        bonuses = new ConfigAPI(null, "bonus.yml", false);
        boosters = new ConfigAPI(null, "boosters.yml", false);
        drops = new ConfigAPI(null, "drops.yml", false);

        (new Bonuses()).load();
        (new Boosters()).load();
        (new Miniatures()).load();
        (new MiniaturesItem()).load();
        (new Economies()).load();

        Utils.DEBUGGING = configAPI.getBoolean("Database.Debug");
        if (!SaveAndLoad.prepareDatabase(this)) return;
        MiniatureObject.loadAll(this.mainDataManager.MINIATURES);
        PlayerManager.loadAll(this.mainDataManager.KILLSTACK);

        registerInventorys();

        new ActiveBooster(this);
        new ActiveKillStack(this);
        new EntityDamage(this);
        new InteractGerador(this);
        new PlaceMiniature(this);
        new TrafficPlayer(this);

        this.miniatureManager = new MiniatureManager(this);
        this.spawnMiniature = new SpawnMiniature(this, this.miniatureManager);

        new MiniatureAdminCommand(this);
        new KillStackCommand(this);

        new AutoSave(this, mainDataManager);

        this.runnableSpawn = new RunnableSpawn(this, this.miniatureManager);
        this.runnableSpawn.runTaskTimer(this, 0, 20 * 50);

        boosterUpdater = new BoosterUpdater(this);
        boosterUpdater.start();

        this.delayManager = new DelayManager(2000);
        this.delayKill = new DelayKill(500);
    }

    @Override
    public void onDisable() {
        World world = Bukkit.getWorld("Terrenos");
        List<ArmorStand> armorStands = new ArrayList<>(world.getEntitiesByClass(ArmorStand.class));
        armorStands.forEach(ArmorStand::remove);

        SaveAndLoad.saveAll(this);
    }

    public void registerInventorys() {
        InventoryManager.enable(this);
    }
}
