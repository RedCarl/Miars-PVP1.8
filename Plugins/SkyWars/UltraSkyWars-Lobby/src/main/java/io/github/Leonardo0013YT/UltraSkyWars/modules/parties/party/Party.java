package io.github.Leonardo0013YT.UltraSkyWars.modules.parties.party;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.UUID;

public class Party {

    private final int maxCapacity;
    private final HashMap<UUID, String> members;
    private final HashSet<UUID> chatMembers = new HashSet<>();
    private UUID leader;
    private String leaderName;
    private boolean privacy;

    public Party(UUID leader, String leaderName, boolean privacy, int maxCapacity, HashMap<UUID, String> members) {
        this.leader = leader;
        this.leaderName = leaderName;
        this.privacy = privacy;
        this.maxCapacity = maxCapacity;
        this.members = members;
        this.members.put(leader, leaderName);
    }

    public HashSet<UUID> getChatMembers() {
        return chatMembers;
    }

    public int getMaxCapacity() {
        return maxCapacity;
    }

    public boolean isPrivacy() {
        return privacy;
    }

    public void setPrivacy(boolean privacy) {
        this.privacy = privacy;
    }

    public String getLeaderName() {
        return leaderName;
    }

    public void setLeaderName(String leaderName) {
        this.leaderName = leaderName;
    }

    public UUID getLeader() {
        return leader;
    }

    public void setLeader(UUID leader) {
        this.leader = leader;
    }

    public HashMap<UUID, String> getMembers() {
        return members;
    }

    public UUID getUUIDByName(String name) {
        if (members.containsValue(name)) {
            Map.Entry<UUID, String> entry = members.entrySet().stream().filter(e -> e.getValue().equals(name)).findFirst().orElse(null);
            if (entry != null) {
                return entry.getKey();
            }
        }
        return null;
    }

}