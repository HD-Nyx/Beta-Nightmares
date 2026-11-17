package io.github.hd_nyx.beta_nightmares;

import net.minecraft.world.entity.EntityType;
import java.util.List;

public class NightmareMobs
{
    // This is a pool of all the mobs available
    // TODO: replace this with a toml array configuration
    public static final List<EntityType<?>> ENTITY_POOL = List.of(
            EntityType.ZOMBIE,
            EntityType.SKELETON,
            EntityType.SPIDER
    );

}