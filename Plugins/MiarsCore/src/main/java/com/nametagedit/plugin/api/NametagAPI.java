package com.nametagedit.plugin.api;

import com.nametagedit.plugin.NametagManager;
import com.nametagedit.plugin.api.data.FakeTeam;
import com.nametagedit.plugin.api.data.GroupData;
import com.nametagedit.plugin.api.data.Nametag;
import lombok.AllArgsConstructor;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * Implements the INametagAPI interface. There only
 * exists one instance of this class.
 */
@AllArgsConstructor
public final class NametagAPI implements INametagApi {


    private final NametagManager manager;

    @Override
    public FakeTeam getFakeTeam(Player player) {
        return manager.getFakeTeam(player.getName());
    }

    @Override
    public Nametag getNametag(Player player) {
        FakeTeam team = manager.getFakeTeam(player.getName());
        boolean nullTeam = team == null;
        return new Nametag(nullTeam ? "" : team.getPrefix(), nullTeam ? "" : team.getSuffix());
    }

    @Override
    public void clearNametag(Player player) {
        manager.reset(player.getName());
    }

    @Override
    public void reloadNametag(Player player) {
    }


    @Override
    public void clearNametag(String player) {
        manager.reset(player);
    }

    @Override
    public void setPrefix(Player player, String prefix) {
        FakeTeam fakeTeam = manager.getFakeTeam(player.getName());
        manager.setNametag(player.getName(), prefix, fakeTeam == null ? null : fakeTeam.getSuffix());
    }

    @Override
    public void setSuffix(Player player, String suffix) {
        FakeTeam fakeTeam = manager.getFakeTeam(player.getName());
        manager.setNametag(player.getName(), fakeTeam == null ? null : fakeTeam.getPrefix(), suffix);
    }

    @Override
    public void setPrefix(String player, String prefix) {
        FakeTeam fakeTeam = manager.getFakeTeam(player);
        manager.setNametag(player, prefix, fakeTeam == null ? null : fakeTeam.getSuffix());
    }

    @Override
    public void setSuffix(String player, String suffix) {
        FakeTeam fakeTeam = manager.getFakeTeam(player);
        manager.setNametag(player, fakeTeam == null ? null : fakeTeam.getPrefix(), suffix);
    }

    @Override
    public void setNametag(Player player, String prefix, String suffix) {
        manager.setNametag(player.getName(), prefix, suffix);
    }

    @Override
    public void setNametag(Player player, String prefix, String suffix,int sortPriority) {
        manager.setNametag(player.getName(), prefix, suffix, sortPriority);
    }

    @Override
    public void setNametag(String player, String prefix, String suffix) {
        manager.setNametag(player, prefix, suffix);
    }

    @Override
    public void setNametag(String player, String prefix, String suffix,int sortPriority) {
        manager.setNametag(player, prefix, suffix, sortPriority);
    }

    @Override
    public void hideNametag(Player player) {
        FakeTeam fakeTeam = manager.getFakeTeam(player.getName());
        manager.setNametag(player.getName(), fakeTeam == null ? null : fakeTeam.getPrefix(), fakeTeam == null ? null : fakeTeam.getSuffix(), false);
    }

    @Override
    public void hideNametag(String player) {
        FakeTeam fakeTeam = manager.getFakeTeam(player);
        manager.setNametag(player, fakeTeam == null ? null : fakeTeam.getPrefix(), fakeTeam == null ? null : fakeTeam.getSuffix(), false);
    }

    @Override
    public void showNametag(Player player) {
        FakeTeam fakeTeam = manager.getFakeTeam(player.getName());
        manager.setNametag(player.getName(), fakeTeam == null ? null : fakeTeam.getPrefix(), fakeTeam == null ? null : fakeTeam.getSuffix(), true);
    }

    @Override
    public void showNametag(String player) {
        FakeTeam fakeTeam = manager.getFakeTeam(player);
        manager.setNametag(player, fakeTeam == null ? null : fakeTeam.getPrefix(), fakeTeam == null ? null : fakeTeam.getSuffix(), true);
    }

    @Override
    public List<GroupData> getGroupData() {
        return null;
    }

    @Override
    public void saveGroupData(GroupData... groupData) {

    }

    @Override
    public void applyTags() {

    }

    @Override
    public void applyTagToPlayer(Player player, boolean loggedIn) {

    }

    @Override
    public void updatePlayerPrefix(String target, String prefix) {

    }

    @Override
    public void updatePlayerSuffix(String target, String suffix) {

    }

    @Override
    public void updatePlayerNametag(String target, String prefix, String suffix) {

    }

}