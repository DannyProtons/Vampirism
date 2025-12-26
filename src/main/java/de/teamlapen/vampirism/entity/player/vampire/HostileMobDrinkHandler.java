package de.teamlapen.vampirism.entity.player.vampire;

import de.teamlapen.vampirism.api.event.BloodDrinkEvent;
import de.teamlapen.vampirism.command.arguments.MobTypeArgument;
import de.teamlapen.vampirism.config.VampirismConfig;
import de.teamlapen.vampirism.entity.vampire.HostileMobBloodRegistry;
import de.teamlapen.vampirism.world.BloodDrinkProgressionData;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import org.jetbrains.annotations.NotNull;

/**
 * Handles tracking and tier progression for hostile mob blood drinking
 */
@EventBusSubscriber
public class HostileMobDrinkHandler {

    @SubscribeEvent
    public static void onPlayerDrinkBlood(@NotNull BloodDrinkEvent.PlayerDrinkBloodEvent event) {
        // Only process on server side
        if (!(event.getVampire() instanceof VampirePlayer vampirePlayer)) {
            return;
        }

        // Check if we're on server side and entity is ServerPlayer
        if (!(vampirePlayer.asEntity() instanceof ServerPlayer player)) {
            return; // Skip client-side execution
        }

        if (player.level() instanceof ServerLevel serverLevel) {
            // Check if the blood source is a hostile mob
            event.getBloodSource().getEntity().ifPresent(entity -> {
                if (entity instanceof LivingEntity livingEntity && HostileMobBloodRegistry.isHostileMobDrinkable(livingEntity)) {
                    handleHostileMobDrink(serverLevel, player, livingEntity);
                }
            });
        }
    }

    private static void handleHostileMobDrink(@NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull LivingEntity mobEntity) {
        // Get progression data
        BloodDrinkProgressionData progressionData = BloodDrinkProgressionData.getData(level);
        PlayerBloodDrinkData playerData = progressionData.getOrCreatePlayerData(player.getUUID());

        // Determine mob type
        MobTypeArgument.MobType mobType = HostileMobBloodRegistry.getMobType(mobEntity);
        if (mobType == null || mobType == MobTypeArgument.MobType.ALL) {
            return;
        }

        // Increment counter
        int newCount;
        int currentTier;
        switch (mobType) {
            case ZOMBIE -> {
                playerData.incrementZombie();
                newCount = playerData.getZombieCount();
                currentTier = playerData.getZombieTier();
            }
            case ENDERMAN -> {
                playerData.incrementEnderman();
                newCount = playerData.getEndermanCount();
                currentTier = playerData.getEndermanTier();
            }
            case CREEPER -> {
                playerData.incrementCreeper();
                newCount = playerData.getCreeperCount();
                currentTier = playerData.getCreeperTier();
            }
            default -> {
                return;
            }
        }

        // Check if threshold reached
        int baseThreshold = VampirismConfig.BALANCE.vpHostileDrinkBaseThreshold.get();
        int increment = VampirismConfig.BALANCE.vpHostileDrinkThresholdIncrement.get();
        int requiredCount = baseThreshold + (currentTier * increment);

        if (newCount >= requiredCount) {
            // Tier up!
            tierUp(level, player, playerData, mobType, currentTier + 1);
        }

        // Mark data as dirty for save
        progressionData.setDirty();
    }

    private static void tierUp(@NotNull ServerLevel level, @NotNull ServerPlayer player, @NotNull PlayerBloodDrinkData playerData,
                                @NotNull MobTypeArgument.MobType mobType, int newTier) {
        // Reset counter and increment tier
        switch (mobType) {
            case ZOMBIE -> {
                playerData.resetZombieCount();
                playerData.setZombieTier(newTier);
            }
            case ENDERMAN -> {
                playerData.resetEndermanCount();
                playerData.setEndermanTier(newTier);
            }
            case CREEPER -> {
                playerData.resetCreeperCount();
                playerData.setCreeperTier(newTier);
            }
        }

        // Apply buffs
        VampirePlayer vampirePlayer = VampirePlayer.get(player);
        HostileMobDrinkBuffs.applyBuffs(vampirePlayer, mobType, newTier);

        // Notify player
        String mobName = mobType.name().substring(0, 1) + mobType.name().substring(1).toLowerCase();
        player.sendSystemMessage(Component.literal("§6§l" + mobName + " Blood Mastery - Tier " + newTier + "!"));
        player.sendSystemMessage(Component.literal("§7" + getBuffDescription(mobType, newTier)));

        // Play sound and effects
        level.playSound(null, player.blockPosition(), SoundEvents.PLAYER_LEVELUP, SoundSource.PLAYERS, 1.0f, 1.0f);

        // TODO: Add particle effects
    }

    private static @NotNull String getBuffDescription(@NotNull MobTypeArgument.MobType mobType, int tier) {
        double zombieArmor = VampirismConfig.BALANCE.vpZombieArmorPerTier.get() * tier;
        double zombieRegen = VampirismConfig.BALANCE.vpZombieRegenPerTier.get() * tier;
        double endermanSpeed = VampirismConfig.BALANCE.vpEndermanSpeedPerTier.get() * tier * 100;
        double endermanReach = VampirismConfig.BALANCE.vpEndermanReachPerTier.get() * tier;
        double creeperDamage = VampirismConfig.BALANCE.vpCreeperDamagePerTier.get() * tier * 100;
        double creeperMining = VampirismConfig.BALANCE.vpCreeperMiningPerTier.get() * tier * 100;

        return switch (mobType) {
            case ZOMBIE -> String.format("+%.1f Armor, +%.1f HP/s Regeneration", zombieArmor, zombieRegen);
            case ENDERMAN -> String.format("+%.0f%% Speed, +%.1f Block Reach", endermanSpeed, endermanReach);
            case CREEPER -> String.format("+%.0f%% Damage, +%.0f%% Mining Speed", creeperDamage, creeperMining);
            default -> "";
        };
    }
}
