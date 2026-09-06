package com.ahh;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.Environment;
import net.fabricmc.api.EnvType;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import com.ahh.module.ModuleManager;
import com.ahh.ui.GuiManager;
import com.ahh.anticheat.AntiCheatBypass;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

@Environment(EnvType.CLIENT)
public class AhhClientEvents implements ClientModInitializer {
    public static ModuleManager moduleManager;
    public static GuiManager guiManager;
    public static AntiCheatBypass antiCheat;
    private static boolean lastShiftState = false;

    @Override
    public void onInitializeClient() {
        AhhClient.LOGGER.info("Ahh Client initialized!");
        
        // Initialize managers
        moduleManager = new ModuleManager();
        guiManager = new GuiManager();
        antiCheat = new AntiCheatBypass();
        
        // Register all modules
        moduleManager.registerModules();
        
        AhhClient.LOGGER.info("Ahh Client modules loaded: " + moduleManager.getModules().size());
        
        // Register keybind handler
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null && client.currentScreen == null) {
                boolean shiftPressed = GLFW.glfwGetKey(client.getWindow().getHandle(), GLFW.GLFW_KEY_LEFT_SHIFT) == GLFW.GLFW_PRESS;
                
                // Detect shift press (transition from false to true)
                if (shiftPressed && !lastShiftState) {
                    AhhClient.LOGGER.info("LEFT SHIFT pressed - Opening GUI");
                    guiManager.toggleGui();
                }
                
                lastShiftState = shiftPressed;
            } else {
                lastShiftState = false;
            }
        });
    }
}
