package cn.mcarl.miars.storage.storage;

import cn.mcarl.miars.storage.conf.PluginConfig;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
 
public class JedisConnectionFactory {
    private static final JedisPool jedisPool;
 
    static {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(8);
        poolConfig.setMaxIdle(8);
        poolConfig.setMinIdle(0);
        poolConfig.setMaxWaitMillis(1000);
 
//        创建连接池对象
         jedisPool = new JedisPool(poolConfig, PluginConfig.REDIS.URL.get(),PluginConfig.REDIS.PORT.get(),1000,PluginConfig.REDIS.PASSWORD.get());
    }
//    调用getJedis可以拿到jedis对象
    public static Jedis getJedis(){
        return jedisPool.getResource();
    }
    public static void closeJedis(){
        jedisPool.close();
    }
}