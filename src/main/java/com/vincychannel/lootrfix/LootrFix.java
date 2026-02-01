package com.vincychannel.lootrfix;

import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@Mod(
    modid = LootrFix.MODID,
    name = LootrFix.NAME,
    version = LootrFix.VERSION,
    updateJSON = LootrFix.UPDATE_URL
)
public class LootrFix {
    public static final String MODID = "lootrfix";
    public static final String NAME = "Lootr Fixes";
    public static final String VERSION = "1.2";
    public static final String UPDATE_URL = "https://raw.githubusercontent.com/VincyChannel/Lootr-Fixes/refs/heads/main/update.json";

    public static final Logger LOGGER = LogManager.getLogger(MODID);

    @Mod.EventHandler
    public void preinit(FMLPreInitializationEvent preinit) {
        LOGGER.info("Enabling " + NAME + " v." + VERSION);
        MinecraftForge.EVENT_BUS.register(this);
    }

    @Mod.EventHandler
    public void postInit(FMLPostInitializationEvent postInit) {
        LOGGER.info("Enabled " + NAME + " v." + VERSION);
    }
}
