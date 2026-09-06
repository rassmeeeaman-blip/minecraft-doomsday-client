package com.ahh.module;

public abstract class Module {
    protected String name;
    protected String category;
    protected boolean enabled;

    public Module(String name, String category) {
        this.name = name;
        this.category = category;
        this.enabled = false;
    }

    public abstract void onEnable();
    public abstract void onDisable();
    public abstract void onTick();

    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }

    public void enable() {
        if (!enabled) {
            enabled = true;
            onEnable();
        }
    }

    public void disable() {
        if (enabled) {
            enabled = false;
            onDisable();
        }
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
        if (enabled) onEnable();
        else onDisable();
    }
}
