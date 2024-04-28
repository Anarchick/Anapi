package fr.anarchick.anapi.bukkit.customItem;

import fr.anarchick.anapi.bukkit.commands.Commands;
import org.bukkit.entity.Player;

@SuppressWarnings("unused")
public class CustomItemCommand extends Commands {

	@Override
	public boolean onCommand() {
		if (sender instanceof Player player && player.hasPermission("anapi.command.customitem.use")) {
			CustomItemGUI.GUI.open(player);
        }
		return true;
	}
	
}