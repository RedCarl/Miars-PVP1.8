
package cn.mcarl.miars.pay.utils;

public class PlayerRankData implements Comparable<PlayerRankData>
{
    private String player;
    private double amount;
    
    public PlayerRankData(final String player, final double amount) {
        this.player = player;
        this.amount = amount;
    }
    
    public String getPlayer() {
        return this.player;
    }
    
    public void setPlayer(final String player) {
        this.player = player;
    }
    
    public double getAmount() {
        return this.amount;
    }
    
    public void setAmount(final double amount) {
        this.amount = amount;
    }
    
    @Override
    public int compareTo(final PlayerRankData o) {
        return Double.compare(o.amount, this.amount);
    }
    
    @Override
    public String toString() {
        return "PlayerRankData@[Player=" + this.player + ", Amount=" + this.amount + "]";
    }
}
