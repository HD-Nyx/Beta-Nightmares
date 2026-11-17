package io.github.hd_nyx.beta_nightmares;

import net.minecraft.commands.arguments.EntityAnchorArgument;
import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.RandomSource;
import net.minecraft.Util;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MobSpawnType;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.level.pathfinder.Path;
import net.minecraft.world.phys.AABB;

import java.util.List;

public class NightmareHandler
{
    public static final int selectionRadius = 10;
    public static final String NIGHTMARE_TAG = "BetaNightmares";

    // Main function
    public static void onStartSleeping(ServerPlayer player, BlockPos bedPos)
    {
        ServerLevel level = player.serverLevel();

        Mob tester = EntityType.ZOMBIE.create(level);

        // In case difficulty is peaceful
        if (tester == null)
        {
            return;
        }

        for (int attempt = 0; attempt < 20; attempt++)
        {
            BlockPos chosenBlock = randomNear(bedPos, selectionRadius, level.getRandom());

            if (level.getMaxLocalRawBrightness(chosenBlock) > 7 || !validSpawnState(level, chosenBlock))
            {
                continue;
            }

            if (!canPathToBed(tester, chosenBlock, bedPos))
            {
                continue;
            }

            EntityType<?> mobType = Util.getRandom(NightmareMobs.ENTITY_POOL, level.getRandom());
            Entity entity = mobType.spawn(level, bedPos.offset(-2, 1, 0), MobSpawnType.EVENT);

            if (entity == null)
            {
                continue;
            }

            if (entity instanceof Mob mob)
            {
                mob.setPersistenceRequired();
                mob.addTag(NIGHTMARE_TAG);

                mob.setTarget(player);

                mob.doHurtTarget(player);
                mob.lookAt(EntityAnchorArgument.Anchor.EYES, player.getEyePosition());
            }

            BetaNightmares.LOGGER.info("Spawned {} at {}", entity, chosenBlock);

            return;
        }

        BetaNightmares.LOGGER.info("No valid spot found after 20 attempts");
    }

    // Attempts to path the probe to the bed to see if it can reach the bed
    private static boolean canPathToBed(Mob probe, BlockPos spot, BlockPos bedPos)
    {
        probe.moveTo(spot.getX() + 0.5, spot.getY(), spot.getZ() + 0.5);
        probe.setOnGround(true);

        Path path = probe.getNavigation().createPath(bedPos.above(), 0);

        if (path == null || path.getEndNode() == null)
        {
            return false;
        }

        return path.getEndNode().asBlockPos().closerThan(bedPos, 2.0);
    }

    // Checks if mob near player has NIGHTMARE_TAG
    public static boolean nightmareNearBed(ServerLevel level, BlockPos bedPos, int radius)
    {
        AABB area = new AABB(bedPos).inflate(radius);

        List<Monster> nearby = level.getEntitiesOfClass(Monster.class, area,
                mob -> mob.getTags().contains(NIGHTMARE_TAG)
        );

        return !nearby.isEmpty();
    }

    // From a radius point, select a random block in a 3D space
    public static BlockPos randomNear(BlockPos bedPos, int radius, RandomSource random)
    {
        int deltaX = random.nextInt(radius * 2 + 1) - radius;
        int deltaY = random.nextInt(5) - 2;
        int detlaZ = random.nextInt(radius * 2 + 1) - radius;

        return bedPos.offset(deltaX, deltaY, detlaZ);
    }

    // Check if spawn position is valid
    private static boolean validSpawnState(ServerLevel level, BlockPos choosenBlock)
    {
        return level.getBlockState(choosenBlock).isAir() && !level.getBlockState(choosenBlock.below()).isAir();
    }
}
