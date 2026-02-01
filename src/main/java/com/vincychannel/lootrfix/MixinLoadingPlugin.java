package com.vincychannel.lootrfix;

import net.minecraftforge.fml.common.Loader;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.ArrayList;
import java.util.List;

public class MixinLoadingPlugin implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        List<String> mixins = new ArrayList<>();
        mixins.add("mixins.lootr.json");

        if (Loader.isModLoaded("artifacts")) {
            mixins.add("mixins.rlartifacts.json");
        }

        return mixins;
    }
}
