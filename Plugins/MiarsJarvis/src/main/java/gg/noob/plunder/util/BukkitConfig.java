// 
// Decompiled by Procyon v0.5.36
// 

package gg.noob.plunder.util;

import org.bukkit.configuration.file.FileConfiguration;
import gg.noob.plunder.Plunder;

public class BukkitConfig
{
    private boolean mongoAuth;
    private int mongoPort;
    private String mongoHost;
    private String mongoUsername;
    private String mongoPassword;
    private String mongoDatabase;
    
    public BukkitConfig() {
        final FileConfiguration configuration = Plunder.getInstance().getConfig();
        this.mongoHost = configuration.getString("mongodb.host");
        this.mongoPort = configuration.getInt("mongodb.port");
        this.mongoAuth = configuration.getBoolean("mongodb.auth.enabled");
        this.mongoUsername = configuration.getString("mongodb.auth.username");
        this.mongoPassword = configuration.getString("mongodb.auth.password");
        this.mongoDatabase = configuration.getString("mongodb.database");
    }
    
    public boolean isMongoAuth() {
        return this.mongoAuth;
    }
    
    public int getMongoPort() {
        return this.mongoPort;
    }
    
    public String getMongoHost() {
        return this.mongoHost;
    }
    
    public String getMongoUsername() {
        return this.mongoUsername;
    }
    
    public String getMongoPassword() {
        return this.mongoPassword;
    }
    
    public String getMongoDatabase() {
        return this.mongoDatabase;
    }
}
