package com.vincychannel.lootrfix;

import zone.rong.mixinbooter.ILateMixinLoader;

import java.util.Collections;
import java.util.List;

public class MixinLoadingPlugin implements ILateMixinLoader {
    @Override
    public List<String> getMixinConfigs() {
        return Collections.singletonList("mixins.lootrfix.json");
    }
}
