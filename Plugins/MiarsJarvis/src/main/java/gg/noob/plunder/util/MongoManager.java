package gg.noob.plunder.util;

import com.mongodb.BasicDBObject;
import com.mongodb.BasicDBObjectBuilder;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import gg.noob.lib.util.MongoUtils;
import gg.noob.plunder.Plunder;
import gg.noob.plunder.logs.Log;
import gg.noob.plunder.manager.Manager;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import org.bukkit.ChatColor;

public class MongoManager {
    private MongoClient mongoClient;
    private DB database;
    private DBCollection logsCollection;
    private DBCollection managersCollection;

    public MongoManager() {
        if (!Plunder.getBukkitConfig().isMongoAuth()) {
            String link = "mongodb://" + Plunder.getBukkitConfig().getMongoHost() + ":" + Plunder.getBukkitConfig().getMongoPort();
            this.mongoClient = new MongoClient(new MongoClientURI(link));
        } else {
            ArrayList<ServerAddress> seeds = new ArrayList<>();
            seeds.add(new ServerAddress(Plunder.getBukkitConfig().getMongoHost()));
            ArrayList<MongoCredential> credentials = new ArrayList<>();
            credentials.add(MongoCredential.createScramSha1Credential(Plunder.getBukkitConfig().getMongoUsername(), "admin", Plunder.getBukkitConfig().getMongoPassword().toCharArray()));
            this.mongoClient = new MongoClient(seeds, credentials);
        }
        try {
            this.database = this.mongoClient.getDB(Plunder.getBukkitConfig().getMongoDatabase());
            this.logsCollection = this.database.getCollection("logs");
            this.managersCollection = this.database.getCollection("managers");
        }
        catch (Exception ex) {
            Plunder.getInstance().getLogger().warning(ChatColor.translateAlternateColorCodes('&', "&cFailed to initialize backend."));
            Plunder.getInstance().getLogger().warning(ex.getClass().getSimpleName() + " - " + ex.getMessage());
        }
    }

    public Set<Log> getLogs(UUID uuid) {
        HashSet<Log> logs = new HashSet<>();
        BasicDBObject basicDBObject = new BasicDBObject("uuid", uuid);
        DBCursor cursor = this.logsCollection.find(basicDBObject);
        cursor.forEach(dbObject -> {
            Log log = new Log(UUID.fromString(dbObject.get("uuid").toString()), dbObject.get("lastUserName").toString(), dbObject.get("check").toString(), dbObject.get("moreMessage").toString(), (Integer)dbObject.get("ping"), (Integer)dbObject.get("violations"), (Boolean)dbObject.get("banned"));
            log.setTime((Long)dbObject.get("time"));
            logs.add(log);
        });
        return logs;
    }

    public Set<Log> getLogs(String name) {
        HashSet<Log> logs = new HashSet<>();
        BasicDBObject basicDBObject = new BasicDBObject("caseLastUserName", name.toUpperCase());
        DBCursor cursor = this.logsCollection.find(basicDBObject);
        cursor.forEach(dbObject -> {
            Log log = new Log(UUID.fromString(dbObject.get("uuid").toString()), dbObject.get("lastUserName").toString(), dbObject.get("check").toString(), dbObject.get("moreMessage") == null ? null : dbObject.get("moreMessage").toString(), (Integer)dbObject.get("ping"), (Integer)dbObject.get("violations"), (Boolean)dbObject.get("banned"));
            log.setTime((Long)dbObject.get("time"));
            logs.add(log);
        });
        return logs;
    }

    public void removeLog(Log log) {
        BasicDBObject basicDBObject = new BasicDBObject("uuid", log.getUuid());
        MongoUtils.removeNotAsync(this.logsCollection, basicDBObject);
    }

    public void saveLog(Log log) {
        DBObject violationsObject = new BasicDBObjectBuilder().add("uuid", log.getUuid()).add("lastUserName", log.getLastUserName()).add("caseLastUserName", log.getCaseLastUserName()).add("check", log.getCheck()).add("moreMessage", log.getMoreMessage()).add("ping", log.getPing()).add("violations", log.getViolations()).add("time", log.getTime()).add("banned: ", log.isBanned()).get();
        MongoUtils.update(this.logsCollection, violationsObject, new BasicDBObject("uuid", log.getUuid().toString()));
    }

