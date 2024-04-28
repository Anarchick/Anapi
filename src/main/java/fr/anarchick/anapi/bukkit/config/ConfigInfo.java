package fr.anarchick.anapi.bukkit.config;

import fr.anarchick.anapi.java.BiPredicate;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Unmodifiable;

import javax.annotation.Nullable;
import java.util.List;

@SuppressWarnings("unused")
public class ConfigInfo<T> {

    private final ConfigContainer container;
    private final String path;
    private final T defaultValue;
    private T value;
    private List<String> inlineComments = List.of();
    private List<String> comments = List.of();
    private BiPredicate<CommandSender,T> biPredicate = (sender, value) -> true;

    protected ConfigInfo(final @NotNull ConfigContainer container, final @NotNull String path, final @Nullable T defaultValue) {
        this.container = container;
        this.path = path;
        this.defaultValue = defaultValue;
        this.value = defaultValue;
    }

    @NotNull
    private FileConfiguration getConfig() {
        return container.getConfig();
    }

    @NotNull
    public ConfigContainer getConfigContainer() {
        return container;
    }

    @NotNull
    public String getPath() {
        return path;
    }

    public T getValue() {
        return value;
    }

    @Unmodifiable
    @NotNull
    public List<String> getInlineComments() {
        return inlineComments;
    }

    public ConfigInfo<T> setInlineComments(final @NotNull String comments) {
        this.inlineComments = List.of(comments);
        return this;
    }

    @Unmodifiable
    @NotNull
    public List<String> getComments() {
        return inlineComments;
    }

    public ConfigInfo<T> setComments(final @NotNull String... comments) {
        this.comments = List.of(comments);
        return this;
    }

    public void write(final CommandSender sender, final T value) {
        if (isValid(sender, value)) {
            getConfig().set(path, value);
        }
    }

    /**
     * Called when the value is loaded from the config
     * Check if the new value is applicable or not.
     * You should also send a message to the sender if the value is not valid.
     * @param biPredicate the consumer to check the value
     * @return this insatnce
     */
    @NotNull
    public ConfigInfo<T> check(final BiPredicate<CommandSender, T> biPredicate) {
        this.biPredicate = biPredicate;
        return this;
    }

    public boolean isValid(final CommandSender sender, final T value) {
        return biPredicate.test(sender, value);
    }

    protected void reload(final CommandSender sender) {
        try {
            final FileConfiguration config = getConfig();

            if (!inlineComments.isEmpty() && getConfig().getInlineComments(path).isEmpty()) {
                config.setInlineComments(path, inlineComments);
            }

            if (!comments.isEmpty() && getConfig().getComments(path).isEmpty()) {
                config.setInlineComments(path, comments);
            }

            if (!getConfig().isSet(path)) {
                config.set(path, defaultValue);
            } else {
                T temp = (T) config.get(path);

                if (isValid(sender, temp)) {
                    value = temp;
                }

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
