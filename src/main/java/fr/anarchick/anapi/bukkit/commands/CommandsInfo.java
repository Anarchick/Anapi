package fr.anarchick.anapi.bukkit.commands;

import fr.anarchick.anapi.java.Pair;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Add some Utilities to {@link Commands}
 *
 * Example of how to setup USAGES, REGEX, EXAMPLES and DESCRIPTION :
 *
 * 		USAGES.put("create", "/file [file] create [overwrite]");
 * 		REGEX.put("create", Pattern.compile("\\/file (.*?) (create) (true|false)"));
 * 		EXAMPLES.put("create", "/file 'plugins/log.json' create false");
 * 		DESCRIPTION.put("create", "Create a new file at given path. True/false if you want to overwrite existing file.");
 *
 */
@SuppressWarnings("unused")
public abstract class CommandsInfo extends Commands {

    protected final List<String> ARGUMENTS = new ArrayList<>();
    protected final HashMap<String, String> USAGES = new HashMap<>();
    protected final HashMap<String, Pattern> REGEX = new HashMap<>();
    protected final HashMap<String, String> EXAMPLES = new HashMap<>();
    protected final HashMap<String, String> DESCRIPTION = new HashMap<>();

    /**
     * Send a message containing information about how the use of this command.
     * Required to set the variables USAGES, DESCRIPTION and EXAMPLES.
     *
     * Support PlaceHolderAPI and use MiniMessage format
     * @param sender the receiver
     * @param id the id of USAGES, DESCRIPTION and EXAMPLES
     * @return Always true;
     */
    protected boolean info(CommandSender sender, String id) {
        if (USAGES.containsKey(id)) {
            sendMessage(sender, "<blue>Usage: <gray>" + USAGES.get(id));
        }
        if (DESCRIPTION.containsKey(id)) {
            sendMessage(sender, "<blue>Description: <gray>" + DESCRIPTION.get(id));
        }
        if (EXAMPLES.containsKey(id)) {
            sendMessage(sender, "<blue>Example: <gray>" + EXAMPLES.get(id));
        }
        return true;
    }

    /**
     * Send all informations about the usage of this command.
     * @param sender the receiver
     * @return Always true.
     */
    protected boolean info(CommandSender sender) {
        for (String id : USAGES.keySet()) {
            info(sender, id);
            sendMessage(sender, "");
        }
        return true;
    }

    /**
     * Return the first id and Matcher which correspond to the REGEX.
     *
     * Can impact performance if there are a lot of REGEX to compute
     * @return the id pair with the Matcher.
     */
    @Nullable
    protected Pair<String, Matcher> getMatchPattern() {
        final String fullCommand = getOriginalCommand();
        for (Map.Entry<String, Pattern> entry : REGEX.entrySet()) {
            final Matcher matcher = entry.getValue().matcher(fullCommand);
            if (matcher.find()) {
                return new Pair<>(entry.getKey(), matcher);
            }
        }
        return null;
    }

}
