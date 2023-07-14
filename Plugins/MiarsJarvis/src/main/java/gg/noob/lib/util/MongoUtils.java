package gg.noob.lib.util;

import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.client.model.DBCollectionUpdateOptions;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import gg.noob.plunder.Plunder;
import org.bukkit.scheduler.BukkitRunnable;

public class MongoUtils {
    private static List<Runnable> runnables = new ArrayList<>();

    private static ExecutorService thread = Executors.newSingleThreadExecutor((new ThreadFactoryBuilder()).setNameFormat("NoobLib MongoUtils Thread").build());

    public static void init() {
        (new BukkitRunnable() {
            boolean stop = false;
            @Override
            public void run() {
                if (this.stop) {
                    return;
                }
                this.stop = true;
                try {
                    MongoUtils.thread.execute(() -> {
                        for (Runnable runnable : MongoUtils.runnables) {
                            runnable.run();
                        }
                        MongoUtils.runnables.clear();
                    });
                } catch (Exception exception) {}
                this.stop = false;
            }
        }).runTaskTimerAsynchronously(Plunder.getInstance(), 1L, 1L);
    }

    public static void addRunnable(Runnable runnable) {
        thread.execute(() -> runnables.add(runnable));
    }

    public static void update(DBCollection collection, DBObject dbObject, BasicDBObject basicDBObject, Callback<Boolean> callback) {
        addRunnable(() -> {
            collection.update(basicDBObject, dbObject, (new DBCollectionUpdateOptions()).upsert(true));
            callback.callback(Boolean.valueOf(true));
        });
    }

    public static void update(DBCollection collection, DBObject dbObject, BasicDBObject basicDBObject) {
        addRunnable(() -> collection.update(basicDBObject, dbObject, (new DBCollectionUpdateOptions()).upsert(true)));
    }

    public static void remove(DBCollection collection, DBObject query) {
        addRunnable(() -> {
            DBCursor cursor = collection.find(query);
            DBObject storedTag = cursor.one();
            if (storedTag != null) {
                collection.remove(storedTag);
            }
        });
    }

    public static void remove(DBCollection collection, DBObject query, Callback<Boolean> callback) {
        addRunnable(() -> {
            DBCursor cursor = collection.find(query);
            DBObject storedTag = cursor.one();
            if (storedTag != null) {
                collection.remove(storedTag);
            }
            callback.callback(Boolean.valueOf(true));
        });
    }

    public static void updateNotAsync(DBCollection collection, DBObject dbObject, BasicDBObject basicDBObject) {
        collection.update(basicDBObject, dbObject, (new DBCollectionUpdateOptions()).upsert(true));
    }

    public static void removeNotAsync(DBCollection collection, DBObject query) {
        DBCursor cursor = collection.find(query);
        DBObject storedTag = cursor.one();
        if (storedTag != null) {
            collection.remove(storedTag);
        }
    }
}
