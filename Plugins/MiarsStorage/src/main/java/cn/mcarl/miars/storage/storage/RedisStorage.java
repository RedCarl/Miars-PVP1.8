package cn.mcarl.miars.storage.storage;

import cn.mcarl.miars.storage.MiarsStorage;
import cn.mcarl.miars.storage.conf.PluginConfig;
import com.google.gson.Gson;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;

import java.util.List;
import java.util.Objects;

public class RedisStorage {
    public boolean initialize(){

        try {
            MiarsStorage.getInstance().log("	尝试连接到 Redis 数据库...");
            JedisConnectionFactory.getJedis();
        } catch (Exception exception) {
            MiarsStorage.getInstance().log("无法连接到数据库，请检查配置文件。");
            exception.printStackTrace();
            return false;
        }

        return true;
    }

    public void shutdown() {
        MiarsStorage.getInstance().log("	关闭 Redis 连接池...");
        JedisConnectionFactory.closeJedis();
    }

    public void setJedis(String key, String value){
        try(Jedis jedis = JedisConnectionFactory.getJedis()){
            jedis.set(key,value);
        }

    }

    public String getJedis(String key){
        try(Jedis jedis = JedisConnectionFactory.getJedis()){
            return jedis.get(key);
        }
    }

    public Long delJedis(String key){
        try(Jedis jedis = JedisConnectionFactory.getJedis()){
            return jedis.del(key);
        }
    }



}
