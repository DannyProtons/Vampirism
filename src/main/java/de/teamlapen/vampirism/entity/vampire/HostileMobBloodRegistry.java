package de.teamlapen.vampirism.entity.vampire;

import de.teamlapen.vampirism.command.arguments.MobTypeArgument;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.monster.Drowned;
import net.minecraft.world.entity.monster.EnderMan;
import net.minecraft.world.entity.monster.Husk;
import net.minecraft.world.entity.monster.Zombie;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.HashMap;
import java.util.Map;

/**
 * Registry for hostile mobs that can be bitten by vampires
 * Maps entity types to their mob category for progression tracking
 */
public class HostileMobBloodRegistry {

    private static final Map<EntityType<?>, MobTypeArgument.MobType> ENTITY_TYPE_MAP = new HashMap<>();

    static {
        // Zombie category - includes all zombie variants
        ENTITY_TYPE_MAP.put(EntityType.ZOMBIE, MobTypeArgument.MobType.ZOMBIE);
        ENTITY_TYPE_MAP.put(EntityType.DROWNED, MobTypeArgument.MobType.ZOMBIE);
        ENTITY_TYPE_MAP.put(EntityType.HUSK, MobTypeArgument.MobType.ZOMBIE);
        ENTITY_TYPE_MAP.put(EntityType.ZOMBIE_VILLAGER, MobTypeArgument.MobType.ZOMBIE);

        // Enderman category
        ENTITY_TYPE_MAP.put(EntityType.ENDERMAN, MobTypeArgument.MobType.ENDERMAN);

        // Creeper category
        ENTITY_TYPE_MAP.put(EntityType.CREEPER, MobTypeArgument.MobType.CREEPER);
    }

    /**
     * Check if an entity can be bitten for hostile mob progression
     * @param entity The entity to check
     * @return true if the entity is a drinkable hostile mob
     */
    public static boolean isHostileMobDrinkable(@NotNull Entity entity) {
        return ENTITY_TYPE_MAP.containsKey(entity.getType());
    }

    /**
     * Get the mob type category for an entity
     * @param entity The entity
     * @return The mob type category, or null if not a drinkable hostile mob
     */
    @Nullable
    public static MobTypeArgument.MobType getMobType(@NotNull Entity entity) {
        return ENTITY_TYPE_MAP.get(entity.getType());
    }

    /**
     * Get the blood amount for a hostile mob
     * @param entity The entity
     * @return Blood amount in food units
     */
    public static int getBloodAmount(@NotNull Entity entity) {
        MobTypeArgument.MobType mobType = getMobType(entity);
        if (mobType == null) {
            return 0;
        }

        return switch (mobType) {
            case ZOMBIE -> 6;      // Zombies are common, moderate blood
            case ENDERMAN -> 8;     // Endermen are rarer, more blood
            case CREEPER -> 4;      // Creepers are risky, less blood
            case ALL -> 0;          // ALL is only for commands, not actual entities
        };
    }

    /**
     * Get the saturation modifier for hostile mob blood
     * @param entity The entity
     * @return Saturation modifier (0.0 - 1.0)
     */
    public static float getSaturation(@NotNull Entity entity) {
        // Hostile mob blood has lower saturation than passive mobs
        return 0.3f;
    }
}
