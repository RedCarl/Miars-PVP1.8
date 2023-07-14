package dev.fls.tablist.animations;

import dev.fls.tablist.TabHeaderAndFooter;
import dev.fls.tablist.animations.tasks.TablistAnimationTask;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

@Setter
@Getter
public class TablistAnimation {

    private final JavaPlugin plugin;

    private boolean animateHeader, animateFooter, animateBody;
    private final List<String[]> headerFrames = new ArrayList<>(), footerFrames = new ArrayList<>();
    private final int headerInterval, footerInterval, headerAnimationDelay, footerAnimationDelay;

    private boolean isRunning;
    private final TabHeaderAndFooter template;
    private TablistAnimationTask task;


    public TablistAnimation(JavaPlugin plugin, int headerInterval, int footerInterval, int bodyInterval, int headerAnimationDelay, int bodyAnimationDelay, int footerAnimationDelay) {
        this.headerInterval = headerInterval;
        this.footerInterval = footerInterval;
        this.plugin = plugin;
        this.template = new TabHeaderAndFooter(plugin);
        this.headerAnimationDelay = headerAnimationDelay;
        this.footerAnimationDelay = footerAnimationDelay;

        if(headerInterval != -1) {
            animateHeader = true;
        }
        if(bodyInterval != -1) {
            animateBody = true;
        }
        if(footerInterval != -1) {
            animateFooter = true;
        }
    }

    public TablistAnimation addHeaderFrame(String... lines) {
        headerFrames.add(lines);
        return this;
    }

    public TablistAnimation addFooterFrame(String... lines) {
        footerFrames.add(lines);
        return this;
    }

    public void run() {
        if(isRunning) {
            return;
        }
        task = new TablistAnimationTask(this);
        task.runTaskTimerAsynchronously(plugin,0,1);
    }

    public void stop() {
        if(task == null) {
            return;
        }
        task.cancel();
        isRunning = false;
    }
}
