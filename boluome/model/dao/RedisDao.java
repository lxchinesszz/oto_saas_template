package phoenix.jhbank.model.dao;

import com.google.gson.Gson;
import phoenix.jhbank.config.BoluomeConfig;
import phoenix.jhbank.config.JhbankConfig;
import phoenix.jhbank.util.DataUtils;
import phoenix.jhbank.util.ResponseBuilder;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.methods.PostMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Pipeline;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;


/**
 * @Package: honeybee.beebill.dao
 * @Description: 操作redis
 * @author: liuxin
 * @date: 17/6/3 下午12:27
 */
@Component
public class RedisDao {
    protected static Logger logger = LoggerFactory.getLogger(RedisDao.class);

    static ThreadLocal<Jedis> redisConnection = new ThreadLocal<Jedis>();
    static ThreadLocal<Pipeline> pipeConnection = new ThreadLocal<Pipeline>();
    private static final Gson gson = new Gson();

    @Autowired
    JedisPool jedisPool;
    @Autowired
    Environment evn;
    @Autowired
    BoluomeConfig boluomeConfig;


    /**
     * 根据订单号得到redis中的订单信息
     *
     * @param orderIdNum 订单号流水
     *                   Redis中目前使用流水后面加两个00
     * @return
     */
    public Map<String, String> getRedisOrderPay(String appCode, String orderIdNum) {
        String redisKey = String.format("p:%s:%s", appCode, orderIdNum);
        logger.info("redis订单查询key:{}", redisKey);
        Map<String, String> map = getJedis().hgetAll(redisKey);
        return map;
    }

    /**
     * 根据订单号得到redis中的退款订单信息
     *
     * @param orderIdNum 订单号流水
     *                   Redis中目前使用流水后面加两个00
     * @return
     */
    public Map<String, String> getRedisOrderRefund(String appCode, String orderIdNum) {
        String redisKey = String.format("r:%s:%s", appCode, orderIdNum);
        logger.debug("redis订单查询key:{}", redisKey);
        Map<String, String> map = getJedis().hgetAll(redisKey);
        return map;
    }

    /**
     * 修改订单支付价格和状态
     *
     * @param orderIdNum  订单号
     * @param payPrice    支付金额
     * @param orderStatus 订单状态
     */
    public void upOrderPayPiceAndSta(String appCode, String orderIdNum, Integer payPrice, int orderStatus, String timestamp) {
        Jedis jedis = getJedis();
        String redisKey = String.format("p:%s:%s", appCode, orderIdNum);
        Map<String, String> map = jedis.hgetAll(redisKey);
        //更新支付渠道
        map.put("payPrice", String.valueOf(payPrice));
        map.put("status", String.valueOf(orderStatus));
        map.put("channel", appCode);
        map.put("paidAt", timestamp);
        String code = jedis.hmset(redisKey, map);
        logger.info("操作状态:{}", code);
    }

    /**
     * 同步获取Jedis实例
     * 涉及流水存放在payMent 3库
     *
     * @return Jedis
     */

    public Jedis getJedis() {
        if (redisConnection.get() != null) {
            return redisConnection.get();
        }
        Jedis jedis = jedisPool.getResource();
        int select = Integer.parseInt(evn.getProperty("redis.info.select"));
        jedis.select(select);
        redisConnection.set(jedis);
        return jedis;
    }


    /**
     * 同步获取Jedis实例
     * 涉及流水存放在payMent 3库
     *
     * @return Jedis
     */

    public Pipeline getJedisPip() {
        if (pipeConnection.get() != null) {
            return pipeConnection.get();
        }
        Jedis jedis = jedisPool.getResource();
        Pipeline pipeline = jedis.pipelined();
        int select = Integer.parseInt(evn.getProperty("redis.info.select"));
        jedis.select(select);
        pipeConnection.set(pipeline);
        return pipeline;
    }


    public void clear(Jedis jedis) {
        jedisPool.returnBrokenResource(jedis);
    }


    /**
     * 校验支付成功，分发下去
     *
     * @param orderIdNum 订单号
     */
    @SuppressWarnings("deprecation")
    public boolean forwardPost(String orderIdNum) {
        boolean flag = false;
        Map<String, String> map = getRedisOrderPay("jhbank", orderIdNum);
        String orderID = map.get("id");
        String orderType = map.get("orderType");
        String channel = map.get("channel");
        String fenfaurl = boluomeConfig.getPayorder();
        HttpClient client = new HttpClient();
        PostMethod postMethod = new PostMethod(fenfaurl);
        postMethod.setRequestHeader("Content-Type", "application/json");
        if (StringUtils.isEmpty(orderID) || StringUtils.isEmpty(orderType) || StringUtils.isEmpty(channel)) {
            logger.warn("订单号:{}，分发参数有空值【id:{}，ordertype:{}，channel：{}】", orderID, orderID, orderType, channel);
        } else {
            Map<String, String> json = new HashMap<String, String>();
            json.put("id", orderID);
            json.put("orderType", orderType);
            json.put("payChannel", channel);
            json.put("serialNum", orderIdNum);
            postMethod.setRequestBody(gson.toJson(json));
            try {
                client.executeMethod(postMethod);
                ResponseBuilder.ResponseVo responseVo = gson.fromJson(postMethod.getResponseBodyAsString(), ResponseBuilder.ResponseVo.class);
                int falg = responseVo.getCode();
                logger.info("分发服务器响应code:" + falg);
            } catch (IOException io) {
                logger.error("订单号:{}，分发参数【id:{},ordertype:{},channel:{}】,分发异常:", io.getMessage(), orderID, orderID, orderType, channel);
            }
            logger.info("订单号:{},分发参数【id:{},ordertype:{},channel:{}】，分发发送成功时间:{}", orderID, orderID, orderType, channel, DataUtils.getDateFormat("MM/dd HH:mm:ss"));
            flag = true;
        }
        return flag;
    }

}
