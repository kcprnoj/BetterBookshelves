package com.bawnorton.betterbookshelves.networking;

import com.bawnorton.betterbookshelves.BetterBookshelves;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.Identifier;

public record BlockUpdatePayload(BlockPos blockPos) implements CustomPayload {
    public static final CustomPayload.Id<BlockUpdatePayload> ID = new CustomPayload.Id<>(Identifier.of(BetterBookshelves.MOD_ID, "/"));
    public static final PacketCodec<RegistryByteBuf, BlockUpdatePayload> CODEC = PacketCodec.tuple(BlockPos.PACKET_CODEC, BlockUpdatePayload::blockPos, BlockUpdatePayload::new);

    @Override
    public CustomPayload.Id<? extends CustomPayload> getId() {
        return ID;
    }
}