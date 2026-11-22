package io.github.hd_nyx.beta_nightmares;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.client.gui.ConfigurationScreen;
import net.neoforged.neoforge.client.gui.IConfigScreenFactory;

@Mod(value = BetaNightmares.MODID, dist = Dist.CLIENT)
public class BetaNightmaresClient
{
    public BetaNightmaresClient(ModContainer modContainer)
    {
        // Registers to config gui api
        // TODO: Add proper translations instead of relying on java class names
        modContainer.registerExtensionPoint(IConfigScreenFactory.class, ConfigurationScreen::new);
    }
}
