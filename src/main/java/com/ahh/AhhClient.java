package com.ahh;

import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class AhhClient implements ModInitializer {
    public static final String MOD_ID = "ahh_client";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LOGGER.info("Ahh Client initialized!");
    }
}
