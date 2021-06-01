package abhigya.alonsoleagueaddon;

import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Locale;

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
    private final String newLeaguePlaceholder = "[oldleague]";
    private final String oldLeaguePlaceholder = "[oldleague]";
    private final String pointsPlaceholder = "[points]";

    public Configuration(FileConfiguration config) {
        this.config = config;
        this.isEnabled = config.getBoolean(ENABLED);
    }

    /**
     * Format text with respecting placeholders
     * <p>
     *
     * @param text      Text input
     * @param player    Player name
     * @param newLeague New League name
     * @param oldLeague Old League name
     * @param points    Player Points
     * @return Formatted text
     */
    public String formatText(String text, String player, String newLeague, String oldLeague, int points) {
        text = toLowerCase(text, playerPlaceholder);
        text = toLowerCase(text, newLeaguePlaceholder);
        text = toLowerCase(text, oldLeaguePlaceholder);
        text = toLowerCase(text, pointsPlaceholder);
        return text.replace(playerPlaceholder, player)
                .replace(newLeaguePlaceholder, newLeague)
                .replace(oldLeaguePlaceholder, oldLeague)
                .replace(pointsPlaceholder, String.valueOf(points));
    }

    /**
     * Check if the league is configured
     * <p>
     *
     * @param league League name
     * @return True if configured, false otherwise
     */
    public boolean isLeagueConfigured(String league) {
        return (config.getKeys(false).contains(league));
    }

    /**
     * Get console commands associated with the league
     * <p>
     *
     * @param league League name
     * @return List of console commands
     */
    public List<String> getConsoleCommands(String league) {
        return config.getStringList(league + CONSOLEKEY);
    }

    /**
     * Get player commands associated with the league
     * <p>
     *
     * @param league League name
     * @return List of player commands
     */
    public List<String> getPlayerCommands(String league) {
        return config.getStringList(league + PLAYERKEY);
    }

    /**
     * Checks of the command is a operator command.
     * <p>
     *
     * @param command Command to check for operator cmd
     * @return Whether the command is operator command
     */
    public boolean isOperatorCommand(String command) {
        return command.startsWith("[OP]") || command.startsWith("[op]") ||
                command.startsWith("[Op]") || command.startsWith("[oP]");
    }

    /**
     * Get messages associated with the league
     * <p>
     *
     * @param league League name
     * @return List of messages
     */
    public List<String> getMessages(String league) {
        return config.getStringList(league + MESSAGEKEY);
    }

    /**
     * Converts only the characters sequence of the given target to lower case.
     *
     * @param string {@code String} where the characters to convert are located
     * @param target Characters sequence reference
     * @return Characters sequence of the given target in the given string converted to lower case
     */
    public static String toLowerCase(String string, String target) {
        return toLowerCase(string, target, Locale.getDefault());
    }

    /**
     * Converts only the characters sequence of the given target to lower case,
     * using the given {@code Locale} rules.
     * <p>
     *
     * @param string {@code String} where the characters to convert are located
     * @param target Characters sequence reference
     * @param locale {@code Locale} rules reference
     * @return Characters sequence of the given target in the given string converted to lower case
     */
    public static String toLowerCase(String string, String target, Locale locale) {
        String lower_case = string.toLowerCase(locale);
        String target_lower_case = target.toLowerCase(locale);

        if (!lower_case.contains(target_lower_case))
            return string;

        char[] chars = string.toCharArray();
        int last_index = 0;

        for (int i = 0; i < lower_case.length(); i++) {
            int current_index = lower_case.indexOf(target_lower_case, last_index);

            if (current_index != -1) {
                int end_index = current_index + target_lower_case.length();
                last_index = end_index;

                for (int j = current_index; j < end_index; j++)
                    chars[j] = lower_case.charAt(j);
            }
        }

        return new String(chars);
    }

    /**
     * Check if the configuration is enabled
     * <p>
     *
     * @return Boolean from config
     */
    public boolean isEnabled() {
        return isEnabled;
    }

    public FileConfiguration getConfig() {
        return config;
    }
}
