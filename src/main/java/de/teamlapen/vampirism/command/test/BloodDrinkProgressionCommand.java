package de.teamlapen.vampirism.command.test;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import de.teamlapen.lib.lib.util.BasicCommand;
import de.teamlapen.vampirism.command.arguments.MobTypeArgument;
import de.teamlapen.vampirism.entity.player.vampire.HostileMobDrinkBuffs;
import de.teamlapen.vampirism.entity.player.vampire.PlayerBloodDrinkData;
import de.teamlapen.vampirism.entity.player.vampire.VampirePlayer;
import de.teamlapen.vampirism.world.BloodDrinkProgressionData;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import org.jetbrains.annotations.NotNull;

public class BloodDrinkProgressionCommand extends BasicCommand {

    public static ArgumentBuilder<CommandSourceStack, ?> registerTest() {
        return Commands.literal("hostileblood")
                .requires(context -> context.hasPermission(PERMISSION_LEVEL_ADMIN))
                // /vampirism-test hostileblood set <player> <type> <count>
                .then(Commands.literal("set")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("type", MobTypeArgument.mobType())
                                        .then(Commands.argument("count", IntegerArgumentType.integer(0))
                                                .executes(context -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                    MobTypeArgument.MobType mobType = MobTypeArgument.getMobType(context, "type");
                                                    int count = IntegerArgumentType.getInteger(context, "count");
                                                    return setCount(context.getSource(), player, mobType, count);
                                                })))))
                // /vampirism-test hostileblood reset <player> <type>
                .then(Commands.literal("reset")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("type", MobTypeArgument.mobType())
                                        .executes(context -> {
                                            ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                            MobTypeArgument.MobType mobType = MobTypeArgument.getMobType(context, "type");
                                            return resetCount(context.getSource(), player, mobType);
                                        }))))
                // /vampirism-test hostileblood tier <player> <type> <tier>
                .then(Commands.literal("tier")
                        .then(Commands.argument("player", EntityArgument.player())
                                .then(Commands.argument("type", MobTypeArgument.mobType())
                                        .then(Commands.argument("tier", IntegerArgumentType.integer(0))
                                                .executes(context -> {
                                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                                    MobTypeArgument.MobType mobType = MobTypeArgument.getMobType(context, "type");
                                                    int tier = IntegerArgumentType.getInteger(context, "tier");
                                                    return setTier(context.getSource(), player, mobType, tier);
                                                })))))
                // /vampirism-test hostileblood info <player>
                .then(Commands.literal("info")
                        .then(Commands.argument("player", EntityArgument.player())
                                .executes(context -> {
                                    ServerPlayer player = EntityArgument.getPlayer(context, "player");
                                    return info(context.getSource(), player);
                                })));
    }

    private static int setCount(@NotNull CommandSourceStack source, @NotNull ServerPlayer player, @NotNull MobTypeArgument.MobType mobType, int count) {
        BloodDrinkProgressionData data = BloodDrinkProgressionData.getData(player.serverLevel());
        PlayerBloodDrinkData playerData = data.getOrCreatePlayerData(player.getUUID());

        switch (mobType) {
            case ZOMBIE -> playerData.setZombieCount(count);
            case ENDERMAN -> playerData.setEndermanCount(count);
            case CREEPER -> playerData.setCreeperCount(count);
            case ALL -> {
                playerData.setZombieCount(count);
                playerData.setEndermanCount(count);
                playerData.setCreeperCount(count);
            }
        }

        data.setDirty();
        source.sendSuccess(() -> Component.literal("Set " + mobType.name().toLowerCase() + " blood drink count to " + count + " for " + player.getName().getString()), true);
        return 0;
    }

    private static int resetCount(@NotNull CommandSourceStack source, @NotNull ServerPlayer player, @NotNull MobTypeArgument.MobType mobType) {
        BloodDrinkProgressionData data = BloodDrinkProgressionData.getData(player.serverLevel());
        PlayerBloodDrinkData playerData = data.getOrCreatePlayerData(player.getUUID());

        switch (mobType) {
            case ZOMBIE -> playerData.resetZombieCount();
            case ENDERMAN -> playerData.resetEndermanCount();
            case CREEPER -> playerData.resetCreeperCount();
            case ALL -> {
                playerData.resetZombieCount();
                playerData.resetEndermanCount();
                playerData.resetCreeperCount();
            }
        }

        data.setDirty();
        source.sendSuccess(() -> Component.literal("Reset " + mobType.name().toLowerCase() + " blood drink count for " + player.getName().getString()), true);
        return 0;
    }

    private static int setTier(@NotNull CommandSourceStack source, @NotNull ServerPlayer player, @NotNull MobTypeArgument.MobType mobType, int tier) {
        BloodDrinkProgressionData data = BloodDrinkProgressionData.getData(player.serverLevel());
        PlayerBloodDrinkData playerData = data.getOrCreatePlayerData(player.getUUID());
        VampirePlayer vampirePlayer = VampirePlayer.get(player);

        switch (mobType) {
            case ZOMBIE -> {
                playerData.setZombieTier(tier);
                HostileMobDrinkBuffs.applyBuffs(vampirePlayer, MobTypeArgument.MobType.ZOMBIE, tier);
            }
            case ENDERMAN -> {
                playerData.setEndermanTier(tier);
                HostileMobDrinkBuffs.applyBuffs(vampirePlayer, MobTypeArgument.MobType.ENDERMAN, tier);
            }
            case CREEPER -> {
                playerData.setCreeperTier(tier);
                HostileMobDrinkBuffs.applyBuffs(vampirePlayer, MobTypeArgument.MobType.CREEPER, tier);
            }
            case ALL -> {
                playerData.setZombieTier(tier);
                playerData.setEndermanTier(tier);
                playerData.setCreeperTier(tier);
                HostileMobDrinkBuffs.applyBuffs(vampirePlayer, MobTypeArgument.MobType.ZOMBIE, tier);
                HostileMobDrinkBuffs.applyBuffs(vampirePlayer, MobTypeArgument.MobType.ENDERMAN, tier);
                HostileMobDrinkBuffs.applyBuffs(vampirePlayer, MobTypeArgument.MobType.CREEPER, tier);
            }
        }

        data.setDirty();
        source.sendSuccess(() -> Component.literal("Set " + mobType.name().toLowerCase() + " tier to " + tier + " for " + player.getName().getString() + " and applied buffs"), true);
        return 0;
    }

    private static int info(@NotNull CommandSourceStack source, @NotNull ServerPlayer player) {
        BloodDrinkProgressionData data = BloodDrinkProgressionData.getData(player.serverLevel());
        PlayerBloodDrinkData playerData = data.getPlayerData(player.getUUID());

        if (playerData == null) {
            source.sendSuccess(() -> Component.literal("No blood drink progression data for " + player.getName().getString()), false);
            return 0;
        }

        source.sendSuccess(() -> Component.literal("Blood Drink Progression for " + player.getName().getString() + ":"), false);
        source.sendSuccess(() -> Component.literal("  Zombie: Tier " + playerData.getZombieTier() + " (" + playerData.getZombieCount() + " drinks)"), false);
        source.sendSuccess(() -> Component.literal("  Enderman: Tier " + playerData.getEndermanTier() + " (" + playerData.getEndermanCount() + " drinks)"), false);
        source.sendSuccess(() -> Component.literal("  Creeper: Tier " + playerData.getCreeperTier() + " (" + playerData.getCreeperCount() + " drinks)"), false);

        return 0;
    }
}
