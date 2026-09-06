package com.ahh.module.impl;

import com.ahh.module.Module;
import net.minecraft.client.MinecraftClient;

public class FlightModule extends Module {
    private float originalSpeed;
    private static final float FLIGHT_SPEED = 0.1f;

    public FlightModule() {
        super("Flight", "Movement");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            originalSpeed = client.player.getAbilities().getFlySpeed();
            client.player.getAbilities().allowFlying = true;
            client.player.getAbilities().flying = true;
            client.player.getAbilities().setFlySpeed(FLIGHT_SPEED);
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.getAbilities().allowFlying = false;
            client.player.getAbilities().flying = false;
            client.player.getAbilities().setFlySpeed(originalSpeed);
        }
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null && enabled) {
            client.player.getAbilities().allowFlying = true;
            client.player.getAbilities().flying = true;
        }
    }
}
