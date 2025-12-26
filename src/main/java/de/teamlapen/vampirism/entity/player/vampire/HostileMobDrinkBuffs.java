package de.teamlapen.vampirism.entity.player.vampire;

import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.command.arguments.MobTypeArgument;
import de.teamlapen.vampirism.config.VampirismConfig;
import net.minecraft.core.Holder;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeInstance;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import org.jetbrains.annotations.NotNull;

/**
 * Manages attribute modifiers for hostile mob blood drinking buffs
 */
public class HostileMobDrinkBuffs {

    // UUIDs for each buff type - must be unique and consistent
    private static final ResourceLocation ZOMBIE_ARMOR_UUID = VResourceLocation.mod("zombie_drink_armor");
    private static final ResourceLocation ZOMBIE_REGEN_UUID = VResourceLocation.mod("zombie_drink_regen");
    private static final ResourceLocation ENDERMAN_SPEED_UUID = VResourceLocation.mod("enderman_drink_speed");
    private static final ResourceLocation ENDERMAN_REACH_UUID = VResourceLocation.mod("enderman_drink_reach");
    private static final ResourceLocation CREEPER_DAMAGE_UUID = VResourceLocation.mod("creeper_drink_damage");
    private static final ResourceLocation CREEPER_MINING_UUID = VResourceLocation.mod("creeper_drink_mining");

    /**
     * Apply buffs for a specific mob type at a specific tier
     */
    public static void applyBuffs(@NotNull VampirePlayer vampirePlayer, @NotNull MobTypeArgument.MobType mobType, int tier) {
        Player player = vampirePlayer.asEntity();

        switch (mobType) {
            case ZOMBIE -> applyZombieBuffs(player, tier);
            case ENDERMAN -> applyEndermanBuffs(player, tier);
            case CREEPER -> applyCreeperBuffs(player, tier);
        }
    }

    /**
     * Remove all hostile mob blood drinking buffs (called when leaving vampire faction)
     */
    public static void removeAllBuffs(@NotNull Player player) {
        removeModifier(player, Attributes.ARMOR, ZOMBIE_ARMOR_UUID);
        removeModifier(player, Attributes.MOVEMENT_SPEED, ENDERMAN_SPEED_UUID);
        removeModifier(player, Attributes.BLOCK_INTERACTION_RANGE, ENDERMAN_REACH_UUID);
        removeModifier(player, Attributes.ATTACK_DAMAGE, CREEPER_DAMAGE_UUID);
        removeModifier(player, Attributes.BLOCK_BREAK_SPEED, CREEPER_MINING_UUID);
        // Note: Regeneration is handled in VampirePlayer.tick() based on tier
    }

    /**
     * Reapply all buffs based on current tiers (called when joining world or leveling up)
     */
    public static void reapplyAllBuffs(@NotNull VampirePlayer vampirePlayer, @NotNull PlayerBloodDrinkData data) {
        Player player = vampirePlayer.asEntity();

        if (data.getZombieTier() > 0) {
            applyZombieBuffs(player, data.getZombieTier());
        }
        if (data.getEndermanTier() > 0) {
            applyEndermanBuffs(player, data.getEndermanTier());
        }
        if (data.getCreeperTier() > 0) {
            applyCreeperBuffs(player, data.getCreeperTier());
        }
    }

    private static void applyZombieBuffs(@NotNull Player player, int tier) {
        double armorPerTier = VampirismConfig.BALANCE.vpZombieArmorPerTier.get();
        double totalArmor = armorPerTier * tier;

        applyModifier(player, Attributes.ARMOR, ZOMBIE_ARMOR_UUID, "Zombie Blood Armor", totalArmor, AttributeModifier.Operation.ADD_VALUE);
        // Regeneration is handled in VampirePlayer.tick() by checking tier
    }

    private static void applyEndermanBuffs(@NotNull Player player, int tier) {
        double speedPerTier = VampirismConfig.BALANCE.vpEndermanSpeedPerTier.get();
        double reachPerTier = VampirismConfig.BALANCE.vpEndermanReachPerTier.get();
        double totalSpeed = speedPerTier * tier;
        double totalReach = reachPerTier * tier;

        applyModifier(player, Attributes.MOVEMENT_SPEED, ENDERMAN_SPEED_UUID, "Enderman Blood Speed", totalSpeed, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        applyModifier(player, Attributes.BLOCK_INTERACTION_RANGE, ENDERMAN_REACH_UUID, "Enderman Blood Reach", totalReach, AttributeModifier.Operation.ADD_VALUE);
    }

    private static void applyCreeperBuffs(@NotNull Player player, int tier) {
        double damagePerTier = VampirismConfig.BALANCE.vpCreeperDamagePerTier.get();
        double miningPerTier = VampirismConfig.BALANCE.vpCreeperMiningPerTier.get();
        double totalDamage = damagePerTier * tier;
        double totalMining = miningPerTier * tier;

        applyModifier(player, Attributes.ATTACK_DAMAGE, CREEPER_DAMAGE_UUID, "Creeper Blood Damage", totalDamage, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
        applyModifier(player, Attributes.BLOCK_BREAK_SPEED, CREEPER_MINING_UUID, "Creeper Blood Mining", totalMining, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    private static void applyModifier(@NotNull Player player, @NotNull Holder<Attribute> attribute, @NotNull ResourceLocation id, @NotNull String name, double amount, @NotNull AttributeModifier.Operation operation) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            // Remove existing modifier if present
            instance.removeModifier(id);
            // Add new modifier with updated value
            instance.addPermanentModifier(new AttributeModifier(id, amount, operation));
        }
    }

    private static void removeModifier(@NotNull Player player, @NotNull Holder<Attribute> attribute, @NotNull ResourceLocation id) {
        AttributeInstance instance = player.getAttribute(attribute);
        if (instance != null) {
            instance.removeModifier(id);
        }
    }

    /**
     * Get the regeneration bonus for zombie blood drinking
     */
    public static float getZombieRegenBonus(int tier) {
        return (float) (VampirismConfig.BALANCE.vpZombieRegenPerTier.get() * tier);
    }
}
