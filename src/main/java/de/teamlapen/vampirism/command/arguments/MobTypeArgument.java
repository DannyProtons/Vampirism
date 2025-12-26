package de.teamlapen.vampirism.command.arguments;

import com.mojang.brigadier.StringReader;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import com.mojang.brigadier.exceptions.DynamicCommandExceptionType;
import com.mojang.brigadier.suggestion.Suggestions;
import com.mojang.brigadier.suggestion.SuggestionsBuilder;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.SharedSuggestionProvider;
import net.minecraft.network.chat.Component;
import org.jetbrains.annotations.NotNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

/**
 * Argument type for hostile mob blood drinking types
 */
public class MobTypeArgument implements ArgumentType<MobTypeArgument.MobType> {
    private static final Collection<String> EXAMPLES = Arrays.asList("zombie", "enderman", "creeper", "all");
    private static final DynamicCommandExceptionType INVALID_TYPE = new DynamicCommandExceptionType(
        (type) -> Component.translatable("command.vampirism.blooddrink.invalid_type", type)
    );

    public enum MobType {
        ZOMBIE,
        ENDERMAN,
        CREEPER,
        ALL
    }

    public static MobTypeArgument mobType() {
        return new MobTypeArgument();
    }

    public static MobType getMobType(@NotNull CommandContext<CommandSourceStack> context, String id) {
        return context.getArgument(id, MobType.class);
    }

    @Override
    public MobType parse(@NotNull StringReader reader) throws CommandSyntaxException {
        String input = reader.readUnquotedString().toLowerCase(Locale.ROOT);
        try {
            return MobType.valueOf(input.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw INVALID_TYPE.create(input);
        }
    }

    @Override
    public <S> @NotNull CompletableFuture<Suggestions> listSuggestions(CommandContext<S> context, @NotNull SuggestionsBuilder builder) {
        return SharedSuggestionProvider.suggest(
            Arrays.stream(MobType.values()).map(type -> type.name().toLowerCase(Locale.ROOT)),
            builder
        );
    }

    @Override
    public Collection<String> getExamples() {
        return EXAMPLES;
    }
}
