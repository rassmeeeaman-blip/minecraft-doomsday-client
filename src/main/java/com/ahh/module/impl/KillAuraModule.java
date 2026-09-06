package com.ahh.module.impl;

import com.ahh.module.Module;
import com.ahh.anticheat.AntiCheatBypass;
import net.minecraft.client.MinecraftClient;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.math.Vec3d;

public class KillAuraModule extends Module {
    private static final double MAX_RANGE = 4.5;
    private static final float FOV = 90f;
    private long lastAttackTime = 0;
    private static final long ATTACK_DELAY = 100; // milliseconds
    private PlayerEntity targetPlayer = null;

    public KillAuraModule() {
        super("Kill Aura", "Combat");
    }

    @Override
    public void onEnable() {
        lastAttackTime = 0;
        targetPlayer = null;
    }

    @Override
    public void onDisable() {
        targetPlayer = null;
    }

    @Override
    public void onTick() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.world == null || client.player == null) return;

        // Find nearest target
        targetPlayer = findNearestTarget(client);

        if (targetPlayer != null) {
            // Rotate towards target
            rotateTowardsTarget(client, targetPlayer);

            // Attack if conditions are met
            if (canAttack()) {
                attackTarget(client, targetPlayer);
                lastAttackTime = System.currentTimeMillis();
            }
        }
    }

    /**
     * Find the nearest valid target player
     */
    private PlayerEntity findNearestTarget(MinecraftClient client) {
        double closestDistance = MAX_RANGE;
        PlayerEntity closestPlayer = null;

        for (Entity entity : client.world.getEntities()) {
            if (!(entity instanceof PlayerEntity)) continue;
            if (entity == client.player) continue;
            if (entity.isDead()) continue;

            // Check if player is in range
            double distance = client.player.distanceTo(entity);
            if (distance > closestDistance) continue;

            // Check if in FOV
            if (!isInFOV(client, entity)) continue;

            // Check if not a teammate (same team)
            if (isTeammate(client.player, (PlayerEntity) entity)) continue;

            closestDistance = distance;
            closestPlayer = (PlayerEntity) entity;
        }

        return closestPlayer;
    }

    /**
     * Check if entity is within FOV
     */
    private boolean isInFOV(MinecraftClient client, Entity entity) {
        Vec3d playerPos = client.player.getEyePos();
        Vec3d entityPos = entity.getEyePos();
        Vec3d direction = entityPos.subtract(playerPos).normalize();

        // Get player's look direction
        float yaw = client.player.getYaw();
        float pitch = client.player.getPitch();

        // Convert to radians
        float yawRad = (float) Math.toRadians(yaw);
        float pitchRad = (float) Math.toRadians(pitch);

        // Calculate look direction
        Vec3d lookDir = new Vec3d(
            -Math.sin(yawRad) * Math.cos(pitchRad),
            -Math.sin(pitchRad),
            Math.cos(yawRad) * Math.cos(pitchRad)
        );

        // Calculate dot product
        double dotProduct = direction.dotProduct(lookDir);
        double fovRadians = Math.toRadians(FOV / 2);

        return dotProduct > Math.cos(fovRadians);
    }

    /**
     * Check if two players are teammates
     */
    private boolean isTeammate(PlayerEntity player1, PlayerEntity player2) {
        return player1.getScoreboardTeam() != null && 
               player2.getScoreboardTeam() != null &&
               player1.getScoreboardTeam().equals(player2.getScoreboardTeam());
    }

    /**
     * Rotate smoothly towards target
     */
    private void rotateTowardsTarget(MinecraftClient client, PlayerEntity target) {
        Vec3d playerPos = client.player.getEyePos();
        Vec3d targetPos = target.getEyePos();
        Vec3d direction = targetPos.subtract(playerPos);

        // Calculate yaw and pitch
        double horizontal = Math.sqrt(direction.x * direction.x + direction.z * direction.z);
        float yaw = (float) Math.toDegrees(Math.atan2(direction.x, direction.z));
        float pitch = (float) -Math.toDegrees(Math.atan2(direction.y, horizontal));

        // Normalize angles
        yaw = wrapDegrees(yaw);
        pitch = Math.max(-90f, Math.min(90f, pitch));

        // Smooth rotation with anti-cheat awareness
        float smoothSpeed = 1.0f; // 0-1, lower = smoother
        float newYaw = AntiCheatBypass.getSmoothRotation(client.player.getYaw(), yaw, smoothSpeed);
        float newPitch = AntiCheatBypass.getSmoothRotation(client.player.getPitch(), pitch, smoothSpeed);

        client.player.setYaw(newYaw);
        client.player.setPitch(newPitch);
    }

    /**
     * Check if enough time has passed to attack
     */
    private boolean canAttack() {
        long timeSinceLastAttack = System.currentTimeMillis() - lastAttackTime;
        
        // Add human-like randomization
        long randomDelay = ATTACK_DELAY + (long) (Math.random() * 50);
        return timeSinceLastAttack >= randomDelay && AntiCheatBypass.isSafeAction();
    }

    /**
     * Attack the target
     */
    private void attackTarget(MinecraftClient client, PlayerEntity target) {
        if (!AntiCheatBypass.isValidReach(client.player.getEyePos(), target.getEyePos())) {
            return;
        }

        // Only attack if should perform action (human-like miss chance)
        if (!AntiCheatBypass.shouldPerformAction()) {
            return;
        }

        // Perform attack
        client.interactionManager.attackEntity(client.player, target);
        client.player.swingHand(net.minecraft.util.Hand.MAIN_HAND);

        // Add human delay
        AntiCheatBypass.addHumanDelay();
    }

    /**
     * Normalize angle to -180 to 180
     */
    private float wrapDegrees(float value) {
        value = value % 360f;
        if (value >= 180f) return value - 360f;
        if (value < -180f) return value + 360f;
        return value;
    }

    public PlayerEntity getTarget() {
        return targetPlayer;
    }
}
