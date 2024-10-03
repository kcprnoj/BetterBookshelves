package com.bawnorton.betterbookshelves;

import com.bawnorton.betterbookshelves.config.ServerConfigManager;
import com.bawnorton.betterbookshelves.networking.BlockUpdatePayload;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.minecraft.server.MinecraftServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BetterBookshelves implements ModInitializer {
	public static final String MOD_ID = "betterbookshelves";
	public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
	public static MinecraftServer SERVER;

	@Override
	public void onInitialize() {
		ServerConfigManager.loadConfig();
		LOGGER.info("BetterBookshelves Initialized");
		PayloadTypeRegistry.playS2C().register(BlockUpdatePayload.ID, BlockUpdatePayload.CODEC);
	}
}