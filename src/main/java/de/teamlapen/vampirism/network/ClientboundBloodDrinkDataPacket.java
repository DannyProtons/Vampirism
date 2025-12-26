package de.teamlapen.vampirism.network;

import de.teamlapen.vampirism.api.util.VResourceLocation;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import org.jetbrains.annotations.NotNull;

/**
 * Sends blood drink progression data from server to client
 */
public record ClientboundBloodDrinkDataPacket(
        int zombieCount,
        int zombieTier,
        int endermanCount,
        int endermanTier,
        int creeperCount,
        int creeperTier
) implements CustomPacketPayload {

    public static final Type<ClientboundBloodDrinkDataPacket> TYPE = new Type<>(VResourceLocation.mod("blood_drink_data"));
    public static final StreamCodec<RegistryFriendlyByteBuf, ClientboundBloodDrinkDataPacket> CODEC = StreamCodec.composite(
            ByteBufCodecs.VAR_INT, ClientboundBloodDrinkDataPacket::zombieCount,
            ByteBufCodecs.VAR_INT, ClientboundBloodDrinkDataPacket::zombieTier,
            ByteBufCodecs.VAR_INT, ClientboundBloodDrinkDataPacket::endermanCount,
            ByteBufCodecs.VAR_INT, ClientboundBloodDrinkDataPacket::endermanTier,
            ByteBufCodecs.VAR_INT, ClientboundBloodDrinkDataPacket::creeperCount,
            ByteBufCodecs.VAR_INT, ClientboundBloodDrinkDataPacket::creeperTier,
            ClientboundBloodDrinkDataPacket::new
    );

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }
}
