package com.ahh;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import com.ahh.module.ModuleManager;
import com.ahh.ui.GuiManager;
import com.ahh.anticheat.AntiCheatBypass;

@Environment(EnvType.CLIENT)
public class AhhClientEvents implements ClientModInitializer {
    public static ModuleManager moduleManager;
    public static GuiManager guiManager;
    public static AntiCheatBypass antiCheat;

    @Override
    public void onInitializeClient() {
        AhhClient.LOGGER.info("Ahh Client client events initialized!");
        
        // Initialize managers
        moduleManager = new ModuleManager();
        guiManager = new GuiManager();
        antiCheat = new AntiCheatBypass();
        
        // Register all modules
        moduleManager.registerModules();
        
        AhhClient.LOGGER.info("Anti-Cheat Bypass system enabled!");
    }
}
