package com.bawnorton.betterbookshelves.networking.client;

import com.bawnorton.betterbookshelves.networking.BlockUpdatePayload;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.minecraft.block.Block;

import java.util.Objects;

public class Networking {
    public static void init() {
        ClientPlayNetworking.registerGlobalReceiver(BlockUpdatePayload.ID, (payload, context) -> {
            context.client().execute(() -> {
                assert context.client().world != null;
                Objects.requireNonNull(context.client().world).updateListeners(payload.blockPos(), context.client().world.getBlockState(payload.blockPos()), context.client().world.getBlockState(payload.blockPos()), Block.NOTIFY_LISTENERS);
            });
        });
    }
}
