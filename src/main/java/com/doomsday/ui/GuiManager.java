package com.doomsday.ui;

import com.doomsday.ui.screen.DoomsdayMainScreen;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import org.lwjgl.glfw.GLFW;

public class GuiManager {
    private static final int GUI_TOGGLE_KEY = GLFW.GLFW_KEY_LEFT_SHIFT;
    private boolean guiOpen = false;
    private boolean lastKeyState = false;

    public GuiManager() {
        registerKeyBindings();
    }

    private void registerKeyBindings() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.currentScreen == null) {
                boolean isKeyPressed = GLFW.glfwGetKey(client.getWindow().getHandle(), GUI_TOGGLE_KEY) == GLFW.GLFW_PRESS;
                
                // Detect key press (transition from not pressed to pressed)
                if (isKeyPressed && !lastKeyState) {
                    toggleGui();
                }
                
                lastKeyState = isKeyPressed;
            } else {
                lastKeyState = false;
            }
        });
    }

    public void toggleGui() {
        MinecraftClient client = MinecraftClient.getInstance();
        guiOpen = !guiOpen;
        
        if (guiOpen) {
            client.setScreen(new DoomsdayMainScreen(null));
        } else {
            client.setScreen(null);
        }
    }

    public boolean isGuiOpen() {
        return guiOpen;
    }
}
