package de.teamlapen.vampirism.world;

import de.teamlapen.vampirism.entity.player.vampire.PlayerBloodDrinkData;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;
import java.util.UUID;

/**
 * Stores hostile mob blood drinking progression data per-world
 */
public class BloodDrinkProgressionData extends SavedData {
    private static final Logger LOGGER = LogManager.getLogger();
    private static final String ID = "vampirism-blood-drink-progression";

    @NotNull
    public static BloodDrinkProgressionData getData(@NotNull ServerLevel world) {
        return getData(world.getServer());
    }

    @NotNull
    public static BloodDrinkProgressionData getData(@NotNull MinecraftServer server) {
        return server.getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(
            new Factory<>(() -> new BloodDrinkProgressionData(),
                         (data, provider) -> BloodDrinkProgressionData.load(data, provider)),
            ID
        );
    }

    @NotNull
    public static Optional<BloodDrinkProgressionData> getData(Level world) {
        if (world instanceof ServerLevel serverLevel) {
            return Optional.of(getData(serverLevel.getServer()));
        }
        return Optional.empty();
    }

    private final Object2ObjectOpenHashMap<UUID, PlayerBloodDrinkData> playerData = new Object2ObjectOpenHashMap<>();
    private boolean dirty = false;

    public BloodDrinkProgressionData() {
    }

    /**
     * Gets player data, creating it if it doesn't exist
     */
    @NotNull
    public PlayerBloodDrinkData getOrCreatePlayerData(@NotNull UUID playerUUID) {
        return playerData.computeIfAbsent(playerUUID, uuid -> {
            this.setDirty();
            return new PlayerBloodDrinkData();
        });
    }

    /**
     * Gets player data if it exists
     */
    @Nullable
    public PlayerBloodDrinkData getPlayerData(@NotNull UUID playerUUID) {
        return playerData.get(playerUUID);
    }

    /**
     * Marks this data as needing to be saved
     */
    @Override
    public void setDirty() {
        this.dirty = true;
        super.setDirty();
    }

    @Override
    public boolean isDirty() {
        return this.dirty;
    }

    public static @NotNull BloodDrinkProgressionData load(@NotNull CompoundTag nbt, HolderLookup.Provider provider) {
        BloodDrinkProgressionData data = new BloodDrinkProgressionData();
        ListTag playerList = nbt.getList("players", Tag.TAG_COMPOUND);

        for (Tag tag : playerList) {
            CompoundTag playerTag = (CompoundTag) tag;
            UUID playerUUID = playerTag.getUUID("uuid");
            PlayerBloodDrinkData playerBloodData = new PlayerBloodDrinkData();
            playerBloodData.deserializeNBT(provider, playerTag.getCompound("data"));
            data.playerData.put(playerUUID, playerBloodData);
        }

        return data;
    }

    @NotNull
    @Override
    public CompoundTag save(@NotNull CompoundTag compound, HolderLookup.Provider provider) {
        ListTag playerList = new ListTag();

        playerData.forEach((uuid, data) -> {
            CompoundTag playerTag = new CompoundTag();
            playerTag.putUUID("uuid", uuid);
            playerTag.put("data", data.serializeNBT(provider));
            playerList.add(playerTag);
        });

        compound.put("players", playerList);
        this.dirty = false;
        return compound;
    }
}
