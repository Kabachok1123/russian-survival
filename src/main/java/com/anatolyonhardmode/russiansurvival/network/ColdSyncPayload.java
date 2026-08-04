package com.anatolyonhardmode.russiansurvival.network;

import com.anatolyonhardmode.russiansurvival.RussianSurvival;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;

public record ColdSyncPayload(float cold, int intoxication) implements CustomPacketPayload {
    public static final Type<ColdSyncPayload> TYPE = new Type<>(ResourceLocation.fromNamespaceAndPath(RussianSurvival.MOD_ID, "cold_sync"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ColdSyncPayload> CODEC = StreamCodec.composite(
            ByteBufCodecs.FLOAT, ColdSyncPayload::cold,
            ByteBufCodecs.VAR_INT, ColdSyncPayload::intoxication,
            ColdSyncPayload::new);
    @Override public Type<? extends CustomPacketPayload> type() { return TYPE; }
}
