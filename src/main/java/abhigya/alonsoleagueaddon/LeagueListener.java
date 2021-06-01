package abhigya.alonsoleagueaddon;

import com.alonsoaliaga.alonsoleagues.api.AlonsoLeaguesAPI;
import com.alonsoaliaga.alonsoleagues.api.events.PlayerLeagueChangeEvent;
import com.alonsoaliaga.alonsoleagues.api.events.PlayerRegisterEvent;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

import java.util.List;

public class LeagueListener implements Listener {

    private final Main plugin = Main.getInstance();
    private static Configuration configuration = new Configuration(YamlConfiguration.loadConfiguration(Main.getInstance().getConfigFile()));

    @EventHandler
    public void onLeagueChange(PlayerLeagueChangeEvent e) {
        if (!configuration.isEnabled())
            return;

        Player player = e.getPlayer();

        String newLeague = e.getNewLeague();
        String oldLeague = e.getOldLeague();

        int points = AlonsoLeaguesAPI.getPoints(player.getUniqueId());

        if (!configuration.isLeagueConfigured(newLeague))
            return;

        //Get commands and messages
        List<String> consoleCommands = configuration.getConsoleCommands(newLeague);
        List<String> playerCommands = configuration.getPlayerCommands(newLeague);
        List<String> messages = configuration.getMessages(newLeague);

        //Execute console commands
        for (String cmd : consoleCommands) {
            String command = configuration.formatText(cmd, player.getName(), newLeague, oldLeague, points);
            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
        }

        //Execute player commands
        for (String cmd : playerCommands) {
            String command = configuration.formatText(cmd, player.getName(), newLeague, oldLeague, points);
            if (configuration.isOperatorCommand(command)) {
                command = Configuration.toLowerCase(command, "[op]")
                        .replace("[op]", "").trim();
                if (player.isOp()) {
                    player.performCommand(command);
                } else {
                    try {
                        player.setOp(true);
                        player.performCommand(command);
                    } catch (Exception ignored) {}
                    finally {
                        player.setOp(false);
                    }
                }
                continue;
            }
            player.performCommand(command);
        }

        //Send message to player
        for (String msg : messages) {
            String message = configuration.formatText(msg, player.getName(), newLeague, oldLeague, points);
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    @EventHandler
    public void onDataRegister(PlayerRegisterEvent e) {
        if (!configuration.isEnabled())
            return;

        Player player = e.getPlayer();
        int points = AlonsoLeaguesAPI.getPoints(player.getUniqueId());

        if (!AlonsoLeaguesAPI.isRanked(player.getUniqueId()))
            return;

        if (!configuration.isLeagueConfigured("unranked"))
            return;

        //Get unranked commands and messages if exist
        List<String> consoleCommands = configuration.getConsoleCommands("unranked");
        List<String> playerCommands = configuration.getPlayerCommands("unranked");
        List<String> messages = configuration.getMessages("unranked");

        //Execute console commands
        for (String cmd : consoleCommands) {
            String command = Configuration.toLowerCase(cmd, "[player]")
                    .replace("[player]", player.getName());
            command = Configuration.toLowerCase(command, "[points]")
                    .replace("[points]", String.valueOf(points));

            plugin.getServer().dispatchCommand(plugin.getServer().getConsoleSender(), command);
        }

        //Execute player commands
        for (String cmd : playerCommands) {
            String command = Configuration.toLowerCase(cmd, "[player]")
                    .replace("[player]", player.getName());
            command = Configuration.toLowerCase(command, "[points]")
                    .replace("[points]", String.valueOf(points));

            if (configuration.isOperatorCommand(command)) {
                command = Configuration.toLowerCase(command, "[op]")
                        .replace("[op]", "").trim();
                if (player.isOp()) {
                    player.performCommand(command);
                } else {
                    try {
                        player.setOp(true);
                        player.performCommand(command);
                    } catch (Exception ignored) {}
                    finally {
                        player.setOp(false);
                    }
                }
                continue;
            }
            plugin.getServer().dispatchCommand(player, command);
        }

        //Send message to player
        for (String msg : messages) {
            String message = Configuration.toLowerCase(msg, "[player]")
                    .replace("[player]", player.getName());
            message = Configuration.toLowerCase(message, "[points]")
                    .replace("[points]", String.valueOf(points));

            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }

    /**
     * Reload configuration instance
     */
    public static void reloadConfigInstance() {
        configuration = null;
        configuration = new Configuration(YamlConfiguration.loadConfiguration(Main.getInstance().getConfigFile()));
    }

}
