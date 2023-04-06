package io.github.Leonardo0013YT.UltraSkyWars.modules.parties;

import com.google.gson.Gson;
import io.github.Leonardo0013YT.UltraSkyWars.UltraSkyWars;
import io.github.Leonardo0013YT.UltraSkyWars.config.Settings;
import io.github.Leonardo0013YT.UltraSkyWars.interfaces.Injection;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.cmds.PartyCMD;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.inventories.PartyMainMenu;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.inventories.parties.PartyMemberMenu;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.inventories.parties.PartyPartiesMenu;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.listeners.PartyListener;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.managers.PartyManager;
import io.github.Leonardo0013YT.UltraSkyWars.modules.parties.menus.PartyMenu;
import org.bukkit.command.CommandExecutor;

public class InjectionParty implements Injection {

    private Settings parties;
    private PartyManager pam;
    private Gson gson;
    private PartyMenu pem;

    @Override
    public void loadInjection(UltraSkyWars main) {
        this.gson = new Gson();
        parties = new Settings("modules/parties", true, false);
        pam = new PartyManager(main, this);
        pem = new PartyMenu(main, this);
        main.getUim().getMenus().put("mainparty", new PartyMainMenu(this, "mainparty"));
        main.getUim().getMenus().put("partymember", new PartyMemberMenu(this, "partymember"));
        main.getUim().getMenus().put("partylist", new PartyPartiesMenu(this, "partylist"));
        main.getServer().getPluginManager().registerEvents(new PartyListener(this), main);
        CommandExecutor ce = new PartyCMD(this);
        main.getCommand("swp").setExecutor(ce);
        if (parties.getBoolean("partyCommand")) {
            main.getCommand("party").setExecutor(ce);
        }
    }

    @Override
    public void reload() {
        pam.reload();
    }

    @Override
    public void disable() {

    }

    public PartyMenu getPem() {
        return pem;
    }

    public Gson getGson() {
        return gson;
    }

    public Settings getParties() {
        return parties;
    }

    public PartyManager getPam() {
        return pam;
    }
}