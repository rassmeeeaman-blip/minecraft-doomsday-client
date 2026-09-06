package com.doomsday.module.impl;

import com.doomsday.module.Module;
import net.minecraft.client.MinecraftClient;
import net.minecraft.util.math.Vec3d;

public class FreecamModule extends Module {
    private Vec3d originalPos;
    private boolean wasFlying;
    private static final float SPEED = 0.2f;

    public FreecamModule() {
        super("Freecam", "Movement");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            originalPos = client.player.getPos();
            wasFlying = client.player.getAbilities().flying;
            client.player.getAbilities().flying = true;
            client.player.getAbilities().allowFlying = true;
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player != null) {
            client.player.setPos(originalPos.x, originalPos.y, originalPos.z);
            client.player.getAbilities().flying = wasFlying;
            if (!wasFlying) {
                client.player.getAbilities().allowFlying = false;
            }
        }
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.player == null) return;

        // Handle movement input for freecam
        if (client.options.forwardKey.isPressed()) {
            moveCamera(client, 0, SPEED);
        }
        if (client.options.backKey.isPressed()) {
            moveCamera(client, 180, SPEED);
        }
        if (client.options.leftKey.isPressed()) {
            moveCamera(client, 90, SPEED);
        }
        if (client.options.rightKey.isPressed()) {
            moveCamera(client, -90, SPEED);
        }
        if (client.options.spaceKey.isPressed()) {
            client.player.setPos(client.player.getX(), client.player.getY() + SPEED, client.player.getZ());
        }
        if (client.options.sneakKey.isPressed()) {
            client.player.setPos(client.player.getX(), client.player.getY() - SPEED, client.player.getZ());
        }
    }

    private void moveCamera(MinecraftClient client, float angle, float speed) {
        float yaw = client.player.getYaw();
        float rad = (float) Math.toRadians(yaw + angle);
        double x = client.player.getX() + Math.sin(rad) * speed;
        double z = client.player.getZ() - Math.cos(rad) * speed;
        client.player.setPos(x, client.player.getY(), z);
    }
}
