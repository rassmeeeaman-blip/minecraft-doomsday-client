package com.doomsday.module;

import com.doomsday.module.impl.FlightModule;
import com.doomsday.module.impl.ESPModule;
import com.doomsday.module.impl.XrayModule;
import com.doomsday.module.impl.FreecamModule;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;

import java.util.ArrayList;
import java.util.List;

public class ModuleManager {
    private List<Module> modules;

    public ModuleManager() {
        this.modules = new ArrayList<>();
        registerClientTickEvent();
    }

    public void registerModules() {
        registerModule(new FlightModule());
        registerModule(new ESPModule());
        registerModule(new XrayModule());
        registerModule(new FreecamModule());
    }

    public void registerModule(Module module) {
        modules.add(module);
    }

    public void registerClientTickEvent() {
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            for (Module module : modules) {
                if (module.isEnabled()) {
                    module.onTick();
                }
            }
        });
    }

    public List<Module> getModules() {
        return modules;
    }

    public Module getModule(String name) {
        return modules.stream()
                .filter(m -> m.getName().equalsIgnoreCase(name))
                .findFirst()
                .orElse(null);
    }

    public void enableModule(String name) {
        Module module = getModule(name);
        if (module != null) module.enable();
    }

    public void disableModule(String name) {
        Module module = getModule(name);
        if (module != null) module.disable();
    }
}
