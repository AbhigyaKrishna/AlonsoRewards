package abhigya.alonsoleagueaddon;

import com.alonsoaliaga.alonsoleagues.api.AlonsoLeaguesAPI;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class Commands implements CommandExecutor {

    private final Main plugin = Main.getInstance();

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        //Console only command
        if (sender instanceof Player)
            return false;

        if (args.length == 1) {

            //Reset Config command
            if (args[0].equalsIgnoreCase("resetConfig")) {
                try {
                    plugin.getConfigFile().delete();
                    plugin.loadConfig();
                    LeagueListener.reloadConfigInstance();
                    sender.sendMessage(Main.prefix + ChatColor.GREEN + "Config reset successful!");
                    return true;
                } catch (Exception ex) {
                    sender.sendMessage(Main.prefix + ChatColor.RED + "Something went wrong while resetting config!");
                }
            }
            //Reload command
            else if (args[0].equalsIgnoreCase("reload")) {
                try {
                    plugin.loadConfig();
                    LeagueListener.reloadConfigInstance();
                    sender.sendMessage(Main.prefix + ChatColor.GREEN + "Plugin reloaded!");
                    return true;
                } catch (Exception ex) {
                    sender.sendMessage(Main.prefix + ChatColor.RED + "Something went wrong while reloading plugin!");
                }
            }
            //Import leagues command
            else if (args[0].equalsIgnoreCase("import")) {
                plugin.loadConfig();

                FileConfiguration config = (FileConfiguration) YamlConfiguration.loadConfiguration(plugin.getConfigFile());

                List<String> leagues = AlonsoLeaguesAPI.getLeagues();
                Set<String> configKeys = config.getKeys(false);
                configKeys.remove(Configuration.ENABLED);

                for (String key : leagues) {

                    if (configKeys.contains(key))
                        continue;

                    config.set(key + Configuration.CONSOLEKEY, new String[]{});
                    config.set(key + Configuration.PLAYERKEY, new String[]{});
                    config.set(key + Configuration.MESSAGEKEY, new String[]{});
                }

                try {
                    config.save(plugin.getConfigFile());
                    LeagueListener.reloadConfigInstance();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                sender.sendMessage(Main.prefix + ChatColor.GREEN + "Imported successfully!");
                return true;
            }
            //Help command
            else if (args[0].equalsIgnoreCase("help")) {
                sender.sendMessage(ChatColor.AQUA + "=================== AlonsoLeagues Rewards ===================");
                sender.sendMessage(ChatColor.AQUA + "alonsoreward help: " + ChatColor.YELLOW + "Show help message");
                sender.sendMessage(ChatColor.AQUA + "alonsoreward resetconfig: " + ChatColor.YELLOW + "Reset the config file.");
                sender.sendMessage(ChatColor.AQUA + "alonsoreward import: " + ChatColor.YELLOW + "Import leagues from Alonso League Plugin");
                sender.sendMessage(ChatColor.AQUA + "alonsoreward reload: " + ChatColor.YELLOW + "Reload plugin");
                sender.sendMessage("");
                sender.sendMessage(ChatColor.AQUA + "Aliases: " + ChatColor.YELLOW + "ar, areward, alonsorewards");
                sender.sendMessage(ChatColor.AQUA + "=============================================================");

                return true;
            }
        }
        else {
            sender.sendMessage(ChatColor.AQUA + "=================== AlonsoLeagues Rewards ===================");
            sender.sendMessage(ChatColor.AQUA + "alonsoreward help: " + ChatColor.YELLOW + "Show help message");
            sender.sendMessage(ChatColor.AQUA + "alonsoreward resetconfig: " + ChatColor.YELLOW + "Reset the config file.");
            sender.sendMessage(ChatColor.AQUA + "alonsoreward import: " + ChatColor.YELLOW + "Import leagues from Alonso League Plugin");
            sender.sendMessage(ChatColor.AQUA + "alonsoreward reload: " + ChatColor.YELLOW + "Reload plugin");
            sender.sendMessage("");
            sender.sendMessage(ChatColor.AQUA + "Aliases: " + ChatColor.YELLOW + "ar, areward, alonsorewards");
            sender.sendMessage(ChatColor.AQUA + "=============================================================");
        }

        return false;
    }
}
