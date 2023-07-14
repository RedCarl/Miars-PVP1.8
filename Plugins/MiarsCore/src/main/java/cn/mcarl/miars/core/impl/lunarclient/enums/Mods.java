package cn.mcarl.miars.core.impl.lunarclient.enums;


import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * 2022/6/25<br>
 * VioLeaft<br>
 *
 * @author huanmeng_qwq
 */

// Mod Ids: (Last Updated: 05/05/2021)
@AllArgsConstructor
@Getter
public enum Mods {
    ONE_SEVEN_VISUALS("one_seven_visuals", "1.7 Visuals"),
    FPS("fps", "FPS"),
    CPS("cps", "CPS"),
    SKY_BLOCK_ADDONS("skyblockAddons", "Skyblock Addons"),
    TOGGLE_SNEAK("toggleSneak", "Toggle Sneak/Sprint"),
    HYPIXEL_MOD("hypixel_mod", "Hypixel Mods"),
    ARMOR_STATUS("armorstatus", "Armor Status"),
    KEYSTROKES("keystrokes", "Key Strokes"),
    COORDS("coords", "Coordinates"),
    CROSSHAIR("crosshair", "Crosshair"),
    POTION_EFFECTS("potioneffects", "Potion Effects"),
    DIRECTION_HUD("directionhud", "Direction HUD"),
    WAYPOINTS("waypoints", "Waypoints"),
    SCOREBOARD("scoreboard", "Scoreboard"),
    POTION_COUNTER("potion_counter", "Potion Counter"),
    PING("ping", "Ping"),
    MOTION_BLUR("motionBlur", "Motion Blur"),
    CHAT("chat", "Chat"),
    SCROLLABLE_TOOLTIPS("scrollable_tooltips", "Scrollable Tooltips"),
    UHC_OVERLAY("uhc_overlay", "UHC Overlay"),
    PARTICLE_MULTIPLIER_MOD("particleMultiplierMod", "Particle Multiplier"),
    COOLDOWNS("cooldowns", "Cooldowns"),
    WORLDEDIT_CUI("worldedit_cui", "WorldEdit CUI"),
    CLOCK("clock", "Clock"),
    STOPWATCH("stopwatch", "Stopwatch"),
    MEMORY("memory", "Memory Usage"),
    COMBO("combo", "Combo Counter"),
    RANGE("range", "Reach Display"),
    TIME_CHANGER("time_changer", "Time Changer"),
    SERVER_ADDRESS_MOD("serverAddressMod", "Server Address"),
    SATURATION("saturation", "Saturation"),
    ITEM_PHYSIC("itemPhysic", "Item Physics"),
    ITEM_TRACKER_CHILD("itemTrackerChild", "Item Tracker"),
    SHINY_POTS("shinyPots", "Shiny Pots"),
    BLOCK_OUTLINE("block_outline", "Block Outline"),
    SCREENSHOT("screenshot", "Screenshot Uploader"),
    FOV_MOD("fov_mod", "FOV Mod"),
    TEXT_HOTKEY("textHotKey", "Auto Text Hot Key"),
    NET_GRAPH("netgraph", "Net Graph"),
    MUMBLE("mumble-link", "Mumble Link"),
    BOSS_BAR("bossbar", "Boss Bar"),
    FREE_LOOK("freelook", "Freelook"),
    REPLAY_MOD("replaymod", "ReplayMod"),
    NICK_HIDER("nickHider", "NickHider"),

    ;
    private final String modId;
    private final String displayName;

}
