package fr.anarchick.anapi.bukkit;

import fr.anarchick.anapi.java.FileUtils;
import fr.anarchick.anapi.java.JsonSerializationManager;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.File;
import java.io.IOException;
import java.time.Instant;
import java.util.HashMap;
import java.util.Map;

public class PersistentCooldown {

    private static final JsonSerializationManager<PersistentCooldown> serializationManager = new JsonSerializationManager<>();

    /**
     *
     * @param file must be a .json
     * @return
     */
    public static PersistentCooldown createInstance(@Nonnull File file) throws IOException {
        if (!file.getName().endsWith(".json")) throw new IllegalArgumentException("File must be a .json");
        PersistentCooldown persistentCooldown;
        if (file.exists()) {
            String json = FileUtils.loadContent(file);
            persistentCooldown = serializationManager.deserialization(json, PersistentCooldown.class);
            persistentCooldown.file = file;
        } else {
            FileUtils.createFile(file);
            persistentCooldown = new PersistentCooldown(file);
        }
        persistentCooldown.clean();
        return persistentCooldown;
    }

    transient private @Nonnull File file;
    private Map<String, Cooldown> PERSISTENT_COOLDOWNS = new HashMap<>();

    private PersistentCooldown(@Nonnull File file) {
        this.file = file;
    }

    public @Nonnull File getFile() {
        return file;
    }

    public @Nullable Cooldown getPersistentCooldown(String key) {
        return PERSISTENT_COOLDOWNS.get(key);
    }

    public Long getRemainingTime(String key) {
        Cooldown cooldown = getPersistentCooldown(key);
        return (cooldown == null) ? 0L : cooldown.getRemainingTime();
    }

    public Cooldown createPersistentCooldown(String key, long ticks) {
        final Cooldown cooldown = new Cooldown(key, ticks);
        PERSISTENT_COOLDOWNS.put(key, cooldown);
        save();
        return cooldown;
    }

    /**
     * Remove all finished cooldowns from the file and memory
     */
    public void clean() {
        Map<String, Cooldown> clean = new HashMap<>();
        for (Cooldown cooldown : PERSISTENT_COOLDOWNS.values()) {
            if (cooldown.getRemainingTime() > 0) {
                clean.put(cooldown.getKey(), cooldown);
            }
        }
        PERSISTENT_COOLDOWNS = clean;
        save();
    }

    private void save() {
        final String json = serializationManager.serialize(this);
        FileUtils.save(file, json);
    }

    public static class Cooldown {
        private final String key;
        private final long start;
        private long end;

        /**
         * Use this constructor will not make this cooldown persistent
         * @param key Unique ID
         * @param ticks in ticks
         */
        public Cooldown(String key, long ticks) {
            this.key = key;
            this.start = Instant.now().toEpochMilli();
            this.end = this.start + ticks*50;
        }

        /**
         * Remaining duration in ticks
         * @return > 0 duration left, <= 0 duration since
         */
        public Long getRemainingTime() {
            return (end - Instant.now().toEpochMilli()) / 50;
        }

        public void add(long ticks) {
            this.end += ticks*50;
        }

        public void remove(long ticks) {
            this.end -= ticks*50;
        }

        public String getKey() {
            return key;
        }

        @Override
        public String toString() {
            return "["+key+"{"+start+";"+end+"}]";
        }

        @Override
        public int hashCode() {
            return (int) (start * end);
        }

    }

}
