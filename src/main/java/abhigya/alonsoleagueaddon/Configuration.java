package abhigya.alonsoleagueaddon;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;

public class Configuration {

    //Config labels
    public static String ENABLED = "enabled";
    public static String CONSOLEKEY = ".console-cmd";
    public static String PLAYERKEY = ".player-cmd";
    public static String MESSAGEKEY = ".message";

    private FileConfiguration config;
    private boolean isEnabled;

    //Placeholders
    private final String playerPlaceholder = "[player]";
    private final String newLeaguePlaceholder = "[newleague]";
    private final String oldLeaguePlaceholder = "[oldleague]";
    private final String pointsPlaceholder = "[points]";

    public Configuration(FileConfiguration config) {
        this.config = config;
        this.isEnabled = config.getBoolean(ENABLED);
    }

    /**
     * Format text with respecting placeholders
     * @param text          Text input
     * @param player        Player name
     * @param newLeague     New League name
     * @param oldLeague     Old League name
     * @param points        Player Points
     * @return              Formatted text
     */
    public String formatText(String text, String player, String newLeague, String oldLeague, int points) {
        return text.replaceAll("(?i)" + playerPlaceholder, player)
                .replaceAll("(?i)" + newLeaguePlaceholder, newLeague)
                .replaceAll("(?i)" + oldLeaguePlaceholder, oldLeague)
                .replaceAll("(?i)" + pointsPlaceholder, String.valueOf(points));
    }

    /**
     * Check if the league is configured
     * @param league    League name
     * @return          True if configured, false otherwise
     */
    public boolean isLeagueConfigured(String league) {
        return (config.getKeys(false).contains(league));
    }

    /**
     * Get console commands associated with the league
     * @param league    League name
     * @return          List of console commands
     */
    public List<String> getConsoleCommands(String league) {
        return config.getStringList(league + CONSOLEKEY);
    }

    /**
     * Get player commands associated with the league
     * @param league    League name
     * @return          List of player commands
     */
    public List<String> getPlayerCommands(String league) {
        return config.getStringList(league + PLAYERKEY);
    }

    /**
     * Get messages associated with the league
     * @param league    League name
     * @return          List of messages
     */
    public List<String> getMessages(String league) {
        return config.getStringList(league + MESSAGEKEY);
    }

    /**
     * Check if the configuration is enabled
     * @return  Boolean from config
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
