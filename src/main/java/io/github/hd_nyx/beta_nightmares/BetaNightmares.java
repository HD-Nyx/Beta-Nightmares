package io.github.hd_nyx.beta_nightmares;

import com.mojang.logging.LogUtils;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.Difficulty;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.tick.LevelTickEvent;
import net.minecraft.network.chat.Component;
import org.slf4j.Logger;

@Mod(BetaNightmares.MODID)
public class BetaNightmares
{
    public static final String MODID = "betanightmares";
    public static final Logger LOGGER = LogUtils.getLogger();

    public static boolean runnable = true;
    private static final int WAKE_RADIUS = 2;

    public BetaNightmares(IEventBus modEventBus)
    {
        NeoForge.EVENT_BUS.addListener(this::onLevelTick);
    }

    private void onLevelTick(LevelTickEvent.Post event)
    {
        if (!runnable) return;

        if (!(event.getLevel() instanceof ServerLevel level))
        {
            return;
        }

        if (level.getDifficulty() == Difficulty.PEACEFUL) return;

        for (ServerPlayer player : level.players())
        {
            if (!player.isSleeping()) continue;

            player.getSleepingPos().ifPresent(bedPos ->
            {
                if (player.getSleepTimer() == 40)
                {
                    NightmareHandler.onStartSleeping(player, bedPos);
                }

                if (NightmareHandler.nightmareNearBed(level, bedPos, WAKE_RADIUS))
                {
                    player.stopSleepInBed(true, true); // wake up, it's the first of the month
                    player.sendSystemMessage(Component.literal("You have been woken up by a mob, make sure you aren't exposed before sleeping"));
                }
            });
        }
    }
}