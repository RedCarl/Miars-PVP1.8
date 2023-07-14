/*
 * BedWars1058 - A bed wars mini-game.
 * Copyright (C) 2021 Andrei Dascălu
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 *
 * Contact e-mail: andrew.dascalu@gmail.com
 */

package com.andrei1058.bedwars.arena.team;

import com.andrei1058.bedwars.api.arena.IArena;
import com.andrei1058.bedwars.api.arena.team.ITeam;
import com.andrei1058.bedwars.api.arena.team.ITeamAssigner;
import com.andrei1058.bedwars.api.events.gameplay.TeamAssignEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.*;

public class TeamAssigner implements ITeamAssigner {

    private final LinkedList<Player> skip = new LinkedList<>();

    @Override
    public void assignTeams(IArena arena) {

        List<Player> ps = new ArrayList<>(arena.getPlayers());
        Iterator<Player> players = ps.listIterator();

        // 队伍分配
        while (players.hasNext()){
            for (ITeam team : arena.getTeams()) {
                if (team.getMembers().size() < arena.getMaxInTeam()) {
                    if (players.hasNext()){
                        Player remaining = players.next();
                        TeamAssignEvent e = new TeamAssignEvent(remaining, team, arena);
                        Bukkit.getPluginManager().callEvent(e);
                        if (!e.isCancelled()) {
                            remaining.closeInventory();
                            team.addPlayers(remaining);
                        }
                    }
                }
            }
        }

    }
}
