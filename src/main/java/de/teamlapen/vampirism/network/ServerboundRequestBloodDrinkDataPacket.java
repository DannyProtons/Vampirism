package de.teamlapen.vampirism.network;

import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.entity.player.vampire.PlayerBloodDrinkData;
import de.teamlapen.vampirism.world.BloodDrinkProgressionData;
import io.netty.buffer.ByteBuf;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;

/**
 * Request blood drink progression data from server
 */
public record ServerboundRequestBloodDrinkDataPacket() implements CustomPacketPayload {

    public static final Type<ServerboundRequestBloodDrinkDataPacket> TYPE = new Type<>(VResourceLocation.mod("request_blood_drink_data"));
    public static final StreamCodec<ByteBuf, ServerboundRequestBloodDrinkDataPacket> CODEC = StreamCodec.unit(new ServerboundRequestBloodDrinkDataPacket());

    @Override
    public @NotNull Type<? extends CustomPacketPayload> type() {
        return TYPE;
    }

    public static void handle(@NotNull ServerboundRequestBloodDrinkDataPacket packet, @NotNull IPayloadContext context) {
        context.enqueueWork(() -> {
            if (context.player() instanceof ServerPlayer player && player.level() instanceof ServerLevel serverLevel) {
                PlayerBloodDrinkData data = BloodDrinkProgressionData.getData(serverLevel).getPlayerData(player.getUUID());

                if (data != null) {
                    // Send data to client
                    context.reply(new ClientboundBloodDrinkDataPacket(
                            data.getZombieCount(),
                            data.getZombieTier(),
                            data.getEndermanCount(),
                            data.getEndermanTier(),
                            data.getCreeperCount(),
                            data.getCreeperTier()
                    ));
                } else {
                    // Send zeros if no data exists
                    context.reply(new ClientboundBloodDrinkDataPacket(0, 0, 0, 0, 0, 0));
                }
            }
        });
    }
}
