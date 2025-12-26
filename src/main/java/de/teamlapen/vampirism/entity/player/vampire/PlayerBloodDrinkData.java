package de.teamlapen.vampirism.entity.player.vampire;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import org.jetbrains.annotations.NotNull;

/**
 * Stores per-player hostile mob blood drinking progress
 */
public class PlayerBloodDrinkData {

    // Drink counters for each mob type
    private int zombieCount = 0;
    private int endermanCount = 0;
    private int creeperCount = 0;

    // Current tier for each mob type
    private int zombieTier = 0;
    private int endermanTier = 0;
    private int creeperTier = 0;

    public PlayerBloodDrinkData() {
    }

    // Zombie methods
    public int getZombieCount() {
        return zombieCount;
    }

    public void setZombieCount(int count) {
        this.zombieCount = Math.max(0, count);
    }

    public int getZombieTier() {
        return zombieTier;
    }

    public void setZombieTier(int tier) {
        this.zombieTier = Math.max(0, tier);
    }

    public void incrementZombie() {
        this.zombieCount++;
    }

    public void resetZombieCount() {
        this.zombieCount = 0;
    }

    public void incrementZombieTier() {
        this.zombieTier++;
    }

    // Enderman methods
    public int getEndermanCount() {
        return endermanCount;
    }

    public void setEndermanCount(int count) {
        this.endermanCount = Math.max(0, count);
    }

    public int getEndermanTier() {
        return endermanTier;
    }

    public void setEndermanTier(int tier) {
        this.endermanTier = Math.max(0, tier);
    }

    public void incrementEnderman() {
        this.endermanCount++;
    }

    public void resetEndermanCount() {
        this.endermanCount = 0;
    }

    public void incrementEndermanTier() {
        this.endermanTier++;
    }

    // Creeper methods
    public int getCreeperCount() {
        return creeperCount;
    }

    public void setCreeperCount(int count) {
        this.creeperCount = Math.max(0, count);
    }

    public int getCreeperTier() {
        return creeperTier;
    }

    public void setCreeperTier(int tier) {
        this.creeperTier = Math.max(0, tier);
    }

    public void incrementCreeper() {
        this.creeperCount++;
    }

    public void resetCreeperCount() {
        this.creeperCount = 0;
    }

    public void incrementCreeperTier() {
        this.creeperTier++;
    }

    // Serialization
    @NotNull
    public CompoundTag serializeNBT(HolderLookup.Provider provider) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("zombieCount", zombieCount);
        tag.putInt("zombieTier", zombieTier);
        tag.putInt("endermanCount", endermanCount);
        tag.putInt("endermanTier", endermanTier);
        tag.putInt("creeperCount", creeperCount);
        tag.putInt("creeperTier", creeperTier);
        return tag;
    }

    public void deserializeNBT(HolderLookup.Provider provider, @NotNull CompoundTag tag) {
        this.zombieCount = tag.getInt("zombieCount");
        this.zombieTier = tag.getInt("zombieTier");
        this.endermanCount = tag.getInt("endermanCount");
        this.endermanTier = tag.getInt("endermanTier");
        this.creeperCount = tag.getInt("creeperCount");
        this.creeperTier = tag.getInt("creeperTier");
    }
}
