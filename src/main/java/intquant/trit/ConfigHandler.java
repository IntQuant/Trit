package intquant.trit;

import java.io.File;

import net.minecraftforge.common.config.Configuration;
import net.minecraftforge.fml.client.event.ConfigChangedEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;

public class ConfigHandler {
    public static Configuration configuration;

    public static void init(File configFile) {
        //configuration = new Configuration(new File(configDir, "settings.cfg"), null);
        //loadConfiguration();
    }

    @SubscribeEvent
    public void onConfigurationChanged(ConfigChangedEvent event) {
        if(!event.getModID().equalsIgnoreCase(Trit.MODID)) {
            return;
        }

        //loadConfiguration();
    }

    private static void loadConfiguration() {
        if(configuration.hasChanged()) {
            configuration.save();
        }
    }

    public static class TritSettings {
        public static int dimId = 1000;

    }
}