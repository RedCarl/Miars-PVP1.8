package io.github.Leonardo0013YT.UltraSkyWars.modules.mobfriends.friends;

import org.bukkit.entity.Entity;

import java.util.ArrayList;

public class FriendSession {

    private final ArrayList<Entity> entities = new ArrayList<>();

    public ArrayList<Entity> getEntities() {
        return entities;
    }

    public void clear() {
        entities.forEach(Entity::remove);
    }

}