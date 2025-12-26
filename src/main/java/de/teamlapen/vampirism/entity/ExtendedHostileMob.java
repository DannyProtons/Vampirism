package de.teamlapen.vampirism.entity;

import de.teamlapen.lib.lib.storage.IAttachment;
import de.teamlapen.vampirism.api.entity.IExtendedCreatureVampirism;
import de.teamlapen.vampirism.api.entity.vampire.IVampire;
import de.teamlapen.vampirism.api.util.VResourceLocation;
import de.teamlapen.vampirism.core.ModAttachments;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.entity.vampire.HostileMobBloodRegistry;
import de.teamlapen.vampirism.util.DamageHandler;
import de.teamlapen.vampirism.world.ModDamageSources;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.neoforged.neoforge.attachment.IAttachmentSerializer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Optional;

/**
 * Extended property for hostile mobs to track blood separately from health
 */
public class ExtendedHostileMob implements IAttachment, IExtendedCreatureVampirism {
    private static final String NBT_KEY = "extended_hostile_mob";
    public static final ResourceLocation SERIALIZER_ID = VResourceLocation.mod(NBT_KEY);

    private static final String KEY_BLOOD = "bloodLevel";
    private static final String KEY_MAX_BLOOD = "max_blood";

    public static @NotNull Optional<ExtendedHostileMob> getSafe(@NotNull Entity entity) {
        if (entity instanceof LivingEntity && HostileMobBloodRegistry.isHostileMobDrinkable(entity)) {
            return Optional.of(((LivingEntity) entity).getData(ModAttachments.EXTENDED_HOSTILE_MOB));
        }
        return Optional.empty();
    }

    private final LivingEntity entity;
    private int maxBlood;
    private int blood;

    public ExtendedHostileMob(LivingEntity entity) {
        this.entity = entity;
        // Get blood amount from registry
        this.maxBlood = HostileMobBloodRegistry.getBloodAmount(entity);
        this.blood = maxBlood;
    }

    @Override
    public @NotNull Entity asEntity() {
        return this.entity;
    }

    @Override
    public boolean canBeBitten(IVampire biter) {
        return getBlood() > 0 && entity.isAlive();
    }

    @Override
    public int getBlood() {
        return blood;
    }

    @Override
    public float getBloodSaturation() {
        return HostileMobBloodRegistry.getSaturation(entity);
    }

    @Override
    public int getMaxBlood() {
        return maxBlood;
    }

    @Override
    public boolean hasPoisonousBlood() {
        return false; // Hostile mobs don't have poisonous blood
    }

    @Override
    public int onBite(IVampire biter) {
        if (getBlood() <= 0) return 0;

        // Calculate blood amount to drink
        int amt = Math.max(1, (getMaxBlood() / (biter instanceof VampirePlayer ? 6 : 2)));

        // Advanced biter: stop before taking the last blood point
        if (amt >= blood) {
            if (blood > 1 && biter.isAdvancedBiter()) {
                amt = blood - 1;
            } else {
                amt = blood;
            }
        }

        blood -= amt;

        // Apply debuffs when blood is low (75% or more drained)
        float bloodPercentage = (float) blood / maxBlood;
        if (bloodPercentage <= 0.25f) {
            // Severe weakness and slowness when almost drained
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 200, 1));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 200, 1));
        } else if (bloodPercentage <= 0.5f) {
            // Moderate debuffs when half drained
            entity.addEffect(new MobEffectInstance(MobEffects.WEAKNESS, 100, 0));
            entity.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 100, 0));
        }

        // Kill entity when blood reaches 0
        if (blood == 0) {
            DamageHandler.hurtModded(entity, ModDamageSources::noBlood, 1000);
        }

        return amt;
    }

    @Override
    public @NotNull CompoundTag serializeNBT(HolderLookup.@NotNull Provider provider) {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt(KEY_BLOOD, blood);
        nbt.putInt(KEY_MAX_BLOOD, maxBlood);
        return nbt;
    }

    @Override
    public void deserializeNBT(HolderLookup.@NotNull Provider provider, @NotNull CompoundTag nbt) {
        if (nbt.contains(KEY_BLOOD)) {
            blood = nbt.getInt(KEY_BLOOD);
        }
        if (nbt.contains(KEY_MAX_BLOOD)) {
            maxBlood = nbt.getInt(KEY_MAX_BLOOD);
        }
    }

    public static class Serializer implements IAttachmentSerializer<CompoundTag, ExtendedHostileMob> {
        @Override
        public @NotNull ExtendedHostileMob read(@NotNull IAttachmentHolder holder, @NotNull CompoundTag tag, HolderLookup.@NotNull Provider provider) {
            if (holder instanceof LivingEntity entity) {
                ExtendedHostileMob ext = new ExtendedHostileMob(entity);
                ext.deserializeNBT(provider, tag);
                return ext;
            }
            throw new IllegalArgumentException("Holder must be a LivingEntity");
        }

        @Override
        public @Nullable CompoundTag write(@NotNull ExtendedHostileMob attachment, HolderLookup.@NotNull Provider provider) {
            return attachment.serializeNBT(provider);
        }
    }
}
