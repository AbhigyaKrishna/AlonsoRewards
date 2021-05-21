package abhigya.alonsoleagueaddon;

import com.alonsoaliaga.alonsoleagues.api.AlonsoLeaguesAPI;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.SingleLineChart;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.Callable;

public final class Main extends JavaPlugin {

    private static Main instance;

    private File configFile;

    //Plugin prefix
    public static final String prefix = ChatColor.YELLOW + "[AlonsoLeagues] ";
    public static final int pluginId = 11425;

    @Override
    public void onEnable() {
        instance = this;

        //Check for AlonsoLeagues plugin
        if (this.getServer().getPluginManager().getPlugin("AlonsoLeagues") == null) {
            System.out.println(prefix + ChatColor.RED + "The plugin AlonsoLeagues not found!");
            System.out.println(prefix + ChatColor.RED + "This plugin only works with Alonso Leagues plugin.");
            System.out.println(prefix + ChatColor.RED + "Disabling plugin...");
            this.getServer().getPluginManager().disablePlugin(this);
            return;
        }

        metrics();

        loadConfig();

        //Register Commands and Listeners
        System.out.println(prefix + ChatColor.GREEN + "Registering Listener...");
        this.getServer().getPluginManager().registerEvents(new LeagueListener(), this);
        System.out.println(prefix + ChatColor.GREEN + "Registering Command...");
        this.getCommand("alonsoreward").setExecutor(new Commands());
        System.out.println(prefix + ChatColor.AQUA + "Plugin by " + ChatColor.GOLD + "" + ChatColor.BOLD + this.getDescription().getAuthors().get(0));

    }

    /**
     * Create and save configurations
     */
    public void loadConfig() {
        //Create config file
        if (!this.getDataFolder().exists())
            this.getDataFolder().mkdirs();

        System.out.println(prefix + ChatColor.GREEN + "Checking configuration...");
        this.configFile = new File(this.getDataFolder(), "config.yml");

        if (!configFile.exists()) {
            try {
                configFile.createNewFile();

                FileConfiguration config = (FileConfiguration) YamlConfiguration.loadConfiguration(configFile);

                config.set(Configuration.ENABLED, true);

                List<String> leagues = AlonsoLeaguesAPI.getLeagues();

                //Create Configuration
                if (!leagues.isEmpty()) {
                    int i = 1;
                    for (String key : leagues) {
                        if (i == 1) {
                            if (key.equalsIgnoreCase("unranked"))
                                continue;

                            config.set(key + Configuration.CONSOLEKEY, new String[]{"kill [player]",
                                    "broadcast [player] achieved [newleague] with [points] points!"});
                            config.set(key + Configuration.PLAYERKEY, new String[]{"me Yey! I leveled up from [oldleague]"});
                            config.set(key + Configuration.MESSAGEKEY, new String[]{"&6Congratulations! &bYou leveled up to [newleague] league."});
                        } else {
                            config.set(key + Configuration.CONSOLEKEY, new String[]{});
                            config.set(key + Configuration.PLAYERKEY, new String[]{});
                            config.set(key + Configuration.MESSAGEKEY, new String[]{});
                        }
                        i++;
                    }
                } else {
                    config.set("unranked" + Configuration.CONSOLEKEY, new String[]{});
                    config.set("unranked" + Configuration.PLAYERKEY, new String[]{});
                    config.set("unranked" + Configuration.MESSAGEKEY, new String[]{});
                }

                config.save(configFile);
            } catch (IOException e) {
                e.printStackTrace();
                System.out.println(prefix + ChatColor.RED + "Something went wrong config file not created!");
            }
        }
    }

    /**
     * Metrics
     */
    private void metrics() {
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new SingleLineChart("alonsoleague_rewards_players", new Callable<Integer>() {
            @Override
            public Integer call() throws Exception {
                return Bukkit.getOnlinePlayers().size();
            }
        }));

    }

    public static Main getInstance() {
        return instance;
    }

    public File getConfigFile() {
        return configFile;
    }
}
