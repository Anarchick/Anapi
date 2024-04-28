package fr.anarchick.anapi.bukkit.config;

import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

@SuppressWarnings("unused")
public class ConfigContainer {

    private FileConfiguration config;
    private final Set<ConfigInfo<?>> configInfos = new HashSet<>();

    public ConfigContainer() {}

    public <T> ConfigInfo<T> of(@NotNull String path, @NotNull T defaultValue) {
        final ConfigInfo<T> configInfo = new ConfigInfo<>(this, path, defaultValue);
        configInfos.add(configInfo);
        return configInfo;
    }

    public void reload(final @NotNull CommandSender sender, @NotNull FileConfiguration config) {
        this.config = config;

        for (final ConfigInfo<?> configInfo : configInfos) {
            configInfo.reload(sender);
        }
    }

    @NotNull
    public FileConfiguration getConfig() {
        return config;
    }

}
