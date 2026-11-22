package io.github.hd_nyx.beta_nightmares;

import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.MinecraftServer;
import net.minecraft.world.entity.EntityType;
import net.neoforged.neoforge.common.ModConfigSpec;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class Config
{
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();

    private static final String mobDescription = " " + "What mobs can spawn from a nightmare (use mob ID)";
    private static final String commandDescription = " " + "Execute a raw server command when a nightmare occurs (zero erroring)";

    // Hardcoded defaults for the config file
    private static final List<String> defaultMobs = List.of(
            "minecraft:skeleton", "minecraft:spider", "minecraft:zombie"
    );

    // ["mod:id1", "Mod:id2"]
    public static final ModConfigSpec.ConfigValue<List<? extends String>> SPAWNABLE_MOBS = BUILDER
            .comment(mobDescription)
            .defineListAllowEmpty("spawnable_mobs", defaultMobs, () -> "minecraft:zombie", obj -> obj instanceof String);

    // "minecraft_command"
    public static final ModConfigSpec.ConfigValue<String> COMMAND = BUILDER
            .comment(commandDescription)
            .define("command", "");

    static final ModConfigSpec SPEC = BUILDER.build();


    // Gathers the pool from the config
    public static List<EntityType<?>> mobPool()
    {
        List<EntityType<?>> pool = new ArrayList<>();

        for (String id : Config.SPAWNABLE_MOBS.get())
        {
            // Checks for string
            ResourceLocation key = ResourceLocation.tryParse(id);

            if (key == null)
            {
                BetaNightmares.LOGGER.warn("Bad mob ID in config: {}", id);
                continue;
            }

            Optional<EntityType<?>> type = BuiltInRegistries.ENTITY_TYPE.getOptional(key);

            if (type.isEmpty())
            {
                BetaNightmares.LOGGER.warn("Unknown mob in config: {}", id);
                continue;
            }

            pool.add(type.get());
        }

        return pool;
    }

    public static void command(MinecraftServer server)
    {
        CommandSourceStack commandSource = server.createCommandSourceStack()
                .withPermission(4)
                .withSuppressedOutput();

        server.getCommands().performPrefixedCommand(commandSource, COMMAND.get());
    }
}
