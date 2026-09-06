package com.doomsday;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.doomsday.module.ModuleManager;
import com.doomsday.ui.GuiManager;
import com.doomsday.anticheat.AntiCheatBypass;

@Environment(EnvType.CLIENT)
public class DoomsdayClientEvents implements ClientModInitializer {
    public static ModuleManager moduleManager;
    public static GuiManager guiManager;
    public static AntiCheatBypass antiCheat;

    @Override
    public void onInitializeClient() {
        DoomsdayClient.LOGGER.info("Doomsday Client client events initialized!");
        
        // Initialize managers
        moduleManager = new ModuleManager();
        guiManager = new GuiManager();
        antiCheat = new AntiCheatBypass();
        
        // Register all modules
        moduleManager.registerModules();
        
        DoomsdayClient.LOGGER.info("Anti-Cheat Bypass system enabled!");
    }
}
