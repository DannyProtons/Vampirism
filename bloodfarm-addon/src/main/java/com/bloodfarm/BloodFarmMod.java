package com.bloodfarm;

import com.bloodfarm.registry.ModBlocks;
import com.bloodfarm.registry.ModItems;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Mod(BloodFarmMod.MODID)
public class BloodFarmMod {
    public static final String MODID = "bloodfarm";
    public static final Logger LOGGER = LoggerFactory.getLogger(BloodFarmMod.MODID);

    public BloodFarmMod(IEventBus modEventBus) {
        LOGGER.info("Initializing Blood Farm mod...");

        ModItems.ITEMS.register(modEventBus);
        ModBlocks.BLOCKS.register(modEventBus);

        LOGGER.info("Blood Farm mod initialized!");
    }
}
