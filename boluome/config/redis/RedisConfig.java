package phoenix.jhbank.config.redis;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * Created by liuxin on 16/12/23.
 */
@Configuration
@EnableAutoConfiguration
@ConfigurationProperties(prefix ="redis.info")
public class RedisConfig {

   private String ip;

   private Integer port;

   private Integer maxIdle;

   private Integer Maxtotal;

   private Integer idleTimeMillis;

   private Integer minIdle;

   private Integer maxWailMills;

   private boolean onBorrow;

   private boolean onReturn;

    @Bean
    public JedisPool jedis() {
        JedisPoolConfig config = new JedisPoolConfig();
        //最大空闲连接数, 默认8个
        config.setMaxIdle(maxIdle);
        //最大连接数, 默认8个
        config.setMaxTotal(Maxtotal);
        //逐出连接的最小空闲时间 默认1800000毫秒(30分钟)
        config.setMinEvictableIdleTimeMillis(idleTimeMillis);
        //最小空闲连接数, 默认0
        config.setMinIdle(minIdle);
        config.setTestOnBorrow(onBorrow);
        config.setTestOnReturn(onReturn);
        config.setMaxWaitMillis(maxWailMills);
        //空闲30分钟后逐出
        config.setSoftMinEvictableIdleTimeMillis(1800000);
        JedisPool jedisPool=new JedisPool(config,ip,port);
        return jedisPool;
    }


    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public Integer getPort() {
        return port;
    }

    public void setPort(Integer port) {
        this.port = port;
    }

    public Integer getMaxIdle() {
        return maxIdle;
    }

    public void setMaxIdle(Integer maxIdle) {
        this.maxIdle = maxIdle;
    }

    public Integer getMaxtotal() {
        return Maxtotal;
    }

    public void setMaxtotal(Integer maxtotal) {
        Maxtotal = maxtotal;
    }

    public Integer getIdleTimeMillis() {
        return idleTimeMillis;
    }

    public void setIdleTimeMillis(Integer idleTimeMillis) {
        this.idleTimeMillis = idleTimeMillis;
    }

    public Integer getMinIdle() {
        return minIdle;
    }

    public void setMinIdle(Integer minIdle) {
        this.minIdle = minIdle;
    }

    public Integer getMaxWailMills() {
        return maxWailMills;
    }

    public void setMaxWailMills(Integer maxWailMills) {
        this.maxWailMills = maxWailMills;
    }

    public boolean isOnBorrow() {
        return onBorrow;
    }

    public void setOnBorrow(boolean onBorrow) {
        this.onBorrow = onBorrow;
    }

    public boolean isOnReturn() {
        return onReturn;
    }

    public void setOnReturn(boolean onReturn) {
        this.onReturn = onReturn;
    }
}
