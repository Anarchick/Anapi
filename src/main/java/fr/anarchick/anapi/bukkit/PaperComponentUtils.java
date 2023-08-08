package fr.anarchick.anapi.bukkit;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.event.HoverEvent;
import net.kyori.adventure.text.minimessage.MiniMessage;
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver;
import net.kyori.adventure.text.minimessage.tag.standard.StandardTags;
import net.kyori.adventure.text.serializer.gson.GsonComponentSerializer;
import net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer;
import net.kyori.adventure.text.serializer.plain.PlainTextComponentSerializer;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.annotation.Nonnull;
import java.util.LinkedList;
import java.util.List;

public class PaperComponentUtils {

    public static final MiniMessage DEFAULT_MINIMESSAGE = MiniMessage.miniMessage();
    /**
     * Without Click Event and Hover events resolver
     */
    public static final MiniMessage TEXT_MINIMESSAGE = MiniMessage.builder()
            .tags(TagResolver.builder()
                    .resolver(StandardTags.clickEvent())
                    .resolver(StandardTags.hoverEvent())
                    .build()
            )
            .build();

    // Converts textComponent to the JSON form used for serialization by Minecraft.
    public static @NotNull String toJson(Component component) {
        return GsonComponentSerializer.gson().serialize(component);
    }
    // Converts JSON in the form used for serialization by Minecraft to a Component
    public static @NotNull Component fromJson(String json) {
        return GsonComponentSerializer.gson().deserialize(json);
    }

    // Converts textComponent to a legacy string - "&6Hello &b&lworld&c!"
    public static String toColoredString(Component component) {
        return LegacyComponentSerializer.legacyAmpersand().serialize(component);
    }
    // Converts a legacy string (using formatting codes) to a TextComponent
    public static @NotNull Component fromColoredString(String text) {
        return LegacyComponentSerializer.legacyAmpersand().deserialize(text);
    }

    // Converts textComponent to a plain string - "Hello world!"
    public static String toString(Component component) {
        return PlainTextComponentSerializer.plainText().serialize(component);
    }
    // Converts a plain string to a TextComponent
    public static @NotNull Component fromString(String text) {
        return PlainTextComponentSerializer.plainText().deserialize(text);
    }

    public static @NotNull String componentToColoredString(Component component) {
        if (component == null) component = Component.empty();
        return ChatColor.translateAlternateColorCodes('&', LegacyComponentSerializer.legacyAmpersand().serialize(component));
    }

    @Nonnull
    public static String asMiniMessage(@Nonnull String text, @Nullable String cmd, @Nullable ClickEvent.Action action, @Nullable List<String> hovers) {
        String[] h = (hovers == null) ? null : hovers.toArray(new String[0]);
        return asMiniMessage(text, cmd, action, h);
    }

    @Nonnull
    public static String asMiniMessage(@Nonnull String text, @Nullable String cmd, @Nullable ClickEvent.Action action, @Nullable String... hovers) {
        if (hovers != null) {
            String hover = String.join("<br>", hovers);
            text = "<hover:show_text:'"+hover+"'>"+text+"</hover>";
        }
        if (cmd != null && action != null) {
            text = "<click:"+action.toString()+":'"+cmd+"'>"+text+"</click>";
        }
        return text;
    }

    @Nonnull
    public static String getMiniMessageText(@Nonnull String miniMessage) {
        return TEXT_MINIMESSAGE.stripTags(miniMessage);
    }

    @Nonnull
    public static Component getMiniMessageTextComponent(@Nonnull String miniMessage) {
        return DEFAULT_MINIMESSAGE.deserialize(TEXT_MINIMESSAGE.stripTags(miniMessage));
    }

    @Nonnull
    public static List<String> getMiniMessageText(@Nonnull List<String> miniMessages) {
        List<String> flat = new LinkedList<>();
        miniMessages.forEach(str -> flat.add(TEXT_MINIMESSAGE.stripTags(str)));
        return flat;
    }

    @Deprecated
    @Nonnull
    public static Component asComponent(@Nonnull Component component, @Nullable String cmd, @Nullable String... hovers) {
        if (hovers != null) {
            String hover = String.join("<br>", hovers);
            @NotNull HoverEvent<Component> hoverEvent = DEFAULT_MINIMESSAGE.deserialize(hover).asHoverEvent();
            component = component.hoverEvent(hoverEvent);
        }
        if (cmd != null) {
            ClickEvent clickEvent = ClickEvent.clickEvent(ClickEvent.Action.RUN_COMMAND, cmd);
            component = component.clickEvent(clickEvent);
        }
        return component;
    }

}
