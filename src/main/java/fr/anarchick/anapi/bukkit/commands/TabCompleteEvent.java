package fr.anarchick.anapi.bukkit.commands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginCommand;
import org.bukkit.event.*;

import javax.annotation.Nonnull;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class TabCompleteEvent implements Listener {

    @EventHandler(ignoreCancelled = true, priority = EventPriority.NORMAL)
    public void onTabComplete(final org.bukkit.event.server.TabCompleteEvent event) {
        final String buffer = event.getBuffer();
        final String command = buffer.split(" ")[0].substring(1);
        PluginCommand pc = Bukkit.getPluginCommand(command);
        if (pc != null && pc.getExecutor() instanceof Commands executor) {
            new PluginTabCompleteEvent(executor, event, command, buffer).callEvent();
        }
    }

    public static class PluginTabCompleteEvent extends Event implements Cancellable{

        private static final HandlerList HANDLERS_LIST = new HandlerList();

        private final Commands executor;
        private final org.bukkit.event.server.TabCompleteEvent event;
        private final String command;
        private final List<String> arguments;
        private final String lastArgument;
        private final CommandSender sender;

        public PluginTabCompleteEvent(@Nonnull Commands executor, org.bukkit.event.server.TabCompleteEvent event, String command, String buffer) {
            this.executor = executor;
            this.event = event;
            this.sender = event.getSender();
            this.command = command;

            final String[] args = buffer.split(" ");
            this.arguments = Stream.of(args).skip(1).toList();

            final char c = buffer.charAt(buffer.length() - 1);
            this.lastArgument = (c == ' ') ? "" : arguments.get(arguments.size() -1);

        }

        @Override
        public HandlerList getHandlers() {
            return HANDLERS_LIST;
        }

        public static HandlerList getHandlerList() {
            return HANDLERS_LIST;
        }

        public Commands getExecutor() {
            return executor;
        }

        public CommandSender getSender() {
            return sender;
        }

        public String getCommand() {
            return command;
        }

        @Nonnull
        public List<String> getArguments() {
            return arguments;
        }

        @Nonnull
        public String getArgument(int i) {
            return (arguments.size() > i) ? arguments.get(i) : "";
        }

        @Nonnull
        public String getLastArgument() {
            return lastArgument;
        }

        @Nonnull
        public List<String> getCompletions() {
            return event.getCompletions();
        }

        public void setCompletions(@Nonnull List<String> completions) {
            event.setCompletions(completions);
        }

        public void setCompletions(@Nonnull Set<String> completions) {
            event.setCompletions(completions.stream().toList());
        }

        public int currentArgument() {
            int size = arguments.size();
            return (getLastArgument().isEmpty()) ? ++size : size;
        }

        @Override
        public boolean isCancelled() {
            return event.isCancelled();
        }

        @Override
        public void setCancelled(boolean cancel) {
            event.setCancelled(cancel);
        }

    }

}
