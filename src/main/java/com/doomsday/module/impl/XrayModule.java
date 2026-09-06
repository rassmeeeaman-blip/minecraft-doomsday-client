package com.doomsday.module.impl;

import com.doomsday.module.Module;
import net.minecraft.block.Blocks;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.chunk.ChunkBuilder;

public class XrayModule extends Module {
    private static final int[] ORE_BLOCKS = {
        // Common ores
        1, // Stone (for reference)
        15, // Iron Ore
        14, // Gold Ore
        16, // Diamond Ore
        21, // Lapis Ore
        73, // Redstone Ore
        129, // Emerald Ore
    };

    public XrayModule() {
        super("X-Ray", "Render");
    }

    @Override
    public void onEnable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
    }

    @Override
    public void onDisable() {
        MinecraftClient client = MinecraftClient.getInstance();
        if (client.worldRenderer != null) {
            client.worldRenderer.reload();
        }
    }

    @Override
    public void onTick() {
        // X-Ray rendering is handled via mixin
        // This updates chunk data for x-ray vision
    }

    public static boolean isOreBlock(int blockId) {
        for (int ore : ORE_BLOCKS) {
            if (ore == blockId) return true;
        }
        return false;
    }
}
