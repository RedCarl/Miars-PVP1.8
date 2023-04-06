package cn.mcarl.miars.faction;

import cc.carm.lib.easyplugin.EasyPlugin;

public class MiarsFaction extends EasyPlugin {

    private static MiarsFaction instance;
    public static MiarsFaction getInstance() {
        return instance;
    }
    @Override
    protected boolean initialize() {
        instance = this;
        return true;
    }

    @Override
    protected void shutdown() {

    }
}
