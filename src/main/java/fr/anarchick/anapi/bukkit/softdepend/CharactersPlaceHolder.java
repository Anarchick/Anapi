package fr.anarchick.anapi.bukkit.softdepend;

import fr.anarchick.anapi.bukkit.Characters;
import fr.anarchick.anapi.java.Utils;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.YamlConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

public class CharactersPlaceHolder extends PlaceholderExpansion {

    public static void reload() {
        final File file = Characters.FILE;
        final YamlConfiguration config = YamlConfiguration.loadConfiguration(file);

        for (String id : config.getConfigurationSection("").getKeys(false)) {

            try {
                final String value = config.getString(id);
                final @Nullable Map<Integer, String> groups = Utils.extractGroups(Characters.PATTERN, value);

                if (groups.isEmpty()) {
                    throw new IllegalArgumentException(String.format("Invalid pattern in the config file '%s' , found '%s' instead of something like 'A010-A1FF'", file, value));
                }

                final Characters characters = new Characters(groups.get(1), groups.get(2));
                Characters.MAPPINGS.put(id.toLowerCase(), characters);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

    /**
     * The placeholder identifier of this expansion. May not contain {@literal %},
     * {@literal {}} or _
     *
     * @return placeholder identifier that is associated with this expansion
     */
    @Override
    public @NotNull String getIdentifier() {
        return "character";
    }

    /**
     * The author of this expansion
     *
     * @return name of the author for this expansion
     */
    @Override
    public @NotNull String getAuthor() {
        return "Anarchick";
    }

    /**
     * The version of this expansion
     *
     * @return current version of this expansion
     */
    @Override
    public @NotNull String getVersion() {
        return "1.0.0";
    }

    @Override
    @Nullable
    public String onRequest(final OfflinePlayer player, @NotNull final String params) {
        final Characters characters = Characters.MAPPINGS.get(params.toLowerCase());
        return (characters != null) ? characters.getFirst() : null;
    }


}
