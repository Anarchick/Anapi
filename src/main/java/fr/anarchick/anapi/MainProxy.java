package fr.anarchick.anapi;

import net.md_5.bungee.api.plugin.Plugin;

public class MainProxy extends Plugin {

    private static MainProxy INSTANCE;

    @Override
    public void onEnable() {
        this.INSTANCE = this;
    }

    public static MainProxy getInstance() {
        return INSTANCE;
    }

}