    public void saveLogToAddProcess(Log log) {
        DBObject violationsObject = new BasicDBObjectBuilder().add("uuid", log.getUuid()).add("lastUserName", log.getLastUserName()).add("caseLastUserName", log.getCaseLastUserName()).add("check", log.getCheck()).add("moreMessage", log.getMoreMessage()).add("ping", log.getPing()).add("violations", log.getViolations()).add("time", log.getTime()).add("banned", log.isBanned()).get();
        Plunder.getInstance().addProcessMongoRunnable(() -> MongoUtils.updateNotAsync(this.logsCollection, violationsObject, new BasicDBObject("uuid", log.getUuid().toString())));
    }

    public Manager getManager(UUID uuid) {
        BasicDBObject basicDBObject = new BasicDBObject("uuid", uuid);
        DBCursor cursor = this.managersCollection.find(basicDBObject);
        DBObject dbObject = cursor.one();
        if (dbObject == null) {
            return null;
        }
        Manager manager = new Manager(UUID.fromString(dbObject.get("uuid").toString()), dbObject.get("lastUserName").toString());
        manager.setAlerts(Boolean.parseBoolean(dbObject.get("isAlerts").toString()));
        manager.setVerbose(Boolean.parseBoolean(dbObject.get("isVerbose").toString()));
        if (dbObject.get("isDebug") != null) {
            manager.setDebug(Boolean.parseBoolean(dbObject.get("isDebug").toString()));
        }
        if (dbObject.get("firstJoin") != null) {
            manager.setFirstJoin(Long.parseLong(dbObject.get("firstJoin").toString()));
        }
        if (dbObject.get("trust") != null) {
            manager.setTrust(Integer.parseInt(dbObject.get("trust").toString()));
        }
        return manager;
    }

    public Manager getManager(String name) {
        BasicDBObject basicDBObject = new BasicDBObject("caseLastUserName", name.toUpperCase());
        DBCursor cursor = this.managersCollection.find(basicDBObject);
        DBObject dbObject = cursor.one();
        if (dbObject == null) {
            return null;
        }
        Manager manager = new Manager(UUID.fromString(dbObject.get("uuid").toString()), dbObject.get("lastUserName").toString());
        manager.setAlerts(Boolean.parseBoolean(dbObject.get("isAlerts").toString()));
        manager.setVerbose(Boolean.parseBoolean(dbObject.get("isVerbose").toString()));
        if (dbObject.get("isDebug") != null) {
            manager.setDebug(Boolean.parseBoolean(dbObject.get("isDebug").toString()));
        }
        if (dbObject.get("firstJoin") != null) {
            manager.setFirstJoin(Long.parseLong(dbObject.get("firstJoin").toString()));
        }
        if (dbObject.get("trust") != null) {
            manager.setTrust(Integer.parseInt(dbObject.get("trust").toString()));
        }
        return manager;
    }

    public void saveManager(Manager manager) {
        DBObject managerObject = new BasicDBObjectBuilder().add("uuid", manager.getUuid()).add("lastUserName", manager.getLastUserName()).add("caseLastUserName", manager.getCaseLastUserName()).add("isAlerts", manager.isAlerts()).add("isVerbose", manager.isVerbose()).add("isDebug", manager.isDebug()).add("firstJoin", manager.getFirstJoin()).add("trust", manager.getTrust()).get();
        MongoUtils.update(this.managersCollection, managerObject, new BasicDBObject("uuid", manager.getUuid()));
    }

    public MongoClient getMongoClient() {
        return this.mongoClient;
    }

    public DB getDatabase() {
        return this.database;
    }

    public DBCollection getLogsCollection() {
        return this.logsCollection;
    }

    public DBCollection getManagersCollection() {
        return this.managersCollection;
    }
}

