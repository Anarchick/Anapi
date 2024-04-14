package fr.anarchick.anapi.bukkit.softdepend;

import fr.anarchick.anapi.bukkit.Characters;
import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class CharactersPlaceHolder extends PlaceholderExpansion {

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
        final Characters characters = Characters.MAPPINGS.get(params);
        return (characters != null) ? characters.getFirst() : null;
    }


}
